/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import com.google.common.primitives.Ints;
import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO @Iñigo Documentar como Javadoc (recuerda /**)
public interface Validator {

	ValidationResult validate(CrawlConfiguration config);

	enum Status {
		OK, ERROR_UNSUPPORTED_OS_VERSION, ERROR_UNSUPPORTED_OS_NAME, ERROR_UNSUPPORTED_CRAWL_VERSION, ERROR_UNSUPPORTED_CRAWL_NAME, ERROR_UNSUPPORTED_CRAWL_ROUNDS, ERROR_UNSUPPORTED_CRAWL_SEEDS, ERROR_UNSUPPORTED_CRAWL_PLUGINS, ERROR_UNSUPPORTED_CRAWL_INFO, ERROR_UNSUPPORTED_CRAWL_TIMEOUTS, ERROR_UNSUPPORTED_CRAWL_QUEUEMODE, ERROR_UNSUPPORTED_CRAWL_FILE_LENGTH, ERROR_UNSUPPORTED_CRAWL_LIMIT_URL, ERROR_UNSUPPORTED_CRAWL_DELAY
	}

	default ValidationResult validateIntValue(String value, Status status) {
		return validateIntValue(value, status, true);
	}


	default ValidationResult validateIntValue(String value, Status status, boolean mandatory) {
			if ((value == null && !mandatory) || (Ints.tryParse(value) != null)) {
				return new LatestValidationResult();
			} else {
				return new LatestValidationResult(status, value);
			}
	}

	default ValidationResult validateStringValue(String value, String regex, Status status) {
		if (value == null) {
			return new LatestValidationResult(status, null);
		}
		Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(value);
		if (matcher.matches()) {
			return new LatestValidationResult();
		} else {
			return new LatestValidationResult(status, value);
		}
	}
}
