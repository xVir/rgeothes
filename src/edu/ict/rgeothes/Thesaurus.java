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
