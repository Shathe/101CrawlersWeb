package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class DockerValidator implements Validator {

    @Override
    public ValidationResult validate(Configuration config) {
        /* Se comprueba el nombre */
        Validator validadorName = new OSNameValidator();
        ValidationResult resultado = validadorName.validate(config);
        if (resultado.isOk()) {
			/* Si el nombre esta bien se comprueba la version */
            Validator validadorVersion = new OSVersionValidator();
            resultado = validadorVersion.validate(config);
            return resultado;

        } else {
            return resultado;
        }

    }

}
