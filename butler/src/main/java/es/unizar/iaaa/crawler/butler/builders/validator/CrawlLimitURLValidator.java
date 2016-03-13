package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlLimitURLValidator implements Validator {

	@Override
	public ValidationResult validate(Configuration config) {
		try {
			/* Valida que sea un numero entero */
			Integer.valueOf(config.getCrawlSystem().getLinksLimitURL());
			return new LatestValidationResult();

		} catch (Exception a) {
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_LIMIT_URL,
					config.getCrawlSystem().getLinksLimitURL());
		}
	}

}
