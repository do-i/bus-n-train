package org.djd.busntrain.bus;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.djd.busntrain.commons.XmlUtil;
import org.djd.busntrain.commons.XmlUtilException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class BusDirections {

  /**
   * @param directionXmlTxt <bustime-response><dir>North Bound</dir><dir>South Bound</dir></bustime-response>
   * @return
   * @throws XmlUtilException
   */
  public static ArrayList<Direction> parseValue(String directionXmlTxt) throws XmlUtilException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    InputSource inputSource = new InputSource(new StringReader(directionXmlTxt));
    try {
      Node response = (Node) xpath.evaluate("/bustime-response", inputSource, XPathConstants.NODE);
      ArrayList<Direction> directions = new ArrayList<Direction>(2);
      directions.add(new Direction(xpath.evaluate("dir[position()=1]", response)));
      directions.add(new Direction(xpath.evaluate("dir[position()=2]", response)));
      return directions;
    } catch (XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }

  public static class Direction extends HashMap<String, String> {

    public static final String[] COLUMNS = new String[]{NameKey.DIRECTION.text};
    private static final long serialVersionUID = -544040097017251479L;

    enum NameKey {
      DIRECTION("dir");

      private String text;

      private NameKey(String text) {
        this.text = text;
      }
    }

    Direction (String value) {
      put(NameKey.DIRECTION.text, value);
    }

    public String getByNameKey(NameKey nameKey) {
      return get(nameKey.text);
    }
  }
}