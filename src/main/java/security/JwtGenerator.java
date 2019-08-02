package security;

// import com.ibm.websphere.security.jwt.Claims;
import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtBuilder;
import com.ibm.websphere.security.jwt.JwtException;
import com.ibm.websphere.security.jwt.KeyException;

public class JwtGenerator {

    public String getToken(String newUser)
        throws JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        // 1. Create a JWTBuilder Object.
        JwtBuilder jb = JwtBuilder.create();

        // Overwrite issuer. This is optional and if issuer is not specified either in
        // the server configuration or here,
        // then the Builder will construct a default issuer Url
        // jwtBuilder = jwtBuilder.issuer("http://localhost:9080/RealWorld");

        // Overwrite any of the following
        // audience, expiration time, not before, subject, signing key or algorithm, jti
        jb.subject(newUser);
    
        // jwtBuilder = jwtBuilder.signWith("HS256", "shared secret");

        // Overwrite or set any additional claims
        // jwtBuilder = jwtBuilder.claim(Claims.SUBJECT, username);

        // 2. Create a JWT token
        return jb.buildJwt().compact();
    }

}
