package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class OSNameValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getDockerOS().getName();
        return validateStringValue(value, "ubuntu", Status.ERROR_UNSUPPORTED_OS_NAME);
    }

}
