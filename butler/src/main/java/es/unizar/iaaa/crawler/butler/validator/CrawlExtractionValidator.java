/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl queue mode is well formed
 * in this case it's well formed if it's by host, by ip or bydomain 
 */
public class CrawlExtractionValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getExtraction();
        return validateStringValue(value, "round|finish", Status.ERROR_UNSUPPORTED_CRAWL_EXTRACTION);
    }

}
