package edu.ict.rgeothes.tools.loaders;

import java.util.List;

import edu.ict.rgeothes.entity.Record;

public interface IRecordsLoader {
	List<Record> parseRecords(List<String> inputLines);
}
