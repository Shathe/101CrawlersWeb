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
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.DocumentBuilder;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing. Run
 * it with no command-line arguments for usage information.
 */
public class IndexFiles {

	public IndexFiles() {
	}

	/**
	 * Index all text files under a directory.
	 */
	public void index(File file) {
		String indexPath = "index";
		if (file.exists()) {

			try {
				FileUtils.deleteDirectory(new File(indexPath));
			} catch (IOException e1) {
			}
			Date start = new Date();
			try {
				System.out.println("Indexing to directory '" + indexPath + "'...");

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
				System.out.println(end.getTime() - start.getTime() + " total milliseconds");

			} catch (IOException e) {
				System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
			}
		} else {
			System.out.println(file.getName() + " don't exists");
		}
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
	static void indexDocs(IndexWriter writer, File file) throws IOException {
		// file ins salida.txt where the structure is (URI + parsetext)+
		/*
		 * Recno:: 0 URL:: http://www.unizar.es/
		 * 
		 * ParseText:: blablabla
		 */

		if (file.canRead()) {

			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException fnfe) {
				// at least on windows, some temporary files raise this
				// exception with an "access denied" message
				// checking if the file can be read doesn't help
				return;
			}

			try {

				// make a new, empty document

				// Add the path of the file as a field named "path". Use a
				// field that is indexed (i.e. searchable), but don't
				// tokenize
				// the field into separate words and don't index term
				// frequency
				// or positional information:
				Scanner scan = new Scanner(fis);
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

				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					if (line.contains("Recno::")) {
						// fichero
						String url = scan.nextLine();
						url = url.replace("URL:: ", "");
						scan.nextLine();
						scan.nextLine();
						String content = scan.nextLine();

						Document doc = new Document();
						insertarenIndice(url, "url", doc, "text");
						insertarenIndice(content, "content", doc, "text");
						System.out.println("adding " + url);

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

			} finally {
				fis.close();
			}

		}
	}

	public static void insertarenIndice(String contenido, String campo, Document doc, String tipo) {

		if (tipo.toLowerCase().contains("string")) {
			Field pathField = new StringField(campo, contenido, Field.Store.YES);
			doc.add(pathField);
		}
		if (tipo.toLowerCase().contains("text")) {
			doc.add(new TextField(campo, contenido, Field.Store.YES));

		}

	}
}
