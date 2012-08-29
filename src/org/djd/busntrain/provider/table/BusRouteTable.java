package org.djd.busntrain.provider.table;

import static android.provider.BaseColumns._ID;
import static org.djd.busntrain.provider.BusRouteContentProvider.BUS_ROUTE_TABLE_NAME;
import static org.djd.busntrain.bus.BusRouteEntity.Columns.*;

import org.djd.busntrain.provider.table.AaaTableHelper.Table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

class BusRouteTable implements Table {
  private static final String TAG = BusRouteTable.class.getSimpleName();
  private static final String BUS_ROUTE_TABLE_DROP_SQL = String.format("DROP TABLE IF EXISTS  %s", BUS_ROUTE_TABLE_NAME);
  private static final String BUS_ROUTE_TABLE_CREATE_SQL = String.format(
      "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT UNIQUE, %s TEXT);",
      BUS_ROUTE_TABLE_NAME, _ID, ROUTE_ID, ROUTE_NAME);

  public BusRouteTable() {
    Log.i(TAG, "table is created.");
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.i(TAG, "Create db table if not yet exists." + BUS_ROUTE_TABLE_CREATE_SQL);
    db.execSQL(BUS_ROUTE_TABLE_CREATE_SQL);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.i(TAG, "Android called onUpgrade() method.");
    db.execSQL(BUS_ROUTE_TABLE_DROP_SQL);
    onCreate(db);
  }

}
