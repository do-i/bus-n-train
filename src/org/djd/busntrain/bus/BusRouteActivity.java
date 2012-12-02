package org.djd.busntrain.bus;

import java.util.Date;

import org.djd.busntrain.R;
import org.djd.busntrain.commons.ApplicationCommons;
import org.djd.busntrain.provider.BusRouteContentProvider;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class BusRouteActivity extends ListActivity {

  private static final String TAG = BusRouteActivity.class.getSimpleName();

  private static final int[] VIEW_ROUTE_ID_ARRAY = new int[]{R.id.route_id, R.id.route_name_id};

  private static final String ROUTE_FILTER_TEMPLATE = BusRouteEntity.Columns.ROUTE_NAME + " like '%%%s%%' or "
      + BusRouteEntity.Columns.ROUTE_ID + " like '%%%s%%'";

  private EditText filterText;
  private BusRouteActivityBroadcastReceiver receiver;

  private SimpleCursorAdapter listAdapter;
  private boolean dateNeedsUpdate;
  private TextWatcher filterTextWatcher = new TextWatcher() {

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence constraint, int start, int before, int count) {
      Log.i(TAG, "filter txt=" + constraint);
      listAdapter.getFilter().filter(constraint);

      //
    }

  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_list_filter_view);

    filterText = (EditText) findViewById(R.id.search_box);
    filterText.addTextChangedListener(filterTextWatcher);

    receiver = new BusRouteActivityBroadcastReceiver();

    long lastUpateTime = ApplicationCommons.getBusRouteLastUpdate(this);

    Log.i(TAG, "last update time=" + new Date(lastUpateTime));
    dateNeedsUpdate = ApplicationCommons.isMoreThanOneYearOld(lastUpateTime);

    if (dateNeedsUpdate) {

      Intent intent = new Intent(this, BusRouteService.class);
      startService(intent);
      Toast.makeText(this, R.string.toast_getting_routes, Toast.LENGTH_SHORT).show();
    } else {
      displayListItems();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
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
    filterText.removeTextChangedListener(filterTextWatcher);
  }

  @Override
  protected void onListItemClick(ListView listView, View v, int position, long id) {
    super.onListItemClick(listView, v, position, id);

    Intent intent = new Intent(this, BusDirectionActivity.class);
    Cursor cursor = (Cursor) listView.getItemAtPosition(position);

    String route = cursor.getString(cursor.getColumnIndexOrThrow(BusRouteEntity.Columns.ROUTE_ID));
    String routeName = cursor.getString(cursor.getColumnIndexOrThrow(BusRouteEntity.Columns.ROUTE_NAME));
    Uri data = Uri.parse(route);
    intent.setData(data);
    BusFavoriteEntity busDto = new BusFavoriteEntity();
    busDto.route = route;
    busDto.routeName = routeName;
    intent.putExtra(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY, busDto);

    startActivity(intent);
  }

  private void displayListItems() {

    Cursor cursor = managedQuery(BusRouteContentProvider.CONTENT_URI, BusRouteEntity.Columns.FULL_PROJECTION, null,
        null, BusRouteEntity.Columns._ID);

    listAdapter = new SimpleCursorAdapter(this, R.layout.route_list_item_view, cursor,
        BusRouteEntity.Columns.LIST_VIEW_PROJECTION, VIEW_ROUTE_ID_ARRAY);

    listAdapter.setFilterQueryProvider(new FilterQueryProvider() {

      private CharSequence previous;
      private Cursor filteredCursor;

      public Cursor runQuery(CharSequence constraint) {
        if (constraint.equals(previous)) {
          Log.d(TAG, "skip requery.");
          return filteredCursor;
        }
        String selection = String.format(ROUTE_FILTER_TEMPLATE, constraint, constraint);
        Log.d(TAG, "selection=" + selection);
        filteredCursor = managedQuery(BusRouteContentProvider.CONTENT_URI,
            BusRouteEntity.Columns.FULL_PROJECTION, selection, null, BusRouteEntity.Columns._ID);
        previous = constraint;
        return filteredCursor;
      }
    });
    setListAdapter(listAdapter);
  }

  /**
   * Callback handler receives category list data from database.
   */
  public class BusRouteActivityBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_RESPONSE = "org.djd.busntrain.bus.BusRouteActivityBroadcastReceiver";
    public static final String XML_DATA_TXT = "XML_DATA_TXT";
    public final IntentFilter intentFilter;

    public BusRouteActivityBroadcastReceiver() {
      intentFilter = new IntentFilter(ACTION_RESPONSE);
      intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      String result = "Completed Download and Updated Database.";
      Toast.makeText(BusRouteActivity.this, result, Toast.LENGTH_SHORT).show();
      ApplicationCommons.setBusRouteLastUpdate(BusRouteActivity.this);

      displayListItems();

    }
  }

}
