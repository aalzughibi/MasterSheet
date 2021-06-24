package master.sheet.mastersheet;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import master.sheet.mastersheet.Auth.Auth;
import master.sheet.mastersheet.SheetsModel.item;
import master.sheet.mastersheet.SheetsModel.po;
import master.sheet.mastersheet.databaseHelper.databaseHelper;
import master.sheet.mastersheet.excelHelper.excelHelper;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

// import com.mysql.cj.xdevapi.SessionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

@SpringBootApplication
public class MastersheetApplication {

	public static void printTable() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:" + databaseHelper.port + "/" + databaseHelper.database, databaseHelper.username,
			databaseHelper.password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from po");
			ResultSetMetaData rsMetaData = rs.getMetaData();
			System.out.println("----------------------------------------------------------");
			for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
				System.out.println(rsMetaData.getColumnName(i));
			}
			System.out.println("----------------------------------------------------------");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  "
						+ rs.getString(4) + "  " + rs.getString(5) + "  " + rs.getString(6) + "  " + rs.getString(7)
						+ "  " + rs.getString(8) + "  " + rs.getString(9) + "  " + rs.getString(10) + "  "
						+ rs.getString(11) + "  " + rs.getString(12));
		} catch (Exception e) {
			System.out.println("System discover error.");
			System.out.println(e);
		}

	}

	public static boolean checkDatabase() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/?allowPublicKeyRetrieval=true&useSSL=false", databaseHelper.username,
					databaseHelper.password);
			ResultSet resultSet = con.getMetaData().getCatalogs();
			while (resultSet.next()) {
				String databaseName = resultSet.getString(1);
				if (databaseName.equals(databaseHelper.database))
					return true;
			}
			con.close();
			return false;
		} catch (Exception e) {
			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static boolean checkTable(String Table) {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/" + databaseHelper.database + "?allowPublicKeyRetrieval=true&useSSL=false",
					databaseHelper.username, databaseHelper.password);
			DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] { "TABLE" });
			while (resultSet.next()) {
				String name = resultSet.getString("TABLE_NAME");
				if (name.equals(Table)) {
					con.close();
					return true;
				}
			}
			con.close();
			return false;
		} catch (Exception e) {
			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static boolean createUserTable() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/" + databaseHelper.database + "?allowPublicKeyRetrieval=true&useSSL=false",
					databaseHelper.username, databaseHelper.password);
			if (!checkTable(databaseHelper.userTable)) {
				Statement stmt = con.createStatement();
				String sql = "CREATE TABLE " + databaseHelper.userTable
						+ "(id int NOT NULL PRIMARY KEY AUTO_INCREMENT,username VARCHAR(255) NOT NULL UNIQUE,email VARCHAR(255) NOT NULL UNIQUE,password VARCHAR(255) NOT NULL,role int NOT NULL,first_name VARCHAR(255) NOT NULL,last_name VARCHAR(255) NOT NULL,display_name VARCHAR(255) NOT NULL,uid VARCHAR(255) NOT NULL,BirthDate VARCHAR(255),first_time int NOT NULL)";
				stmt.executeUpdate(sql);
			}
			con.close();
			return true;
		} catch (Exception e) {

			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static boolean createLogTable() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/" + databaseHelper.database + "?allowPublicKeyRetrieval=true&useSSL=false",
					databaseHelper.username, databaseHelper.password);
			if (!checkTable(databaseHelper.logTable)) {
				Statement stmt = con.createStatement();
				String sql = "CREATE TABLE " + databaseHelper.logTable
						+ "(id int NOT NULL PRIMARY KEY AUTO_INCREMENT,username VARCHAR(255) NOT NULL,"
						+ "colName VARCHAR(255) NOT NULL,rowName VARCHAR(255) NOT NULL,"
						+ "dateOfUpdate VARCHAR(255) NOT NULL,beforeChange VARCHAR(255) NOT NULL,afterChange VARCHAR(255) NOT NULL)";
				stmt.executeUpdate(sql);
			}
			con.close();
			return true;
		} catch (Exception e) {

			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static boolean createMasterDataTable() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/" + databaseHelper.database + "?allowPublicKeyRetrieval=true&useSSL=false",
					databaseHelper.username, databaseHelper.password);
			Statement stmt = con.createStatement();
			// stmt.executeUpdate("DROP TABLE "+masterDataTable);
			if (!checkTable(databaseHelper.masterDataTable)) {
				String sql = "CREATE TABLE " + databaseHelper.masterDataTable
						+ "(id int NOT NULL PRIMARY KEY AUTO_INCREMENT, item_id VARCHAR(255),item_name VARCHAR(255),"
						+ "item_type VARCHAR(255),item_start_date VARCHAR(255),item_end_date VARCHAR(255),item_remarks VARCHAR(255),po_no VARCHAR(255),"
						+ "po_start_date VARCHAR(255),po_end_date VARCHAR(255),po_value VARCHAR(255),project_id VARCHAR(255) NOT NULL,project_name VARCHAR(255) NOT NULL,"
						+ "project_start_date VARCHAR(255) NOT NULL,project_end_date VARCHAR(255) NOT NULL,project_remarks VARCHAR(255) NOT NULL,project_manager VARCHAR(255) NOT NULL,"
						+ "project_type VARCHAR(255) NOT NULL,project_stauts VARCHAR(255) NOT NULL, payment_value VARCHAR(255),payment_date VARCHAR(255))";
				stmt.executeUpdate(sql);
				System.out.println("created successfully");
			}
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {

			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static boolean createProjectTable() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/" + databaseHelper.database + "?allowPublicKeyRetrieval=true&useSSL=false",
					databaseHelper.username, databaseHelper.password);
			Statement stmt = con.createStatement();
			// stmt.executeUpdate("DROP TABLE "+masterDataTable);
			if (!checkTable(databaseHelper.projectTable)) {
				String sql = "CREATE TABLE " + databaseHelper.projectTable
						+ "(id int NOT NULL PRIMARY KEY AUTO_INCREMENT,project_id VARCHAR(255) NOT NULL,project_name VARCHAR(255) NOT NULL,"
						+ "project_start_date VARCHAR(255) ,project_end_date VARCHAR(255) ,project_remarks VARCHAR(255) NOT NULL,project_manager VARCHAR(255) NOT NULL,"
						+ "project_type VARCHAR(255) NOT NULL,project_stauts VARCHAR(255) NOT NULL,project_max_amount VARCHAR(255) NOT NULL, payment_value VARCHAR(255),payment_date VARCHAR(255))";
				stmt.executeUpdate(sql);
				System.out.println("created successfully");
			}
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {

			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static boolean createItemTable() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/" + databaseHelper.database + "?allowPublicKeyRetrieval=true&useSSL=false",
					databaseHelper.username, databaseHelper.password);
			Statement stmt = con.createStatement();
			// stmt.executeUpdate("DROP TABLE "+masterDataTable);
			if (!checkTable(databaseHelper.itemTable)) {
				String sql = "CREATE TABLE " + databaseHelper.itemTable
						+ "(id int NOT NULL PRIMARY KEY AUTO_INCREMENT, item_id VARCHAR(255),item_name VARCHAR(255),"
						+ "item_type VARCHAR(255),item_start_date VARCHAR(255),item_end_date VARCHAR(255),item_remarks VARCHAR(255),po_no VARCHAR(255),"
						+ "po_value VARCHAR(255),project_id VARCHAR(255) NOT NULL,"
						+ "payment_value VARCHAR(255),payment_date VARCHAR(255))";
				stmt.executeUpdate(sql);
				System.out.println("created successfully");
			}
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {

			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static boolean createPoTable() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/" + databaseHelper.database + "?allowPublicKeyRetrieval=true&useSSL=false",
					databaseHelper.username, databaseHelper.password);
			Statement stmt = con.createStatement();
			// stmt.executeUpdate("DROP TABLE "+masterDataTable);
			if (!checkTable(databaseHelper.poTable)) {
				String sql = "CREATE TABLE " + databaseHelper.poTable
						+ "(id int NOT NULL PRIMARY KEY AUTO_INCREMENT, po_no VARCHAR(255),po_start_date VARCHAR(255),"
						+ "po_end_date VARCHAR(255))";
				stmt.executeUpdate(sql);
				System.out.println("created successfully");
			}
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {

			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static boolean createTaskTable() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/" + databaseHelper.database + "?allowPublicKeyRetrieval=true&useSSL=false",
					databaseHelper.username, databaseHelper.password);
			Statement stmt = con.createStatement();
			// stmt.executeUpdate("DROP TABLE task");
			// id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
			// FOREIGN KEY (item_id) REFERENCES masterdata(item_id) ON UPDATE CASCADE ON
			// DELETE RESTRICT
			if (!checkTable(databaseHelper.taskTable)) {
				String sql = "CREATE TABLE " + databaseHelper.taskTable
						+ "(id int PRIMARY KEY AUTO_INCREMENT,task_id VARCHAR(255) NOT NULL ,item_id VARCHAR(255) NOT NULL,task_description VARCHAR(255) NOT NULL)";
				stmt.executeUpdate(sql);
				System.out.println("created successfully");
			}
			con.close();
			return true;
		} catch (Exception e) {

			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static boolean createDatabase() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mariadb://localhost:" + databaseHelper.port + "/?allowPublicKeyRetrieval=true&useSSL=false", databaseHelper.username,
					databaseHelper.password);
			if (!checkDatabase()) {
				Statement stmt = con.createStatement();
				String sql = "CREATE DATABASE " + databaseHelper.database;
				stmt.executeUpdate(sql);
			}
			con.close();
			return true;
		} catch (Exception e) {

			System.out.println(
					"Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
			System.out.println(e);
			return false;
		}
	}

	public static void main(String[] args) {
		// createDatabase();
		// createUserTable();
		// printTable();
		// // createMasterDataTable();
		// createTaskTable();
		// createLogTable();
		// createProjectTable();
		// createPoTable();
		// createItemTable();
		// excelHelper.wrtieExcelFile();
		// sysout

		SpringApplication.run(MastersheetApplication.class, args);
	}

}
