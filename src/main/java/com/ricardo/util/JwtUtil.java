package com.ricardo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    // Built once at class-load time from the env var - never hardcoded
    // Encrypt our secret key using the HMAC-SHA cryptographic algorithm
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            System.getenv("JWT_SECRET").getBytes()
    );

    // Time-To Live for tokens (in milliseconds)
    private static final long ACCESS_TOKEN_TTL = 15 * 60 * 1000L; // 15 Minutes
    private static final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60 * 1000L; // 7 Days

    // A private constructor that makes the class impossible to instantiate with new JwtUtil().
    // This is a standard pattern for pure utility/helper classes — every method is static, so there's no meaningful instance state.
    // This signals to anyone reading the code that this class is never meant to be an object.
    private JwtUtil() {};

    // For the Access Token we need:
    // Subject standard JWT claim
    // Expiration time standard JWT claim
    // Issued At standard JWT claim
    // In our case we added one claim of our own not part of any standard and that's the 'userId' claim
    public static String generateAccessToken(long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TTL))
                .signWith(SECRET_KEY)
                .compact();
    }

    // In our case the Refresh Token is not a JWT - it's an opaque string stored in H2
    // JWT would be wasteful here since you validate it by DB lookup anyway
    public static String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    // Returns Claims on success, null on any failure (expired, revoked, tampered...)
    // AuthenticatedHandler class this and checks for null - no exception leaks upward
    public static Claims validateToken(String token) {
        try {
            return Jwts.parser() // Returns a JwtParserBuilder - the reading counterpart to JwtBuilder. Nothing is parsed yet
                    .verifyWith(SECRET_KEY) // Tells the parser which key to use when verifying the signature. When it parses the token, it will recompute the HMAC of header.payload using this key and compare it to the signature in the token. If they don't match, SignatureException is thrown. This is what prevents attackers from forging tokens or tampering with claims.
                    .build() // Finalizes the parser configuration and returns a JwtParser - the actual parser object, now ready to process tokens. The
                    // builder is separate from the parser itself because configuring and using a parser are two different concerns.
                    .parseSignedClaims(token) // The main event. it does four things sequentially:
                    // 1. Splits the string on . into header, payload and signature
                    // 2. Base64-decodes and JSON-parses header and payload
                    // 3. Recomputes the HMAC signature and compares to the one in the token
                    // 4. Checks that the current time is before exp
                    // If any of these fail, a JwtException subclass is thrown. On success, it returns a Jws<Claims> object - a wrapper that holds both the
                    // header and the payload.
                    .getPayload(); // Unwraps the Jws<Claims> to give you just the Claims map. This is what gets returned to AuthenticatedHandler, which then
            // passes it down to your actual handler methods where you extract userId, role, etc.
        } catch (JwtException | IllegalArgumentException e) {
            return null;
            // JwtException catches every JJWT failure mode — expired, wrong signature, malformed format, unsupported algorithm. IllegalArgumentException covers the edge case where token is null or an empty string — JJWT throws this (not a JwtException) in that situation. Returning null rather than rethrowing is a deliberate design choice: it keeps AuthenticatedHandler simple with a single null-check, and prevents exception stack traces from leaking implementation details in HTTP responses.
        }
    }



}
