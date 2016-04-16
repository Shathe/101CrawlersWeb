/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.model;

import static java.lang.String.format;

public class Timeouts {
    private String parser;
    private String fetchTimes;
    private String network;

	/* Getters and setters */

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }

    public String getFetchTimes() {
        return fetchTimes;
    }

    public void setFetchTimes(String fetchTimes) {
        this.fetchTimes = fetchTimes;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @Override
    public String toString() {
        return format("Timeouts:\n") +
                format("		number of times a thread will delay when trying to fetch a page: %s\n", fetchTimes) +
                format("		seconds for the parsing of a document: %s\n", parser) +
                format("		network: %s\n", network);
    }

}