package td.api.smgp30.nioApi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	public static String Md5(String src){
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(src.getBytes());
			byte [] dest = md.digest();
			//System.out.println(new String(dest));
			return new String(dest);
			
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		} 
	}



}
