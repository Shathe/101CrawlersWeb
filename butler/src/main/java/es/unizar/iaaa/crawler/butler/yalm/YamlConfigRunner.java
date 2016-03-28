/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.yalm;

import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public class YamlConfigRunner {

    /**
     * Given a file configuration, returns a poblated Configuration class  if it's a well form file
     * the structure of the file is explained in
     * https://github.com/Shathe/101CrawlersWeb/wiki/Especificaci%C3%B3n-del-fichero-de-configuraci%C3%B3n-yaml
     */
    public static CrawlConfiguration read(Resource resource) {
        CrawlConfiguration result = null;
        if (resource != null) {
            Yaml yaml = new Yaml();
            try (InputStream is = resource.getInputStream()) {
                result = yaml.loadAs(is, CrawlConfiguration.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}