package edu.ict.rgeothes.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.ict.rgeothes.dao.RecordDao;
import edu.ict.rgeothes.entity.Record;

public class NskImporter {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {

		NskReestrParser parser = new NskReestrParser();

		String fileName = "input/novosib.txt";
		List<Record> records = parser.readRecordsFormFile(fileName);
		
		
		Connection connection = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/rgeothes", "postgres",
				"postgres");

		connection.setAutoCommit(true);

		clearThesaurus(connection);

		new RecordDao(connection).addRecords(records);
	}

	private static void clearThesaurus(Connection connection)
			throws SQLException {
		Statement statement = connection.createStatement();
		statement.execute("TRUNCATE document CASCADE; TRUNCATE record CASCADE; TRUNCATE record_reference CASCADE");

	}
	
}
