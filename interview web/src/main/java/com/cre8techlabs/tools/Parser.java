/*************************************************************************
 * 
 * Cre8Tech Labs CONFIDENTIAL
 * __________________
 * 
 *  [2015] - [2015] Cre8Tech Labs 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Cre8Tech Labs and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Cre8Tech Labs
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Cre8Tech Labs.
 */
package com.cre8techlabs.tools;

public class Parser {
	static final String Digits     = "(\\p{Digit}+)";
    static final String HexDigits  = "(\\p{XDigit}+)";
            // an exponent is 'e' or 'E' followed by an optionally 
            // signed decimal integer.
    static final String Exp        = "[eE][+-]?"+Digits;
    static final String fpRegex    =
                ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
                 "[+-]?(" + // Optional sign character
                 "NaN|" +           // "NaN" string
                 "Infinity|" +      // "Infinity" string

                 // A decimal floating-point string representing a finite positive
                 // number without a leading sign has at most five basic pieces:
                 // Digits . Digits ExponentPart FloatTypeSuffix
                 // 
                 // Since this method allows integer-only strings as input
                 // in addition to strings of floating-point literals, the
                 // two sub-patterns below are simplifications of the grammar
                 // productions from the Java Language Specification, 2nd 
                 // edition, section 3.10.2.

                 // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                 "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

                 // . Digits ExponentPart_opt FloatTypeSuffix_opt
                 "(\\.("+Digits+")("+Exp+")?)|"+

           // Hexadecimal strings
           "((" +
            // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HexDigits + "(\\.)?)|" +

            // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

            ")[pP][+-]?" + Digits + "))" +
                 "[fFdD]?))" +
                 "[\\x00-\\x20]*");// Optional trailing "whitespace"
    
    public static boolean isFloat(String str) {
    	return str.matches(fpRegex) || str.matches("[(]" + fpRegex + "[)]");
    }
    public static double parseDouble(String str) {
    	if (str.matches("[(]" + fpRegex + "[)]")) {
    		str = "-" + str.replaceAll("[()]", "");
    	}
    	return Double.parseDouble(str);
    }
    public static void main(String[] args) {
	    
	    
    }
    
    
    public static char cleanV2(String str) {
    	if (str == null || str.isEmpty()) return 0;
    	
    	char _char = str.charAt(0);
    		
        if (_char == 8208) {
        	_char = 45;
        } else if (Character.isWhitespace(_char) || ((int)_char) == 160) {
        	_char = ' ';
        }
        
        if (_char != 173) {
        	return _char;
        }
    	return 0;
    }
    
    public static String clean(String str) {

    	char[] chars = str.toCharArray();
    	StringBuffer b = new StringBuffer();
    	for (int i = 0; i < chars.length; i++) {
    		
            if (((int)chars[i]) == 8208) {
            	chars[i] = 45;
            }
            if (Character.isWhitespace(chars[i]) || ((int)chars[i]) == 160) {
            	chars[i] = ' ';
            }
            if (chars[i] != 173) {
            	b.append(chars[i]);
            }
		}
    	
    	return b.toString();
    }
	public static boolean isInteger(String text) {
		return text.matches("^[0-9]+$");
	}
}
