package es.unizar.iaaa.crawler.butler.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.logging.Logger;

// TODO @Iñigo Document me!
@Component
public class CrawlerCommands implements CommandMarker {

	static Logger log = Logger.getLogger(CrawlerCommands.class.getName());
	@Autowired
	private Operations ops;

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
		String response = "";
		try {
			String id = idUser + "_" + idCrawler;
			File directorio = new File(id);
			if (directorio.isDirectory()) {
				// docker run -i -d nameOfImage nameOfContainer
				String comando = "docker run -i -d --name=\"" + id + "\" " + id;
				ops.executeCommand(comando, true);
				response += " with name " + id;
			} else {
				response = "Docker image dont exist, please, try executing the build command";
			}

		} catch (Exception e) {
			response = "Files not found";
		}

		return response;
	}

	/* Run the crawl in the docker container */
	@CliCommand(value = "run", help = "the docker cointainer must be running")
	public String run(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = {
					"idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler) {
		String response = "";
		try {
			String id = idUser + "_" + idCrawler;

			// docker exec idContainer sh crawler/run.sh
			String comando = "docker exec " + id + " sh crawler/run.sh";
			ops.executeCommand(comando, true);

		} catch (Exception e) {
			response = "Docker container dont exist, please, try executing the start command";
		}

		return response;
	}

	/* "Extracts the info crawled in the docker container to an output path */
	@CliCommand(value = "extract", help = "Extracts the info crawled in the docker container to an output path")
	public String extract(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = { "path" }, mandatory = true, help = "output path") final String path, @CliOption(key = {
					"idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler) {
		String response = "";
		try {
			String id = idUser + "_" + idCrawler;
			// docker exec idContainer sh crawler/juntarSalidas.sh
			String comando = "docker exec " + id + " sh crawler/juntarSalidas.sh";
			ops.executeCommand(comando, true);
			// docker cp $idContainer:root/crawler/salida/salida $path
			comando = "docker exec " + id + ":root/crawler/salida/salida " + path;
			ops.executeCommand(comando, true);

		} catch (Exception e) {
			response = "Docker container dont exist, please, try executing the start command";
		}

		return response;
	}
}
