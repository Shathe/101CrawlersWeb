/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import java.io.File;
import java.util.List;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.model.Plugin;

/**
 * Validates if the plugins part is well formed in this case it's well formed if
 * it's an integer number
 */
public class CrawlPluginsValidator implements Validator {

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
    	List<Plugin> plugins = config.getCrawlSystem().getPlugins();

        for (int i = 0; plugins != null && !plugins.isEmpty()
                && i < plugins.size(); i++) {
        	//For each plugin
        	Plugin nextPlugin=plugins.get(i);
        	String plugin = nextPlugin.getName();
        	List<File> files= nextPlugin.getFiles();
        	boolean xml=false;
        	boolean jar=false;
            // Check whether the files plugins are jar files and there's a plugin.xml
        	for (int j = 0; files != null && !files.isEmpty()
                    && j < files.size(); j++) {
        		if(files.get(j).getName().contains(".xml"))xml=true;
        		if(files.get(j).getName().contains(".jar"))jar=true;
        	}
        	if(!(xml && jar)){
        		//bad plugin
                return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_PLUGINS, plugin);
        	}
        }
    	
        return new LatestValidationResult();
    }
    

    
}
