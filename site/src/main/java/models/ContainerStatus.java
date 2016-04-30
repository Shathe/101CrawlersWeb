/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package models;


import javax.persistence.Entity;

/**
 * Model of the Docker container
 * 
 * @author shathe
 *
 */
@Entity
public class ContainerStatus {

	private String crawlerStatus, containerStatus,rounds, fetchedLinks,unfetchedLinks;

	public ContainerStatus(String containerStatus, String crawlerStatus) {
		this.containerStatus = containerStatus;
		this.crawlerStatus = crawlerStatus;
		
	}
	public ContainerStatus() {
	}

	public String getCrawlerStatus() {
		return crawlerStatus;
	}

	public void setCrawlerStatus(String crawlerStatus) {
		this.crawlerStatus = crawlerStatus;
	}

	public String getContainerStatus() {
		return containerStatus;
	}

	public void setContainerStatus(String containerStatus) {
		this.containerStatus = containerStatus;
	}
	public String getRounds() {
		return rounds;
	}
	public void setRounds(String rounds) {
		this.rounds = rounds;
	}
	public String getFetchedLinks() {
		return fetchedLinks;
	}
	public void setFetchedLinks(String fetchedLinks) {
		this.fetchedLinks = fetchedLinks;
	}
	public String getUnfetchedLinks() {
		return unfetchedLinks;
	}
	public void setUnfetchedLinks(String unfetchedLinks) {
		this.unfetchedLinks = unfetchedLinks;
	}

	

}
