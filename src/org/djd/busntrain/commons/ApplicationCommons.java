package org.djd.busntrain.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

public final class ApplicationCommons {

  public static final String SCHEME = "content://";

  public static final String DATABASE_NAME = "busntrain.db";
  public static final int DATABASE_VERSION = 2;

  public static final String PREFERENCE_FILE_NAME = "BUSNTRAIN_APPLICATION_PREFERENCE_FILE_NAME";
  public static final String PREFERENCE_KEY_BUS_ROUTE_LAST_UPDATE_TIME = "PREFERENCE_KEY_BUS_ROUTE_LAST_UPDATE_TIME";

  public static final long ONE_DAY_IN_MILLISECONDS = 86400000;
  public static final long ONE_WEEK_IN_MILLISECONDS = ONE_DAY_IN_MILLISECONDS * 7;
  public static final long ONE_MONTH_IN_MILLISECONDS = ONE_WEEK_IN_MILLISECONDS * 30;
  public static final long ONE_YEAR_IN_MILLISECONDS = ONE_WEEK_IN_MILLISECONDS * 52;

  public static boolean isMoreThanOneYearOld(long lastUpateTime) {
    long diff = System.currentTimeMillis() - lastUpateTime;
    return ONE_YEAR_IN_MILLISECONDS < diff;
  }

  public static boolean isMoreThanOneDayOld(long lastUpateTime) {
    long diff = System.currentTimeMillis() - lastUpateTime;
    return ONE_DAY_IN_MILLISECONDS < diff;
  }

  public static long getBusRouteLastUpdate(final Context context) {
    SharedPreferences settings = getSharedPreferences(context);
    return settings.getLong(PREFERENCE_KEY_BUS_ROUTE_LAST_UPDATE_TIME, 0);
  }

  public static void setBusRouteLastUpdate(final Context context) {
    SharedPreferences settings = getSharedPreferences(context);
    settings.edit().putLong(PREFERENCE_KEY_BUS_ROUTE_LAST_UPDATE_TIME, System.currentTimeMillis()).commit();
  }


  public static void setTextToTextView(View view, int targetResourceId, String text) {
    TextView stopNameTextView = (TextView) view.findViewById(targetResourceId);
    if (stopNameTextView != null) {
      stopNameTextView.setText(text);
    }
  }

  public static void setTextToTextView(View view, int targetResourceId, int text) {
    setTextToTextView(view, targetResourceId, String.valueOf(text));
  }

  private static SharedPreferences getSharedPreferences(final Context context) {
    return context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
  }
}
