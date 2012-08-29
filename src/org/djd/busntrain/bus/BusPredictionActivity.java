package org.djd.busntrain.bus;

import java.util.ArrayList;

import org.djd.busntrain.R;
import org.djd.busntrain.bus.BusPredictions.Prediction;
import org.djd.busntrain.commons.StringUtil;
import org.djd.busntrain.commons.XmlUtilException;
import org.djd.busntrain.provider.BusFavoriteContentProvider;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BusPredictionActivity extends ListActivity {

  private static final String TAG = BusPredictionActivity.class.getSimpleName();
  private static final String SAVE_PREDICTION_INFO_KEY = "SAVE_PREDICTION_INFO";
  private static final String SAVE_LAST_UPDATE_TIME_KEY = "SAVE_LAST_UPDATE_TIME_KEY";
  private static final String SAVE_STAR_ON_KEY = "SAVE_STAR_ON_KEY";
  private static final String SAVE_STAR_OFF_KEY = "SAVE_STAR_OFF_KEY";

  private static final int[] VIEW_PREDICTION_ID_ARRAY = new int[]{R.id.prediction_wait_time_in_minutes,
      R.id.prediction_prediction_time, R.id.prediction_vehicle_id, R.id.prediction_route,
      R.id.prediction_destination};
  private BusPredictionActivityBroadcastReceiver receiver;

  private ArrayList<Prediction> predictions;
  private long lastUpdateTime;
  private TextView lastUpdateTimeTextView;
  private ImageButton starOnImageButton;
  private ImageButton starOffImageButton;
  private BusFavoriteEntity busFavoriteEntity;

  @SuppressWarnings("unchecked")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.prediction_list_view);
    lastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time);


    View.OnClickListener clickLIstener = new StarButtonClickLIstener();
    starOnImageButton = (ImageButton) findViewById(R.id.btn_favorite_star_on);
    starOnImageButton.setOnClickListener(clickLIstener);

    starOffImageButton = (ImageButton) findViewById(R.id.btn_favorite_star_off);
    starOffImageButton.setOnClickListener(clickLIstener);

    receiver = new BusPredictionActivityBroadcastReceiver();


    if (null != savedInstanceState) {

      predictions = (ArrayList<Prediction>) savedInstanceState.getSerializable(SAVE_PREDICTION_INFO_KEY);
      lastUpdateTime = savedInstanceState.getLong(SAVE_LAST_UPDATE_TIME_KEY);
      busFavoriteEntity = (BusFavoriteEntity) savedInstanceState
          .getSerializable(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY);
      starOnImageButton.setVisibility(savedInstanceState.getInt(SAVE_STAR_ON_KEY));
      starOffImageButton.setVisibility(savedInstanceState.getInt(SAVE_STAR_OFF_KEY));

      displayListItems();
    } else {

      Intent payload = getIntent();
      if (null == payload) {
        throw new IllegalArgumentException("route and direction is required.");
      }

      busFavoriteEntity = (BusFavoriteEntity) payload
          .getSerializableExtra(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY);

      callBusPredictionService();

      checkFavorite();
      Toast.makeText(this, R.string.toast_getting_predictions, Toast.LENGTH_SHORT).show();
    }

    TextView directionTextView = (TextView) findViewById(R.id.prediction_direction);
    directionTextView.setText(busFavoriteEntity.direction);

    TextView stopNameTextView = (TextView) findViewById(R.id.prediction_stop_name);
    stopNameTextView.setText(busFavoriteEntity.stopName);


  }


  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(SAVE_PREDICTION_INFO_KEY, predictions);
    outState.putLong(SAVE_LAST_UPDATE_TIME_KEY, lastUpdateTime);
    outState.putSerializable(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY, busFavoriteEntity);
    outState.putInt(SAVE_STAR_ON_KEY, starOnImageButton.getVisibility());
    outState.putInt(SAVE_STAR_OFF_KEY, starOffImageButton.getVisibility());
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

    Toast.makeText(this, R.string.msg_refresh, Toast.LENGTH_SHORT).show();
    callBusPredictionService();
  }

  private void callBusPredictionService() {
    Intent intent = new Intent(this, BusPredictionService.class);
    intent.putExtra(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY, busFavoriteEntity);
    startService(intent);
  }

  private void checkFavorite() {
    String where = String.format("%s=? and %s=?", BusFavoriteEntity.Columns.ROUTE, BusFavoriteEntity.Columns.STOP_ID);
    String whereArgs[] = new String[]{busFavoriteEntity.route, busFavoriteEntity.stopId};

    Cursor cursor = managedQuery(BusFavoriteContentProvider.CONTENT_URI, BusFavoriteEntity.Columns.FULL_PROJECTION,
        where, whereArgs, BusFavoriteEntity.Columns._ID);

    if (1 == cursor.getCount()) {
      busFavoriteEntity = BusFavoriteEntity.Helper.createBusFavoriteEntity(cursor, 0);
      if (isFavorite()) {
        Log.i(TAG, "Star is already shining.");
      } else {
        toggle();
      }
    }
  }

  private void displayListItems() {

    ListAdapter listAdapter = new SimpleAdapter(this, predictions, R.layout.prediction_list_item_view,
        Prediction.COLUMNS, VIEW_PREDICTION_ID_ARRAY);
    lastUpdateTime = System.currentTimeMillis();
    lastUpdateTimeTextView.setText(StringUtil.timeToString(this, lastUpdateTime));
    setListAdapter(listAdapter);
  }

  /**
   * This is workaround to get imageButton toggle feature.
   * <p/>
   * TODO improve image button toggle code.
   * <p/>
   * Also, save the BusDto to Database
   */
  public class StarButtonClickLIstener implements OnClickListener {

    @Override
    public void onClick(View v) {
      if (isFavorite()) {
        removeStar();
      } else {
        addStar();
      }
      toggle();
    }

    private void addStar() {
      ContentResolver contentResolver = getContentResolver();
      contentResolver.insert(BusFavoriteContentProvider.CONTENT_URI, busFavoriteEntity.getContentValuesForInsert());

    }

    private void removeStar() {
      ContentResolver contentResolver = getContentResolver();
      Uri uri = Uri.withAppendedPath(BusFavoriteContentProvider.CONTENT_URI, String.valueOf(busFavoriteEntity.id));
      contentResolver.delete(uri, null, null);

    }


  }

  private boolean isFavorite() {
    return Button.VISIBLE == starOnImageButton.getVisibility();
  }

  private void toggle() {
    int visibility = starOnImageButton.getVisibility();
    starOnImageButton.setVisibility(starOffImageButton.getVisibility());
    starOffImageButton.setVisibility(visibility);
  }

  /**
   * Callback handler receives category list data from database.
   */
  public class BusPredictionActivityBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_RESPONSE = "org.djd.busntrain.bus.BusPredictionActivityBroadcastReceiver";
    public static final String XML_DATA_TXT = "XML_DATA_TXT";
    public final IntentFilter intentFilter;

    public BusPredictionActivityBroadcastReceiver() {
      intentFilter = new IntentFilter(ACTION_RESPONSE);
      intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      String result = "Completed Download and Updated Database.";
      Toast.makeText(BusPredictionActivity.this, result, Toast.LENGTH_SHORT).show();
      String xmlDataTxt = intent.getStringExtra(XML_DATA_TXT);

      try {
        predictions = BusPredictions.parseValue(xmlDataTxt);
      } catch (XmlUtilException e) {
        throw new RuntimeException(e);
      }

      displayListItems();

    }
  }

}
