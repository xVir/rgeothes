package edu.ict.rgeothes.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.postgresql.PGConnection;

import edu.ict.rgeothes.entity.Record;

public class RecordDao {

//	private static final SessionFactory sessionFactory = createSessionFactory();

	private Connection connection;

	public RecordDao() {
	}
	
	public RecordDao(Connection connection) {
		this.connection = connection;
	}
	

	//private static SessionFactory createSessionFactory() {
	//	Configuration configuration = new Configuration();
		
//		configuration.configure();
		
//		return configuration.buildSessionFactory();
	//}

	public void addRecords(List<Record> records) {

		for (Record rec : records) {
			insertRecord(rec);
		}

	}

	private void insertRecord(Record rec) {
		/*Session s = sessionFactory.getCurrentSession();
		s.beginTransaction();

		s.save(rec);

		s.getTransaction().commit();
		
		*/
		
		try {
			Statement s = connection.createStatement();
		
			createSqlCommands(rec);
		
				
		
		
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		
		
		

	}

	private List<String> createSqlCommands(Record rec) {

		List<String> result = new  ArrayList<>();
		
		result.add(createSqlFromRecord(rec));
		
		
		return result;
	}

	private String createSqlFromRecord(Record rec) {
		// TODO Auto-generated method stub
		return null;
	}

}
