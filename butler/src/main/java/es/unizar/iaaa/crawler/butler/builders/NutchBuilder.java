/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import com.google.common.base.Strings;
import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class NutchBuilder implements CrawlerBuilder {

	public void addDockerfile(CrawlConfiguration configuracion, String directoryName, PrintWriter pw) {
		/* Descarga y preparación de carpetas para nutch */
		pw.println("RUN svn checkout http://svn.apache.org/repos/asf/nutch/branches/branch-"
				+ configuracion.getCrawlSystem().getVersion() + "/ nutch_source && cd nutch_source && ant");
		pw.println("RUN ln -s nutch_source/runtime/local $HOME/crawler");
		pw.println("Run mkdir crawler/"+directoryName);
		pw.println("Run mkdir crawler/urls");
		pw.println("Run mkdir crawler/salida");
		pw.println("Run mkdir crawler/micrawl");
		pw.println("Run mkdir crawler/micrawl/segments");
		pw.println("Run mkdir crawler/micrawl/linkbd");
		pw.println("Run mkdir crawler/micrawl/crawlbd");

		/* Añade las seeds */
		for (int i = 0; i < configuracion.getCrawlSystem().getSeeds().size(); i++) {
			pw.println("RUN echo " + configuracion.getCrawlSystem().getSeeds().get(i) + " >> crawler/urls/seeds.txt");
		}

		/* Añade los ficheros a docker creados */
		pw.println("ADD juntarSalidas.sh crawler/juntarSalidas.sh");
		pw.println("ADD run.sh crawler/run.sh");

		/*
		 * Pasar el fichero nutch-site a su carpeta correspondiente y después
		 * por cada plugin, crear su carpeta con su nombre, pasar allí sus jar y
		 * su plugin.xml con ese nombre
		 */
		pw.println("ADD nutch-site.xml crawler/conf/nutch-site.xml");
        pw.println();
		/* Para cada plugin */
		for (int i = 0; configuracion.getCrawlSystem().getPlugins() != null
				&& !configuracion.getCrawlSystem().getPlugins().isEmpty()
				&& i < configuracion.getCrawlSystem().getPlugins().size(); i++) {
			/* Estructura: nombre file.xml (file.jar)+ */
			String[] all = configuracion.getCrawlSystem().getPlugins().get(i).split(" ");
			String plugin = all[0];
			/* Creas la carpeta para cada plugin */
			String nombrePlugin = plugin.split("/")[plugin.split("/").length - 1];
			pw.write("Run mkdir crawler/plugins/" + nombrePlugin + "\n");
			/* Se añade su fichero de configuracion */
			String xmlName = all[1].split("/")[all[1].split("/").length - 1];
			try {
				copiarFichero(all[1], directoryName + "/" + xmlName);
				pw.println("ADD " + xmlName + " crawler/plugins/" + nombrePlugin + "/plugin.xml");

				for (int j = 2; j < all.length; j++) {
					/* Para cada jar se añade */
					String jarName = all[j].split("/")[all[j].split("/").length - 1];
					copiarFichero(all[j], directoryName + "/" + jarName);
					pw.println("ADD " + jarName + " crawler/plugins/" + nombrePlugin + "/" + jarName);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void createNutchSite(CrawlConfiguration configuracion, String directoryName) {
		ArrayList<Property> properties = new ArrayList<>();
		/* Añadimos posibles configuraciones */
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

			/*
			 * Crea el fichero nutch-site.xml el cual contiene todas las
			 * configuraciones personalizadas de nutch
			 */

		try(PrintWriter pw = new PrintWriter(new FileWriter(new File(directoryName, "nutch-site.xml")))) {
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

	/* Añade una property a un fichero */
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

	/* Genera una cadena aleatoria */

	/* Comprueba si en la confiuración ese campo está vacío o no */
	private boolean campoVacio(Object campo) {
		return campo == null || campo.toString().equals("");
	}

	private void copiarFichero(String origenF, String destinoF) throws Exception {
		File origen = new File(origenF);
		File destino = new File(destinoF);
		InputStream in = new FileInputStream(origen);
		OutputStream out = new FileOutputStream(destino);
		byte[] buf = new byte[1024];
		int len;

		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();

	}

	private String pluginsValue(List<String> list) {
		String pluginsOR = "";
		boolean hayPlugin = false;
		for (int i = 0; list != null && i < list.size(); i++) {
			pluginsOR += list.get(i).split(" ")[0] + "|";
			hayPlugin = true;
		}
		/* Devuelves el resultado */
		if (hayPlugin) {
			pluginsOR += "protocol-http|urlfilter-regex|parse-(text|html|js)|index-basic|query-(basic|site|url)|summary-basic|scoring-opic|urlnormalizer-(pass|regex|basic)";
			return pluginsOR;
		} else
			return null;
	}

}
