/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Url limit is well formed
 * in this case it's well formed if it's an integer number 
 */
public class CrawlLimitURLValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getLinksLimitURL();
        return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_LIMIT_URL, false);
    }

}
