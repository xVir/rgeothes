package edu.ict.rgeothes.tools;

import org.apache.commons.lang.StringUtils;

import edu.ict.rgeothes.entity.Point;

public class CoordinatesConverter {
	
	public static Point parsePoint(String pointData){
		
		//52°00′00″ с. ш. 70°00′00″ в. д
		
		String latitude = pointData.substring(0, pointData.indexOf("с. ш.")-1).trim();
		String longitude = pointData.substring(pointData.indexOf("с. ш.") + "с. ш.".length(),pointData.indexOf("в. д")).trim();
		
		return new Point(parseCoordinate(latitude), parseCoordinate(longitude));
		
	}
	
	public static double parseCoordinate(String coord){
		//52°00′00″
		
		String[] coordParts = StringUtils.split(coord, "°′″");
		
		int degrees = Integer.parseInt(coordParts[0]);
		int minutes = Integer.parseInt(coordParts[1]);
		int seconds = Integer.parseInt(coordParts[2]);
		
		return degrees + minutes/60.0 + seconds/ (60.0*60.0);
		
	}
	

}
