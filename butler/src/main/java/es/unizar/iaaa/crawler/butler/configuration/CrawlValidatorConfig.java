package es.unizar.iaaa.crawler.butler.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.unizar.iaaa.crawler.butler.builders.validator.CrawlCrawlDelayValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlInfoValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlLimitURLValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlMaxFileValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlNameValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlPluginsValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlQueueModeValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlRoundsValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlSeedsValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlTimeoutsValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.CrawlVersionValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.Validator;

@Configuration

public class CrawlValidatorConfig {

	@Bean
	public CrawlValidator crawlerValidator() {
		ArrayList<Validator> validadores = new ArrayList<Validator>();
		validadores.add(new CrawlNameValidator());
		validadores.add(new CrawlVersionValidator());
		validadores.add(new CrawlSeedsValidator());
		validadores.add(new CrawlRoundsValidator());
		validadores.add(new CrawlPluginsValidator());
		validadores.add(new CrawlInfoValidator());
		validadores.add(new CrawlCrawlDelayValidator());
		validadores.add(new CrawlMaxFileValidator());
		validadores.add(new CrawlLimitURLValidator());
		validadores.add(new CrawlQueueModeValidator());
		validadores.add(new CrawlTimeoutsValidator());
		return new CrawlValidator(validadores);
	}
}
