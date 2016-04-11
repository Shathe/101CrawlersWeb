package crawlers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dataBase.ProjectDatabase;
import dataBase.ProjectMapper;
import models.Project;
import models.User;
import ops.CommonOps;

@RestController
public class ProjectController {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	@Autowired
	JdbcTemplate jdbcTemplate;


	/*
	 * Returns the projects of a specified user
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.GET/*, produces = { "application/json" }*/)
	ResponseEntity<List<Project>> listProjects(@RequestParam(value = "idUser") String idUser) {
		/*
		 * Si se puede loguear se crea el ususario y se devuelve, sino, se
		 * devuelve null
		 */
		log.info("ListProjects " + idUser);
		ProjectDatabase projectDB= new ProjectDatabase();
		List<Project> projects = projectDB.getProjects(idUser);

		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}

}
