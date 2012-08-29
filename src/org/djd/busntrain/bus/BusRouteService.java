package org.djd.busntrain.bus;

import java.util.ArrayList;

import org.djd.busntrain.bus.BusRouteActivity.BusRouteActivityBroadcastReceiver;
import org.djd.busntrain.commons.Downloader;
import org.djd.busntrain.commons.StringUtil;
import org.djd.busntrain.provider.BusRouteContentProvider;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class BusRouteService extends IntentService {

  private static final String TAG = BusRouteService.class.getSimpleName();

  public BusRouteService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {

    String urlTxt = StringUtil.getBusRouteUrl(this);
    Log.i(TAG, "url=" + urlTxt);
    try {
      String routesXmlTxt = Downloader.getAsString(urlTxt);
      ContentResolver contentResolver = super.getContentResolver();
      ArrayList<BusRouteEntity> busRouteEntityList = BusRouteEntity.Helper.parseValue(routesXmlTxt);

      for (BusRouteEntity busRouteEntity : busRouteEntityList) {

        Uri uri = contentResolver.insert(BusRouteContentProvider.CONTENT_URI,
            busRouteEntity.getContentValuesForInsert());
        Log.i(TAG, "rowId:" + ContentUris.parseId(uri));
      }

      // String responseXmlTxt = getDebugData();
      broadcase();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private void broadcase() {
    Intent responseIntent = new Intent();

    responseIntent.setAction(BusRouteActivityBroadcastReceiver.ACTION_RESPONSE);
    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
//		responseIntent.putExtra(BusRouteActivityBroadcastReceiver.XML_DATA_TXT, responseXmlTxt);

    sendBroadcast(responseIntent);
  }

  // private String getDebugData() {
  // return
  // "<bustime-response>		<route>		<rt>1</rt>		<rtnm>Indiana/Hyde Park</rtnm>		</route>		<route>"
  // +
  // "<rt>2</rt>		<rtnm>Hyde Park Express</rtnm>		</route>		<route>		<rt>3</rt>		<rtnm>King Drive</rtnm>"
  // +
  // "</route>		<route>		<rt>4</rt>		<rtnm>Cottage Grove</rtnm>		</route>		<route>		<rt>5</rt>		"
  // + "<rtnm>South Shore Night Bus</rtnm>		</route> </bustime-response>	";
  // }

}
