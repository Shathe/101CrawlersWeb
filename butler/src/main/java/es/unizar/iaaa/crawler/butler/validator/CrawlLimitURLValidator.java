package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

// TODO @Iñigo Document me!
public class CrawlLimitURLValidator implements Validator {

	@Override
	public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getLinksLimitURL();
        return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_LIMIT_URL, false);
    }

}
