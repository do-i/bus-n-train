package org.djd.busntrain.train;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/28/12
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class StationModel implements Serializable {

  public static final Type TYPE = new TypeToken<ArrayList<StationModel>>() {
  }.getType();
  private String color;
  private String destination;
  private int sequence;
  private int stopId;
  private String stopName;

  public int getStopId() {
    return stopId;
  }

  public String getStopName() {
    return stopName;
  }

  public String getColor() {
    return color;
  }

  public String getDestination() {
    return destination;
  }

  public int getSequence() {
    return sequence;
  }


  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("Stations");
    sb.append("{color='").append(color).append('\'');
    sb.append(", destination='").append(destination).append('\'');
    sb.append(", sequence=").append(sequence);
    sb.append(", stopId=").append(stopId);
    sb.append(", stopName='").append(stopName).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
