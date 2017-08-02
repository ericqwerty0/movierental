package com.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AdminDBConnection {

	private static Connection con;

	public static  Connection DBC() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/movie", "eric", "123456");
			if (con != null) {
				System.out.println("Connected to the database");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
}
