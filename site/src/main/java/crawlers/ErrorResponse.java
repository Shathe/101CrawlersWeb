package crawlers;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.BAD_REQUEST)  // 404
public class ErrorResponse extends RuntimeException {

	public ErrorResponse(String msg) {
		super (msg);
	}
	
}