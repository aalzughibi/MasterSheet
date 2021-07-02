package master.sheet.mastersheet.Auth;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
// import java.sql.*;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import master.sheet.mastersheet.Service.UserSerivce;

public class Auth {
	public static String port = "3306";
    public static String database = "mastersheetdatabase";
    public static String username = "root";
    public static String password = "1234";
    public static String userTable="user";
    public static  String setJWT(String subject,String uid,long expired,String username){

			String secretkey="NovaIsbestWaterBrand";
		
			//The JWT signature algorithm we will be using to sign the token
			String jwtToken = Jwts.builder()
				.setSubject(subject)
				.claim("userId", uid).claim("exp", expired).claim("username", username)
				.signWith(SignatureAlgorithm.HS256,secretkey.getBytes()).compact();
			return jwtToken;
	
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
    public static boolean isAuthenticated(UserSerivce userSerivce,String jwtToken){
        try{
            if(validJWT(jwtToken)){
               String payload = getJWTToken(jwtToken)[1];
               JSONObject payloadJSON = convert_JsonString_To_Json(payload);
                if(userSerivce.isExist(payloadJSON.getString("userId")))
                    return true;                
                return false; 
            }
            return false;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    public static boolean isAdmin(UserSerivce userSerivce,String jwtToken){
        try{
            if(validJWT(jwtToken)){
               String payload = getJWTToken(jwtToken)[1];
               JSONObject payloadJSON = convert_JsonString_To_Json(payload);
               System.out.println(payloadJSON.getString("userId"));
                if(userSerivce.isExist(payloadJSON.getString("userId")))
                    if(userSerivce.isAdmin(payloadJSON.getString("userId"))){
                    System.out.println(true);
                        return true;      
                    }
                    else          
                        return false;
                else
                    return false; 
            }
            return false;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    public static boolean validJWT(String jwt){
        try{
            String[] jwtArr = getJWTToken(jwt);
            // String payload = jwtArr[1];
            JSONObject header = new JSONObject(jwtArr[0]);
            JSONObject payload = new JSONObject(jwtArr[1]);
            // payload.getLong("exp");
            long expiredDate = payload.getLong("exp");
            long nowDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

            if(expiredDate<nowDate)
                return false;
            
            // JSONObject jsonObject3 = new JSONObject(jwtArr[2]);
            return true;
        }catch(Exception e){
            System.out.println("JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR JWT_ERROR ");
            return false;
        }
    }
    // 1624969391 now
    // 1624971578 expired
}
