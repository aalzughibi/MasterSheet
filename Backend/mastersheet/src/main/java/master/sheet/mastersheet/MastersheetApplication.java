package master.sheet.mastersheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.*;
@SpringBootApplication
public class MastersheetApplication {
	public static String port = "3306";
    public static  String database = "mastersheetdatabase";
    public static String username = "root";
    public static String password = "1234";
	public static String userTable = "user";
	public static boolean checkDatabase(){
        try{  
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/",username,password);
			ResultSet resultSet = con.getMetaData().getCatalogs();
			while (resultSet.next()) {
				String databaseName = resultSet.getString(1);
				  if(databaseName.equals(database))
					  return true;
			  }
              con.close();  
              return false;
			}catch(Exception e){
                
				System.out.println("Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
				 System.out.println(e);
                return false;
            }
    }
	public static boolean checkTable(String Table){
		try{  
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] {"TABLE"});
	while (resultSet.next()) {
    	String name = resultSet.getString("TABLE_NAME");
		if (name.equals(Table))
		return true;
}
              con.close();  
              return false;
			}catch(Exception e){
                
				System.out.println("Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
				 System.out.println(e);
                return false;
            }
	}
	public static boolean createUserTable(){
		try{  
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			if (!checkTable(userTable)){
				Statement stmt = con.createStatement();
				String sql = "CREATE TABLE "+userTable+"(id int NOT NULL PRIMARY KEY AUTO_INCREMENT,username VARCHAR(255) NOT NULL UNIQUE,email VARCHAR(255) NOT NULL UNIQUE,password VARCHAR(255) NOT NULL,role int NOT NULL,first_name VARCHAR(255) NOT NULL,last_name VARCHAR(255) NOT NULL,display_name VARCHAR(255) NOT NULL,uid VARCHAR(255) NOT NULL,BirthDate VARCHAR(255) NOT NULL,first_time int NOT NULL)";
				stmt.executeUpdate(sql); 
			}
			con.close();  
            return true;
			}catch(Exception e){
                
				System.out.println("Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
				 System.out.println(e);
                return false;
            }
	}
    public static boolean createDatabase(){
        try{  
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/",username,password);
			if (!checkDatabase()){
				Statement stmt = con.createStatement();
				String sql = "CREATE DATABASE "+database;
				stmt.executeUpdate(sql); 
			}
			con.close();  
            return true;
			}catch(Exception e){
                
				System.out.println("Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
				 System.out.println(e);
                return false;
            }
    }
	public static void main(String[] args) {
		createDatabase();
		createUserTable();
		SpringApplication.run(MastersheetApplication.class, args);
	}

}
