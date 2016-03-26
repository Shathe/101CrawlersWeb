package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlVersionValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getVersion();
        return validateStringValue(value, "1.9", Status.ERROR_UNSUPPORTED_CRAWL_VERSION);
    }

}
