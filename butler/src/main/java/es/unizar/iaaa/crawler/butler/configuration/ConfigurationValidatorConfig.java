package es.unizar.iaaa.crawler.butler.configuration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import es.unizar.iaaa.crawler.butler.builders.validator.*;

@Configuration()
@ContextConfiguration(classes={CrawlValidatorConfig.class,DockerValidatorConfig.class})
public class ConfigurationValidatorConfig {

	@Autowired
	DockerValidator dockerV;
	@Autowired
	CrawlValidator crawlV;
	
	@Bean
	public ConfigurationValidator configurationValidator() {
		ArrayList<Validator> validadores = new ArrayList<Validator>();
		CrawlValidatorConfig context = new CrawlValidatorConfig();
		crawlV = context.crawlerValidator();
		DockerValidatorConfig context2 = new DockerValidatorConfig();
		dockerV = context2.dockerValidator();
		validadores.add(dockerV);
		validadores.add(crawlV);
		return new ConfigurationValidator(validadores);
	}


}