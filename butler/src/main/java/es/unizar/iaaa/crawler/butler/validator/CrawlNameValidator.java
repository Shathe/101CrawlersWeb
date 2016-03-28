/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl system name is well formed
 * in this case it's well formed if it's nutch 
 */
public class CrawlNameValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getName();
        return validateStringValue(value, "nutch", Status.ERROR_UNSUPPORTED_CRAWL_NAME);
    }

}
