package com.project.lgw.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Util {
	
	  public String encrypt(String text) throws NoSuchAlgorithmException {
		  
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        try {
				md.update(text.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return bytesToHex(md.digest());
	        
	    }
	    private String bytesToHex(byte[] bytes) {
	    	
	        StringBuilder builder = new StringBuilder();
	        for (byte b : bytes) {
	            builder.append(String.format("%02x", b));	            
	        }
	        return builder.toString();
	    }

	}