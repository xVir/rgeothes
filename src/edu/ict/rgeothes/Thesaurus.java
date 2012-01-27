package edu.ict.rgeothes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.ict.rgeothes.entity.Document;
import edu.ict.rgeothes.entity.Record;
import edu.ict.rgeothes.exceptions.DuplicateRecordException;
import edu.ict.rgeothes.exceptions.RecordNotFoundException;
import edu.ict.rgeothes.search.SearchQuery;

public class Thesaurus {

	private Map<UUID, Record> records = new HashMap<UUID, Record>();

	public Thesaurus() {
	}

	public void initialize() {

	}

	public int getRecordsCount() {
		return records.size();
	}

	/*
	 * Changes qualifier of record and returns record with new qualifier
	 * 
	 * @param record
	 * 
	 * @param newQualifier
	 * 
	 * @param sourceDocument
	 */
	public Record changeQualifier(Record record, String newQualifier,
			Document sourceDocument) throws RecordNotFoundException,
			DuplicateRecordException {

		// check if object with new name already exists
		if (records.containsKey(newQualifier)) {
			throw new DuplicateRecordException();
		}

		// check if record already in thesaurus
		if (records.containsKey(record.getQualifier())) {
			Record newRecord = null;

			// TODO Implementation

			return newRecord;
		} else {
			throw new RecordNotFoundException();
		}

	}

	public void addRecords(List<Record> recordsToAdd) {

		for (Record rec : recordsToAdd) {
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

	public List<Record> search(SearchQuery query) {
		List<Record> relevantRecords = new ArrayList<Record>();

		if (query.isNameSpecified()) {
			
			List<Record> recordsToFilter = new ArrayList<>();
			
			List<Record> recordsByName = searchByName(query.getName());

			if (!query.isDateRangeSpecified()) {
				return recordsByName;
			}

			for (Record record : recordsByName) {
				recordsToFilter.add(record);
				recordsToFilter.addAll(getAllLifecycleRecords(record));
			}
			
			//TODO filter by date range
			for (Record record : recordsToFilter) {
				if (record.hasNamesValidIn(query.getDateRange())) {
					relevantRecords.add(record);
				}
			}
			
		}

		return relevantRecords;
	}

	private Collection<? extends Record> getAllLifecycleRecords(Record record) {
		List<Record> relevantRecords = new ArrayList<Record>();
		
		if (record.getPrevious() != null) {
			relevantRecords.addAll(getAllPastRecords(record));
		}
		
		relevantRecords.addAll(getAllFutureRecords(record));
		
		return relevantRecords;
	}

	private Collection<? extends Record> getAllFutureRecords(Record record) {
		List<Record> futureRecords = new ArrayList<>();
		
		for (Record thesaurusRecord : records.values()) {
			if (thesaurusRecord.getPrevious() != null) {
				if (thesaurusRecord.getPrevious().getRecordFromQualifier()
						.equals(record.getQualifier())) {
					
					futureRecords.add(thesaurusRecord);
					
					futureRecords.addAll(getAllFutureRecords(thesaurusRecord));
				}
			}
		}
		
		return futureRecords;
	}

	private Collection<? extends Record> getAllPastRecords(Record record) {
		List<Record> pastRecords = new ArrayList<>();
		
		 UUID recordFromQualifier = record.getPrevious().getRecordFromQualifier();
		 
		 Record recordFrom = records.get(recordFromQualifier);
		 if (recordFrom != null) {
			pastRecords.add(recordFrom);
			
			if (recordFrom.getPrevious()!= null) {
				pastRecords.addAll(getAllPastRecords(recordFrom));
			}
		}
		
		return pastRecords;
	}

	private List<Record> searchByName(String name) {
		List<Record> resultRecords = new ArrayList<Record>();
		for (Record r : records.values()) {
			if (r.hasName(name)) {
				resultRecords.add(r);
			}
		}

		return resultRecords;
	}

}
