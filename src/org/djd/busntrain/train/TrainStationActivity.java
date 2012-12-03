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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import org.djd.busntrain.R;
import org.djd.busntrain.commons.ApplicationCommons;
import org.djd.busntrain.provider.TrainStationsContentProvider;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/29/12
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainStationActivity extends ListActivity {
  private static final String TAG = TrainStationActivity.class.getSimpleName();
  private static final int[] VIEW_STATION_ID_ARRAY = new int[]{R.id.train_stop_id, R.id.train_stop_name_id};
  private TrainStopActivityBroadcastReceiver receiver;
  private Uri uri;
  private boolean dateNeedsUpdate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_list_view);
    receiver = new TrainStopActivityBroadcastReceiver();
    long lastUpdate = ApplicationCommons.getTrainStationsLastUpdate(this);
    Log.i(TAG, "last update time=" + new Date(lastUpdate));
    dateNeedsUpdate = ApplicationCommons.isMoreThanOneYearOld(lastUpdate);
    Intent intent = getIntent();
    if (intent != null) {
      uri = intent.getData();
    } else {
      Log.e(TAG, "intent is null");
    }
    if (dateNeedsUpdate) {
      Intent intentService = new Intent(this, TrainStationService.class);
      startService(intentService);
      Toast.makeText(this, R.string.toast_getting_routes, Toast.LENGTH_SHORT).show();
    } else {
      displayListItems();
    }
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Intent intent = new Intent(this, TrainPredictionsActivity.class);
    intent.putExtra(TrainPredictionsActivity.EXTRA_DATA_STATIONS_KEY, id);
    startActivity(intent);
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
    Cursor cursor = managedQuery(TrainStationsContentProvider.CONTENT_URI,
        TrainStationsEntity.Columns.FULL_PROJECTION, String.format("color='%s'", uri.toString()),
        null, TrainStationsEntity.Columns._ID);

    setListAdapter(new SimpleCursorAdapter(this, R.layout.train_stop_list_item_view, cursor,
        TrainStationsEntity.Columns.LIST_VIEW_PROJECTION, VIEW_STATION_ID_ARRAY));
  }

  /**
   * Callback handler receives category list data from database.
   */
  public class TrainStopActivityBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_RESPONSE = "org.djd.busntrain.train.TrainStopActivityBroadcastReceiver";
    public final IntentFilter intentFilter;

    public TrainStopActivityBroadcastReceiver() {
      intentFilter = new IntentFilter(ACTION_RESPONSE);
      intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      ApplicationCommons.setTrainStationsLastUpdate(TrainStationActivity.this);
      displayListItems();
    }
  }
}
