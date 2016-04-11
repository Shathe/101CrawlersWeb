package dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import models.Project;

public final class ProjectMapper implements RowMapper<Project> {

	public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
		Project project = new Project();
		project.setId(rs.getLong("id"));
		project.setIdUser(rs.getString("idUSer"));
		project.setName(rs.getString("name"));
		project.setDslPath(rs.getString("dslPath"));
		project.setDate(rs.getDate("date"));
		return project;
	}
}        

