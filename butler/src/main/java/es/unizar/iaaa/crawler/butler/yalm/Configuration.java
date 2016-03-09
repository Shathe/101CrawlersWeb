/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.yalm;

import es.unizar.iaaa.crawler.butler.model.CrawlSystem;
import es.unizar.iaaa.crawler.butler.model.DockerOS;

import static java.lang.String.format;

public final class Configuration {
    private DockerOS dockerOS;
    private CrawlSystem crawlSystem;

	/* Getters and setters */

    public DockerOS getDockerOS() {
        return dockerOS;
    }

    public void setDockerOS(DockerOS dockerOS) {
        this.dockerOS = dockerOS;
    }

    public CrawlSystem getCrawlSystem() {
        return crawlSystem;
    }

    public void setCrawlSystem(CrawlSystem crawlSystem) {
        this.crawlSystem = crawlSystem;
    }

    @Override
    public String toString() {
        return format("DockerOS: %s\n", dockerOS) +
                format("CrawlSystem: %s\n", crawlSystem);
    }
}