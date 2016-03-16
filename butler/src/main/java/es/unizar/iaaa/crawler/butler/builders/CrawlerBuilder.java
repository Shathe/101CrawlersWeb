package es.unizar.iaaa.crawler.butler.builders;

import java.io.PrintWriter;

public interface CrawlerBuilder {

	void createNutchSite();
	void addDockerfile(PrintWriter pw);
}
