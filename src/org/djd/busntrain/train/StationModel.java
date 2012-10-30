package org.djd.busntrain.train;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/28/12
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class StationModel {

  public static final Type TYPE = new TypeToken<List<StationModel>>() {
  }.getType();
  private String color;
  private String destination;
  private int sequence;
  private int stopId;
  private String stopName;

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
