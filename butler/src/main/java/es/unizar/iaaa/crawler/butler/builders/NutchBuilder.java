/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NutchBuilder implements CrawlerBuilder {

	private final String directoryName;
	private Configuration configuracion;

	public NutchBuilder(Configuration config, String directoryName) {
		configuracion = config;
		this.directoryName = directoryName;
	}

	public Configuration getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(Configuration configuracion) {
		this.configuracion = configuracion;
	}

	public void addDockerfile(PrintWriter pw) {
		/* Descarga y preparación de carpetas para nutch */
		pw.write("RUN svn checkout http://svn.apache.org/repos/asf/nutch/branches/branch-"
				+ configuracion.getCrawlSystem().getVersion() + "/ nutch_source && cd nutch_source && ant\n");
		pw.write("RUN ln -s nutch_source/runtime/local $HOME/crawler \n");
		pw.write("Run mkdir crawler/urls \n");
		pw.write("Run mkdir  crawler/salida  \n");
		pw.write("Run mkdir crawler/micrawl \n");
		pw.write("Run mkdir crawler/micrawl/segments \n");
		pw.write("Run mkdir crawler/micrawl/linkbd \n");
		pw.write("Run mkdir crawler/micrawl/crawlbd \n");

		/* Añade las seeds */
		for (int i = 0; i < configuracion.getCrawlSystem().getSeeds().size(); i++) {
			pw.write("RUN echo " + configuracion.getCrawlSystem().getSeeds().get(i) + " >> crawler/urls/seeds.txt"
					+ "\n");
		}

		/* Añade los ficheros a docker creados */
		pw.write("ADD " + "juntarSalidas.sh" + " crawler/juntarSalidas.sh" + "\n");
		pw.write("ADD " + "run.sh" + " crawler/run.sh" + "\n");

		/*
		 * Pasar el fichero nutch-site a su carpeta correspondiente y después
		 * por cada plugin, crear su carpeta con su nombre, pasar allí sus jar y
		 * su plugin.xml con ese nombre
		 */
		pw.write("ADD " + "nutch-site.xml" + " crawler/conf/nutch-site.xml" + "\n\n");
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
				pw.write("ADD " + xmlName + " crawler/plugins/" + nombrePlugin + "/plugin.xml" + "\n");

				for (int j = 2; j < all.length; j++) {
					/* Para cada jar se añade */
					String jarName = all[j].split("/")[all[j].split("/").length - 1];
					copiarFichero(all[j], directoryName + "/" + jarName);
					pw.write("ADD " + jarName + " crawler/plugins/" + nombrePlugin + "/" + jarName + "\n");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void createNutchSite() {
		if (configuracion.isOk()) {
			createNutchSiteXml();
		}
	}

	private void createNutchSiteXml() {
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
			pw.write("<?xml version=\"1.0\"?>" + "\n");
			pw.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>" + "\n");
			pw.write("<configuration>" + "\n");

            for (Property property : properties) {
                property.add(pw);
            }

			pw.write("</configuration>" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/* Añade una property a un fichero */
	private void anadirProperty(PrintWriter pw, String nombre, String valor, String descripcion) {
		pw.write("	<property>" + "\n");
		pw.write("		<name>" + "\n");
		pw.write("		" + nombre + "\n");
		pw.write("		</name>" + "\n");
		pw.write("		<value>" + "\n");
		pw.write("		" + valor + "\n");
		pw.write("		</value>" + "\n");
		if (descripcion != null && !descripcion.equals("")) {
			pw.write("		<description>" + "\n");
			pw.write("		" + descripcion + "\n");
			pw.write("		</description>" + "\n");
		}
		pw.write("	</property>" + "\n");

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
