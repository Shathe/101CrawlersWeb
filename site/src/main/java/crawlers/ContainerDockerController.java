/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package crawlers;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletResponse;

import dataBase.ConfigurationDatabase;
import dataBase.ContainerDockerDatabase;
import dataBase.ProjectDatabase;
import errors.InternalError;
import models.Configuration;
import models.ContainerDocker;
import models.ContainerStatus;
import ops.CommonOps;

/**
 * Controller for containers. Manage every operation which deals with the
 * projects.
 *
 * @author shathe
 */
@RestController
public class ContainerDockerController {
	private static final Logger log = LoggerFactory.getLogger(ContainerDockerController.class);
	@Autowired
	JdbcTemplate jdbcTemplate;

	private CommonOps ops = new CommonOps();

	/**
	 * Returns the containers of a specified image
	 */
	@RequestMapping(value = "/containers", method = RequestMethod.GET)
	ResponseEntity<List<ContainerDocker>> listContainer(@RequestParam(value = "idImage") String idImage) {

		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);

		log.info("listContainers " + idImage);
		List<ContainerDocker> containers;
		try {
			containers = containerDB.getContainers(idImage);

		} catch (Exception a) {
			log.warn("Error listing: " + a.getMessage());
			throw new InternalError("Error listing containers: " + a.getMessage());
		}

		return new ResponseEntity<>(containers, HttpStatus.OK);
	}

	/**
	 * Returns the deleted containers if it has been deleted, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/deleteContainer", method = RequestMethod.DELETE)
	ResponseEntity<ContainerDocker> deleteContainer(@RequestBody ContainerDocker container) {
		List<ContainerDocker> containers = new ArrayList<>();
		containers.add(container);
		try {
			ops.deleteContainers(containers, jdbcTemplate);
		} catch (Exception e) {
			log.warn("Error deleting: " + e.getMessage());
			throw new InternalError("Error deleting: " + e.getMessage());
		}

		return new ResponseEntity<>(container, HttpStatus.OK);
	}

	/**
	 * Returns the updated containers if it has been updated, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/editContainer", method = RequestMethod.POST)
	ResponseEntity<ContainerDocker> editContainer(@RequestBody ContainerDocker container) {

		log.info("updating containers " + container.getId());

		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		try {
			containerDB.updateContainer(container);
			log.info("updated container " + container.getId());

		} catch (Exception a) {
			log.warn("Error updating: " + a.getMessage());
			throw new InternalError("Error updating: " + a.getMessage());
		}

		return new ResponseEntity<>(container, HttpStatus.OK);
	}

	/**
	 * Returns the created containers if it has been created and start the
	 * cralwer, if not, returns an error message
	 */
	@RequestMapping(value = "/createContainer", method = RequestMethod.POST)
	ResponseEntity<ContainerDocker> createContainer(@RequestParam(value = "idProject") String idProject,
			@RequestParam(value = "idImage") String idImage, @RequestParam(value = "name") String name) {
		// gets the last configuration of the project
		ContainerDocker container = new ContainerDocker(0, idImage, idProject, name,
				new Date(System.currentTimeMillis()));
		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		try {
			containerDB.createContainer(container);
			log.info("created container " + container.getName());
			container = containerDB.getContainerJustCreated(idImage);
			ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
			// gets the last configuration of the project
			Configuration config = confDB.GetConfigurationFromProject(idProject);
			String command = "java -jar ../butler.jar do start --containerName " + container.getId() + " --imageName "
					+ container.getIdImage() + " --idProject " + idProject + "_" + config.getId();
			log.info("Command: " + command);
			BufferedReader out = ops.executeCommand(command, false);
			String lineOut;
			String errorMessage = "";
			boolean error = true;
			while ((lineOut = out.readLine()) != null) {
				errorMessage = lineOut;
				if ((lineOut.contains("Container started"))) {
					error = false;
				}
			}
			if (error) {
				log.warn("Not valid container: " + errorMessage);
				throw new InternalError("Not valid container: " + errorMessage);
			}
			// starts the crawl
			command = "java -jar ../butler.jar do run --containerName " + container.getId() + " --imageName "
					+ container.getIdImage() + " --idProject " + container.getIdProject() + "_" + config.getId();
			log.info("Command: " + command);
			ops.executeCommand(command, true);

		} catch (Exception a) {
			throw new InternalError("Error creating: " + a.getMessage());
		}

		return new ResponseEntity<>(container, HttpStatus.OK);
	}

	/**
	 * Starts the crawler of a container Returns a message with the status of
	 * the action
	 */
	@RequestMapping(value = "/startsContainer", method = RequestMethod.POST)
	ResponseEntity<String> startsContainer(@RequestParam(value = "idContainer") String idContainer) {
		// gets the last configuration of the project

		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		try {
			ContainerDocker container = containerDB.getContainerFromId(idContainer);
			ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
			// gets the last configuration of the project
			Configuration config = confDB.GetConfigurationFromProject(container.getIdProject());
			String command = "java -jar ../butler.jar do start --containerName " + container.getId() + " --imageName "
					+ container.getIdImage() + " --idProject " + container.getIdProject() + "_" + config.getId();
			log.info("Command: " + command);
			BufferedReader out = ops.executeCommand(command, false);
			String lineOut;
			String errorMessage = "";
			boolean error = true;
			while ((lineOut = out.readLine()) != null) {
				errorMessage = lineOut;
				if (lineOut.contains("Container started") || lineOut.contains("Container unpaused")
						|| lineOut.contains("Container restarted")) {
					error = false;
				}
			}
			if (error) {
				log.warn("Not valid container: " + errorMessage);
				throw new InternalError("Not valid container: " + errorMessage);
			}

		} catch (Exception a) {
			throw new InternalError("Error starting container: " + a.getMessage());
		}

		return new ResponseEntity<>("Container started", HttpStatus.OK);
	}

	/**
	 * Starts the crawler of a container Returns a message with the status of
	 * the action
	 */
	/*
	 * @RequestMapping(value = "/runContainer", method = RequestMethod.POST)
	 * ResponseEntity<String> runContainer(@RequestParam(value = "idContainer")
	 * String idContainer) { // gets the last configuration of the project
	 * 
	 * ContainerDockerDatabase containerDB = new
	 * ContainerDockerDatabase(jdbcTemplate); try { ContainerDocker container =
	 * containerDB.getContainerFromId(idContainer); ConfigurationDatabase confDB
	 * = new ConfigurationDatabase(jdbcTemplate); // gets the last configuration
	 * of the project Configuration config =
	 * confDB.GetConfigurationFromProject(container.getIdProject()); String
	 * command = "java -jar ../butler.jar do run --containerName " +
	 * container.getId() + " --imageName " + container.getIdImage() +
	 * " --idProject " + container.getIdProject() + "_" + config.getId();
	 * log.info("Command: " + command); BufferedReader out =
	 * ops.executeCommand(command, false); String lineOut = ""; String
	 * errorMessage = ""; boolean error = true; while ((lineOut =
	 * out.readLine()) != null) { errorMessage = lineOut; if ((lineOut.contains(
	 * "Crawler started"))) { error = false; } } if (error) { log.warn(
	 * "Not valid container: " + errorMessage); throw new InternalError(
	 * "Not valid container: " + errorMessage); }
	 * 
	 * } catch (Exception a) { throw new InternalError(
	 * "Error starting crawler: " + a.getMessage()); }
	 * 
	 * return new ResponseEntity<>("Crawler started", HttpStatus.OK); }
	 */

	/**
	 * Get the status of a container Returns a message with the status of the
	 * action
	 */
	@RequestMapping(value = "/statusContainer", method = RequestMethod.GET)
	ResponseEntity<ContainerStatus> statusContainer(@RequestParam(value = "idContainer") String idContainer) {
		// gets the last configuration of the project
		ContainerStatus status = new ContainerStatus();

		String message = "error connecting the server";
		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		try {
			ContainerDocker container = containerDB.getContainerFromId(idContainer);
			ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
			// gets the last configuration of the project
			Configuration config = confDB.GetConfigurationFromProject(container.getIdProject());
			String command = "java -jar ../butler.jar do status --containerName " + container.getId() + " --imageName "
					+ container.getIdImage() + " --idProject " + container.getIdProject() + "_" + config.getId();
			log.info("Command: " + command);
			BufferedReader out = ops.executeCommand(command, false);
			String lineOut;
			while ((lineOut = out.readLine()) != null) {
				// get the last message with the state
				message = lineOut;
			}
			status.setContainerStatus(message);
			command = "java -jar ../butler.jar do info --containerName " + container.getId() + " --imageName "
					+ container.getIdImage() + " --idProject " + container.getIdProject() + "_" + config.getId();
			log.info("Command: " + command);
			out = ops.executeCommand(command, false);
			while ((lineOut = out.readLine()) != null) {
				// get the last message with the state
				message = lineOut;
			}
			status.setCrawlerStatus(message);

			command = "java -jar ../butler.jar do runningStatus --containerName " + container.getId() + " --imageName "
					+ container.getIdImage() + " --idProject " + container.getIdProject() + "_" + config.getId();
			log.info("Command: " + command);
			out = ops.executeCommand(command, false);
			while ((lineOut = out.readLine()) != null) {
				// get the last message with the state
				message = lineOut;
			}
			// message -> Fetched links: 2, unfetched links: 158, rounds: 1/2
			message = message.replace("Fetched links:", "");
			message = message.replace("unfetched links:", "");
			message = message.replace("rounds:", "");
			message = message.replace(",", "");
			Scanner scan = new Scanner(message);
			try {
				status.setFetchedLinks(scan.next());
				status.setUnfetchedLinks(scan.next());
				status.setRounds(scan.next());
			} catch (Exception a) {
				status.setFetchedLinks("0");
				status.setUnfetchedLinks("0");
				status.setRounds("0");
			}
			scan.close();
		} catch (Exception a) {
			throw new InternalError("Error getting the status of the crawler: " + a.getMessage());
		}

		return new ResponseEntity<>(status, HttpStatus.OK);
	}

	/**
	 * Indexes information crawled Returns a message with the status of the
	 * action
	 */
	@RequestMapping(value = "/indexContainer", method = RequestMethod.POST)
	ResponseEntity<String> indexContainer(@RequestParam(value = "idContainer") String idContainer) {
		// gets the last configuration of the project

		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		ContainerDocker container = containerDB.getContainerFromId(idContainer);
		ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
		// gets the last configuration of the project
		Configuration config = confDB.GetConfigurationFromProject(container.getIdProject());
		String command = "java -jar ../butler.jar do index --containerName " + container.getId() + " --imageName "
				+ container.getIdImage() + " --idProject " + container.getIdProject() + "_" + config.getId();
		log.info("Command: " + command);

		ops.checkMessage(command, "Indexed correctly", "Error indexing crawler");
		return new ResponseEntity<>("Crawler indexed", HttpStatus.OK);
	}

	/**
	 * searchs a query in information crawled Returns a message with the status
	 * of the action
	 */
	@RequestMapping(value = "/searchContainer", method = RequestMethod.GET)
	ResponseEntity<List<String>> searchContainer(@RequestParam(value = "idContainer") String idContainer,
			@RequestParam(value = "query") String query) {
		// gets the last configuration of the project
		List<String> results = new ArrayList<>();
		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		try {
			ContainerDocker container = containerDB.getContainerFromId(idContainer);
			ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
			// gets the last configuration of the project
			Configuration config = confDB.GetConfigurationFromProject(container.getIdProject());
			String command = "java -jar ../butler.jar do search --query " + query + " --top 20 --containerName "
					+ container.getId() + " --imageName " + container.getIdImage() + " --idProject "
					+ container.getIdProject() + "_" + config.getId();
			log.info("Command: " + command);
			BufferedReader out = ops.executeCommand(command, false);
			String lineOut;
			boolean error = false;
			while ((lineOut = out.readLine()) != null) {
				if (!lineOut.equals("") && !lineOut.contains("total matching documents")
						&& !lineOut.contains("Results shown"))
					results.add(lineOut);
				if (lineOut.contains("[WARN]"))
					error = true;
			}
			// falta poner lo del --top
			if (error)
				throw new InternalError("Error searching crawler, try again in some minutes");

		} catch (Exception a) {
			throw new InternalError("Error searching crawler: " + a.getMessage());
		}

		return new ResponseEntity<>(results, HttpStatus.OK);
	}


	/**
	 * searchs a query in information crawled Returns a message with the status
	 * of the action
	 */
	@RequestMapping(value = "/downloadResults", method = RequestMethod.GET)
	void downloadResults(@RequestParam(value = "idContainer") String idContainer,
			@RequestParam(value = "query") String query, HttpServletResponse response) {
		File file;
		// gets the last configuration of the project
		List<String> results = new ArrayList<>();
		ContainerDocker container;
		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		ProjectDatabase projectDB = new ProjectDatabase(jdbcTemplate);
		try {
			container = containerDB.getContainerFromId(idContainer);
			ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
			// gets the last configuration of the project
			Configuration config = confDB.GetConfigurationFromProject(container.getIdProject());
			String command = "java -jar ../butler.jar do search --query " + query + " --containerName "
					+ container.getId() + " --imageName " + container.getIdImage() + " --idProject "
					+ container.getIdProject() + "_" + config.getId();
			log.info("Command: " + command);
			BufferedReader out = ops.executeCommand(command, false);
			String lineOut;
			boolean error = false;
			while ((lineOut = out.readLine()) != null) {
				if (!lineOut.equals("") && !lineOut.contains("total matching documents")
						&& !lineOut.contains("Results shown"))
					results.add(lineOut);
				if (lineOut.contains("[WARN]"))
					error = true;
			}
			if (error)
				throw new InternalError("Error searching crawler, try again in some minutes");
			// creates a empty file to fill with de data
			file = new File(projectDB.getUserFromProject(container.getIdProject()) + "/output.txt");
			if (file.exists())
				file.delete();
			file.createNewFile();
			// puts data in
			PrintWriter pw = new PrintWriter(new FileWriter(file));
            for (String result : results) {
                pw.println(result);
            }
			pw.flush();
			pw.close();

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			// "Content-Disposition : inline" will show viewable types [like
			// images/text/pdf/anything viewable by browser] right on browser
			// while others(zip e.g) will be directly downloaded [may provide
			// save as popup, based on your browser setting.]
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			// "Content-Disposition : attachment" will be directly download, may
			// provide save as popup, based on your browser setting
			// response.setHeader("Content-Disposition",
			// String.format("attachment; filename=\"%s\"", file.getName()));

			response.setContentLength((int) file.length());

			InputStream inputStream = new FileInputStream(file);

			// Copy bytes from source to destination(outputstream in this
			// example), closes both streams.
			FileCopyUtils.copy(inputStream, response.getOutputStream());
			inputStream.close();
		} catch (Exception a) {
			log.warn("Error downloading crawler: " + a.getMessage());
		}

	}

	/**
	 * searchs a query in information crawled Returns a message with the status
	 * of the action
	 */
	@RequestMapping(value = "/downloadAll", method = RequestMethod.GET)
	void downloadAll(@RequestParam(value = "idContainer") String idContainer, HttpServletResponse response) {
		// gets the last configuration of the project
		ContainerDocker container;
		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		try {
			container = containerDB.getContainerFromId(idContainer);
			ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
			// gets the last configuration of the project
			Configuration config = confDB.GetConfigurationFromProject(container.getIdProject());

			// this files have every content download (update on every index
			// action)
			File file = new File(container.getIdProject() + "_" + config.getId() + "_" + container.getIdImage() + "_"
					+ container.getId() + "_index/out.txt");

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			FileInputStream inputStream = new FileInputStream(file);

			response.setContentLength((int) file.length());

			// response header
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
			response.setHeader(headerKey, headerValue);

			response.setContentType(mimeType);

			// "Content-Disposition : attachment" will be directly download, may
			// provide save as popup, based on your browser setting
			// response.setHeader("Content-Disposition",
			// String.format("attachment; filename=\"%s\"", file.getName()));

			response.setContentLength((int) file.length());

			// Copy bytes from source to destination(outputstream in this
			// example), closes both streams.
			OutputStream outStream = response.getOutputStream();
			IOUtils.copy(inputStream, outStream);
			response.flushBuffer();
			inputStream.close();
		} catch (Exception e) {
			log.warn("Error writing file to output stream", e);
			throw new RuntimeException("IOError writing file to output stream");
		}
	}

	/**
	 * Starts the crawler of a container Returns a message with the status of
	 * the action
	 *//*
		 * @RequestMapping(value = "/stopCrawler", method = RequestMethod.POST)
		 * ResponseEntity<String> stopCrawler(@RequestParam(value =
		 * "idContainer") String idContainer) { // gets the last configuration
		 * of the project
		 * 
		 * ContainerDockerDatabase containerDB = new
		 * ContainerDockerDatabase(jdbcTemplate); try { ContainerDocker
		 * container = containerDB.getContainerFromId(idContainer);
		 * ConfigurationDatabase confDB = new
		 * ConfigurationDatabase(jdbcTemplate); // gets the last configuration
		 * of the project Configuration config =
		 * confDB.GetConfigurationFromProject(container.getIdProject()); String
		 * command = "java -jar ../butler.jar do stopCrawl --containerName " +
		 * container.getId() + " --imageName " + container.getIdImage() +
		 * " --idProject " + container.getIdProject() + "_" + config.getId();
		 * log.info("Command: " + command); BufferedReader out =
		 * ops.executeCommand(command, false); String lineOut = ""; String
		 * errorMessage = ""; boolean error = true; while ((lineOut =
		 * out.readLine()) != null) { errorMessage = lineOut; if
		 * ((lineOut.contains("Crawl stopped correctly"))) { error = false; } }
		 * if (error) { log.warn("Not valid container: " + errorMessage); throw
		 * new InternalError("Not valid container: " + errorMessage); }
		 * 
		 * } catch (Exception a) { throw new InternalError(
		 * "Error stopping crawler: " + a.getMessage()); }
		 * 
		 * return new ResponseEntity<>("Crawler stopped", HttpStatus.OK); }
		 */

	/**
	 * Starts the crawler of a container Returns a message with the status of
	 * the action
	 */
	@RequestMapping(value = "/stopContainer", method = RequestMethod.POST)
	ResponseEntity<String> stopContainer(@RequestParam(value = "idContainer") String idContainer) {
		// gets the last configuration of the project

		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		ContainerDocker container = containerDB.getContainerFromId(idContainer);
		ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
		// gets the last configuration of the project
		Configuration config = confDB.GetConfigurationFromProject(container.getIdProject());
		String command = "java -jar ../butler.jar do stopContainer --containerName " + container.getId()
				+ " --imageName " + container.getIdImage() + " --idProject " + container.getIdProject() + "_"
				+ config.getId();
		log.info("Command: " + command);
		ops.checkMessage(command, "Container stopped correctly", "Error stopping Container");
		return new ResponseEntity<>("Container stopped", HttpStatus.OK);
	}

	/**
	 * Starts the crawler of a container Returns a message with the status of
	 * the action
	 */
	@RequestMapping(value = "/pauseContainer", method = RequestMethod.POST)
	ResponseEntity<String> pauseContainer(@RequestParam(value = "idContainer") String idContainer) {
		// gets the last configuration of the project

		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		ContainerDocker container = containerDB.getContainerFromId(idContainer);
		ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
		// gets the last configuration of the project
		Configuration config = confDB.GetConfigurationFromProject(container.getIdProject());
		String command = "java -jar ../butler.jar do pauseContainer --containerName " + container.getId()
				+ " --imageName " + container.getIdImage() + " --idProject " + container.getIdProject() + "_"
				+ config.getId();
		log.info("Command: " + command);
		ops.checkMessage(command, "Container paused correctly", "Error pausing Container");
		return new ResponseEntity<>("Container paused", HttpStatus.OK);
	}

	

}
