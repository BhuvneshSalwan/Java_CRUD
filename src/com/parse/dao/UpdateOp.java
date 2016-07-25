package com.parse.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateOp {
	static Object columnValue[] = new Object[20];
	static Object columnName[] = new Object[20];
	static JSONObject whereId;
	static String columnNameQuery="";
	static int intColumnNames,intColumnValues;

	public static boolean setUpdateOp(JSONObject dataObj, PrintWriter out){
		
		JSONArray columnNames = dataObj.getJSONArray("column_name");
		JSONArray columnValues = dataObj.getJSONArray("column_values");
		whereId = dataObj.getJSONObject("where");
		if(columnNames.length() == columnValues.length()){
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < columnNames.length(); i++)
			{				
				if(i == 0){
					sb.append(columnNames.getString(0)+"=?");
				}
				else if(i == columnNames.length()-1){
					sb.append(","+columnNames.getString(i)+"=?");
				}
				else{
					sb.append(","+columnNames.getString(i)+"=?");
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
	
	public static boolean createUpdateOp(String dbname, String table_name, PrintWriter out){
	try{
			DBDao dbManager = DBDao.getInstance();
			Connection con = dbManager.createConnection();
			String sql = "update "+dbname+"."+table_name+" set "+columnNameQuery+" where "+whereId.getString("primary_key")+"=?;";
			PreparedStatement ps = con.prepareStatement(sql);
			for(int i = 0; i <= intColumnValues; i++)
			{	
				ps.setObject(i+1, columnValue[i]);
				if(i == intColumnValues)
				{
					ps.setObject(i+1, whereId.getString("key_value"));
				}
			}
			int status = ps.executeUpdate();
			if(status == 1){
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
