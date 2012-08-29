package org.djd.busntrain.provider;

import static org.djd.busntrain.commons.ApplicationCommons.SCHEME;

import org.djd.busntrain.provider.table.AaaTableHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class BusRouteContentProvider extends ContentProvider {

  /**
   *
   */
  public static final String BUS_ROUTE_TABLE_NAME = "BUS_ROUTE";

  public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.djd.busntrans.bus.route";
  private static final String AUTHORITY = "org.djd.busntrain.provider.busroutecontentprovider";
  private static final String PATH_BUS_ROUTE = "/busroute/";
  public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_BUS_ROUTE);

  private SQLiteDatabase database;

  private static final int BUS_ROUTE = 1;

  private static final UriMatcher URI_MATCHER;

  static {
    URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    URI_MATCHER.addURI(AUTHORITY, PATH_BUS_ROUTE, BUS_ROUTE);
  }

  @Override
  public String getType(Uri uri) {
    switch (URI_MATCHER.match(uri)) {
      case BUS_ROUTE:
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

    long id = database.replace(BUS_ROUTE_TABLE_NAME, null, contentValues);
    if (0 <= id) {
      Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
      getContext().getContentResolver().notifyChange(_uri, null);
      return _uri;
    }
    throw new SQLException("Failed to insert row into " + uri);

  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    return database.query(BUS_ROUTE_TABLE_NAME, projection, selection, selectionArgs,
        null, null, sortOrder);

  }

  @Override
  public int update(Uri uri, ContentValues arg1, String arg2, String[] arg3) {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public int delete(Uri uri, String whereClause, String[] whereArgs) {
    return database.delete(BUS_ROUTE_TABLE_NAME, whereClause, whereArgs);
  }

}
