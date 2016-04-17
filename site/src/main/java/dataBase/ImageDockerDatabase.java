/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import models.ImageDocker;

/**
 * Image database operations
 * 
 * @author shathe
 *
 */
@Component
public class ImageDockerDatabase {

	JdbcTemplate jdbcTemplate;

	public ImageDockerDatabase(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Lists the Docker images of a idProject
	 * 
	 * @param idProject
	 * @return
	 */
	public List<ImageDocker> getImages(String idProject) {
		return this.jdbcTemplate.query(
				"select * from imageCrawlers where idProject = " + idProject + " order by date asc",
				new ImageDockerMapper());
	}

	/**
	 * Deletes all the images of a project
	 * 
	 * @param idProject
	 * @return
	 */
	public int deleteImagesOfAProject(String idProject) {
		return this.jdbcTemplate.update("delete from  imageCrawlers where idProject = ?", idProject);
	}

	/**
	 * Deletes a image
	 * 
	 * @param image
	 * @return
	 */
	public int deleteImage(ImageDocker image) {
		return this.jdbcTemplate.update("delete from  imageCrawlers where id = ?", image.getId());
	}

	/**
	 * Updates a image
	 * 
	 * @param image
	 * @return
	 */
	public int updateImage(ImageDocker image) {
		return this.jdbcTemplate.update("update imageCrawlers set name = ?  where id = ?", image.getName(),
				image.getId());
	}

	/**
	 * Creates a new project
	 * 
	 * @param image
	 * @return
	 */
	public int createImage(ImageDocker image) {
		return this.jdbcTemplate.update(
				"insert into imageCrawlers (idProject, idConfiguration,name,date) values (?,?,?,?)",
				image.getIdProject(), image.getIdConfiguration(), image.getName(),
				new Date(System.currentTimeMillis()));

	}

	/**
	 * Gets the last created image
	 * 
	 * @param idProject
	 * @return
	 */
	public ImageDocker getImageJustCreated(String idProject) {
		//
		return this.jdbcTemplate.queryForObject(
				"select TOP 1 * from imageCrawlers where idProject = " + idProject + " ORDER BY id DESC",
				new ImageDockerMapper());
	}

}
