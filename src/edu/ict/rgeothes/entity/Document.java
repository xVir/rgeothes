package edu.ict.rgeothes.entity;

import java.util.Date;

/**
 * Class representing document item
 */
public class Document {
	
	public static final Document UNKNOWN_DOCUMENT = new Document("unknown document");

	public Document(){
		
	}
	
	private Document(String description){
		this.description = description;
	}
	
	private String uri;
	private String description;
	
	private Date beginDate;
	private Date endDate;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	private Date creationDate;
}
