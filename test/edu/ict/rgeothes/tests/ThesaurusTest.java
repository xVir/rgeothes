package edu.ict.rgeothes.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ict.rgeothes.Thesaurus;

public class ThesaurusTest {

	@Test
	public void testInitialize() {
		Thesaurus thesaurus = loadFromFile("input/novosib.txt");
		assertNotNull(thesaurus);
		assertEquals(867, thesaurus.getRecordsCount());
	}

	private Thesaurus loadFromFile(String string) {
		
		Thesaurus result = new Thesaurus();
		
		// TODO Auto-generated method stub
		
		return result;
	}

}
