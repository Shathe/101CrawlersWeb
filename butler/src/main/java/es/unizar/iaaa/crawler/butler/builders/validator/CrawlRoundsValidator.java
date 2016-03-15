package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlRoundsValidator implements Validator {

    @Override
    public ValidationResult validate(Configuration config) {
        if (config.getCrawlSystem().getRounds() == null)
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_ROUNDS,
                    "There's no number of rounds specified in the configuration file.");

        try {
            /* Valida que sea un numero entero */
            Integer.valueOf(config.getCrawlSystem().getRounds());
            return new LatestValidationResult();

        } catch (Exception a) {
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_ROUNDS,
                    config.getCrawlSystem().getRounds());
        }
    }

}
