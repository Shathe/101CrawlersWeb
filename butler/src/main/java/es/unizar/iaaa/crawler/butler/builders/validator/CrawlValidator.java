package es.unizar.iaaa.crawler.butler.builders.validator;

import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlValidator implements Validator {

	@Override
	public ValidationResult validate(Configuration config) {
		
		/* Add every validator needed */
		ArrayList<Validator> validators= new ArrayList<Validator>();
		validators.add(new CrawlVersionValidator());
		validators.add(new CrawlSeedsValidator());
		validators.add(new CrawlRoundsValidator());
		validators.add(new CrawlPluginsValidator());
		validators.add(new CrawlInfoValidator());
		validators.add(new CrawlCrawlDelayValidator());
		validators.add(new CrawlMaxFileValidator());
		validators.add(new CrawlLimitURLValidator());
		validators.add(new CrawlQueueModeValidator());
		validators.add(new CrawlTimeoutsValidator());
		
		/* Validate each one until it finds an error (if theres any) */
		Validator validadorName = new CrawlNameValidator();
		ValidationResult resultado = validadorName.validate(config);
		for (int i=0; resultado.isOk() && i< validators.size();i++){
			resultado =validators.get(i).validate(config);
		}
			return resultado;

	}

}
