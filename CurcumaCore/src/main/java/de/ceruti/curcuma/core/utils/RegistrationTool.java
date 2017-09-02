package de.ceruti.curcuma.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class RegistrationTool {
	private RegistrationTool() {

	}
	
	public static boolean verify(String text, String magic_salt, String test){
		String gen = genAuthcode(text, magic_salt);
		return gen.equalsIgnoreCase(test);
	}

	public static String genAuthcode(String text,String magic_salt) {
		MessageDigest md = null;
		String hash = null;
		byte[] encryptMsg = null;
		try {
			md = MessageDigest.getInstance("MD5");
			encryptMsg = md.digest((magic_salt+text).getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("No Such Algorithm Exception!");
			return null;
		}
		String swap = "";
		String byteStr = "";
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i <= encryptMsg.length - 1; i++) {
			byte cipher = encryptMsg[i];
			byteStr = Integer.toHexString(cipher);
			switch (byteStr.length()) {
			case 1:
				// if hex-number length is 1, add a '0' before
				swap = "0" + Integer.toHexString(cipher);
				break;
			case 2:
				// correct hex-letter
				swap = Integer.toHexString(cipher);
				break;
			case 8:
				// get the correct substring
				swap = (Integer.toHexString(cipher)).substring(6, 8);
				break;
			}
			strBuf.append(swap);
		}
		hash = strBuf.toString();
		
//		hash = "E580DCDD0B5E5939";
		
		//add 2 on each digit:
		char buf[]=new char[hash.length()];
		for(int i=0;i<hash.length();i++){
			char c = hash.charAt(i);
			if(!Character.isDigit(c)) {
				buf[i] = (char) (c+2);
			}
			else {
				byte b = (byte)Integer.parseInt(String.valueOf(c),16);
				String h = Integer.toHexString(b+2);
				buf[i]= h.charAt(h.length()-1);
			}
		}
		hash = new String(buf);
		
		//toUppercase and insert - after each 4 Digits
		StringBuffer out = new StringBuffer();
		for(int i=0;i<hash.length();i++){
			out.append(Character.toUpperCase(hash.charAt(i)));
			if((i+1)%4==0 && (i+1)< hash.length())
				out.append("-");
		}
		
		return out.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(RegistrationTool.genAuthcode(args[0],args[1]));
	}
}
