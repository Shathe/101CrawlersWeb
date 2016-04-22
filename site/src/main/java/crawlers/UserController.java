package crawlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dataBase.UserDatabase;
import errors.InternalError;
import models.User;
import ops.CommonOps;

/**
 * Controller for users. Manage every operation which deals with the users.
 * @author shathe
 *
 */
@RestController
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	JdbcTemplate jdbcTemplate;

	CommonOps ops= new CommonOps();

	
	/**
	 * Register the user in the data base if there is no user with the same email or username already
	 * registered.
	 * In the case the user is not registered, it generates an error response.
	 * @param user
	 * @param psdw
	 * @param email
	 */
	@RequestMapping(value = "/registro", method = RequestMethod.POST)
	User registro(@RequestParam(value = "user") String user, @RequestParam(value = "pass") String psdw,
			@RequestParam(value = "email") String email) {
	
		UserDatabase userDB = new UserDatabase(jdbcTemplate);
		String error = "";
		log.info("Petición registro");
		int mismoNick = userDB.getNumberUsersSameNick(user);

		int mismoEmail = userDB.getNumberUsersSameEmail(email);

		User newUser;
		if (user != null && psdw != null && email != null && !user.equals("") && !psdw.equals("") && !email.equals("")
				&& mismoNick <= 0 && mismoEmail <= 0) {
			// Everything OK. Hash the password
			userDB.insertarUsuario(user, email, psdw);
			Long id = userDB.getIdFromEmail(email);
			newUser = new User(id, user, email, psdw);

		} else {
			// There's an error

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

			throw new InternalError(error);

		}
		return newUser;
	}

	/**
	 * Check if the user is registered in the database.
	 * If the user is registered and the password is ok, it returns the UserId,
	 * if not, it returns an error response
	 * @param user
	 * @param psdw
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	String login(@RequestParam(value = "user") String user, @RequestParam(value = "pass") String psdw) {

		UserDatabase userDB = new UserDatabase(jdbcTemplate);

		String message;
		String error = "";
		String password = "";
		int mismoNick;
		if (user != null && psdw != null && !user.equals("") && !psdw.equals("")) {
			
			mismoNick = userDB.getNumberUsersSameNick(user);

			if (mismoNick > 0)
				password = userDB.getpswdFromUser(user);

			if (password.equals(ops.HashFunction(psdw)) && mismoNick > 0) {
				//Everything OK
				
				message = String.valueOf(userDB.getIdFromUser(user).longValue());
				log.info("Logged id: " + message);

			} else {
				// Error (something is wrong)

				if (!password.equals(ops.HashFunction(psdw)))
					error = "Incorrect password";
				if (mismoNick <= 0)
					error = "User doesn't exist";
				log.info(error);

				throw new InternalError(error);
			}

		} else {
			//Error (something is empty)

			if (psdw == null || psdw.equals(""))
				error = "Fill the password";
			if (user == null || user.equals(""))
				error = "Fill the user";
			log.info(error);

			throw new InternalError(error);

		}

		return message;
	}

}
