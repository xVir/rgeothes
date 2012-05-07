package edu.ict.rgeothes.tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerFunctionsTest {

	
	
	private Connection connection;

	@Before
	public void setUp() throws Exception{
		connection = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/rgeothes", "postgres",
				"postgres");

	}
	
	@After
	public void tearDown() throws Exception{
		connection.close();
	}
	
	@Test
	public void testGetObjectHistory() throws Exception {
		
		String queryStatement = "SELECT get_object_history()";
		
		fail("Not yet implemented"); // TODO
	}
	
	private Statement createStatement() throws Exception{
		return connection.createStatement();
	}

}
