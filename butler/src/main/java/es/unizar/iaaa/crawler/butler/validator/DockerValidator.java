/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;


/**
 * Validates a list of validatiors, it validates if the configuration file
 * is well formed concerning the docker part
 */
public class DockerValidator implements Validator {

    ArrayList<Validator> lista;

    public DockerValidator(ArrayList<Validator> lista) {
        this.lista = lista;
    }

    @Override
    public ValidationResult validate(CrawlConfiguration config) {

        ValidationResult resultado = new LatestValidationResult();

        for (int i = 0; i < lista.size() && resultado.isOk(); i++) {
            resultado = lista.get(i).validate(config);
        }
        return resultado;
    }

}
