package es.unizar.iaaa.crawler.butler.commands;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.index.IndexFiles;
import es.unizar.iaaa.crawler.butler.index.SearchFiles;
import es.unizar.iaaa.crawler.butler.model.SearchResult;

/**
 * Crawler commands. This class contains every command which deals with the
 * crawl system once it's built and created.
 */

@Component
public class CrawlerCommands implements CommandMarker {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerCommands.class);
	@Autowired
	private Operations ops;

	@CliAvailabilityIndicator({ "start" })
	public boolean startAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "index" })
	public boolean indexAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "search" })
	public boolean searchAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "finished" })
	public boolean finAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "run" })
	public boolean stopNutchAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "info" })
	public boolean infoAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "status" })
	public boolean statusAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "runningStatus" })
	public boolean runningStatusAvailable() {
		// always available
		return true;
	}

	/**
	 * Start docker container
	 */
	@CliCommand(value = "start", help = "the docker image must be created")
	public String start(

			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String response = "";
		String idImage = idProject + "_" + imageName;
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (ops.containerRunning(idContainer)) {
			return "The container is already running";
		}
		try {

			if (ops.imageExists(idImage)) {
				// docker run -i -d nameOfImage nameOfContainer
				String command;
				// if it was stopped it has to be restarted
				if (ops.containerStopped(idContainer)) {
					command = "docker restart " + idContainer;
					response += "Container restarted";

				} else if (ops.containerPaused(idContainer)) {
					command = "docker unpause " + idContainer;
					response += "Container unpaused";
				} else {

					command = "docker run -i -d --name=\"" + idContainer + "\" " + idImage;
					response += "Container started";

				}
				ops.executeCommand(command, true);
			} else {
				response = "Docker image don't exist, please, try executing the build command";
			}
			LOGGER.info(response + " " + idContainer);

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
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String response;
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (ops.containerExists(idContainer) && ops.containerRunning(idContainer)) {
			try {

				// docker exec idContainer sh crawler/run.sh
				String command = "docker exec -d " + idContainer + " sh crawler/run.sh";
				ops.executeCommand(command, true);
				response = "Crawler started";
				LOGGER.info(response + " " + idContainer);

			} catch (Exception e) {
				LOGGER.warn("IOException: " + e.getMessage(), e);
				response = "Docker container don't exist, please, try executing the start command";
			}
		} else {
			response = "Docker container don't exist, please, try executing the start command";
		}

		return response;
	}

	/**
	 * Extracts the info crawled in the docker container and index it
	 */
	@CliCommand(value = "index", help = "Extracts the info crawled in the docker container and index it")
	public String index(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;
		String indexPath = idContainer + "_index";
		Path outputPath = Paths.get(indexPath);
		// If the folder exists, delete it (rewrite the index)

		try {
			FileUtils.deleteDirectory(outputPath.toFile());
			Files.createDirectory(outputPath);
		} catch (IOException e1) {
			LOGGER.warn("IOException: " + e1.getMessage(), e1);
			return "Failing creating the index folder";
		}
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer) || !ops.containerRunning(idContainer)) {
			return "Docker container don't exist, please, try executing the start command";
		}

		String command1 = "docker exec " + idContainer + " sh crawler/juntarSalidas.sh";
		try {
			ops.executeCommand(command1, true);
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "Docker exec failed";
		}

		String command2 = "docker cp " + idContainer + ":root/crawler/salida/salida " + indexPath + "/output.txt";
		try {
			ops.executeCommand(command2, true);
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "Docker cp failed";
		}

		try {
			// Index
			IndexFiles nuevo = new IndexFiles();
			nuevo.index(idContainer + "_index/index", new File(idContainer + "_index/output.txt"));

			// Ahora este índice está más actualizado o igual que el de
			// docker,
			// así
			// que se borra que el indice está pendiente
			// en el contendor respecto a el del sistema
			command1 = "docker exec " + idContainer + " rm crawler/IndexPending";
			ops.executeCommand(command1, false);
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "Docker exec failed";

		}
		LOGGER.info("Indexed correctly " + idContainer);

		return "Indexed correctly";
	}

	/**
	 * Returns true only if the crawler has finished
	 */
	@CliCommand(value = "finished", help = "Returns true only if the crawler has finished")
	public String finished(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		String command = "docker exec " + idContainer + " ls crawler";
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer) || !ops.containerRunning(idContainer)) {
			return "Docker container don't exist, please, try executing the start command";
		}

		String s;
		try (BufferedReader out = ops.executeCommand(command, false)) {
			while ((s = out.readLine()) != null) {
				if (s.contains("FIN"))
					return "Yes, the crawler has finished";
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "I don't known";
		}
		return "The crawler hasn't finished yet";
	}

	/**
	 * Returns the number of unfetched links, fetched links and number of rounds
	 * done
	 */
	@CliCommand(value = "runningStatus", help = "Returns the number of unfetched links")
	public String links(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		String command = "docker exec " + idContainer + " crawler/bin/nutch readdb crawler/micrawl/crawldb -stats";
		if (!ops.dockerIsRunning()) {
			return "Fetched links: Unknwon, unfetched links: Unknwon, rounds: Unknwon";
		}
		if (!ops.containerExists(idContainer) || !ops.containerRunning(idContainer)) {
			return "Fetched links: Unknwon, unfetched links: Unknwon, rounds: Unknwon";
		}

		String s;
		String unfetched = "0";
		String fetched = "0";
		String rounds = "0";
		String result = "The crawler hasn't been started yet";
		try {
			BufferedReader out = ops.executeCommand(command, false);
			while ((s = out.readLine()) != null) {
				if (s.contains("status 1 (db_unfetched):")) {
					s = s.replace("status 1 (db_unfetched):", "");
					s = s.replace("	", "");
					unfetched = s;
				}
			}
			command = "docker exec " + idContainer + " cat crawler/roundsDone.txt";
			out = ops.executeCommand(command, false);
			while ((s = out.readLine()) != null) {
				if (s.contains("/"))
					rounds = s;
			}
			command = "docker exec " + idContainer + " crawler/bin/nutch readdb crawler/micrawl/crawldb -stats";
			out = ops.executeCommand(command, false);
			while ((s = out.readLine()) != null) {
				if (s.contains("status 2 (db_fetched):")) {
					s = s.replace("status 2 (db_fetched):", "");
					s = s.replace("	", "");
					fetched = s;
				}
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			fetched = "I don't known";
		}
		if (unfetched.equals("") && rounds.equals("") && fetched.equals(""))
			return result;

		if (!(rounds.contains("/") || rounds.contains("0")))
			rounds = "Don't know";
		try {
			Integer.valueOf(fetched);
			Integer.valueOf(unfetched);
		} catch (Exception a) {
			unfetched = "Don't know";
			fetched = "Don't know";

		}

		return "Fetched links: " + fetched + ", unfetched links: " + unfetched + ", rounds: " + rounds;
	}

	/**
	 * Returns the number of fetch links
	 */
	@CliCommand(value = "fetchLinks", help = "Returns the number of fetched links")
	public String unfetchLinks(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		String command = "docker exec " + idContainer + " crawler/bin/nutch readdb crawler/micrawl/crawldb -stats";
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer) || !ops.containerRunning(idContainer)) {
			return "Docker container don't exist, please, try executing the start command";
		}

		String s;
		try (BufferedReader out = ops.executeCommand(command, false)) {
			while ((s = out.readLine()) != null) {
				if (s.contains("status 2 (db_fetched):")) {
					s = s.replace("status 2 (db_fetched):", "");
					s = s.replace("	", "");
					s = s.replace(" ", "");
					return s;
				}
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "I don't known";
		}
		return "The crawler hasn't been started yet";
	}

	/**
	 * Gives information about the crawl, if the crawl is actually running
	 */
	@CliCommand(value = "info", help = "information about the crawl")
	public String info(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		String command = "docker exec " + idContainer + " ls crawler";
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer)) {
			return "Docker container don't exist, please, try executing the start command";
		}
		if (!ops.containerRunning(idContainer)) {
			return "I don't known (container stopped/paused)";
		}

		// docker exec idContainer sh crawler/juntarSalidas.sh
		String s;
		try (BufferedReader out = ops.executeCommand(command, false)) {
			while ((s = out.readLine()) != null) {
				if (s.contains("STARTED"))
					return "Running";
				if (s.contains("FIN"))
					return "Finished";
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "I don't known";
		}
		return "Not started";
	}

	/**
	 * Gives information about the container status, if the container is paused,
	 * stopped, running
	 */
	@CliCommand(value = "status", help = "status about the container")
	public String status(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer)) {
			return "Docker container don't exist, please, try executing the start command";
		}

		if (ops.containerRunning(idContainer))
			return "Running";
		if (ops.containerPaused(idContainer))
			return "Paused";
		if (ops.containerStopped(idContainer))
			return "Stopped";

		return "Unknown status";
	}

	/**
	 * search in the index a query
	 */
	@CliCommand(value = "search", help = "search in the index a query, you can specify the maximun number of results with the 'top' argument")
	public String search(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = { "top" }, help = "number of Max results that will be shown") final Integer max,
			@CliOption(key = {
					"query" }, mandatory = true, help = "the query is going to be search") final String query,

			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer) || !ops.containerRunning(idContainer)) {
			return "Docker container don't exist, please, try executing the start command";
		}
		// Check if there is any index locally pending in the container
		String command = "docker exec " + idContainer + " ls crawler";
		String s;
		try (BufferedReader out = ops.executeCommand(command, false)) {
			while ((s = out.readLine()) != null) {
				if (s.contains("IndexPending")) {
					// copy the index files
					String indexPath = idContainer + "_index";
					Path outputPath = Paths.get(indexPath);
					try {
						FileUtils.deleteDirectory(outputPath.toFile());
						Files.createDirectory(outputPath);
					} catch (IOException e1) {
						return "Failing managing the index folder";
					}
					// copy the index to the system
					command = "docker cp " + idContainer + ":root/crawler/index " + indexPath + "/index";
					ops.executeCommand(command, true);
					// It is not pending now
					command = "docker exec " + idContainer + " rm crawler/IndexPending";
					ops.executeCommand(command, false);
				}
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
		}

		SearchFiles searcher = new SearchFiles();
		try {
			ArrayList<SearchResult> result = searcher.search(idContainer + "_index/", query);
			// return result.size() > 0 ? "best match "+result.get(0).getUrl() :
			// "no matches";
			if (result.size() > 0) {
				// paging results
				for (int i = 0; i < result.size() && (max == null || i < max); i++) {
					System.out.println(result.get(i).getUrl());
				}
			}
			return "Results shown";
		} catch (Exception e) {
			LOGGER.warn("Exception : " + e.getMessage(), e);
			return "Search failed, try indexing first";
		}

	}

}