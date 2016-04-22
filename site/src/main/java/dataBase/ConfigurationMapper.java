/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import models.Configuration;
/**
 * Mapper for the project
 * @author shathe
 *
 */
public final class ConfigurationMapper implements RowMapper<Configuration> {

	/**
	 * Maps a resultSet with a new Project instance
	 */
	public Configuration mapRow(ResultSet rs, int rowNum) throws SQLException {
		Configuration configuration = new Configuration();
		configuration.setId(rs.getLong("id"));
		configuration.setIdProject(rs.getString("idProject"));
		return configuration;
	}
}        

