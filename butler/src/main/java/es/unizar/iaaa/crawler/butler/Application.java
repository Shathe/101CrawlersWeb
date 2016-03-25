package es.unizar.iaaa.crawler.butler;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.Bootstrap;

@SpringBootApplication
public class Application {

	public static void main(String[] args)throws IOException {

				Bootstrap.main(args);
		//SpringApplication.run(Application.class, args);

	}

}