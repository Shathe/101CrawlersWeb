package es.unizar.iaaa.crawler.butler.index;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.unizar.iaaa.crawler.butler.model.SearchResult;

public class RWFicheros {

	/**
	 * Metodo que coge los resultados de la query y los escribe en un fichero de
	 * texto
	 * 
	 * @param ficheroSalida
	 * @param numTotalHits
	 * @param results
	 * @param hits
	 * @param searcher
	 */
	public ArrayList<SearchResult>  escribirFicheroSalida(String ficheroSalida,
			int numTotalHits, TopDocs results, ScoreDoc[] hits,
			IndexSearcher searcher) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		ArrayList<SearchResult> result= new ArrayList<>();
		try {
			fichero = new FileWriter(ficheroSalida, false);
			pw = new PrintWriter(fichero);
			Document doc;
			String url;
			String content;
			// escribimos cada resultado de la busqueda
			for (int i = 0; i < numTotalHits; i++) {
				doc = searcher.doc(hits[i].doc);
				url = doc.get("url");
				if (url != null) {
					pw.println(url);
				}
				content = doc.get("content");
				if (content != null) {
					pw.println(content);
				}
				pw.println();
				result.add(new SearchResult(url,content));
			}
			pw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Metodo que lee un fichero XML y devuelve una lista con las necesidades
	 * expresadas en dicho fichero
	 * 
	 * @param ficheroXML
	 */
	public  ArrayList<String> leerXML(String ficheroXML) {
		File file = new File(ficheroXML);
		ArrayList<String> listaNecesidades = new ArrayList<String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb;
		try {
			docb = factory.newDocumentBuilder();
			org.w3c.dom.Document docP = docb.parse(file);
			// queremos parsear la etiqueta de informationNeed
			NodeList nodos = docP.getElementsByTagName("informationNeed");
			// leemos el identificador y el texto de cada necesidad
			for (int i = 0; i < nodos.getLength(); i++) {
				String texto = "";
				// para cada etiqueta de informationNeed cogemos la
				// etiqueta identifier y text
				NodeList nodoIdentifier = docP
						.getElementsByTagName("identifier");
				NodeList nodoText = docP.getElementsByTagName("text");
				Element identifier = (Element) nodoIdentifier.item(i);
				Element text = (Element) nodoText.item(i);
				texto += identifier.getTextContent().toLowerCase() + " "
						+ text.getTextContent().toLowerCase();
				// las guardamos en un array list
				listaNecesidades.add(texto);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return listaNecesidades;
	}
}
