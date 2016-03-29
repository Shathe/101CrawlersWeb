/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Builds the system. Depending the configuration the adapter will choose the correct builders
 */
@Component
public class AdapterBuilder {

    @Autowired
    private NutchBuilder builder;

    @Autowired
    private DockerBuilder dockerBuilder;

    @Autowired
    private ApplicationContext ctx;


    /**
     * Creates the configuration files, according the crawling system Crear ficheros de
     * configuration dependiendo del sistema de crawling
     */
    public void createConfigurationFiles(CrawlConfiguration configuration, String outputDir) throws IOException {


        // The adapter choose the correct builers according the configuration
        // Aquí si hubiera varias posibilidades de sistemas de crawling
        // o OS's para docker, el adaptador eligiría la adecuada segun la configuarión
        File theDir = new File(outputDir);
        theDir.mkdir();
        Path newDirectoryPath = Paths.get(outputDir);

        if (Files.exists(newDirectoryPath)) {
        	// If the folder exists, delete it
        	// In practice, as the user and crawler are unic, it is only 
        	// possible to happen if it's not the first time you're trying to build it
        	FileUtils.deleteDirectory(new File(outputDir)); 
        }
        try {
            Files.createDirectory(newDirectoryPath);
        } catch (IOException e) {
            System.err.println(e);
        }
        
        
        // First crawl builder and then docker builder
        // Primero se llama al builder de nutch y después al de docker
        builder.createNutchSite(configuration, outputDir);

        dockerBuilder.crearDockerfile(configuration, ctx.getResource("classpath:/templates").getFile(), outputDir);
    }

}
