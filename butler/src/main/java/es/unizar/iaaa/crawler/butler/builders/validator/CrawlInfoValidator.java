package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlInfoValidator implements Validator {

    @Override
    public ValidationResult validate(Configuration config) {
        if (config.getCrawlSystem().getInfoCrawled() == null)
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_INFO,
                    "There's no info crawled option in the configuration file.");

        if (!"text".equals(config.getCrawlSystem().getInfoCrawled().toLowerCase()) &&
                !"html".equals(config.getCrawlSystem().getInfoCrawled().toLowerCase())) {
            /* Crea el resultado con el error encontrado  */
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_INFO,
                    config.getCrawlSystem().getInfoCrawled());
        }
		/* Crea el resultado sin errores */
        return new LatestValidationResult();
    }

}
