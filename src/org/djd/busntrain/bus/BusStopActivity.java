package org.djd.busntrain.bus;

import java.util.ArrayList;

import org.djd.busntrain.R;
import org.djd.busntrain.bus.BusStops.Stop;
import org.djd.busntrain.bus.BusStops.Stop.NameKey;
import org.djd.busntrain.commons.XmlUtilException;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class BusStopActivity extends ListActivity {

  private static final String TAG = BusStopActivity.class.getSimpleName();
  private static final String SAVE_STOP_INFO_KEY = "SAVE_STOP_INFO";

  private static final int[] VIEW_STOP_ID_ARRAY = new int[]{R.id.stop_id, R.id.stop_name_id};

  private BusStopActivityBroadcastReceiver receiver;

  private String route;
  private ArrayList<Stop> stops;
  private BusFavoriteEntity busDto;

  @SuppressWarnings("unchecked")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_list_view);
    receiver = new BusStopActivityBroadcastReceiver();

    if (null != savedInstanceState) {
      stops = (ArrayList<Stop>) savedInstanceState.getSerializable(SAVE_STOP_INFO_KEY);
      busDto = (BusFavoriteEntity) savedInstanceState.getSerializable(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY);
      displayListItems();
    } else {

      Intent payload = getIntent();
      if (null == payload) {
        throw new IllegalArgumentException("route and direction is required.");
      }

      busDto = (BusFavoriteEntity) payload.getSerializableExtra(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY);
      Uri data = payload.getData();
      String[] tokens = data.toString().split("/");
      if (2 != tokens.length) {
        throw new IllegalArgumentException(data.toString());
      }
      route = tokens[0];
      Intent intent = new Intent(this, BusStopService.class);
      intent.setData(data);
      startService(intent);
      Toast.makeText(this, R.string.toast_getting_stops, Toast.LENGTH_SHORT).show();
    }

    setTitle(busDto.route + " " + busDto.routeName + " " + busDto.direction);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(SAVE_STOP_INFO_KEY, stops);
    outState.putSerializable(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY, busDto);

    Log.i(TAG, "Saving activity state.");
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

  @Override
  protected void onDestroy() {

    super.onDestroy();

  }

  @Override
  protected void onListItemClick(ListView listView, View v, int position, long id) {
    super.onListItemClick(listView, v, position, id);
    Intent intent = new Intent(this, BusPredictionActivity.class);
    Stop selectedStop = (Stop) listView.getItemAtPosition(position);
    String stopTxt = selectedStop.getByNameKey(NameKey.STOP_ID);

    busDto.stopName = selectedStop.getByNameKey(NameKey.STOP_NAME);
    busDto.route = route;
    busDto.stopId = stopTxt;

    intent.putExtra(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY, busDto);

    startActivity(intent);
  }

  private void displayListItems() {

    ListAdapter listAdapter = new SimpleAdapter(this, stops, R.layout.stop_list_item_view, Stop.COLUMNS,
        VIEW_STOP_ID_ARRAY);

    setListAdapter(listAdapter);
  }

  /**
   * Callback handler receives category list data from database.
   */
  public class BusStopActivityBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_RESPONSE = "org.djd.busntrain.bus.BusStopActivityBroadcastReceiver";
    public static final String XML_DATA_TXT = "XML_DATA_TXT";
    public final IntentFilter intentFilter;

    public BusStopActivityBroadcastReceiver() {
      intentFilter = new IntentFilter(ACTION_RESPONSE);
      intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      String result = "Completed Download and Updated Database.";
      Toast.makeText(BusStopActivity.this, result, Toast.LENGTH_SHORT).show();
      String xmlDataTxt = intent.getStringExtra(XML_DATA_TXT);
      Log.d(TAG, xmlDataTxt);
      try {
        stops = BusStops.parseValue(xmlDataTxt);
        // BusStops.Helper.sort(NameKey.STOP_NAME, stops);
      } catch (XmlUtilException e) {
        throw new RuntimeException(e);
      }

      displayListItems();

    }
  }

}
