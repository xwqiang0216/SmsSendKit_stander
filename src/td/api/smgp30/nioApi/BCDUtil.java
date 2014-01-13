package td.api.smgp30.nioApi;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;


public class BCDUtil {
	/**
	 * @函数功能: BCD码转为10进制串(阿拉伯数据)
	 * @输入参数: BCD码
	 * @输出结果: 10进制串
	 */
	public static String bcd2Str(byte[] bytes){
		
	    StringBuffer temp=new StringBuffer(bytes.length*2);
        //System.out.println("----------------bytes length is:"+bytes.length+"--------------------");
        //debugData("bytes are",bytes);
	    for(int i=0;i<bytes.length;i++){
	     temp.append((byte)((bytes[i]& 0xf0)>>4));
	     //System.out.println("first of byte"+(byte)((bytes[i]& 0xf0)>>4));
	     temp.append((byte)(bytes[i]& 0x0f));
	    // System.out.println("second of byte"+(byte)(bytes[i]& 0x0f));
	    }
	    return temp.toString().substring(0,1).equalsIgnoreCase("0")?temp.toString().substring(1):temp.toString();
	}
	public static String bcd2Str1(byte[] b) {
		  StringBuffer sb = new StringBuffer();
		  for (int i = 0; i < b.length; i++) {
		   int h = ((b[i]&0xff) >> 4) + 48;
		   sb.append((char) h);
		   int l = (b[i] & 0x0f) + 48;
		   sb.append((char) l);
		  }
		  return sb.toString();
		 }
	
	
	/**
	    * @函数功能: 10进制串转为BCD码
	    * @输入参数: 10进制串
	    * @输出结果: BCD码
	    */
	public static byte[] str2Bcd(String asc) {
	    int len = asc.length();
	    int mod = len % 2;

	    if (mod != 0) {
	     asc = "0" + asc;
	     len = asc.length();
	    }

	    byte abt[] = new byte[len];
	    if (len >= 2) {
	     len = len / 2;
	    }

	    byte bbt[] = new byte[len];
	    abt = asc.getBytes();
	    int j, k;

	    for (int p = 0; p < asc.length()/2; p++) {
	     if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
	      j = abt[2 * p] - '0';
	     } else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
	      j = abt[2 * p] - 'a' + 0x0a;
	     } else {
	      j = abt[2 * p] - 'A' + 0x0a;
	     }

	     if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
	      k = abt[2 * p + 1] - '0';
	     } else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
	      k = abt[2 * p + 1] - 'a' + 0x0a;
	     }else {
	      k = abt[2 * p + 1] - 'A' + 0x0a;
	     }

	     int a = (j << 4) + k;
	     byte b = (byte) a;
	     bbt[p] = b;
	    }
	    return bbt;
	}
	
	public static void main(String []args) throws UnsupportedEncodingException{
		byte b[] = str2Bcd("148781");
		
		String str = bcd2Str(b);
		System.out.println(str);
	}
	public static void debugData(String desc,byte[] data){
		System.out.println("消息总长:"+data.length +" "+desc);		
		int count=0;
	      for(int i=0;i<data.length;i++){
	    	 int b=data[i];
	    	  if(b<0){b+=256;}
	    	 String hexString= Integer.toHexString(b);
	hexString = (hexString.length() == 1) ? "0" + hexString : hexString;
	    	 System.out.print(hexString+"  ");
	    	 count++;
	    	 if(count%4==0){
	    		 System.out.print( "  ");
	    	 }
	    	 if(count%16==0){
	    		 System.out.println();
	    	 }
	      }
	      System.out.println();
    }
}
