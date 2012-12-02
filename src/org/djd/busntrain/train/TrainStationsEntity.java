package org.djd.busntrain.train;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import org.djd.busntrain.commons.ApplicationCommons;
import org.djd.busntrain.commons.XmlUtil;
import org.djd.busntrain.commons.XmlUtilException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrainStationsEntity implements Serializable {
  public static final Type TYPE = new TypeToken<ArrayList<TrainStationsEntity>>() {
  }.getType();
  private static final String TAG = TrainStationsEntity.class.getSimpleName();
  public long id;
  public int stopId;
  public String stopName;
  public String color;
  public String destination;
  public int sequence;

  public ContentValues getContentValues() {
    ContentValues contentValues = getContentValuesForInsert();
    contentValues.put(Columns._ID, id);
    return contentValues;
  }

  public ContentValues getContentValuesForInsert() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(Columns.STOP_ID, stopId);
    contentValues.put(Columns.STOP_NAME, stopName);
    contentValues.put(Columns.COLOR, color);
    contentValues.put(Columns.DESTINATION, destination);
    contentValues.put(Columns.SEQUENCE, sequence);
    return contentValues;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("TrainStationsEntity");
    sb.append("{id=").append(id);
    sb.append(", stopId=").append(stopId);
    sb.append(", stopName='").append(stopName).append('\'');
    sb.append(", color='").append(color).append('\'');
    sb.append(", destination='").append(destination).append('\'');
    sb.append(", sequence=").append(sequence);
    sb.append('}');
    return sb.toString();
  }

  public static final class Columns implements BaseColumns {

    public static final String STOP_ID = "STOP_ID";
    public static final String STOP_NAME = "STOP_NAME";
    public static final String COLOR = "COLOR";
    public static final String DESTINATION = "DESTINATION";
    public static final String SEQUENCE = "SEQUENCE";

    public static final String[] FULL_PROJECTION =
        {_ID, STOP_ID, STOP_NAME, COLOR, DESTINATION, SEQUENCE};
    public static final String[] LIST_VIEW_PROJECTION =
        {STOP_ID, STOP_NAME, COLOR, DESTINATION, SEQUENCE};

    private Columns() {
    }
  }

  public static final class Helper {

    public static TrainStationsEntity toTrainStationsEntity(Cursor cursor, int position) {
      cursor.moveToPosition(position);
      TrainStationsEntity stationsEntity = new TrainStationsEntity();
      stationsEntity.stopId = cursor.getInt(cursor.getColumnIndex(Columns.STOP_ID));
      stationsEntity.stopName = cursor.getString(cursor.getColumnIndex(Columns.STOP_NAME));
      stationsEntity.color = cursor.getString(cursor.getColumnIndex(Columns.COLOR));
      stationsEntity.destination = cursor.getString(cursor.getColumnIndex(Columns.DESTINATION));
      stationsEntity.sequence = cursor.getInt(cursor.getColumnIndex(Columns.SEQUENCE));
      return stationsEntity;
    }

    public static ArrayList<TrainPredictionsModel> filterByColor(
        ArrayList<TrainPredictionsModel> stationsModels, ApplicationCommons.ColorCode colorCode) {
      ArrayList<TrainPredictionsModel> filteredList = new ArrayList<TrainPredictionsModel>();
      for(TrainPredictionsModel predictionsModel : stationsModels) {
        if(predictionsModel.rt.equals(colorCode.toString())) {
          filteredList.add(predictionsModel);
        }
      }
      return filteredList;
    }

    public static ArrayList<TrainPredictionsModel> orderByDestination(String destination,
        ArrayList<TrainPredictionsModel> stationsModels) {
      ArrayList<TrainPredictionsModel> orderedList = new ArrayList<TrainPredictionsModel>();
      ArrayList<TrainPredictionsModel> remainderList = new ArrayList<TrainPredictionsModel>();
      for(TrainPredictionsModel predictionsModel : stationsModels) {
        Log.d(TAG, String.format("modeldest=%s, dest=%s", predictionsModel.destNm, destination));
        if(predictionsModel.destNm.equals(destination)) {
          orderedList.add(predictionsModel);
        } else {
          remainderList.add(predictionsModel);
        }
      }
      orderedList.addAll(remainderList);
      return orderedList;
    }


    /**
     * Converts list of models into list of entities
     *
     * @param stationModels
     * @return List of {@link TrainStationsEntity}
     */
    @Deprecated
    public static ArrayList<TrainStationsEntity> modelsToEntities(
        ArrayList<StationModel> stationModels) {
      ArrayList<TrainStationsEntity> stationsEntities =
          new ArrayList<TrainStationsEntity>(stationModels.size());
      for (StationModel stationModel : stationModels) {
        TrainStationsEntity stationsEntity = new TrainStationsEntity();
        stationsEntity.stopId = stationModel.getStopId();
        stationsEntity.stopName = stationModel.getStopName();
        stationsEntity.color = stationModel.getColor();
        stationsEntity.destination = stationModel.getDestination();
        stationsEntity.sequence = stationModel.getSequence();
        stationsEntities.add(stationsEntity);
      }
      return stationsEntities;
    }

    @Deprecated
    public static ArrayList<StationModel> entitiesToModels(
        ArrayList<TrainStationsEntity> stationsEntities) {
      ArrayList<StationModel> stationModels =
          new ArrayList<StationModel>(stationsEntities.size());
      for (TrainStationsEntity stationsEntity : stationsEntities) {
        throw new UnsupportedOperationException("TODO");
      }
      return stationModels;
    }
  }
}
