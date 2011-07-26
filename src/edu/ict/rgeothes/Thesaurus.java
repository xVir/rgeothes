package edu.ict.rgeothes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.ict.rgeothes.entity.Document;
import edu.ict.rgeothes.entity.Record;
import edu.ict.rgeothes.exceptions.DuplicateRecordException;
import edu.ict.rgeothes.exceptions.RecordNotFoundException;

public class Thesaurus {
	
	private Map<UUID,Record> records = new HashMap<UUID, Record>(); 
	
	public Thesaurus() {
		// TODO Auto-generated constructor stub
	}
	
	public void initialize(){
		
	}

	public int getRecordsCount() {
		return records.size();
	}

	/*
	 * Changes qualifier of record and returns record with new qualifier
	 * @param record
	 * @param newQualifier
	 * @param sourceDocument
	 */
	public Record changeQualifier(Record record, String newQualifier, Document sourceDocument) 
			throws RecordNotFoundException, DuplicateRecordException{

		//check if object with new name already exists
		if(records.containsKey(newQualifier))
		{
			throw new DuplicateRecordException();
		}
		
		//check if record already in thesaurus
		if (records.containsKey(record.getQualifier())) {
			Record newRecord = null;
			
			//TODO Implementation
			
			return newRecord;
		}
		else{
			throw new RecordNotFoundException();
		}
		
	}

	public void addRecords(List<Record> recordsToAdd) {
		
		for(Record rec : recordsToAdd){
			try {
				addRecord(rec);
			} catch (DuplicateRecordException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void addRecord(Record rec) throws DuplicateRecordException {
		
		if (records.containsKey(rec.getQualifier())) {
			throw new DuplicateRecordException(rec);
		}
		
		records.put(rec.getQualifier(), rec);
		
	}
	
}
