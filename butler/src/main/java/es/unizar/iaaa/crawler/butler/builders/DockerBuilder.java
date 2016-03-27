/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/* 
 * Builds the dockerbuilder creates the dockerfile and the files needed for the system
 */

@Component
public class DockerBuilder {

    @Autowired
    private NutchBuilder crawlerBuilder;

    public void crearDockerfile(CrawlConfiguration config, File resources, String directoryName) {
                /*
                 * Crear el fichero run.sh que servirá para empezar a ejecutar
				 * el crawler 
				 */
            createRunSh(config, resources, directoryName);
                /*
                 * Crear el fichero junstarSalidas.sh que servirá para empezar a
				 * ejecutar el crawler
				 */
            createJuntarSalidasSh(resources, directoryName);

				/* Crear el fichero docker */

            createDockerfile(config, resources, directoryName);
    }

    private void createDockerfile(CrawlConfiguration configuracion, File resources, String directoryName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "Dockerfile")))) {
				/* Añadir dockerOS */
            pw.println("From " + configuracion.getDockerOS().getName() + ":"
                    + configuracion.getDockerOS().getVersion());
            Path dockerbase = Paths.get(new File(resources, "DockerBase").toURI());
				/* Añadir contenido fijo */
            Scanner scan = new Scanner(dockerbase);
            while (scan.hasNextLine()) {
                String linea = scan.nextLine();
                pw.write(linea + "\n");
            }
            scan.close();
            crawlerBuilder.addDockerfile(configuracion, directoryName, pw);
				
				pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    private void createRunSh(CrawlConfiguration configuracion, File resources, String directoryName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "run.sh")))) {
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

            Scanner scan = new Scanner(new File(resources, "runNutch"));
        	while (scan.hasNextLine()) {
        		String linea = scan.nextLine();
        		pw.write(linea + "\n");
        	}
        	scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
	
    private void createJuntarSalidasSh(File resources,  String directoryName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "juntarSalidas.sh")))) {
            Scanner scan = new Scanner(new File(resources, "juntarSalidas"));
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
