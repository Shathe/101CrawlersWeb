package es.unizar.iaaa.crawler.butler.commands;

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

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.yalm.YamlConfigRunner;

/**
 * Operations related with the commads.
 */
@Component
public class Operations implements CommandMarker {

	static Logger log = Logger.getLogger(Operations.class.getName());

	@Autowired
	private ApplicationContext ctx;

	/**
	 * Executes a command it the OS system shell. if the print flag is
	 * activated, it shows the output in the console, it it's not, it will
	 * return a bufferedReader with the output.
	 */
	public BufferedReader executeCommand(String comando, boolean print) throws IOException {
		String s;
		Process p = Runtime.getRuntime().exec(comando);

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		// Read command error output
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
		// Get the file from 2 possible routes
		// The first one is the final one
		// The second one is for debugging
		Resource res = ctx.getResource("file:../" + route);
		if (!res.exists())
			return ctx.getResource("classpath:es/unizar/iaaa/crawler/butler/builders/" + route);
		else
			return res;
	}

	public CrawlConfiguration readConfiguration(String route) throws Exception {
		return YamlConfigRunner.read(readPath(route));
	}


	public boolean containerExists(String idUser, String idCrawl) {
		String id = idUser + "_" + idCrawl;
		boolean exist = false;
		String s = "";
		try {
			BufferedReader out = executeCommand("docker ps -a ", false);
			while ((s = out.readLine()) != null) {
				if (s.contains(" " + id + " "))
					exist = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return exist;
	}

	public boolean containerRunning(String idUser, String idCrawl) {
		String id = idUser + "_" + idCrawl;
		boolean exist = false;
		String s = "";
		try {//docker ps -f=[name='1_1']
			String command="docker ps -a --filter \"status=running\" --filter \"name="+id+"\"";

			BufferedReader out = executeCommand(command, false);
			while ((s = out.readLine()) != null) {
				if (s.contains(id))
					exist = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return exist ;
	}

	public boolean imageExists(String idUser, String idCrawl) {
		String id = idUser + "_" + idCrawl;
		boolean exist = false;
		String s = "";
		try {
			BufferedReader out = executeCommand("docker images " + id, false);
			while ((s = out.readLine()) != null) {
				if (s.contains( id ))
					exist = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return exist;
	}
	public boolean containerStopped(String idUser, String idCrawl) {
		String id = idUser + "_" + idCrawl;
		boolean exist = false;
		String s = "";
		try {// docker ps --filter "status=exited" --filter "name=1_1"
			String command="docker ps -a --filter \"status=exited\" --filter \"name="+id+"\"";
			BufferedReader out = executeCommand(command, false);
			while ((s = out.readLine()) != null) {
				if (s.contains(id) && s.contains("Exited"))
					exist = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return exist && ! containerPaused(idUser,idCrawl);
	}

	public boolean containerPaused(String idUser, String idCrawl) {
		String id = idUser + "_" + idCrawl;
		boolean exist = false;
		String s = "";
		try {// docker ps -f=[name='1_1']
			String command="docker ps -a --filter \"status=paused\" --filter \"name="+id+"\"";

			BufferedReader out = executeCommand(command, false);
			while ((s = out.readLine()) != null) {
				if (s.contains(id) && s.contains("Paused"))
					exist = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return exist;
	}
}
