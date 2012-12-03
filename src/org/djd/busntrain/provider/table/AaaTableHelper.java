package org.djd.busntrain.provider.table;

import static org.djd.busntrain.commons.ApplicationCommons.DATABASE_NAME;
import static org.djd.busntrain.commons.ApplicationCommons.DATABASE_VERSION;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AaaTableHelper extends SQLiteOpenHelper {

  private static final String TAG = AaaTableHelper.class.getSimpleName();

  private ArrayList<Table> tables;

  public AaaTableHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  protected AaaTableHelper(Context context, boolean test) {
    super(context, null, null, DATABASE_VERSION);
    Log.d(TAG, "In-Memory Database is created.");
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.i(TAG, "onCreate is invoked.");
    if (null == tables) {
      tables = AaaTableHelper.createAllTables();
    }
    for (Table table : tables) {
      table.onCreate(db);
    }
  }

  @Override
  public void onOpen(SQLiteDatabase db) {
    Log.i(TAG, "onOpen is invoked.");
    super.onOpen(db);
    if (!db.isReadOnly()) {
      db.execSQL("PRAGMA foreign_keys=ON;");
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.i(TAG, "onUpgrade is invoked.");
    if (null == tables) {
      tables = AaaTableHelper.createAllTables();
    }
    for (Table table : tables) {
      table.onUpgrade(db, oldVersion, newVersion);
    }
  }

  /**
   * Implement this interface and use {@link AaaTableHelper} to register this
   * table to DatabaseHelper for initialization.
   */
  public static interface Table {
    public void onCreate(SQLiteDatabase db);
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
  }

  /**
   * Manage all the tables for this application here.
   */
  public static final ArrayList<Table> createAllTables() {
    ArrayList<Table> mTables = new ArrayList<Table>();
    mTables.add(new BusRouteTable());
    mTables.add(new BusFavoriteTable());
    mTables.add(new TrainStationsTable());
    mTables.add(new TrainStopsTable());
    return mTables;
  }
}