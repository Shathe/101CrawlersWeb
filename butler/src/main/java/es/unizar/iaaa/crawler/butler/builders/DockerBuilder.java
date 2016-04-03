/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Builds the dockerbuilder creates the dockerfile and the files needed for the
 * system
 */
@Component
public class DockerBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(DockerBuilder.class);

	@Autowired
	private NutchBuilder crawlerBuilder;

	public void crearDockerfile(CrawlConfiguration config, Resource resources, String directoryName)
			throws IOException {
		// Creates the run.sh file which will be used for executing the crawl
		createRunSh(config, resources, directoryName);
		// Creates the juntarSalidas.sh file which will be used for executing
		// the crawl
		createJuntarSalidasSh(resources, directoryName);
		// Create dockerfile
		createDockerfile(config, resources, directoryName);
	}

	private void createDockerfile(CrawlConfiguration configuracion, Resource resources, String directoryName)
			throws IOException {
		Resource dockerBase = resources.createRelative("DockerBase");
		try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "Dockerfile")));
				Scanner scan = new Scanner(dockerBase.getInputStream())) {
			// Add dockerOS
			pw.println(
					"From " + configuracion.getDockerOS().getName() + ":" + configuracion.getDockerOS().getVersion());
			// Add static content
			while (scan.hasNextLine()) {
				String linea = scan.nextLine();
				pw.println(linea);
			}
			crawlerBuilder.addDockerfile(configuracion, directoryName, pw);
		}
	}

	private void createRunSh(CrawlConfiguration configuracion, Resource resources, String directoryName)
			throws IOException {
		Resource runNutch = resources.createRelative("runNutch");
		try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "run.sh")));
				Scanner scan = new Scanner(runNutch.getInputStream())) {
			// rounds number
			pw.println("#!/usr/bin/env bash");
			pw.println("#Number of rounds the crawler will run");
			pw.println("rounds=" + configuracion.getCrawlSystem().getRounds());
			pw.println("#readseg options");
			// What information will be crawled
			if (configuracion.getCrawlSystem().getInfoCrawled().toLowerCase().equals("html")) {
				pw.println("dumpOptions=\"-nogenerate -nofetch -noparsetext -noparse -noparsedata\"");
			} else {
				pw.println("dumpOptions=\"-nogenerate -nofetch -nocontent -noparse -noparsedata\"");
			}
			while (scan.hasNextLine()) {
				String linea = scan.nextLine();
				if (linea.contains("i=$(($i+1))") && configuracion.getCrawlSystem().getExtraction().contains("round")) {
					// Indexing in every round
					pw.println("sh juntarSalidas.sh");
					pw.println("java -jar $HOME/101CrawlersWeb/butler.jar index");
				}
				if (linea.contains("rm STARTED") && configuracion.getCrawlSystem().getExtraction().contains("finish")) {

					// Indexing when it's finished
					pw.println("sh juntarSalidas.sh");
					pw.println("java -jar $HOME/101CrawlersWeb/butler.jar index");
				}
				pw.println(linea);
			}
		}
	}

	private void createJuntarSalidasSh(Resource resources, String directoryName) throws IOException {
		Resource juntarSalidas = resources.createRelative("juntarSalidas");
		try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "juntarSalidas.sh")));
				Scanner scan = new Scanner(juntarSalidas.getInputStream())) {
			while (scan.hasNextLine()) {
				String linea = scan.nextLine();
				pw.println(linea);
			}
		}
	}

}
