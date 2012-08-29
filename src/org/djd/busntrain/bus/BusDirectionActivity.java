package org.djd.busntrain.bus;

import java.util.ArrayList;

import org.djd.busntrain.R;
import org.djd.busntrain.bus.BusDirections.Direction;
import org.djd.busntrain.bus.BusDirections.Direction.NameKey;
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

public class BusDirectionActivity extends ListActivity {
  private static final String TAG = BusDirectionActivity.class.getSimpleName();
  private static final String SAVE_DIRECTION_INFO_KEY = "SAVE_DIRECTION_INFO";

  private final static int[] VIEW_DIRECTION_ID_ARRAY = new int[]{R.id.direction_id};
  private BusDirectionActivityBroadcastReceiver receiver;

  private BusFavoriteEntity busDto;
  private String selectedRoute;
  private ArrayList<Direction> directions;

  @SuppressWarnings("unchecked")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_list_view);
    receiver = new BusDirectionActivityBroadcastReceiver();

    if (null != savedInstanceState) {
      directions = (ArrayList<Direction>) savedInstanceState.getSerializable(SAVE_DIRECTION_INFO_KEY);
      busDto = (BusFavoriteEntity) savedInstanceState
          .getSerializable(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY);
      displayListItems();
    } else {
      Intent payload = getIntent();
      if (null == payload) {
        throw new IllegalArgumentException("Intent with route data is required.");
      }

      busDto = (BusFavoriteEntity) payload
          .getSerializableExtra(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY);
      Uri data = payload.getData();
      Log.i(TAG, "rt=" + data);
      selectedRoute = data.toString();
      Intent intent = new Intent(this, BusDirectionService.class);
      intent.setData(data);
      startService(intent);
      Toast.makeText(this, R.string.toast_getting_directions, Toast.LENGTH_SHORT).show();
    }

    setTitle(busDto.route + " " + busDto.routeName);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(SAVE_DIRECTION_INFO_KEY, directions);
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

    Intent intent = new Intent(this, BusStopActivity.class);
    Direction selectedDirection = (Direction) listView.getItemAtPosition(position);
    String directionTxt = selectedDirection.getByNameKey(NameKey.DIRECTION);
    busDto.direction = directionTxt;
    Uri data = Uri.parse(selectedRoute + "/" + directionTxt);
    intent.setData(data);
    intent.putExtra(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY, busDto);
    startActivity(intent);
  }

  /**
   * TODO run db query in background thread.
   */
  private void displayListItems() {

    ListAdapter listAdapter = new SimpleAdapter(this, directions, R.layout.direction_list_item_view,
        Direction.COLUMNS, VIEW_DIRECTION_ID_ARRAY);

    setListAdapter(listAdapter);
  }

  /**
   * Callback handler receives category list data from database.
   */
  public class BusDirectionActivityBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_RESPONSE = "org.djd.busntrain.bus.BusDirectionActivityBroadcastReceiver";
    public static final String XML_DATA_TXT = "XML_DATA_TXT";
    public final IntentFilter intentFilter;

    public BusDirectionActivityBroadcastReceiver() {
      intentFilter = new IntentFilter(ACTION_RESPONSE);
      intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      // String result = "Completed Download and Updated Database.";
      // Toast.makeText(BusDirectionActivity.this, result,
      // Toast.LENGTH_SHORT).show();
      String xmlDataTxt = intent.getStringExtra(XML_DATA_TXT);

      try {
        directions = BusDirections.parseValue(xmlDataTxt);
      } catch (XmlUtilException e) {
        throw new RuntimeException(e);
      }

      displayListItems();

    }
  }

}
