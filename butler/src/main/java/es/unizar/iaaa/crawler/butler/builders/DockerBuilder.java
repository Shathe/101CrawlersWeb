/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class DockerBuilder {

    private final String resources;
    private Configuration configuracion;

    public DockerBuilder(Configuration config, String resources) {
        configuracion = config;
        this.resources = resources;
    }

    public Configuration getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuration configuracion) {
        this.configuracion = configuracion;
    }

    public void crearDockerfile() {
        if (configuracion.isOk()) {
                /*
                 * Crear el fichero run.sh que servirá para empezar a ejecutar
				 * el crawler 
				 */
            createRunSh();
                /*
                 * Crear el fichero junstarSalidas.sh que servirá para empezar a
				 * ejecutar el crawler
				 */
            createJuntarSalidasSh();

				/* Crear el fichero docker */

            createDockerfile();
        }
    }

    private void createDockerfile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("Dockerfile"))) {
				/* Añadir dockerOS */
            pw.println("From " + configuracion.getDockerOS().getName() + ":"
                    + configuracion.getDockerOS().getVersion());
            Path dockerbase = Paths.get(new File(resources + "DockerBase").toURI());
				/* Añadir contenido fijo */
            Scanner scan = new Scanner(dockerbase);
            while (scan.hasNextLine()) {
                String linea = scan.nextLine();
                pw.write(linea + "\n");
                System.out.println(linea);
            }
            scan.close();
				/* Añade las seeds */
            for (int i = 0; i < configuracion.getCrawlSystem().getSeeds().size(); i++) {
                pw.write("RUN echo " + configuracion.getCrawlSystem().getSeeds().get(i)
                        + " >> crawler/urls/seeds.txt" + "\n");
            }
				/* Añade los ficheros a docker creados */
            Path juntarSalidasURI = Paths.get(new File(resources + "juntarSalidas.sh").toURI());
            Path runURI = Paths.get(new File(resources + "run.sh").toURI());

            pw.write("ADD " + resources + juntarSalidasURI + " crawler/juntarSalidas.sh" + "\n");
            pw.write("ADD " + resources + runURI + " crawler/run.sh" + "\n");

				/*
				 * Pasar el fichero nutch-site a su carpeta correspondiente y
				 * después por cada plugin, crear su carpeta con su nombre,
				 * pasar allí sus jar y su plugin.xml con ese nombre
				 */
            Path nutchSite = Paths.get(new File(resources + "nutch-site.xml").toURI());
            pw.write("ADD " + nutchSite + " crawler/conf/nutch-site.xml" + "\n\n");
				/* Para cada plugin */
            for (int i = 0; configuracion.getCrawlSystem().getPlugins() != null && !configuracion.getCrawlSystem().getPlugins().isEmpty()
                    && i < configuracion.getCrawlSystem().getPlugins().size(); i++) {
            /* Estructura: nombre file.xml (file.jar)+ */
                String[] all = configuracion.getCrawlSystem().getPlugins().get(i).split(" ");
                String plugin = all[0];
            /* Creas la carpeta para cada plugin */
                String nombrePlugin = plugin.split("/")[plugin.split("/").length - 1];
                pw.write("Run mkdir crawler/plugins/" + nombrePlugin + "\n");
            /* Se añade su fichero de configuracion */
                Path xmlURI = Paths.get(new File(all[1]).toURI());

                pw.write("ADD " + xmlURI + " crawler/plugins/" + nombrePlugin + "/plugin.xml" + "\n");

                for (int j = 2; j < all.length; j++) {
                    // jars
                    Path jarURI = Paths.get(new File(all[j]).toURI());
                    String jarName = all[j].split("/")[all[j].split("/").length - 1];

                    pw.write("ADD " + jarURI + " crawler/plugins/" + nombrePlugin + "/" + jarName + "\n");

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createRunSh() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("run.sh"))) {
				/* Numero de rondas */
            pw.write("#Number of rounds the crawler will run\n");
            pw.write("rounds=" + configuracion.getCrawlSystem().getRounds() + "\n");
            pw.write("#readseg options \n");
				/* Qué información se recuperará */
            if (configuracion.getCrawlSystem().getInfoCrawled().toLowerCase().equals("html")) {
                pw.write("dumpOptions=\"-nogenerate -nofetch -noparsetext -noparse -noparsedata\"\n");
            } else {
                pw.write("dumpOptions=\"-nogenerate -nofetch -nocontent -noparse -noparsedata\"\n");
            }

            Path runNutch = Paths.get(new File(resources + "runNutch").toURI());
            Scanner scan = new Scanner(runNutch);
            while (scan.hasNextLine()) {
                String linea = scan.nextLine();
                pw.write(linea + "\n");
                System.out.println(linea);
            }
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createJuntarSalidasSh() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("juntarSalidas.sh"))) {
            Path juntarSalidas = Paths.get(new File(resources + "juntarSalidas").toURI());
            Scanner scan = new Scanner(juntarSalidas);
            while (scan.hasNextLine()) {
                String linea = scan.nextLine();
                pw.write(linea + "\n");
                System.out.println(linea);
            }
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
