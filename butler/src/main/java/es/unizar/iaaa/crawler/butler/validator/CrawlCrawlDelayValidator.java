/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl delay is well formed
 * in this case it's well formed if it's an integer number 
 */
public class CrawlCrawlDelayValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getMaxCrawlDelay();
        return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_DELAY, false);
    }
}
