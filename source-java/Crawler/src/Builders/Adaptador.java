package Builders;

import YalmReader.Configuration;

public class Adaptador {

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
	
	public boolean validar(){
		boolean valido=true;
		/* comprobar que todos los valores son del tipo y rango esperado */
		return valido;
	}
}
