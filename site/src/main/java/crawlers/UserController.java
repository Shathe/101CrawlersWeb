package crawlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	@Autowired
	JdbcTemplate jdbcTemplate;

	/*
	 * Registra a un usuario si no existen usuarios con el mismo email o nombre
	 * ya en la base de datos Si se inserta correctamente devuelve un objeto
	 * usuario con su información, sino, devuelve un error con un mensaje
	 * informativo de este.
	 */
	@RequestMapping(value = "/registro", method = RequestMethod.POST)
	User registro(@RequestParam(value = "user") String user, @RequestParam(value = "pass") String psdw,
			@RequestParam(value = "email") String email) {
		/*
		 * Si se puede registrar se crea el ususario y se devuelve, sino, se
		 * devuelve null
		 */
		String error = "";
		log.info("Petición registro");
		int mismoNick = this.jdbcTemplate.queryForObject("select count(*) from userCrawlers where nick = ?",
				Integer.class, user);

		int mismoEmail = this.jdbcTemplate.queryForObject("select count(*) from userCrawlers where email = ?",
				Integer.class, email);

		User usuario = null;
		if (user != null && psdw != null && email != null && !user.equals("") && !psdw.equals("") && !email.equals("")
				&& mismoNick <= 0 && mismoEmail <= 0) {
			// Buen contenido. Haces un hash de la contrasena al guardarla
			this.jdbcTemplate.update("insert into userCrawlers (nick, email, contrasena) values (?,?, ?)", user, email,
					CommonOps.HashFunction(psdw));
			Long id = this.jdbcTemplate.queryForObject("select id from userCrawlers where email = ?", Long.class,
					email);
			usuario = new User(id, user, email, psdw);

		} else {
			// Hay error y se mira cual es.

			if (email == null || email.equals(""))
				error = "Rellene el email";
			if (psdw == null || psdw.equals(""))
				error = "Rellene la contraseña";
			if (user == null || user.equals(""))
				error = "Rellene el usuario";
			if (mismoEmail > 0)
				error = "Este email ya esá en uso";
			if (mismoNick > 0)
				error = "Este nombre de usuario ya está en uso";
			if (mismoNick > 0 && mismoNick > 0)
				error = "Este nombre de usuario y email ya están en uso";

			throw new ErrorResponse(error);

		}
		return usuario;
	}

	/*
	 * Registra a un usuario si no existen usuarios con el mismo email o nombre
	 * ya en la base de datos Si se inserta correctamente devuelve un mensaje de
	 * acepación, sino, devuelve un error con un mensaje informativo de este.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	String login(@RequestParam(value = "user") String user, @RequestParam(value = "pass") String psdw) {
		/*
		 * Si se puede loguear se crea el ususario y se devuelve, sino, se
		 * devuelve null
		 */
		String mensaje = "";
		String error = "";
		String contrasena = "";
		int mismoNick = 1;
		log.info("Petición login");
		if (user != null && psdw != null && !user.equals("") && !psdw.equals("")) {
			/*
			 * Obtienes la info de la bd
			 */
			mismoNick = this.jdbcTemplate.queryForObject("select count(*) from userCrawlers where nick = ?",
					Integer.class, user);
			if (mismoNick > 0)
				contrasena = this.jdbcTemplate.queryForObject("select contrasena from userCrawlers where nick = ?",
						String.class, user);
			
			if ( contrasena.equals(CommonOps.HashFunction(psdw)) && mismoNick > 0) {
				mensaje = "Logueado correctamente";
				log.info("Bien logueado");

			}else {
				// Hay error y se mira cual es.
				log.info("Mala contraseña o no existe usuario");

				if (!contrasena.equals(CommonOps.HashFunction(psdw)))
					error = "contraseña incorrecta";
				if (mismoNick <= 0)
					error = "El usuario no existe";
				throw new ErrorResponse(error);
			}
			
		} else {
			// Hay error y se mira cual es.

			if (psdw == null || psdw.equals(""))
				error = "Rellene la contraseña";
			if (user == null || user.equals(""))
				error = "Rellene el usuario";
			throw new ErrorResponse(error);

		}

		return mensaje;
	}

}
