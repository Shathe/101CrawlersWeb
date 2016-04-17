/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import models.ContainerDocker;
/**
 * Mapper for the Docker image
 * @author shathe
 *
 */
public final class ContainerDockerMapper implements RowMapper<ContainerDocker> {

	/**
	 * Maps a resultSet with a new DockerImage instance
	 */
	public ContainerDocker mapRow(ResultSet rs, int rowNum) throws SQLException {
		ContainerDocker container = new ContainerDocker();
		container.setId(rs.getLong("id"));
		container.setIdProject(rs.getString("idProject"));
		container.setIdImage(rs.getString("idImage"));
		container.setName(rs.getString("name"));
		container.setDate(rs.getDate("date"));
		return container;
	}
}        

