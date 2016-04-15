/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import models.ImageDocker;
/**
 * Mapper for the Docker image
 * @author shathe
 *
 */
public final class ImageDockerMapper implements RowMapper<ImageDocker> {

	/**
	 * Maps a resultSet with a new DockerImage instance
	 */
	public ImageDocker mapRow(ResultSet rs, int rowNum) throws SQLException {
		ImageDocker image = new ImageDocker();
		image.setId(rs.getLong("id"));
		image.setIdProject(rs.getString("idProject"));
		image.setIdConfiguration(rs.getString("idConfiguration"));
		image.setName(rs.getString("name"));
		image.setDate(rs.getDate("date"));
		return image;
	}
}        

