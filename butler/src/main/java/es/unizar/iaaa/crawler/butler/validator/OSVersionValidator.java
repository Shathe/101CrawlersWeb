/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl system os is well formed
 * in this case it's well formed if it's 14.04
 */
public class OSVersionValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getDockerOS().getVersion();
        return validateStringValue(value, "14.04", Status.ERROR_UNSUPPORTED_OS_VERSION);
    }

}
