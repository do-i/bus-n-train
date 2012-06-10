package org.djd.busntrain.provider.table;

import static android.provider.BaseColumns._ID;
import static org.djd.busntrain.provider.BusFavoriteContentProvider.BUS_FAVORITE_TABLE_NAME;
import static org.djd.busntrain.bus.BusFavoriteEntity.Columns.*;
import org.djd.busntrain.provider.table.AaaTableHelper.Table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

class BusFavoriteTable implements Table {
	private static final String TAG = BusFavoriteTable.class.getSimpleName();
	private static final String BUS_FAVORITE_TABLE_DROP_SQL = String.format("DROP TABLE IF EXISTS  %s",
	      BUS_FAVORITE_TABLE_NAME);
	private static final String BUS_FAVORITE_TABLE_CREATE_SQL = String.format(
	      "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT , %s TEXT, %s TEXT, %s TEXT, %s TEXT, UNIQUE (%s, %s) ON CONFLICT REPLACE);",
	      BUS_FAVORITE_TABLE_NAME, _ID, ROUTE, STOP_ID, ROUTE_NAME, DIRECTION, STOP_NAME, ROUTE, STOP_ID);

	public BusFavoriteTable() {
		Log.i(TAG, "table is created.");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "Create db table if not yet exists." + BUS_FAVORITE_TABLE_CREATE_SQL);
		db.execSQL(BUS_FAVORITE_TABLE_CREATE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Android called onUpgrade() method.");
		db.execSQL(BUS_FAVORITE_TABLE_DROP_SQL);
		onCreate(db);
	}

}
