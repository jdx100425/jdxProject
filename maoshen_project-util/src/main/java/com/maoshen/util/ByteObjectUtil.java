package com.maoshen.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * 对象与byte[]之间的转换 
 */
public class ByteObjectUtil {

	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(ByteObjectUtil.class);

	/**
	 * 从字节数组获取对象
	 * 
	 * @param objBytes
	 *            字节数组
	 * @return 对象
	 */
	public static Object getObjectFromBytes(byte[] objBytes) {
		if (objBytes == null || objBytes.length == 0) {
			return null;
		}
		ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
		try {
			ObjectInputStream oi = new ObjectInputStream(bi);
			return oi.readObject();
		} catch (IOException e) {
			logger.error(e);
		} catch (ClassNotFoundException e) {
			logger.error(e);
		}
		return null;

	}

	/**
	 * 从对象获取一个字节数组
	 * 
	 * @param obj
	 *            对象
	 * @return 字节数组
	 */
	public static byte[] getBytesFromObject(Object obj) {
		if (obj == null) {
			return null;
		}
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = null;
		try {
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
		} catch (IOException e) {
			logger.error(e);
		}

		return bo.toByteArray();
	}

	/**
	 * inputStream转化成byte[]
	 * 
	 * @param is
	 *            inputStream
	 * @return byte[]
	 */
	public static byte[] inputStreamToByte(InputStream is) {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		try {
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (IOException e) {
			logger.error("inputStream转成byte[]时出错.", e);
		}
		return null;

	}

	/**
	 * String to InputStream
	 * @param str 字符串
	 * @return InputStrem
	 */
	public static InputStream string2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

}
