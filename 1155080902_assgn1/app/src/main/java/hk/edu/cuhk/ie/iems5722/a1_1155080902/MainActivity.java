package hk.edu.cuhk.ie.iems5722.a1_1155080902;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hk.edu.cuhk.ie.iems5722.a1_1155080902.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.activity_main_label);
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity2 = new Intent(MainActivity.this, Activity2.class);
                startActivity(activity2);
            }
        });

    }

    @Override
    protected void onPause() {

        super.onPause();
    }
}
