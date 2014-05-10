package org.chalup.widgetfail.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.BaseColumns;

public class ItemDeleterService extends IntentService {
  public static PendingIntent getPendingIntent(Context context) {
    return PendingIntent.getService(context, 0, getIntent(context), PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public static Intent getIntent(Context context) {
    return new Intent(context, ItemDeleterService.class);
  }

  public ItemDeleterService() {
    super(ItemDeleterService.class.getSimpleName());
    setIntentRedelivery(true);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Cursor cursor = getContentResolver().query(FailWidgetContentProvider.ITEMS_CONTENT_URI, null, null, null, null);
    try {
      if (cursor.moveToFirst()) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
        getContentResolver().delete(ContentUris.withAppendedId(FailWidgetContentProvider.ITEMS_CONTENT_URI, id), null, null);
      }
    } finally {
      cursor.close();
    }
  }
}
