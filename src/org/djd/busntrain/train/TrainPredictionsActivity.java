package org.djd.busntrain.train;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import org.djd.busntrain.R;

import java.util.ArrayList;

import static org.djd.busntrain.commons.ApplicationCommons.getColorDestination;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/31/12
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainPredictionsActivity extends Activity {
  private static final String TAG = TrainPredictionsActivity.class.getSimpleName();
  public static final String EXTRA_DATA_STATIONS_KEY = "EXTRA_DATA_STATIONS_KEY";
  private TrainPredictionActivityBroadcastReceiver receiver;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.train_prediction_test_view);
    receiver = new TrainPredictionActivityBroadcastReceiver();

    Intent intent = getIntent();
    if (intent != null) {
      StationModel station = (StationModel) intent.getSerializableExtra(EXTRA_DATA_STATIONS_KEY);
      // TODO need to get parentStopId from stationModel.stopid.
      Intent intentService = new Intent(this, TrainPredictionService.class);
      intentService.setData(Uri.parse(String.valueOf(station.getStopId())));
      startService(intentService);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    super.registerReceiver(receiver, receiver.intentFilter);

  }

  @Override
  protected void onPause() {
    super.onPause();
    if (null != receiver) {
      super.unregisterReceiver(receiver);
    }
  }
  /**
   * Callback handler receives prediction from the cloud.
   */
  public class TrainPredictionActivityBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_RESPONSE = "org.djd.busntrain.train.TrainPredictionActivityBroadcastReceiver";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public final IntentFilter intentFilter;

    public TrainPredictionActivityBroadcastReceiver() {
      intentFilter = new IntentFilter(ACTION_RESPONSE);
      intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      String result = "Train predictions....";
      Toast.makeText(TrainPredictionsActivity.this, result, Toast.LENGTH_SHORT).show();
      ArrayList<TrainPredictionsModel> trainPredictionsModels =
          TrainPredictionsModel.parse((String) intent.getSerializableExtra(EXTRA_DATA));
      Log.d(TAG, trainPredictionsModels.toString());
//      displayListItems();
    }
  }
}