/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model of the Project
 * @author shathe
 *
 */
@Entity
public class User {
	@Id
    @GeneratedValue
    private long id;

	private String username, email;
	
    @JsonIgnore
    private String contrasena;

    public User (long id, String username, String email, String ps) {
        this.id = id;
        this.username = username;
        this.email = email;        
        this.contrasena = ps;
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
    @Override
    public String toString() {
        return String.format(
                "username [id=%d, username='%s', email='%s']",
                id, username, email);
    }


}
