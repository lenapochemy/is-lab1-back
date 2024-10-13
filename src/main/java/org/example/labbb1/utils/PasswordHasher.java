package org.example.labbb1.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

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
}
