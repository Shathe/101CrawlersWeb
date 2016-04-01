package es.unizar.iaaa.crawler.butler.model;

public class SearchResult {
	
	private String url;
	private String content;
	
	public SearchResult(String url, String content){
		this.url=url;
		this.content=content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
