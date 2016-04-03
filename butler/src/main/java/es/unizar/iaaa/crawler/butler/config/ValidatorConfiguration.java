package es.unizar.iaaa.crawler.butler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.validator.ConfigurationValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlCrawlDelayValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlExtractionValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlInfoValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlLimitURLValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlMaxFileValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlNameValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlPluginsValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlQueueModeValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlRoundsValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlSeedsValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlTimeoutsValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlValidator;
import es.unizar.iaaa.crawler.butler.validator.CrawlVersionValidator;
import es.unizar.iaaa.crawler.butler.validator.DockerValidator;
import es.unizar.iaaa.crawler.butler.validator.OSNameValidator;
import es.unizar.iaaa.crawler.butler.validator.OSVersionValidator;
import es.unizar.iaaa.crawler.butler.validator.Validator;

/**
 * Configuration of the validators
 */

@Configuration
public class ValidatorConfiguration {

    @Bean
    public ConfigurationValidator configurationValidator(DockerValidator dockerValidator, CrawlValidator crawlValidator) {
        ArrayList<Validator> validators = new ArrayList<>();
        validators.add(dockerValidator);
        validators.add(crawlValidator);
        return new ConfigurationValidator(validators);
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
        validators.add(new CrawlExtractionValidator());
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