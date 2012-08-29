package org.djd.busntrain.bus;

import java.io.Serializable;
import java.util.ArrayList;

import org.djd.busntrain.commons.XmlUtil;
import org.djd.busntrain.commons.XmlUtilException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.util.Log;

public class BusRouteEntity implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -1403007970562748140L;
  private static final String TAG = BusRouteEntity.class.getSimpleName();

  public long id;
  public String rt;
  public String rtnm;

  public ContentValues getContentValues() {

    ContentValues contentValues = new ContentValues();
    contentValues.put(BusRouteEntity.Columns._ID, id);
    contentValues.put(BusRouteEntity.Columns.ROUTE_ID, rt);
    contentValues.put(BusRouteEntity.Columns.ROUTE_NAME, rtnm);
    return contentValues;

  }

  public ContentValues getContentValuesForInsert() {

    ContentValues contentValues = new ContentValues();
    contentValues.put(BusRouteEntity.Columns.ROUTE_ID, rt);
    contentValues.put(BusRouteEntity.Columns.ROUTE_NAME, rtnm);
    return contentValues;

  }

  @Override
  public String toString() {
    return String.format("BusRouteEntity [id=%s, rt=%s, rtnm=%s]", id, rt, rtnm);
  }

  public static final class Columns implements BaseColumns {

    public static final String ROUTE_ID = "ROUTE_ID";
    public static final String ROUTE_NAME = "ROUTE_NAME";

    public static final String[] FULL_PROJECTION = {_ID, ROUTE_ID, ROUTE_NAME};
    public static final String[] LIST_VIEW_PROJECTION = {ROUTE_ID, ROUTE_NAME};

    private Columns() {
    }

  }

  public static final class Helper {
    private static final String ROUTE_ELEMENT = "route";

    enum NameKey {
      ROUTE("rt"), ROUTE_NAME("rtnm");
      private String text;

      private NameKey(String text) {
        this.text = text;
      }

    }

    /**
     * @param routesNodeList contain <route> elements
     * @return
     * @throws XmlUtilException
     */
    public static ArrayList<BusRouteEntity> parseValue(String routesXmlTxt) throws XmlUtilException {

      Document document = XmlUtil.unmarshall(routesXmlTxt);
      NodeList routesNodeList = document.getElementsByTagName(ROUTE_ELEMENT);

      final int ROUTE_COUNT = routesNodeList.getLength();

      Log.i(TAG, String.format("ROUTE_COUNT=%d", ROUTE_COUNT));
      ArrayList<BusRouteEntity> routes = new ArrayList<BusRouteEntity>(ROUTE_COUNT);

      for (int i = 0; i < ROUTE_COUNT; i++) {
        Node routeNode = routesNodeList.item(i);
        Element element = (Element) routeNode;

        String rtValue = XmlUtil.getValue(element, NameKey.ROUTE.text);
        String rtnmValue = XmlUtil.getValue(element, NameKey.ROUTE_NAME.text);

        routes.add(createBusRouteEntity(rtValue, rtnmValue));

      }

      return routes;

    }

    /**
     * @param routeId
     * @param routeName
     * @return new {@link BusRouteEntity} instance.
     */
    private static final BusRouteEntity createBusRouteEntity(String routeId, String routeName) {

      Log.i(TAG, String.format("routeId=%s, routeName=%s", routeId, routeName));
      BusRouteEntity busRouteEntity = new BusRouteEntity();
      busRouteEntity.rt = routeId;
      busRouteEntity.rtnm = routeName;
      return busRouteEntity;

    }

  }
}
