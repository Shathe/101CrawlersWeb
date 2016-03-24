package es.unizar.iaaa.crawler.butler.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.unizar.iaaa.crawler.butler.builders.validator.DockerValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.OSNameValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.OSVersionValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.Validator;

@Configuration

public class DockerValidatorConfig {

	@Bean
	public DockerValidator dockerValidator() {
		ArrayList<Validator> validadores = new ArrayList<Validator>();
		validadores.add(new OSNameValidator());
		validadores.add(new OSVersionValidator());
		return new DockerValidator(validadores);
	}

}
