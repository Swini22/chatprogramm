package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DB_connection {
	
	private Connection connect;
	
	public Connection connect() throws SQLException{
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "admin");
		return connect;
	}
}
