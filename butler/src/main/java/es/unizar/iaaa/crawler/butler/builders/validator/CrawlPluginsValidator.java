package es.unizar.iaaa.crawler.butler.builders.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.unizar.iaaa.crawler.butler.builders.CoordinatorTest;
import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlPluginsValidator implements Validator {

	@Override
	public ValidationResult validate(Configuration config) {
		
		boolean correcto=true;
		String siguientePlugin=""; String sigueinteFile="";
		for (int i = 0; i < config.getCrawlSystem().getPlugins().size(); i++) {
			/* Estructura: nombre (file.jar)+ */
			String [] all=config.getCrawlSystem().getPlugins().get(i).split(" ");
			siguientePlugin=all[0];
			InputStream in;
			/* Compruebas que los ficheros existen */
	        try {
	        	String namePlun=siguientePlugin+".xml";
	    		for (int j = 1; j < all.length; j++) {
	    			sigueinteFile=all[j];
		            in = Files.newInputStream(Paths.get(CoordinatorTest.class.getResource(sigueinteFile).toURI()));
	    		}
	    		sigueinteFile=namePlun;
	            in = Files.newInputStream(Paths.get(CoordinatorTest.class.getResource(sigueinteFile).toURI()));
	            
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	correcto=false;
	        }
		}
		if (correcto) {
			return new LatestValidationResult();
		}
		else {
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_PLUGINS, 
					"Error en el plugin:"+siguientePlugin +", no se encuentra:"+sigueinteFile );
		}

	}

}
