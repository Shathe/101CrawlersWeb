/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author shathe
 *
 *Default exceptions for errors
 */
@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.BAD_REQUEST)  // 404
public class BadRequestError extends RuntimeException {

	public BadRequestError(String msg) {
		super (msg);
	}
	
}