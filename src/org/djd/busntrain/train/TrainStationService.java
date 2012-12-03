package org.djd.busntrain.train;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.google.gson.Gson;
import org.djd.busntrain.provider.TrainStationsContentProvider;
import org.djd.busntrain.provider.TrainStopsContentProvider;

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
  private static final String URL_STATIONS_TXT =
      "http://shielded-taiga-4473.herokuapp.com/v1/stations/";
  private static final String URL_STOPS_TXT =
      "http://shielded-taiga-4473.herokuapp.com/v1/stops/";
  private static final Gson gson = new Gson();

  public TrainStationService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Reader reader = null;
    ContentResolver contentResolver = super.getContentResolver();
    try {
      reader = new InputStreamReader(new URL(URL_STATIONS_TXT).openStream());
      for (TrainStationsEntity stationsEntity :
          gson.<ArrayList<TrainStationsEntity>>fromJson(reader, TrainStationsEntity.TYPE)) {
        Uri uri = contentResolver.insert(TrainStationsContentProvider.CONTENT_URI,
            stationsEntity.getContentValuesForInsert());
        Log.i(TAG, "rowId:" + ContentUris.parseId(uri));
      }
      Log.d(TAG, "stations download done.");
      reader.close();
      reader = new InputStreamReader(new URL(URL_STOPS_TXT).openStream());
      for (TrainStopsEntity trainStopsEntity:
          gson.<ArrayList<TrainStopsEntity>>fromJson(reader, TrainStopsEntity.TYPE)) {
        Uri uri = contentResolver.insert(TrainStopsContentProvider.CONTENT_URI,
            trainStopsEntity.getContentValuesForInsert());
        Log.i(TAG, "rowId:" + ContentUris.parseId(uri));
      }
      broadcast();
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
  }

  private void broadcast() {
    Intent responseIntent = new Intent();
    responseIntent.setAction(
        TrainStationActivity.TrainStopActivityBroadcastReceiver.ACTION_RESPONSE);
    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
    sendBroadcast(responseIntent);
  }
}
