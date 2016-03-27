package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

import java.io.File;

public class CrawlPluginsValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {

		/* Para cada plugin */
        for (int i = 0; config.getCrawlSystem().getPlugins() != null && !config.getCrawlSystem().getPlugins().isEmpty()
                && i < config.getCrawlSystem().getPlugins().size(); i++) {
            /* Estructura: nombre file.xml (file.jar)+ */
            String[] all = config.getCrawlSystem().getPlugins().get(i).split(" ");
            String plugin = all[0];
			/* Compruebas que los ficheros existen */

            if (!checkFileExists(all[1])) {
                return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_PLUGINS, all[1]);
            }
            for (int j = 2; j < all.length; j++) {
                if (!checkFileExists(all[j])) {
                    return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_PLUGINS, all[j]);
                }
            }

        }
        return new LatestValidationResult();
    }

    private boolean checkFileExists(String file) {
        try {
            // TODO Determinar la mejor manera de comprobar la existencia de
            // recursos (¿Tenemos que introducir ya Spring?)
            if (new File(file).exists()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
