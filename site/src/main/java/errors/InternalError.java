/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package errors;

import org.springframework.web.bind.annotation.ResponseStatus;

import ops.CommonOps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author shathe
 *
 *         Default exceptions for errors
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalError extends RuntimeException {

	private static final Logger log = LoggerFactory.getLogger(CommonOps.class);

	public InternalError(String msg) {
		super(msg);
		log.warn(msg);

	}

}