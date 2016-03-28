/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl max file  is well formed
 * in this case it's well formed if it's an integer number 
 */
public class CrawlMaxFileValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getMaxFileLength();
        return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_FILE_LENGTH, false);
    }

}
