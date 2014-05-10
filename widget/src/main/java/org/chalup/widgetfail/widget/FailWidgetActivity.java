package org.chalup.widgetfail.widget;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class FailWidgetActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fail_widget);

    findViewById(R.id.add_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getContentResolver().insert(FailWidgetContentProvider.ITEMS_CONTENT_URI, new ContentValues());
      }
    });

    findViewById(R.id.remove_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startService(ItemDeleterService.getIntent(FailWidgetActivity.this));
      }
    });

    findViewById(R.id.remove_all_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getContentResolver().delete(FailWidgetContentProvider.ITEMS_CONTENT_URI, null, null);
      }
    });
  }
}
