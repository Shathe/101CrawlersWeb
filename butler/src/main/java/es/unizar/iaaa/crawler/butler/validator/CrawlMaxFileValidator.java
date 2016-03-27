package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlMaxFileValidator implements Validator {

	@Override
	public ValidationResult validate(CrawlConfiguration config) {
		// Esta configuracion es opcional
		if(config.getCrawlSystem().getMaxFileLength()==null)
			return new LatestValidationResult();
		
		String value = config.getCrawlSystem().getMaxFileLength();
		return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_FILE_LENGTH);
	}

}
