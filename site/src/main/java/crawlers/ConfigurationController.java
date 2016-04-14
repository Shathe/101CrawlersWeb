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
import dataBase.ProjectDatabase;
import errors.InternalError;
import models.Configuration;
import models.Project;

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
			throw new InternalError("Error getting configuration: "+a.getMessage());
		}

		return new ResponseEntity<Configuration>(configuration, HttpStatus.OK);
	}



	/**
	 * Returns the created project if it has been created, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/createConfiguration", method = RequestMethod.POST)
	ResponseEntity<Configuration> createProject(@RequestParam(value = "idProject") String idProject, @RequestParam(value = "pluginsPath") String pluginsPath,
			@RequestParam(value = "dslPath") String dslPath) {

		Configuration configuration = new Configuration(0,  pluginsPath,  dslPath,  idProject);
		ConfigurationDatabase configurationDB = new ConfigurationDatabase(jdbcTemplate);
		try {
			configurationDB.createConfiguration(configuration);
			log.info("created configuration " + configuration.getId());
			// Creates the project files (Not implemented)
			// CCreate docker image?
		} catch (Exception a) {
			throw new InternalError("Error creating: "+a.getMessage());
		}
		return new ResponseEntity<Configuration>(configuration, HttpStatus.OK);
	}

}
