package com.parse.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.parse.dao.DeleteOp;
import com.parse.dao.InsertOp;
import com.parse.dao.SelectOp;
import com.parse.dao.UpdateOp;
import com.parse.views.DataSourceVerify;

public class DataSeeker extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    protected void goGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    	response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		StringBuilder buffer = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	    }  
	    String query = buffer.toString();
		JSONObject jsonData = new JSONObject(query);
		String type = jsonData.getString("type");
		String db_name = jsonData.getString("database_name");
		String table_name = jsonData.getString("table_name");
		JSONObject dataObj = jsonData.getJSONObject("data");
		if(type.equalsIgnoreCase("insert")){
			if(DataSourceVerify.DataSource(db_name, table_name, out)){
				if(InsertOp.setInsertOp(dataObj, out)){
					if(InsertOp.createInsertOp(db_name, table_name, out)){
						response.setStatus(HttpServletResponse.SC_CREATED);	
					}		
					else{
						response.sendError(403, "There is no column with such name in the database.");
					}
				}
				else{
					response.sendError(402, "The Column Name and Column Values doesn't match to each other.");
				}
			}
			else{
				response.sendError(401, "Database or Table doesn't Exist with such name.");		
			}
		}
		if(type.equalsIgnoreCase("update")){
			if(DataSourceVerify.DataSource(db_name, table_name, out)){
				if(UpdateOp.setUpdateOp(dataObj, out)){
					if(UpdateOp.createUpdateOp(db_name, table_name, out)){
						response.setStatus(HttpServletResponse.SC_CREATED);	
					}
					else{
						response.sendError(403, "There is no column with such name in the database.");
					}	
				}
				else{
				response.sendError(402, "The Column Name and Column Values doesn't match to each other.");	
				}
			}
			else{
				response.sendError(401, "Database or Table doesn't Exist with such name.");		
			}			
		}
		if(type.equalsIgnoreCase("select")){
			if(DataSourceVerify.DataSource(db_name, table_name, out)){
				if(SelectOp.setSelectOp(dataObj, out)){
					if(SelectOp.createSelectOp(db_name, table_name, out, dataObj)){
						response.setStatus(HttpServletResponse.SC_CREATED);						
					}
					else{
						response.sendError(403, "There is no column with such name in the database.");
					}	
				}
				else{
				response.sendError(402, "The Column Name doesn't have a appropriate value.");	
				}
			}
			else{
				response.sendError(401, "Database or Table doesn't Exist with such name.");		
			}
		}
		if(type.equalsIgnoreCase("delete")){
			if(DataSourceVerify.DataSource(db_name, table_name, out)){
					if(DeleteOp.createDeleteOp(db_name, table_name, out, dataObj)){
						response.setStatus(HttpServletResponse.SC_CREATED);	
					}
					else{
						response.sendError(403, "There is no column with such name in the database.");
					}	
				}
			else{
				response.sendError(401, "Database or Table doesn't Exist with such name.");
			}				
		}
	}
}