/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

<<<<<<< HEAD
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
=======
import es.unizar.iaaa.crawler.butler.yalm.Configuration;

import java.io.FileWriter;
import java.io.IOException;
>>>>>>> 5a23b7e7125595cf928ede0f70b89763eea9ce0e
import java.io.PrintWriter;
import java.util.Random;

public class NutchBuilder implements CrawlerBuilder {

<<<<<<< HEAD
	private final String resources;
	private final String directoryName;
	private Configuration configuracion;

	public NutchBuilder(Configuration config, String resources, String directoryName) {
		configuracion = config;
		this.resources = resources;
		this.directoryName = directoryName;
	}

	public Configuration getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(Configuration configuracion) {
		this.configuracion = configuracion;
	}

	public void anadirDockerfile(PrintWriter pw) {
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
		// Path juntarSalidasURI = Paths.get(new
		// File("juntarSalidas.sh").toURI());
		// Path runURI = Paths.get(new File("run.sh").toURI());

		pw.write("ADD " + "juntarSalidas.sh" + " crawler/juntarSalidas.sh" + "\n");
		pw.write("ADD " + "run.sh" + " crawler/run.sh" + "\n");

		/*
		 * Pasar el fichero nutch-site a su carpeta correspondiente y después
		 * por cada plugin, crear su carpeta con su nombre, pasar allí sus jar y
		 * su plugin.xml con ese nombre
		 */
		// Path nutchSite = Paths.get(new
		// File("nutch-site.xml").toURI());
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
			// Path xmlURI = Paths.get(new File(all[1]).toURI());
			String xmlName = all[1].split("/")[all[1].split("/").length - 1];
			try {
				copiarFichero(all[1], directoryName + "/" + xmlName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			pw.write("ADD " + xmlName + " crawler/plugins/" + nombrePlugin + "/plugin.xml" + "\n");

			for (int j = 2; j < all.length; j++) {
				// jars
				// Path jarURI = Paths.get(new File(all[j]).toURI());
				String jarName = all[j].split("/")[all[j].split("/").length - 1];
				try {
					copiarFichero(all[j], directoryName + "/" + jarName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				pw.write("ADD " + jarName + " crawler/plugins/" + nombrePlugin + "/" + jarName + "\n");

			}

		}
	}

	public void crearNutchSite() {
		if (configuracion.isOk()) {

			FileWriter fichero = null;
			PrintWriter pw = null;
			try {
				File theDir = new File(directoryName);

				/*
				 * Crea el fichero nutch-site.xml el cual contiene todas las
				 * configuraciones personalizadas de nutch
				 */

				fichero = new FileWriter(theDir + "/nutch-site.xml");
				pw = new PrintWriter(fichero);
				pw.write("<?xml version=\"1.0\"?>" + "\n");
				pw.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>" + "\n");
				pw.write("<configuration>" + "\n");

				/* Añadimos el nombre aleatorio */
				anadirProperty(pw, "http.agent.name", directoryName, "");

				/*
				 * Añadimos si está en la configuración el tamaño máximo del
				 * fichero
				 */
				if (!campoVacio(configuracion.getCrawlSystem().getMaxFileLength())) {
					anadirProperty(pw, "http.content.limit", configuracion.getCrawlSystem().getMaxFileLength(), "");
					anadirProperty(pw, "file.content.limit", configuracion.getCrawlSystem().getMaxFileLength(), "");
				} else {
					anadirProperty(pw, "http.content.limit", "-1", "");
					anadirProperty(pw, "file.content.limit", "-1", "");
				}

				/* Añadimos si está en la configuración el el máximo de urls */
				if (!campoVacio(configuracion.getCrawlSystem().getLinksLimitURL())) {
					anadirProperty(pw, "db.max.outlinks.per.page", configuracion.getCrawlSystem().getLinksLimitURL(),
							"");
					anadirProperty(pw, "db.max.inlinks", configuracion.getCrawlSystem().getLinksLimitURL(), "");
				} else {
					anadirProperty(pw, "db.max.outlinks.per.page", "-1", "");
					anadirProperty(pw, "db.max.inlinks", "-1", "");
				}

				/*
				 * Añadimos si está en la configuración el modo de cola si no es
				 * el valor por defecto
				 */
				if (!campoVacio(configuracion.getCrawlSystem().getQueueMode())
						&& !configuracion.getCrawlSystem().getQueueMode().toLowerCase().equals("byhost")) {
					anadirProperty(pw, "fetcher.queue.mode", configuracion.getCrawlSystem().getQueueMode(), "");

				}

				/* Añadimos si está en la configuración el delay del fetch */
				if (!campoVacio(configuracion.getCrawlSystem().getMaxCrawlDelay())) {
					anadirProperty(pw, "fetcher.max.crawl.delay", configuracion.getCrawlSystem().getMaxCrawlDelay(),
							"");

				}

				if (configuracion.getCrawlSystem().getTimeouts() != null) {
					/* Añadimos si está en la configuración el fetch timeout */
					if (!campoVacio(configuracion.getCrawlSystem().getTimeouts().getFetchTimes())) {
						anadirProperty(pw, "http.max.delays",
								configuracion.getCrawlSystem().getTimeouts().getFetchTimes(), "");

					}

					/*
					 * Añadimos si está en la configuración timeout del network
					 */
					if (!campoVacio(configuracion.getCrawlSystem().getTimeouts().getNetwork())) {
						anadirProperty(pw, "http.timeout", configuracion.getCrawlSystem().getTimeouts().getNetwork(),
								"");

					}

					/*
					 * Añadimos si está en la configuración el timeout del
					 * parser
					 */
					if (!campoVacio(configuracion.getCrawlSystem().getTimeouts().getParser())) {
						anadirProperty(pw, "parser.timeout", configuracion.getCrawlSystem().getTimeouts().getParser(),
								"");

					}
				}
				/* Añadimos si está en la configuración los plugins */
				String pluginsOR = "";
				boolean hayPlugin = false;
				for (int i = 0; configuracion.getCrawlSystem().getPlugins() != null
						&& !configuracion.getCrawlSystem().getPlugins().isEmpty()
						&& i < configuracion.getCrawlSystem().getPlugins().size(); i++) {
					pluginsOR += configuracion.getCrawlSystem().getPlugins().get(i).split(" ")[0];
					hayPlugin = true;
				}
				pluginsOR += "protocol-http|urlfilter-regex|parse-(text|html|js)|index-basic|query-(basic|site|url)|summary-basic|scoring-opic|urlnormalizer-(pass|regex|basic)";

				if (hayPlugin) {

					anadirProperty(pw, "plugin.includes", pluginsOR,
							"Regular expression naming plugin directory names to"
									+ "  include.  Any plugin not matching this expression is excluded."
									+ "  In any case you need at least include the nutch-extensionpoints plugin. By"
									+ "  default Nutch includes crawling just HTML and plain text via HTTP,"
									+ "  and basic indexing and search plugins.");
				}
				pw.write("</configuration>" + "\n");

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
	private boolean campoVacio(String campo) {
		return campo == null || campo.equals("");
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
=======
    private final String resources;
    private Configuration configuracion;

    public NutchBuilder(Configuration config, String resources) {
        configuracion = config;
        this.resources = resources;
    }

    public Configuration getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuration configuracion) {
        this.configuracion = configuracion;
    }

    public void crearNutchSite() {
        if (configuracion.isOk()) {
            createNutchSiteXml();
        }
    }

    /*
     * Crea el fichero nutch-site.xml el cual contiene todas las configuraciones
     * personalizadas de nutch
     */
    private  void createNutchSiteXml()  {

        try(PrintWriter pw = new PrintWriter(new FileWriter("nutch-site.xml"))) {
            pw.write("<?xml version=\"1.0\"?>" + "\n");
            pw.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>" + "\n");
            pw.write("<configuration>" + "\n");

				/* Añadimos el nombre aleatorio*/
            anadirProperty(pw, "http.agent.name", getCadenaAlfanumAleatoria(10), "");

				/* Añadimos si está en la configuración el tamaño máximo del fichero */
            if (!campoVacio(configuracion.getCrawlSystem().getMaxFileLength())) {
                anadirProperty(pw, "http.content.limit", configuracion.getCrawlSystem().getMaxFileLength(), "");
                anadirProperty(pw, "file.content.limit", configuracion.getCrawlSystem().getMaxFileLength(), "");
            } else {
                anadirProperty(pw, "http.content.limit", "-1", "");
                anadirProperty(pw, "file.content.limit", "-1", "");
            }

				/* Añadimos si está en la configuración el el máximo de urls*/
            if (!campoVacio(configuracion.getCrawlSystem().getLinksLimitURL())) {
                anadirProperty(pw, "db.max.outlinks.per.page", configuracion.getCrawlSystem().getLinksLimitURL(),
                        "");
                anadirProperty(pw, "db.max.inlinks", configuracion.getCrawlSystem().getLinksLimitURL(), "");
            } else {
                anadirProperty(pw, "db.max.outlinks.per.page", "-1", "");
                anadirProperty(pw, "db.max.inlinks", "-1", "");
            }

				/* Añadimos si está en la configuración el modo de cola si no es el valor por defecto */
            if (!campoVacio(configuracion.getCrawlSystem().getQueueMode()) &&
                    !configuracion.getCrawlSystem().getQueueMode().toLowerCase().equals("byhost")) {
                anadirProperty(pw, "fetcher.queue.mode", configuracion.getCrawlSystem().getQueueMode(), "");

            }

				/* Añadimos si está en la configuración el delay del fetch */
            if (!campoVacio(configuracion.getCrawlSystem().getMaxCrawlDelay())) {
                anadirProperty(pw, "fetcher.max.crawl.delay", configuracion.getCrawlSystem().getMaxCrawlDelay(), "");

            }

				/* Añadimos si está en la configuración el fetch timeout */
            if (!campoVacio(configuracion.getCrawlSystem().getTimeouts().getFetchTimes())) {
                anadirProperty(pw, "http.max.delays", configuracion.getCrawlSystem().getTimeouts().getFetchTimes(), "");

            }

				/* Añadimos si está en la configuración timeout del network */
            if (!campoVacio(configuracion.getCrawlSystem().getTimeouts().getNetwork())) {
                anadirProperty(pw, "http.timeout", configuracion.getCrawlSystem().getTimeouts().getNetwork(), "");

            }

				/* Añadimos si está en la configuración el timeout del parser */
            if (!campoVacio(configuracion.getCrawlSystem().getTimeouts().getParser())) {
                anadirProperty(pw, "parser.timeout", configuracion.getCrawlSystem().getTimeouts().getParser(), "");

            }

				/* Añadimos si está en la configuración los plugins */
            String pluginsOR = "";
            for (int i = 0; configuracion.getCrawlSystem().getPlugins() != null && !configuracion.getCrawlSystem().getPlugins().isEmpty()
                    && i < configuracion.getCrawlSystem().getPlugins().size(); i++) {
                pluginsOR += configuracion.getCrawlSystem().getPlugins().get(i).split(" ")[0];
                if (i < configuracion.getCrawlSystem().getPlugins().size() - 1)
                    pluginsOR += "|";
            }
            anadirProperty(pw, "plugin.includes", pluginsOR,
                    "Regular expression naming plugin directory names to"
                            + "  include.  Any plugin not matching this expression is excluded."
                            + "  In any case you need at least include the nutch-extensionpoints plugin. By"
                            + "  default Nutch includes crawling just HTML and plain text via HTTP,"
                            + "  and basic indexing and search plugins.");

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

    /* Comprueba si en la confiuración ese campo está vacío o no */
    private boolean campoVacio(String campo) {
        return campo == null || campo.equals("");
    }
>>>>>>> 5a23b7e7125595cf928ede0f70b89763eea9ce0e
}
