/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Random;

public class AdaptadorBuilder {

	private Configuration configuration;
	private final String nombre;


	public AdaptadorBuilder(Configuration config, String nombre) {
		configuration = config;
		this.nombre = nombre;
	}
    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /* Crear ficheros de configuration dependiendo del sistema de crawling */
    public void crearFicherosConfiguracion() {
        /* Si está bien la configuración */
        if (configuration.isOk()) {
			/*
			 * Aquí si hubiera varias posibilidades de sistemas de crawling o
			 * OS's para docker, el adaptador
			 */
			String directoryName = nombre;
			File theDir = new File(directoryName);
			theDir.mkdir();
			/* Primero se llama al builder de nutch y después al de docker */
			CrawlerBuilder builder = new NutchBuilder(configuration, directoryName);
			builder.createNutchSite();


            DockerBuilder dockerbuilder;
            try {
                dockerbuilder = new DockerBuilder(configuration,
                    new File(getClass().getResource("/templates").toURI()), directoryName,builder);
                dockerbuilder.crearDockerfile();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
		}
		else{
			System.out.println(configuration.getLatestErrorValue());
		}
	}

	private String getCadenaAlfanumAleatoria(int longitud) {
		String cadenaAleatoria = "";
		long milis = new java.util.GregorianCalendar().getTimeInMillis();
		Random r = new Random(milis);
		int i = 0;
		while (i < longitud) {
			char c = (char) r.nextInt(255);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				cadenaAleatoria += c;
				i++;
			}
		}
		return cadenaAleatoria;
	}

}
