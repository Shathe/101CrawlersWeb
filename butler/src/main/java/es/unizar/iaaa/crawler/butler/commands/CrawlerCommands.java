package es.unizar.iaaa.crawler.butler.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

import es.unizar.iaaa.crawler.butler.Application;
import es.unizar.iaaa.crawler.butler.builders.AdaptadorBuilder;
import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.validator.ConfigurationValidator;
import es.unizar.iaaa.crawler.butler.validator.ValidationResult;
import es.unizar.iaaa.crawler.butler.yalm.YamlConfigRunner;

@Component
@ContextConfiguration(classes = { Application.class })
public class CrawlerCommands implements CommandMarker {

	static Logger log = Logger.getLogger(CrawlerCommands.class.getName());
	
	private Operations ops=new Operations();


	@CliAvailabilityIndicator({ "start" })
	public boolean startAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "extract" })
	public boolean extractAvailable() {
		// always available
		return true;
	}


	@CliAvailabilityIndicator({ "run" })
	public boolean stopNutchAvailable() {
		// always available
		return true;
	}


	/* Start docker container */
	@CliCommand(value = "start", help = "the docker image must be created")
	public String start(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = {
					"idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler) {
		String respuesta = "";
		try {
			String id = idUser + "_" + idCrawler;
			File directorio = new File(id);
			if (directorio.isDirectory()) {
				// docker run -i -d nameOfImage nameOfContainer
				String comando = "docker run -i -d --name=\"" + id + "\" " + id;
				ops.executeCommand(comando, true);
				respuesta += " with name " + id;
			} else {
				respuesta = "Docker image dont exist, please, try executing the build command";
			}

		} catch (Exception e) {
			respuesta = "Files not found";
		}

		return respuesta;
	}

	/* Run the crawl in the docker container */
	@CliCommand(value = "run", help = "the docker cointainer must be running")
	public String run(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = {
					"idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler) {
		String respuesta = "";
		try {
			String id = idUser + "_" + idCrawler;

			// docker exec idContainer sh crawler/run.sh
			String comando = "docker exec " + id + " sh crawler/run.sh";
			ops.executeCommand(comando, true);

		} catch (Exception e) {
			respuesta = "Docker container dont exist, please, try executing the start command";
		}

		return respuesta;
	}
	/* "Extracts the info crawled in the docker container to an output path*/
	@CliCommand(value = "extract", help = "Extracts the info crawled in the docker container to an output path")
	public String extract(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = { "path" }, mandatory = true, help = "output path") final String path, @CliOption(key = {
					"idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler) {
		String respuesta = "";
		try {
			String id = idUser + "_" + idCrawler;
			// docker exec idContainer sh crawler/juntarSalidas.sh
			String comando = "docker exec " + id + " sh crawler/juntarSalidas.sh";
			ops.executeCommand(comando, true);
			// docker cp $idContainer:root/crawler/salida/salida $path
			comando = "docker exec " + id + ":root/crawler/salida/salida " + path;
			ops.executeCommand(comando, true);

		} catch (Exception e) {
			respuesta = "Docker container dont exist, please, try executing the start command";
		}

		return respuesta;
	}}
