package es.unizar.iaaa.crawler.butler.commands;

import java.io.File;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import es.unizar.iaaa.crawler.butler.builders.AdapterBuilder;
import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.validator.ConfigurationValidator;
import es.unizar.iaaa.crawler.butler.validator.ValidationResult;

@Component
public class BuildingCommands implements CommandMarker {

	static Logger log = Logger.getLogger(BuildingCommands.class.getName());

	private Operations ops=new Operations();
	

	@Autowired
	private AdapterBuilder builder;
	 
	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private ConfigurationValidator configurationValidator;

	@CliAvailabilityIndicator({ "config" })
	public boolean configAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "build" })
	public boolean buildAvailable() {
		// always available start --idUser 1 -- idCrawler 1
		return true;
	}


	/* Config command, the one which crates every configuration file */
	@CliCommand(value = "config", help = "If the configuration file is ok, creates every file needed for the crawling system")
	public String config(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = { "idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler,
			@CliOption(key = {
					"file" }, mandatory = true, help = "The name onf the configuration file") final String configuration) {
		String response = "configurated successfully";
		CrawlConfiguration config;
		try {
			config = ops.readConfiguration(configuration);
			ValidationResult result = configurationValidator.validate(config);
			if (result.isOk()) {
				String id = idUser + "_" + idCrawler;
				builder.createConfigurationFiles(ops.readConfiguration(configuration), id);
			} else {
				response = result.getFirstErrorValue().toString();
			}
		} catch (Exception e) {
			response = "File not found";
		}
		return response;
	}

	/* Build docker and crawler files */
	@CliCommand(value = "build", help = "the directory with the files must exist")
	public String build(

			@CliOption(key = { "idUser" }, mandatory = true, help = "id of the user") final String idUser,
			@CliOption(key = {
					"idCrawler" }, mandatory = true, help = "id of the new crawler") final String idCrawler) {
		String response = "Image built successfully";
		try {
			String id = idUser + "_" + idCrawler;
			File directorio = new File(id);
			if (directorio.isDirectory()) {
				// docker build -t nameOfImage directory
				String comando = "docker build -t " + id + " " + id;
				ops.executeCommand(comando, true);

			} else {
				response = "Files dont exist, please, try executing the config command";
			}

		} catch (Exception e) {
			response = "File not found";
		}

		return response;
	}

}
