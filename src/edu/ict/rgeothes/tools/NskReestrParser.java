package edu.ict.rgeothes.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.ict.rgeothes.entity.Document;
import edu.ict.rgeothes.entity.Name;
import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.entity.Record;

public class NskReestrParser {

	private static final String RETIRED_NAME_DOCUMENT_DESCR = "Снят с учета ";

	private static final String RU = "RU-ru";

	private static final char TAB = '\t';

	private static String[] documentBeginings = { "Снят с", "Изменился род", };

	private List<Record> districts = new ArrayList<Record>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			String inputFilePath = "input/novosib.txt";
			
			List<Record> records = readRecordsFromFile(inputFilePath);

			printNamesToFile(records);
			printRecordsToJson(records);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static List<Record> readRecordsFromFile(String inputFilePath) {
		NskReestrParser parser = new NskReestrParser();

		List<Record> records = parser.readRecordsFormFile(inputFilePath);
		return records;
	}

	private static void printRecordsToJson(List<Record> records) throws FileNotFoundException {
		
		PrintStream outStream = new PrintStream(new FileOutputStream(
				new File("sysout.txt")));

		System.setOut(outStream);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		for (Record record : records) {
			System.out.println(gson.toJson(record));
			System.out.println();	
		}
	}

	private static void printNamesToFile(List<Record> records)
			throws FileNotFoundException {
		PrintStream namesOutputStream = new PrintStream(
				new FileOutputStream("nsk_names.txt"));
		for (Record record : records) {
			namesOutputStream.println(record.getNames().get(0).getName());
		}
		namesOutputStream.close();
	}

	public List<Record> readRecordsFormFile(String inputFilePath) {
		try {

			Record russiaRecord = createRussiaRecord();
			Record novosibirskStateRecord = createNovosibirskStateRecord(russiaRecord);

			List<String> fileContent = FileUtils.readLines(new File(
					inputFilePath));

			List<Record> records = new ArrayList<Record>();

			if (fileContent.size() <= 0) {
				System.out.println("Nothing to do");
				return records;
			}

			System.out.println("Lines to proceed: " + fileContent.size());

			records.add(russiaRecord);
			records.add(novosibirskStateRecord);
			
			int startIndex;
			int endIndex;

			startIndex = 0;

			List<Record> parsedRecords = new ArrayList<Record>();
			while (startIndex < fileContent.size() - 1) {
				endIndex = getNextRecordIndex(fileContent, startIndex);

				try {
					Record record = createRecordFromLines(
							fileContent.subList(startIndex, endIndex),
							novosibirskStateRecord);

					System.out.println(record);

					parsedRecords.add(record);
				} catch (Exception ex) {
					System.out.println("error: " + ex.getMessage());
				}

				startIndex = endIndex;
			}

			records.addAll(districts);
			records.addAll(parsedRecords);
			
			System.out.println("Finished!");

			System.out.println(records.size() + " records processed");
			System.out.println(records.size() / 867.0);

			return records;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Record createNovosibirskStateRecord(Record russiaRecord) {
		Record parentRecord = new Record();
		parentRecord.setPrimaryParent(russiaRecord, Document.PAST);
		parentRecord.addName(new Name("Новосибирская область", "область", RU));
		return parentRecord;
	}

	private Record createRussiaRecord() {
		Record russiaRecord = new Record();
		russiaRecord.setPrimaryParent(Record.ROOT_RECORD, Document.PAST);
		russiaRecord
				.addName(new Name("Российская Федерация", "государство", RU));
		return russiaRecord;
	}

	private int getNextRecordIndex(List<String> fileContent, int startIndex)
			throws IllegalArgumentException {
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

	private Record createRecordFromLines(List<String> lines, Record parentRecord) {

		Record resultRecord = new Record();

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

		resultRecord.setPrimaryParent(districtRecord, Document.PAST);

		Name resultName = new Name(name, placeType, RU);

		Document retiredDocument=null;
		if (documentDescription.toString()
				.contains(RETIRED_NAME_DOCUMENT_DESCR)) {
			retiredDocument = createRetireDocument(documentDescription);
		}

		if (retiredDocument != null) {
			resultName.setEndDocument(retiredDocument);
		}
		
		resultRecord.addName(resultName);

		resultRecord.addLocation(new Point(latitudeValue, longitudeValue));

		for (String anotherName : anotherNames) {
			final Name additionalName = new Name(anotherName, placeType, RU);
			
			if (retiredDocument!=null) {
				additionalName.setEndDocument(retiredDocument);	
			}
			
			resultRecord.addName(additionalName);
		}

		return resultRecord;

	}

	private Document createRetireDocument(StringBuilder documentDescription) {
		Document retiredDocument;
		String docDescr = documentDescription.toString();

		String documentDateString = StringUtils.substringBetween(docDescr,
				RETIRED_NAME_DOCUMENT_DESCR, " ");

		Date documentDate = null;
		try {
			documentDate = new SimpleDateFormat("dd/MM/yyyy")
					.parse(documentDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String shortDocumentDescription = StringUtils.substringAfter(
				docDescr, documentDateString).trim();

		retiredDocument = new Document(shortDocumentDescription,
				documentDate);
		return retiredDocument;
	}

	private void fillNamesAndDocDescription(int lineNumber, List<String> lines,
			StringBuilder outDocDescription, List<String> outAnotherNames) {

		if (lines.size() > lineNumber) {
			String firstToken = getFirstTokenFromLine(lines.get(lineNumber));

			if (StringUtils.isNotBlank(firstToken)) {

				if (firstToken.contains("Снят с")) {

					String secondToken = getSecondTokenFromLine(lines
							.get(lineNumber));
					if (StringUtils.isNotBlank(secondToken)) {
						outAnotherNames.add(secondToken);
					}

					outDocDescription.append(firstToken);

					if (lines.size() > lineNumber + 1) {
						outDocDescription.append(" ");
						outDocDescription.append(getFirstTokenFromLine(lines
								.get(lineNumber + 1)));

						secondToken = getSecondTokenFromLine(lines
								.get(lineNumber + 1));
						if (StringUtils.isNotBlank(secondToken)) {
							outAnotherNames.add(secondToken);
						}

						if (lines.size() > lineNumber + 2) {
							outDocDescription.append(" ");
							outDocDescription
									.append(getFirstTokenFromLine(lines
											.get(lineNumber + 2)));

							secondToken = getSecondTokenFromLine(lines
									.get(lineNumber + 2));
							if (StringUtils.isNotBlank(secondToken)) {
								outAnotherNames.add(secondToken);
							}

							fillNamesAndDocDescription(lineNumber + 3, lines,
									outDocDescription, outAnotherNames);
						}

					}

				} else if (firstToken.contains("Изменился род")) {

					String secondToken = getSecondTokenFromLine(lines
							.get(lineNumber));
					if (StringUtils.isNotBlank(secondToken)) {
						outAnotherNames.add(secondToken);
					}

					fillNamesAndDocDescription(lineNumber + 1, lines,
							outDocDescription, outAnotherNames);

				} else {
					// firstToken is additional name
					outAnotherNames.add(firstToken);

					fillNamesAndDocDescription(lineNumber + 1, lines,
							outDocDescription, outAnotherNames);
				}
			}
		}

	}

	private Record getDistrictRecord(Record parentRecord, String districtName) {

		Record existsDistrict = searchDistrict(districtName);
		if (existsDistrict == null) {
			existsDistrict = new Record();
			existsDistrict.setPrimaryParent(parentRecord, Document.PAST);
			existsDistrict.addName(new Name(districtName, "район", RU));
			districts.add(existsDistrict);
		}
		return existsDistrict;
		
	}

	private Record searchDistrict(String districtName) {
		for(Record district : districts){
			if(district.hasName(districtName)){
				return district;
			}
		}
		return null;
	}

	private static double longitudeFromString(String longitudeString) {
		// 84° 08' В.Д.

		int degrees = Integer.parseInt(longitudeString.substring(0,
				longitudeString.indexOf('°')));
		int minutes = Integer.parseInt(longitudeString.substring(
				longitudeString.indexOf("° ") + 2,
				longitudeString.indexOf('\'')));

		double result = degrees + minutes / 60.0;

		return result;
	}

	private static double latitudeFromString(String latitudeString) {
		// 54° 56' С.Ш.

		int degrees = Integer.parseInt(latitudeString.substring(0,
				latitudeString.indexOf('°')));
		int minutes = Integer
				.parseInt(latitudeString.substring(
						latitudeString.indexOf("° ") + 2,
						latitudeString.indexOf('\'')));

		double result = degrees + minutes / 60.0;

		return result;
	}

	private static String getFirstTokenFromLine(String input) {
		StrTokenizer tokenizer = new StrTokenizer(input, TAB);
		return tokenizer.nextToken();
	}

	private static String getSecondTokenFromLine(String input) {
		StrTokenizer tokenizer = new StrTokenizer(input, TAB);
		tokenizer.nextToken();
		if (tokenizer.hasNext()) {
			return tokenizer.nextToken();
		} else {
			return "";
		}
	}

}
