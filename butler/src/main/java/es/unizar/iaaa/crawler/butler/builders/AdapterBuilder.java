/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class AdapterBuilder {

    @Autowired
    private NutchBuilder builder;

    @Autowired
    private DockerBuilder dockerBuilder;

    @Autowired
    private ApplicationContext ctx;


    /* Crear ficheros de configuration dependiendo del sistema de crawling */
    public void createConfigurationFiles(CrawlConfiguration configuration, String outputDir) {


				/*
                 * Aquí si hubiera varias posibilidades de sistemas de crawling
				 * o OS's para docker, el adaptador
				 */
        File theDir = new File(outputDir);
        theDir.mkdir();
				/*
				 * Primero se llama al builder de nutch y después al de docker
				 */
        builder.createNutchSite(configuration, outputDir);

        try {
            dockerBuilder.crearDockerfile(configuration, ctx.getResource("classpath:/templates").getFile(), outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
