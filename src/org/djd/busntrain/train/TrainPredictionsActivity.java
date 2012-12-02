package org.djd.busntrain.train;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.djd.busntrain.R;
import org.djd.busntrain.commons.StringUtil;
import org.djd.busntrain.provider.TrainStationsContentProvider;
import static org.djd.busntrain.commons.ApplicationCommons.*;
import static org.djd.busntrain.train.TrainStationsEntity.Helper.filterByColor;
import static org.djd.busntrain.train.TrainStationsEntity.Helper.orderByDestination;

import java.util.ArrayList;


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
  private TrainStationsEntity stationsEntity;
  private long lastUpdateTime;
  private TextView lastUpdateTimeTextView;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.train_prediction_list_view);
    lastUpdateTimeTextView = (TextView) findViewById(R.id.train_last_update_time);
    receiver = new TrainPredictionActivityBroadcastReceiver();
    Intent intent = getIntent();
    if (intent != null) {
      long stationId = intent.getLongExtra(EXTRA_DATA_STATIONS_KEY, -1);
      Cursor cursor = managedQuery(TrainStationsContentProvider.CONTENT_URI,
          TrainStationsEntity.Columns.FULL_PROJECTION, String.format("_id=%d", stationId), null,
          TrainStationsEntity.Columns._ID);
      stationsEntity = TrainStationsEntity.Helper.toTrainStationsEntity(cursor, 0);
      callBusPredictionService();
      ((TextView) findViewById(R.id.train_prediction_direction)).setText(stationsEntity.destination);
      ((TextView) findViewById(R.id.train_prediction_stop_name)).setText(stationsEntity.stopName);
    } else {
      Log.e(TAG, "intent is null. Need to handle this in gracefully... hint: get it from savedInstanceState");
    }
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
    // TODO filter and sort trainPredictionsModels either here. order by is broken.
    // TODO create stop table and download stops from HEROKU and cache them.
    // TODO from stop data there is destination direction to sort order.

    ListAdapter listAdapter = new TrainPredictionsAdapter(this,
        orderByDestination(stationsEntity.destination,
        filterByColor(trainPredictionsModels, COLOR_CODE_BY_COLOR_NAME.get(stationsEntity.color))));
    lastUpdateTime = System.currentTimeMillis();
    lastUpdateTimeTextView.setText(StringUtil.timeToString(this, lastUpdateTime));
    setListAdapter(listAdapter);
  }

  private void callBusPredictionService() {
    Log.d(TAG, "stationEntity:" + stationsEntity);
    Intent intentService = new Intent(this, TrainPredictionService.class);
    intentService.setData(Uri.parse(String.valueOf(stationsEntity.stopId)));
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