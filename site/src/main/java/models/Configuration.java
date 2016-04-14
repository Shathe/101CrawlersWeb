/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model of the Configuration
 * 
 * @author shathe
 *
 */
@Entity
public class Configuration {
	@Id
	@GeneratedValue
	private long id;

	private String idProject, dslPath, pluginsPath;

	public Configuration(long id, String pluginsPath, String dslPath, String idProject) {
		this.id = id;
		this.dslPath = dslPath;
		this.pluginsPath = pluginsPath;
		this.idProject = idProject;
	}

	public Configuration() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDslPath() {
		return dslPath;
	}

	public void setDslPath(String dslPath) {
		this.dslPath = dslPath;
	}

	public String getPluginsPath() {
		return pluginsPath;
	}

	public void setPluginsPath(String pluginsPath) {
		this.pluginsPath = pluginsPath;
	}

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	@Override
	public String toString() {
		return String.format("Project [id=%d, pluginsPath='%s', dslPath='%s']", id, pluginsPath, dslPath);
	}

}
