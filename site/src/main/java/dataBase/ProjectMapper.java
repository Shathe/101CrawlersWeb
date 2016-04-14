/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import models.Project;
/**
 * Mapper for the project
 * @author shathe
 *
 */
public final class ProjectMapper implements RowMapper<Project> {

	/**
	 * Maps a resultSet with a new Project instance
	 */
	public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
		Project project = new Project();
		project.setId(rs.getLong("id"));
		project.setIdUser(rs.getString("idUser"));
		project.setName(rs.getString("name"));
		project.setDate(rs.getDate("date"));
		return project;
	}
}        

