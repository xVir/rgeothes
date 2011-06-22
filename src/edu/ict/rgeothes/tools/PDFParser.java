package edu.ict.rgeothes.tools;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Window;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.util.PDFTextStripper;


public class PDFParser {

	private static final String INPUT_FILE = "input/novosib_obl.pdf";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//Frame frame = new Frame("Hello");
		//frame.setBounds(300, 300, 300, 300);
		//frame.setVisible(true);
		//Graphics graphics = frame.getGraphics();
		
		try {
			
			PDDocument pddDocument=PDDocument.load(new File(INPUT_FILE));
			
			//PageDrawerExt pageDrawerExt = new PageDrawerExt();
			
//			Iterator<?> pageIter =
//				pddDocument.getDocumentCatalog().getAllPages().iterator();
//			
//			int iter=0;
//			
//			while( pageIter.hasNext() )
//	        {
//				PDPage nextPage = (PDPage)pageIter.next();
//				
//				pageDrawerExt.drawPage(graphics, nextPage, new Dimension(200, 300));
//				
//				iter++;
//				
//				if (iter >= 10) {
//					break;
//				}
//	        }
			
			
			PDFTextStripper textStripper=new PDFTextStripper();
			textStripper.setSortByPosition(true);
			textStripper.setWordSeparator("\t");
			FileWriter fileWriter = new FileWriter("output.txt");
			
			fileWriter.write(textStripper.getText(pddDocument));
			
			fileWriter.close();
			
			pddDocument.close();
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		
	}

}
