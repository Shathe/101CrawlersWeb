package es.unizar.iaaa.crawler.butler.builders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.Application;
import es.unizar.iaaa.crawler.butler.index.IndexFiles;
import es.unizar.iaaa.crawler.butler.index.SearchFiles;
import es.unizar.iaaa.crawler.butler.model.SearchResult;

/**
 * Test the configuration builder and validation
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@ActiveProfiles("test")
public class IndexerTest {

	@Autowired
	private ApplicationContext ctx;

    @Value("${butler.base}/")
	private String baseDir;

	/** Detects if a well formed configuration file, pass the validation */
	@Test
	public void detectEverythingIsOK() throws Exception {
		IndexFiles nuevo = new IndexFiles();
		nuevo.index("index",ctx.getResource(baseDir + "salida.txt").getFile());
		SearchFiles searcher = new SearchFiles();
		ArrayList<SearchResult> result = searcher.search("","stop start crawler");
		assertNotNull("The result shouldn't be null",result);
		assertTrue("The searcher should does not work propperly ",result.get(0).getUrl().equals("http://www.inigol.es/"));
	}

}
