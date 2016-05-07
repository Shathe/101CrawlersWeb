/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package crawlers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dataBase.ConfigurationDatabase;
import errors.BadRequestError;
import errors.InternalError;
import models.Configuration;
import ops.CommonOps;

/**
 * Controller for projects. Manage every operation which deals with the
 * projects.
 * 
 * @author shathe
 */
@RestController
public class ConfigurationController {
	private static final Logger log = LoggerFactory.getLogger(ConfigurationController.class);
	@Autowired
	JdbcTemplate jdbcTemplate;
	CommonOps ops = new CommonOps();

	/**
	 * Returns the projects of a specified user
	 */
	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	ResponseEntity<Configuration> configuration(@RequestParam(value = "idProject") String idProject) {

		ConfigurationDatabase configDB = new ConfigurationDatabase(jdbcTemplate);

		log.info("getting configuration of " + idProject);
		Configuration configuration;
		try {
			configuration = configDB.GetConfigurationFromProject(idProject);
		} catch (Exception a) {
			log.warn("Exception: " + a.getMessage(), a);
			throw new InternalError("Error getting configuration: " + a.getMessage());
		}

		return new ResponseEntity<>(configuration, HttpStatus.OK);
	}

	/**
	 * Returns the created project if it has been created, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/createConfiguration", method = RequestMethod.POST)
	ResponseEntity<Configuration> createConfiguration(@RequestParam(value = "idProject") String idProject) {

		Configuration configuration = new Configuration(0, idProject);
		ConfigurationDatabase configurationDB = new ConfigurationDatabase(jdbcTemplate);
		try {
			configurationDB.createConfiguration(configuration);
			log.info("created configuration " + configuration.getId());
			configuration = configurationDB.GetConfigurationFromProject(idProject);
		
		} catch (Exception a) {
			log.warn("Exception: " + a.getMessage(), a);
			throw new InternalError("Error creating: " + a.getMessage());
		}
		return new ResponseEntity<>(configuration, HttpStatus.OK);
	}

	/**
	 * Uploads the dsl file of a user to an auxiliar folder, if it's not upload
	 * returns an error
	 * 
	 * @param file
	 * @param idUser
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/uploadDSL")
	public ResponseEntity<String> handleFileUploadDSL(@RequestParam("dsl") MultipartFile file,
			@RequestParam("idUser") String idUser) {

		// target DSL name
		String name = "dsl.yml";
		if (!file.isEmpty()) {
			try {
				// User folder
				File userFolder = new File(idUser);
				if (!userFolder.exists())
					Files.createDirectory(userFolder.toPath());
				// Auxiliar folder where the temporal configuration files are
				// copy
				String folderAux = idUser + "/aux";
				File folder = new File(folderAux);
				if (!folder.exists())
					Files.createDirectory(folder.toPath());
				// Copy DSL file
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(folderAux + "/" + name)));
				FileCopyUtils.copy(file.getInputStream(), stream);
				stream.close();
				log.info("DSL copied: " + idUser);
			} catch (Exception e) {
				log.warn("DSL not copied:" + e.getMessage());
				throw new InternalError("Error copying: " + e.getMessage());

			}
		} else {
			log.warn("DSL not copied, empty file: " + idUser);
			throw new BadRequestError("Empty file");
		}
		return new ResponseEntity<>("copied", HttpStatus.OK);
	}

	/**
	 * Uploads a list of files of a user, the plugin files to an auxiliar
	 * folder, if it's not upload returns an error
	 *
	 * @param name
	 * @param files
	 * @param idUser
	 */

	@RequestMapping(method = RequestMethod.POST, value = "/pluginForm")
	public ResponseEntity<String> PluginForm(@RequestParam("name") String name, @RequestParam("idUser") String idUser,
			@RequestParam("plugin") List<MultipartFile> files) {
		if (!files.isEmpty()) {
			try {
				// If there's no user folder, creates it
				File userFolder = new File(idUser);
				if (!userFolder.exists())
					Files.createDirectory(userFolder.toPath());
				// Auxiliar folder where the temporal configuration files are
				// copy
				String folderAux = idUser + "/aux";
				File folder = new File(folderAux);
				if (!folder.exists())
					Files.createDirectory(folder.toPath());
				// Plugins folder
				String pluginsfolder = folderAux + "/plugins";
				File folderPluginAll = new File(pluginsfolder);
				if (!folderPluginAll.exists())
					Files.createDirectory(folderPluginAll.toPath());
				// Plugin folder (in the plugins folder)
				File folderPlugin = new File(pluginsfolder + "/" + name);
				if (!folderPlugin.exists())
					Files.createDirectory(folderPlugin.toPath());
				// Copy every plugin file (plugin.xml and jars)
				for (MultipartFile file : files) {
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(new File(folderPlugin + "/" + file.getOriginalFilename())));
					FileCopyUtils.copy(file.getInputStream(), stream);
					stream.close();
				}
				log.info("Plugin copied: " + idUser);
			} catch (Exception e) {
				log.warn("Plugin not copied:" + e.getMessage());
				throw new InternalError("Error copying: " + e.getMessage());
			}
		} else {
			log.warn("There are no plugins to upload: " + idUser);
			throw new BadRequestError("There are no plugins to upload");
		}
		return new ResponseEntity<>("copied", HttpStatus.OK);
	}

	/**
	 * Deletes every file uploaded in the auxiliar folder of a user
	 * 
	 * @param idUser
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/resetUploadConfig")
	public ResponseEntity<String> resetUploadConfig(@RequestParam("idUser") String idUser) {

		try {
			// Deletes the auxiliar folder
			String folderAux = idUser + "/aux";
			File folder = new File(folderAux);
			FileUtils.deleteDirectory(folder);

			log.info("Auxuliar folder deleted: " + idUser);
		} catch (Exception e) {
			log.warn("Auxuliar folder deleted:" + e.getMessage());
			throw new InternalError("Error deleting auxiliar folder: " + e.getMessage());
		}
		return new ResponseEntity<>("deleted", HttpStatus.OK);
	}

	/**
	 * Saves the configuration files of the auxiliar folder to an static one
	 * 
	 * @param idUser
	 * @param idProject
	 * @param idConfig
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveConfigurationFiles")
	public ResponseEntity<String> saveConfigurationFiles(@RequestParam("idUser") String idUser,
			@RequestParam("idProject") String idProject, @RequestParam("idConfig") String idConfig) {

		try {
			// Rename auxiliar folder to IdProject/idConfiguration (1/2)
			// This files will be used by Butler builder
			String folderAux = idUser + "/aux";
			File folder = new File(folderAux);
			File folderProject = new File(idUser + "/" + idProject);
			if (!folderProject.exists())
				Files.createDirectory(folderProject.toPath());
			File folderConfig = new File(idUser + "/" + idProject + "/" + idConfig);
			// if exist delete it (old bad config)
			try {
				FileUtils.deleteDirectory(folderConfig);
			} catch (Exception e) {
				log.warn("Old config folder not deleted:" + e.getMessage());
			}

			folder.renameTo(folderConfig);
			log.info("Configuration saved: " + idUser);
			// Now you have to validate the project
			String command = "java -jar ../butler.jar do config --file " + folderConfig.getPath()
					+ "/dsl.yml --idProject " + idProject + "_" + idConfig;

			log.info("Command: " + command);
			BufferedReader out = ops.executeCommand(command, false);
			String lineOut;
			String errorMessage = "";
			boolean error = true;
			while ((lineOut = out.readLine()) != null) {
				errorMessage = lineOut;
				if ((lineOut.contains("successfully"))) {
					error = false;
				}
			}
			// if its not valid, throw an error
			if (error) {
				log.warn("Not valid DSL: " + errorMessage);
				throw new InternalError("Not valid DSL: " + errorMessage);
			}

		} catch (Exception e) {
			log.warn("Configuration not saved:" + e.getMessage());
			throw new InternalError("Error saving: " + e.getMessage());
		}
		return new ResponseEntity<>("saved", HttpStatus.OK);
	}

}
