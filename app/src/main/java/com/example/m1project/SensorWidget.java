package com.example.m1project;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SensorWidgetConfigureActivity SensorWidgetConfigureActivity}
 */
public class SensorWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.row_sensor_info);
        SharedPreferences sharedPref =
                context.getSharedPreferences("com.example.m1project.SensorWidget",Context.MODE_PRIVATE);
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        ArrayList<Sensor> sensors = new ArrayList<>(sensorManager.getSensorList(Sensor.TYPE_ALL));
        Sensor s = sensors.get(0);
        for(Sensor s1 : sensors)
            if(s1.getName().equals(sharedPref.getString("appwidget_","")))
                s = s1;
        if(s!=null) {
            views.setTextViewText(R.id.NameNonMultiChoice,s.getName());
            views.setTextViewText(R.id.MaxRange,s.getMaximumRange()+"");
            views.setTextViewText(R.id.Power,s.getPower()+" mA");
            views.setTextViewText(R.id.Resolution,s.getResolution()+"");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH)
                views.setTextViewText(R.id.Type,s.getStringType());
            else
                views.setTextViewText(R.id.Type,s.getType()+"");
            views.setTextViewText(R.id.Vendor,s.getVendor());
            views.setTextViewText(R.id.Version_Mine,"v"+s.getVersion());
            views.setTextViewText(R.id.MinDelay_Mine,s.getMinDelay()+" ms MINIMUM");
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SensorWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}