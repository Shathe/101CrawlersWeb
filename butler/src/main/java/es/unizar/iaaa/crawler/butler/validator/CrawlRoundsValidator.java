/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl rounds configuration is well formed
 * in this case it's well formed if it's an integer number 
 */
public class CrawlRoundsValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getRounds();
        return validateIntValue(value, Status.ERROR_UNSUPPORTED_CRAWL_ROUNDS);
    }

}
