/**
 * This is a Bean Class containing all Constants used in this project
 * @author Raghu Keerthi Petla, Aditi Vishwas Rao, Mehul Parmar, Anirudh Palakurthi 
 */
package com.uic.ids520.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

public class Constants {
	public static final String MYSQLDRIVER = "com.mysql.jdbc.Driver";
	public static final String DB2DRIVER = "com.ibm.db2.jcc.DB2Driver";
	public static final String ORACLEDRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String SQLDRIVER = "com.jdbc.driver.sqldriver";
	public static final String MYSQLPORT = "3306";
	public static final String DB2PORT = "50000";
	public static final String ORACLEPORT = "1521";
	public static final String SQLPORT = "1433";
	public static final String DEFAULT_USER = "root";
	public static final String DEFAULT_PASSWORD = "root";
	public static final String groupby = "GROUP BY";
	public static final String orderby = "ORDER BY";
	public static final String desc = "DESC";
	public static final String DEFAULT_SCHEMA="ids520";
	public static Map<UserBean, HttpSession> logins = new ConcurrentHashMap<>();
	public static final Map<Integer,String>errorMap;
	static{
		errorMap = new HashMap<>();
		errorMap.put(100,"DBMS has not been set up for the host");
		errorMap.put(110, "Invalid Database Credentials");
		errorMap.put(120, "Credentials cannot be empty or empty spaces");
		errorMap.put(130,"");
	}
}
