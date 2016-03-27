package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

// TODO @Iñigo Document me!
public class CrawlMaxFileValidator implements Validator {

	@Override
	public ValidationResult validate(CrawlConfiguration config) {
		String value = config.getCrawlSystem().getMaxFileLength();
		return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_FILE_LENGTH, false);
	}

}
