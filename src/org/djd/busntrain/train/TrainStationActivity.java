package org.djd.busntrain.train;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.djd.busntrain.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/29/12
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainStationActivity extends ListActivity {
  private static final String TAG = TrainStationActivity.class.getSimpleName();

  private TrainStopActivityBroadcastReceiver receiver;

  private ArrayList<StationModel> stations;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_list_view);
    receiver = new TrainStopActivityBroadcastReceiver();

    Intent intent = getIntent();
    if (intent != null) {
      Intent intentService = new Intent(this, TrainStationService.class);
      intentService.setData(intent.getData());
      startService(intentService);

    } else {
      Log.e(TAG, "intent is null");
    }
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);

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
    ListAdapter listAdapter = new TrainStationAdapter(this, stations);
    setListAdapter(listAdapter);
  }

  /**
   * Callback handler receives category list data from database.
   */
  public class TrainStopActivityBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_RESPONSE = "org.djd.busntrain.train.TrainStopActivityBroadcastReceiver";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public final IntentFilter intentFilter;

    public TrainStopActivityBroadcastReceiver() {
      intentFilter = new IntentFilter(ACTION_RESPONSE);
      intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      String result = "Train stops....";
      Toast.makeText(TrainStationActivity.this, result, Toast.LENGTH_SHORT).show();
      stations = (ArrayList<StationModel>) intent.getSerializableExtra(EXTRA_DATA);
      displayListItems();
    }
  }
}
