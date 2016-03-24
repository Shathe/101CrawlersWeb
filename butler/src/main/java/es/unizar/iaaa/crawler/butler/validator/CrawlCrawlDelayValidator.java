package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlCrawlDelayValidator implements Validator {

	@Override
	public ValidationResult validate(CrawlConfiguration config) {
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
