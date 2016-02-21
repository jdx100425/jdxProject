package com.maoshen.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

public class NumberUtility {
    /**
     * Compares two doubles for order.
     * 
     * @param first the first double
     * @param second the second double
     * @return -1 if first is less, +1 if greater, 0 if equal to second
     */
    public static int compare(double first, double second) {
        if (first < second) {
            return -1;
        }
        if (first > second) {
            return +1;
        }

        long firstBits = Double.doubleToLongBits(first);

        long secondBits = Double.doubleToLongBits(second);

        if (firstBits == secondBits) {
            return 0;
        }

        if (firstBits < secondBits) {
            return -1;
        } else {
            return +1;
        }
    }

    /**
     * Compares two floats for order.
     * 
     * @param first the first float
     * @param second the second float
     * @return -1 if first is less, +1 if greater, 0 if equal to second
     */
    public static int compare(float first, float second) {
        if (first < second) {
            return -1;
        }

        if (first > second) {
            return +1;
        }

        int firstBits = Float.floatToIntBits(first);

        int secondBits = Float.floatToIntBits(second);

        if (firstBits == secondBits) {
            return 0;
        }

        if (firstBits < secondBits) {
            return -1;
        } else {
            return +1;
        }
    }

    /**
     * Convert a String to a BigDecimal.
     * 
     * @param str a String to convert
     * @return converted BigDecimal, null if the value cannot be converted
     */
    public static BigDecimal createBigDecimal(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        return new BigDecimal(str);
    }

    /**
     * Convert a String to a BigInteger.
     * 
     * @param str a String to convert
     * @return converted BigInteger, null if the value cannot be converted
     */
    public static BigInteger createBigInteger(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        return new BigInteger(str);
    }

    /**
     * Formats a float to produce a string.
     * 
     * @param number The float to format
     * @param pattern A non-localized pattern string
     * @return The formatted number string, null if the pattern is empty
     */
    public static String format(float number, String pattern) {
        if (pattern == null || pattern.length() == 0) {
            return null;
        }

        DecimalFormat formatter = new DecimalFormat(pattern);

        return formatter.format(number);
    }

    /**
     * Formats a Float to produce a string.
     * 
     * @param number The Float to format
     * @param pattern A non-localized pattern string
     * @return The formatted number string, null if the pattern is empty
     */
    public static String format(Float number, String pattern) {
        if (number == null || pattern == null || pattern.length() == 0) {
            return null;
        }

        DecimalFormat formatter = new DecimalFormat(pattern);

        return formatter.format(number);
    }

    /**
     * Formats a double to produce a string.
     * 
     * @param number The double to format
     * @param pattern A non-localized pattern string
     * @return The formatted number string, null if the pattern is empty
     */
    public static String format(double number, String pattern) {
        if (pattern == null || pattern.length() == 0) {
            return null;
        }

        DecimalFormat formatter = new DecimalFormat(pattern);

        return formatter.format(number);
    }

    /**
     * Formats a Double to produce a string.
     * 
     * @param number The Double to format
     * @param pattern A non-localized pattern string
     * @return The formatted number string, null if the pattern is empty
     */
    public static String format(Double number, String pattern) {
        if (number == null || pattern == null || pattern.length() == 0) {
            return null;
        }

        DecimalFormat formatter = new DecimalFormat(pattern);

        return formatter.format(number);
    }

    /**
     * Formats a int to produce a string.
     * 
     * @param number The int to format
     * @param pattern A non-localized pattern string
     * @return The formatted number string, null if the pattern is empty
     */
    public static String format(int number, String pattern) {
        if (pattern == null || pattern.length() == 0) {
            return null;
        }

        DecimalFormat formatter = new DecimalFormat(pattern);

        return formatter.format(number);
    }

    /**
     * Formats a Integer to produce a string.
     * 
     * @param number The Integer to format
     * @param pattern A non-localized pattern string
     * @return The formatted number string, null if the pattern is empty
     */
    public static String format(Integer number, String pattern) {
        if (number == null || pattern == null || pattern.length() == 0) {
            return null;
        }

        DecimalFormat formatter = new DecimalFormat(pattern);

        return formatter.format(number);
    }

    /**
     * Formats a long to produce a string.
     * 
     * @param number The long to format
     * @param pattern A non-localized pattern string
     * @return The formatted number string, null if the pattern is empty
     */
    public static String format(long number, String pattern) {
        if (pattern == null || pattern.length() == 0) {
            return null;
        }

        DecimalFormat formatter = new DecimalFormat(pattern);

        return formatter.format(number);
    }

    /**
     * Formats a Long to produce a string.
     * 
     * @param number The Long to format
     * @param pattern A non-localized pattern string
     * @return The formatted number string, null if the pattern is empty
     */
    public static String format(Long number, String pattern) {
        if (number == null || pattern == null || pattern.length() == 0) {
            return null;
        }

        DecimalFormat formatter = new DecimalFormat(pattern);

        return formatter.format(number);
    }

    /**
     * Checks whether the String contains only digit characters.
     * 
     * @param str the String to check
     * @return true if str contains only unicode numeric
     */
    public static boolean isDigits(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether the String a valid Java number.
     * 
     * @param str the String to check
     * @return true if the string is a correctly formatted number
     */
    public static boolean isNumber(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        char[] chars = str.toCharArray();

        int sz = chars.length;

        boolean hasExp = false;
        boolean hasDec = false;
        boolean allowSigns = false;
        boolean foundDigit = false;

        int start = (chars[0] == '-') ? 1 : 0;

        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;

                if (i == sz) {
                    return false;
                }

                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }

        sz--;

        int i = start;

        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;
            } else if (chars[i] == '.') {
                if (hasDec || hasExp) {
                    return false;
                }
                hasDec = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                if (hasExp) {
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false;
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                return false;
            }
            if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                return foundDigit && !hasExp;
            }
            return false;
        }
        return !allowSigns && foundDigit;
    }

    /**
     * Returns the maximum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static double max(double[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        double max = array[0];

        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * Returns the maximum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static float max(float[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        float max = array[0];

        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * Returns the maximum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static int max(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        int max = array[0];

        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * Returns the maximum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static long max(long[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        long max = array[0];

        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * Returns the maximum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static short max(short[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        short max = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    /**
     * Returns the minimum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static double min(double[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        double min = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    /**
     * Returns the minimum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static float min(float[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        float min = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    /**
     * Returns the minimum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static int min(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        int min = array[0];

        for (int j = 1; j < array.length; j++) {
            if (array[j] < min) {
                min = array[j];
            }
        }

        return min;
    }

    /**
     * Returns the minimum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static long min(long[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        long min = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    /**
     * Returns the minimum value in an array.
     * 
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if array is empty
     */
    public static short min(short[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("The array cannot be empty");
        }

        short min = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    /**
     * Convert a String to a byte, returning zero if the conversion fails.
     * 
     * @param str the string to convert, may be null
     * @return the byte represented by the string, or zero if conversion fails
     */
    public static byte toByte(String str) {
        return toByte(str, 10, (byte) 0);
    }

    /**
     * Convert a String to a byte, returning a default value if the conversion
     * fails.
     * 
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the byte represented by the string, or the default if conversion
     * fails
     */
    public static byte toByte(String str, byte defaultValue) {
        return toByte(str, 10, defaultValue);
    }

    /**
     * Convert a String to a byte in the radix specified, returning zero if the
     * conversion fails.
     * 
     * @param str the string to convert, may be null
     * @param radix the radix to be used while parsing str
     * @return the byte represented by the string, or zero if conversion fails
     */
    public static byte toByte(String str, int radix) {
        return toByte(str, radix, (byte) 0);
    }

    /**
     * Convert a String to a byte in the radix specified, returning a default
     * value if the conversion fails.
     * 
     * @param str the string to convert, may be null
     * @param radix the radix to be used while parsing str
     * @param defaultValue the default value
     * @return the byte represented by the string, or the default if conversion
     * fails
     */
    public static byte toByte(String str, int radix, byte defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(str, radix);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Convert a String to a double, returning 0.0d if the conversion fails.
     * 
     * @param str the string to convert, may be null
     * @return the double represented by the string, or 0.0d if conversion fails
     */
    public static double toDouble(String str) {
        return toDouble(str, 0.0d);
    }

    /**
     * Convert a String to a double, returning a default value if the conversion
     * fails.
     * 
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the double represented by the string, or defaultValue if
     * conversion fails
     */
    public static double toDouble(String str, double defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Convert a String to a float, returning 0.0f if the conversion fails.
     * 
     * @param str the string to convert, may be null
     * @return the float represented by the string, or 0.0f if conversion fails
     */
    public static float toFloat(String str) {
        return toFloat(str, 0.0f);
    }

    /**
     * Convert a String to a float, returning a default value if the conversion
     * fails.
     * 
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the float represented by the string, or defaultValue if
     * conversion fails
     */
    public static float toFloat(String str, float defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Convert a String to an int, returning zero if the conversion fails.
     * 
     * @param str the string to convert, may be null
     * @return the int represented by the string, or zero if conversion fails
     */
    public static int toInt(String str) {
        return toInt(str, 0);
    }

    /**
     * Convert a String to an int, returning a default value if the conversion
     * fails.
     * 
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion
     * fails
     */
    public static int toInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Convert a String to a long, returning zero if the conversion fails.
     * 
     * @param str the string to convert, may be null
     * @return the long represented by the string, or 0 if conversion fails
     */
    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    /**
     * Convert a String to a long, returning a default value if the conversion
     * fails.
     * 
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the long represented by the string, or the default if conversion
     * fails
     */
    public static long toLong(String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }}
