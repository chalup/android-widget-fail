package org.chalup.widgetfail.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.RemoteViews;

public class FailWidget extends AppWidgetProvider {
  public static final String ACTION_ADD = "ADD";

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context) {
    FailWidgetContentObserver.register(context);
  }

  @Override
  public void onDisabled(Context context) {
    FailWidgetContentObserver.unregisterIfNotNeeded(context);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    if (ACTION_ADD.equals(intent.getAction())) {
      FailWidgetContentObserver.register(context);
      context.getContentResolver().insert(FailWidgetContentProvider.ITEMS_CONTENT_URI, new ContentValues());
    } else {
      super.onReceive(context, intent);
    }
  }

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fail_widget);

    views.setOnClickPendingIntent(
        R.id.widget_add_button,
        PendingIntent.getBroadcast(
            context,
            0,
            new Intent(context, FailWidget.class).setAction(ACTION_ADD),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    );

    views.setOnClickPendingIntent(
        R.id.widget_remove_button,
        ItemDeleterService.getPendingIntent(context)
    );

    views.setOnClickPendingIntent(
        R.id.widget_open_activity,
        PendingIntent.getActivity(
            context,
            0,
            new Intent(context, FailWidgetActivity.class),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    );

    final Intent intent = new Intent(context, FailWidgetService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

    views.setRemoteAdapter(R.id.widget_tasks_list, intent);
    views.setEmptyView(R.id.widget_tasks_list, android.R.id.empty);

    appWidgetManager.updateAppWidget(appWidgetId, views);
  }
}
