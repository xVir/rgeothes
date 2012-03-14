package edu.ict.rgeothes.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.ict.rgeothes.entity.Document;
import edu.ict.rgeothes.entity.Location;
import edu.ict.rgeothes.entity.Name;
import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.entity.Record;
import edu.ict.rgeothes.entity.RecordReference;

public class RecordDao {

	private Connection connection;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public RecordDao() {
	}

	public RecordDao(Connection connection) {
		this.connection = connection;
	}

	public void addRecords(List<Record> records) {

		for (Record rec : records) {
			insertRecord(rec);
		}

	}

	private void insertRecord(Record rec) {

		try {

			Statement s = connection.createStatement();

			List<String> sqlCommands = createSqlCommands(rec);

			for (int i = 0; i < sqlCommands.size(); i++) {
				System.out.println(sqlCommands.get(i));
				s.execute(sqlCommands.get(i));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private long getNextDocumentId() {
		String query = "SELECT  NEXTVAL('thesaurus_document_id_seq')";

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			resultSet.next();
			long val = resultSet.getLong("nextval");
			return val;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	private List<String> createSqlCommands(Record rec) {

		List<String> result = new ArrayList<>();

		result.add(createSqlFromRecord(rec));

		if (rec.getPrevious()!=null) {
			result.add(createSqlFromDocument(rec.getPrevious().getDocument()));
			result.add(createSqlFromReference(rec.getPrevious()));	
		}
		

		for (Name n : rec.getNames()) {

			result.add(createSqlFromDocument(n.getBeginDocument()));
			result.add(createSqlFromDocument(n.getEndDocument()));

			result.add(createSqlFromName(rec, n));
		}

		for (Location loc : rec.getLocations()) {
			result.add(createSqlFromDocument(loc.getBeginDocument()));
			result.add(createSqlFromDocument(loc.getEndDocument()));

			// TODO another location types
			result.add(createSqlFromPoint(rec, (Point) loc));
		}

		return result;
	}

	private static String INSERT_RECORD_REFERENCE_FORMAT = "INSERT INTO record_reference("
			+ " document_id, record_from_qualifier, record_to_qualifier)"
			+ " VALUES (%s, '%s', '%s');";

	private String createSqlFromReference(RecordReference previous) {
		return	String.format(INSERT_RECORD_REFERENCE_FORMAT, previous
					.getDocument().getId(), previous.getRecordFromQualifier(),
					previous.getRecordToQualifier());
		
	}

	private static String INSERT_POINT_FORMAT = "INSERT INTO "
			+ "thesaurus_location("
			+ "qualifier,  begin_document_id, end_document_id, location_type, "
			+ "point_simple) " + "VALUES ('%s', %s, %s, '%s', %s)";

	private String createSqlFromPoint(Record rec, Point loc) {
		return String.format(INSERT_POINT_FORMAT, rec.getQualifier(), loc
				.getBeginDocument().getId(), loc.getEndDocument().getId(),
				"point", String.format("ROW('(%s, %s)')", loc.getLatitude(),
						loc.getLongitude()));
	}

	private static String INSERT_RECORD_FORMAT = "INSERT INTO thesaurus_record(qualifier)  VALUES ('%s')";

	private String createSqlFromRecord(Record rec) {
		return String.format(INSERT_RECORD_FORMAT, rec.getQualifier());
	}

	private static String INSERT_DOCUMENT_FORMAT = "INSERT INTO thesaurus_document(id, uri, description, document_date, creation_date)"
			+ "   VALUES (%s, '%s', '%s', %s, %s)";

	private String createSqlFromDocument(Document document) {

		document.setId(getNextDocumentId());

		return String.format(INSERT_DOCUMENT_FORMAT, document.getId(),
				document.getUri(), document.getDescription(),
				toSqlString(dateFormat.format(document.getDate())),
				toSqlString(dateFormat.format(document.getCreationDate())));
	}
	
	private String toSqlString(String string){
		return String.format("'%s'", string);
	}

	private static String INSERT_NAME_FORMAT = "INSERT INTO thesaurus_name( name, type, lang, qualifier, begin_document_id, end_document_id) "
			+ "  VALUES ('%s', '%s', '%s', '%s',%s , %s)";

	private String createSqlFromName(Record rec, Name name) {
		return String.format(INSERT_NAME_FORMAT, name.getName(),
				name.getType(), name.getLanguage(), rec.getQualifier(), name
						.getBeginDocument().getId(), name.getEndDocument()
						.getId());
	}

}
