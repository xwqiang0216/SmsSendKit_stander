package utils;

import java.io.IOException;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class DecryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	@Override
	protected String convertProperty(String propertyName, String propertyValue){
		String result = propertyValue;
		if(isEncryptPropertyVal(propertyName)){
			//调用解密方法
		    try {
				result = DesUtils.decrypt(propertyValue);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}  
        }
		return result;
	}

	private boolean isEncryptPropertyVal(String propertyName) {
		boolean result = false;
		if(propertyName != null && propertyName.startsWith("encrypt")){
			result = true;
		}
		return result;
	}

}
