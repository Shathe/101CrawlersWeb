package dataBase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import models.Project;

public class ProjectDatabase {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Project> getProjects(String idUser) {
		return this.jdbcTemplate.query(
				"select id, idUser, name, dslPath,date from projectCrawlers where idUser = " + idUser,
				new ProjectMapper());
	}
}
