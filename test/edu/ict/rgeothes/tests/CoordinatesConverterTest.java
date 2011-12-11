package edu.ict.rgeothes.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.tools.CoordinatesConverter;

public class CoordinatesConverterTest {

	double eps = 0.0001;
	
	@Test
	public void testParseCoordinate() {
		String input ="52°00′00″";
		double parsedCoordinate = CoordinatesConverter.parseCoordinate(input);
		
		assertEquals(52, parsedCoordinate,eps);
	}
	
	@Test
	public void testParsePoint() {
		//52°00′00″ с. ш. 70°00′00″ в. д
		String input = "52°00′00″ с. ш. 70°00′00″ в. д";
		
		Point point = CoordinatesConverter.parsePoint(input);
		
		
		assertEquals(52, point.getLatitude(),eps);
		assertEquals(70, point.getLongitude(),eps);
		
	}

}
