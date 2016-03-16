package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlCrawlDelayValidator implements Validator {

<<<<<<< HEAD
	@Override
	public ValidationResult validate(Configuration config) {
		try {
			/* Valida que sea un numero entero */
			
			if (config.getCrawlSystem().getMaxCrawlDelay() != null)
				Integer.valueOf(config.getCrawlSystem().getMaxCrawlDelay());
			return new LatestValidationResult();
=======
    @Override
    public ValidationResult validate(Configuration config) {
        try {
            /* Valida que sea un numero entero */
            Integer.valueOf(config.getCrawlSystem().getMaxCrawlDelay());
            return new LatestValidationResult();
>>>>>>> 5a23b7e7125595cf928ede0f70b89763eea9ce0e

        } catch (Exception a) {
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_DELAY,
                    config.getCrawlSystem().getMaxCrawlDelay());
        }
    }

}
