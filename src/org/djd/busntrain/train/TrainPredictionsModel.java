package org.djd.busntrain.train;

import android.util.Log;
import org.djd.busntrain.commons.XmlUtil;
import org.djd.busntrain.commons.XmlUtilException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 11/17/12
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainPredictionsModel implements Serializable {
  private static final String TAG = TrainPredictionsModel.class.getSimpleName();
  private static SimpleDateFormat TIME_PATTERN = new SimpleDateFormat("yyyyMMdd HH:mm");
  final String tmst;
  final String staId;
  final String stpId;
  final String staNm;
  final String stpDe;
  final String prdt;
  final String arrT;
  final String minutes;

  public TrainPredictionsModel(Builder builder) {
    tmst = builder.tmst;
    staId = builder.staId;
    stpId = builder.stpId;
    staNm = builder.staNm;
    stpDe = builder.stpDe;
    prdt = builder.prdt;
    arrT = builder.arrT;
    minutes = TrainPredictionsModel.getMinute(builder);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("TrainPredictionsModel");
    sb.append("{tmst='").append(tmst).append('\'');
    sb.append(", staId='").append(staId).append('\'');
    sb.append(", stpId='").append(stpId).append('\'');
    sb.append(", staNm='").append(staNm).append('\'');
    sb.append(", stpDe='").append(stpDe).append('\'');
    sb.append(", prdt='").append(prdt).append('\'');
    sb.append(", arrT='").append(arrT).append('\'');
    sb.append(", minutes='").append(minutes).append('\'');
    sb.append('}');
    return sb.toString();
  }



  public static String getMinute(Builder builder) {
    try {
      Date curDate = TIME_PATTERN.parse(builder.tmst);
      Date arrDate = TIME_PATTERN.parse(builder.arrT);
      long diffInMilliSec = arrDate.getTime() - curDate.getTime();
      double diffInMilliSecDouble = (double) diffInMilliSec;
      double diffInSecDouble = diffInMilliSecDouble / 1000.0;
      double diffInMinDouble = diffInSecDouble / 60.0;
      diffInMinDouble = Math.floor(diffInMinDouble);
      int diffInMinutes = (int) diffInMinDouble;
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

  public static ArrayList<TrainPredictionsModel> parse(String predictionXml) {
    Log.d(TAG, predictionXml);
    try {
      Builder builder = new Builder();
      Document document = XmlUtil.unmarshall(predictionXml);
      String timeStamp = XmlUtil.getValue(document.getDocumentElement(), "tmst");
      Log.d(TAG, timeStamp);
      NodeList nodeList = document.getElementsByTagName("eta");
      final int COUNT = nodeList.getLength();
      ArrayList<TrainPredictionsModel> trainPredictionsModels =
          new ArrayList<TrainPredictionsModel>(COUNT);
      for (int i = 0; i < COUNT; i++) {
        Element element = (Element) nodeList.item(i);
        trainPredictionsModels.add(builder.tmst(timeStamp)
            .staId(XmlUtil.getValue(element, "staId"))
            .stpId(XmlUtil.getValue(element, "stpId"))
            .staNm(XmlUtil.getValue(element, "staNm"))
            .prdt(XmlUtil.getValue(element, "prdt"))
            .arrT(XmlUtil.getValue(element, "arrT"))
            .build());
      }
      return trainPredictionsModels;
    } catch (XmlUtilException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    throw new IllegalStateException();
  }

  private static class Builder {
    private String tmst;
    private String staId;
    private String stpId;
    private String staNm;
    private String stpDe;
    private String prdt;
    private String arrT;

    public Builder tmst(String tmst) {
      this.tmst = tmst;
      return this;
    }

    public Builder staId(String staId) {
      this.staId = staId;
      return this;
    }

    public Builder stpId(String stpId) {
      this.stpId = stpId;
      return this;
    }

    public Builder staNm(String staNm) {
      this.staNm = staNm;
      return this;
    }

    public Builder stpDe(String stpDe) {
      this.stpDe = stpDe;
      return this;
    }

    public Builder prdt(String prdt) {
      this.prdt = prdt;
      return this;
    }

    public Builder arrT(String arrT) {
      this.arrT = arrT;
      return this;
    }

    public TrainPredictionsModel build() {
      return new TrainPredictionsModel(this);
    }
  }
}