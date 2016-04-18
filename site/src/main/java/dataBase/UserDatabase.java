/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ops.CommonOps;
/**
 * Users database operations
 * @author shathe
 *
 */
@Component
public class UserDatabase {

	JdbcTemplate jdbcTemplate;
	public UserDatabase (JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}
	
	/**
	 * Gets the number of users with a given nick/username
	 * @param user
	 * @return
	 */
	public int getNumberUsersSameNick(String user) {
		return this.jdbcTemplate.queryForObject("select count(*) from userCrawlers where nick = ?", Integer.class,
				user);
	}
	/**
	 * Gets the number of users with a given email
	 * @param user
	 * @return
	 */
	public int getNumberUsersSameEmail(String email) {
		return this.jdbcTemplate.queryForObject("select count(*) from userCrawlers where email = ?", Integer.class,
				email);
	}
	
	/**
	 * Get the password (encrypted) of a user
	 * @param user
	 * @return
	 */
	public String getpswdFromUser(String user) {
		return this.jdbcTemplate.queryForObject("select contrasena from userCrawlers where nick = ?",
				String.class, user);
	}
	
	/**
	 * Creates a new user
	 * @param user
	 * @param email
	 * @param pswd
	 */
	public void insertarUsuario(String user, String email,String pswd) {
		CommonOps ops= new CommonOps();
		this.jdbcTemplate.update("insert into userCrawlers (nick, email, contrasena) values (?,?, ?)", user, email,
				ops.HashFunction(pswd));
	}
	/**
	 * Gets the id from a email
	 * @param user
	 * @return
	 */
	public Long getIdFromEmail(String email) {
		return this.jdbcTemplate.queryForObject("select id from userCrawlers where email = ?", Long.class,
				email);
	}
	
	/**
	 * Gets the id from a username
	 * @param user
	 * @return
	 */
	public Long getIdFromUser(String user) {
		return this.jdbcTemplate.queryForObject("select id from userCrawlers where nick = ?",
				Long.class, user);
	}
	
}
