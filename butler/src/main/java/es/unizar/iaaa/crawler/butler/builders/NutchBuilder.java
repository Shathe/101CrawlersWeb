/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.model.Plugin;

/**
 * Builds the nutch system. Creates every file needed and writes in the dockerfile every command
 * needed
 */
@Component
public class NutchBuilder implements CrawlerBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerBuilder.class);

    public void addDockerfile(CrawlConfiguration configuration, String directoryName, PrintWriter pw) throws IOException {
        /* Download and prepare folder for nutch */
        pw.println("RUN svn checkout http://svn.apache.org/repos/asf/nutch/branches/branch-"
                + configuration.getCrawlSystem().getVersion() + "/ nutch_source && cd nutch_source && ant");
        pw.println("RUN ln -s nutch_source/runtime/local $HOME/crawler");
        pw.println("Run mkdir crawler/" + directoryName);
        pw.println("Run mkdir crawler/urls");
        pw.println("Run mkdir crawler/salida");
        pw.println("Run mkdir crawler/micrawl");
        pw.println("Run mkdir crawler/micrawl/segments");
        pw.println("Run mkdir crawler/micrawl/linkbd");
        pw.println("Run mkdir crawler/micrawl/crawlbd");

        // Add seeds
        for (int i = 0; i < configuration.getCrawlSystem().getSeeds().size(); i++) {
            pw.println("RUN echo " + configuration.getCrawlSystem().getSeeds().get(i) + " >> crawler/urls/seeds.txt");
        }

        // Add docker files
        pw.println("ADD juntarSalidas.sh crawler/juntarSalidas.sh");
        pw.println("ADD run.sh crawler/run.sh");

        // Put nutch-site file to its folder and then for each plugin, create
        // its folder with its name and put there its jars and xml file,
        pw.println("ADD nutch-site.xml crawler/conf/nutch-site.xml");
        pw.println();
        configurePlugins(configuration, directoryName, pw);
    }

    private void configurePlugins(CrawlConfiguration configuration, String directoryName, PrintWriter pw)
            throws IOException {
    	
    	List<Plugin> plugins = configuration.getCrawlSystem().getPlugins();
    	if (plugins == null)
            return;
        for (Plugin nextPlugin : plugins) {
        	//For each plugin
        	String pluginName = nextPlugin.getName();
        	//creates the plugin folder
            pw.println("Run mkdir crawler/plugins/" + pluginName);

        	List<File> files= nextPlugin.getFiles();
  		  Path directory = FileSystems.getDefault().getPath(directoryName, pluginName);
            Files.createDirectory(directory);
        	for (File file : files) {
        		//Copy files near the docker file (needed)
                  Path target = directory.resolve(file.getName());
                  Path source = file.toPath();

                  Files.copy(source, target);
                  //Add to dockerfile
                  pw.println("ADD " + pluginName+"/"+file.getName()+ " crawler/plugins/" + pluginName + "/"+file.getName());
                

        	}
        }
        
    }

    public void createNutchSite(CrawlConfiguration configuracion, String directoryName) {
        ArrayList<Property> properties = new ArrayList<>();
        // Add possible configurations
        properties.add(new Property("http.agent.name", directoryName));
        properties.add(new Property("http.content.limit", configuracion.getCrawlSystem().getMaxFileLength()));
        properties.add(new Property("file.content.limit", configuracion.getCrawlSystem().getMaxFileLength()));
        properties.add(new Property("db.max.outlinks.per.page", configuracion.getCrawlSystem().getLinksLimitURL()));
        properties.add(new Property("db.max.inlinks", configuracion.getCrawlSystem().getLinksLimitURL()));
        properties.add(new Property("fetcher.queue.mode", configuracion.getCrawlSystem().getQueueMode()));
        properties.add(new Property("fetcher.max.crawl.delay", configuracion.getCrawlSystem().getMaxCrawlDelay()));
        if (!isNullOrEmpty(configuracion.getCrawlSystem().getTimeouts())) {
            properties
                    .add(new Property("http.max.delays", configuracion.getCrawlSystem().getTimeouts().getFetchTimes()));
            properties.add(new Property("http.timeout", configuracion.getCrawlSystem().getTimeouts().getNetwork()));
            properties.add(new Property("parser.timeout", configuracion.getCrawlSystem().getTimeouts().getParser()));
        }
        properties.add(new Property("plugin.includes", pluginsValue(configuracion.getCrawlSystem().getPlugins())));

        // Creates the nutch-site file which contains all the customized nutch
        // configurations

        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "nutch-site.xml")))) {
            pw.println("<?xml version=\"1.0\"?>");
            pw.println("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>");
            pw.println("<configuration>");

            for (Property property : properties) {
                addProperty(pw, property);
            }

            pw.println("</configuration>");
        } catch (IOException e) {
        	LOGGER.warn("IOException: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Add a propperty to a file
     */
    private void addProperty(PrintWriter pw, Property prop) {
        if (!Strings.isNullOrEmpty(prop.getValue())) {
            pw.println("	<property>");
            pw.println("		<name>");
            pw.println("		" + prop.getName());
            pw.println("		</name>");
            pw.println("		<value>");
            pw.println("		" + prop.getValue());
            pw.println("		</value>");
            pw.println("	</property>");
        }
    }

    /**
     * Check whether the configuration field is empty or not
     */
    private boolean isNullOrEmpty(Object campo) {
        return campo == null || campo.toString().equals("");
    }

    /**
     * returns the value property of a plugin
     */
    private String pluginsValue(List<Plugin> list) {
    	
        String pluginsOR = "";
        boolean hayPlugin = false;
        for (int i = 0; list != null && i < list.size(); i++) {
            pluginsOR += list.get(i).getName() + "|";
            hayPlugin = true;
        }
        // Returns the result
        if (hayPlugin) {
            pluginsOR += "protocol-http|urlfilter-regex|parse-(text|html|js)|index-basic|query-(basic|site|url)|summary-basic|scoring-opic|urlnormalizer-(pass|regex|basic)";
            return pluginsOR;
        } else
            return null;
    }
  

}
