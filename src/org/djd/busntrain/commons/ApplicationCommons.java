package org.djd.busntrain.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.djd.busntrain.R;

public final class ApplicationCommons {
  private static final String TAG = ApplicationCommons.class.getSimpleName();

  public static final String HEROKU_URL = "http://shielded-taiga-4473.herokuapp.com/v1/";
  public static final String URL_STATIONS_TXT = HEROKU_URL + "stations/";
  public static final String URL_STOPS_TXT = HEROKU_URL + "stops/";

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

  /**
   * Red = Red Line (Howard-95th/Dan Ryan service)
   * Blue = Blue Line (O‖Hare-Forest Park service)
   * Brn = Brown Line (Kimball-Loop service)
   * G = Green Line (Harlem/Lake-Ashland/63rd-Cottage Grove service)
   * Org = Orange Line (Midway-Loop service)
   * P = Purple Line (Linden-Howard shuttle service)
   * Pink = Pink Line (54th/Cermak-Loop service)
   * Y = Yellow Line (Skokie-Howard [Skokie Swift] shuttle service)
   */
  public static String getColorCode(int viewId) {
    switch (viewId) {
      case R.id.train_route_red_id: return "Red";
      case R.id.train_route_blue_id: return "Blue";
      case R.id.train_route_brown_id: return "Brn";
      case R.id.train_route_green_id: return "G";
      case R.id.train_route_orange_id: return "O";
      case R.id.train_route_purple_id: return "P";
      case R.id.train_route_pink_id: return "Pink";
      case R.id.train_route_yellow_id: return "Y";
      default:
        Log.e(TAG, "Undefined viewId: " + viewId);
        return "";
    }
  }


  public enum ColorCode {
    Red(R.color.train_red), Blue(R.color.train_blue),
    Brn(R.color.train_brown),G(R.color.train_green), Org(R.color.train_orange),
    P(R.color.train_purple), Pink(R.color.train_pink), Y(R.color.train_yellow);

    final int colorId;

    ColorCode(int colorId) {
      this.colorId = colorId;
    }
  }

  public static int getColorIdByCode(String colorCode) {
    return ColorCode.valueOf(colorCode).colorId;
  }

  public static String getColorDestination(int viewId) {
    switch (viewId) {
      case R.id.train_route_red_id: return "Red/95th";
      case R.id.train_route_blue_id: return "Blue/ForestPark";
      case R.id.train_route_brown_id: return "Brown/Loop";
      case R.id.train_route_green_id: return "Green/63rd";
      case R.id.train_route_orange_id: return "Orange/Loop";
      case R.id.train_route_purple_id: return "PurpleExp/Linden";
      case R.id.train_route_pink_id: return "Pink/Loop";
      case R.id.train_route_yellow_id: return "Yellow/Howard";
      default:
        Log.e(TAG, "Undefined viewId: " + viewId);
        return "";
    }
  }

  private static SharedPreferences getSharedPreferences(final Context context) {
    return context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
  }
}
