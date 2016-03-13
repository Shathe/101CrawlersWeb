package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlMaxFileValidator implements Validator {

	@Override
	public ValidationResult validate(Configuration config) {
		try {
			/* Valida que sea un numero entero */
			Integer.valueOf(config.getCrawlSystem().getMaxFileLength());
			return new LatestValidationResult();

		} catch (Exception a) {
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_FILE_LENGTH,
					config.getCrawlSystem().getMaxFileLength());
		}
	}

}
