package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlQueueModeValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getQueueMode();
        return validateStringValue(value, "byhost|byip|bydomain", Status.ERROR_UNSUPPORTED_CRAWL_QUEUEMODE);
    }

}
