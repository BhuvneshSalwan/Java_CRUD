package com.parse.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.JSONObject;

public class DeleteOp {
	public static boolean createDeleteOp(String dbname, String table_name, PrintWriter out, JSONObject data){
		JSONObject dataObj = data;
		JSONObject whereId = dataObj.getJSONObject("where");
		try{
			DBDao dbManager = DBDao.getInstance();
			Connection con = dbManager.createConnection();
			String sql = "delete from "+dbname+"."+table_name+" where "+whereId.getString("primary_key")+"=?;";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setObject(1, whereId.getString("key_value"));
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