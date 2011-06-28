package edu.ict.rgeothes.entity;

/*
 * Class, containing coordinates and reference to Document
 */
public abstract class Location {

	private Document document;
	
	public Document getDocument() {
		return document;
	}
	
	public void setDocument(Document document) {
		this.document = document;
	}
	
}
