/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.yalm;

import es.unizar.iaaa.crawler.butler.builders.validator.ConfigurationValidator;
import es.unizar.iaaa.crawler.butler.builders.validator.ValidationResult;
import es.unizar.iaaa.crawler.butler.builders.validator.Validator;
import es.unizar.iaaa.crawler.butler.builders.validator.Validator.ErroresValidar;
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
        return format("DockerOS: %s\n", dockerOS) + format("CrawlSystem: %s\n", crawlSystem);
    }

    /* Valida si el contenido es correcto */
    public boolean isOk() {
        Validator validator = new ConfigurationValidator();
        ValidationResult result = validator.validate(this);
        return result.isOk();
    }

    /* Devuelve el codigo de error correspondiente a esta configuracion */
    public ErroresValidar getLatestErrorCode() {
        Validator validator = new ConfigurationValidator();
        ValidationResult result = validator.validate(this);
        return result.getFirstErrorCode();
    }

    /* Devuelve el error/ok  de esta configuracion */
    public Object getLatestErrorValue() {
        Validator validator = new ConfigurationValidator();
        ValidationResult result = validator.validate(this);
        return result.getFirstErrorValue();
    }
}