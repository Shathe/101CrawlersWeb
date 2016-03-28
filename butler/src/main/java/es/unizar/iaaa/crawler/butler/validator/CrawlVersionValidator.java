/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl version is well formed
 * in this case it's well formed if it's 1.9 
 */
public class CrawlVersionValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getVersion();
        return validateStringValue(value, "1.9", Status.ERROR_UNSUPPORTED_CRAWL_VERSION);
    }

}
