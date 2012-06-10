package org.djd.busntrain.bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.djd.busntrain.commons.XmlUtil;
import org.djd.busntrain.commons.XmlUtilException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

public class BusStops {

	private static final String TAG = BusStops.class.getSimpleName();
	private static final String STOP_ELEMENT = "stop";

	/**
	 * 
	 * @param stopsNodeList
	 *           contain <stop> elements
	 * @return
	 * @throws XmlUtilException
	 */
	public static ArrayList<Stop> parseValue(String stopsXmlTxt) throws XmlUtilException {

		Document document = XmlUtil.unmarshall(stopsXmlTxt);
		NodeList stopsNodeList = document.getElementsByTagName(STOP_ELEMENT);

		final int STOP_COUNT = stopsNodeList.getLength();

		Log.i(TAG, String.format("STOP_COUNT=%d", STOP_COUNT));
		ArrayList<Stop> stops = new ArrayList<Stop>(STOP_COUNT);

		for (int i = 0; i < STOP_COUNT; i++) {
			Node stopNode = stopsNodeList.item(i);
			Stop stop = new Stop(stopNode);
			stops.add(stop);

		}

		return stops;

	}

	public static class Helper {
		public static ArrayList<Stop> sort(Stop.NameKey sortKey, ArrayList<Stop> stops) {
			TreeMap<String, Stop> sortedMap = new TreeMap<String, Stop>();
			for (Stop stop : stops) {
				sortedMap.put(stop.getByNameKey(sortKey), stop);
			}

			stops.clear();
			for (String key : sortedMap.keySet()) {
				stops.add(sortedMap.get(key));
			}

			return stops;
		}
	}

	public static class Stop extends HashMap<String, String> {

		/**
       * 
       */
		private static final long serialVersionUID = -8853781666477277978L;

		enum NameKey {
			STOP_ID("stpid"), STOP_NAME("stpnm"), LATITUDE("lat"), LONGITUDE("lon");
			private String text;

			private NameKey(String text) {
				this.text = text;
			}

		}

		public static final String[] COLUMNS = new String[] { NameKey.STOP_ID.text, NameKey.STOP_NAME.text };

		/**
		 * 
		 * @param nodeList
		 *           should contain two elements
		 */
		Stop(Node stopNode) {
			Element element = (Element) stopNode;
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
