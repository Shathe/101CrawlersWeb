package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlLimitURLValidator implements Validator {

	@Override
	public ValidationResult validate(CrawlConfiguration config) {
		// Esta configuracion es opcional
		if(config.getCrawlSystem().getLinksLimitURL()==null)
			return new LatestValidationResult();
        String value = config.getCrawlSystem().getLinksLimitURL();
        return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_LIMIT_URL);
    }

}
