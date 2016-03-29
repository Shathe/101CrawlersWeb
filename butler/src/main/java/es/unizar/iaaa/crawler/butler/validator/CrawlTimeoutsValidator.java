/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.validator.Validator.Status;

/**
 * Validates if the Crawl timeouts is well formed in this case it's well formed
 * if they are integer number s
 */
public class CrawlTimeoutsValidator implements Validator {

	@Override
	public ValidationResult validate(CrawlConfiguration config) {
		ArrayList<ValidationResult> timeouts = new ArrayList<ValidationResult>();
		if (config.getCrawlSystem().getTimeouts() != null) {
			// Validates whether they are integer numbers
			String value = config.getCrawlSystem().getTimeouts().getFetchTimes();
			timeouts.add(validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_TIMEOUTS, false));
			value = config.getCrawlSystem().getTimeouts().getNetwork();
			timeouts.add(validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_TIMEOUTS, false));
			value = config.getCrawlSystem().getTimeouts().getParser();
			timeouts.add(validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_TIMEOUTS, false));
			for (int i = 0; i < timeouts.size(); i++) {
				// If any timeout validator Result is not ok, returns it
				if (!timeouts.get(i).isOk())
					return timeouts.get(i);
			}
		}
		// If theres no timeouts or all of them are OK
		return new LatestValidationResult();

	}

}
