package com.cre8techlabs.tools;

public class StringTools {

	public static String unCamel(String str) {
		return unCamel(str, ' ', true, true);
	}

	public static String unCamel(String str, char seperator,
	        boolean lowercase) {
		return unCamel(str, seperator, lowercase, true);
	}
		
	public static String unCamel(String str, char seperator,
	        boolean lowercase, boolean firstCharUpper) {
		char[] ca = str.toCharArray();
		if (3 > ca.length) {
			return lowercase ? str.toLowerCase() : str;
		}
		// about five seperator
		StringBuilder build = new StringBuilder(ca.length + 5);
		build.append(lowercase ? Character.toLowerCase(ca[0]) : ca[0]);

		boolean lower1 = Character.isLowerCase(ca[0]);
		int i = 1;
		while (i < ca.length - 1) {
			char cur = ca[i];
			char next = ca[i + 1];
			boolean upper2 = Character.isUpperCase(cur);
			boolean lower3 = Character.isLowerCase(next);
			if (lower1 && upper2 && lower3) {
				build.append(seperator);
				build.append(lowercase ? Character.toLowerCase(cur) : cur);
				build.append(next);
				i += 2;
			} else {
				if (lowercase && upper2) {
					build.append(Character.toLowerCase(cur));
				} else {
					build.append(cur);
				}
				lower1 = !upper2;
				i++;
			}
		}
		if (i == ca.length - 1) {
			build.append(lowercase ? Character.toLowerCase(ca[i]) : ca[i]);
		}
		if (firstCharUpper) {
			build.replace(0, 1, Character.toUpperCase(build.charAt(0)) + "");
		}
		return build.toString();
	}
	public static String caseCamel(String name, boolean firstUpper) {

		StringBuilder buffer = new StringBuilder();

		String[] tokens = name.split("_");

		for (String token : tokens) {
			if (token.isEmpty()) {
				continue;
			}
			char[] chars = token.toCharArray();
			boolean continueToConvertToLower = true;
			boolean isLowerMeet = false;

			for (int i = 0; i < chars.length; i++) {
				char ch = chars[i];

				if (i == 0) {
					ch = Character.toUpperCase(ch);

				} else if (continueToConvertToLower) {

					isLowerMeet = Character.isLowerCase(chars[i - 1]);

					if (!isLowerMeet) {
						if (Character.isUpperCase(ch) && (i + 1 < chars.length) && Character.isLowerCase(chars[i + 1])) {
							
						} else {
							ch = Character.toLowerCase(ch);
						}
					}
				}
				buffer.append(ch);
			}
		}
		if (!firstUpper)
			buffer.setCharAt(0, Character.toLowerCase(buffer.charAt(0)));
		else
			buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
			
		return buffer.toString();

	}

	public static String caseCamel(String name) {
		return caseCamel(name, false);
	}


}
