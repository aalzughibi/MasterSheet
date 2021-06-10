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
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.SignatureAlgorithm;
import master.sheet.mastersheet.User.User;
import master.sheet.mastersheet.User.UserPassword;
@RestController
@RequestMapping("Login")
public class Login {
	public  String setJWT(String subject,String uid,String expired,String username){
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
    @PostMapping
    public ResponseEntity<HashMap<String,String>> LoginUser(){
        System.out.println(getJWTToken(setJWT("Login", "moahmedkjns jsbjhs", "duhduiidu", "abody403"))[1]);
return ResponseEntity.ok().build();
    }
}
