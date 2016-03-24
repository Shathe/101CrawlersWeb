package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class OSVersionValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {

        if (config.getDockerOS().getVersion() == null)
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_OS_VERSION,
                    "There's no version in the configuration file.");


        if (!"14.04".equals(config.getDockerOS().getVersion())) {
            /* Crea el resultado con el error encontrado  */
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_OS_VERSION,
                    config.getDockerOS().getVersion());
        }
		/* Crea el resultado sin errores */
        return new LatestValidationResult();
    }

}
