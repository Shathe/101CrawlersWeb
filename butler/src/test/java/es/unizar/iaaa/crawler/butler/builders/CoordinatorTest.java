package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.builders.validator.*;
import es.unizar.iaaa.crawler.butler.yalm.Configuration;
import es.unizar.iaaa.crawler.butler.yalm.YamlConfigRunner;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by javier on 09/03/16.
 */
public class CoordinatorTest {

    @Test
    public void detectEverythingIsOK() throws URISyntaxException {
        Configuration config;
        config = readConfiguration("conf.yml");
        assertNotNull("YamlConfigRunner debe devolver una configuración y no null", config);
        System.out.println(config.toString());
        Validator validator = new ConfigurationValidator();
        ValidationResult result = validator.validate(config);
        assertTrue("DefaultValidator debe devolver que está bien", result.isOk());
        assertEquals("DefaultValidator dbe dar OK", Validator.ErroresValidar.OK, result.getFirstErrorCode());
    }

    @Test
    public void detectUnsupportedOS() throws URISyntaxException {
        Configuration config;
        config = readConfiguration("conf-erroresDocker.yml");
        Validator validator = new OSVersionValidator();
        ValidationResult result = validator.validate(config);
        assertFalse("DefaultValidator no informa que es una mala configuración", result.isOk());
        assertEquals("DefaultValidator no informa del tipo de error",
                Validator.ErroresValidar.ERROR_UNSUPPORTED_OS_VERSION, result.getFirstErrorCode());
        assertEquals("DefaultValidator no informa del que el error es el valor 10.0", "10.0",
                result.getFirstErrorValue());
    }

    @Test
    public void detectUnsupportedCrawl() throws URISyntaxException {
        Configuration config;
        config = readConfiguration("conf-erroresNutch.yml");
        Validator validator = new CrawlValidator();
        ValidationResult result = validator.validate(config);
        assertFalse("DefaultValidator no informa que es una mala configuración", result.isOk());
        assertEquals("DefaultValidator no informa del tipo de error",
                Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_VERSION, result.getFirstErrorCode());
        assertEquals("DefaultValidator no informa del que el error es el valor 1.2", "1.2",
                result.getFirstErrorValue());
    }

    @Test
    public void detectUnsupportedSeedsCrawl() throws URISyntaxException {
        Configuration config;
        config = readConfiguration("conf-erroresNutchSeeds.yml");
        Validator validator = new CrawlSeedsValidator();
        ValidationResult result = validator.validate(config);
        assertFalse("DefaultValidator no informa que es una mala configuración", result.isOk());
        assertEquals("DefaultValidator no informa del tipo de error",
                Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_SEEDS, result.getFirstErrorCode());
    }

    @Test
    public void detectUnsupportedplugCrawl() throws URISyntaxException {
        Configuration config;
        config = readConfiguration("conf-erroresNutchPlugins.yml");
        Validator validator = new CrawlPluginsValidator();
        ValidationResult result = validator.validate(config);
        assertFalse("DefaultValidator no informa que es una mala configuración", result.isOk());
        assertEquals("DefaultValidator no informa del tipo de error",
                Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_PLUGINS, result.getFirstErrorCode());
    }


	@Test
	public void builder() throws URISyntaxException {
		String id="usuarioIdCrawlId";
		Path ruta=readPath("conf.yml");
		AdaptadorBuilder builder= new AdaptadorBuilder(id,ruta);
		builder.crearFicherosConfiguracion();
		assertEquals("DefaultValidator no informa del tipo de error",
				true, checkFileExists(id, "Dockerfile"));
		assertEquals("DefaultValidator no informa del tipo de error",
				true, checkFileExists(id, "nutch-site.xml"));
		assertEquals("DefaultValidator no informa del tipo de error",
				true, checkFileExists(id, "juntarSalidas.sh"));
		assertEquals("DefaultValidator no informa del tipo de error",
				true, checkFileExists(id, "run.sh"));
	}

    private Path readPath(String route) throws URISyntaxException {
        return Paths.get(getClass().getResource(route).toURI());
    }

    private Configuration readConfiguration(String route) throws URISyntaxException {
        return YamlConfigRunner.read(readPath(route));
    }

    private boolean checkFileExists(String parent, String child) {
        return new File(parent, child).exists();
    }
}
