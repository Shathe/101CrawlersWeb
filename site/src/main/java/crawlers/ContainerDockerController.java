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
import dataBase.ContainerDockerDatabase;
import dataBase.ProjectDatabase;
import errors.InternalError;
import models.Configuration;
import models.ContainerDocker;
import models.Project;

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

	/**
	 * Returns the containers of a specified image
	 */
	@RequestMapping(value = "/containers", method = RequestMethod.GET)
	ResponseEntity<List<ContainerDocker>> listImages(@RequestParam(value = "idImage") String idImage) {

		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);

		log.info("listContainers " + idImage);
		List<ContainerDocker> containers;
		try {
			containers = containerDB.getContainers(idImage);
		} catch (Exception a) {
			throw new InternalError("Error listing containers: " + a.getMessage());
		}

		return new ResponseEntity<>(containers, HttpStatus.OK);
	}

	/**
	 * Returns the deleted containers if it has been deleted, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/deleteContainer", method = RequestMethod.DELETE)
	ResponseEntity<ContainerDocker> deleteImage(@RequestBody ContainerDocker container) {

		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		try {
			containerDB.deleteContainer(container);
			log.info("deleted container " + container.getId());
			// FALTA ESTO
			// (also in Docker stop+delete)

		} catch (Exception a) {
			throw new InternalError("Error deleting: " + a.getMessage());
		}

		return new ResponseEntity<>(container, HttpStatus.OK);
	}

	/**
	 * Returns the updated containers if it has been updated, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/editContainer", method = RequestMethod.POST)
	ResponseEntity<ContainerDocker> editImage(@RequestBody ContainerDocker container) {

		log.info("updating containers " + container.getId());

		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		try {
			containerDB.updateContainer(container);
			log.info("updated container " + container.getId());

		} catch (Exception a) {
			throw new InternalError("Error updating: " + a.getMessage());
		}

		return new ResponseEntity<>(container, HttpStatus.OK);
	}

	/**
	 * Returns the created containers if it has been created, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/createContainer", method = RequestMethod.POST)
	ResponseEntity<ContainerDocker> createImage(@RequestParam(value = "idProject") String idProject,
			@RequestParam(value = "idImage") String idImage, @RequestParam(value = "name") String name) {
		// gets the last configuration of the project
		ContainerDocker container = new ContainerDocker(0, idImage, idProject, name,
				new Date(System.currentTimeMillis()));
		ContainerDockerDatabase containerDB = new ContainerDockerDatabase(jdbcTemplate);
		try {
			containerDB.createContainer(container);
			log.info("created container " + container.getName());
			container = containerDB.getContainerJustCreated(idImage);
			
			// CCreate docker container?
			// Recuerda que al ejecutar el jar -Imagename seria el ID en este
		} catch (Exception a) {
			throw new InternalError("Error creating: " + a.getMessage());
		}

		return new ResponseEntity<>(container, HttpStatus.OK);
	}

}
