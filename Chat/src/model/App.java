package model;

import java.sql.SQLException;

import view.LoginView;
import control.DB_connection;

public class App {
	
	public static Service db_service;
	
	public App() throws SQLException {
		DB_connection connection = new DB_connection();
		db_service = new Service(connection.connect());
		
		new LoginView(db_service);
	}

	public static void main(String[] args) throws SQLException {
		new App();
	}
}
