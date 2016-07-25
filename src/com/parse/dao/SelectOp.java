package com.parse.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.json.JSONArray;
import org.json.JSONObject;

public class SelectOp {
	static Object columnName[] = new Object[20];
	static String columnNameQuery;
	static int intColumnNames;
	static String sql;
	static JSONArray responseData;
	static PreparedStatement ps;
	public static boolean setSelectOp(JSONObject dataObj, PrintWriter out){
		
		if(dataObj.has("column_name")){
			JSONArray columnNames = dataObj.getJSONArray("column_name");
			StringBuffer sb = new StringBuffer();
				if(columnNames.getString(0).equals("*"))
			{
				sb.append("*");
			}
			else{
			for(int i = 0; i < columnNames.length(); i++)
			{
				if(i == 0){
					sb.append(columnNames.getString(0));
				}
				else if(i == columnNames.length()-1){
					sb.append(","+columnNames.getString(i));
				}
				else{
					sb.append(","+columnNames.getString(i));
				}
			}
			}
			columnNameQuery = sb.toString();	
			return true;
		}	
		else{
			return false;
		}
	}
	public static boolean createSelectOp(String dbname, String table_name, PrintWriter out, JSONObject dataObj){
		try{
			DBDao dbManager = DBDao.getInstance();
			Connection con = dbManager.createConnection();
			if(dataObj.has("where")){
				JSONObject whereId = dataObj.getJSONObject("where");
				sql = "select "+columnNameQuery+" from "+dbname+"."+table_name+" where "+whereId.getString("primary_key")+"=?;";
				ps = con.prepareStatement(sql);
				ps.setObject(1, whereId.getString("key_value"));	
			}
			else{
				sql = "select "+columnNameQuery+" from "+dbname+"."+table_name+";";
				ps = con.prepareStatement(sql);	
			}
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnNo = rsmd.getColumnCount();
				responseData = new JSONArray();	
				do{
					JSONObject json = new JSONObject();
					for(int i = 1; i <= columnNo ; i++)
					{
						json.put(rsmd.getColumnName(i),rs.getObject(i));
						if(i == columnNo){
							responseData.put(json);
						}	
					}
				}while(rs.next());
					out.println(responseData.toString());
				return true;
			}	
			else{
				out.println("null");
				return true;
			}
		}catch(Exception e)
		{
			return false;
		}
	}
}
