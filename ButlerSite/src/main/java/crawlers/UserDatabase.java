package crawlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import models.Project;
import ops.CommonOps;

@Component
public class UserDatabase {

	@Autowired
	JdbcTemplate jdbcTemplate;

	
	
	public int getNumberUsersSameNick(String user) {
		return this.jdbcTemplate.queryForObject("select count(*) from userCrawlers where nick = ?", Integer.class,
				user);
	}

	public int getNumberUsersSameEmail(String email) {
		return this.jdbcTemplate.queryForObject("select count(*) from userCrawlers where email = ?", Integer.class,
				email);
	}
	public String getpswdFromUser(String user) {
		return this.jdbcTemplate.queryForObject("select contrasena from userCrawlers where nick = ?",
				String.class, user);
	}
	public void insertarUsuario(String user, String email,String pswd) {
		this.jdbcTemplate.update("insert into userCrawlers (nick, email, contrasena) values (?,?, ?)", user, email,
				CommonOps.HashFunction(pswd));
	}
	public Long getIdFromEmail(String email) {
		return this.jdbcTemplate.queryForObject("select id from userCrawlers where email = ?", Long.class,
				email);
	}
	
	public Long getIdFromUser(String user) {
		return this.jdbcTemplate.queryForObject("select id from userCrawlers where nick = ?",
				Long.class, user);
	}
	
}
