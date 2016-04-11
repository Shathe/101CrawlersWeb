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

import dataBase.UserDatabase;
import models.User;
import ops.CommonOps;

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
		UserDatabase userDB = new UserDatabase();
		int mismoNick = userDB.getNumberUsersSameNick(user);

		int mismoEmail = userDB.getNumberUsersSameEmail(email);

		User usuario = null;
		if (user != null && psdw != null && email != null && !user.equals("") && !psdw.equals("") && !email.equals("")
				&& mismoNick <= 0 && mismoEmail <= 0) {
			// Buen contenido. Haces un hash de la contrasena al guardarla
			userDB.insertarUsuario(user, email, psdw);
			Long id = userDB.getIdFromEmail(email);
			usuario = new User(id, user, email, psdw);

		} else {
			// Hay error y se mira cual es.

			if (email == null || email.equals(""))
				error = "Fill the email out";
			if (psdw == null || psdw.equals(""))
				error = "Fill the password out";
			if (user == null || user.equals(""))
				error = "Fill the user out";
			if (mismoEmail > 0)
				error = "This email is already in use";
			if (mismoNick > 0)
				error = "This username is already in use";
			if (mismoNick > 0 && mismoEmail > 0)
				error = "Both username and email are already in use";

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
		if (user != null && psdw != null && !user.equals("") && !psdw.equals("")) {
			/*
			 * Obtienes la info de la bd
			 */

			UserDatabase userDB = new UserDatabase();
			mismoNick = userDB.getNumberUsersSameNick(user);

			if (mismoNick > 0)
				contrasena = userDB.getpswdFromUser(user);

			if (contrasena.equals(CommonOps.HashFunction(psdw)) && mismoNick > 0) {
				mensaje = "Logueado correctamente";
				// Mandar idUser
				mensaje = String.valueOf(userDB.getIdFromUser(user).longValue());
				log.info("Logged id: " + mensaje);

			} else {
				// Hay error y se mira cual es.

				if (!contrasena.equals(CommonOps.HashFunction(psdw)))
					error = "Incorrect password";
				if (mismoNick <= 0)
					error = "User doesn't exist";
				log.info(error);

				throw new ErrorResponse(error);
			}

		} else {
			// Hay error y se mira cual es.

			if (psdw == null || psdw.equals(""))
				error = "Fill the password";
			if (user == null || user.equals(""))
				error = "Fill the user";
			log.info(error);

			throw new ErrorResponse(error);

		}

		return mensaje;
	}

}
