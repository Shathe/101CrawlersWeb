package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class ConfigurationValidator implements Validator {

    @Override
    public ValidationResult validate(Configuration config) {
        /* Se comprueba docker */
        Validator validadorName = new DockerValidator();
        ValidationResult resultado = validadorName.validate(config);
        if (resultado.isOk()) {
			/* Si docker esta bien se comprueba la version */
            Validator validadorVersion = new CrawlValidator();
            resultado = validadorVersion.validate(config);
            return resultado;

        } else {
            return resultado;
        }

    }

}
