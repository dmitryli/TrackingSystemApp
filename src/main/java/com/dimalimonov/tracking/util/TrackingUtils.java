package com.dimalimonov.tracking.util;

public class TrackingUtils {

	public static long timeInHoursToMillis(Integer time) {
		return time * 60 * 60 * 1000;
	}
	public static long timeInMinutesToMillis(Integer time) {
		return time  * 60 * 1000;
	}
}
