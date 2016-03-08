/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package YalmReader;
import static java.lang.String.format;

public final class Timeouts {
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
		return new StringBuilder().append(format("Timeouts:\n"))
				.append(format("		number of times a thread will delay when trying to fetch a page: %s\n", fetchTimes))
				.append(format("		seconds for the parsing of a document: %s\n", parser))
				.append(format("		network: %s\n", network)).toString();
	}

}