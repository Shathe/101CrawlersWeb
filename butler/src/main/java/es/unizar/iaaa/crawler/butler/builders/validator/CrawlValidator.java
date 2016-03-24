package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

import java.util.ArrayList;

public class CrawlValidator implements Validator {

	ArrayList<Validator> lista;

	public CrawlValidator(ArrayList<Validator> lista) {
		this.lista = lista;
	}

    @Override
    public ValidationResult validate(Configuration config) {
    	
        ValidationResult resultado =  new LatestValidationResult();

        for (int i=0; i<lista.size() && resultado.isOk() ;i++){
        	resultado=lista.get(i).validate(config);
        }
        return resultado;
    }
    
    
}
