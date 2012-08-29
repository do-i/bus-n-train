package org.djd.busntrain.commons;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.djd.busntrain.commons.XmlUtilException.ERROR_CODE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class XmlUtil {
  private static final String TAG = XmlUtil.class.getSimpleName();

  public static Document unmarshall(String xmlStr) throws XmlUtilException {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
        .newInstance();
    try {
      DocumentBuilder documentBuilder = documentBuilderFactory
          .newDocumentBuilder();
      InputSource inputSource = new InputSource();
      inputSource.setCharacterStream(new StringReader(xmlStr));
      return documentBuilder.parse(inputSource);

    } catch (ParserConfigurationException e) {
      Log.e(TAG, e.getMessage(), e);
      throw new XmlUtilException(ERROR_CODE.PARSE_ERROR);
    } catch (SAXException e) {
      Log.e(TAG, e.getMessage(), e);
      throw new XmlUtilException(ERROR_CODE.SAX_ERROR);
    } catch (IOException e) {
      Log.e(TAG, e.getMessage(), e);
      throw new XmlUtilException(ERROR_CODE.IO_ERROR);
    }

  }

  public static String getValue(Element element, String elementName) {
    NodeList elements = element.getElementsByTagName(elementName);
    if (1 != elements.getLength()) {
      throw new IllegalStateException("TODO: handle when elements does not contain exactly one item.elementName=" + elementName);
    }
    Node elementNode = elements.item(0);

    NodeList children = elementNode.getChildNodes();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < children.getLength(); i++) {
      String value = children.item(i).getNodeValue();
      sb.append(value);
    }

    return sb.toString();
  }
}
