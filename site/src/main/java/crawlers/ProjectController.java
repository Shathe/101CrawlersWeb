/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package crawlers;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import errors.InternalError;
import models.Project;
import ops.CommonOps;

/**
 * Controller for projects. Manage every operation which deals with the
 * projects.
 * 
 * @author shathe
 */
@RestController
public class ProjectController {
	private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
	@Autowired
	JdbcTemplate jdbcTemplate;
	CommonOps ops = new CommonOps();

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
			throw new InternalError("Error listing projects: " + a.getMessage());
		}

		return new ResponseEntity<>(projects, HttpStatus.OK);
	}

	/**
	 * Returns the deleted project if it has been deleted, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/deleteProject", method = RequestMethod.DELETE)
	ResponseEntity<Project> deleteProject(@RequestBody Project project) {

		log.info("deleting project " + project.getId());
		try {
			ops.deleteProject(project, jdbcTemplate);
			log.info("deleted project " + project.getId());
			File folderProject = new File(project.getIdUser() + "/" + project.getId());
			FileUtils.deleteDirectory(folderProject);

		} catch (Exception a) {
			throw new InternalError("Error deleting: " + a.getMessage());
		}

		return new ResponseEntity<>(project, HttpStatus.OK);
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
			throw new InternalError("Error updating: " + a.getMessage());
		}

		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	/**
	 * Returns the created project if it has been created, if not, returns an
	 * error message
	 */
	@RequestMapping(value = "/createProject", method = RequestMethod.POST)
	ResponseEntity<Project> createProject(@RequestParam(value = "idUser") String idUser,
			@RequestParam(value = "name") String name) {

		Project project = new Project(0, name, idUser, new Date(System.currentTimeMillis()));
		ProjectDatabase projectDB = new ProjectDatabase(jdbcTemplate);
		try {
			projectDB.createProject(project);
			log.info("created project " + project.getId());
			project = projectDB.getProjectJustCreated(idUser);
			// Creates the project files (Not implemented)
			// CCreate docker image?
		} catch (Exception a) {
			throw new InternalError("Error creating: " + a.getMessage());
		}

		return new ResponseEntity<>(project, HttpStatus.OK);
	}

}
