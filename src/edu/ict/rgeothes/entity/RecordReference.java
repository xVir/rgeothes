package edu.ict.rgeothes.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class, used to link records
 */
@Entity
@Table(name="record_reference")
public class RecordReference implements Serializable {

	public RecordReference() {
	}
	
	
	public RecordReference(Record recordFrom, Record recordTo, Document document) {
		super();
		this.recordFrom = recordFrom;
		this.recordTo = recordTo;
		this.document = document;
	}

	@Id
	private long id;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Record from which link begins 
	 */
	private transient Record recordFrom;
	
	/**
	 * Record where link ends
	 */
	private Record recordTo;
	
	/**
	 * Document, assuring link between records
	 */
	private Document document;
	
	public void setDocument(Document document) {
		this.document = document;
	}
	
	public Document getDocument() {
		return document;
	}

	public void setRecordFrom(Record recordFrom) {
		this.recordFrom = recordFrom;
	}
	
	public Record getRecordFrom() {
		return recordFrom;
	}
	
	public void setRecordTo(Record recordTo) {
		this.recordTo = recordTo;
	}

	public Record getRecordTo() {
		return recordTo;
	}
	
}
