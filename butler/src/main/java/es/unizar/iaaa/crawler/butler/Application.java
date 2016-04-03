package es.unizar.iaaa.crawler.butler;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import es.unizar.iaaa.crawler.butler.index.DockerIndexer;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
        	// No arguments for executing the terminal
            SpringApplication app = new SpringApplication(Application.class);
            app.setBannerMode(Banner.Mode.OFF);
            app.run(args);
        } else if (args[0].contains("index")) {
        	// Argument for indexing from inside the docker container
            DockerIndexer dockerI = new DockerIndexer();
            String indexPath="index";
    		Path outputPath = Paths.get(indexPath);

    		// If the folder exists, delete it (rewrite the index)
    		try {
    			FileUtils.deleteDirectory(outputPath.toFile());
    			Files.createDirectory(outputPath);

    		} catch (IOException e1) {
    			System.out.println("Failing creating the index folder");
    		}
            dockerI.indexDocker(indexPath, "salida/salida");
        }
    }

}