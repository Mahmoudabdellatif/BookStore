package eg.edu.alexu.csd.database.mylibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDBConnection {

	private static MyDBConnection myDBConnection;
	private Connection myConnection;
	private Statement myStatement;

	private MyDBConnection() {
		try {
			// connection to database
			myConnection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/book_store",
							"root", "");
			// create statement
			myStatement = myConnection.createStatement();
			/* execute sql query
			ResultSet myRs = myStatement.executeQuery("select * from employees");
			// results set
			while (myRs.next()) {
				System.out.println(myRs.getString("last name") 
						+ " , " + myRs.getString("first name") 
						+ " , " + myRs.getString("email"));
			}*/
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return myConnection;
	}

	public static MyDBConnection getInstance() {
		if (myDBConnection == null) {
			myDBConnection = new MyDBConnection();
		}
		return myDBConnection;
	}

	public Connection getMyConnection() {
		return myConnection;
	}

	public Statement getMyStatement() {
		return myStatement;
	}
	
	public ResultSet executeQuery(String query){
		try {
			ResultSet myRs = myStatement.executeQuery(query);
			return myRs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean executeUpdateQuery(String query){
		try {
			int i = myStatement.executeUpdate(query);
			if(i == 0){
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
}
