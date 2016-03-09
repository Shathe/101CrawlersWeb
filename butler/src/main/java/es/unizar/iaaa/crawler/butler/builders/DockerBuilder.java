/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;
import java.io.FileWriter;
import java.io.PrintWriter;

public class DockerBuilder {

	private Configuration configuracion;

	public DockerBuilder(Configuration config) {
		configuracion = config;
	}

	public Configuration getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(Configuration configuracion) {
		this.configuracion = configuracion;
	}

	public static void crearDockerfile() {

		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			//Crear el fichero docker
			/*fichero = new FileWriter("Dockerfile");
			pw = new PrintWriter(fichero);

			pw.println("Linea ");
			 */
			/*
			 * Si no se he insertado valor en alguna configuracion saldrá el valor null
			 * comprbarlo al hacer los ficheros de salida
			 */
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
