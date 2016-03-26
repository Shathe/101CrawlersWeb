package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlNameValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getName();
        return validateStringValue(value, "nutch", Status.ERROR_UNSUPPORTED_CRAWL_NAME);
    }

}
