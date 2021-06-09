package master.sheet.mastersheet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import master.sheet.mastersheet.User.User;

import java.sql.*;
@RestController
@RequestMapping("users")
public class UserController{
    public static String port = "3306";
    public static String database = "mastersheetdatabase";
    public static String username = "root";
    public static String password = "1234";
    public static String userTable="user";
    // deafult port=3306 database=MasterSheet root 1234
    private static char[] generatePassword(int length) {
		String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
		String specialCharacters = "!@#%_-";
		String numbers = "1234567890";
		String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
		Random random = new Random();
		char[] password = new char[length];
  
		password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
		password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
		password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
		password[3] = numbers.charAt(random.nextInt(numbers.length()));
	 
		for(int i = 4; i< length ; i++) {
		   password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
		}
		return password;
	 }
	public static boolean InsertUser(User u){
		try{
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "+userTable+" (username,password,email,first_name,last_name,role,display_name,uid,BirthDate,first_time) "+
			String.format("VALUES ('%s','%s','%s','%s','%s',%s,'%s','%s','%s',%s)",u.getUsername(),generatePassword(10),u.getEmail(),u.getFirst_name(),u.getLast_name(),u.getRole(),u.getDisplay_name(),RandomStringUtils.randomAlphanumeric(40),u.getBirthDate(),0);
			stmt.executeUpdate(sql);
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
    @GetMapping()
    public ResponseEntity<User[]> getUsers(){
        try{
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "SELECT count(*) FROM "+userTable;
            ResultSet rs=stmt.executeQuery(sql);  
            // System.out.println("length of users: "+rs.getInt(1));
			// while(rs.next())  
            rs.next();
            User[] users = new User[10];
            System.out.println("users length: "+users.length);
             sql = "SELECT * FROM "+userTable;
             rs=stmt.executeQuery(sql);  
            //  id int NOT NULL PRIMARY KEY AUTO_INCREMENT,username VARCHAR(255) NOT NULL UNIQUE,email VARCHAR(255) NOT NULL UNIQUE,password VARCHAR(255) NOT NULL,role int NOT NULL,first_name VARCHAR(255) NOT NULL,last_name VARCHAR(255) NOT NULL,display_name VARCHAR(255) NOT NULL,uid VARCHAR(255) NOT NULL,BirthDate VARCHAR(255) NOT NULL,first_time int NOT NULL
            // for(int i=0;i<users.length;i++){
                int counter=0;
                while(rs.next()) {
                    System.out.println(rs.getString(3));
                users[counter].setUsername(rs.getString(2));
                users[counter].setEmail(rs.getString(3));
                users[counter].setRole(rs.getInt(5));
                users[counter].setFirst_name(rs.getString(6));
                users[counter].setLast_name(rs.getString(7));
                users[counter].setDisplay_name(rs.getString(8));
                users[counter].setUid(rs.getString(9));
                users[counter].setBirthDate(rs.getString(10));
                users[counter++].setFirst_time(rs.getInt(11));
            }
            return new ResponseEntity(users,HttpStatus.OK);
        }
        catch(Exception e){
        System.out.println(e);
        return ResponseEntity.noContent().build();
        }
    }
}