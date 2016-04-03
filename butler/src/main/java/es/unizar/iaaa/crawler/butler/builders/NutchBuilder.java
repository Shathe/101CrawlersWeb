/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import com.google.common.base.Strings;

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

/**
 * Builds the nutch system. Creates every file needed and writes in the dockerfile every command
 * needed
 */
@Component
public class NutchBuilder implements CrawlerBuilder {

    public void addDockerfile(CrawlConfiguration configuracion, String directoryName, PrintWriter pw) throws IOException {
        /* Download and preapare folder for nutch */
        /* Descarga y preparación de carpetas para nutch */
        pw.println("RUN svn checkout http://svn.apache.org/repos/asf/nutch/branches/branch-"
                + configuracion.getCrawlSystem().getVersion() + "/ nutch_source && cd nutch_source && ant");
        pw.println("RUN ln -s nutch_source/runtime/local $HOME/crawler");
        pw.println("Run mkdir crawler/" + directoryName);
        pw.println("Run mkdir crawler/urls");
        pw.println("Run mkdir crawler/salida");
        pw.println("Run mkdir crawler/micrawl");
        pw.println("Run mkdir crawler/micrawl/segments");
        pw.println("Run mkdir crawler/micrawl/linkbd");
        pw.println("Run mkdir crawler/micrawl/crawlbd");

        // TODO Use // for comments wihtin methods
		/* Add seeds */
		/* Añade las seeds */
        for (int i = 0; i < configuracion.getCrawlSystem().getSeeds().size(); i++) {
            pw.println("RUN echo " + configuracion.getCrawlSystem().getSeeds().get(i) + " >> crawler/urls/seeds.txt");
        }

		/* Add docker files */
		/* Añade los ficheros a docker creados */
        pw.println("ADD juntarSalidas.sh crawler/juntarSalidas.sh");
        pw.println("ADD run.sh crawler/run.sh");

		/*
		 * Put nutch-site file to its folder and then for each plugin, create
		 * its folder with its name and put there its jars and xml file Pasar el
		 * fichero nutch-site a su carpeta correspondiente y después por cada
		 * plugin, crear su carpeta con su nombre, pasar allí sus jar y su
		 * plugin.xml con ese nombre
		 */
        pw.println("ADD nutch-site.xml crawler/conf/nutch-site.xml");
        pw.println();
        configurePlugins(configuracion, directoryName, pw);
    }

    private void configurePlugins(CrawlConfiguration configuration, String directoryName, PrintWriter pw)
            throws IOException {
        final List<List<String>> plugins = configuration.getCrawlSystem().getPlugins();
        if (plugins == null)
            return;
        for (List<String> all : plugins) {
            // Structure: nombre file.xml (file.jar)+
            String pluginName = all.get(0);

            pw.println("Run mkdir crawler/plugins/" + pluginName);
            // Create plugin.xml
            Path directory = FileSystems.getDefault().getPath(directoryName, pluginName);
            Path target = directory.resolve("plugin.xml");
            Path source = FileSystems.getDefault().getPath(all.get(1));
            Files.createDirectory(directory);
            Files.copy(source, target);
            pw.println("ADD " + pluginName + "/plugin.xml crawler/plugins/" + pluginName + "/plugin.xml");
            // Create (file.jar)+

            for (int siguiente = 2; siguiente < all.size(); siguiente++) {
                source = FileSystems.getDefault().getPath(all.get(siguiente));
                target = directory.resolve(source.getFileName());
                Files.copy(source, target);
                pw.println("ADD " + pluginName + "/" + target.getFileName() + " crawler/plugins/" + pluginName + "/"
                        + source.getFileName());
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
        if (!campoVacio(configuracion.getCrawlSystem().getTimeouts())) {
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
                anadirProperty(pw, property);
            }

            pw.println("</configuration>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a propperty to a file
     */
    private void anadirProperty(PrintWriter pw, Property prop) {
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
    private boolean campoVacio(Object campo) {
        return campo == null || campo.toString().equals("");
    }

    /**
     * returns the value property of a plugin
     */
    private String pluginsValue(List<List<String>> list) {
        String pluginsOR = "";
        boolean hayPlugin = false;
        for (int i = 0; list != null && i < list.size(); i++) {
            pluginsOR += list.get(i).get(0) + "|";
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
