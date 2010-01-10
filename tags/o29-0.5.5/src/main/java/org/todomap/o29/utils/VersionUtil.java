package org.todomap.o29.utils;

/**
 * Version utility. The only thing to do with this is to provide application
 * 
 * @author kocka
 */
public class VersionUtil {
	private static final String versionUrl = "$HeadURL$";

	public final static String getVersionNumber() {
		final int srcRootPos = versionUrl.indexOf("/src/", 0);
		final int lastSlash = versionUrl.lastIndexOf("/", srcRootPos - 1);
		return versionUrl.substring(lastSlash + 1, srcRootPos);
	}

}
