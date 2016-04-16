/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.model;

import static java.lang.String.format;

public class DockerOS {
    private String name;
    private String version;

	/* Getters and setters */

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

    @Override
    public String toString() {
        return format("Docker image based on %s version %s", getName(), getVersion());
    }
}