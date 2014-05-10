package org.chalup.widgetfail.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class FailWidgetService extends RemoteViewsService {
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new FailWidgetViewsFactory(getApplicationContext());
  }

  private class FailWidgetViewsFactory implements RemoteViewsFactory {
    private final Context mContext;

    private Cursor mCursor;

    public FailWidgetViewsFactory(Context context) {
      mContext = context;
    }

    @Override
    public void onCreate() {
      FailWidgetContentObserver.register(mContext);
    }

    @Override
    public void onDataSetChanged() {
      if (mCursor != null) {
        mCursor.close();
      }

      mCursor = mContext.getContentResolver().query(FailWidgetContentProvider.ITEMS_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onDestroy() {
      if (mCursor != null) {
        mCursor.close();
      }
      FailWidgetContentObserver.unregisterIfNotNeeded(mContext);
    }

    @Override
    public int getCount() {
      return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
      RemoteViews views = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);

      mCursor.moveToPosition(position);
      views.setTextViewText(android.R.id.text1, mCursor.getString(mCursor.getColumnIndexOrThrow(BaseColumns._ID)));

      return views;
    }

    @Override
    public RemoteViews getLoadingView() {
      return new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
    }

    @Override
    public int getViewTypeCount() {
      return 1;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public boolean hasStableIds() {
      return false;
    }
  }
}
