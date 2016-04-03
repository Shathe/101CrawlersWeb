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
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

/**
 * Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing. Run
 * it with no command-line arguments for usage information.
 */
public class IndexFiles {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexFiles.class);

	public IndexFiles() {
	}

	/**
	 * Indexes the given file using the given writer, or if a directory is
	 * given, recurses over files and directories found under the given
	 * directory.
	 *
	 * NOTE: This method indexes one document per input file. This is slow. For
	 * good throughput, put multiple documents into your input file(s). An
	 * example of this is in the benchmark module, which can create "line doc"
	 * files, one document per line, using the <a href=
	 * "../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
	 * >WriteLineDocTask</a>. If there is a low-level I/O error
	 */
	 public void indexDocs(IndexWriter writer, File file) throws IOException {
		// file ins salida.txt where the structure is (URI + parsetext)+
		/*
		 * Recno:: 0 URL:: http://www.unizar.es/
		 *
		 * ParseText:: blablabla
		 */

		if (file.canRead()) {

			try(Scanner scan  = new Scanner(new FileInputStream(file))) {

				// make a new, empty document

				// Add the path of the file as a field named "path". Use a
				// field that is indexed (i.e. searchable), but don't
				// tokenize
				// the field into separate words and don't index term
				// frequency
				// or positional information:
				// Add the last modified date of the file a field named
				// "modified".
				// Use a LongField that is indexed (i.e. efficiently
				// filterable with
				// NumericRangeFilter). This indexes to milli-second
				// resolution, which
				// is often too fine. You could instead create a number
				// based on
				// year/month/day/hour/minutes/seconds, down the resolution
				// you require.
				// For example the long value 2011021714 would mean
				// February 17, 2011, 2-3 PM.
				LOGGER.info("adding ");
				int i=0;
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					if (line.contains("Recno::")) {
						// fichero
						String url = scan.nextLine();
						scan.nextLine();
						scan.nextLine();
						String content = scan.nextLine();
						url = url.replace("URL:: ", "");

						Document doc = new Document();
						insertInIndex(url, "url", doc, "text");
						insertInIndex(content, "content", doc, "text");

						// ya se ha aacbado el fichero
						if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
							// New index, so we just add the
							// document (no old
							// document can be there):

							writer.addDocument(doc);
						} else {
							// Existing index (an old copy of this
							// document may have
							// been indexed) so
							// we use updateDocument instead to
							// replace the old one
							// matching the exact
							// path, if present:

							writer.updateDocument(new Term("path", file.getPath()), doc);
						}
						if(i%100==0)
							LOGGER.info(i+" lines");
						i++;

					}
					// siguiente linea
				}

				// Add the contents of the file to a field named "contents".
				// Specify a Reader,
				// so that the text of the file is tokenized and indexed,
				// but not stored.
				// Note that FileReader expects the file to be in UTF-8
				// encoding.
				// If that's not the case searching for special characters
				// will fail
				LOGGER.info("added "+i);
			}
		}
	}

    public void insertInIndex(String contenido, String campo, Document doc, String tipo) {

		if (tipo.toLowerCase().contains("string")) {
			Field pathField = new StringField(campo, contenido, Field.Store.YES);
			doc.add(pathField);
		}
		if (tipo.toLowerCase().contains("text")) {
			doc.add(new TextField(campo, contenido, Field.Store.YES));

		}

	}

	/**
	 * Index all text files under a directory.
	 */
	public void index(String indexPath,File file) {
		
		if (file.exists()) {

			Date start = new Date();
			try {
				LOGGER.info("Indexing to directory '" + indexPath );
				Directory dir = FSDirectory.open(new File(indexPath));
				Analyzer analyzer = new EnglishAnalyzer();
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);

				iwc.setOpenMode(OpenMode.CREATE);

				// Optional: for better indexing performance, if you
				// are indexing many documents, increase the RAM
				// buffer. But if you do this, increase the max heap
				// size to the JVM (eg add -Xmx512m or -Xmx1g):
				//
				// iwc.setRAMBufferSizeMB(256.0);
				IndexWriter writer = new IndexWriter(dir, iwc);
				indexDocs(writer, file);

				// NOTE: if you want to maximize search performance,
				// you can optionally call forceMerge here. This can be
				// a terribly costly operation, so generally it's only
				// worth it when your index is relatively static (ie
				// you're done adding documents to it):
				//
				// writer.forceMerge(1);
				writer.close();

				Date end = new Date();
				LOGGER.info(end.getTime() - start.getTime() + " total milliseconds");

			} catch (IOException e) {
				LOGGER.error(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
			}
		} else {
			LOGGER.info(file.getName() + " don't exists");
		}
	}
}
