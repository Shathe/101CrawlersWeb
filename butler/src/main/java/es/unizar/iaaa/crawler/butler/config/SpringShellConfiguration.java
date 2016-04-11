package es.unizar.iaaa.crawler.butler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.CommandLine;
import org.springframework.shell.core.JLineShellComponent;

/**
 * Created by javier on 25/03/16.
 */
@Configuration
@ComponentScan({"org.springframework.shell.commands", "org.springframework.shell.converters",
        "org.springframework.shell.plugin.support"})
public class SpringShellConfiguration {
    @Bean
    public JLineShellComponent shell() {
        return new JLineShellComponent();
    }

    @Bean
    public CommandLine commandLine() {
        return new CommandLine(null, 3000, null);
    }
}
