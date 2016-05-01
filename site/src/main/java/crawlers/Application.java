/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package crawlers;

import java.io.File;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ops.CommonOps;

/**
 * Server application
 */

@Component
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
		// Tabla de usuarios
		jdbcTemplate.execute("CREATE TABLE userCrawlers("
				+ "id SERIAL, nick VARCHAR(100), email VARCHAR(100), contrasena VARCHAR(1024))");

		jdbcTemplate
				.execute("CREATE TABLE projectCrawlers(" + "id SERIAL, idUser long, name VARCHAR(100), date DATETIME)");
		jdbcTemplate.execute("CREATE TABLE configurationCrawlers(id SERIAL,idProject long)");

		jdbcTemplate.execute(
				"CREATE TABLE imageCrawlers(id SERIAL, idProject long, idConfiguration long, name VARCHAR(100), date DATETIME)");

		jdbcTemplate.execute("CREATE TABLE containerCrawlers("
				+ "id SERIAL, idProject long,idImage long, name VARCHAR(100), date DATETIME)");


		

		CommonOps ops = new CommonOps();
		// Inserts
		jdbcTemplate.update("insert into userCrawlers (nick, email, contrasena) values (?,?, ?)", "inigo",
				"inigol22zgz@gmail.com", ops.HashFunction("contrasena"));

	}
}