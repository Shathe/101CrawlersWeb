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

	private String idProject;

	public Configuration(long id,  String idProject) {
		this.id = id;
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

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	@Override
	public String toString() {
		return String.format("Project [id=%d]", id);
	}

}
