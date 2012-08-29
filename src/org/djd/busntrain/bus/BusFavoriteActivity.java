package org.djd.busntrain.bus;

import org.djd.busntrain.R;
import org.djd.busntrain.provider.BusFavoriteContentProvider;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class BusFavoriteActivity extends ListActivity implements OnItemLongClickListener {

  private static final String TAG = BusFavoriteActivity.class.getSimpleName();

  private static final int[] VIEW_FAVORITE_ID_ARRAY = new int[]{R.id.favorite_route, R.id.favorite_stop_id,
      R.id.favorite_route_name, R.id.favorite_direction, R.id.favorite_stop_name};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_list_view);


    getListView().setOnItemLongClickListener(this);
    displayListItems();
  }

  /**
   * @return true so that onLIstItemClick handler won't be called.
   */
  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

    final BusFavoriteEntity busFavoriteEntity = BusFavoriteEntity.Helper.createBusFavoriteEntity(cursor, position);

    new AlertDialog.Builder(this).setTitle(R.string.msg_delete_dialog_title).setMessage(busFavoriteEntity.stopName)
        .setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            ContentResolver contentResolver = getContentResolver();
            //
            Uri uri = Uri.withAppendedPath(BusFavoriteContentProvider.CONTENT_URI, String.valueOf(busFavoriteEntity.id));

            int deleteCount = contentResolver.delete(uri, null, null);
            if (1 == deleteCount) {

              Toast.makeText(getBaseContext(), "Item deleted.", Toast.LENGTH_SHORT).show();
              displayListItems();
            }

          }

        }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        // do nothing.
      }

    }).show();

    return true;

  }

  // private CharSequence getMessage() {
  //
  // return "TODO: compose bus stop info.";
  // }

  @Override
  protected void onListItemClick(ListView listView, View v, int position, long id) {
    super.onListItemClick(listView, v, position, id);

    Intent intent = new Intent(this, BusPredictionActivity.class);
    Cursor cursor = (Cursor) listView.getItemAtPosition(position);

    BusFavoriteEntity busFavoriteEntity = BusFavoriteEntity.Helper.createBusFavoriteEntity(cursor, position);
    intent.putExtra(BusFavoriteEntity.BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY, busFavoriteEntity);
    startActivity(intent);

    Log.i(TAG, "favorite item selected. " + busFavoriteEntity);
  }

  private void displayListItems() {

    Cursor cursor = managedQuery(BusFavoriteContentProvider.CONTENT_URI, BusFavoriteEntity.Columns.FULL_PROJECTION,
        null, null, BusFavoriteEntity.Columns._ID);

    ListAdapter listAdapter = new SimpleCursorAdapter(this, R.layout.favorite_list_item_view, cursor,
        BusFavoriteEntity.Columns.LIST_VIEW_PROJECTION, VIEW_FAVORITE_ID_ARRAY);

    setListAdapter(listAdapter);
  }

}
