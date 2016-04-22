package es.unizar.iaaa.crawler.butler.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.core.CommandMarker;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;
import es.unizar.iaaa.crawler.butler.model.CrawlSystem;
import es.unizar.iaaa.crawler.butler.model.Plugin;
import es.unizar.iaaa.crawler.butler.yalm.YamlConfigRunner;

/**
 * Operations related with the commads.
 */
@Component
public class Operations implements CommandMarker {

	private static final Logger LOGGER = LoggerFactory.getLogger(Operations.class);

	@Autowired
	private ApplicationContext ctx;

	@Value("${butler.base:file:.}/")
	private String baseDir;

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
					System.out.println("[Docker] " + s);
					LOGGER.info(s);
				}
			}
		}
		while ((s = stdError.readLine()) != null) {
			if (!(s.contains("WARNING: Error loading config") || s.equals(""))) {
				LOGGER.warn(s);
			}
		}
		return stdInput;
	}

	public CrawlConfiguration readConfiguration(String route) {
		CrawlConfiguration config = YamlConfigRunner.read(ctx.getResource(baseDir + route));
		// Add plugins
		try {
			CrawlSystem Cs = config.getCrawlSystem();
			Cs.setPlugins(readPlugins("plugins"));
			config.setCrawlSystem(Cs);

		} catch (IOException e) {
			LOGGER.error("There is no plugins to add", e);
		}

		return config;
	}

	// FIXME: route and config are not used.
	public ArrayList<Plugin> readPlugins(String route) throws IOException {
		// List of plugins, each plugins is alist of its files

		ArrayList<Plugin> pluginsToAdd = new ArrayList<>();
		File a = new File(route);
		/*
		 * Resource directory = ctx.getResource("classpath:./" + route); File
		 * directoryFile = directory.getFile(); This does not work, what it is
		 * implementes, yes
		 */

		File[] plugins = a.listFiles();
		if (plugins != null) {
			for (File plugin : plugins) {
				// New Plugin to add
				Plugin PluginNew = new Plugin();
				PluginNew.setName(plugin.getName());
				// Its files
				ArrayList<File> pluginsFilesNew = new ArrayList<>();
				File[] pluginsFiles = plugin.listFiles();
				if (pluginsFiles != null) {
					for (File file : pluginsFiles) {
						pluginsFilesNew.add(file);
					}
				}
				// Add files
				PluginNew.setFiles(pluginsFilesNew);
				pluginsToAdd.add(PluginNew);
			}
		}
		return pluginsToAdd;

	}

	/**
	 * @return true when the container asign to the user and crawl exists
	 */
	public boolean containerExists(String idContainer) {
		String s;
		String command = "docker ps -a";
		try (BufferedReader out = executeCommand(command, false)) {
			while ((s = out.readLine()) != null) {
				s = " " + s + " ";
				if (s.contains(" " + idContainer + " "))
					return true;
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
		}
		return false;
	}

	/**
	 * @return true when the container asign to the user and crawl exists
	 */
	public boolean containerofaImageExists(String idImage) {
		String s;
		String command = "docker ps -a";
		try (BufferedReader out = executeCommand(command, false)) {
			while ((s = out.readLine()) != null) {
				s = " " + s + " ";
				if (s.contains(" " + idImage + "_" + " "))
					return true;
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
		}
		return false;
	}

	/**
	 * Returns true if docker is running
	 */
	public boolean dockerIsRunning() {
		try {
			String s;
			String command = "docker ps";
			Process p = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			while ((s = stdInput.readLine()) != null) {
				if (s.contains("Cannot connect to the Docker daemon")) {
					return false;
				}
			}

			while ((s = stdError.readLine()) != null) {
				if (s.contains("Cannot connect to the Docker daemon")) {
					return false;
				}

			}
		} catch (IOException a) {
			LOGGER.warn("IOException: " + a.getMessage(), a);
		}
		return true;
	}

	/**
	 * @return true when the container asign to the user and crawl exists
	 */
	public boolean containerRunning(String idContainer) {
		String s;
		String command = "docker ps -a --filter status=running --filter name=" + idContainer;
		// docker ps -f=[name='1_1']
		try (BufferedReader out = executeCommand(command, false)) {
			while ((s = out.readLine()) != null) {
				if (s.contains(idContainer))
					return true;
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
		}
		return false;
	}

	/**
	 * @return true when the image asign to the user and crawl exists
	 */
	public boolean imageExists(String idImage) {
		String s;
		try (BufferedReader out = executeCommand("docker images " + idImage, false)) {
			while ((s = out.readLine()) != null) {
				if (s.contains(idImage))
					return true;
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
		}
		return false;
	}

	/**
	 * @return true when the container asign to the user and crawl is stopped
	 */
	public boolean containerStopped(String idContainer) {
		String command = "docker ps -a --filter status=exited --filter name=" + idContainer;
		String s;
		// docker ps --filter "status=exited" --filter "name=1_1"
		try (BufferedReader out = executeCommand(command, false)) {
			while ((s = out.readLine()) != null) {
				if (s.contains(idContainer) && s.contains("Exited"))
					return !containerPaused(idContainer);
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
		}
		return false;
	}

	/**
	 * @return true when the container asign to the user and crawl is paused
	 */
	public boolean containerPaused(String idContainer) {
		String command = "docker ps -a --filter status=paused --filter name=" + idContainer;
		String s;
		// docker ps -f=[name='1_1']
		try (BufferedReader out = executeCommand(command, false)) {
			while ((s = out.readLine()) != null) {
				if (s.contains(idContainer) && s.contains("Paused"))
					return true;
			}
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e.getMessage(), e);
		}
		return false;

	}
}
