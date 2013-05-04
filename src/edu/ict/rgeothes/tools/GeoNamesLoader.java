package edu.ict.rgeothes.tools;

import edu.ict.rgeothes.dao.RecordDao;
import edu.ict.rgeothes.entity.Location;
import edu.ict.rgeothes.entity.Name;
import edu.ict.rgeothes.entity.Point;
import edu.ict.rgeothes.entity.Record;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xVir
 * Date: 04.05.13
 * Time: 22:19
 * Tool for loading Geonames dump http://download.geonames.org/export/dump/ into thesaurus
 */
public class GeoNamesLoader {
    public static void main(String[] args) throws SQLException, IOException {


        String fileName = "input/RU.txt";
        List<Record> records = readRecordsFormFile(fileName);

        System.out.println("Records count: " + records.size());

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/rgeothes", "postgres",
                "postgres");

        connection.setAutoCommit(true);

        //clearThesaurus(connection);

        new RecordDao(connection).addRecords(records);

    }

    private static List<Record> readRecordsFormFile(String fileName) throws IOException {
        List<String> fileContent = FileUtils.readLines(new File(
                fileName));

        List<Record> records = new ArrayList<Record>();

        System.out.println("Lines to proceed: " + fileContent.size());

        for (String recordString : fileContent){
            String[] recordParts = recordString.split("\t");
            String namesPart = recordParts[3];
            String latitude = recordParts[4];
            String longitude = recordParts[5];

            if (StringUtils.isNotEmpty(namesPart)){
                String[] names = namesPart.split(",");
                for (String name : names){
                    String clearedName = name.trim();

                    char firstLetter = clearedName.charAt(0);
                    if (firstLetter > 'А' && firstLetter < 'я'){
                        records.add(createNewRecord(clearedName,latitude,longitude));
                    }
                }
            }
        }

        return records;
    }

    private static Record createNewRecord(String clearedName, String latitude, String longitude) {
        Record record = new Record();

        Location location = new Point(Double.parseDouble(latitude),Double.parseDouble(longitude));
        record.addLocation(location);

        record.addName(new Name(clearedName,"нп", "ru-RU"));

        return record;
    }


    private static void clearThesaurus(Connection connection)
            throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("TRUNCATE document CASCADE; TRUNCATE record CASCADE; TRUNCATE record_reference CASCADE");

    }
}
