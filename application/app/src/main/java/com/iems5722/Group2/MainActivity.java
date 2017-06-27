package com.iems5722.Group2;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = (Button)findViewById(R.id.login);
        Button roomlist = (Button)findViewById(R.id.roomlist);
        Button gameroom = (Button)findViewById(R.id.gameroom);
        Button gaming = (Button)findViewById(R.id.gaming);
        Button record = (Button)findViewById(R.id.record);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.putExtra("room_id","1");
                intent.putExtra("user_name","admin");
                intent.putExtra("user_id",4);
                startActivity(intent);
            }
        });
        roomlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RoomListActivity.class);
                intent.putExtra("room_id","1");
                intent.putExtra("user_name","admin");
                intent.putExtra("user_id",4);
                startActivity(intent);
            }
        });
        gameroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GameRoom.class);
                intent.putExtra("room_id","1");
                intent.putExtra("user_name","admin");
                intent.putExtra("user_id",4);
                startActivity(intent);
            }
        });
        gaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Gaming.class);
                intent.putExtra("room_id","1");
                intent.putExtra("user_name","admin");
                intent.putExtra("user_id",4);
                startActivity(intent);
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,audioActivity.class);
                intent.putExtra("room_id","1");
                intent.putExtra("user_name","admin");
                intent.putExtra("user_id",4);
                startActivity(intent);
            }
        });
    }

}