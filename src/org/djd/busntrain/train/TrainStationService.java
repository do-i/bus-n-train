package org.djd.busntrain.train;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/29/12
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainStationService extends IntentService {

  private static final String TAG = TrainStationService.class.getSimpleName();

  private static final String URL_STATIONS_TXT = "http://shielded-taiga-4473.herokuapp.com/v1/stations/";

  public TrainStationService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Reader reader = null;
    ArrayList<StationModel> stations = null;

    String url = URL_STATIONS_TXT + intent.getDataString();
    Log.d(TAG, "urltxt: "+url);
    try {
      reader = new InputStreamReader(new URL(url).openStream());
      stations = new Gson().fromJson(reader, StationModel.TYPE);
      broadcast(stations);

    } catch (MalformedURLException e) {
      Log.e(TAG, e.getMessage());
    } catch (IOException e) {
      Log.e(TAG, e.getMessage());
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          Log.e(TAG, e.getMessage());
        }
      }
    }
    if (stations != null) {
      Log.i(TAG, stations.toString());
    } else {
      Log.e(TAG, "no stations available.");
    }

  }

  private void broadcast(ArrayList<StationModel> responseXmlTxt) {
    Intent responseIntent = new Intent();
    responseIntent.setAction(TrainStationActivity.TrainStopActivityBroadcastReceiver.ACTION_RESPONSE);
    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
    responseIntent.putExtra(TrainStationActivity.TrainStopActivityBroadcastReceiver.EXTRA_DATA, responseXmlTxt);
    sendBroadcast(responseIntent);
  }
}
