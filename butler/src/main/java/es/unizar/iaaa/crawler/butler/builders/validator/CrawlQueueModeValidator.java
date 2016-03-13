package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlQueueModeValidator implements Validator {

	@Override
	public ValidationResult validate(Configuration config) {
		if (config.getCrawlSystem().getQueueMode() == null)
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_QUEUEMODE,
					"There's no info queue mode in the configuration file.");

		if (!"byhost".equals(config.getCrawlSystem().getQueueMode().toLowerCase())
				&& !"byip".equals(config.getCrawlSystem().getQueueMode().toLowerCase())
				&& !"bydomain".equals(config.getCrawlSystem().getQueueMode().toLowerCase())) {
			/* Crea el resultado con el error encontrado */
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_QUEUEMODE,
					config.getCrawlSystem().getQueueMode());
		}
		/* Crea el resultado sin errores */
		return new LatestValidationResult();
	}

}
