package org.djd.busntrain.train;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import org.djd.busntrain.commons.ApplicationCommons;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrainStopsEntity implements Serializable {
  public static final Type TYPE = new TypeToken<ArrayList<TrainStopsEntity>>() {
  }.getType();
  private static final String TAG = TrainStopsEntity.class.getSimpleName();
  public long id;
  public int stopId;
  public String stopName;

  public String directionId;
  public double lon;
  public double lat;
  public String stationName;
  public String stationDescName;
  public String parentStopId;

  public ContentValues getContentValues() {
    ContentValues contentValues = getContentValuesForInsert();
    contentValues.put(Columns._ID, id);
    return contentValues;
  }

  public ContentValues getContentValuesForInsert() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(Columns.STOP_ID, stopId);
    contentValues.put(Columns.STOP_NAME, stopName);
    contentValues.put(Columns.DIRECTION, directionId);
    contentValues.put(Columns.LON, lon);
    contentValues.put(Columns.LAT, lat);
    contentValues.put(Columns.STATION_NAME, stationName);
    contentValues.put(Columns.STATION_DESCRIPTION_NAME, stationDescName);
    contentValues.put(Columns.PARENT_STOP_ID, parentStopId);
    return contentValues;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("TrainStopsEntity");
    sb.append("{id=").append(id);
    sb.append(", stopId=").append(stopId);
    sb.append(", stopName='").append(stopName).append('\'');
    sb.append(", directionId='").append(directionId).append('\'');
    sb.append(", lon=").append(lon);
    sb.append(", lat=").append(lat);
    sb.append(", stationName='").append(stationName).append('\'');
    sb.append(", stationDescName='").append(stationDescName).append('\'');
    sb.append(", parentStopId='").append(parentStopId).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public static final class Columns implements BaseColumns {

    public static final String STOP_ID = "STOP_ID";
    public static final String STOP_NAME = "STOP_NAME";
    public static final String DIRECTION = "DIRECTION";
    public static final String LON = "LON";
    public static final String LAT = "LAT";
    public static final String STATION_NAME = "STATION_NAME";
    public static final String STATION_DESCRIPTION_NAME = "STATION_DESCRIPTION_NAME";
    public static final String PARENT_STOP_ID = "PARENT_STOP_ID";


    public static final String[] FULL_PROJECTION =
        {_ID, STOP_ID, STOP_NAME, DIRECTION, LON, LAT, STATION_NAME, STATION_DESCRIPTION_NAME,
            PARENT_STOP_ID };
    public static final String[] LIST_VIEW_PROJECTION =
        {STOP_ID, STOP_NAME, DIRECTION, LON, LAT, STATION_NAME, STATION_DESCRIPTION_NAME,
            PARENT_STOP_ID};

    private Columns() {
    }
  }
  public static final class Helper {
  }
}
