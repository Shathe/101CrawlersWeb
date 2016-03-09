/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.yalm;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class YamlConfigRunner {

    public static Configuration read(String file) {
        Yaml yaml;
        yaml = new Yaml();
        InputStream in;
        try {
            in = Files.newInputStream(Paths.get(file));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        }
        Configuration config;
        config = yaml.loadAs(in, Configuration.class);
        return config;

    }

}