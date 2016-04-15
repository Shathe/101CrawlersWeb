/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model of the Docker container
 * 
 * @author shathe
 *
 */
@Entity
public class ContainerDocker {
	@Id
	@GeneratedValue
	private long id;

	private String name, idImage, idProject;
	Date date;

	public ContainerDocker(long id, String idImage, String idProject, String name, Date date) {
		this.id = id;
		this.idImage = idImage;
		this.idProject = idProject;
		this.name = name;
		this.date = date;
	}

	public ContainerDocker() {
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

	public String getIdImage() {
		return idImage;
	}

	public void setIdImage(String idImage) {
		this.idImage = idImage;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	@Override
	public String toString() {
		return String.format("User [id=%d, user='%s',  idImage='%s']", id, name, idImage);
	}

}
