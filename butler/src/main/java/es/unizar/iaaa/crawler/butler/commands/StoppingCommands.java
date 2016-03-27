package es.unizar.iaaa.crawler.butler.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.util.logging.Logger;

/* 
 * Stopping commands. This class contains every command which stops the crawling system 
 * or a part of it
 */@Component
public class StoppingCommands implements CommandMarker {

	static Logger log = Logger.getLogger(StoppingCommands.class.getName());

	@Autowired
	private Operations ops;

	@CliAvailabilityIndicator({ "stopNutch" })
	public boolean stopNutchAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "stopContainer" })
	public boolean stopContainerAvailable() {
		// always available
		return true;
	}

	/* Stop the crawl in the docker container */
	@CliCommand(value = "stopNutch", help = "Stops the crawler int the docker container")
	public String stopNutch(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = {
					"idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler) {
		String response = "";
		try {
			String id = idUser + "_" + idCrawler;
			// docker exec $idContainer kill -9 $(docker exec $idContainer ps |
			// grep crawl | awk '{print $1;}')
			// docker exec $idContainer kill -9 $(docker exec $idContainer ps |
			// grep sh | awk '{print $1;}')
			// docker exec $idContainer kill -9 $(docker exec $idContainer ps |
			// grep java | awk '{print $1;}')
			String comando = "docker exec  " + id + " ps";
			BufferedReader out = ops.executeCommand(comando, false);
			String process = "";
			while ((process = out.readLine()) != null) {
				// TODO @Iñigo Documentar en inglés
				/* Para todos los procesos busca los procesos a eliminar */
				if (process.contains("crawl") || process.contains("java") || process.contains("java")) {
					log.info(process);
					comando = " docker exec  " + id + " kill -9 " + process.split(" ")[1];
					log.info(comando);
					ops.executeCommand(comando, true);

				}
			}

		} catch (Exception e) {
			response = "Docker container dont exist, please, try executing the start command";
		}

		return response;
	}

	/* Stop the docker container */
	@CliCommand(value = "stopContainer", help = "stops the docker Container")
	public String stopContainer(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = {
					"time" }, mandatory = false, specifiedDefaultValue = "1", help = "time in seconds (waiting until shutting down)") final String time,
			@CliOption(key = {
					"idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler) {
		String response = "";
		try {
			String id = idUser + "_" + idCrawler;
			// docker stop -t $tiempo $idContainer
			String comando = "docker stop -t " + time + " " + id;
			ops.executeCommand(comando, true);

		} catch (Exception e) {
			response = "Docker container dont exist, please, try executing the start command";
		}

		return response;
	}
}