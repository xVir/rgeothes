package edu.ict.rgeothes.dao;

import java.sql.Connection;
import java.util.List;

import org.postgresql.PGConnection;

import edu.ict.rgeothes.entity.Record;

public class RecordDao {

	public RecordDao() {
	}

	public void addRecords(Connection connection, List<Record> records) {
		
		for (Record rec : records) {
			insertRecord(rec);
		}
		
		
	}

	private void insertRecord(Record rec) {
		// TODO Auto-generated method stub
		
		
		
		
	}

}
