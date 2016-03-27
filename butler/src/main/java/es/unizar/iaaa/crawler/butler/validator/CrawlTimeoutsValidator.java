/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl timeouts is well formed
 * in this case it's well formed if they are  integer number s
 */
public class CrawlTimeoutsValidator implements Validator {

	@Override
	public ValidationResult validate(CrawlConfiguration config) {
		try {
			if (config.getCrawlSystem().getTimeouts() != null) {
			/* Validates whether they are integer numbers */
				if (config.getCrawlSystem().getTimeouts().getFetchTimes() != null)
					Integer.valueOf(config.getCrawlSystem().getTimeouts().getFetchTimes());
				if (config.getCrawlSystem().getTimeouts().getNetwork() != null)
					Integer.valueOf(config.getCrawlSystem().getTimeouts().getNetwork());
				if (config.getCrawlSystem().getTimeouts().getParser() != null)
					Integer.valueOf(config.getCrawlSystem().getTimeouts().getParser());
			}
			return new LatestValidationResult();


        } catch (Exception a) {
            return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_TIMEOUTS,
            		config.getCrawlSystem().getTimeouts());
        }
    }

}
