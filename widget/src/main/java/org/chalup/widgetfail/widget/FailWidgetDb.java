package org.chalup.widgetfail.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FailWidgetDb extends SQLiteOpenHelper {
  public FailWidgetDb(Context context) {
    super(context, "fail_widget_db", null, 1);
  }

  interface Tables {
    String ITEMS = "items";
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + Tables.ITEMS + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT)");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    throw new UnsupportedOperationException();
  }
}
