package org.chalup.widgetfail.widget;

import org.chalup.widgetfail.widget.FailWidgetDb.Tables;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

public class FailWidgetContentProvider extends ContentProvider {
  public static final String AUTHORITY = "org.chalup.failwidget";
  public static Uri ITEMS_CONTENT_URI = new Uri.Builder()
      .authority(AUTHORITY)
      .scheme(ContentResolver.SCHEME_CONTENT)
      .appendPath(Tables.ITEMS)
      .build();

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase database = mDb.getWritableDatabase();

    try {
      if (ITEMS_CONTENT_URI.equals(uri)) {
        return database.delete(Tables.ITEMS, null, null);
      } else {
        return database.delete(
            Tables.ITEMS,
            BaseColumns._ID + "=?",
            new String[] { uri.getLastPathSegment() }
        );
      }
    } finally {
      getContext().getContentResolver().notifyChange(uri, null, false);
    }
  }

  @Override
  public String getType(Uri uri) {
    if (uri.equals(ITEMS_CONTENT_URI)) {
      return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Tables.ITEMS;
    } else {
      return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Tables.ITEMS;
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    Uri insertedUri = ContentUris.withAppendedId(
        ITEMS_CONTENT_URI,
        mDb.getWritableDatabase().insert(Tables.ITEMS, BaseColumns._ID, values)
    );

    getContext().getContentResolver().notifyChange(insertedUri, null, false);

    return insertedUri;
  }

  SQLiteOpenHelper mDb;

  @Override
  public boolean onCreate() {
    mDb = new FailWidgetDb(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    return mDb.getReadableDatabase().query(Tables.ITEMS, null, null, null, null, null, null);
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
