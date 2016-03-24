package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class CrawlMaxFileValidator implements Validator {

	@Override
	public ValidationResult validate(CrawlConfiguration config) {
		try {
			/* Valida que sea un numero entero */
			if (config.getCrawlSystem().getMaxFileLength() != null)
				Integer.valueOf(config.getCrawlSystem().getMaxFileLength());
			return new LatestValidationResult();

        } catch (Exception a) {
            return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_FILE_LENGTH,
                    config.getCrawlSystem().getMaxFileLength());
        }
    }

}
