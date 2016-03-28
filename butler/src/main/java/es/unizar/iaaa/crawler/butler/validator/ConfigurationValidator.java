/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates a list of validatiors, it validates if the configuration file
 * is totally well formed
 */
public class ConfigurationValidator implements Validator {

    ArrayList<Validator> lista;

    public ConfigurationValidator(ArrayList<Validator> lista) {
        this.lista = lista;
    }

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        if (config != null) {
            ValidationResult resultado = new LatestValidationResult();

            for (int i = 0; i < lista.size() && resultado.isOk(); i++) {
                resultado = lista.get(i).validate(config);
            }
            return resultado;
        } else
            return null;
    }
}
