package es.unizar.iaaa.crawler.butler.commands;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Stopping commands. This class contains every command which stops the crawling
 * system or a part of it
 */
@Component
public class StoppingCommands implements CommandMarker {

	private static final Logger LOGGER = Logger.getLogger(StoppingCommands.class);

	@Autowired
	private Operations ops;

	@CliAvailabilityIndicator({ "stopCrawl" })
	public boolean stopNutchAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "bye" })
	public boolean eAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "stopContainer" })
	public boolean stopContainerAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "deleteContainer" })
	public boolean deleteContainerAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "pauseContainer" })
	public boolean pauseContainerAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "deleteImage" })
	public boolean deleteImageAvailable() {
		// always available
		return true;
	}

	/**
	 * Stop the crawl in the docker container
	 */
	@CliCommand(value = "stopCrawl", help = "Stops the crawler int the docker container")
	public String stopNutch(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		// docker exec $idContainer kill -9 $(docker exec $idContainer
		// ps |
		// grep crawl | awk '{print $1;}')
		// docker exec $idContainer kill -9 $(docker exec $idContainer
		// ps |
		// grep sh | awk '{print $1;}')
		// docker exec $idContainer kill -9 $(docker exec $idContainer
		// ps |
		// grep java | awk '{print $1;}')
		String command = "docker exec  " + idContainer + " ps";
		String process;
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer)) {
			return "The container where it is trying to stop the crawl don't exist";
		}
		if (ops.containerRunning(idContainer)) {
			try (BufferedReader out = ops.executeCommand(command, false)) {
				while ((process = out.readLine()) != null) {
					// Para todos los procesos busca los procesos a eliminar
					if (process.contains("crawl") || process.contains("java") || process.contains("sh")) {
						LOGGER.info(process);
						command = " docker exec  " + idContainer + " kill -9 " + process.split(" ")[1];
						LOGGER.info(command);
						ops.executeCommand(command, true);
						LOGGER.info("Crawl stopped correctly " + idContainer);

						return "Crawl stopped correctly";

					}
				}
			} catch (IOException e) {
				LOGGER.warn("IOException: " + e.getMessage(), e);
				return "Docker container don't exist, please, try executing the start command";
			}
		}

		return "Docker container has to be running in order to be able to stop the crawl";
	}

	/**
	 * Pause the docker container
	 */
	@CliCommand(value = "pauseContainer", help = "Pause the docker Container")
	public String pauseContainer(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		String command = "docker pause " + idContainer;
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer)) {
			return "The container trying to pause don't exist";
		}
		if (!ops.containerRunning(idContainer)) {
			return "The container trying to pause is not up";
		}
		try {
			ops.executeCommand(command, true);

		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "Docker container don't exist, please, try executing the start command";
		}
		LOGGER.info("Container paused correctly " + idContainer);

		return "Container paused correctly";
	}

	/**
	 * Stop the docker container
	 */
	@CliCommand(value = "stopContainer", help = "stops the docker Container")
	public String stopContainer(
			@CliOption(key = {
					"time" }, specifiedDefaultValue = "1", help = "time in seconds (waiting until shutting down)") String time,
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		if (time == null)
			time = "1";
		// docker stop -t $tiempo $idContainer
		String command = "docker stop -t " + time + " " + idContainer;
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer)) {
			return "The container trying to stop don't exist";
		}
		if (!ops.containerRunning(idContainer)) {
			return "The container trying to stop is not up";
		}
		try {
			ops.executeCommand(command, true);

		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "Docker container don't exist, please, try executing the start command";
		}
		LOGGER.info("Container stopped correctly " + idContainer);

		return "Container stopped correctly";
	}

	/**
	 * delete the docker container
	 */
	@CliCommand(value = "deleteContainer", help = "deletes the docker Container")
	public String deleteContainer(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName,
			@CliOption(key = {
					"containerName" }, mandatory = true, help = "name of the container") final String containerName) {
		String idContainer = idProject + "_" + imageName + "_" + containerName;

		// docker stop -t $tiempo $idContainer
		String command = "docker rm -f " + idContainer;
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.containerExists(idContainer)) {
			return "The container trying to delete don't exist";
		}
		if (!ops.containerStopped(idContainer)) {
			return "The container has to be stopped in order to deleted it";
		}
		try {
			ops.executeCommand(command, true);
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "Docker container don't exist, please, try executing the start command";
		}
		LOGGER.info("Container deleted correctly " + idContainer);

		return "Container deleted correctly";
	}

	/**
	 * delete the docker image
	 */
	@CliCommand(value = "deleteImage", help = "deletes the docker Image")
	public String deleteImage(
			@CliOption(key = { "idProject" }, mandatory = true, help = "id of the idProject") final String idProject,
			@CliOption(key = { "imageName" }, mandatory = true, help = "name of the image") final String imageName) {
		String idImage = idProject + "_" + imageName;

		// docker stop -t $tiempo $idContainer
		String comando = "docker rmi -f" + idImage;
		if (!ops.dockerIsRunning()) {
			return "Docker is not running, please start it with sudo service docker start";
		}
		if (!ops.imageExists(idImage)) {
			return "The image don't exists";

		}
		if (ops.containerExists(idImage)) {
			return "The image couldn't be deleted due to a container of this image exists";
		}
		try {
			ops.executeCommand(comando, true);
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
			return "Docker image don't exist, please, try executing the start command";
		}
		LOGGER.info("Image delete correctly " + idImage);

		return "Image delete correctly";
	}

}