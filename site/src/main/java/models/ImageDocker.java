/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model of the Docker image
 * 
 * @author shathe
 *
 */
@Entity
public class ImageDocker {
	@Id
	@GeneratedValue
	private long id;

	private String name, idConfiguration, idProject;
	Date date;

	public ImageDocker() {
	}

	public ImageDocker(long id, String name, String idConfiguration, String idProject,Date date) {
		this.id = id;
		this.name = name;
		this.idConfiguration = idConfiguration;
		this.idProject = idProject;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIdConfiguration() {
		return idConfiguration;
	}

	public void setIdConfiguration(String idConfiguration) {
		this.idConfiguration = idConfiguration;
	}

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	@Override
	public String toString() {
		return String.format("User [id=%d, name='%s', IdConfiguration='%s']", id, name, idConfiguration);
	}

}
