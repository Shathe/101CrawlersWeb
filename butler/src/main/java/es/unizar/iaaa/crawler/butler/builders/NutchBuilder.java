/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

public class NutchBuilder {

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

            FileWriter fichero = null;
            PrintWriter pw = null;
            try {
                /*
				 * Crea el fichero nutch-site.xml el cual contiene todas las configuraciones
				 * personalizadas de nutch
				 */

                fichero = new FileWriter("nutch-site.xml");
                pw = new PrintWriter(fichero);
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
}
