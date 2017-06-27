package com.iems5722.Group2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RoomListActivity extends AppCompatActivity {
    private List<Room> chatroom_list = new ArrayList<Room>();
    private String name = "guquanye";
    private ListView main_listview;
    private boolean flag = true;
    private String user_name;
    private int user_id;
    private static final String TAG = "guquanye";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Handler mHandle;
    private Runnable mRunnable;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private ImageView medal;
    private Button a1;
    private Button a2;
    private Button a3;
    private Button a4;
    private Button a5;
    private Button a6;
    private int level;
    private int score;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_roomlist);

        setTitle(R.string.activity_main_label);
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        user_id = intent.getIntExtra("user_id",0);
        Log.d(TAG, "onCreate: user_id"+user_id);
        textView1 = (TextView)findViewById(R.id.username);
        textView2 = (TextView)findViewById(R.id.score);
        textView3 = (TextView)findViewById(R.id.rank);
        textView4 = (TextView)findViewById(R.id.shengchang);
        textView5 = (TextView)findViewById(R.id.level);
        medal = (ImageView)findViewById(R.id.medal);
        a1 = (Button) findViewById(R.id.attend1);
        a2 = (Button) findViewById(R.id.attend2);
        a3 = (Button) findViewById(R.id.attend3);
        a4 = (Button) findViewById(R.id.attend4);
        a5 = (Button) findViewById(R.id.attend5);
        a6 = (Button) findViewById(R.id.attend6);
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGameroom(1);
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGameroom(2);
            }
        });
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGameroom(3);
            }
        });
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGameroom(4);
            }
        });
        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGameroom(5);
            }
        });
        a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGameroom(6);
            }
        });

        a1.setText("0/3");
        a2.setText("0/3");
        a3.setText("0/3");
        a4.setText("0/3");
        a5.setText("0/3");
        a6.setText("0/3");

        score = (int)(Math.random()*5000)+1;

        textView1.setText(user_name);
        textView2.setText("score= "+score);
        textView3.setText("rank= "+10000/score);
        textView4.setText("win= "+score/20);
        level = (int)Math.log(score/2);
        if (level<=1){
            level = 1;
        }else if (level >= 8 ){
            level = 8;
        }
        textView5.setText("level"+level);
        switch (level){
            case 1:
                medal.setImageResource(R.drawable.level1);
                break;
            case 2:
                medal.setImageResource(R.drawable.level2);
                break;
            case 3:
                medal.setImageResource(R.drawable.level3);
                break;
            case 4:
                medal.setImageResource(R.drawable.level4);
                break;
            case 5:
                medal.setImageResource(R.drawable.level5);
                break;
            case 6:
                medal.setImageResource(R.drawable.level6);
                break;
            case 7:
                medal.setImageResource(R.drawable.level7);
                break;
            case 8:
                medal.setImageResource(R.drawable.level8);
                break;
        }

        mHandle = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                init_roomlist();
                mHandle.postDelayed(this,1000);
            }
        };
        init_roomlist();





        /*main_listview = (ListView) findViewById(R.id.main_listview);
        chatroom = new RoomAdapter(RoomListActivity.this, R.layout.chatroom_item, chatroom_list);
        chatroom.notifyDataSetChanged();
        main_listview.setAdapter(chatroom);
        main_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = chatroom_list.get(position);
                Intent activity2 = new Intent(RoomListActivity.this, GameRoom.class);
                flag = ServicesAvailable(RoomListActivity.this);
                Log.d("ServicesAvailable", String.valueOf(flag));
                String title = room.getName().toString();
                int room_id = room.getId();
                int num = room.getPlayerNums();
                if(num>3){
                    Log.d(TAG, "onItemClick: error");
                }
                else{
                    new BackgroundTask().execute("",String.valueOf(room_id));
                    activity2.putExtra("user_name",user_name);
                    activity2.putExtra("room_id",String.valueOf(room_id));
                    startActivity(activity2);
                }

            }
        });*/
    }

    private void gotoGameroom(int room_id) {
        new BackgroundTask().execute("",String.valueOf(room_id));
        Intent intent = new Intent(RoomListActivity.this,GameRoom.class);
        intent.putExtra("room_id",String.valueOf(room_id));
        intent.putExtra("user_id",user_id);
        intent.putExtra("user_name",user_name);
        startActivity(intent);
    }






    public void init_roomlist() {
        mHandle.removeCallbacks(mRunnable);
        mHandle.postDelayed(mRunnable,1000);
        String path = "http://54.169.112.52:5000/get_rooms";
        new BackgroundTask().execute(path);

    }
    class BackgroundTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {

            if(params[0]!=""){
                try {
                    Log.d(TAG, "doInBackground: " + params[0]);
                    String path = String.format(params[0]);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(path).build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "doInBackground: " + responseData);
                    return responseData;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Log.d(TAG, "doInBackground: room_id" + params[1]);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_name", user_name)
                            .add("room_id",params[1])
                            .add("user_id",String.valueOf(user_id))
                            .build();
                    Log.d(TAG, "doInBackground: user_id"+user_id);

                    Request request = new Request.Builder()
                            .url("http://54.169.112.52:5000/enter_room")
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    return responseData;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            try {
                JSONObject json = new JSONObject(jsonString);
                String status = json.getString("status");
                if("OK".equals(status)){
                    Map<Integer,Integer> map = new HashMap<>();
                    JSONArray rooms_list = json.getJSONArray("data");
                    for (int i = 0; i < rooms_list.length(); i++) {

                        JSONObject room = rooms_list.getJSONObject(i);
                        //Log.d(TAG, "onPostExecute: ");
                        String name =room.getString("name");
                        int id = room.getInt("id");
                        map.put(id,1);
                        int playerNums = room.getInt("num");
                        Room room1 = new Room(id,name,playerNums);
                        switch (id){
                            case 1:
                                a1.setText(playerNums+"/3");
                                if(playerNums>=3){
                                    a1.setTextColor(0xFFFF0000);
                                    a1.setEnabled(false);
                                }
                                else{
                                    a1.setTextColor(0XFF6699FF);
                                    a1.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 2:
                                a2.setText(playerNums+"/3");
                                if(playerNums>=3){
                                    a2.setTextColor(0xFFFF0000);
                                    a2.setEnabled(false);
                                }
                                else {
                                    a2.setVisibility(View.VISIBLE);
                                    a2.setTextColor(0XFF6699FF);
                                }
                                    break;
                            case 3:
                                a3.setText(playerNums+"/3");
                                if(playerNums>=3){
                                    a3.setTextColor(0xFFFF0000);
                                    a3.setEnabled(false);
                                }
                                else {
                                    a3.setVisibility(View.VISIBLE);
                                    a3.setTextColor(0XFF6699FF);
                                }
                                break;
                            case 4:
                                a4.setText(playerNums+"/3");
                                if(playerNums>=3){
                                    a4.setTextColor(0xFFFF0000);
                                    a4.setEnabled(false);
                                }
                                else {
                                    a4.setVisibility(View.VISIBLE);
                                    a4.setTextColor(0XFF6699FF);
                                }
                                break;
                            case 5:
                                a5.setText(playerNums+"/3");
                                if(playerNums>=3){
                                    a5.setTextColor(0xFFFF0000);
                                    a5.setEnabled(false);
                                }
                                else {
                                    a5.setVisibility(View.VISIBLE);
                                    a5.setTextColor(0XFF6699FF);
                                }
                                break;
                            case 6:
                                a6.setText(playerNums+"/3");
                                if(playerNums>=3){
                                    a6.setTextColor(0xFFFF0000);
                                    a6.setEnabled(false);
                                }
                                else {
                                    a6.setVisibility(View.VISIBLE);
                                    a6.setTextColor(0XFF6699FF);
                                }
                                break;
                        }

                    }

                }
                Log.d(TAG, "onPostExecute: 231312321");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

}
