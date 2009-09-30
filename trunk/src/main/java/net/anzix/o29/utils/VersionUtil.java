package net.anzix.o29.utils;

/**
 * Version utility. The only thing to do with this is to provide application
 * 
 * @author kocka
 */
public class VersionUtil {
	private static final String versionUrl = "$HeadURL$";
	private static final String revision = "$Revision$";
	private static final String date = "$Date$";

	public final static String getVersionNumber() {
		final int srcRootPos = versionUrl.indexOf("/src/", 0);
		final int lastSlash = versionUrl.lastIndexOf("/", srcRootPos - 1);
		return versionUrl.substring(lastSlash + 1, srcRootPos);
	}
}
