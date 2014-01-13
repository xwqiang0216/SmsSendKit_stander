package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * File: PatternTool.java
 * <P>
 * Description: -none-
 * <P>
 * <B>Change History :</B>
 * <P>
 * <ul>
 * <li>2008-6-27 Created by vivo</li>
 * <li>2008-6-27 Modified by vivo</li>
 * </ul>
 * 
 * @author GaoBo
 */
public class PatternTool {
	/**
	 * Find All Occurrence of Matches. Get indicated group.<BR>
	 * 
	 * @param str
	 * @param patternString
	 * @param maxLine Default 0 (unlimited if less then 0)
	 * @param groupIndex Default 1.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String[]> findAllMatches(String str, Pattern compliedPattern, int maxLine,
			int... groupIndex) {
		assert null == compliedPattern : "CompiledPattern Can't be null";

		if (str == null || str.trim().equals("")) {
			return Collections.EMPTY_LIST;
		}

		List<String[]> result = new ArrayList<String[]>(maxLine);

		String string = str /* .replaceAll("\\$", "\\&") */;
		Matcher matcher = compliedPattern.matcher(string);
		int i = 0;
		while (matcher.find() && (maxLine <= 0 || i++ < maxLine)) {
			String[] term = new String[groupIndex.length];
			Arrays.fill(term, "");

			for (int j = 0; j < groupIndex.length; j++) {
				int index = groupIndex[j];
				if (matcher.groupCount() >= index) {
					term[j] = matcher.group(index);
				}
			}
			result.add(term);
		}
		return result;
	}

	public static List<String[]> findAllMatches(String str, String patternString, int maxLine,
			int... groupIndex) {
		assert null == patternString || "".equals(patternString.trim()) : "CompiledPattern Can't be null";
		Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		return findAllMatches(str, pattern, maxLine, groupIndex);
	}

	/**
	 * Find One Occurrence of Matches. Get indicated group.
	 * 
	 * @param str
	 * @param patternString
	 * @param maxLine
	 * @param groupIndex
	 * @return
	 */
	public static String[] findMatches(String str, Pattern compiledPattern, int... groupIndex) {
		List<String[]> result = findAllMatches(str, compiledPattern, 1, groupIndex);
		if (null == result || result.size() == 0) {
			return new String[] {};
		}
		if (result.size() > 1) {
			System.err.println("PatternTool : Get too many results!");
		}
		return result.get(0);
	}

	public static String[] findMatches(String str, String patternString, int... groupIndex) {
		assert null == patternString || "".equals(patternString.trim()) : "CompiledPattern Can't be null";
		Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		return findMatches(str, pattern, groupIndex);
	}

	/*
	 * Others
	 */

	public static String getMatch(String str, Pattern compiledPattern) {
		List<String[]> result = findAllMatches(str, compiledPattern, 1, 1);
		if (result == null || result.size() == 0 || result.get(0).length == 0) {
			return null;
		}
		return result.get(0)[0];
	}

	public static String getMatch(String str, String patternString) {
		return getMatch(str, Pattern.compile(patternString, Pattern.CASE_INSENSITIVE));
	}

	public static String getMatch(String str, Pattern compiledPattern, int groutIndex) {
		List<String[]> viaPattern = findAllMatches(str, compiledPattern, 1, groutIndex);
		if (viaPattern == null || viaPattern.size() == 0 || viaPattern.get(0).length == 0) {
			return null;
		}
		return viaPattern.get(0)[0];
	}

	public static String getMatch(String str, String patternString, int groutIndex) {
		return getMatch(str, Pattern.compile(patternString, Pattern.CASE_INSENSITIVE), groutIndex);
	}

	public static String[] getMatches(String str, Pattern compiledPattern, int groutIndex) {
		List<String[]> viaPattern = findAllMatches(str, compiledPattern, 0, groutIndex);
		if (viaPattern == null || viaPattern.size() == 0 || viaPattern.get(0).length == 0) {
			return null;
		}
		String[] result = new String[viaPattern.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = viaPattern.get(i)[0];
		}
		return result;
	}

	public static String[] getMatches(String str, String patternString, int groutIndex) {
		return getMatches(str, Pattern.compile(patternString, Pattern.CASE_INSENSITIVE), groutIndex);
	}


	public static String replaceA2B(String a, String b, String replaceStr) {
		Pattern p = Pattern.compile(a);
		Matcher m = p.matcher(replaceStr);
		String result = m.replaceAll(b);
		return result;
	}
}
