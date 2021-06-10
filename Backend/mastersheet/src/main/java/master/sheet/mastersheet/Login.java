package master.sheet.mastersheet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import javax.validation.Valid;
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
    private String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
    @PostMapping
    public ResponseEntity<HashMap<String,String>> LoginUser(){
        System.out.println(getJWTToken("Mohammed"));
        
return ResponseEntity.ok().build();
    }
}
