package security;

import java.util.Arrays;

import com.ibm.websphere.security.jwt.Claims;
import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtBuilder;
import com.ibm.websphere.security.jwt.JwtException;
import com.ibm.websphere.security.jwt.JwtToken;
import com.ibm.websphere.security.jwt.KeyException;

public class JwtGenerator {
	
	public JwtGenerator() {
		
	}
	
	public String getToken(String username) throws JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
	    // 1. Create a JWTBuilder Object.
	    JwtBuilder jwtBuilder = JwtBuilder.create();
	    
	   // Overwrite issuer. This is optional and if issuer is not specified either in the server configuration or here,
	   // then the Builder will construct a default issuer Url
	    jwtBuilder = jwtBuilder.issuer("http://localhost:9080/RealWorld");
	    
	   // Overwrite any of the following
	   // audience, expiration time, not before, subject, signing key or algorithm, jti
	    jwtBuilder = jwtBuilder.signWith("HS256", "shared secret");
	    
	    jwtBuilder = jwtBuilder.claim("name", "prince");
	    
	   // Overwrite or set any additional claims
	    jwtBuilder = jwtBuilder.claim(Claims.SUBJECT, username);


	   // 2. Create a JWT token
	     JwtToken jwt = jwtBuilder.buildJwt();
	   
	  // Return JWT as String.
	     return jwt.compact();
	}
	
}
