package es.unizar.iaaa.crawler.butler.commands;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.yalm.YamlConfigRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.shell.core.CommandMarker;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO @Iñigo Document me!
@Component
public class Operations implements CommandMarker {

	static Logger log = Logger.getLogger(Operations.class.getName());

	@Autowired
	private ApplicationContext ctx;


	public BufferedReader executeCommand(String comando, boolean print) throws IOException {
		String s;
		Process p = Runtime.getRuntime().exec(comando);

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		// Leemos la salida del comando

		// Leemos los errores si los hubiera
		if (print) {
			while ((s = stdInput.readLine()) != null) {
				if (!(s.contains("WARNING: Error loading config") || s.equals(""))) {
					log.log(Level.INFO, s);
				}
			}
		}
		while ((s = stdError.readLine()) != null) {
			if (!(s.contains("WARNING: Error loading config") || s.equals(""))) {
				log.log(Level.SEVERE, s);
			}
		}
		return stdInput;
	}

	public Resource readPath(String route) throws Exception {
		return ctx.getResource("classpath:es/unizar/iaaa/crawler/butler/builders/" + route);
	}

	public CrawlConfiguration readConfiguration(String route) throws Exception {
		return YamlConfigRunner.read(readPath(route));
	}

}
