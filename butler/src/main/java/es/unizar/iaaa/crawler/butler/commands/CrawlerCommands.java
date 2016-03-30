package es.unizar.iaaa.crawler.butler.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Crawler commands. This class contains every command which deals with the
 * crawl system once it's built and created.
 */

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

	@CliAvailabilityIndicator({ "finished" })
	public boolean finAvailable() {
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

	/**
	 * Start docker container
	 */
	@CliCommand(value = "start", help = "the docker image must be created")
	public String start(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = { "idCrawl" }, mandatory = true, help = "id of the new crawler") final String idCrawl) {
		String response = "";

		try {
			String id = idUser + "_" + idCrawl;
			if (ops.imageExists(idUser, idCrawl)) {
				// docker run -i -d nameOfImage nameOfContainer
				String comando = "";
				// if it was stopped it has to be restarted
				if (ops.containerStopped(idUser, idCrawl)) {
					comando = "docker restart " + id;
					response += "Container restarted";

				} else if (ops.containerPaused(idUser, idCrawl)) {
					comando = "docker unpause " + id;
					response += "Container unpaused";
				} else {

					comando = "docker run -i -d --name=\"" + id + "\" " + id;
					response += "Container started";

				}
				ops.executeCommand(comando, true);
			} else {
				response = "Docker image don't exist, please, try executing the build command";
			}

		} catch (Exception e) {
			response = "Files not found";
		}

		return response;
	}

	/**
	 * Run the crawl in the docker container
	 */
	@CliCommand(value = "run", help = "the docker cointainer must be running")
	public String run(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = { "idCrawl" }, mandatory = true, help = "id of the new crawler") final String idCrawl) {
		String response = "";

		if (ops.containerExists(idUser, idCrawl) && ops.containerRunning(idUser, idCrawl)) {
			try {
				String id = idUser + "_" + idCrawl;

				// docker exec idContainer sh crawler/run.sh
				String comando = "docker exec -d " + id + " sh crawler/run.sh";
				ops.executeCommand(comando, true);
				response = "Crawler started";

			} catch (Exception e) {
				response = "Docker container don't exist, please, try executing the start command";
			}
		} else {
			response = "Docker container don't exist, please, try executing the start command";
		}

		return response;
	}

	/**
	 * Extracts the info crawled in the docker container to an output path
	 */
	@CliCommand(value = "extract", help = "Extracts the info crawled in the docker container to an output path")
	public String extract(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = { "path" }, mandatory = true, help = "output path") final String path,
			@CliOption(key = { "idCrawl" }, mandatory = true, help = "id of the new crawler") final String idCrawl) {
		String response = "";
		if (ops.containerExists(idUser, idCrawl) && ops.containerRunning(idUser, idCrawl)) {

			try {
				String id = idUser + "_" + idCrawl;
				// docker exec idContainer sh crawler/juntarSalidas.sh
				String comando = "docker exec " + id + " sh crawler/juntarSalidas.sh";
				ops.executeCommand(comando, true);
				// docker cp $idContainer:root/crawler/salida/salida $path
				comando = "docker cp " + id + ":root/crawler/salida/salida " + path;
				ops.executeCommand(comando, true);
				response = "Extracted correctly";

			} catch (Exception e) {
				response = "Docker container don't exist, please, try executing the start command";
			}
		} else {
			response = "Docker container don't exist, please, try executing the start command";
		}

		return response;
	}

	/**
	 * Returns true only if the crawler has finished
	 */
	@CliCommand(value = "finished", help = "Returns true only if the crawler has finished")
	public String finished(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = { "idCrawl" }, mandatory = true, help = "id of the new crawler") final String idCrawl) {
		String response = "The crawler hasn't finised yet";

		if (ops.containerExists(idUser, idCrawl) && ops.containerRunning(idUser, idCrawl)) {

			if(this.fin(idUser, idCrawl)) response = "Yes, the crawler has finished";
		} else {
			response = "Docker container don't exist, please, try executing the start command";
		}

		return response;
	}

	private boolean fin(String idUser, String idCrawl) {
		boolean finished = false;

			try {
				String id = idUser + "_" + idCrawl;
				// docker exec idContainer sh crawler/juntarSalidas.sh
				String comando = "docker exec " + id + " ls crawler/FIN";
				BufferedReader out=ops.executeCommand(comando, false);
				String s="";
				while ((s = out.readLine()) != null) {
					if (s.contains(id) && s.contains("FIN"))
						finished = true;
				}

			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			}
		
		return finished;
	}

}
