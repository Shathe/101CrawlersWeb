/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.model;

import java.util.List;

import static java.lang.String.format;

public  class CrawlSystem {
    private String name;
    private String version;
    private List<String> seeds;
    private List<Plugin> plugins;
    private String rounds;
    private String extraction;
    private String infoCrawled;
    private String maxFileLength;
    private String maxCrawlDelay;
    private String linksLimitURL;
    private String queueMode;
    private Timeouts timeouts;

    public List<String> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<String> seeds) {
        this.seeds = seeds;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

   
    public String getExtraction() {
		return extraction;
	}

	public void setExtraction(String extraction) {
		this.extraction = extraction;
	}

	public String getRounds() {
        return rounds;
    }

    
    public void setRounds(String rounds) {
        this.rounds = rounds;
    }

    public String getInfoCrawled() {
        return infoCrawled;
    }

    public void setInfoCrawled(String infoCrawled) {
        this.infoCrawled = infoCrawled;
    }

    public String getMaxFileLength() {
        return maxFileLength;
    }

    public void setMaxFileLength(String maxFileLength) {
        this.maxFileLength = maxFileLength;
    }

    public String getMaxCrawlDelay() {
        return maxCrawlDelay;
    }

    public void setMaxCrawlDelay(String maxCrawlDelay) {
        this.maxCrawlDelay = maxCrawlDelay;
    }

    public String getLinksLimitURL() {
        return linksLimitURL;
    }

    public void setLinksLimitURL(String linksLimitURL) {
        this.linksLimitURL = linksLimitURL;
    }

    public String getQueueMode() {
        return queueMode;
    }

    public void setQueueMode(String queueMode) {
        this.queueMode = queueMode;
    }

    public Timeouts getTimeouts() {
        return timeouts;
    }

    public void setTimeouts(Timeouts timeouts) {
        this.timeouts = timeouts;
    }
    

    public List<Plugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<Plugin> plugins) {
		this.plugins = plugins;
	}

	@Override
    public String toString() {
        return format("Crawler system: %s version %s\n", getName(), getVersion()) +
                format("	Seeds: %s\n", seeds) +
                format("	Links limit for URL: %s\n", linksLimitURL) +
                format("	format of the info Crawled: %s\n", infoCrawled) +
                format("	Number of rounds: %s\n", rounds) +
                format("	Extraction: %s\n", extraction) +
                format("	Max Crawl delay to fetch: %s\n", maxCrawlDelay) +
                format("	length limit for downloaded content: %s\n", maxFileLength) +
                format("	queue Mode: %s\n", queueMode) +
                format("	%s\n", timeouts);


    }

}