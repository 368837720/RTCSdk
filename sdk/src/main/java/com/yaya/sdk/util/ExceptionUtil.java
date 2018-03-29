package com.yaya.sdk.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by wy on 14-3-11.
 */
public class ExceptionUtil {
	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}
