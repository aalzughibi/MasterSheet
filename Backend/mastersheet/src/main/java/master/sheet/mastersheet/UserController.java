package master.sheet.mastersheet;
import java.util.*;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import master.sheet.mastersheet.Auth.Auth;
import master.sheet.mastersheet.User.User;
import master.sheet.mastersheet.User.UserPassword;
import master.sheet.mastersheet.Validate.Validate;

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
            rs.next();
            User[] users = new User[rs.getInt(1)];
             sql = "SELECT * FROM "+userTable;
             rs=stmt.executeQuery(sql);  
                int counter=0;
                while(rs.next()) {
                User userTemp = new User();
                userTemp.setUsername(rs.getString(2));
                userTemp.setEmail(rs.getString(3));
                userTemp.setRole(rs.getInt(5));
                userTemp.setFirst_name(rs.getString(6));
                userTemp.setLast_name(rs.getString(7));
                userTemp.setDisplay_name(rs.getString(8));
                userTemp.setUid(rs.getString(9));
                userTemp.setBirthDate(rs.getString(10));
                userTemp.setFirst_time(rs.getInt(11));
                users[counter++] = userTemp;
            }
            return new ResponseEntity(users,HttpStatus.OK);
        }
        catch(Exception e){
        System.out.println(e);
        return ResponseEntity.noContent().build();
        }
    }
    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> getUserByUid(@RequestHeader("Authorization") String header_auth,@PathVariable String userId){
        try{
            if(Auth.validJWT(header_auth)){//start//end
                JSONObject jo =  Auth.convert_JsonString_To_Json(Auth.getJWTToken(header_auth)[1]);
                // check if thier admin or user
                if(isAdmin(String.valueOf(jo.get("userId")))){
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "SELECT COUNT(*) AS total FROM "+userTable+" WHERE uid='"+userId+"'";
            ResultSet rs=stmt.executeQuery(sql);  
            rs.next();
            if (rs.getInt("total")==1){
            sql = "SELECT * FROM "+userTable+" WHERE uid='"+userId+"'";
            rs=stmt.executeQuery(sql);  
            rs.next();
                User userTemp = new User();
                userTemp.setUsername(rs.getString(2));
                userTemp.setEmail(rs.getString(3));
                userTemp.setRole(rs.getInt(5));
                userTemp.setFirst_name(rs.getString(6));
                userTemp.setLast_name(rs.getString(7));
                userTemp.setDisplay_name(rs.getString(8));
                userTemp.setUid(rs.getString(9));
                userTemp.setBirthDate(rs.getString(10));
                userTemp.setFirst_time(rs.getInt(11));
            return new ResponseEntity(userTemp,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
}
return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
        catch(Exception e){
        System.out.println(e);
        return ResponseEntity.noContent().build();
        }
    }
    @PostMapping(path = "/{userId}")
    public ResponseEntity<Map<String,Object>> updateUser(@RequestHeader("Authorization") String header_auth,@RequestBody User user,@PathVariable String userId){
        try{
            if(Auth.validJWT(header_auth)){
                JSONObject jo =  Auth.convert_JsonString_To_Json(Auth.getJWTToken(header_auth)[1]);
                if(isAdmin(String.valueOf(jo.get("userId")))){
                    user.setUid(userId);
                    if(updateUser(user)){
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                    else{
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
                else{

                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            
        }
    }
@PutMapping(path = "/updateDisplayName/{userId}")
public ResponseEntity<Map<String,Object>> updateDisplayName(@RequestHeader("Authorization") String header_auth,@RequestBody User user,@PathVariable String userId){
try {
    if(Auth.validJWT(header_auth)){
        JSONObject jo =  Auth.convert_JsonString_To_Json(Auth.getJWTToken(header_auth)[1]);
        String uid = String.valueOf(jo.get("userId"));
        if(uid.equals(userId)){
            
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    else{
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
} catch (Exception e) {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
}
}
    private static boolean updateUser(User user){
        try {
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
           String sql = "UPDATE "+userTable+" SET "+"first_name='"+user.getFirst_name()+"', last_name='"+user.getLast_name()+"', display_name='"+user.getDisplay_name()+"', role="+user.getRole()+" WHERE uid='"+user.getUid()+"'";
            stmt.executeUpdate(sql);
           return true;
        } catch (Exception e) {
            
            return false;
        }
    }
    private static boolean updateDisplayName(User user){
        try {
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
           String sql = "UPDATE "+userTable+" SET "+"display_name='"+user.getDisplay_name()+"'";
            stmt.executeUpdate(sql);
           return true;
        } catch (Exception e) {
            
            return false;
        }
    }
    public static boolean isAdmin(String uid){
        try{
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database+"?allowPublicKeyRetrieval=true&useSSL=false",username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from " +userTable + " where uid='"+uid+"'";
            ResultSet rs=stmt.executeQuery(sql); 
            rs.next();
            return rs.getInt(5)==0?true:false;
        }catch(Exception e){
            return false;
        }
    }
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> PostUser(@RequestHeader("Authorization") String header_auth, @RequestBody User u){//strat
        try{//strat
            if(Auth.validJWT(header_auth)){//start//end
                JSONObject jo =  Auth.convert_JsonString_To_Json(Auth.getJWTToken(header_auth)[1]);
                // check if thier admin or user
                if(isAdmin(String.valueOf(jo.get("userId")))){

                    if (Validate.isValidUsername(u.getUsername()) &&
                    (EmailValidator.getInstance().isValid(u.getEmail())) &&
                    Validate.isValidFirstName(u.getFirst_name()) &&
                    Validate.isValidLastName(u.getLast_name())&&
                    ((u.getDisplay_name().length()>=2)&&(u.getDisplay_name().length()<=20))
                    ){//start //end
                        
                        if(InsertUser(u))
                        return ResponseEntity.accepted().build();
                        else
                        return ResponseEntity.badRequest().build();
                    }
                //end
                    else{
                return ResponseEntity.noContent().build();
            }
            }
        
            else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        }
    

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
}