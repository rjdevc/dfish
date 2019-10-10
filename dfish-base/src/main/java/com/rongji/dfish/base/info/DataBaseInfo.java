package com.rongji.dfish.base.info;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 数据库信息
 * 
 * @author DFish Team
 * @version 1.0
 */
public final class DataBaseInfo {
	private String databaseProductName;

	private String databaseProductVersion;

	private String databaseUrl;

	private String databaseUsername;

	private String driverName;

	private String driverVersion;

	private int databaseType;

	/**
	 * 未知的数据库类型
	 */
	public static final int DATABASE_UNKNOWN = 0;

	/**
	 * ORACLE
	 */
	public static final int DATABASE_ORACLE = 1;

	/**
	 * PostgreSQL
	 */
	public static final int DATABASE_POSTGRESQL = 2;

	/**
	 * IBM DB2
	 */
	public static final int DATABASE_DB2 = 3;

	/**
	 * Microsoft SQLServer
	 */
	public static final int DATABASE_SQLSERVER = 4;

	/**
	 * MySQL
	 */
	public static final int DATABASE_MYSQL = 5;

	/**
	 * HSQL
	 */
	public static final int DATABASE_HSQLDB = 6;

	/**
	 * SAP
	 */
	public static final int DATABASE_SAPDB = 7;

	/**
	 * Firebird
	 */
	public static final int DATABASE_FIREBIRD = 8;

	/**
	 * Sybase
	 */
	public static final int DATABASE_SYBASE = 9;

	/**
	 * 使用一个连接来获取数据库的头信息。
	 * 
	 * @param conn
	 */
	public DataBaseInfo(Connection conn) {
		DatabaseMetaData dbmd = null;
		try {
			dbmd = conn.getMetaData();
			databaseUrl = dbmd.getURL();
			databaseUsername = dbmd.getUserName();
			databaseProductName = dbmd.getDatabaseProductName();
			databaseProductVersion = dbmd.getDatabaseProductVersion();
			driverName = dbmd.getDriverName();
			driverVersion = dbmd.getDriverVersion();
			String databaseName = databaseProductName.toLowerCase();
			if (databaseName == null) {
				databaseType = DATABASE_UNKNOWN;
			} else if (databaseName.indexOf("oracle") != -1) {
				databaseType = DATABASE_ORACLE;
			} else if (databaseName.indexOf("postgresql") != -1) {
				databaseType = DATABASE_POSTGRESQL;
			} else if (databaseName.toLowerCase().indexOf("db2") != -1) {
				databaseType = DATABASE_DB2;
			} else if (databaseName.indexOf("sql server") != -1) {
				databaseType = DATABASE_SQLSERVER;
			} else if (databaseName.indexOf("mysql") != -1) { // "MySQL"
				databaseType = DATABASE_MYSQL;
			} else if (databaseName.indexOf("hsql") != -1) {
				databaseType = DATABASE_HSQLDB;
			} else if (databaseName.indexOf("sap") != -1) { // "SAP DB"
				databaseType = DATABASE_SAPDB;
			} else if (databaseName.indexOf("firebird") != -1) { // "firebird"
				databaseType = DATABASE_FIREBIRD;
			} else if (databaseName.indexOf("adaptive") != -1) { // "DATABASE_SYBASE"
				databaseType = DATABASE_SYBASE;
			} else {
				databaseType = DATABASE_UNKNOWN;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 取得数据库名称
	 * 
	 * @return
	 */
	public String getDatabaseProductName() {
		return databaseProductName;
	}

	/**
	 * 取得数据库版本号
	 * 
	 * @return
	 */
	public String getDatabaseProductVersion() {
		return databaseProductVersion;
	}

	/**
	 * 取得数据库连接字符串
	 * 
	 * @return
	 */
	public String getDatabaseUrl() {
		return databaseUrl;
	}

	/**
	 * 取得数据库连接用户名
	 * @return
	 */
	public String getDatabaseUsername() {
		return databaseUsername;
	}
	/**
	 * 取得驱动名称
	 * @return
	 */
	public String getDriverName() {
		return driverName;
	}
	/**
	 * 取得驱动版本号。
	 * @return
	 */
	public String getDriverVersion() {
		return driverVersion;
	}
	/**
	 * 取得数据库类型
	 * @return
	 * @see #DATABASE_UNKNOWN
	 * @see #DATABASE_ORACLE
	 * @see #DATABASE_POSTGRESQL
	 * @see #DATABASE_DB2
	 * @see #DATABASE_SQLSERVER
	 * @see #DATABASE_MYSQL
	 * @see #DATABASE_HSQLDB
	 * @see #DATABASE_SAPDB
	 * @see #DATABASE_FIREBIRD
	 * @see #DATABASE_SYBASE
	 */
	public int getDatabaseType() {
		return databaseType;
	}
}
