package edu.ict.rgeothes.tools.loaders;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.ict.rgeothes.entity.Document;
import edu.ict.rgeothes.entity.Name;
import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.entity.Record;
import edu.ict.rgeothes.entity.RecordReference;
import edu.ict.rgeothes.tools.CoordinatesConverter;
import edu.ict.rgeothes.tools.DateBuilder;

public class KzLoader implements IRecordsLoader {

	private static final String PRIMARY_LANG = "kz";
	private static final String SECONDARY_LANG = "ru";
	
	
	@Override
	public List<Record> parseRecords(List<String> inputLines) {
Map<String, Record> recordsMap = new HashMap<>();
		
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
			
			String previousRecordName = stringItems[5];
			
			if (StringUtils.isNotBlank(previousRecordName) && recordsMap.containsKey(previousRecordName)) {
				RecordReference previousReference = new RecordReference();
				previousReference.setRecordFrom(recordsMap.get(previousRecordName));
				previousReference.setRecordTo(record);
				
				Document changeNameDocument = new Document(
						String.format( "Изменение названия с %s на %s",
								previousReference.getRecordFrom().getPrimaryName().getName(),
								previousReference.getRecordTo().getPrimaryName().getName()),
						date);
				
				changeNameDocument.setCreationDate(date);
				
				previousReference.setDocument(changeNameDocument);
				
				record.setPrevious(previousReference);
			}
			
			//object location
			Point p = createPoint(stringItems[8]);
			
			p.setBeginDocument(beginDoc);
			
			record.addLocation(p);
			
			result.add(record);
			
			recordsMap.put(primaryName.getName(), record);
			
		}
		return result;
	}
	
	private static Point createPoint(String pointData) {
		//52°00′00″ с. ш. 70°56′00″ в. д
		
		return CoordinatesConverter.parsePoint(pointData);
	}

}
