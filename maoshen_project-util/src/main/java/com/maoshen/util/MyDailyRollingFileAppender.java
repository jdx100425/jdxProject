package com.maoshen.util;

import java.io.File;

import org.apache.log4j.DailyRollingFileAppender;

public class MyDailyRollingFileAppender extends DailyRollingFileAppender {
	public void setFile(String file) {
		String filePath = file;
		File fileCheck = new File(filePath);
		if (!fileCheck.exists())
			fileCheck.getParentFile().mkdirs();
		super.setFile(filePath);
	}
}
