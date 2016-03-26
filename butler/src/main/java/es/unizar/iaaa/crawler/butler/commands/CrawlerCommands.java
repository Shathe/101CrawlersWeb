package es.unizar.iaaa.crawler.butler.commands;

import es.unizar.iaaa.crawler.butler.builders.AdapterBuilder;
import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.validator.ConfigurationValidator;
import es.unizar.iaaa.crawler.butler.validator.ValidationResult;
import es.unizar.iaaa.crawler.butler.yalm.YamlConfigRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class CrawlerCommands implements CommandMarker {

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private ConfigurationValidator configurationValidator;

	@Autowired
	private AdapterBuilder builder;

	@CliAvailabilityIndicator({ "config" })
	public boolean isSimpleAvailable() {
		// always available
		return true;
	}

	@CliCommand(value = "config", help = "If the configuration file is ok, creates every file needed for the crawling system")
	public String simple(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = { "idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler,
			@CliOption(key = {
					"file" }, mandatory = true, help = "The name onf the configuration file") final String configuration) {
		String response = "";
		CrawlConfiguration config;
		try {
			config = readConfiguration(configuration);
			ValidationResult result = configurationValidator.validate(config);
			if (result.isOk()) {
				String id = idUser+idCrawler;
				builder.createConfigurationFiles(readConfiguration(configuration), id);
			} else {
				response = result.getFirstErrorValue().toString();
			}
		} catch (Exception e) {
			response = "File not found";
		}
		return response;
	}

	private Resource readPath(String route) throws Exception {
		return ctx.getResource("classpath:es/unizar/iaaa/crawler/butler/builders/" + route);
	}

	private CrawlConfiguration readConfiguration(String route) throws Exception {
		return YamlConfigRunner.read(readPath(route));
	}
}
