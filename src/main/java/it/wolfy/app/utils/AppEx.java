package it.wolfy.app.utils;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.Tracker;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.HashMap;

public class AppEx extends MultiDexApplication
{
    private static AppEx instance;

    public static AppEx getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA--6";

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName
    {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public AppEx()
    {
        super();
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        instance=this;
        try
        {
            JodaTimeAndroid.init(this);
//            ViewConfiguration config = ViewConfiguration.get(this);
//            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//            if ( menuKeyField != null )
//            {
//                menuKeyField.setAccessible(true);
//                menuKeyField.setBoolean(config, false);
//            }
//           CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                                                  .setDefaultFontPath("RobotoRegular.ttf")
//                                                  .setFontAttrId(R.attr.fontPath)
//                                                  .build());
        }
        catch (Exception ex)
        {
            // Ignore
        }
    }

   /* public synchronized Tracker getTracker(TrackerName trackerId)
    {
        if ( !mTrackers.containsKey(trackerId) )
        {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.global_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }*/

}