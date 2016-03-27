/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.yalm;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

// TODO @Iñigo Document me!
public class YamlConfigRunner {

    public static CrawlConfiguration read(Resource resource) {
        CrawlConfiguration result = null;
        Yaml yaml = new Yaml();
        try(InputStream is = resource.getInputStream()) {
            result = yaml.loadAs(is, CrawlConfiguration.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}