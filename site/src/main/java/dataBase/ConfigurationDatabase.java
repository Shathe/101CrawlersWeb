/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import models.Configuration;

/**
 * Configuration database operations
 * 
 * @author shathe
 *
 */
@Component
public class ConfigurationDatabase {

	JdbcTemplate jdbcTemplate;

	public ConfigurationDatabase(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Returns the last configuration of a project
	 * 
	 * @param idProject
	 * @return
	 */
	public Configuration GetConfigurationFromProject(String idProject) {
		//
		return this.jdbcTemplate.queryForObject(
				"select TOP 1 * from configurationCrawlers where idProject = " + idProject + " ORDER BY id DESC",
				new ConfigurationMapper());
	}
	/**
	 * Returns a list of configurations of a project
	 * 
	 * @param idProject
	 * @return
	 */

	public List<Configuration> GetConfigurationsFromProject(String idProject) {
		return this.jdbcTemplate.query(
				"select * from configurationCrawlers where idProject = " + idProject ,
				new ConfigurationMapper());
	}

	/**
	 * Deletes a configuration
	 * 
	 * @param configuration
	 * @return
	 */
	public int deleteConfiguration(Configuration configuration) {
		return this.jdbcTemplate.update("delete from  configurationCrawlers where id = ?", configuration.getId());
	}

	/**
	 * Deletes every configuration of a project
	 * 
	 * @param idProject
	 * @return
	 */
	public int deleteConfigurationsOfProject(String idProject) {
		return this.jdbcTemplate.update("delete from  configurationCrawlers where idProject = ?", idProject);
	}

	/**
	 * Creates a new configuration
	 * 
	 * @param idProject
	 * @return
	 */
	public int createConfiguration(Configuration configuration) {
		return this.jdbcTemplate.update("insert into configurationCrawlers (idProject) values (?)",
				configuration.getIdProject());

	}
}
