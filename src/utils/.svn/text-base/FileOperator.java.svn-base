package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileOperator {

	public static final String GBK = "gbk";
	public static final String UTF8 = "utf-8"; 
	/**
	 * 以行为单位读取文件，拿到字符串列表
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<String> readFileByLines(String fileName,String encoding) {
		List<String> ls = new ArrayList<String>();

		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), encoding);
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
//				 System.out.println("line " + line + ": " + tempString);
				ls.add(tempString);
				line++;
			}
			reader.close();
			return ls;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return null;
	}

	
	
	public static void main(String[] args) {
//		FileOperator.split("./808504_921.txt", "gbk");
	}
	
	public static List<String> readFileByLines(String fileName){
		return readFileByLines(fileName,FileOperator.UTF8);
	}
	/**
	 * 将文件转换成字符串
	 * 
	 * @param fileName
	 * @return
	 */
	/**
	 * 以行为单位读取文件，将文件内容转换成字符串
	 * 
	 * @param fileName
	 * @param isTrim
	 *            读取每行时是否trim()
	 * @return
	 */

	public static String fileToString(String fileName, boolean isTrim,String encoding) {

		String result = null;
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), encoding);
			reader = new BufferedReader(isr);

			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
//				System.out.println("line " + line + ": " + tempString);
				if (isTrim) {
					tempString = tempString.trim();
				}

				sb.append(tempString + "\n");
				line++;
			}
			result = new String(sb);
			reader.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return null;
	}

	/**
	 * 默认用utf-8字符编码
	 * @param fileName
	 * @param isTrim
	 * @return
	 */
	public static String fileToString(String fileName, boolean isTrim) {
		return fileToString(fileName, isTrim, FileOperator.UTF8);
	}
	/**
	 * 根据指定正则切分文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<String> splitFile(String fileName, String regex,String encoding) {
		List<String> ls = new ArrayList<String>();

		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
//			 System.out.println("以行为单位读取文件内容，一次读一整行：");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), encoding);
			reader = new BufferedReader(isr);
			


			
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
//				 System.out.println("line " + line + ": " + tempString);

				if (tempString.matches(regex)) {
					String each = new String(sb);
					if (line != 1)
						ls.add(each);
					sb = new StringBuffer();

				}
				sb.append(tempString + "\n");
				line++;
			}

			String each = new String(sb);
			ls.add(each);

			reader.close();
			return ls;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return ls;
	}
}
