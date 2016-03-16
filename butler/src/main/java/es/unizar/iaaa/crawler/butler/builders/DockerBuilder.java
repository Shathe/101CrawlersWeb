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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
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

			FileWriter fichero = null;
			PrintWriter pw = null;
			try {
				File theDir = new File(directoryName);

				/*
				 * Crear el fichero run.sh que servirá para empezar a ejecutar
				 * el crawler
				 */
				fichero = new FileWriter(theDir + "/run.sh");
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
				}
				pw.close();
				scan.close();
				/*
				 * Crear el fichero junstarSalidas.sh que servirá para empezar a
				 * ejecutar el crawler
				 */
				fichero = new FileWriter(theDir + "/juntarSalidas.sh");
				pw = new PrintWriter(fichero);
				Path juntarSalidas = Paths.get(new File(resources + "juntarSalidas").toURI());
				scan = new Scanner(juntarSalidas);
				while (scan.hasNextLine()) {
					String linea = scan.nextLine();
					pw.write(linea + "\n");
				}
				scan.close();
				pw.close();

				/* Crear el fichero docker */

				fichero = new FileWriter(theDir + "/Dockerfile");
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
				}
				scan.close();
				crawlerbuilder.anadirDockerfile(pw);
				
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
