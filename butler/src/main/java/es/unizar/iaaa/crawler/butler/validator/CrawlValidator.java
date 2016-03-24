package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

import java.util.ArrayList;

public class CrawlValidator implements Validator {

	ArrayList<Validator> lista;

	public CrawlValidator(ArrayList<Validator> lista) {
		this.lista = lista;
	}

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
    	
        ValidationResult resultado =  new LatestValidationResult();

        for (int i=0; i<lista.size() && resultado.isOk() ;i++){
        	resultado=lista.get(i).validate(config);
        }
        return resultado;
    }
    
    
}
