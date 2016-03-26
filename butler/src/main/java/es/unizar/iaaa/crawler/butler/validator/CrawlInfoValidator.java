package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlInfoValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getInfoCrawled();
        return validateStringValue(value, "text|html", Status.ERROR_UNSUPPORTED_CRAWL_INFO);
    }

}
