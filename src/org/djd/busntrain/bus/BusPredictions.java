package org.djd.busntrain.bus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import org.djd.busntrain.commons.XmlUtil;
import org.djd.busntrain.commons.XmlUtilException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

public class BusPredictions {

  private static final String TAG = BusPredictions.class.getSimpleName();
  private static final String PREDICTION_ELEMENT = "prd";

  /**
   * @param predictionsNodeList contain <prd> elements
   * @return
   * @throws XmlUtilException
   */
  public static ArrayList<Prediction> parseValue(String predictionsXmlTxt) throws XmlUtilException {

    Document document = XmlUtil.unmarshall(predictionsXmlTxt);
    NodeList predictionsNodeList = document.getElementsByTagName(PREDICTION_ELEMENT);

    final int PREDICTION_COUNT = predictionsNodeList.getLength();

    Log.i(TAG, String.format("PREDICTION_COUNT=%d", PREDICTION_COUNT));
    ArrayList<Prediction> predictions = new ArrayList<Prediction>(PREDICTION_COUNT);

    for (int i = 0; i < PREDICTION_COUNT; i++) {
      Node predictionNode = predictionsNodeList.item(i);
      Prediction prediction = new Prediction(predictionNode);
      prediction.put(Prediction.MINUTE_KEY, Helper.getMinute(prediction));
      predictions.add(prediction);

    }

    return predictions;

  }

  public static class Helper {
    private static SimpleDateFormat BUS_TIME_PATTERN = new SimpleDateFormat("yyyyMMdd HH:mm");

    public static ArrayList<Prediction> sort(Prediction.NameKey sortKey, ArrayList<Prediction> predictions) {
      TreeMap<String, Prediction> sortedMap = new TreeMap<String, Prediction>();
      for (Prediction prediction : predictions) {
        sortedMap.put(prediction.getByNameKey(sortKey), prediction);
      }

      predictions.clear();
      for (String key : sortedMap.keySet()) {
        predictions.add(sortedMap.get(key));
      }

      return predictions;
    }

    public static String getMinute(Prediction prediction) {
      String curTime = prediction.getByNameKey(Prediction.NameKey.TIMESTAMP);
      String prdTime = prediction.getByNameKey(Prediction.NameKey.PREDICTION_TIME);

      try {
        Date curDate = BUS_TIME_PATTERN.parse(curTime);
        Date prdDate = BUS_TIME_PATTERN.parse(prdTime);
        long diffInMilliSec = prdDate.getTime() - curDate.getTime();
//				Log.i(TAG, "diffInMilliSec=" + diffInMilliSec);
        double diffInMilliSecDouble = (double) diffInMilliSec;
        double diffInSecDouble = diffInMilliSecDouble / 1000.0;
        double diffInMinDouble = diffInSecDouble / 60.0;

//				Log.i(TAG, "diffInMilliSecDouble=" + diffInMilliSecDouble);
//				Log.i(TAG, "diffInSecDouble=" + diffInSecDouble);
//				Log.i(TAG, "diffInMinDouble=" + diffInMinDouble);
        diffInMinDouble = Math.floor(diffInMinDouble);
//				Log.i(TAG, "floor_diffInMinDouble=" + diffInMinDouble);

        int diffInMinutes = (int) diffInMinDouble;
//				Log.i(TAG, "diffInMinutes=" + diffInMinutes);

        // practical adjustment
        --diffInMinutes;

        if (1 >= diffInMinutes) {
          return "Due";
        }
        return String.format("%d Min", diffInMinutes);
      } catch (ParseException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  public static class Prediction extends HashMap<String, String> {

    /**
     *
     */
    private static final long serialVersionUID = -8853781666477277978L;

    public static final String MINUTE_KEY = "MINUTE_KEY";

    enum NameKey {
      TIMESTAMP("tmstmp"), PREDICTION_TIME("prdtm"), VEHICLE_ID("vid"), ROUTE_ID("rt"), DESTINATION("des");
      private String text;

      private NameKey(String text) {
        this.text = text;
      }

    }

    public static final String[] COLUMNS = new String[]{MINUTE_KEY, NameKey.PREDICTION_TIME.text,
        NameKey.VEHICLE_ID.text, NameKey.ROUTE_ID.text, NameKey.DESTINATION.text};

    /**
     * @param nodeList should contain two elements
     */
    Prediction(Node predictionNode) {
      Element element = (Element) predictionNode;
      for (NameKey nameKey : NameKey.values()) {

        String value = XmlUtil.getValue(element, nameKey.text);

        put(nameKey.text, value);
      }
    }

    /**
     * convenience method, same effect as get(key)
     *
     * @param nameKey
     * @return
     */
    public String getByNameKey(NameKey nameKey) {
      return get(nameKey.text);
    }

  }

}
