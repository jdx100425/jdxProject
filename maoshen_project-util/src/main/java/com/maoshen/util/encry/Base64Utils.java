package com.maoshen.util.encry;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.maoshen.util.Preconditions;

public class Base64Utils {

	private static final String CHARSET = "utf-8";

	public static String encode(String str, String charset) throws UnsupportedEncodingException,
			IllegalArgumentException {
		Preconditions.checkStrIsNotBank(str);
		Preconditions.checkStrIsNotBank(charset);
		return encode(str.getBytes(charset));
	}

	public static String encode(String str) throws UnsupportedEncodingException, IllegalArgumentException {
		return encode(str, CHARSET);
	}

	public static String encode(byte[] bytes) throws UnsupportedEncodingException, IllegalArgumentException {
		Preconditions.checkByteArray(bytes);
		return new sun.misc.BASE64Encoder().encode(bytes);
	}

	public static String decoder(String base64Str, String charset) throws UnsupportedEncodingException, IOException,
			IllegalArgumentException {
		Preconditions.checkStrIsNotBank(base64Str);
		Preconditions.checkStrIsNotBank(charset);
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		return new String(decoder.decodeBuffer(base64Str), charset);
	}

	public static String decoder(String base64Str) throws UnsupportedEncodingException, IOException,
			IllegalArgumentException {
		return decoder(base64Str, CHARSET);
	}
}