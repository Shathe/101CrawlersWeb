/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import models.Project;

/**
 * Projects database operations
 * @author shathe
 *
 */
@Component
public class ProjectDatabase {

	JdbcTemplate jdbcTemplate;

	public ProjectDatabase(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Lists the projects of a user
	 * @param idUser
	 * @return
	 */
	public List<Project> getProjects(String idUser) {
		return this.jdbcTemplate.query("select * from projectCrawlers where idUser = " + idUser, new ProjectMapper());
	}
	
	/**
	 * Deletes a project
	 * @param project
	 * @return
	 */
	public int deleteProject(Project project) {
		return this.jdbcTemplate.update("delete from  projectCrawlers where id = ?", project.getId());
	}

	/**
	 * Updates a project
	 * @param project
	 * @return
	 */
	public int updateProject(Project project) {
		return this.jdbcTemplate.update("update projectCrawlers set name = ?  where id = ?", project.getName(),
				 project.getId());
	}

	/**
	 * Creates a new project
	 * @param project
	 * @return
	 */
	public int createProject(Project project) {
		return this.jdbcTemplate.update(
				"insert into projectCrawlers (idUser,name,date) values (?,?,?)", project.getIdUser(),
				project.getName(), 
				project.getDate());

	}
	/**
	 * Gets the last project created
	 * @param idUser
	 * @return
	 */
	public Project getProjectJustCreated(String idUser) {
		return this.jdbcTemplate.queryForObject(
				"select TOP 1 * from projectCrawlers where idUser = " + idUser + " ORDER BY id DESC",
				new ProjectMapper());
	}
	/**
	 * Gets the id from the user who owns this project
	 * @param id
	 * @return
	 */
	public String getUserFromProject(String id) {
		return this.jdbcTemplate.queryForObject("select idUser from projectCrawlers where id = " + id,  String.class);

	}
}
