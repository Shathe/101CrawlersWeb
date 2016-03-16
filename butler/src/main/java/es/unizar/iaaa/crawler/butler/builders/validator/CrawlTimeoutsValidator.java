package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlTimeoutsValidator implements Validator {

<<<<<<< HEAD
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
=======
    @Override
    public ValidationResult validate(Configuration config) {
        try {
            /* Valida que sea un numero entero */
            if (config.getCrawlSystem().getTimeouts().getFetchTimes() != null)
                Integer.valueOf(config.getCrawlSystem().getTimeouts().getFetchTimes());
            if (config.getCrawlSystem().getTimeouts().getNetwork() != null)
                Integer.valueOf(config.getCrawlSystem().getTimeouts().getNetwork());
            if (config.getCrawlSystem().getTimeouts().getParser() != null)
                Integer.valueOf(config.getCrawlSystem().getTimeouts().getParser());
            return new LatestValidationResult();
>>>>>>> 5a23b7e7125595cf928ede0f70b89763eea9ce0e

        } catch (Exception a) {
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_TIMEOUTS,
                    config.getCrawlSystem().getTimeouts());
        }
    }

}
