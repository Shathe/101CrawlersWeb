package es.unizar.iaaa.crawler.butler;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

import es.unizar.iaaa.crawler.butler.index.DockerIndexer;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            // Executing the terminal
            SpringApplication app = new SpringApplication(Application.class);
            app.setBannerMode(Banner.Mode.OFF);
            app.run(args);
        } else if (args[0].contains("index")) {
            // The docker will be index
            DockerIndexer dockerI = new DockerIndexer();
            dockerI.indexDocker("index", "salida");
        }
    }

}