package ops;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import dataBase.ConfigurationDatabase;
import dataBase.ContainerDockerDatabase;
import dataBase.ImageDockerDatabase;
import dataBase.ProjectDatabase;
import errors.InternalError;
import models.Configuration;
import models.ContainerDocker;
import models.ImageDocker;
import models.Project;

public class CommonOps {
	private static final Logger log = LoggerFactory.getLogger(CommonOps.class);

	/*
	 * Devuelve el hash de una palabra
	 */
	public String HashFunction(String word) {
		String hash = word;
		try {
			MessageDigest hasher = MessageDigest.getInstance("MD5");
			hasher.reset();
			hasher.update(word.getBytes());
			byte[] digest = hasher.digest();
			hash = new String(digest, StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			hash = String.valueOf(hash.hashCode());
		}
		return hash;
	}

	/**
	 * Executes a command it the OS system shell. if the print flag is
	 * activated, it shows the output in the console, it it's not, it will
	 * return a bufferedReader with the output.
	 */
	public BufferedReader executeCommand(String comando, boolean print) throws IOException {
		String s;
		Process p = Runtime.getRuntime().exec(comando);

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		// Read command error output
		if (print) {
			while ((s = stdInput.readLine()) != null) {
				if (!(s.contains("WARNING: Error loading config") || s.equals(""))) {
					System.out.println("[Docker] " + s);
					log.info(s);
				}
			}
		}
		while ((s = stdError.readLine()) != null) {
			if (!(s.contains("WARNING: Error loading config") || s.equals(""))) {
				log.warn(s);
			}
		}
		return stdInput;
	}

	/**
	 * Delete everything linked to a list of containers, docker containers and
	 * from the DB
	 * 
	 * @param containers
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public void deleteContainers(List<ContainerDocker> containers, JdbcTemplate jdbcTemplate) throws Exception {
		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);

		for (ContainerDocker container : containers) {
			Configuration config = confDB.GetConfigurationFromProject(container.getIdProject());
			containerDB.deleteContainer(container);
			log.info("deleted container " + container.getId());
			try {
				// If there's any index folder, deletes it
				File folderProject = new File(container.getIdProject() + "_" + config.getId() + "_"
						+ container.getIdImage() + "_"+ container.getId()  + "_index");
				FileUtils.deleteDirectory(folderProject);
			} catch (Exception a) {
				log.warn("Exception: " + a.getMessage(), a);
			}
			// containers cant be stopped if the are paused, first, unpaussed
			String command = "java -jar ../butler.jar do start --containerName " + container.getId() + " --imageName "
					+ container.getIdImage() + " --idProject " + container.getIdProject() + "_" + config.getId();
			// stops the container and delete it
			executeCommand(command, true);
			command = "java -jar ../butler.jar do stopContainer --containerName " + container.getId() + " --imageName "
					+ container.getIdImage() + " --idProject " + container.getIdProject() + "_" + config.getId();
			// stops the container and delete it
			executeCommand(command, true);
			command = "java -jar ../butler.jar do deleteContainer --containerName " + container.getId()
					+ " --imageName " + container.getIdImage() + " --idProject " + container.getIdProject() + "_"
					+ config.getId();
			executeCommand(command, true);

		}

	}

	/**
	 * Delete everything linked to a list of images, docker images and
	 * containers and from the DB
	 * 
	 * @param images
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public void deleteImages(List<ImageDocker> images, JdbcTemplate jdbcTemplate) throws Exception {
		ImageDockerDatabase imageDB = new ImageDockerDatabase(jdbcTemplate);
		ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);

		for (ImageDocker image : images) {
			Configuration config = confDB.GetConfigurationFromProject(image.getIdProject());
			imageDB.deleteImage(image);
			log.info("deleted image " + image.getId());
			try {
				// Deletes the building files
				File folderProject = new File(image.getIdProject() + "_" + config.getId());
				FileUtils.deleteDirectory(folderProject);
			} catch (Exception a) {
				log.warn("Exception: " + a.getMessage(), a);

			}
			// delete containers from DB and Docker
			ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
			List<ContainerDocker> containers = containerDB.getContainers(String.valueOf(image.getId()));
			deleteContainers(containers, jdbcTemplate);
			containerDB.deleteContainersOfAImage(String.valueOf(image.getId()));
			String command = "java -jar ../butler.jar do deleteImage --imageName " + image.getId() + " --idProject "
					+ image.getIdProject() + "_" + config.getId();
			// delete image form docker
			executeCommand(command, true);
		}

	}

	/**
	 * Delete everything linked to a project, files, configurations, docker
	 * images and containers and from the DB
	 * 
	 * @param project
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public void deleteProject(Project project, JdbcTemplate jdbcTemplate) throws Exception {
		ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
		ImageDockerDatabase imageDB = new ImageDockerDatabase(jdbcTemplate);
		ProjectDatabase projectDB = new ProjectDatabase(jdbcTemplate);
		// delete its images
		List<ImageDocker> images = imageDB.getImages(String.valueOf(project.getId()));
		deleteImages(images, jdbcTemplate);
		
		// delete its configurations
		List<Configuration> configurations = confDB.GetConfigurationsFromProject(String.valueOf(project.getId()));
		for (Configuration configuration : configurations) {
			confDB.deleteConfiguration(configuration);
			log.info("deleted configuration " + configuration.getId());
		}

		
		projectDB.deleteProject(project);

	}

	public void checkMessage(String command, String message, String exceptionMessage) {
		try (BufferedReader out = executeCommand(command, false)) {
			String lineOut;
			String errorMessage = "";
			boolean error = true;
			while ((lineOut = out.readLine()) != null) {
				errorMessage = lineOut;
				if ((lineOut.contains(message))) {
					error = false;
				}
			}
			if (error) {
				log.warn("Not valid container/image: " + errorMessage);
				throw new InternalError("Not valid container/image: " + errorMessage);
			}
		} catch (IOException e) {
			
			throw new InternalError(exceptionMessage + ": " + e.getMessage());
		}
	}
}
