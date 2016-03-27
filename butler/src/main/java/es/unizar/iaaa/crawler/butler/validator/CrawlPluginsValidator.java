/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

import java.io.File;
import java.util.List;

/**
 * Validates if the plugins part is well formed
 * in this case it's well formed if it's an integer number 
 */
public class CrawlPluginsValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {

        // TODO Use // for comments wihtin methods
		/* For each plugin */
        for (int i = 0; config.getCrawlSystem().getPlugins() != null && !config.getCrawlSystem().getPlugins().isEmpty()
                && i < config.getCrawlSystem().getPlugins().size(); i++) {
            // TODO Use // for comments wihtin methods
            /* Structure: nombre file.xml (file.jar)+ */
        	List<String> all=config.getCrawlSystem().getPlugins().get(i);
            String plugin = all.get(0);
            // TODO Use // for comments wihtin methods
			/* Check whether the files exists */

            if (!checkFileExists(all.get(1))) {
                return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_PLUGINS, all.get(1));
            }
            for (int siguiente = 2; siguiente < all.size(); siguiente++) {
                if (!checkFileExists(all.get(siguiente))) {
                    return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_PLUGINS, all.get(siguiente));
                }
            }

        }
        return new LatestValidationResult();
    }

    private boolean checkFileExists(String file) {
        try {
            if (new File(file).exists()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
