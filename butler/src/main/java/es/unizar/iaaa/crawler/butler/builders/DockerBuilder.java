/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.builders.validator.ConfigurationValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.LatestValidationResult;
import es.unizar.iaaa.crawler.butler.builders.validator.ValidationResult;
import es.unizar.iaaa.crawler.butler.builders.validator.Validator;
import es.unizar.iaaa.crawler.butler.builders.validator.Validator.ErroresValidar;
import es.unizar.iaaa.crawler.butler.yalm.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

			FileWriter fichero = null;
			PrintWriter pw = null;
			try {

				/*
				 * Crear el fichero run.sh que servirá para empezar a ejecutar
				 * el crawler 
				 */
				fichero = new FileWriter("run.sh");
				pw = new PrintWriter(fichero);
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
				pw.close();
				scan.close();
				/*
				 * Crear el fichero junstarSalidas.sh que servirá para empezar a
				 * ejecutar el crawler
				 */
				fichero = new FileWriter( "juntarSalidas.sh");
				pw = new PrintWriter(fichero);
				Path juntarSalidas = Paths.get(new File(resources + "juntarSalidas").toURI());
				scan = new Scanner(juntarSalidas);
				while (scan.hasNextLine()) {
					String linea = scan.nextLine();
					pw.write(linea + "\n");
					System.out.println(linea);
				}
				scan.close();
				pw.close();

				/* Crear el fichero docker */

				fichero = new FileWriter("Dockerfile");
				pw = new PrintWriter(fichero);
				/* Añadir dockerOS */
				pw.println("From " + configuracion.getDockerOS().getName() + ":"
						+ configuracion.getDockerOS().getVersion());
				Path dockerbase = Paths.get(new File(resources + "DockerBase").toURI());
				/* Añadir contenido fijo */
				scan = new Scanner(dockerbase);
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

				pw.write("ADD " + resources + juntarSalidasURI + " crawler/juntarSalidas.sh"+ "\n");
				pw.write("ADD " + resources + runURI + " crawler/run.sh"+ "\n");

				/*
				 * Pasar el fichero nutch-site a su carpeta correspondiente y
				 * después por cada plugin, crear su carpeta con su nombre,
				 * pasar allí sus jar y su plugin.xml con ese nombre
				 */
				Path nutchSite = Paths.get(new File(resources + "nutch-site.xml").toURI());
				pw.write("ADD " + nutchSite + " crawler/conf/nutch-site.xml"+ "\n\n");
				/* Para cada plugin */
				for (int i = 0;  configuracion.getCrawlSystem().getPlugins() != null && !configuracion.getCrawlSystem().getPlugins().isEmpty()
						&& i < configuracion.getCrawlSystem().getPlugins().size(); i++) {
					/* Estructura: nombre file.xml (file.jar)+ */
					String[] all = configuracion.getCrawlSystem().getPlugins().get(i).split(" ");
					String plugin = all[0];
					/* Creas la carpeta para cada plugin */
					String nombrePlugin=plugin.split("/")[plugin.split("/").length - 1];
					pw.write("Run mkdir crawler/plugins/" + nombrePlugin+ "\n");
					/* Se añade su fichero de configuracion */
					Path xmlURI = Paths.get(new File(all[1]).toURI());

					pw.write("ADD " + xmlURI + " crawler/plugins/" + nombrePlugin + "/plugin.xml"+ "\n");

					for (int j = 2; j < all.length; j++) {
						// jars
						Path jarURI = Paths.get(new File(all[j]).toURI());
						String jarName=all[j].split("/")[all[j].split("/").length - 1];

						pw.write("ADD " + jarURI + " crawler/plugins/" + nombrePlugin + "/" + jarName + "\n");

					}
					
				}
				pw.close();


			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fichero)
						fichero.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

}
