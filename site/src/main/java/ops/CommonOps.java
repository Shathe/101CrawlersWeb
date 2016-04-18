package ops;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawlers.Application;

public class CommonOps {
	private static final Logger log = LoggerFactory.getLogger(CommonOps.class);

	/*
	 * Devuelve el hash de una palabra
	 */
	public  String HashFunction(String word) {
		String hash = word;
		try {
			MessageDigest hasher = MessageDigest.getInstance("MD5");
			hasher.reset();
			hasher.update(word.getBytes());
			byte[] digest = hasher.digest();
			hash=new String(digest, StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			hash = String.valueOf(hash.hashCode());
		}
		return hash;
	}

	/**
	 * Executes a command it the OS system shell. if the print flag is
	 * activated, it shows the output in the console, it it's not, it will
	 * return a bufferedReader with the output.
	 */
	public BufferedReader executeCommand(String comando, boolean print) throws IOException {
		String s;
		Process p = Runtime.getRuntime().exec(comando);

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		// Read command error output
		if (print) {
			while ((s = stdInput.readLine()) != null) {
				if (!(s.contains("WARNING: Error loading config") || s.equals(""))) {
					System.out.println("[Docker] " + s);
					log.info(s);
				}
			}
		}
		while ((s = stdError.readLine()) != null) {
			if (!(s.contains("WARNING: Error loading config") || s.equals(""))) {
				log.warn(s);
			}
		}
		return stdInput;
	}
}
