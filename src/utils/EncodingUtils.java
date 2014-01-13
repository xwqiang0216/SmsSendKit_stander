/**
 * 
 */
package utils;

import java.security.MessageDigest;
import java.util.List;

/**
 * @author Chenlz 
 *
 */
public class EncodingUtils {
	public static String gbk2iso(String src){
		try{
			return new String(src.getBytes("GBK"),"ISO8859_1");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String gbk2utf(String src){
		try{
			return new String(src.getBytes("GBK"),"UTF-8");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String iso2gbk(String src){
		try{
			return new String(src.getBytes("ISO8859_1"),"GBK");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String  lisToStr(List<String> list,String spit){
		StringBuilder buider = new StringBuilder();
		for(int i = 0 ;list != null &&i < list.size() ;i++){
			buider.append(list.get(i));
			if(i < list.size() -1){
				buider.append(spit);
			}
		}
		return buider.toString();
	}
	public final static String MD5(String s) {

	    char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e', 'f'};

	    try {

	      byte[] strTemp = s.getBytes();

	      MessageDigest mdTemp = MessageDigest.getInstance("MD5");

	      mdTemp.update(strTemp);

	      byte[] md = mdTemp.digest();

	      int j = md.length;

	      char str[] = new char[j * 2];

	      int k = 0;

	      for (int i = 0; i < j; i++) {

	        byte byte0 = md[i];

	        str[k++] = hexdigits[byte0 >>> 4 & 0xf];

	        str[k++] = hexdigits[byte0 & 0xf];

	      }

	      return new String(str);

	    }

	    catch (Exception e) {

	      return null;

	    }

	  }


}
