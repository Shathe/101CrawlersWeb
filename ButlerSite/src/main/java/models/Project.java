package models;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Project {
	@Id
	@GeneratedValue
	private long id;

	private String name, dslPath, idUser;
	Date date;
	
	private ArrayList<String> jarsPath;

	public Project(long id, String name, String dslPath, String idUser, ArrayList<String> jarsPath) {
		this.id = id;
		this.name = name;
		this.idUser = idUser;
		this.dslPath = dslPath;
		this.jarsPath = jarsPath;
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

	public ArrayList<String> getJarsPath() {
		return jarsPath;
	}

	public void setJarsPath(ArrayList<String> jarsPath) {
		this.jarsPath = jarsPath;
	}

	@Override
	public String toString() {
		return String.format("Project [id=%d, name='%s', idUser='%s', dslPath='%s']", id, name, idUser, dslPath);
	}

}
