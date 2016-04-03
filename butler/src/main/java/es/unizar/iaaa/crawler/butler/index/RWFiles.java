package es.unizar.iaaa.crawler.butler.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.model.SearchResult;

public class RWFiles {

	private static final Logger LOGGER = LoggerFactory.getLogger(RWFiles.class);
	/**
	 * Metodo que coge los resultados de la query y los escribe en un fichero de
	 * texto
	 * 
	 * @param outputFile
	 * @param numTotalHits
	 * @param hits
	 * @param searcher
	 */
	public ArrayList<SearchResult> writeOutputFile(String outputFile,
												   int numTotalHits,  ScoreDoc[] hits,
												   IndexSearcher searcher) {
		ArrayList<SearchResult> result= new ArrayList<>();
		try(PrintWriter pw = new PrintWriter(new FileWriter(outputFile, false))) {
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
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return result;
	}
}
