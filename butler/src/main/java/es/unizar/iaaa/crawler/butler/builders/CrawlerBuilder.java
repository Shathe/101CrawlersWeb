package es.unizar.iaaa.crawler.butler.builders;

import java.io.PrintWriter;

public interface CrawlerBuilder {

	public void crearNutchSite();
	public void anadirDockerfile(PrintWriter pw);
}
