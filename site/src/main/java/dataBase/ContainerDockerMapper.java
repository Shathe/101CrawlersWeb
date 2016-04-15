/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import models.ContainerDocker;
import models.ImageDocker;
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
		ContainerDocker contenedor = new ContainerDocker();
		contenedor.setId(rs.getLong("id"));
		contenedor.setIdProject(rs.getString("idProject"));
		contenedor.setIdImage(rs.getString("idImage"));
		contenedor.setName(rs.getString("name"));
		contenedor.setDate(rs.getDate("date"));
		return contenedor;
	}
}        

