package es.unizar.iaaa.crawler.butler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.shell.core.JLineShellComponent;
import org.springframework.stereotype.Component;

/**
 * Created by javier on 25/03/16.
 */
@Component
@Profile("!test")
public class SpringShellLauncher implements CommandLineRunner {

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private JLineShellComponent shell;

    @Override
    public void run(String... args) throws Exception {
        shell.start();
        shell.promptLoop();
        shell.waitForComplete();
        SpringApplication.exit(ctx);
    }
    
}

