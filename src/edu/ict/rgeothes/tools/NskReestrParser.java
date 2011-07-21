package edu.ict.rgeothes.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import edu.ict.rgeothes.entity.Name;
import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.entity.Record;

public class NskReestrParser {

	private static final String RU = "RU-ru";

	private static final char TAB = '\t';

	private static String[] documentBeginings = { "Снят с", "Изменился род", };

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String inputFilePath = "input/novosib.txt";
		
		readRecordsFormFile(inputFilePath);
		

	}

	public static List<Record> readRecordsFormFile(String inputFilePath) {
		try {
			
			
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
								startIndex, endIndex));

						System.out.println(record);
	//					writer.append(record.toString());
//						writer.append("\n");
						
						records.add(record);
					} catch (Exception ex) {

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

	private static int getNextRecordIndex(List<String> fileContent,
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

	private static Record getRecordFromLines(List<String> lines) {

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

		StrTokenizer secondLine = new StrTokenizer(lines.get(1), TAB);

		String firstToken = secondLine.nextToken();

		List<String> anotherNames = new ArrayList<String>();
		StringBuilder documentDescription = new StringBuilder();

		if (!StringUtils.isBlank(firstToken)) {

			if (firstToken.contains("Снят с")) {
				documentDescription.append(firstToken);

				documentDescription.append(" ");
				documentDescription.append(getFirstTokenFromLine(lines.get(2)));

				documentDescription.append(" ");
				documentDescription.append(getFirstTokenFromLine(lines.get(3)));

			} else if (firstToken.contains("Изменился род")) {

			} else {
				// firstToken is additional name
				anotherNames.add(firstToken);
			}
		}

		result.setQualifier(name);
		result.addName(new Name(name, placeType, RU));

		result.addLocation(new Point(latitudeValue, longitudeValue));
		
		for (String anotherName : anotherNames) {
			result.addName(new Name(anotherName, placeType, RU));
		}

		return result;

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

}
