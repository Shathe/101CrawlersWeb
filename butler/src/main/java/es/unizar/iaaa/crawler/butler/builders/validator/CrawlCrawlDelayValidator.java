package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlCrawlDelayValidator implements Validator {

	@Override
	public ValidationResult validate(Configuration config) {
		try {
			/* Valida que sea un numero entero */
			
			if (config.getCrawlSystem().getMaxCrawlDelay() != null)
				Integer.valueOf(config.getCrawlSystem().getMaxCrawlDelay());
			return new LatestValidationResult();

		} catch (Exception a) {
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_DELAY,
					config.getCrawlSystem().getMaxCrawlDelay());
		}
	}

}
