package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlCrawlDelayValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
		// Esta configuracion es opcional
		if(config.getCrawlSystem().getMaxCrawlDelay()==null)
			return new LatestValidationResult();
		
        String value = config.getCrawlSystem().getMaxCrawlDelay();
        return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_DELAY);
    }
}
