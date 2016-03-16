package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlTimeoutsValidator implements Validator {

	@Override
	public ValidationResult validate(Configuration config) {
		try {
			if (config.getCrawlSystem().getTimeouts() != null) {
				/* Valida que sea un numero entero */
				if (config.getCrawlSystem().getTimeouts().getFetchTimes() != null)
					Integer.valueOf(config.getCrawlSystem().getTimeouts().getFetchTimes());
				if (config.getCrawlSystem().getTimeouts().getNetwork() != null)
					Integer.valueOf(config.getCrawlSystem().getTimeouts().getNetwork());
				if (config.getCrawlSystem().getTimeouts().getParser() != null)
					Integer.valueOf(config.getCrawlSystem().getTimeouts().getParser());
			}
			return new LatestValidationResult();


        } catch (Exception a) {
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_TIMEOUTS,
                    config.getCrawlSystem().getTimeouts());
        }
    }

}
