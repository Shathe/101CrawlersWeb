/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package crawlers;

import java.util.Date;

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
		// tener una tabla para DSL que un proyecto eude tener varias
		// configuraciones?
		// enrealidad son del mismo proyecto, lo que cambia es laconfiguracion
		// y las imagenes dockers son de un proyecto y de una configuracion
		jdbcTemplate.execute(
				"CREATE TABLE configurationCrawlers(id SERIAL,idProject long,  dslPath VARCHAR(200),pluginsPath  VARCHAR(200))");

		jdbcTemplate.execute("CREATE TABLE imageCrawlers(id SERIAL, idProject long, idConfiguration long, name VARCHAR(100), date DATETIME)");

		
		
		
		jdbcTemplate.execute("CREATE TABLE pluginsCrawlers(id SERIAL, idProject long, idConfiguration long)");

		jdbcTemplate.execute("CREATE TABLE jarsCrawlers(id SERIAL, idPlugin long,path VARCHAR(200))");
		jdbcTemplate.execute("CREATE TABLE xmlCrawlers(id SERIAL, idPlugin long,path VARCHAR(200))");


		jdbcTemplate.execute(
				"CREATE TABLE containerCrawlers(" + "id SERIAL, idImage long, name VARCHAR(100), status VARCHAR(100))");

		
		//Inserts
		jdbcTemplate.update("insert into userCrawlers (nick, email, contrasena) values (?,?, ?)", "inigo",
				"inigol22zgz@gmail.com", CommonOps.HashFunction("contrasena"));

		jdbcTemplate.update("insert into projectCrawlers (idUser,name,date) values (?,?,?)", "1", "First project",
				new Date(System.currentTimeMillis()));

		jdbcTemplate.update("insert into configurationCrawlers (dslPath,pluginsPath,idProject) values (?,?,?)",
				"Vieja", "C://RutaLarga/lolo/jeje/plugins", 1);
		jdbcTemplate.update("insert into configurationCrawlers (dslPath,pluginsPath,idProject) values (?,?,?)",
				"Nueva", "C://RutaLarga/lolo/jeje/plugins", 1);
		jdbcTemplate.update("insert into imageCrawlers (idProject,idConfiguration,name,date) values (?,?,?,?)",
				1, 1, "Image :D First",new Date(System.currentTimeMillis()));

	}
}