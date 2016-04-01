/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.unizar.iaaa.crawler.butler.index;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import es.unizar.iaaa.crawler.butler.model.SearchResult;

/** Simple command-line based search demo. */
public class SearchFiles {

	static String ficheroSalida = "result.txt";
	final static int NUM_MAXIMO_DOCS = 7052;

	public SearchFiles() {
	}
	/** Simple command-line based search demo. */
	public ArrayList<SearchResult> search(String queryS) throws Exception {
		
		String index = "index";
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
				index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new EnglishAnalyzer();

		// para cada necesidad se preprocesa y se lanza una query

			String line =queryS;

			line = line.trim();
			// quiamos signos de puntuacion para el prefiltrado
			line = line.replace(".", " ");
			line = line.replace(",", " ");
			line = line.replace("(", " ");
			line = line.replace(")", " ");
			line = line.replace(";", " ");
			line = line.replace(":", " ");
			line = line.replace("-", " ");

			// quitamos palabrasvacias de signifcado y expanimos la consulta
			// semanticamente

			Query queryMultiple = null;
			BooleanQuery query = new BooleanQuery();// valor este de la caja de
													// consulta

			
			// Consulta generica sobre todos los indices
			if (!line.equals("")) {
				String[] fields = { "content" };
				// DEBE APARECER EN EL TITULO ALGO SOBRE ESTO
				BooleanClause.Occur[] flags = { BooleanClause.Occur.SHOULD };
				queryMultiple = MultiFieldQueryParser.parse(line,
						fields, flags, analyzer);
				query.add(queryMultiple, BooleanClause.Occur.SHOULD);

			}

			ArrayList<SearchResult> resultado=doPagingSearch(searcher, query);
		
		reader.close();
		return resultado;
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 */
	public ArrayList<SearchResult>  doPagingSearch(IndexSearcher searcher, Query query) throws IOException {
		// searcher.setSimilarity(new BM25Similarity());
		TopDocs results = searcher.search(query, NUM_MAXIMO_DOCS);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		// hits = searcher.search(query, numTotalHits).scoreDocs;
		System.out.println(numTotalHits + " total matching documents");
		RWFicheros filer=new RWFicheros();
		return filer.escribirFicheroSalida(ficheroSalida, numTotalHits, results,
				hits, searcher);

	}
}
