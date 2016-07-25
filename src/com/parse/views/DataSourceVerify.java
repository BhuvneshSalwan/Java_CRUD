package com.parse.views;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.parse.dao.DBDao;

public class DataSourceVerify {
	static ResultSet rs;
	static Connection con;
	public static  boolean DataSource(String dbname, String table_name, PrintWriter out){
	try{
		DBDao dbManager= DBDao.getInstance();
		con = dbManager.createConnection();
		String query = "show tables from "+dbname+" like '"+table_name+"';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if(rs.next()){
			return true;
		}
		else{
			return false;
		}	
	}catch(Exception e)
	{
		return false;
	}
	}
}
