/**
 * This is a Bean Class containing all the methods and attributes required for any
 * kind of database transaction that has to be made.
 * @author Raghu Keerthi Petla, Aditi Vishwas Rao, Mehul Parmar, Anirudh Palakurthi 
 */
package com.uic.ids520.bean;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.uic.ids520.bean.Constants;

public class DatabaseAccessBean {
	private String url;
	private String dbms;
	private String db_schema;
	private String db_port;
	private String db_username;
	private String db_password;
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;
	private Statement statement;
	private ErrorMessageBean error;
	private ResultSetMetaData resultSetMetaData;
	private DatabaseMetaData databaseMetaData;
	private int changedRows;
	private int rowCount;
	
	public DatabaseAccessBean(ErrorMessageBean error){
		this.db_port = "3306";
		this.db_password = Constants.DEFAULT_PASSWORD;
		this.db_username = Constants.DEFAULT_USER;
		this.dbms = "MYSQL";
		this.db_schema=Constants.DEFAULT_SCHEMA;
		this.error = error;
		this.url = "localhost";
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDbms() {
		return dbms;
	}
	public void setDbms(String dbms) {
		this.dbms = dbms;
	}
	public String getDb_schema() {
		return db_schema;
	}
	public void setDb_schema(String db_schema) {
		this.db_schema = db_schema;
	}
	public String getDb_port() {
		return db_port;
	}
	public void setDb_port(String db_port) {
		this.db_port = db_port;
	}
	public String getDb_username() {
		return db_username;
	}
	public void setDb_username(String db_username) {
		this.db_username = db_username;
	}
	public String getDb_password() {
		return db_password;
	}
	public void setDb_password(String db_password) {
		this.db_password = db_password;
	}
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public ResultSet getResultSet() {
		return resultSet;
	}
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}
	public void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}
	public Statement getStatement() {
		return statement;
	}
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	public ErrorMessageBean getError() {
		return error;
	}
	public void setError(ErrorMessageBean error) {
		this.error = error;
	}
	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}
	public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		this.resultSetMetaData = resultSetMetaData;
	}
	public DatabaseMetaData getDatabaseMetaData() {
		return databaseMetaData;
	}
	public void setDatabaseMetaData(DatabaseMetaData databaseMetaData) {
		this.databaseMetaData = databaseMetaData;
	}
	public int getChangedRows() {
		return changedRows;
	}
	public void setChangedRows(int changedRows) {
		this.changedRows = changedRows;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	
	
	public String connect(){
		String host="";
		String status = "SUCCESS";
		try{
			switch (dbms.toLowerCase()) {
			case "mysql":
				Class.forName("com.mysql.jdbc.Driver");
				if(db_port==null || db_port.equals("")){
					db_port = Constants.MYSQLPORT;
				}
				host = "jdbc:"+dbms.toLowerCase()+"://"+url+":"+db_port+"/"+db_schema;
				break;
			case "db2":
				Class.forName(Constants.DB2DRIVER);
				if(db_port==null || db_port.trim().equals("")){
					db_port =Constants.DB2PORT;
				}
				host = "jdbc:"+getDbms()+"://"+url+":"+db_port+"/"+db_schema;
				break;
			case "oracle":
				Class.forName(Constants.ORACLEDRIVER);
				if(db_port==null || db_port.trim().equals("")){
					db_port =Constants.ORACLEPORT;
				}
				host = "jdbc:"+getDbms()+":thin:@"+url+":"+db_port+db_schema;
				break;
			case "sql":
				Class.forName(Constants.SQLDRIVER);
				if(db_port==null || db_port.trim().equals("")){
					db_port =Constants.SQLPORT;
				}
				host = "jdbc:"+getDbms()+":thin:@"+url+":"+db_port+db_schema;
				break;
			default:
				host = null;
			}
		}catch(ClassNotFoundException e){
			status = "FAIL";
			e.printStackTrace();
			setErrorMessage(100,"",Constants.errorMap.get(100));
		}catch(NullPointerException ex){
			status = "FAIL";
			//this.errorMessage = ex.getMessage();
		}
		if(status.equals("FAIL")){
			//this.error.setErrorMessage("Selected DBMS has not been set up for the selected host");
			return status;
		}
		try{
			if(host!=null && host!=""){
				this.connection = DriverManager.getConnection(host, db_username.trim(), db_password.trim());
				if(this.connection!=null){
					this.statement = this.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					this.databaseMetaData = (DatabaseMetaData) this.connection.getMetaData();
					
					if(this.databaseMetaData!=null){
						//getTablesFromDatabase();
						this.resultSet = this.databaseMetaData.getCatalogs();
					}
				}
			}
		}catch(SQLException e){
			status = "FAIL";
			setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			status = "FAIL";
			this.error.setErrorMessage(e.getMessage());
			setErrorMessage(-1,"",e.getMessage());
			e.printStackTrace();
		}
		if(status.equals("FAIL")){
			setErrorMessage(110,"",Constants.errorMap.get(110));
		}
		return status;
	}
	
	public String processQuery(String query){
		String status = "FAIL";
		try{
			if(this.connection==null){
				connect();
			}
		}catch(NullPointerException e){
			this.error.setErrorMessage(e.getMessage());
		}
		String queryType = query.split(" ")[0];
		
		switch (queryType.toLowerCase()) {
		case "select":
			try {
				this.resultSet = this.statement.executeQuery(query);
				if(this.resultSet!=null){
					this.resultSetMetaData = this.resultSet.getMetaData();
					this.resultSet.last();
					rowCount = this.resultSet.getRow();
					this.resultSet.beforeFirst();
					//getColumnNames();
					status = "SUCCESS";
				}
			} catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			break;
		case "insert":
			try{
				setChangedRows(this.statement.executeUpdate(query));
				status = "SUCCESS";
			}catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			break;
		case "update":
			try{
				setChangedRows(this.statement.executeUpdate(query));
				status = "SUCCESS";
			}catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			break;
		case "delete":
			try{
				setChangedRows(this.statement.executeUpdate(query));
				status = "SUCCESS";
			}catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			break;
		case "create":
			try{
				this.statement.executeUpdate(query);
				status = "SUCCESS";
			}catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			break;
		case "drop":
			try{
				this.statement.executeUpdate(query);
				status = "SUCCESS";
			}catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			break;
		default:
			try{
				this.statement.execute(query);
			}catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			break;
		}
		return status;
	}
	
	//processQuery using Prepared Statement
	public String processQueryPreparedStatement(String query,List<String> queryValues){
		String status = "FAIL";
		
		try{
			if(this.connection==null){
				connect();
			}
		}catch(NullPointerException e){
			setErrorMessage(-1, "", e.getMessage());
		}
		String queryType = query.split(" ")[0];
		
		switch (queryType.toLowerCase()) {
		case "select":
			try{
				if(this.preparedStatement!=null){
					if(queryValues!=null && queryValues.size()>0){
						for(int i=0;i<queryValues.size();i++){
							this.preparedStatement.setString(i+1,queryValues.get(i));
						}
					}
					this.resultSet = this.preparedStatement.executeQuery();
					if(this.resultSet!=null){
						this.resultSetMetaData = this.resultSet.getMetaData();
						
						this.resultSet.last();
						rowCount = this.resultSet.getRow();
						
						this.resultSet.beforeFirst();
						status = "SUCCESS";
					}
				}
			}catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			
			break;
		case "insert":
			try{
				if(this.preparedStatement!=null){
					if(queryValues!=null && queryValues.size()>0){
						for(int i=0;i<queryValues.size();i++){
							this.preparedStatement.setString(i+1,queryValues.get(i));
						}
					}
					setChangedRows(this.preparedStatement.executeUpdate());
					status = "SUCCESS";
				}
			}catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			break;
		case "update":
			try{
				if(this.preparedStatement!=null){
					if(queryValues!=null && queryValues.size()>0){
						for(int i=0;i<queryValues.size();i++){
							this.preparedStatement.setString(i+1,queryValues.get(i));
						}
					}
					setChangedRows(this.preparedStatement.executeUpdate());
					status = "SUCCESS";
				}
			}catch (SQLException e) {
				setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
				e.printStackTrace();
			}
			break;
		case "delete":
			break;
		case "create":
			break;
		case "drop":
			break;
		default:
			break;
		}
		return status;
	}
	/* create a query for prepared statement
	 * params: queryType(select,insert,update),table(table name),columns(list of columns),constraints(conditions following where key),conditions(key,value like groupby,colname)
	 */
	public String createPrepareStatement(String queryType,String table,List<String> columns,List<String>constraints,Map<String,String>conditionMap){
		if(queryType!=null && !queryType.trim().equals("")){
			queryType = queryType.trim().toLowerCase();
			String query = "";
			switch (queryType) {
			case "select":
				query = "SELECT "+getQueryColumns(columns,false)+" FROM "+table+" WHERE "+generateQueryConstraints(constraints)+" "+generateQueryCondition(conditionMap);
				break;
			case "insert":
				query = "INSERT INTO "+table+"("+getQueryColumns(columns,false)+") "+"VALUES("+getQueryColumns(columns,true)+")";
				break;
			case "update":
				query = "UPDATE "+table+" SET "+getQueryPairs(columns)+" WHERE "+generateQueryConstraints(constraints);
				break;
			case "delete":
				break;
			default:
				break;
			}
			
			return query;
		}
		return null;
	}
	private String getQueryPairs(List<String> columns) {
		StringBuffer cols = new StringBuffer();
		if(columns!=null && columns.size()>0){
			for(int i=0;i<columns.size();i++){
				String value = columns.get(i).toUpperCase();
				if(i<columns.size()-1){
					cols.append(value+"=?,");
				}else{
					cols.append(value+"=?");
				}
			}
		}else{
			cols.append("");
		}
		return cols.toString();
	}
	private String generateQueryCondition(Map<String, String> conditionMap) {
		// TODO Auto-generated method stub
		StringBuffer condition = new StringBuffer();
		if(conditionMap!=null && conditionMap.size()>0){
			for(Map.Entry<String, String> entry : conditionMap.entrySet()){
				
				switch (entry.getKey().toLowerCase()) {
				case "groupby":
					condition.append(Constants.groupby+" "+entry.getValue());
					break;
				case "orderby":
					condition.append(Constants.orderby+" "+entry.getValue());
					break;
				case "desc":
					condition.append(" "+Constants.desc);
					break;
				default:
					break;
				}
			}
		}else{
			condition.append("");
		}
		return condition.toString();
	}
	private String generateQueryConstraints(List<String> constraints) {
		// TODO Auto-generated method stub
		StringBuffer constraint = new StringBuffer();
		if(constraints!=null && constraints.size()>0){
			//constraint.append("WHERE ");
			for(int i=0;i<constraints.size();i++){
				if(i<constraints.size()-1){
					constraint.append(constraints.get(i)+"=? AND ");
				}else{
					constraint.append(constraints.get(i)+"=?");
				}
			}
		}else{
			constraint.append("");
		}
		return constraint.toString();
	}
	private String getQueryColumns(List<String> columns,boolean insertQuestionMark) {
		// TODO Auto-generated method stub
		StringBuffer cols = new StringBuffer();
		if(columns!=null && columns.size()>0){
			for(int i=0;i<columns.size();i++){
				String value = columns.get(i).toUpperCase();
				if(insertQuestionMark){
					value = "?";
				}
				if(i<columns.size()-1){
					cols.append(value+",");
				}else{
					cols.append(value);
				}
			}
		}else{
			cols.append("*");
		}
		return cols.toString();
	}
	public String getPrepStatement(String query) {
		String status = "FAIL";
		try{
			if(this.connection==null){
				connect();
			}
		}catch(NullPointerException e){
			//this.errorMessage = e.getMessage();
		}
		try {
			//this.preparedStatement = this.connection.prepareStatement(query);
			this.preparedStatement = this.connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			status = "SUCCESS";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
			e.printStackTrace();
		}
		
		return status;
	}
	
	
	private void setErrorMessage(int errorCode,String sqlState,String errorMessage){
		if(!sqlState.isEmpty()){
			error.setSqlState(sqlState);
		}else{
			error.setSqlState("");
		}
		if(errorCode!=-1){
			error.setErrorCode(errorCode);
		}else{
			error.setErrorCode(-1);
		}
		error.setErrorMessage(errorMessage);
	}
	
	public void closeDBResources(){
		try{
			if(this.resultSet!=null){
				this.resultSet.close();
			}
			if(this.statement!=null){
				this.statement.close();
			}
			if(this.preparedStatement!=null){
				this.preparedStatement.close();
			}
			if(this.connection!=null){
				this.connection.close();
			}
		}catch(SQLException ex){
			setErrorMessage(ex.getErrorCode(),ex.getSQLState(),ex.getMessage());
			ex.printStackTrace();
		}catch (Exception e) {
			setErrorMessage(-1,"",e.getMessage());
			e.printStackTrace();
		}
	}
}
