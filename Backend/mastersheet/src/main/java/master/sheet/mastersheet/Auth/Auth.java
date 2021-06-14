package master.sheet.mastersheet.Auth;

import java.sql.*;
import java.util.Base64;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.boot.json.JacksonJsonParser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Auth {
	public static String port = "3306";
    public static String database = "mastersheetdatabase";
    public static String username = "root";
    public static String password = "1234";
    public static String userTable="user";
    public  String setJWT(String subject,String uid,Object expired,String username){
		try {
			String secretkey="NovaIsbestWaterBrand";
		
			//The JWT signature algorithm we will be using to sign the token
			String jwtToken = Jwts.builder()
				.setSubject(subject)
				.claim("userId", uid).claim("expired", expired).claim("username", username)
				.signWith(SignatureAlgorithm.HS256,secretkey.getBytes()).compact();
			System.out.println("jwtToken=");
			System.out.println(jwtToken);
			return jwtToken;
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			return "Error";
		}
	}
	public static String[] getJWTToken(String token){
	// Decode JWT
	String[] chunks = token.split("\\.");
	Base64.Decoder decoder = Base64.getDecoder();
String header = new String(decoder.decode(chunks[0]));
String payload = new String(decoder.decode(chunks[1]));
String[] jwt = {header,payload};
return jwt;
// end decode jwt
	}
    public static JSONObject convert_JsonString_To_Json(String jsonString){
        return new JSONObject(jsonString);
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
            // if (rs.rs.getInt(5)==0)
            return rs.getInt(5)==0?true:false;
        }catch(Exception e){
            return false;
        }
    }
    public static boolean validJWT(String jwt){
        try{
            String[] jwtArr = getJWTToken(jwt);
            String payload = jwtArr[1];
            JSONObject jsonObject = new JSONObject(payload);
            return true;
        }catch(Exception e){
            System.out.println("JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR ");
            return false;
        }
    }
}
