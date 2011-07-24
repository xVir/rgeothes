package edu.ict.rgeothes.tools;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import edu.ict.rgeothes.entity.Document;
import edu.ict.rgeothes.entity.Name;
import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.entity.Record;

public class NskReestrParser {

	private static final String RETIRED_NAME_DOCUMENT_DESCR = "Снят с учета ";

	private static final String RU = "RU-ru";

	private static final char TAB = '\t';

	private static String[] documentBeginings = { "Снят с", "Изменился род", };
	
	private Map<String, Record> districts = new HashMap<String, Record>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String inputFilePath = "input/novosib.txt";
		
		NskReestrParser parser = new NskReestrParser();
		
		parser.readRecordsFormFile(inputFilePath);

	}

	public List<Record> readRecordsFormFile(String inputFilePath) {
		try {
			
			
			
			Record russiaRecord = new Record();
			russiaRecord.setPrimaryParent(Record.ROOT_RECORD, Document.UNKNOWN_DOCUMENT);
			russiaRecord.setPrimaryName(new Name("Российская Федерация", "государство", RU));
			russiaRecord.setQualifier("Российская Федерация");
			
			Record parentRecord = new Record();
			parentRecord.setPrimaryParent(russiaRecord, Document.UNKNOWN_DOCUMENT);
			parentRecord.setPrimaryName(new Name("Новосибирская область", "область", RU));
			parentRecord.setQualifier("Новосибирская область");
			
			List<String> fileContent = FileUtils.readLines(new File(
					inputFilePath));

//FileWriter fstream = new FileWriter("out.txt");
//BufferedWriter writer = new BufferedWriter(fstream);
			
			List<Record> records = new ArrayList<Record>();

			if (fileContent.size() > 0) {
				System.out.println("Lines to proceed: " + fileContent.size());

				int startIndex;
				int endIndex;

				startIndex = 0;

				while (startIndex < fileContent.size()-1) {
					endIndex = getNextRecordIndex(fileContent, startIndex);

					try {
						Record record = getRecordFromLines(fileContent.subList(
								startIndex, endIndex),parentRecord);

						System.out.println(record);
						
						records.add(record);
					} catch (Exception ex) {
						System.out.println("error: " + ex.getMessage());
					}

					startIndex = endIndex;
				}

				System.out.println("Finished!");
				
				System.out.println(records.size() + " records processed");
				System.out.println(records.size() / 867.0);
			} else {
				System.out.println("Nothing to do");
			}
			
	//		writer.close();
			
			return records;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private int getNextRecordIndex(List<String> fileContent,
			int startIndex) throws IllegalArgumentException {
		String firstString = fileContent.get(startIndex);

		StrTokenizer tokenizer = new StrTokenizer(firstString, TAB);

		try {
			int recordNumber = Integer.parseInt(tokenizer.nextToken());
		} catch (NumberFormatException formatException) {
			// first token is not number, so startIndex is wrong
			throw new IllegalArgumentException("startIndex");
		}

		int endIndex = startIndex + 1;

		while (firstElementIsNotInt(fileContent.get(endIndex))
				&& endIndex < fileContent.size() - 1) {
			endIndex++;
		}

		return endIndex;
	}

	private static boolean firstElementIsNotInt(String string) {
		StrTokenizer tokenizer = new StrTokenizer(string, TAB);

		try {
			Integer.parseInt(tokenizer.nextToken());
		} catch (NumberFormatException e) {
			return true;
		}

		return false;
	}

	private Record getRecordFromLines(List<String> lines, Record parentRecord) {

		Record result = new Record();

		StrTokenizer firstLine = new StrTokenizer(lines.get(0), TAB);

		/*
		 * 1 0063376 Абрамовка посёлок Чановский район 55° 04' С.Ш. 75° 04' В.Д.
		 * N-43-31 Снят с учета 06/05/1988 Решение Новосибирского облсовета №
		 * 236 от 06/05/1988.
		 */

		// №№ п.п. -> Рег.№ норм. названия -> Нормализованное название -> Род
		// объекта и Варианты названия -> Адм.-терр. привязка -> Геогр.
		// координаты -> Номенкл. листа

		String recordNumber = firstLine.nextToken();
		String nameNumber = firstLine.nextToken();

		String name = firstLine.nextToken();

		String placeType = firstLine.nextToken();
		String district = firstLine.nextToken();

		String latitude = firstLine.nextToken();
		double latitudeValue = latitudeFromString(latitude);
		
		String longitude = firstLine.nextToken();
		double longitudeValue = longitudeFromString(longitude);
		
		String nomenclature = firstLine.nextToken();

		// ---------------------------------------------------------------------------------------

		List<String> anotherNames = new ArrayList<String>();
		StringBuilder documentDescription = new StringBuilder();

		fillNamesAndDocDescription(1, lines, documentDescription, anotherNames);

		Record districtRecord = getDistrictRecord(parentRecord, district);
		
		result.setPrimaryParent(districtRecord, Document.UNKNOWN_DOCUMENT);
		
		result.setQualifier(name);
		
		Name resultName = new Name(name, placeType, RU);
		
		if (documentDescription.toString().contains(RETIRED_NAME_DOCUMENT_DESCR)) {

			String docDescr = documentDescription.toString();
			
			String documentDateString =StringUtils.substringBetween(docDescr, RETIRED_NAME_DOCUMENT_DESCR, " ");
			
			Date documentDate = null;
			try {
				documentDate = new SimpleDateFormat("dd/MM/yyyy").parse(documentDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			String shortDocumentDescription = StringUtils.substringAfter(docDescr, documentDateString).trim();
			
			resultName.setEndDocument(new Document(shortDocumentDescription,
					documentDate));	
		}
		
		result.setPrimaryName(resultName);

		result.addLocation(new Point(latitudeValue, longitudeValue));
		
		for (String anotherName : anotherNames) {
			result.addName(new Name(anotherName, placeType, RU));
		}

		return result;

	}
	
	private void fillNamesAndDocDescription(int lineNumber,List<String> lines,
			StringBuilder outDocDescription,List<String> outAnotherNames){
		
		if (lines.size() > lineNumber) {
			String firstToken = getFirstTokenFromLine(lines.get(lineNumber));
			
			if (StringUtils.isNotBlank(firstToken)) {

				if (firstToken.contains("Снят с")) {

					String secondToken = getSecondTokenFromLine(lines.get(lineNumber));
					if (StringUtils.isNotBlank(secondToken)) {
						outAnotherNames.add(secondToken);
					}
					
					outDocDescription.append(firstToken);
					
					if (lines.size() > lineNumber+1) {
						outDocDescription.append(" ");
						outDocDescription.append(getFirstTokenFromLine(lines.get(lineNumber+1)));
						
						secondToken = getSecondTokenFromLine(lines.get(lineNumber+1));
						if (StringUtils.isNotBlank(secondToken)) {
							outAnotherNames.add(secondToken);
						}
						
						if (lines.size() > lineNumber + 2) {
							outDocDescription.append(" ");
							outDocDescription.append(getFirstTokenFromLine(lines.get(lineNumber+2)));
							
							secondToken = getSecondTokenFromLine(lines.get(lineNumber+2));
							if (StringUtils.isNotBlank(secondToken)) {
								outAnotherNames.add(secondToken);
							}
						}
					}
					
					fillNamesAndDocDescription(lineNumber+1, lines, outDocDescription, outAnotherNames);

				} else if (firstToken.contains("Изменился род")) {
					
					String secondToken = getSecondTokenFromLine(lines.get(lineNumber));
					if (StringUtils.isNotBlank(secondToken)) {
						outAnotherNames.add(secondToken);
					}
					
					fillNamesAndDocDescription(lineNumber+1, lines, outDocDescription, outAnotherNames);
					
				} else {
					// firstToken is additional name
					outAnotherNames.add(firstToken);
					
					fillNamesAndDocDescription(lineNumber+1, lines, outDocDescription, outAnotherNames);
				}
			}
		}
		
				
		
	}

	private Record getDistrictRecord(Record parentRecord, String district) {
		Record districtRecord = new Record();
		districtRecord.setPrimaryParent(parentRecord, Document.UNKNOWN_DOCUMENT);
		districtRecord.setQualifier(district);
		districtRecord.setPrimaryName(new Name(district, "район", RU));
		
		if (!districts.containsKey(districtRecord.getFullQualifier())) {
			districts.put(districtRecord.getFullQualifier(), districtRecord);
		}
		else{
			districtRecord = districts.get(districtRecord.getFullQualifier());
		}
		return districtRecord;
	}

	private static double longitudeFromString(String longitudeString) {
		// 84° 08' В.Д.
		
		int degrees = Integer.parseInt(longitudeString.substring(0,longitudeString.indexOf('°')));
		int minutes = Integer.parseInt(longitudeString.substring(longitudeString.indexOf("° ")+2,
				longitudeString.indexOf('\'')));
		
		double result = degrees + minutes/60.0;
		
		return result;
	}

	private static double latitudeFromString(String latitudeString) {
		// 54° 56' С.Ш.
		
		int degrees = Integer.parseInt(latitudeString.substring(0,latitudeString.indexOf('°')));
		int minutes = Integer.parseInt(latitudeString.substring(latitudeString.indexOf("° ")+2,
				latitudeString.indexOf('\'')));
		
		double result = degrees + minutes/60.0;
		
		return result;
	}

	private static String getFirstTokenFromLine(String input) {
		StrTokenizer tokenizer = new StrTokenizer(input, TAB);
		return tokenizer.nextToken();
	}
	
	private static String getSecondTokenFromLine(String input) {
		StrTokenizer tokenizer = new StrTokenizer(input, TAB);
		tokenizer.nextToken();
		if(tokenizer.hasNext()){
			return tokenizer.nextToken();
		}
		else{
			return "";
		}
	}

}
