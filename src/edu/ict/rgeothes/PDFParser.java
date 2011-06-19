package edu.ict.rgeothes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


public class PDFParser {

	private static final String INPUT_FILE = "input/novosib_obl.pdf";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			
			PDDocument pddDocument=PDDocument.load(new File(INPUT_FILE));
			PDFTextStripper textStripper=new PDFTextStripper();
			textStripper.setSortByPosition(true);
			
			FileWriter fileWriter = new FileWriter("output.txt");
			
			fileWriter.write(textStripper.getText(pddDocument));
			
			fileWriter.close();
			
			pddDocument.close();
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		
	}

}
