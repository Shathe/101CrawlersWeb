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

	private String name, idUser;
	Date date;
	

	public Project(long id, String name, String idUser,Date date) {
		this.id = id;
		this.name = name;
		this.idUser = idUser;
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
	

	@Override
	public String toString() {
		return String.format("Project [id=%d, name='%s', idUser='%s', Configuration='%s']", id, name, idUser);
	}

}
