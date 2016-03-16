/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

<<<<<<< HEAD
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

=======
>>>>>>> 5a23b7e7125595cf928ede0f70b89763eea9ce0e
import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class AdaptadorBuilder {

<<<<<<< HEAD
	private Configuration configuracion;
	private final String resources = "src/test/resources/es/unizar/iaaa/crawler/butler/builders/";
	private final String nombre;
=======
    private final String resources = "src/test/resources/es/unizar/iaaa/crawler/butler/builders/";
    private Configuration configuracion;
>>>>>>> 5a23b7e7125595cf928ede0f70b89763eea9ce0e

    public AdaptadorBuilder(Configuration config) {
        configuracion = config;
    }

<<<<<<< HEAD
	public AdaptadorBuilder(Configuration config, String nombre) {
		configuracion = config;
		this.nombre = nombre;
	}
=======
    public String getResources() {
        return resources;
    }
>>>>>>> 5a23b7e7125595cf928ede0f70b89763eea9ce0e

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
			 * Aquí si hubiera varias posibilidades de sistemas de crawling o
			 * OS's para docker, el adaptador
			 */
			String directoryName = nombre;
			File theDir = new File(directoryName);
			theDir.mkdir();
			/* Primero se llama al builder de nutch y después al de docker */
<<<<<<< HEAD
			NutchBuilder builder = new NutchBuilder(configuracion, resources, directoryName);
			builder.crearNutchSite();

			DockerBuilder dockerbuilder = new DockerBuilder(configuracion, resources, directoryName,builder);
			dockerbuilder.crearDockerfile();
		}
		else{
			System.out.println(configuracion.getLatestErrorValue());
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
=======
            NutchBuilder builder = new NutchBuilder(configuracion, resources);
            builder.crearNutchSite();

            DockerBuilder dockerbuilder = new DockerBuilder(configuracion, resources);
            dockerbuilder.crearDockerfile();
        }
    }
>>>>>>> 5a23b7e7125595cf928ede0f70b89763eea9ce0e

}
