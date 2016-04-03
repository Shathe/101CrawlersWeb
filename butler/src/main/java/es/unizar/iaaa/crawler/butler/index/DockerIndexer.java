
package es.unizar.iaaa.crawler.butler.index;

import java.io.File;

public class DockerIndexer {

	public DockerIndexer() {
	}

	/**
	 * Index all text files under a directory.
	 */
	public void indexDocker(String indexPath, String file) {
		IndexFiles nuevo = new IndexFiles();
		nuevo.index(indexPath,new File(file));
	}
}