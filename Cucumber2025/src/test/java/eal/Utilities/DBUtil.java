package eal.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

// import oracle.jdbc.OracleConnection;
//import oracle.jdbc.driver.OracleDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBUtil {
	
	public static final Logger logger = LogManager.getLogger(DBUtil.class);
	

	private static Connection connection;

	private static Statement statement;

	private static ResultSet resultSet;

	
	//**************************************This is for ORcale Connection: ***********************************
	
	public static void establishConnection() throws SQLException {
		logger.info("login entered in URL Oracle 1");
		
		 logger.info("Reading DB Url from config file:  "+ConfigurationReader.getProperty("urlOracle"));

		try {
			
			
			
		connection = DriverManager.getConnection(ConfigurationReader.getProperty("urlOracle"),
                 
				
				ConfigurationReader.getProperty("oracleUserId"),

				ConfigurationReader.getProperty("oraclePassword"));
		
           logger.info("Connected to DB!!! "+ConfigurationReader.getProperty("urlOracle"));
	}catch(Exception e){
		 e.printStackTrace();
	}
	}
	
	// ****** PDL DB from AWS Secret: ********
	
	
	public static void establishSQLServerConnectionFromSecret() throws SQLException {
		Map<String, String> secret = SecretManager.getDBSecretDetails();
		
		String host = secret.get("host");
		String port = secret.get("port");
		String username = secret.get("username");
		String password = secret.get("password");


		String jdbcTemplate = ConfigurationReader.getProperty("jdbc.url.template");
		String jdbcUrl = String.format(jdbcTemplate, host, port);

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			logger.info(LogColor.Magenta + "Connected to SQL Server at: " + host + LogColor.RESET);

		} catch (Exception e) {
			logger.error("Failed to connect to SQL Server", e);
			throw new SQLException(e);
		}
	}
	
	
	//*********** PDL DB Direct ***********
	
	public static void establishSQLServerConnectionDirectly() throws SQLException {
		String host = ConfigurationReader.getProperty("testdbHost");
		String port = ConfigurationReader.getProperty("testdbPort");
		String username = ConfigurationReader.getProperty("testdbUserName");
		String password = ConfigurationReader.getProperty("testdbPassword");

		String jdbcTemplate = ConfigurationReader.getProperty("jdbc.url.template");
		String jdbcUrl = String.format(jdbcTemplate, host, port);

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			logger.info(LogColor.Magenta + "Connected to SQL Server at: " + host + LogColor.RESET);

		} catch (Exception e) {
			logger.error("Failed to connect to SQL Server", e);
			throw new SQLException(e);
		}
	}
	
	
	//OLD	
	
	public static void establishConnectionTLS() throws SQLException {
		//erbtctst.magellanaws.com
		//HOST=orclrbtctst.mbh.mhs.magellanhealth.com
	String oradb = "Data Source=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCPS)(HOST=erbtctst.magellanaws.com)(PORT=1521)))"
			+ "(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=rbtctst.magellanhealth.com))\n" + 
			")";
	
	
//	String oradb= "jdbc:oracle:thin:@(DESCRIPTION= (ADDRESS=\n" + 
//			"(PROTOCOL=TCPS)(PORT=1521)(HOST=orclrbtcdev.mbh.mhs.magellanhealth.com))\n" + 
//			"(CONNECT_DATA=(SERVICE_NAME=rbtcdev.magellanhealth.com))\n" + 
//			"(SECURITY=(my_wallet_directory=C:\\WalletCertificates)))";
//		
	//Connection conn = new Connection(oradb);
		
		logger.info("login entered in URL Oracle 1");

		connection = DriverManager.getConnection(oradb,

				ConfigurationReader.getProperty("oracleUserId"),

				ConfigurationReader.getProperty("oraclePassword"));
		 

	}
	
	
	public static void establishConnection_Production() throws SQLException {
		connection = DriverManager.getConnection(ConfigurationReader.getProperty("urlProdOracle"),
			 

				ConfigurationReader.getProperty("oracleUserId_Prod"),

				ConfigurationReader.getProperty("oraclePassword_Prod"));
		
		logger.info("Connected to DB!!! "+ConfigurationReader.getProperty("urlProdOracle"));

	}
//for regression testing 	
	public static void establishConnection_DEV1() throws SQLException {
	 
		connection = DriverManager.getConnection(ConfigurationReader.getProperty("urlDEV1Oracle"),
				

				ConfigurationReader.getProperty("oracleUserId_DEV1"),

				ConfigurationReader.getProperty("oraclePassword_DEV1"));
		
		logger.info("Connected to Dev1 DB!!! "+ConfigurationReader.getProperty("urlDEV1Oracle"));

	}
	
	public static void establishConnection_RBTCTST_STANDBY() throws SQLException {
		 
		connection = DriverManager.getConnection(ConfigurationReader.getProperty("url_TST_STANDBY_PRIME_Oracle"),
				

				ConfigurationReader.getProperty("oracleUserId"),

				ConfigurationReader.getProperty("oraclePassword"));
		
		logger.info("Connected to RBTCTST_STANDBY DB!!! "+ConfigurationReader.getProperty("url_TST_STANDBY_PRIME_Oracle"));

	}
	
	public static void establishConnection_StandBY() throws SQLException {
		connection = DriverManager.getConnection(ConfigurationReader.getProperty("urlStandBY"),
			 

				ConfigurationReader.getProperty("oracleUserId_StandBY"),

				ConfigurationReader.getProperty("oraclePassword_StandBY"));
		logger.info("Connected to DB!!! "+ConfigurationReader.getProperty("urlStandBY"));
	}

	public static void closeConnection() {

		try {

			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connection != null) {
				connection.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param query
	 * @return returns a single cell value. If the results in multiple rows and/or
	 *         columns of data, only first column of the first row will be returned.
	 *         The rest of the data will be ignored
	 */

	public static Object getCellValue(String query) {
		logger.info(
				"from Get first column and row from method getCellValue" + getQueryResultList(query).get(0).get(0));
		return getQueryResultList(query).get(0).get(0);
	}

	/**
	 * 
	 * @param query
	 * @return returns a list of Strings which represent a row of data. If the query
	 *         results in multiple rows and/or columns of data, only first row will
	 *         be returned. The rest of the data will be ignored
	 */

	public static List<Object> getRowList(String query) {

		return getQueryResultList(query).get(0);
	}

	/**
	 * 
	 * @param query
	 * @return returns a map which represent a row of data where key is the column
	 *         name. If the query results in multiple rows and/or columns of data,
	 *         only first row will be returned. The rest of the data will be ignored
	 */

	public static Map<String, Object> getRowMap(String query) {

		return getQueryResultMap(query).get(0);

	}

	/**
	 * 
	 * @param query
	 * @return returns query result in a list of lists where outer list represents
	 *         collection of rows and inner lists represent a single row
	 */

	public static List<List<Object>> getQueryResultList(String query) {

		executeQuery(query);

		List<List<Object>> rowList = new ArrayList<>();

		ResultSetMetaData rsmd;

		try {

			rsmd = resultSet.getMetaData();
			while (resultSet.next()) {

				List<Object> row = new ArrayList<>();

				for (int i = 1; i <= rsmd.getColumnCount(); i++) {

					row.add(resultSet.getObject(i));

				}

				rowList.add(row);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return rowList;

	}

	/**
	 * 
	 * @param query
	 * @param column
	 * @return list of values of a single column from the result set
	 */

	public static List<Object> getColumnData(String query, String column) {

		executeQuery(query);

		List<Object> rowList = new ArrayList<>();

		ResultSetMetaData rsmd;

		try {

			rsmd = resultSet.getMetaData();

			while (resultSet.next()) {

				rowList.add(resultSet.getObject(column));
			}

		} catch (SQLException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return rowList;
	}

	/**
	 * 
	 * @param query
	 * @return returns query result in a list of maps where the list represents
	 *         collection of rows and a map represents represent a single row with
	 *         key being the column name
	 */

	public static List<Map<String, Object>> getQueryResultMap(String query) {

		executeQuery(query);

		List<Map<String, Object>> rowList = new ArrayList<>();

		ResultSetMetaData rsmd;

		try {

			rsmd = resultSet.getMetaData();

			while (resultSet.next()) {

				Map<String, Object> colNameValueMap = new HashMap<>();

				for (int i = 1; i <= rsmd.getColumnCount(); i++) {

					colNameValueMap.put(rsmd.getColumnName(i), resultSet.getObject(i));
				}

				rowList.add(colNameValueMap);

			}

		} catch (SQLException e) {

			// TODO Auto-generated catch block

		//	e.printStackTrace();
		}

		return rowList;
	}
	
	
	
	
	

	/**
	 * 
	 * @param query
	 * @return List of columns returned in result set
	 */
	
	public static void executeUpdateQuery(String Query) {
		executeQuery(Query);
		executeQuery("Commit");
		
	}
	
	public static List<Map<String, Object>> executeQueryAndGetResults(String Query) {
		 
		List<Map<String, Object>> results=	getQueryResultMap(Query);
		logger.info("Results from DBUtil: "+results);
		
		return results;
	}
	

	public static String executeQueryGetColumnValueFromResults(String Query,String ColumnName) {
		String columnValue=null;
		
		List<Map<String, Object>> results=	getQueryResultMap(Query);
		logger.info("Results from DBUtil: "+results);
		for (Map<String, Object> row : results) {

			columnValue = "" + row.get(ColumnName);
		}
		logger.info("Column Value from DBUtil: "+columnValue);
		 
		return columnValue;
	}
	
	

	public static List<String> getColumnNames(String query) {

		executeQuery(query);

		List<String> columns = new ArrayList<>();

		ResultSetMetaData rsmd;

		try {

			rsmd = resultSet.getMetaData();

			int columnCount = rsmd.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {

				columns.add(rsmd.getColumnName(i));

			}

		} catch (SQLException e) {

			// TODO Auto-generated catch block

			
		}

		return columns;

	}

	private static void executeQuery(String query) {

		try {

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		} catch (SQLException e) {

			// TODO Auto-generated catch block
        // logger.info("Testing");
			
		}

		try {

			resultSet = statement.executeQuery(query);

		} catch (SQLException e) {

			// TODO Auto-generated catch block

			//  logger.info("Testing 2");

		}

	}

	public static int getRowCount() throws Exception {

		resultSet.last();
		int rowCount = resultSet.getRow();

		return rowCount;

	}
	public static void writeHeaderLine(XSSFSheet sheet, String query) {
		Cell headerCell;
        Row headerRow = sheet.createRow(0);
        List<Map<String, Object>> listOfMap = DBUtil.getQueryResultMap(query);
        List<String> columnTitle=DBUtil.getColumnNames(query);
        int sizeOfColumn=columnTitle.size();
        logger.info("Size of the column   "+sizeOfColumn);
        int cNumber=0;
        for(String eachHeader: columnTitle ) {
        	logger.info(eachHeader);
        	headerCell=headerRow.createCell(cNumber);
        	cNumber++;
        }
        
//        Cell headerCell = headerRow.createCell(0);
//        headerCell.setCellValue("Course Name");
// 
//        headerCell = headerRow.createCell(1);
//        headerCell.setCellValue("Student Name");
// 
//        headerCell = headerRow.createCell(2);
//        headerCell.setCellValue("Timestamp");
// 
//        headerCell = headerRow.createCell(3);
//        headerCell.setCellValue("Rating");
// 
//        headerCell = headerRow.createCell(4);
//        headerCell.setCellValue("Comment");
    }
	public static String countValidation(String query) {
		String countStr=null;
		String q1="Select Count(*) COUNT "+query;
		
		logger.info("q1 from DBUtil"+q1);
		List<Map<String, Object>> resultsFromq1 = DBUtil.getQueryResultMap(q1);
		

		for (Map<String, Object> row : resultsFromq1) {
			
			logger.info("row from DBUitl"+row);
			countStr =  String.valueOf(row.get("COUNT"));  
			logger.info("Count from DBUtil: "+countStr);
		}
		
		
 return countStr;
	
	}
	public static void initializeConnection() throws ClassNotFoundException {
		String host = ConfigurationReader.getProperty("testdbHost");
		String port = ConfigurationReader.getProperty("testdbPort");
		String username = ConfigurationReader.getProperty("testdbUserName");
		String password = ConfigurationReader.getProperty("testdbPassword");

		String jdbcTemplate = ConfigurationReader.getProperty("jdbc.url.template");
		String jdbcUrl = String.format(jdbcTemplate, host, port);
	    try {
	        if (connection == null || connection.isClosed()) {

				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				connection = DriverManager.getConnection(jdbcUrl, username, password);
				logger.info(LogColor.Magenta + "Connected to SQL Server at: " + host + LogColor.RESET);

	        }
	    } catch (SQLException e) {
	        logger.error("Failed to initialize database connection", e);
	        throw new RuntimeException("Database connection failed");
	    }
	}
	
	public static int executeQueryUID(String query) {
		try {
			initializeConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    int affectedRows = 0;

	    try (Statement statement = connection.createStatement()) {
	        // Execute the update query (INSERT, UPDATE, DELETE)
	        affectedRows = statement.executeUpdate(query);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return affectedRows;
	}

}