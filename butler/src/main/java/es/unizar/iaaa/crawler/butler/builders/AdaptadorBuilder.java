/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class AdaptadorBuilder {

	private Configuration configuracion;
	private final String resources = "src/test/resources/es/unizar/iaaa/crawler/butler/builders/";

	public String getResources() {
		return resources;
	}

	public AdaptadorBuilder(Configuration config) {
		configuracion = config;
	}

	public Configuration getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(Configuration configuracion) {
		this.configuracion = configuracion;
	}

	/* Crear ficheros de configuracion dependiendo del sistema de crawling */
	public void crearFicherosConfiguracion() {
		/* Si está bien la configuración */
		if (configuracion.isOk()) {
			/*
			 * Aquí si hubiera varias posibilidades de sistemas de crawling o OS's
			 * para docker, el adaptador 
			 */
			
			/* Primero se llama al builder de nutch y después al de docker */
			NutchBuilder builder = new NutchBuilder(configuracion, resources);
			builder.crearNutchSite();

			DockerBuilder dockerbuilder = new DockerBuilder(configuracion, resources);
			dockerbuilder.crearDockerfile();
		}
	}

}
