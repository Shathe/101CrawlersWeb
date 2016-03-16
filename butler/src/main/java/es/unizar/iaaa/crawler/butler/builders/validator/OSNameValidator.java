package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class OSNameValidator implements Validator {

    @Override
    public ValidationResult validate(Configuration config) {
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
