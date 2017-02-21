package cuhk.edu.hk.mytestapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wataxiwahuohuo on 2017/2/17.
 */

public class MyBroadcastReveive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("seaggy", "onReceive: MyBroadcastReceive");
    }
}
