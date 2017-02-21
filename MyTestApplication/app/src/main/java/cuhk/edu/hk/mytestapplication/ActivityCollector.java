package cuhk.edu.hk.mytestapplication;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wataxiwahuohuo on 2017/2/21.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
