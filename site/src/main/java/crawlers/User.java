package crawlers;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
	@Id
    @GeneratedValue
    private long id;

	private String user, email;
	
    @JsonIgnore
    private String contrasena;

    public User (long id, String user, String email, String ps) {
        this.id = id;
        this.user = user;
        this.email = email;        
        this.contrasena = ps;
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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
                "User [id=%d, user='%s', email='%s']",
                id, user, email);
    }


}
