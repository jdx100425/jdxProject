package com.maoshen.util.algorithm;

import java.util.UUID;

public class UUIDGenerator {

	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.replaceAll("-", "");
	}
}