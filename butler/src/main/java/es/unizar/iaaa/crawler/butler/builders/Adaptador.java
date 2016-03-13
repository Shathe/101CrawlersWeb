package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class Adaptador {

	/* Valida los fichero de configuración, si son válidos, elegirá a que builders pasarselos
	 * para que cree los ficheros de configuracion del crawler y dokcer
	 */
	private Configuration configuracion;

	public Adaptador(Configuration config) {
		configuracion = config;
	}

	public Configuration getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(Configuration configuracion) {
		this.configuracion = configuracion;
	}
	
	
}
