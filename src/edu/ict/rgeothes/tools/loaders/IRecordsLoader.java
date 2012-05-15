package edu.ict.rgeothes.tools.loaders;

import java.util.List;

import edu.ict.rgeothes.entity.Record;
import edu.ict.rgeothes.exceptions.InvalidInputException;
import edu.ict.rgeothes.exceptions.RecordNotFoundException;

public interface IRecordsLoader {
	List<Record> parseRecords(List<String> inputLines) throws InvalidInputException;
}
