/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model of the Project
 * @author shathe
 *
 */
@Entity
public class Project {
	@Id
	@GeneratedValue
	private long id;

	private String name, dslPath, idUser,pluginsPath;
	Date date;
	

	public Project(long id, String name, String dslPath, String idUser, String pluginsPath,Date date) {
		this.id = id;
		this.name = name;
		this.idUser = idUser;
		this.dslPath = dslPath;
		this.pluginsPath = pluginsPath;
		this.date = date;
	}

	public Project() {

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

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public void setJarsPath(String pluginsPath) {
		this.pluginsPath = pluginsPath;
	}

	@Override
	public String toString() {
		return String.format("Project [id=%d, name='%s', idUser='%s', dslPath='%s']", id, name, idUser, dslPath);
	}

}
