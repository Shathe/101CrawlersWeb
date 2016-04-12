/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model of the Docker container
 * @author shathe
 *
 */
@Entity
public class ContainerDocker {
	@Id
    @GeneratedValue
    private long id;

	private String name, idImage, status,date;
	

    public ContainerDocker (long id, String name, String idImage, String status) {
        this.id = id;
        this.idImage = idImage;
        this.name = name;        
        this.status = status;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	@Override
    public String toString() {
        return String.format(
                "User [id=%d, user='%s', status='%s, idImage='%s']",
                id, name,status, idImage);
    }


}
