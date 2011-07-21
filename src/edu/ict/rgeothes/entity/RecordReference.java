package edu.ict.rgeothes.entity;

/**
 * Class, used to link records
 */
public class RecordReference {

	public RecordReference() {
	}
	
	
	public RecordReference(Record recordFrom, Record recordTo, Document document) {
		super();
		this.recordFrom = recordFrom;
		this.recordTo = recordTo;
		this.document = document;
	}


	/**
	 * Record from which link begins 
	 */
	private Record recordFrom;
	
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
