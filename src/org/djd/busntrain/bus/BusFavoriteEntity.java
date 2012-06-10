package org.djd.busntrain.bus;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

public class BusFavoriteEntity implements Serializable {
	/**
    * 
    */
   private static final long serialVersionUID = -1425538076637127557L;

   public static final String BUS_FAVORITE_ENTITY_EXTRA_DATA_KEY = "BusFavoriteEntityExtraDataKey";

	public long id;

	public String route;
	public String stopId;

	

	public String routeName;
	public String direction;
	public String stopName;

	public ContentValues getContentValues() {

		ContentValues contentValues = new ContentValues();
		contentValues.put(BusFavoriteEntity.Columns._ID, id);
		contentValues.put(BusFavoriteEntity.Columns.ROUTE, route);
		contentValues.put(BusFavoriteEntity.Columns.STOP_ID, stopId);

		contentValues.put(BusFavoriteEntity.Columns.ROUTE_NAME, routeName);
		contentValues.put(BusFavoriteEntity.Columns.DIRECTION, direction);
		contentValues.put(BusFavoriteEntity.Columns.STOP_NAME, stopName);
		return contentValues;

	}

	public ContentValues getContentValuesForInsert() {

		ContentValues contentValues = new ContentValues();
		contentValues.put(BusFavoriteEntity.Columns.ROUTE, route);
		contentValues.put(BusFavoriteEntity.Columns.STOP_ID, stopId);
		contentValues.put(BusFavoriteEntity.Columns.ROUTE_NAME, routeName);
		contentValues.put(BusFavoriteEntity.Columns.DIRECTION, direction);
		contentValues.put(BusFavoriteEntity.Columns.STOP_NAME, stopName);
		return contentValues;

	}

	public static final class Columns implements BaseColumns {

		public String route;
		public String stopId;

		public String routeName;
		public String direction;
		public String stopName;

		public static final String ROUTE = "ROUTE";
		public static final String STOP_ID = "STOP_ID";

		public static final String ROUTE_NAME = "ROUTE_NAME";
		public static final String DIRECTION = "DIRECTION";
		public static final String STOP_NAME = "STOP_NAME";

		public static final String[] FULL_PROJECTION = { _ID, ROUTE, STOP_ID, ROUTE_NAME, DIRECTION, STOP_NAME };
		public static final String[] LIST_VIEW_PROJECTION = { ROUTE, STOP_ID, ROUTE_NAME, DIRECTION, STOP_NAME };

		private Columns() {
		}

	}
	
	public static final class Helper{
		public static BusFavoriteEntity createBusFavoriteEntity(Cursor cursor, int position){
			cursor.moveToPosition(position);
			BusFavoriteEntity busFavoriteEntity = new BusFavoriteEntity();
			
			busFavoriteEntity.id = cursor.getLong(cursor.getColumnIndexOrThrow(Columns._ID));
			busFavoriteEntity.route = cursor.getString(cursor.getColumnIndexOrThrow(Columns.ROUTE));
			busFavoriteEntity.stopId = cursor.getString(cursor.getColumnIndexOrThrow(Columns.STOP_ID));
			busFavoriteEntity.direction = cursor.getString(cursor.getColumnIndexOrThrow(Columns.DIRECTION));
			busFavoriteEntity.stopName = cursor.getString(cursor.getColumnIndexOrThrow(Columns.STOP_NAME));
			
			return  busFavoriteEntity ;
		}
	}

	
	@Override
   public String toString() {
	   return String.format("BusFavoriteEntity [id=%s, route=%s, stopId=%s, routeName=%s, direction=%s, stopName=%s]",
	         id, route, stopId, routeName, direction, stopName);
   }
}
