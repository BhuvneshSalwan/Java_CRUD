package com.parse.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class InsertOp {
	static Object columnValue[] = new Object[20];
	static Object columnName[] = new Object[20];
	static String columnNameQuery="";
	static int intColumnNames,intColumnValues;

	public static boolean setInsertOp(JSONObject dataObj, PrintWriter out){
		JSONArray columnNames = dataObj.getJSONArray("column_name");
		JSONArray columnValues = dataObj.getJSONArray("column_values");
		if(columnNames.length() == columnValues.length()){
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < columnNames.length(); i++)
			{
				if(i == 0){
					sb.append("("+columnNames.getString(0));
				}
				else if(i == columnNames.length()-1){
					sb.append(","+columnNames.getString(i)+")");
				}
				else{
					sb.append(","+columnNames.getString(i));
				}
			}
			columnNameQuery = sb.toString();
			for(int i = 0; i < columnNames.length(); i++)
			{
				columnName[i] = columnNames.getString(i);
			}
			for(int i = 0; i < columnValues.length(); i++)
			{
				columnValue[i] = columnValues.getString(i);
			}
			intColumnNames = columnNames.length();
			intColumnValues = columnNames.length();
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean createInsertOp(String dbname, String table_name, PrintWriter out){
		try{
			DBDao dbManager = DBDao.getInstance();
			Connection con = dbManager.createConnection();
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < intColumnNames; i++)
			{
				if(i == 0){
					sb.append("(?");
				}
				else if(i == intColumnNames-1){
					sb.append(",?);");
				}
				else{
					sb.append(",?");
				}
			}
			String partialQuery = sb.toString();
			String sql = "insert into "+dbname+"."+table_name+""+columnNameQuery+" values "+partialQuery;
			PreparedStatement ps = con.prepareStatement(sql);
			for(int i = 0; i < intColumnValues; i++)
				ps.setObject(i+1, columnValue[i]);
				int status = ps.executeUpdate();
				if(status == 1){
					String query = "select last_insert_id() as last_id from "+dbname+"."+table_name+";";
					ps = con.prepareStatement(query);
					ResultSet rs = ps.executeQuery();
					if(rs.next()){
					out.println(rs.getObject("last_id"));
				}
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