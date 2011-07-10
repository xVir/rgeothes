package edu.ict.rgeothes.exceptions;

import edu.ict.rgeothes.entity.Record;

public class DuplicateRecordException extends Exception {

	private static final String DEFAULT_MESSAGE = "Record with this qualifier already exists in thesaurus";
	
	private static final String MESSAGE_WITH_QUALIFIER_FORMAT = "Record with qualifier %s already exists in thesaurus";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7147073234091451937L;
	private String detailMessage;
	
	public DuplicateRecordException() {
		detailMessage = DEFAULT_MESSAGE;
	}
	
	public DuplicateRecordException(Record record){
		detailMessage = String.format(MESSAGE_WITH_QUALIFIER_FORMAT, record.getQualifier());
	}
	
	@Override
	public String toString() {
		return detailMessage;
	}

}
