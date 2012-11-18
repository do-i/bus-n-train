package org.djd.busntrain.bus;

import org.djd.busntrain.bus.BusPredictionActivity.BusPredictionActivityBroadcastReceiver;
import org.djd.busntrain.commons.Downloader;
import org.djd.busntrain.commons.StringUtil;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class BusPredictionService extends IntentService {

  private static final String TAG = BusPredictionService.class.getSimpleName();

  public BusPredictionService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    BusFavoriteEntity busDto = (BusFavoriteEntity) intent.getSerializableExtra(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY);

    if (null == busDto) {
      throw new IllegalArgumentException("route and stopid is required.");
    }


    String urlTxt = StringUtil.getBusPredictionUrl(this, busDto.route, busDto.stopId);
    Log.i(TAG, "url=" + urlTxt);
    try {
      String responseXmlTxt = Downloader.getAsString(urlTxt);

      broadcast(responseXmlTxt);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private void broadcast(String responseXmlTxt) {
    Intent responseIntent = new Intent();

    responseIntent.setAction(BusPredictionActivityBroadcastReceiver.ACTION_RESPONSE);
    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
    responseIntent.putExtra(BusPredictionActivityBroadcastReceiver.XML_DATA_TXT, responseXmlTxt);

    sendBroadcast(responseIntent);
  }


}
