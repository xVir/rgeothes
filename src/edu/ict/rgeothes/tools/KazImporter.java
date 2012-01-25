package edu.ict.rgeothes.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.ict.rgeothes.dao.RecordDao;
import edu.ict.rgeothes.entity.Document;
import edu.ict.rgeothes.entity.Location;
import edu.ict.rgeothes.entity.Name;
import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.entity.Record;
import edu.ict.rgeothes.entity.RecordReference;
import edu.ict.rgeothes.entity.Rectangle;
import edu.ict.rgeothes.tools.loaders.KzLoader;

public class KazImporter {

	
	private static final boolean ToDatabase = false;
	private static final boolean ToXml = true;

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
		
		List<Record> result = parseRecords(inputLines);
		
		//trying to save 
		
		if (ToDatabase) {
			Connection connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/rgeothes", "postgres",
					"postgres");

			connection.setAutoCommit(true);
			
			new RecordDao(connection).addRecords(result);
	
		}

		if (ToXml) {
			
			XStream xstream = new XStream(new DomDriver());
			
			xstream.alias("record", Record.class);
			xstream.alias("name", Name.class);
			xstream.alias("document", Document.class);
			xstream.alias("recordReference", RecordReference.class);
			xstream.alias("location", Location.class);
			xstream.alias("point", Point.class);
			xstream.alias("rectangle", Rectangle.class);
			
			xstream.setMode(XStream.NO_REFERENCES);
			
			String serialized = xstream.toXML(result);
			
			File outputFile = new File("kz.xml");
		
			FileUtils.writeStringToFile(outputFile, serialized, "Unicode");
			
		}
		
	}

	private static List<Record> parseRecords(List<String> inputLines) {
		return new KzLoader().parseRecords(inputLines);
	}

	

}
