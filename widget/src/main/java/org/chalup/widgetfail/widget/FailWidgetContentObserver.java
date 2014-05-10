package org.chalup.widgetfail.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.HandlerThread;

class FailWidgetContentObserver extends ContentObserver {
  private final AppWidgetManager mAppWidgetManager;
  private final ComponentName mComponentName;
  private final Handler mHandler;

  private static FailWidgetContentObserver sContentObserver;

  private static Handler getBackgroundThreadHandler() {
    HandlerThread workerThread = new HandlerThread("FailWidget-worker");
    workerThread.start();
    return new Handler(workerThread.getLooper());
  }

  public synchronized static void register(Context context) {
    if (sContentObserver == null) {
      sContentObserver = new FailWidgetContentObserver(context, getBackgroundThreadHandler());
      context.getContentResolver().registerContentObserver(FailWidgetContentProvider.ITEMS_CONTENT_URI, true, sContentObserver);
    }
  }

  private static synchronized boolean isAnyBaseWidgetActive(Context context) {
    final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
    final ComponentName cn = new ComponentName(context, FailWidget.class);

    return mgr.getAppWidgetIds(cn).length > 0;
  }

  public synchronized static void unregisterIfNotNeeded(Context context) {
    if (sContentObserver != null && !isAnyBaseWidgetActive(context)) {
      sContentObserver.unregisterAndStop(context);
      sContentObserver = null;
    }
  }

  FailWidgetContentObserver(Context context, Handler handler) {
    super(handler);

    mHandler = handler;
    mAppWidgetManager = AppWidgetManager.getInstance(context);
    mComponentName = new ComponentName(context, FailWidget.class);
  }

  @Override
  public void onChange(boolean selfChange) {
    mAppWidgetManager.notifyAppWidgetViewDataChanged(
        mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.widget_tasks_list);
  }

  public void unregisterAndStop(Context context) {
    context.getContentResolver().unregisterContentObserver(this);
    mHandler.getLooper().quit();
  }
}
