package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class OSNameValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        if (config.getDockerOS().getName() == null)
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_OS_NAME,
                    "There's no name in the configuration file.");


        if (!"ubuntu".equals(config.getDockerOS().getName().toLowerCase())) {
            /* Crea el resultado con el error encontrado  */
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_OS_NAME,
                    config.getDockerOS().getName());
        }
		/* Crea el resultado sin errores */
        return new LatestValidationResult();
    }

}
