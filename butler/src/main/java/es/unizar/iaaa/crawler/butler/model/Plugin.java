/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.model;

import java.util.List;


import java.io.File;

public class Plugin {
    private String name;
    private List<File> files;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
    
    
}