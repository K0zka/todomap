package org.todomap.feed.utils;

public class StringUtils {
	public static String max(final String orig, final int max) {
		if (orig == null) {
			return null;
		}
		if (orig.length() > max) {
			if (max > 3) {
				return orig.substring(0, max - 3) + "...";
			} else {
				return orig.substring(0, 1) + "~";
			}
		} else {
			return orig;
		}
	}
}
