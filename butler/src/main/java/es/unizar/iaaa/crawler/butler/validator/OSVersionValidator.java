package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class OSVersionValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getDockerOS().getVersion();
        return validateStringValue(value, "14.04", Status.ERROR_UNSUPPORTED_OS_VERSION);
    }

}
