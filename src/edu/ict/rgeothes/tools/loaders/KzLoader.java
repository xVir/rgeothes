package edu.ict.rgeothes.tools.loaders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.ict.rgeothes.entity.Document;
import edu.ict.rgeothes.entity.Name;
import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.entity.Record;
import edu.ict.rgeothes.exceptions.InvalidInputException;
import edu.ict.rgeothes.tools.CoordinatesConverter;
import edu.ict.rgeothes.tools.DateBuilder;

public class KzLoader implements IRecordsLoader {

	private static final String PRIMARY_LANG = "kz";
	private static final String SECONDARY_LANG = "ru";
	
	
	@Override
	public List<Record> parseRecords(List<String> inputLines) throws InvalidInputException {
		
		List<Record> result = new ArrayList<Record>(); 
		for (int i = 1; i < inputLines.size(); i++) {
			String inputString = inputLines.get(i);
			
			String[] stringItems = StringUtils.splitPreserveAllTokens(inputString, '\t');
			
			String previousRecordName = stringItems[5];
			Date appearsDate = DateBuilder.buildDate(Integer.parseInt(stringItems[4]), 1, 1);
			Name firstName = new Name(stringItems[1], stringItems[6], PRIMARY_LANG);
			Name secondName = new Name(stringItems[2], stringItems[6], SECONDARY_LANG);
			
			Record record;
			Document nameDocument;
			
			if (StringUtils.isNotBlank(previousRecordName)) {
				record = getExistsRecord(result,previousRecordName);
				
				Name previousName = getPreviousName(record,previousRecordName);
				
				Document changeNameDocument = new Document(
						String.format( "Изменение названия с %s на %s",
								previousName.getName(),
								firstName.getName()),
						appearsDate);
				
				changeNameDocument.setCreationDate(appearsDate);
				previousName.setEndDocument(changeNameDocument);
				
				firstName.setBeginDocument(changeNameDocument);
				secondName.setBeginDocument(changeNameDocument);
				
				record.addName(firstName);
				record.addName(secondName);
			}
			else {
				record = new Record();
				
				nameDocument = new Document();
				nameDocument.setDate(appearsDate);
				nameDocument.setCreationDate(appearsDate);
				nameDocument.setDescription("Создание объекта " + stringItems[1]);
				nameDocument.setUri("uri of document");
				
				firstName.setBeginDocument(nameDocument);
				secondName.setBeginDocument(nameDocument);
				
				record.addName(firstName);
				record.addName(secondName);
				
				Point objectLocation = createPoint(stringItems[8]);
				objectLocation.setBeginDocument(nameDocument);
				record.addLocation(objectLocation);
				
				result.add(record);
			}
			
		}
		return result;
	}
	
	private Name getPreviousName(Record record, String previousRecordName) throws InvalidInputException {
		for(Name name : record.getNames()){
			if (name.getName().equals(previousRecordName)) {
				return name;
			}
		}
		throw new InvalidInputException();
	}

	private Record getExistsRecord(List<Record> records,
			String previousRecordName) throws InvalidInputException {
		for(Record rec:records){
			if (rec.hasName(previousRecordName)) {
				return rec;
			}
		}
		throw new InvalidInputException();
	}

	private static Point createPoint(String pointData) {
		//52°00′00″ с. ш. 70°56′00″ в. д
		
		return CoordinatesConverter.parsePoint(pointData);
	}

}
