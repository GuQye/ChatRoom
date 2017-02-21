package cuhk.edu.hk.mytestapplication;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by wataxiwahuohuo on 2017/2/21.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
