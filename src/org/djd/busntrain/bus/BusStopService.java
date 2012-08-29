package org.djd.busntrain.bus;

import org.djd.busntrain.bus.BusStopActivity.BusStopActivityBroadcastReceiver;
import org.djd.busntrain.commons.Downloader;
import org.djd.busntrain.commons.StringUtil;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class BusStopService extends IntentService {

  private static final String TAG = BusStopService.class.getSimpleName();

  public BusStopService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {

    Uri data = intent.getData();

    if (null == data) {
      throw new IllegalArgumentException("route and direction is required.");
    }
    String[] tokens = data.toString().split("/");
    if (2 != tokens.length) {
      throw new IllegalArgumentException(data.toString());
    }
    String route = tokens[0];
    String direction = tokens[1];
    String urlTxt = StringUtil.getBusStopUrl(this, route, direction);
    Log.i(TAG, "url=" + urlTxt);
    try {
      String responseXmlTxt = Downloader.getAsString(urlTxt);
      // String responseXmlTxt = getDebugData();
      broadcase(responseXmlTxt);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void broadcase(String responseXmlTxt) {
    Intent responseIntent = new Intent();

    responseIntent.setAction(BusStopActivityBroadcastReceiver.ACTION_RESPONSE);
    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
    responseIntent.putExtra(BusStopActivityBroadcastReceiver.XML_DATA_TXT, responseXmlTxt);

    sendBroadcast(responseIntent);
  }

//	private String getDebugData() {
//		return "<bustime-response><stop><stpid>8269</stpid><stpnm>4102 S Western</stpnm><lat>41.819183793255</lat><lon>-87.684749364853</lon>"
//		      + "</stop><stop><stpid>8303</stpid><stpnm>7521 S Western</stpnm><lat>41.757083133928</lat><lon>-87.683081030846</lon></stop></bustime-response>";
//	}

}
