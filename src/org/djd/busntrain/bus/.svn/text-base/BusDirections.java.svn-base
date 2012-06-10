package org.djd.busntrain.bus;

import java.util.ArrayList;
import java.util.HashMap;

import org.djd.busntrain.commons.XmlUtil;
import org.djd.busntrain.commons.XmlUtilException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

public class BusDirections {

	private static final String TAG = BusDirections.class.getSimpleName();
	private static final String DIRECTION_ELEMENT = "dir";

	/**
	 * 
	 * @param routesNodeList
	 *           contain <dir> elements
	 * @return
	 * @throws XmlUtilException
	 */
	public static ArrayList<Direction> parseValue(String directionXmlTxt) throws XmlUtilException {

		Document document = XmlUtil.unmarshall(directionXmlTxt);
		NodeList routesNodeList = document.getElementsByTagName(DIRECTION_ELEMENT);

		final int DIRECTION_COUNT = routesNodeList.getLength();

		Log.i(TAG, String.format("DIRECTION_COUNT=%d", DIRECTION_COUNT));
		ArrayList<Direction> directions = new ArrayList<Direction>(DIRECTION_COUNT);

		for (int i = 0; i < DIRECTION_COUNT; i++) {
			Node directionNode = routesNodeList.item(i);
			Direction direction = new Direction(directionNode);
			directions.add(direction);

		}

		return directions;

	}

	public static class Direction extends HashMap<String, String> {

		/**
       * 
       */
		private static final long serialVersionUID = -544040097017251479L;

		enum NameKey {
			DIRECTION("dir");
			private String text;

			private NameKey(String text) {
				this.text = text;
			}

		}

		public static final String[] COLUMNS = new String[] { NameKey.DIRECTION.text };

		/**
		 * 
		 * @param nodeList
		 *           should contain two elements
		 */
		Direction(Node routeNode) {
			Element element = (Element) routeNode;
			for (NameKey nameKey : NameKey.values()) {

				String value = XmlUtil.getValue(element, nameKey.text);

				put(nameKey.text, value);
			}
		}

		public String getByNameKey(NameKey nameKey) {
			return get(nameKey.text);
		}

	}

}
