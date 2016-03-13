package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

import java.io.File;

public class CrawlPluginsValidator implements Validator {

    @Override
    public ValidationResult validate(Configuration config) {

        for (int i = 0; i < config.getCrawlSystem().getPlugins().size(); i++) {
            /* Estructura: nombre (file.jar)+ */
            String[] all = config.getCrawlSystem().getPlugins().get(i).split(" ");
            String plugin = all[0];
			/* Compruebas que los ficheros existen */

            for (int j = 1; j < all.length; j++) {
                if (!checkFileExists(all[j])) {
                    return new LatestValidationResult(ErroresValidar.ERROR_UNSUPPORTED_CRAWL_PLUGINS,
                            "Error en el plugin: " + plugin + ", no se encuentra: " + all[j]);
                }
            }
            String namePlun = plugin + ".xml";
            if (!checkFileExists(namePlun)) {
                return new LatestValidationResult(ErroresValidar.ERROR_UNSUPPORTED_CRAWL_PLUGINS,
                        "Error en el plugin: " + plugin + ", no se encuentra: " + namePlun);
            }
            ;
        }
        return new LatestValidationResult();
    }

    private boolean checkFileExists(String file) {
        try {
            // TODO Determinar la mejor manera de comprobar la existencia de recursos (¿Tenemos que introducir ya Spring?)
            if (new File(file).exists()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
