/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package YalmReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.yaml.snakeyaml.Yaml;

public class YamlConfigRunner {
	
	public static Configuration read(String file) {
		Yaml yaml = new Yaml();
		InputStream in;
		try {
			in = Files.newInputStream(Paths.get(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		Configuration config = yaml.loadAs(in, Configuration.class);
		return config;

	}

}