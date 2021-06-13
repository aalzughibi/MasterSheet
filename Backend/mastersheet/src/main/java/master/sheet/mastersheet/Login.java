package master.sheet.mastersheet;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import javax.validation.Valid;

import com.nimbusds.jose.shaded.json.JSONObject;

import io.jsonwebtoken.Jwts;
// import org.apache.tomcat.util.http.MediaType;
import org.springframework.http.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.SignatureAlgorithm;
import master.sheet.mastersheet.Auth.Auth;
import master.sheet.mastersheet.User.User;
import master.sheet.mastersheet.User.UserPassword;
@RestController
@RequestMapping("Login")
public class Login {
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
	public String[] getJWTToken(String token){
	// Decode JWT
	String[] chunks = token.split("\\.");
	Base64.Decoder decoder = Base64.getDecoder();
String header = new String(decoder.decode(chunks[0]));
String payload = new String(decoder.decode(chunks[1]));
System.out.println(header);
System.out.println(payload);
String[] jwt = {header,payload};
return jwt;
// end decode jwt
	}
	public static User checkLogin(String userStr,String pass){
		try{

			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database+"?allowPublicKeyRetrieval=true&useSSL=false",username,password);
			Statement stmt = con.createStatement();
			String sql = "select count(*) from " +userTable + " where username='"+userStr+"' and password='"+pass+"'";
            ResultSet rs=stmt.executeQuery(sql);  
            rs.next();
			User user;
			System.out.println("outside");
			if (rs.getInt(1)==1){
				user =new User();
				System.out.println(pass);
				sql = "select * from " +userTable + " where username='"+userStr+"' and password='"+pass+"'";
				rs=stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(2));
				user.setUsername(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setRole(rs.getInt(5));
                user.setFirst_name(rs.getString(6));
                user.setLast_name(rs.getString(7));
                user.setDisplay_name(rs.getString(8));
                user.setUid(rs.getString(9));
                user.setBirthDate(rs.getString(10));
                user.setFirst_time(rs.getInt(11));
				return user;
			}
			return null;
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
    @PostMapping
    public ResponseEntity<Map<String,Object>> LoginUser(@RequestBody LoginUser lu){	
		// check username and password
		User user = checkLogin(lu.getUsername(),lu.getPassword());
		if (user!=null){
			// if auth return JWT
			LocalDateTime ldt = LocalDateTime.now().plusHours(1);
			Object expired = ldt.toEpochSecond(ZoneOffset.UTC);
			String jwt = setJWT("LOGIN_AUTH", user.getUid(),expired , user.getUsername());
			Map data =new HashMap();
			data.put("JWT",jwt);
			// Auth.validJWT(jwt);
			Boolean isFirstTime =user.getFirst_time()==1?false:true;
			data.put("first_time",isFirstTime);
			data.put("expired", expired);
			return new ResponseEntity<>(data,HttpStatus.OK);
		}
		return ResponseEntity.noContent().build();
    }
}
