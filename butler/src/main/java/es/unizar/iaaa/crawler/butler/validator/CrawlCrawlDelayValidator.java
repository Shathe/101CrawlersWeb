package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

// TODO @Iñigo Document me!
public class CrawlCrawlDelayValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getMaxCrawlDelay();
        return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_DELAY, false);
    }
}
