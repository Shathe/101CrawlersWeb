package es.unizar.iaaa.crawler.butler.builders;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

import java.io.PrintWriter;

public interface CrawlerBuilder {

	void createNutchSite(CrawlConfiguration configuration, String outputDir);
	void addDockerfile(CrawlConfiguration configuracion, String directoryName, PrintWriter pw);
}
