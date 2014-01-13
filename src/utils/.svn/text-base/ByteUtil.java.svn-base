package utils;

import java.nio.ByteBuffer;

public class ByteUtil {
	
	public static final String bytesToHexString(ByteBuffer bb) {
		byte[] bArray = bb.array();
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			if(i != 0 && i % 4 == 0){
				sb.append("\t");
			}
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase()).append(" ");
		}
		return sb.toString();
	}
}
