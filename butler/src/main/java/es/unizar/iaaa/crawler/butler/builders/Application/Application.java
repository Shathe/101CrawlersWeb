package es.unizar.iaaa.crawler.butler.builders.Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import es.unizar.iaaa.crawler.butler.builders.validator.ConfigurationValidator;
import es.unizar.iaaa.crawler.butler.configuration.ConfigurationValidatorConfig;

@SpringBootApplication // same as @Configuration @
@EnableAutoConfiguration
@ComponentScan
@ContextConfiguration(classes = { ConfigurationValidator.class })
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}

}