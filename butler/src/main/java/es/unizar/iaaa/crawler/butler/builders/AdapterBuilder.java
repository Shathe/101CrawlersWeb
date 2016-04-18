/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Builds the system. Depending the configuration the adapter will choose the
 * correct builders.
 */
@Component
public class AdapterBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdapterBuilder.class);

	@Autowired
	private NutchBuilder builder;

	@Autowired
	private DockerBuilder dockerBuilder;

	@Autowired
	private ApplicationContext ctx;

	@Value("${butler.templates:classpath:/templates}/")
	private String templatesDir;

	/**
	 * Creates the configuration files, according the crawling system.
	 */
	public void createConfigurationFiles(CrawlConfiguration configuration, String outputDir) throws IOException {

		Path outputPath = Paths.get(outputDir);
        Resource resFile = ctx.getResource(templatesDir);

        // If the folder exists, delete it
		FileUtils.deleteDirectory(outputPath.toFile());
        Files.createDirectory(outputPath);

		// Call the crawler builder and then the docker builder
		builder.createNutchSite(configuration, outputDir);
		dockerBuilder.createDockerfile(configuration, resFile, outputDir);
	}

}
