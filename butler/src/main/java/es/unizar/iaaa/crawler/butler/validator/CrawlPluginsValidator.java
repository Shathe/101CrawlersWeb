/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

import java.io.File;

/**
 * Validates if the plugins part is well formed
 * in this case it's well formed if it's an integer number 
 */
public class CrawlPluginsValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {

		/* For each plugin */
        for (int i = 0; config.getCrawlSystem().getPlugins() != null && !config.getCrawlSystem().getPlugins().isEmpty()
                && i < config.getCrawlSystem().getPlugins().size(); i++) {
            /* Structure: nombre file.xml (file.jar)+ */
        	int siguiente=0;
            String[] all = config.getCrawlSystem().getPlugins().get(i).split(" ");
            String plugin = all[nextNotVoid(all,siguiente)];
            siguiente++;
			/* Check whether the files exists */

            if (!checkFileExists(all[nextNotVoid(all,siguiente)])) {
                return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_PLUGINS, all[nextNotVoid(all,siguiente)]);
            }
            siguiente++;
            for (siguiente = nextNotVoid(all,siguiente); siguiente < all.length; siguiente++) {
            	siguiente = nextNotVoid(all,siguiente);
                if (!checkFileExists(all[siguiente])) {
                    return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_PLUGINS, all[siguiente]);
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

    /** Returns the next not void index */
    private int nextNotVoid(String [] array, int i){
    	while (i< array.length && array[i].equals(""))i++;
    	return i;
    }
}
