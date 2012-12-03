package org.djd.busntrain.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.djd.busntrain.R;

import java.util.HashMap;
import java.util.Map;

public final class ApplicationCommons {
  private static final String TAG = ApplicationCommons.class.getSimpleName();

  public static final String HEROKU_URL = "http://shielded-taiga-4473.herokuapp.com/v1/";
  public static final String URL_STATIONS_TXT = HEROKU_URL + "stations/";
  public static final String URL_STOPS_TXT = HEROKU_URL + "stops/";

  public static final String SCHEME = "content://";

  public static final String DATABASE_NAME = "busntrain.db";
  public static final int DATABASE_VERSION = 6;

  public static final String PREFERENCE_FILE_NAME = "BUSNTRAIN_APPLICATION_PREFERENCE_FILE_NAME";
  public static final String PREFERENCE_KEY_DATABASE_LAST_UPDATE_TIME = "PREFERENCE_KEY_DATABASE_LAST_UPDATE_TIME";
  public static final String PREFERENCE_KEY_BUS_ROUTE_LAST_UPDATE_TIME = "PREFERENCE_KEY_BUS_ROUTE_LAST_UPDATE_TIME";
  public static final String PREFERENCE_KEY_TRAIN_STATIONS_LAST_UPDATE_TIME = "PREFERENCE_KEY_TRAIN_STATIONS_LAST_UPDATE_TIME";
  public static final String PREFERENCE_KEY_TRAIN_STOPS_LAST_UPDATE_TIME = "PREFERENCE_KEY_TRAIN_STOPS_LAST_UPDATE_TIME";

  public static final long ONE_DAY_IN_MILLISECONDS = 86400000;
  public static final long ONE_WEEK_IN_MILLISECONDS = ONE_DAY_IN_MILLISECONDS * 7;
  public static final long ONE_MONTH_IN_MILLISECONDS = ONE_WEEK_IN_MILLISECONDS * 30;
  public static final long ONE_YEAR_IN_MILLISECONDS = ONE_WEEK_IN_MILLISECONDS * 52;

  public static final Map<ColorCode, String> COLOR_NAME_BY_COLOR_CODE;

  static {
    COLOR_NAME_BY_COLOR_CODE = new HashMap<ColorCode, String>(8);
    COLOR_NAME_BY_COLOR_CODE.put(ColorCode.Red, "Red");
    COLOR_NAME_BY_COLOR_CODE.put(ColorCode.Blue, "Blue");
    COLOR_NAME_BY_COLOR_CODE.put(ColorCode.Brn, "Brown");
    COLOR_NAME_BY_COLOR_CODE.put(ColorCode.G, "Green");
    COLOR_NAME_BY_COLOR_CODE.put(ColorCode.Org, "Orange");
    COLOR_NAME_BY_COLOR_CODE.put(ColorCode.P, "Purple");
    COLOR_NAME_BY_COLOR_CODE.put(ColorCode.Pink, "Pink");
    COLOR_NAME_BY_COLOR_CODE.put(ColorCode.Y, "Yellow");
  }

  public static final Map<String, ColorCode> COLOR_CODE_BY_COLOR_NAME;

  static {
    COLOR_CODE_BY_COLOR_NAME = new HashMap<String, ColorCode>(8);
    COLOR_CODE_BY_COLOR_NAME.put("Red", ColorCode.Red);
    COLOR_CODE_BY_COLOR_NAME.put("Blue", ColorCode.Blue);
    COLOR_CODE_BY_COLOR_NAME.put("Brown", ColorCode.Brn);
    COLOR_CODE_BY_COLOR_NAME.put("Green", ColorCode.G);
    COLOR_CODE_BY_COLOR_NAME.put("Orange", ColorCode.Org);
    COLOR_CODE_BY_COLOR_NAME.put("Purple", ColorCode.P);
    COLOR_CODE_BY_COLOR_NAME.put("Pink", ColorCode.Pink);
    COLOR_CODE_BY_COLOR_NAME.put("Yellow", ColorCode.Y);
  }

  private static final Map<String, String> TRAIN_DESTINATION_NAME;

  static {
    TRAIN_DESTINATION_NAME = new HashMap<String, String>(16);
    TRAIN_DESTINATION_NAME.put("ForestPark", "Forest Park");
    TRAIN_DESTINATION_NAME.put("OHare", "O'Hare");
    TRAIN_DESTINATION_NAME.put("95th", "95th/Dan Ryan");
    TRAIN_DESTINATION_NAME.put("Howard", "Howard");
  }

  public static boolean isMoreThanOneYearOld(long lastUpateTime) {
    long diff = System.currentTimeMillis() - lastUpateTime;
    return ONE_YEAR_IN_MILLISECONDS < diff;
  }

  public static boolean isMoreThanOneDayOld(long lastUpateTime) {
    long diff = System.currentTimeMillis() - lastUpateTime;
    return ONE_DAY_IN_MILLISECONDS < diff;
  }

  public static boolean isMoreThanEnoughOld(long lastUpateTime) {
    Log.w(TAG, "Don't forget to change to isMoreThanOneYearOld after finish debugging!!");
    return true;
  }

  public static long getBusRouteLastUpdate(final Context context) {
    checkDatabaseChanged(context);
    return getSharedPreferences(context).getLong(PREFERENCE_KEY_BUS_ROUTE_LAST_UPDATE_TIME, 0);
  }

  public static void setBusRouteLastUpdate(final Context context) {
    getSharedPreferences(context).edit().putLong(PREFERENCE_KEY_BUS_ROUTE_LAST_UPDATE_TIME,
        System.currentTimeMillis()).commit();
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

  public static long getTrainStationsLastUpdate(final Context context) {
    SharedPreferences settings = getSharedPreferences(context);
    checkDatabaseChanged(context);
    return settings.getLong(PREFERENCE_KEY_TRAIN_STATIONS_LAST_UPDATE_TIME, 0);
  }

  public static void setTrainStationsLastUpdate(final Context context) {
    SharedPreferences settings = getSharedPreferences(context);
    settings.edit().putLong(PREFERENCE_KEY_TRAIN_STATIONS_LAST_UPDATE_TIME,
        System.currentTimeMillis()).commit();
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
      case R.id.train_route_red_id:
        return ColorCode.Red.toString();
      case R.id.train_route_blue_id:
        return ColorCode.Blue.toString();
      case R.id.train_route_brown_id:
        return ColorCode.Brn.toString();
      case R.id.train_route_green_id:
        return ColorCode.G.toString();
      case R.id.train_route_orange_id:
        return ColorCode.Org.toString();
      case R.id.train_route_purple_id:
        return ColorCode.P.toString();
      case R.id.train_route_pink_id:
        return ColorCode.Pink.toString();
      case R.id.train_route_yellow_id:
        return ColorCode.Y.toString();
      default:
        Log.e(TAG, "Undefined viewId: " + viewId);
        return "";
    }
  }

  public enum ColorCode {
    Red(R.color.train_red), Blue(R.color.train_blue),
    Brn(R.color.train_brown), G(R.color.train_green), Org(R.color.train_orange),
    P(R.color.train_purple), Pink(R.color.train_pink), Y(R.color.train_yellow);

    final int colorId;

    ColorCode(int colorId) {
      this.colorId = colorId;
    }
  }

  public static int getColorIdByCode(String colorCode) {
    return ColorCode.valueOf(colorCode).colorId;
  }

  @Deprecated
  public static String getColorDestination(int viewId) {
    switch (viewId) {
      case R.id.train_route_red_id:
        return "Red/95th";
      case R.id.train_route_blue_id:
        return "Blue/ForestPark";
      case R.id.train_route_brown_id:
        return "Brown/Loop";
      case R.id.train_route_green_id:
        return "Green/63rd";
      case R.id.train_route_orange_id:
        return "Orange/Loop";
      case R.id.train_route_purple_id:
        return "PurpleExp/Linden";
      case R.id.train_route_pink_id:
        return "Pink/Loop";
      case R.id.train_route_yellow_id:
        return "Yellow/Howard";
      default:
        Log.e(TAG, "Undefined viewId: " + viewId);
        return "";
    }
  }

  public static String getColorWithoutDestination(int viewId) {
    switch (viewId) {
      case R.id.train_route_red_id:
        return "Red";
      case R.id.train_route_blue_id:
        return "Blue";
      case R.id.train_route_brown_id:
        return "Brown";
      case R.id.train_route_green_id:
        return "Green";
      case R.id.train_route_orange_id:
        return "Orange";
      case R.id.train_route_purple_id:
        return "PurpleExp";
      case R.id.train_route_pink_id:
        return "Pink";
      case R.id.train_route_yellow_id:
        return "Yellow";
      default:
        Log.e(TAG, "Undefined viewId: " + viewId);
        return "";
    }
  }

  public static String getTrainDestinationName(String destination) {
    if (TRAIN_DESTINATION_NAME.containsKey(destination)) {
      return TRAIN_DESTINATION_NAME.get(destination);
    } else {
      Log.w(TAG, "undefined key:" + destination);
      return destination;
    }
  }

  private static SharedPreferences getSharedPreferences(final Context context) {
    return context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
  }

  private static void checkDatabaseChanged(final Context context) {
    SharedPreferences settings = getSharedPreferences(context);
    if (DATABASE_VERSION != settings.getInt(PREFERENCE_KEY_DATABASE_LAST_UPDATE_TIME, 0)) {
      settings.edit().putInt(PREFERENCE_KEY_DATABASE_LAST_UPDATE_TIME, DATABASE_VERSION).commit();
      settings.edit().putLong(PREFERENCE_KEY_BUS_ROUTE_LAST_UPDATE_TIME, 0).commit();
      settings.edit().putLong(PREFERENCE_KEY_TRAIN_STATIONS_LAST_UPDATE_TIME, 0).commit();
      settings.edit().putLong(PREFERENCE_KEY_TRAIN_STOPS_LAST_UPDATE_TIME, 0).commit();
    }
  }
}
