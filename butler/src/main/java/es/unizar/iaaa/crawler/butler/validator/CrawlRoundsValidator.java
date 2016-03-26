package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlRoundsValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        if (config.getCrawlSystem().getRounds() == null)
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_ROUNDS,
                    "There's no number of rounds specified in the configuration file.");

        try {
            /* Valida que sea un numero entero */
            Integer.valueOf(config.getCrawlSystem().getRounds());
            return new LatestValidationResult();

        } catch (Exception a) {
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_ROUNDS,
            		"Crawl rounds error:" +   config.getCrawlSystem().getRounds());
        }
    }

}
