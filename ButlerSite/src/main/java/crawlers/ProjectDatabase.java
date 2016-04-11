package crawlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import dataBase.ProjectMapper;
import models.Project;

@Component
public class ProjectDatabase {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Project> getProjects(String idUser) {
		return this.jdbcTemplate.query(
				"select id, idUser, name, dslPath,date from projectCrawlers where idUser = " + idUser,
				new ProjectMapper());
	}
}
