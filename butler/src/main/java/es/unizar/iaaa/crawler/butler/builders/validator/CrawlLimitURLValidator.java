package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlLimitURLValidator implements Validator {

<<<<<<< HEAD
	@Override
	public ValidationResult validate(Configuration config) {
		try {
			/* Valida que sea un numero entero */
			if (config.getCrawlSystem().getLinksLimitURL() != null)
			Integer.valueOf(config.getCrawlSystem().getLinksLimitURL());
			return new LatestValidationResult();
=======
    @Override
    public ValidationResult validate(Configuration config) {
        try {
            /* Valida que sea un numero entero */
            Integer.valueOf(config.getCrawlSystem().getLinksLimitURL());
            return new LatestValidationResult();
>>>>>>> 5a23b7e7125595cf928ede0f70b89763eea9ce0e

        } catch (Exception a) {
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_LIMIT_URL,
                    config.getCrawlSystem().getLinksLimitURL());
        }
    }

}
