package ops;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonOps {

	/*
	 * Devuelve el hash de una palabra
	 */
	public static String HashFunction(String word) {
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
}
