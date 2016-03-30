/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public void crearDockerfile(CrawlConfiguration config, File resources, String directoryName) throws IOException {
        // Creates the run.sh file  which will be used for executing the crawl
        createRunSh(config, resources, directoryName);
        // Creates the juntarSalidas.sh file which will be used for executing the crawl
        createJuntarSalidasSh(resources, directoryName);
        // Create dockerfile
        createDockerfile(config, resources, directoryName);
    }

    private void createDockerfile(CrawlConfiguration configuracion, File resources, String directoryName) throws IOException {
        Path dockerbase = Paths.get(new File(resources, "DockerBase").toURI());
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "Dockerfile")));
             Scanner scan = new Scanner(dockerbase)
        ) {
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

    private void createRunSh(CrawlConfiguration configuracion, File resources, String directoryName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "run.sh")));
             Scanner scan = new Scanner(new File(resources, "runNutch"))
        ) {
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
                pw.println(linea);
            }
        } catch (IOException e) {
            LOGGER.warn("IOException: "+e.getMessage(), e);
        }
    }

    private void createJuntarSalidasSh(File resources, String directoryName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "juntarSalidas.sh")));
             Scanner scan = new Scanner(new File(resources, "juntarSalidas"));
        ) {
            while (scan.hasNextLine()) {
                String linea = scan.nextLine();
                pw.println(linea);
            }
        } catch (IOException e) {
            LOGGER.warn("IOException: "+e.getMessage(), e);
        }
    }

}
