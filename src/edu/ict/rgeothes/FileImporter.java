package edu.ict.rgeothes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import edu.ict.rgeothes.dao.RecordDao;
import edu.ict.rgeothes.entity.Document;
import edu.ict.rgeothes.entity.Name;
import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.entity.Record;
import edu.ict.rgeothes.tools.CoordinatesConverter;
import edu.ict.rgeothes.tools.DateBuilder;

public class FileImporter {
	
	private static final String PRIMARY_LANG = "kz";
	private static final String SECONDARY_LANG = "ru";

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException {

		if (args.length < 1) {
			System.out.println("Specify input file");
			return;
		}

		String fileName = args[0];
		System.out.println("Processing " + fileName);
//
//		Reader in = new InputStreamReader(new FileInputStream(fileName), "Unicode");
//		BufferedReader r = new BufferedReader(in);
//		String readLine = r.readLine();
//		String readLine2 = r.readLine();
//		r.close();
//		
		
		File inputFile = new File(fileName);
		List<String> inputLines = FileUtils.readLines(inputFile, "Unicode");


		
		if (inputLines.size() <= 0) {
			System.out.println("No data for processing");
			return;
		}

		System.out.println("Skipping header: " + inputLines.get(0));

		if (inputLines.size() == 1) {
			System.out.println("No data for processing");
			return;
		}
		
		List<Record> result = new ArrayList<Record>(); 
		for (int i = 1; i < inputLines.size(); i++) {
			String inputString = inputLines.get(i);
			
			String[] stringItems = StringUtils.splitPreserveAllTokens(inputString, '\t');
			
			Record record = new Record();
			
			//document about place creation
			Document beginDoc = new Document();
			Date date = DateBuilder.buildDate(Integer.parseInt(stringItems[4]), 1, 1);
			beginDoc.setDate(date);
			beginDoc.setCreationDate(date);
			beginDoc.setDescription("Создание объекта " + stringItems[1]);
			beginDoc.setUri("uri of document");
			
			Name primaryName = new Name(stringItems[1], stringItems[6], PRIMARY_LANG); 
			primaryName.setBeginDocument(beginDoc);
			record.setPrimaryName(primaryName);
			
			Name anotherName = new Name(stringItems[2], stringItems[6], SECONDARY_LANG);
			anotherName.setBeginDocument(beginDoc);
			record.addName(anotherName);
			
			//object location
			Point p = createPoint(stringItems[8]);
			
			p.setBeginDocument(beginDoc);
			
			record.addLocation(p);
			
			result.add(record);
			
		}
		
		//trying to save 
		

		Connection connection = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/rgeothes", "postgres",
				"postgres");

		connection.setAutoCommit(true);
		
		new RecordDao(connection).addRecords(result);

	}

	private static Point createPoint(String pointData) {
		//52°00′00″ с. ш. 70°56′00″ в. д
		
		return CoordinatesConverter.parsePoint(pointData);
	}

}
