package edu.ict.rgeothes.entity;

/*
 * Class, containing coordinates and reference to Document
 */
public abstract class Location {

	private Document beginDocument;
	
	private Document endDocument;

	
	
	public Document getBeginDocument() {
		return beginDocument;
	}

	public void setBeginDocument(Document beginDocument) {
		this.beginDocument = beginDocument;
	}

	public Document getEndDocument() {
		return endDocument;
	}

	public void setEndDocument(Document endDocument) {
		this.endDocument = endDocument;
	}

	public Location(Document beginDocument) {
		super();
		this.beginDocument = beginDocument;
	}
	
	public Location() {
	}
	
	
	
}
