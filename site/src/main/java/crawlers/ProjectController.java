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

import dataBase.ProjectDatabase;
import errors.ErrorInternal;
import models.Project;

/**
 * Controller for projects. Manage every operation which deals with the projects.
 * @author shathe
 */
@RestController
public class ProjectController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Returns the projects of a specified user
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	ResponseEntity<List<Project>> listProjects(@RequestParam(value = "idUser") String idUser) {

		ProjectDatabase projectDB = new ProjectDatabase(jdbcTemplate);

		log.info("ListProjects " + idUser);
		List<Project> projects;
		try {
			projects = projectDB.getProjects(idUser);
		} catch (Exception a) {
			throw new ErrorInternal("Error listing projects");
		}

		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}

	/**
	 * Returns the deleted project if it has been deleted, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/deleteProject", method = RequestMethod.DELETE)
	ResponseEntity<Project> deleteProject(@RequestBody Project project) {

		log.info("deleting project " + project.getId());

		ProjectDatabase projectDB = new ProjectDatabase(jdbcTemplate);
		try {
			projectDB.deleteProject(project);
			log.info("deleted project " + project.getId());

		} catch (Exception a) {
			throw new ErrorInternal("Error deleting");
		}

		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

	/**
	 * Returns the updated project if it has been updated, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/editProject", method = RequestMethod.POST)
	ResponseEntity<Project> editProject(@RequestBody Project project) {

		log.info("updating project " + project.getId());

		ProjectDatabase projectDB = new ProjectDatabase(jdbcTemplate);
		try {
			projectDB.updateProject(project);
			log.info("updated project " + project.getId());

		} catch (Exception a) {
			throw new ErrorInternal("Error updating");
		}

		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

	/**
	 * Returns the created project if it has been created, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/createProject", method = RequestMethod.POST)
	ResponseEntity<Project> createProject(@RequestParam(value = "idUser") String idUser,
			@RequestParam(value = "pluginsPath") String pluginsPath, @RequestParam(value = "name") String name,
			@RequestParam(value = "dslPath") String dslPath) {

		Project project = new Project(0, name, dslPath, idUser, pluginsPath, new Date(System.currentTimeMillis()));
		ProjectDatabase projectDB = new ProjectDatabase(jdbcTemplate);
		try {
			projectDB.createProject(project);
			log.info("created project " + project.getId());

		} catch (Exception a) {
			throw new ErrorInternal("Error creating");
		}

		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

}
