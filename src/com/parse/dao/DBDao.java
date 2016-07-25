package com.parse.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBDao {
	private static DBDao instance;

	public static DBDao getInstance() {
		if (instance == null) {
			instance = new DBDao();
		}
		return instance;
	}
	public Connection createConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 	conn = DriverManager.getConnection("------connection url------------","------username------", "---------password---------");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return conn;
		}
}
