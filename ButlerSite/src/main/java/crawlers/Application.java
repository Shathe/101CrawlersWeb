package crawlers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import ops.CommonOps;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String args[]) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {

		log.info("Creating tables");

		/* DATETIME???? */
		jdbcTemplate.execute("CREATE TABLE userCrawlers("
				+ "id SERIAL, nick VARCHAR(100), email VARCHAR(100), contrasena VARCHAR(1024))");

		jdbcTemplate.execute("CREATE TABLE projectCrawlers("
				+ "id SERIAL, idUser long, name VARCHAR(100), dslPath VARCHAR(200), date DATETIME)");

		jdbcTemplate.execute("CREATE TABLE jarCrawlers(" + "id SERIAL, idProject long)");

		jdbcTemplate.execute("CREATE TABLE imageCrawlers(" + "id SERIAL, dslId long, name VARCHAR(100))");

		jdbcTemplate.execute(
				"CREATE TABLE containerCrawlers(" + "id SERIAL, idImage long, name VARCHAR(100), status VARCHAR(100))");

		jdbcTemplate.update("insert into userCrawlers (nick, email, contrasena) values (?,?, ?)", "inigo",
				"inigol22zgz@gmailcom", CommonOps.HashFunction("contrasena"));
		java.util.Date date = new Date();
		jdbcTemplate.update("insert into projectCrawlers (idUser,name, dslPath,date) values (?,?,?,?)", "1",
				"primer proyecto", "C://RutaLarga/lolo/jeje/inigoool",new Date(System.currentTimeMillis()));

	}
}