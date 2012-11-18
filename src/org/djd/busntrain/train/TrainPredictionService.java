package org.djd.busntrain.train;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.gson.Gson;
import org.djd.busntrain.commons.DownloadException;
import org.djd.busntrain.commons.Downloader;
import org.djd.busntrain.commons.StringUtil;

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
    String url = URL_STOPS_TXT + intent.getDataString();
    Reader reader = null;
    try {
      reader = new InputStreamReader(new URL(url).openStream());
      Integer parentStopId = new Gson().fromJson(reader, Integer.class);
      if (parentStopId != null) {
        broadcast(Downloader.getAsString(StringUtil.getTrainPredictionUrl(this, parentStopId)));
      }
    } catch (MalformedURLException e) {
      Log.e(TAG, e.getMessage());
    } catch (IOException e) {
      Log.e(TAG, e.getMessage());
    } catch (DownloadException e) {
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

  private void broadcast(String responseXmlTxt) {
    Intent responseIntent = new Intent();
    responseIntent.setAction(ACTION_RESPONSE);
    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
    responseIntent.putExtra(EXTRA_DATA, responseXmlTxt);
    sendBroadcast(responseIntent);
  }
}