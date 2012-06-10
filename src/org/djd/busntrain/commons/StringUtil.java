package org.djd.busntrain.commons;

import org.djd.busntrain.R;

import android.content.Context;
import android.text.format.DateUtils;

public class StringUtil {

	public static String timeToString(Context context, long time) {
		return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_SHOW_DATE + DateUtils.FORMAT_SHOW_TIME
		     );

	}

	public static String getBusRouteUrl(Context context) {
		String url = context.getString(R.string.bus_tracker_base_url);
		String key = context.getString(R.string.bus_tracker_auth_key);
		return String.format("%sgetroutes?key=%s", url, key);
	}

	public static String getBusDirectionUrl(Context context, String route) {
		String url = context.getString(R.string.bus_tracker_base_url);
		String key = context.getString(R.string.bus_tracker_auth_key);
		return String.format("%sgetdirections?key=%s&rt=%s", url, key, route);
	}

	public static String getBusStopUrl(Context context, String route, String direction) {
		String url = context.getString(R.string.bus_tracker_base_url);
		String key = context.getString(R.string.bus_tracker_auth_key);
		direction = direction.replace(" ", "%20");
		return String.format("%sgetstops?key=%s&rt=%s&dir=%s", url, key, route, direction);
	}

	public static String getBusPredictionUrl(Context context, String route, String stopId) {
		String url = context.getString(R.string.bus_tracker_base_url);
		String key = context.getString(R.string.bus_tracker_auth_key);
		return String.format("%sgetpredictions?key=%s&rt=%s&stpid=%s", url, key, route, stopId);
	}

}
