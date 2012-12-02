package org.djd.busntrain.provider.table;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.djd.busntrain.provider.table.AaaTableHelper.Table;

import static android.provider.BaseColumns._ID;
import static org.djd.busntrain.provider.TrainStationsContentProvider.TRAIN_STATIONS_TABLE_NAME;
import static org.djd.busntrain.train.TrainStationsEntity.Columns.COLOR;
import static org.djd.busntrain.train.TrainStationsEntity.Columns.DESTINATION;
import static org.djd.busntrain.train.TrainStationsEntity.Columns.SEQUENCE;
import static org.djd.busntrain.train.TrainStationsEntity.Columns.STOP_ID;
import static org.djd.busntrain.train.TrainStationsEntity.Columns.STOP_NAME;

class TrainStationsTable implements Table {
  private static final String TAG = TrainStationsTable.class.getSimpleName();
  private static final String TRAIN_STATIONS_TABLE_DROP_SQL =
      String.format("DROP TABLE IF EXISTS  %s", TRAIN_STATIONS_TABLE_NAME);
  private static final String TRAIN_STATIONS_TABLE_CREATE_SQL = String.format(
      "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, " +
          "%s TEXT, %s TEXT, %s INTEGER);",
      TRAIN_STATIONS_TABLE_NAME, _ID, STOP_ID, STOP_NAME, COLOR, DESTINATION, SEQUENCE);

  public TrainStationsTable() {
    Log.i(TAG, "table is created.");
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.i(TAG, "Create db table if not yet exists." + TRAIN_STATIONS_TABLE_CREATE_SQL);
    db.execSQL(TRAIN_STATIONS_TABLE_CREATE_SQL);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.i(TAG, "Android called onUpgrade() method.");
    db.execSQL(TRAIN_STATIONS_TABLE_DROP_SQL);
    onCreate(db);
  }
}