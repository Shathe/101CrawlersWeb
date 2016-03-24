package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlNameValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        if (config.getCrawlSystem().getName() == null)
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_NAME,
                    "There's no name in the configuration file.");

        if (!"nutch".equals(config.getCrawlSystem().getName().toLowerCase())) {
            /* Crea el resultado con el error encontrado  */
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_NAME,
                    config.getCrawlSystem().getName());
        }
		/* Crea el resultado sin errores */
        return new LatestValidationResult();
    }

}
