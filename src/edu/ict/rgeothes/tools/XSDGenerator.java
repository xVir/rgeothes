package edu.ict.rgeothes.tools;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import edu.ict.rgeothes.entity.*;

public class XSDGenerator {

	/**
	 * @param args
	 * @throws JAXBException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws JAXBException, IOException {

		class MySchemaOutputResolver extends SchemaOutputResolver {
			File baseDir = new File(".");

			public Result createOutput(String namespaceUri,
					String suggestedFileName) throws IOException {
				return new StreamResult(new File(baseDir, suggestedFileName));
			}
		}

		JAXBContext context = JAXBContext.newInstance(
				Record.class,
				Point.class, 
				Document.class,
				Location.class,
				Name.class,
				RecordReference.class,
				Rectangle.class);
		context.generateSchema(new MySchemaOutputResolver());

	}

}
