package org.djd.busntrain.provider.table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.djd.busntrain.provider.table.AaaTableHelper.Table;

import static android.provider.BaseColumns._ID;
import static org.djd.busntrain.provider.TrainStopsContentProvider.TRAIN_STOPS_TABLE_NAME;
import static org.djd.busntrain.train.TrainStopsEntity.Columns.DIRECTION;
import static org.djd.busntrain.train.TrainStopsEntity.Columns.LAT;
import static org.djd.busntrain.train.TrainStopsEntity.Columns.LON;
import static org.djd.busntrain.train.TrainStopsEntity.Columns.PARENT_STOP_ID;
import static org.djd.busntrain.train.TrainStopsEntity.Columns.STATION_DESCRIPTION_NAME;
import static org.djd.busntrain.train.TrainStopsEntity.Columns.STATION_NAME;
import static org.djd.busntrain.train.TrainStopsEntity.Columns.STOP_ID;
import static org.djd.busntrain.train.TrainStopsEntity.Columns.STOP_NAME;

class TrainStopsTable implements Table {
  private static final String TAG = TrainStopsTable.class.getSimpleName();
  private static final String TRAIN_STOPS_TABLE_DROP_SQL =
      String.format("DROP TABLE IF EXISTS  %s", TRAIN_STOPS_TABLE_NAME);
  private static final String TRAIN_STOPS_TABLE_CREATE_SQL = String.format(
      "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, " +
          "%s TEXT, %s REAL, %s REAL, %s TEXT, %s TEXT, %s INTEGER);",
      TRAIN_STOPS_TABLE_NAME, _ID, STOP_ID, STOP_NAME, DIRECTION, LON, LAT, STATION_NAME,
      STATION_DESCRIPTION_NAME, PARENT_STOP_ID);

  public TrainStopsTable() {
    Log.i(TAG, "table is created.");
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.i(TAG, "Create db table if not yet exists." + TRAIN_STOPS_TABLE_CREATE_SQL);
    db.execSQL(TRAIN_STOPS_TABLE_CREATE_SQL);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//    Log.i(TAG, "Android called onUpgrade() method.");
//    db.execSQL(TRAIN_STOPS_TABLE_DROP_SQL);
//    onCreate(db);
  }
}