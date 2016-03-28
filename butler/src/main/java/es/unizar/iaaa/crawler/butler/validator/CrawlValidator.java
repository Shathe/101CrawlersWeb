package es.unizar.iaaa.crawler.butler.validator;

import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;


/**
 * Validates a list of validatiors, it validates if the configuration file is well formed concerning
 * the crawling system
 */
public class CrawlValidator implements Validator {

    ArrayList<Validator> list;

    public CrawlValidator(ArrayList<Validator> list) {
        this.list = list;
    }

    @Override
    public ValidationResult validate(CrawlConfiguration config) {

        ValidationResult resultado = new LatestValidationResult();

        for (int i = 0; i < list.size() && resultado.isOk(); i++) {
            resultado = list.get(i).validate(config);
        }
        return resultado;
    }


}
