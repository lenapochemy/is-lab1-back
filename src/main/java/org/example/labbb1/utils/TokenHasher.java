package org.example.labbb1.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.labbb1.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenHasher {

//    private final long expireTime = 30 * 60 * 1000; //30 минут
//    private final long expireTimeSeconds = expireTime / 1000;
    private final String tokenSecret = "iwanttodie";
    private final Algorithm algorithm = Algorithm.HMAC256(tokenSecret);

    public TokenHasher(){}

    public String generateToken(User user){
//        Date date = new Date(System.currentTimeMillis() + expireTime);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return JWT.create()
                .withHeader(header)
//                .withClaim("login", user.getLogin())
                .withClaim("id", user.getId())
//                .withExpiresAt(date)
                .sign(algorithm);
    }

    public boolean verifyToken(String token){
        try {
            JWTVerifier verifier = JWT.require(algorithm)
//                    .acceptExpiresAt(expireTimeSeconds)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e){
            return false;
        }
    }

    public User decodeToken(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
//        String badLogin = decodedJWT.getClaim("login").asString(); //getClaim возвращает с кавычками
//       System.out.println(badLogin);
//        String login = badLogin.substring(1, badLogin.length()-1);
        Integer id = decodedJWT.getClaim("id").asInt();
        User user = new User();
//        user.setLogin(login);
        user.setId(id);
        return user;
    }




}
