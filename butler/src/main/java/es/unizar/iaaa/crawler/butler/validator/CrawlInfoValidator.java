/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the information carlwed configuration is well formed
 * in this case it's well formed if it's text or html 
 */
public class CrawlInfoValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getInfoCrawled();
        return validateStringValue(value, "text|html", Status.ERROR_UNSUPPORTED_CRAWL_INFO);
    }

}
