package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlVersionValidator implements Validator {

	@Override
	public ValidationResult validate(Configuration config) {
		if (config.getCrawlSystem().getVersion()==null) 
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_VERSION,
					"There's no vesion in the configuration file.");
		
		if (!"1.9".equals(config.getCrawlSystem().getVersion())) {
			/* Crea el resultado con el error encontrado  */
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_VERSION,
					config.getCrawlSystem().getVersion());
		}
		/* Crea el resultado sin errores */
		return new LatestValidationResult();
	}

}
