package es.unizar.iaaa.crawler.butler.builders.validator;

import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class DockerValidator implements Validator {

	ArrayList<Validator> lista;

	public DockerValidator(ArrayList<Validator> lista) {
		this.lista = lista;
	}

	@Override
	public ValidationResult validate(Configuration config) {

		ValidationResult resultado = new LatestValidationResult();

		for (int i = 0; i < lista.size() && resultado.isOk(); i++) {
			resultado = lista.get(i).validate(config);
		}
		return resultado;
	}

}
