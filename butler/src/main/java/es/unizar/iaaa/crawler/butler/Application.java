package es.unizar.iaaa.crawler.butler;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.JLineShellComponent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import es.unizar.iaaa.crawler.butler.index.DockerIndexer;

@SpringBootApplication
public class Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			// No arguments for executing the terminal
			SpringApplication app = new SpringApplication(Application.class);
			app.setBannerMode(Banner.Mode.OFF);
			app.run(args);
		} else if (args[0].contains("index")) {
			// Argument for indexing from inside the docker container
			DockerIndexer dockerI = new DockerIndexer();
			String indexPath = "index";
			Path outputPath = Paths.get(indexPath);

			// If the folder exists, delete it (rewrite the index)
			try {
				FileUtils.deleteDirectory(outputPath.toFile());
				Files.createDirectory(outputPath);

			} catch (IOException e1) {
				System.out.println("Failing creating the index folder");
				LOGGER.warn(e1.getMessage(), e1);
			}
			dockerI.indexDocker(indexPath, "salida/salida");

		}

		// do command, example "do build --idProject 5 --imageName nueva"
		else if (args[0].contains("do")) {
			String command = "";
			for (int i = 1; i < args.length; i++) {
				command += args[i] + " ";
			}

			ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
			JLineShellComponent shell = ctx.getBean(JLineShellComponent.class);
			CommandResult cr = shell.executeCommand(command);
			System.out.println(cr.getResult().toString());
			//shell.stop();
			//SpringApplication.exit(ctx);
			((AnnotationConfigApplicationContext) ctx).close();
		}
	}

}