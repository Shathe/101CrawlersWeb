/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import java.util.Date;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import models.Configuration;
import models.ContainerDocker;
import models.ImageDocker;
import models.Project;

/**
 * Docker database operations
 * 
 * @author shathe
 *
 */
@Component
public class ContainerDockerDatabase {

	JdbcTemplate jdbcTemplate;

	public ContainerDockerDatabase(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Lists the Docker containers of a image
	 * 
	 * @param idImage
	 */
	public List<ContainerDocker> getContainers(String idImage) {
		return this.jdbcTemplate.query(
				"select * from containerCrawlers where idImage = " + idImage + " order by date asc",
				new ContainerDockerMapper());
	}

	/**
	 * Deletes all the containers of a image
	 * 
	 * @param idImage
	 */
	public int deleteContainersOfAImage(String idImage) {
		return this.jdbcTemplate.update("delete from containerCrawlers where idImage = ?", idImage);
	}
	/**
	 * Deletes all the containers of a project
	 * 
	 * @param idProject
	 */
	public int deleteContainersOfAProject(String idProject) {
		return this.jdbcTemplate.update("delete from containerCrawlers where idProject = ?", idProject);
	}

	/**
	 * Deletes a containers
	 * 
	 * @param project
	 * @return
	 */
	public int deleteContainer(ContainerDocker container) {
		return this.jdbcTemplate.update("delete from  containerCrawlers where id = ?", container.getId());
	}

	/**
	 * Updates a containers
	 * 
	 * @param project
	 * @return
	 */
	public int updateContainer(ContainerDocker container) {
		return this.jdbcTemplate.update("update containerCrawlers set name = ?  where id = ?", container.getName(),
				container.getId());
	}

	/**
	 * Creates a new containers
	 * 
	 * @param project
	 * @return
	 */
	public int createContainer(ContainerDocker container) {
		return this.jdbcTemplate.update(
				"insert into containerCrawlers (idProject, idImage,name,date) values (?,?,?,?)",
				container.getIdProject(), container.getIdImage(), container.getName(),
				new Date(System.currentTimeMillis()));

	}

	/**
	 * Gets the last created containers
	 * 
	 * @param idImage
	 * @return
	 */
	public ContainerDocker getContainerJustCreated(String idImage) {
		//
		return this.jdbcTemplate.queryForObject(
				"select TOP 1 * from containerCrawlers where idImage = " + idImage + " ORDER BY id DESC",
				new ContainerDockerMapper());
	}

}
