package org.djd.busntrain.train;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import com.google.gson.Gson;
import org.djd.busntrain.commons.DownloadException;
import org.djd.busntrain.commons.Downloader;
import org.djd.busntrain.commons.StringUtil;
import org.djd.busntrain.provider.TrainStopsContentProvider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import static org.djd.busntrain.commons.ApplicationCommons.URL_STOPS_TXT;
import static org.djd.busntrain.train.TrainPredictionsActivity.TrainPredictionActivityBroadcastReceiver.ACTION_RESPONSE;
import static org.djd.busntrain.train.TrainPredictionsActivity.TrainPredictionActivityBroadcastReceiver.EXTRA_DATA;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 11/1/12
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainPredictionService extends IntentService {
  private static final String TAG = TrainPredictionService.class.getSimpleName();

  public TrainPredictionService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Cursor cursor = null;
    try {
      cursor = getContentResolver().query(TrainStopsContentProvider.CONTENT_URI,
          TrainStopsEntity.Columns.FULL_PROJECTION,
          String.format("%s='%s'", TrainStopsEntity.Columns.STOP_ID, intent.getDataString()), null,
          TrainStopsEntity.Columns._ID);
      cursor.moveToPosition(0);
      Integer parentStopId = cursor.getInt(cursor.getColumnIndex(TrainStopsEntity.Columns.PARENT_STOP_ID));
      cursor.close();
      if (parentStopId != null) {
        broadcast(Downloader.getAsString(StringUtil.getTrainPredictionUrl(this, parentStopId)));
      }
    } catch (DownloadException e) {
      Log.e(TAG, e.getMessage());
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  private void broadcast(String responseXmlTxt) {
    Intent responseIntent = new Intent();
    responseIntent.setAction(ACTION_RESPONSE);
    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
    responseIntent.putExtra(EXTRA_DATA, responseXmlTxt);
    sendBroadcast(responseIntent);
  }
}