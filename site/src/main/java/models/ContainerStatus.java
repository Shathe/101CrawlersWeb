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

	private String crawlerStatus, containerStatus;

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

}
