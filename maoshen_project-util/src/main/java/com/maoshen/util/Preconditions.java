package com.maoshen.util;

import java.io.File;

import org.apache.commons.lang.StringUtils;

/**
 * 参数检测工具类
 * 
 * @author seara
 * @Email Zhixiang.tian@vipshop.com
 * @Date May 4, 2014 5:47:03 PM
 */
public class Preconditions {

	public static void checkArgument(boolean expression) {
		if (!expression) {
			throw new IllegalArgumentException();
		}
	}

	public static void checkArgument(boolean expression, Object errorMessage) {
		if (!expression) {
			throw new IllegalArgumentException(String.valueOf(errorMessage));
		}
	}

	// ----------------------------------------------------------------------

	public static void checkNotNull(Object reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
	}

	public static void checkNotNull(Object reference, Object errorMessage) {
		if (reference == null) {
			throw new NullPointerException(String.valueOf(errorMessage));
		}
	}

	public static void checkNotNull(Object reference, String errorMessageTemplate, Object... errorMessageArgs) {
		if (reference == null) {
			throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
		}
	}

	// ----------------------------------------------------------------------
	public static void checkStrIsNotBank(String arg, Object errorMessage) {
		if (StringUtils.isBlank(arg)) {
			throw new IllegalArgumentException(String.valueOf(errorMessage));
		}
	}

	public static void checkStrIsNotBank(String arg, String errorMessageTemplate, Object... errorMessageArgs) {
		if (StringUtils.isBlank(arg)) {
			throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
		}
	}

	public static void checkStrIsNotBank(String arg) {
		if (StringUtils.isBlank(arg)) {
			throw new IllegalArgumentException();
		}
	}

	// ----------------------------------------------------------------------

	public static void checkFileIsExists(File file) {
		checkNotNull(file);
		if (!file.exists()) {
			throw new IllegalArgumentException();
		}
	}

	public static void checkFileIsExists(File file, Object errorMessage) {
		checkNotNull(file, errorMessage);
		if (!file.exists()) {
			throw new IllegalArgumentException(String.valueOf(errorMessage));
		}
	}

	public static void checkFileIsExists(File file, String errorMessageTemplate, Object... errorMessageArgs) {
		checkNotNull(file, errorMessageTemplate, errorMessageArgs);
		if (!file.exists()) {
			throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
		}
	}

	// ----------------------------------------------------------------------

	public static void checkByteArray(byte[] bytes) {
		checkNotNull(bytes);
		if (bytes.length < 1) {
			throw new IllegalArgumentException();
		}
	}

	public static void checkByteArray(byte[] bytes, Object errorMessage) {
		checkNotNull(bytes, errorMessage);
		if (bytes.length < 1) {
			throw new IllegalArgumentException(String.valueOf(errorMessage));
		}
	}

	public static void checkByteArray(byte[] bytes, String errorMessageTemplate, Object... errorMessageArgs) {
		checkNotNull(bytes, errorMessageTemplate, errorMessageArgs);
		if (bytes.length < 1) {
			throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
		}
	}

	// ----------------------------------------------------------------------
	/**
	 * 输出模板,采用%s做占位符,类似于string的模板
	 * 
	 * @author seara
	 * @Email Zhixiang.tian@vipshop.com
	 * @Date May 4, 2014 6:29:54 PM
	 * @param template
	 *            模板,如,x=%s,y=%s
	 * @param args
	 * @return
	 */
	private static String format(String template, Object... args) {
		template = String.valueOf(template);

		StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
		int templateStart = 0;
		int i = 0;
		while (i < args.length) {
			int placeholderStart = template.indexOf("%s", templateStart);
			if (placeholderStart == -1) {
				break;
			}
			builder.append(template.substring(templateStart, placeholderStart));
			builder.append(args[i++]);
			templateStart = placeholderStart + 2;
		}
		builder.append(template.substring(templateStart));

		if (i < args.length) {
			builder.append(" [");
			builder.append(args[i++]);
			while (i < args.length) {
				builder.append(", ");
				builder.append(args[i++]);
			}
			builder.append(']');
		}

		return builder.toString();
	}
}