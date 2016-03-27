package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlRoundsValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getRounds();
        return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_ROUNDS);
    }

}
