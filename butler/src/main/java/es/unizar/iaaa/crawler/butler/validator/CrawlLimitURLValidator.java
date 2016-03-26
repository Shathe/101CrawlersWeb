package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlLimitURLValidator implements Validator {

	@Override
	public ValidationResult validate(CrawlConfiguration config) {
		try {
			/* Valida que sea un numero entero */
			if (config.getCrawlSystem().getLinksLimitURL() != null)
			Integer.valueOf(config.getCrawlSystem().getLinksLimitURL());
			return new LatestValidationResult();

        } catch (Exception a) {
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_LIMIT_URL,
            		"LimitUrl error:" + config.getCrawlSystem().getLinksLimitURL());
        }
    }

}
