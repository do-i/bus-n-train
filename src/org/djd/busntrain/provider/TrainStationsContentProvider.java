package org.djd.busntrain.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import org.djd.busntrain.provider.table.AaaTableHelper;

import static org.djd.busntrain.commons.ApplicationCommons.SCHEME;

public class TrainStationsContentProvider extends ContentProvider {

  public static final String TRAIN_STATIONS_TABLE_NAME = "TRAIN_STATIONS";

  public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.djd.busntrans.train.stations";
  private static final String AUTHORITY = "org.djd.busntrain.provider.trainstationscontentprovider";
  private static final String PATH_TRAIN_STATIONS = "/trainstations/";
  public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_TRAIN_STATIONS);

  private SQLiteDatabase database;

  private static final int TRAIN_STATIONS = 1;

  private static final UriMatcher URI_MATCHER;

  static {
    URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    URI_MATCHER.addURI(AUTHORITY, PATH_TRAIN_STATIONS, TRAIN_STATIONS);
  }

  @Override
  public String getType(Uri uri) {
    switch (URI_MATCHER.match(uri)) {
      case TRAIN_STATIONS:
        return CONTENT_TYPE;
      default:
        throw new IllegalArgumentException("Unsupported URI: " + uri);
    }
  }

  @Override
  public boolean onCreate() {
    Context context = getContext();
    AaaTableHelper dbHelper = new AaaTableHelper(context);
    database = dbHelper.getWritableDatabase();
    return (null != database);
  }

  @Override
  public Uri insert(Uri uri, ContentValues contentValues) {
    long id = database.replace(TRAIN_STATIONS_TABLE_NAME, null, contentValues);
    if (0 <= id) {
      Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
      getContext().getContentResolver().notifyChange(_uri, null);
      return _uri;
    }
    throw new SQLException("Failed to insert row into " + uri);
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    return database.query(TRAIN_STATIONS_TABLE_NAME, projection, selection, selectionArgs,
        null, null, sortOrder);
  }

  @Override
  public int update(Uri uri, ContentValues arg1, String arg2, String[] arg3) {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public int delete(Uri uri, String whereClause, String[] whereArgs) {
    return database.delete(TRAIN_STATIONS_TABLE_NAME, whereClause, whereArgs);
  }
}
