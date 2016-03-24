package es.unizar.iaaa.crawler.butler.config;

import es.unizar.iaaa.crawler.butler.validator.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class ValidatorConfiguration {

	@Bean
	public ConfigurationValidator configurationValidator(DockerValidator dockerValidator, CrawlValidator crawlValidator) {
		ArrayList<Validator> validadores = new ArrayList<>();
		validadores.add(dockerValidator);
		validadores.add(crawlValidator);
		return new ConfigurationValidator(validadores);
	}

	@Bean
	public DockerValidator dockerValidator() {
		ArrayList<Validator> validators = new ArrayList<>();
		validators.add(new OSNameValidator());
		validators.add(new OSVersionValidator());
		return new DockerValidator(validators);
	}


	@Bean
	public CrawlValidator crawlerValidator() {
		ArrayList<Validator> validators = new ArrayList<>();
		validators.add(new CrawlNameValidator());
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
		return new CrawlValidator(validators);
	}
}