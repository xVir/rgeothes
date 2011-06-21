package edu.ict.rgeothes.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.itextpdf.text.pdf.parser.TextRenderInfo;

import edu.ict.rgeothes.Thesaurus;

public class ThesaurusTest {

	@Test
	public void testInitialize() {
		Thesaurus thesaurus = loadFromFile("input/novosib.txt");
		assertNotNull(thesaurus);
		assertEquals(867, thesaurus.getRecordsCount());
	}

	private Thesaurus loadFromFile(String fileName) {
		
		Thesaurus result = new Thesaurus();
		
		/*
		 * 1 0063376 Абрамовка посёлок Чановский район 55° 04' С.Ш. 75° 04' В.Д. N-43-31
		   Снят с учета 06/05/1988
		   Решение Новосибирского облсовета № 236 от 
		   06/05/1988.
		   2 0080244 Абушкан деревня Чистоозерный район 54° 46' С.Ш. 76° 11' В.Д. N-43-45
		   Абышкан	
	   	   Снят с учета 14/09/1982
		   Решение Новосибирского облсовета народных 
		   депутатов № 561 от 14/09/1982.
		   
		   5 0063371 Акпановка аул Карасукский район 53° 34' С.Ш. 77° 45' В.Д. N-43-96
		   Карасук-Казах
		   Снят с учета 24/10/1971 Карасук-Казах (Акпановский)
	       Решение Новосибирского облсовета № 604 от 
 		   24/10/1971.
		   Стр.  1  из  111
		   5 1 2 3 4 6 7
		   
		   625 0112609 Посёлок фермы № 1 свх. посёлок Венгеровский район 55° 41' С.Ш. 77° 17' В.Д. N-43-11
		   "Усть-Ламенский" Усть Ламенка Новый род - село
		   Усть-Ламенка
		   Изменился род объекта Усть-Ламенкай
		   Усть-Ламенский
           Усть-Ламенское
           Усть-Ламинка
           Усть-Сламанка
		   
		 */
		
		
		try {
			FileReader fileReader = new FileReader(fileName);
			
			BufferedReader reader = new BufferedReader(fileReader);
			
			String first,second,third, forth;
			
			first = reader.readLine();
			
			while(StringUtils.isNotBlank(first)){
				
				if (first.startsWith("Стр.  ")) {
					//skip 2 lines
					reader.readLine();
					first = reader.readLine();
					continue;
				}
				
				second = reader.readLine();
				third = reader.readLine();
				
				forth = reader.readLine();
				
				if (forth.charAt(0) >= '0' && forth.charAt(0) <= '9') {
					//it is next record
					first = forth;
					continue;
				}
				else{
					//it is not the next record
					//parsing forth string
					System.out.println(forth);
					
					
				}
				
				
				first = reader.readLine();
			}
			
			
			
			fileReader.close();
			
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		// TODO Auto-generated method stub
		
		return result;
	}
	
	

}
