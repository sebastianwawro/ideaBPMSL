/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.controller.helpers;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 *
 * @author Sebastian
 */
public class SecurityHelper {
    private static final SecurityHelper _instance = new SecurityHelper();
    
    private SecurityHelper(){}
    
    public static final SecurityHelper getInstance() {
        return _instance;
    }
    
    public int getCurrentTime() {
        return (int)(System.currentTimeMillis()/1000);
    }
    
    public String getRandomUuid() {
        return UUID.randomUUID().toString();
    }
    
    public String getRandomSalt() {
        return getRandomUuid().substring(0, 8);
    }

    public String getMD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest salt = MessageDigest.getInstance("MD5");
        salt.update(text.getBytes("UTF-8"));

        byte[] messageDigest = salt.digest();
        //byte[] messageDigest = salt.digest(input.getBytes()); 

        // Convert byte array into signum representation 
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value 
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public String getSHA256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest salt = MessageDigest.getInstance("SHA-256");
        salt.update(text.getBytes("UTF-8"));

        byte[] messageDigest = salt.digest();
        //byte[] messageDigest = salt.digest(input.getBytes()); 

        // Convert byte array into signum representation 
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value 
        String hashtext = no.toString(16);
        while (hashtext.length() < 64) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
    
}
