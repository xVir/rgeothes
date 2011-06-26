package edu.ict.rgeothes.entity;

/*
 * Class, used to link records
 */
public class RecordReference {

	public RecordReference() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	private Record record;
	
	/*
	 * Document, assuring link between records
	 */
	private Document document;
	
	public void setDocument(Document document) {
		this.document = document;
	}
	
	public void setRecord(Record record) {
		this.record = record;
	}
	
}
