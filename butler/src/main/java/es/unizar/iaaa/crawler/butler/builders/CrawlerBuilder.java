package es.unizar.iaaa.crawler.butler.builders;

import java.io.IOException;
import java.io.PrintWriter;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

public interface CrawlerBuilder {

    void createNutchSite(CrawlConfiguration configuration, String outputDir);

    void addDockerfile(CrawlConfiguration configuracion, String directoryName, PrintWriter pw) throws IOException;
}
