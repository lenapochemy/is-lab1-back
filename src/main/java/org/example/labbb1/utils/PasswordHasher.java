package org.example.labbb1.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher implements PasswordEncoder {

    public PasswordHasher(){}
    public String hashPassword(String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-224");
            byte[] md = messageDigest.digest(password.getBytes());
            BigInteger no = new BigInteger(1, md);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32){
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e){
            return null;
        }
    }

    @Override
    public String encode(CharSequence rawPassword){
        return hashPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(hashPassword(rawPassword.toString()));
    }
}
