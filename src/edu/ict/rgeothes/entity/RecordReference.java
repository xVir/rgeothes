package edu.ict.rgeothes.entity;

import java.io.Serializable;
import java.util.UUID;

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
		
		setRecordFrom(recordFrom);
		setRecordTo(recordTo);
		
		this.document = document;
	}

	@Id
	private transient long id;
	
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
	
	private UUID recordFromQualifier;
	
	/**
	 * Record where link ends
	 */
	private transient  Record recordTo;
	
	private UUID recordToQualifier;
	
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
		recordFromQualifier = recordFrom.getQualifier();
	}
	
	public Record getRecordFrom() {
		return recordFrom;
	}
	
	public void setRecordTo(Record recordTo) {
		this.recordTo = recordTo;
		recordToQualifier = recordTo.getQualifier();
	}

	public Record getRecordTo() {
		return recordTo;
	}


	public UUID getRecordFromQualifier() {
		return recordFromQualifier;
	}


	public void setRecordFromQualifier(UUID recordFromQualifier) {
		this.recordFromQualifier = recordFromQualifier;
	}


	public UUID getRecordToQualifier() {
		return recordToQualifier;
	}


	public void setRecordToQualifier(UUID recordToQualifier) {
		this.recordToQualifier = recordToQualifier;
	}
	
	
}
