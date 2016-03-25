package es.unizar.iaaa.crawler.butler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.shell.core.JLineShellComponent;
import org.springframework.stereotype.Component;

/**
 * Created by javier on 25/03/16.
 */
@Component
public class SpringShellLauncher implements CommandLineRunner {

    @Autowired
    private JLineShellComponent shell;

    @Override
    public void run( String... args ) throws Exception {
        shell.start();
        shell.promptLoop();
        shell.waitForComplete();
    }
}

