package common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import static constant.CT_Common.*;
import static common.TestLogger.*;

public class JDBCConnectManagement extends CommonBase {
	private String db;
	private String sid;
	private String userName;
	private String passWord;
	private String pt;
	private String port;
	private String typeDb;


	private Connection conn;

	public ResultSet executeSql( String sql) {
		try {
			if (conn == null) {
				connect();
			}
			Statement state = conn.createStatement();
			info("Thuc thi cau lenh " + sql);
			ResultSet rs = state.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			info("Khong thuc thi duoc cau lenh: " + sql);
			Assert.assertFalse(false);
		}
		return null;
	}

	/**
	 * 
	 * @param sql
	 */
	public void executeManySQL(String db, String... sql){
		if(sql.length > 0){
			try {
				if (conn == null) {
					connect();
				}
				Statement state = conn.createStatement();
				for(int i = 0; i< sql.length; i++){
					state.executeQuery(sql[i]);
				}
			} catch (Exception e) {
				info("Khong thuc hien duoc cau lenh sql " + sql);
			}

		}
	}
	/**
	 * Ket noi den CSDL
	 */
	public void connect(String... database) {
		if (database.length < 4) {
			db = DB;
			sid = SID;
			port = PORT;
			userName = USERNAME;
			passWord = PASSWORD;
		} else {
			typeDb = database[0];
			db = database[1];
			sid = database[2];
			userName = database[3];
			passWord = database[4];
		}	
		if(typeDb.toUpperCase() == "ORACLE"){
			try {
				info("Connect to DB " + db);
				Class.forName( "oracle.jdbc.driver.OracleDriver");
				String dbConnectString = "jdbc:oracle:thin:@//" + db + ":"+port+"/"
						+ sid;
				conn = DriverManager.getConnection(dbConnectString, userName,
						passWord);
			} catch (Exception e) {
				info("Can not connect to DB: " + database);
			}
		}
		if(typeDb.toUpperCase() == "SQL"){
			try {
				info("Connect to DB " + db);
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String dbConnectString = "jdbc:sqlserver://localhost//sqlexpress";
				conn = DriverManager.getConnection(dbConnectString, userName,
						passWord);
			} catch (Exception e) {
				info("Can not connect to DB: " + database);
			}
		}

	}
	/**
	 * connect mariaDB
	 * @param database
	 */
	public void connectMariaDB(String... database){
		if (database.length >= 4) {
			db = System.getProperty("DBHost") != null ? System.getProperty("DBHost"):database[0];
			pt = System.getProperty("port") != null ? System.getProperty("port"):database[1];
			sid = System.getProperty("DBSid") != null ? System.getProperty("DBSid"):database[2];
			userName = System.getProperty("DBUser") != null ? System.getProperty("DBUser"):database[3];
			passWord = System.getProperty("DBPass") != null ? System.getProperty("DBPass"):database[4];
			
			try{
				info("Connect to DB " + db);
				Class.forName("org.mariadb.jdbc.Driver");
				String dbConnectString = "jdbc:mariadb://" + db + ":" + pt +"/"
						+ sid;
				if (database.length == 5){
					conn = DriverManager.getConnection(dbConnectString, userName,
							passWord);
				}else {
					if (database[5].equalsIgnoreCase("1")){
						conn = DriverManager.getConnection(dbConnectString, userName,
								passWord);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				info("Can not connect to DB: " + database[0]);
			}
		}	
	}

	/**
	 * Dong connection
	 */
	public void close() {
		try {
			if (conn != null) {
				conn.close();
				info("Close connect to DB");
			}
		} catch (Exception e) {
			info("Can not close connection to DB");
		}
	}

	/**
	 * Dem so luong ban ghi co ton tai theo dieu kien cau lenh truy van
	 * @param sql
	 * @return
	 */
	public String checkRecordExist(String sql) {
		try {
			ResultSet rs = executeSql(sql);
			while (rs.next()) {
				return rs.getString("count(*)");
			}
		} catch (Exception e) {
			info("Khong thuc thi duoc cau lenh " + sql);
		}
		return "";
	}

	/**
	 * So sanh du lieu tu cau lenh SQL tra ve voi mang du lieu
	 * @param sql
	 * @param params
	 * @param expects
	 * @return
	 */
	public boolean compareResult(String sql, String[] params, String[][] expects) {
		String[][] data = getData(sql, params);
		if (data != null){
			if (expects.length > 0 && data.length > 0) {
				for (int i = 0; i < expects.length; i++) {
					if (expects[i] != null && data[i] != null) {
						for (int j = 0; j < expects[i].length; j++) {
							String s = StringUtils.trim(data[i][j]);
							info("Data get from DB: " + s);
							String d = StringUtils.trim(expects[i][j]);
							info("Data expect: " + d);
							if (d != null){
								if (s != null && d != ""){
								
									if (!d.equalsIgnoreCase(s)) {
										info("data not map " + s + " and " + d);
										return false;
									}
								} else if (d.equals("")){
									if (s != null){
										return false;
									}
								}															
							}
						}
					}
				}
			} else {
				info("size of expects: " + expects.length);
				info("size of data: " + data.length);
				return false;
			}
		} else {
			info("Data get from DB is null");
			return false;
		}
		return true;
	}

	/**
	 * lay du lieu tra ve tu cau lenh truy van, luu vao List 2 chieu
	 * @param sql
	 * @param params
	 * @return
	 */
	public String[][] getData(String sql, String... params) {
		String[][] data = new String[100][100];
		try {
			info("Thuc thi cau lenh " + sql);
			ResultSet rs = executeSql(sql);
			int i = 0;
			if (rs != null){
				while (rs.next()) {
					String[] currData = new String[params.length];
					for (int j = 0; j < params.length; j++) {
						info("Get collumn name " + params[j]);
						currData[j] = rs.getString(params[j]);
					}
					data[i] = currData;
					i++;
				}
			}
		} catch (Exception e) {
			info("Exception: " + e);
			e.printStackTrace();
			return data;
		}
		return data;
	}
	
	public void executeSetData(CallableStatement cal, String... param) {
		try {
			if (param.length > 0){
				for (int i = 0; i < param.length; i ++){
					info("param:" + param[i]);
					if (param[i] != null){
						String[] pa1 = param[i].split("!");
						String[] pa2 = param[i].split("/");
						String[] pa = pa1.length > 1 ? pa1:pa2;
						switch (pa[0]) {
						case "Long":
							if (pa[1].equalsIgnoreCase("NULL")){
								cal.setNull(i+1, Types.INTEGER);
							} else {
								cal.setLong(i+1, Long.parseLong(pa[1]));
							}
							break;
						case "String":
							if (pa[1].equalsIgnoreCase("NULL")){
								cal.setNull(i+1, Types.VARCHAR);
							}else {
								cal.setString(i+1, pa[1]);
							}
							break;
						case "Date":
//							cal.setDate(i+1, formatDate("dd/MM/yyyy HH:mm:ss", pa[1]));
							break;
						case "Boolean":
							cal.setBoolean(i+1, Boolean.parseBoolean(pa[1]));
							break;
						default:
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			info("Exception: " + e.getMessage());
		}
		
	}
	/**
	 * 
	 * @param command
	 * @param param
	 */
	public void executeProcedure(String procedure, String db, String... param){
		CallableStatement cal = null;
		String mariaDb = System.getProperty("MariaDb");
		String command = "";
		if (mariaDb == null || mariaDb == "0"){
			command = "begin " + procedure + "; end;";
		}else{			
			command = "{ call " + procedure.substring(procedure.indexOf(".")+1) + " }";
		}
		try {
			if (conn == null) {
				connect();
			}
			cal = conn.prepareCall(command);
			executeSetData(cal, param);
			cal.execute();
		} catch (Exception e) {
			info("Can not execute procedure");
			info("Exception: " + e.getMessage());
		}
	}
	
	
	
	/**
	 * thuc hien thu tuc lay ra 1 output theo chi so outPut
	 * @param procedule
	 * @param outPut
	 * @param param
	 * @return
	 */
	public String executeProcedureHasOutPut(String procedule, Integer outPut, String db, String... param){
		CallableStatement cal = null;
		String command = "begin " + procedule + "; end;";
		try {
			if (conn == null) {
				connect();
			}
			cal = conn.prepareCall(command);
			executeSetData(cal, param);
			cal.registerOutParameter(outPut, Types.VARCHAR);
			cal.execute();
			return cal.getString(outPut);
		} catch (Exception e) {
			info("Can not execute procedure");
			info("Exception: " + e.getMessage());
			return null;
		}
	}
	
	public String executeFunction(String function, Integer outPut,String db,  String... param){
		CallableStatement cal = null;
		String command = "begin " + function + "; end;";
		try {
			if (conn == null) {
				connect();
			}
			cal = conn.prepareCall(command);
			cal.registerOutParameter(1, Types.VARCHAR);
			if (param.length > 0){
				for (int i = 0; i < param.length; i ++){
					info("param:" + param[i]);
					if (param[i] != null){
						String[] pa = param[i].split("/");
						switch (pa[0]) {
						case "Long":
							if (pa[1].equalsIgnoreCase("NULL")){
								cal.setNull(i+2, Types.INTEGER);
							} else {
								cal.setLong(i+2, Long.parseLong(pa[1]));
							}
							break;
						case "String":
							if (pa[1].equalsIgnoreCase("NULL")){
								cal.setNull(i+2, Types.VARCHAR);
							}else {
								cal.setString(i+2, pa[1]);
							}
							break;
						case "Date":
//							cal.setDate(i+2, formatDate("dd/MM/yyyy HH:mm:ss", pa[1]));
							break;
						case "Boolean":
							cal.setBoolean(i+2, Boolean.parseBoolean(pa[1]));
							break;
						default:
							break;
						}
					}
				}
			}
			if (outPut>0){
				cal.registerOutParameter(outPut, Types.VARCHAR);
			}
			cal.execute();
			return cal.getString(1);
		} catch (Exception e) {
			info("Can not execute function");
			info("Exception: " + e.getMessage());
			return null;
		}
	}		
}
