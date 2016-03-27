package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.Application;
import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.validator.*;
import es.unizar.iaaa.crawler.butler.yalm.YamlConfigRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * Created by javier on 09/03/16.
 */

// TODO @Iñigo Documentar como Javadoc (recuerda /**)
/*
 * Test the configuration builder and validation
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={Application.class})
public class CoordinatorTest {

    @Autowired
    private ApplicationContext ctx;

	@Autowired
    private ConfigurationValidator configurationValidator;

    @Autowired
    private CrawlValidator crawlValidator;

    /** Detects if a well formed configuration file, pass the validation */
    @Test
    public void detectEverythingIsOK() throws URISyntaxException {
        CrawlConfiguration config;
        config = readConfiguration("conf.yml");
        assertNotNull("YamlConfigRunner debe devolver una configuración y no null", config);
        System.out.println(config.toString());

        ValidationResult result = configurationValidator.validate(config);
        assertTrue("DefaultValidator debe devolver que está bien", result.isOk());
        assertEquals("DefaultValidator dbe dar OK", Validator.Status.OK, result.getFirstErrorCode());
    }

	/**
	 * Detects if a well formed plugin is detected as such 
	 */
    @Test
    public void detectSuportedPlugin() throws URISyntaxException {
        CrawlConfiguration config;
        config = readConfiguration("conf-Well-Plugin.yml");
        Validator validator = new CrawlPluginsValidator();
        ValidationResult result = validator.validate(config);
        assertTrue("DefaultValidator no informa que es una buena configuración", result.isOk());
   
    }

	/**
	 * Detects if a bad formed configuration file, dont pass the validation the
	 * error is a crawl system not supported
	 */
    @Test
    public void detectUnsupportedCrawl() throws URISyntaxException {
        CrawlConfiguration config;
        config = readConfiguration("conf-erroresNutch.yml");
        ValidationResult result = crawlValidator.validate(config);
        assertFalse("DefaultValidator no informa que es una mala configuración", result.isOk());
        assertEquals("DefaultValidator no informa del tipo de error",
                Validator.Status.ERROR_UNSUPPORTED_CRAWL_VERSION, result.getFirstErrorCode());
        assertEquals("DefaultValidator no informa del que el error es el valor 1.4", "1.4",
                result.getFirstErrorValue());
    }

	/**
	 * Detects if a bad formed configuration file, dont pass the validation the
	 * error is a bad seed 
	 */
    @Test
    public void detectUnsupportedSeedsCrawl() throws URISyntaxException {
        CrawlConfiguration config;
        config = readConfiguration("conf-erroresNutchSeeds.yml");
        Validator validator = new CrawlSeedsValidator();
        ValidationResult result = validator.validate(config);
        assertFalse("DefaultValidator no informa que es una mala configuración", result.isOk());
        assertEquals("DefaultValidator no informa del tipo de error",
                Validator.Status.ERROR_UNSUPPORTED_CRAWL_SEEDS, result.getFirstErrorCode());
    }

	/**
	 * Detects if a bad formed configuration file, dont pass the validation
	 * The plugin has no xml file
	 */
    @Test
    public void detectUnsupportedplugCrawl() throws URISyntaxException {
        CrawlConfiguration config;
        config = readConfiguration("conf-erroresNutchPlugins.yml");
        Validator validator = new CrawlPluginsValidator();
        ValidationResult result = validator.validate(config);
        assertFalse("DefaultValidator no informa que es una mala configuración", result.isOk());
        assertEquals("DefaultValidator no informa del tipo de error",
                Validator.Status.ERROR_UNSUPPORTED_CRAWL_PLUGINS, result.getFirstErrorCode());
    }

	/**
	 * Detects if a well form configuration file, creates the building files
	 */
    @Test
	public void builder() throws URISyntaxException {
		String id="usuarioId_CrawlId";
		AdapterBuilder builder= ctx.getBean(AdapterBuilder.class);
		builder.createConfigurationFiles(readConfiguration("conf.yml"), id);
		assertEquals("DefaultValidator no informa del tipo de error",
				true, checkFileExists(id, "Dockerfile"));
		assertEquals("DefaultValidator no informa del tipo de error",
				true, checkFileExists(id, "nutch-site.xml"));
		assertEquals("DefaultValidator no informa del tipo de error",
				true, checkFileExists(id, "juntarSalidas.sh"));
		assertEquals("DefaultValidator no informa del tipo de error",
				true, checkFileExists(id, "run.sh"));
	}

    private Resource readPath(String route) throws URISyntaxException {
        return ctx.getResource("classpath:es/unizar/iaaa/crawler/butler/builders/"+route);
    }

    private CrawlConfiguration readConfiguration(String route) throws URISyntaxException {
        return YamlConfigRunner.read(readPath(route));
    }

    private boolean checkFileExists(String parent, String child) {
        return new File(parent, child).exists();
    }
}
