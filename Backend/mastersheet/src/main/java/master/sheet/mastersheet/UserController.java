package master.sheet.mastersheet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
@RestController
@RequestMapping("users")
public class UserController{
    public String port = "3306";
    public String database = "mastersheet";
    public String username = "root";
    public String password = "1234";
    // deafult port=3306 database=MasterSheet root 1234
    public boolean Connection(String port,String database,String username,String password){
        try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
              
			//here sonoo is database name, root is username and password  
			System.out.println("Success Success Success Success Success Success Success Success Success Success Success Success Success ");
 
			con.close();  
			}catch(Exception e){
				System.out.println("Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
				 System.out.println(e);} 
            return true;
    }
    public boolean checkDatabase(){
        try{  
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/",username,password);
            Statement stmt = con.createStatement();
			ResultSet resultSet = con.getMetaData().getCatalogs();
			while (resultSet.next()) {
        
				String databaseName = resultSet.getString(1);
				System.out.println(databaseName);
				  if(databaseName.equals("mastersheet")){
					  return true;
				  }
			  }
              con.close();  
              return false;
			}catch(Exception e){
                
				System.out.println("Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
				 System.out.println(e);
                return false;
            }
    }
    public boolean createDatabase(){
        try{  
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/",username,password);
            Statement stmt = con.createStatement();
            String sql = "CREATE DATABASE "+database;
            stmt.executeUpdate(sql); 
			System.out.println("Success Success Success Success Success Success Success Success Success Success Success Success Success ");
			con.close();  
            return true;
			}catch(Exception e){
                
				System.out.println("Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error Error ");
				 System.out.println(e);
                return false;
            }
    }
}