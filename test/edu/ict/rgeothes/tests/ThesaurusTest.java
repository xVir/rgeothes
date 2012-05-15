package edu.ict.rgeothes.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import edu.ict.rgeothes.Thesaurus;
import edu.ict.rgeothes.entity.Record;
import edu.ict.rgeothes.exceptions.InvalidInputException;
import edu.ict.rgeothes.search.DateRange;
import edu.ict.rgeothes.search.SearchQuery;
import edu.ict.rgeothes.tools.NskReestrParser;
import edu.ict.rgeothes.tools.loaders.KzLoader;

public class ThesaurusTest {

	@Test
	public void testInitialize() {
		Thesaurus thesaurus = loadFromFile("input/novosib.txt");
		assertNotNull(thesaurus);
		assertEquals(843, thesaurus.getRecordsCount());
	}
	
	@Test
	public void testSimpleSearch() throws IOException, InvalidInputException{
			Thesaurus thesaurus = loadTestRecords();

			SearchQuery query = new SearchQuery();
			query.setName("Алматы");
			List<Record> records = thesaurus.search(query);
			assertTrue(records.size() >= 1);	
			assertEquals("Алматы", getPrimaryName(records.get(0)));
			
			query = new SearchQuery();
			query.setName("Алматы");
			query.setDateRange(new DateRange(1900, 1901));
			records = thesaurus.search(query);
			assertTrue(records.size() >= 1);	
			assertEquals("Верный", getPrimaryName(records.get(0)));
			
			query = new SearchQuery();
			query.setName("Алматы");
			query.setDateRange(new DateRange(1980));
			records = thesaurus.search(query);
			assertTrue(records.size() >= 1);
			assertEquals("Алма-Ата", getPrimaryName(records.get(0)));
	}
	
	@Test
	public void testLoadingObjects() throws IOException, InvalidInputException{
		String fileName = "tests_input/Almata.txt";
		
		File inputFile = new File(fileName);
		List<String> inputLines = FileUtils.readLines(inputFile, "Unicode");
		List<Record> records = new KzLoader().parseRecords(inputLines);
		
		assertEquals(1, records.size());
	}

	private String getPrimaryName(Record record) {
		return record.getNames().get(0).getName();
	}

	private Thesaurus loadTestRecords() throws IOException, InvalidInputException {
		String fileName = "tests_input/Almata.txt";
		
		File inputFile = new File(fileName);
		List<String> inputLines = FileUtils.readLines(inputFile, "Unicode");
		
		Thesaurus thesaurus = new Thesaurus();
		thesaurus.addRecords(new KzLoader().parseRecords(inputLines));
		
		return thesaurus;
	}

	private Thesaurus loadFromFile(String fileName) {

		Thesaurus result = new Thesaurus();

		/*
		 * 1 0063376 Абрамовка посёлок Чановский район 55° 04' С.Ш. 75° 04' В.Д.
		 * N-43-31 Снят с учета 06/05/1988 Решение Новосибирского облсовета №
		 * 236 от 06/05/1988. 2 0080244 Абушкан деревня Чистоозерный район 54°
		 * 46' С.Ш. 76° 11' В.Д. N-43-45 Абышкан Снят с учета 14/09/1982 Решение
		 * Новосибирского облсовета народных депутатов № 561 от 14/09/1982.
		 * 
		 * 5 0063371 Акпановка аул Карасукский район 53° 34' С.Ш. 77° 45' В.Д.
		 * N-43-96 Карасук-Казах Снят с учета 24/10/1971 Карасук-Казах
		 * (Акпановский) Решение Новосибирского облсовета № 604 от 24/10/1971.
		 * Стр. 1 из 111 5 1 2 3 4 6 7
		 * 
		 * 625 0112609 Посёлок фермы № 1 свх. посёлок Венгеровский район 55° 41'
		 * С.Ш. 77° 17' В.Д. N-43-11 "Усть-Ламенский" Усть Ламенка Новый род -
		 * село Усть-Ламенка Изменился род объекта Усть-Ламенкай Усть-Ламенский
		 * Усть-Ламенское Усть-Ламинка Усть-Сламанка
		 */

		NskReestrParser parser = new NskReestrParser();

		result.addRecords(parser.readRecordsFormFile(fileName));

		return result;
	}

}
