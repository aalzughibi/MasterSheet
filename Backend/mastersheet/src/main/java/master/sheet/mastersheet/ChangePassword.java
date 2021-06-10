package master.sheet.mastersheet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

// import org.apache.tomcat.util.http.MediaType;
import org.springframework.http.*;
import java.sql.*;
import org.springframework.web.bind.annotation.*;

import master.sheet.mastersheet.User.User;
import master.sheet.mastersheet.User.UserPassword;
@RestController
@RequestMapping("changePassword")
public class ChangePassword {
    public static String port = "3306";
    public static String database = "mastersheetdatabase";
    public static String username = "root";
    public static String password = "1234";
    public static String userTable="user";
    public static boolean updatePassword(UserPassword u){
        try{
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "UPDATE "+userTable+" SET password = '"+u.getPassword()+"' WHERE uid = '"+u.getUid()+"'";
            stmt.executeUpdate(sql);
            return true;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    public static User userExist(String uid){
        try{
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
                "jdbc:mariadb://localhost:"+port+"/"+database,username,password); 
			//here sonoo is database name, root is username and password  
			// System.out.println("Success Success Success Success Success Success Success Success Success Success Success Success Success ");
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from user"); 
            // System.out.println("User"); 
			while(rs.next())  {
                if (rs.getString(9).equals(uid)){
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
                    con.close(); 
                    return userTemp;
                }

            }
			con.close(); 
            return null;
        }catch(Exception e){
        System.out.println(e);
        return null;
        }
    }
    @PostMapping()
    public ResponseEntity<HashMap<String,Object>> ChangePasswordAccount(@RequestBody UserPassword up){
        User user = userExist(up.getUid());
        System.out.println(up.getUid());
        if (user !=null){
        if(updatePassword(up))
        {
        System.out.println("success");
            return ResponseEntity.ok().build();
        }
        else
        System.out.println("fail");
    }
    else
    System.out.println("user does not exist");
    return ResponseEntity.badRequest().build();
}
}
