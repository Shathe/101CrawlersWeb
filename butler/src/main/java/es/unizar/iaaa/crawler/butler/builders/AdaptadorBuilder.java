/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Random;
import es.unizar.iaaa.crawler.butler.yalm.YamlConfigRunner;

public class AdaptadorBuilder {

	String nombre;
	Path fichero;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setConfig(String nombre, Path fichero) {
		this.nombre = nombre;
		this.fichero = fichero;
	}

	/* Crear ficheros de configuration dependiendo del sistema de crawling */
	public void crearFicherosConfiguracion() {
		try {
			Configuration configuration = YamlConfigRunner.read(fichero);

			/* Si está bien la configuración */
			if (configuration.isOk()) {
				/*
				 * Aquí si hubiera varias posibilidades de sistemas de crawling
				 * o OS's para docker, el adaptador
				 */
				String directoryName = nombre;
				File theDir = new File(directoryName);
				theDir.mkdir();
				/*
				 * Primero se llama al builder de nutch y después al de docker
				 */
				CrawlerBuilder builder = new NutchBuilder(configuration, directoryName);
				builder.createNutchSite();

				DockerBuilder dockerbuilder;
				try {
					dockerbuilder = new DockerBuilder(configuration,
							new File(getClass().getResource("/templates").toURI()), directoryName, builder);
					dockerbuilder.crearDockerfile();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println(configuration.getLatestErrorValue());
			}
		} catch (Exception a) {
			System.out.println(a.toString());
		}
	}

}
