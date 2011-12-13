package edu.ict.rgeothes.entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/*
 * Class, containing coordinates and reference to Document
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="location_type",discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("Plane")
public abstract class Location {

	@Id
	private long id;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
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
		beginDocument = Document.UNKNOWN_DOCUMENT;
		endDocument = Document.UNKNOWN_DOCUMENT;
		
	}
	
	
	
}
