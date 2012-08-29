package org.djd.busntrain.bus;

import org.djd.busntrain.bus.BusDirectionActivity.BusDirectionActivityBroadcastReceiver;
import org.djd.busntrain.commons.Downloader;
import org.djd.busntrain.commons.StringUtil;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class BusDirectionService extends IntentService {

  private static final String TAG = BusDirectionService.class.getSimpleName();

  public BusDirectionService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {

    String routeTxt = intent.getDataString();
    Log.i(TAG, "routeTxt=" + routeTxt);
    String urlTxt = StringUtil.getBusDirectionUrl(this, routeTxt);
    Log.i(TAG, "url=" + urlTxt);
    try {
      String responseXmlTxt = Downloader.getAsString(urlTxt);
      broadcase(responseXmlTxt);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private void broadcase(String responseXmlTxt) {
    Intent responseIntent = new Intent();

    responseIntent.setAction(BusDirectionActivityBroadcastReceiver.ACTION_RESPONSE);
    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
    responseIntent.putExtra(BusDirectionActivityBroadcastReceiver.XML_DATA_TXT, responseXmlTxt);

    sendBroadcast(responseIntent);
  }

}
