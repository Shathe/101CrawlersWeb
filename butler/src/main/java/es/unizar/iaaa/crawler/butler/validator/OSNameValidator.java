/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl system name is well formed
 * in this case it's well formed if it's ubuntu
 */
public class OSNameValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        String value = config.getDockerOS().getName();
        return validateStringValue(value, "ubuntu", Status.ERROR_UNSUPPORTED_OS_NAME);
    }

}
