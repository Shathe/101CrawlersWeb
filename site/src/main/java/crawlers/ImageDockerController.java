/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package crawlers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dataBase.ConfigurationDatabase;
import dataBase.ImageDockerDatabase;
import dataBase.ProjectDatabase;
import errors.InternalError;
import models.Configuration;
import models.ImageDocker;
import models.Project;

/**
 * Controller for projects. Manage every operation which deals with the
 * projects.
 * 
 * @author shathe
 */
@RestController
public class ImageDockerController {
	private static final Logger log = LoggerFactory.getLogger(ImageDockerController.class);
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Returns the images of a specified project
	 */
	@RequestMapping(value = "/images", method = RequestMethod.GET)
	ResponseEntity<List<ImageDocker>> listImages(@RequestParam(value = "idProject") String idProject) {

		ImageDockerDatabase imageDB = new ImageDockerDatabase(jdbcTemplate);

		log.info("listImages " + idProject);
		List<ImageDocker> images;
		try {
			images = imageDB.getImages(idProject);
		} catch (Exception a) {
			throw new InternalError("Error listing projects: "+a.getMessage());
		}

		return new ResponseEntity<List<ImageDocker>>(images, HttpStatus.OK);
	}

	/**
	 * Returns the deleted image if it has been deleted, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)
	ResponseEntity<ImageDocker> deleteImage(@RequestBody ImageDocker image) {


		ImageDockerDatabase imageDB = new ImageDockerDatabase(jdbcTemplate);
		try {
			imageDB.deleteImage(image);
			log.info("deleted image " + image.getId());
			// FALTA ESTO
			// Deletes also the dockerImages, Containers.. (also in Docker stop+delete)
			// Delete the project files (dsl,plugins..) (Not implemented)

		} catch (Exception a) {
			throw new InternalError("Error deleting: "+a.getMessage());
		}

		return new ResponseEntity<ImageDocker>(image, HttpStatus.OK);
	}

	/**
	 * Returns the updated image if it has been updated, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/editImage", method = RequestMethod.POST)
	ResponseEntity<ImageDocker> editImage(@RequestBody ImageDocker image) {

		log.info("updating image " + image.getId());

		ImageDockerDatabase imageDB = new ImageDockerDatabase(jdbcTemplate);
		try {
			imageDB.updateImage(image);
			log.info("updated project " + image.getId());
			// Change the project files (Not implemented)

		} catch (Exception a) {
			throw new InternalError("Error updating: "+a.getMessage());
		}

		return new ResponseEntity<ImageDocker>(image, HttpStatus.OK);
	}

	/**
	 * Returns the created image if it has been created, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/createImage", method = RequestMethod.POST)
	ResponseEntity<ImageDocker> createImage(@RequestParam(value = "idProject") String idProject,
			@RequestParam(value = "name") String name) {
		ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
		//gets the last configuration of the project
		Configuration config=confDB.GetConfigurationFromProject(idProject);
		ImageDocker image = new ImageDocker(0, name,String.valueOf(config.getId()),idProject, new Date(System.currentTimeMillis()));
		ImageDockerDatabase imageDB = new ImageDockerDatabase(jdbcTemplate);
		try {
			imageDB.createProject(image);
			log.info("created image " + image.getName());
			image=imageDB.getImageJustCreated(idProject);
			// Creates the project files (Not implemented)
			// CCreate docker image?
		} catch (Exception a) {
			throw new InternalError("Error creating: "+a.getMessage());
		}

		return new ResponseEntity<ImageDocker>(image, HttpStatus.OK);
	}

}