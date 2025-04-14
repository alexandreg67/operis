package com.operis.project.infrastructure.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTParser {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public JWTClaimsSet parseToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(jwtSecret);
            // Verify the signature
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid JWT signature");
            }
            // Retrieve JWT claims
            return signedJWT.getJWTClaimsSet();
        } catch (JOSEException | java.text.ParseException e) {
            throw new RuntimeException("Failed to parse JWT token", e);
        }
    }


}
