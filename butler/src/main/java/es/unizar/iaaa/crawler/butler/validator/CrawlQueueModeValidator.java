/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl queue mode is well formed
 * in this case it's well formed if it's by host, by ip or bydomain 
 */
public class CrawlQueueModeValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getCrawlSystem().getQueueMode();
        return validateStringValue(value, "byhost|byip|bydomain", Status.ERROR_UNSUPPORTED_CRAWL_QUEUEMODE);
    }

}
