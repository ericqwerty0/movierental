package com.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import com.security.NewPasswordHashing;
import com.security.PasswordHashing;

public class Test {
    
	  public static void main(String[] args) throws NoSuchAlgorithmException {
		  String regExp = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
		  String text = ("ASD34.5");
	  
		  if (text == null || text.matches(regExp) ) {
			  System.out.println(text);
		  }else{
			  System.out.println("wrong");
		  }
	  }
}
