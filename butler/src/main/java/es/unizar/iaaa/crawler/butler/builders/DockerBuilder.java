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
	private final String directoryName;
	private Configuration configuracion;
	private CrawlerBuilder crawlerbuilder;

	public DockerBuilder(Configuration config, String resources, String directoryName, CrawlerBuilder crawlerbuilder) {
		configuracion = config;
		this.resources = resources;
		this.directoryName = directoryName;
		this.crawlerbuilder=crawlerbuilder;
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
				crawlerbuilder.anadirDockerfile(pw);
				
				pw.close();

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
			}
			scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
