package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;
import es.unizar.iaaa.crawler.butler.yalm.YamlConfigRunner;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Created by javier on 09/03/16.
 */
public class CoordinatorTest {

    @Test
    public void firstRun() {
        Configuration config;
        config = YamlConfigRunner.read("conf.yml");
        assertNotNull("YamlConfigRunner debe devolver una configuración y no null", config);
        System.out.println(config.toString());
    }
}
