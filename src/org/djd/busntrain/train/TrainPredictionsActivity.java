package org.djd.busntrain.train;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.djd.busntrain.R;
import org.djd.busntrain.commons.StringUtil;

import java.util.ArrayList;

import static org.djd.busntrain.commons.ApplicationCommons.getColorDestination;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/31/12
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainPredictionsActivity extends ListActivity {
  public static final String EXTRA_DATA_STATIONS_KEY = "EXTRA_DATA_STATIONS_KEY";
  private static final String TAG = TrainPredictionsActivity.class.getSimpleName();
  private TrainPredictionActivityBroadcastReceiver receiver;
  private ArrayList<TrainPredictionsModel> trainPredictionsModels;
  private StationModel station;
  private long lastUpdateTime;
  private TextView lastUpdateTimeTextView;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.train_prediction_list_view);
    lastUpdateTimeTextView = (TextView) findViewById(R.id.train_last_update_time);
    receiver = new TrainPredictionActivityBroadcastReceiver();
    Intent intent = getIntent();
    if (intent != null) {
      station = (StationModel) intent.getSerializableExtra(EXTRA_DATA_STATIONS_KEY);
      callBusPredictionService();
    }
    ((TextView) findViewById(R.id.train_prediction_direction)).setText(station.getDestination());
    ((TextView) findViewById(R.id.train_prediction_stop_name)).setText(station.getStopName());
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Toast.makeText(this, R.string.msg_refresh, Toast.LENGTH_SHORT).show();
    callBusPredictionService();
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

  private void displayListItems() {
    ListAdapter listAdapter = new TrainPredictionsAdapter(this, trainPredictionsModels);
    lastUpdateTime = System.currentTimeMillis();
    lastUpdateTimeTextView.setText(StringUtil.timeToString(this, lastUpdateTime));
    setListAdapter(listAdapter);
  }

  private void callBusPredictionService() {
    Intent intentService = new Intent(this, TrainPredictionService.class);
    intentService.setData(Uri.parse(String.valueOf(station.getStopId())));
    startService(intentService);
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
      trainPredictionsModels =
          TrainPredictionsModel.parse((String) intent.getSerializableExtra(EXTRA_DATA));
      displayListItems();
    }
  }
}