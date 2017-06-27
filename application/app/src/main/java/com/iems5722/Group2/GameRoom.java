package com.iems5722.Group2;

/**
 * Created by wataxiwahuohuo on 2017/1/20.
 */


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GameRoom extends AppCompatActivity {
    private List<Player> playerList = new ArrayList<Player>();
    private PlayerAdapter adapter;
    private ListView listview1;
    private String title = "Swaggy";
    private String user_name;
    private ImageButton start;
    private String room_id;
    private int user_id;
    private ImageView roompicture;
    private ImageView touxiang2;
    private ImageView touxiang3;
    private TextView roomnumber;
    private TextView hint;
    private Handler mHandle;
    private Runnable mRunnable;
    private int score=-1;
    private String TAG = "guquanye";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitactivity);
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        room_id = intent.getStringExtra("room_id");
        user_id = intent.getIntExtra("user_id",0);
        score = intent.getIntExtra("score",-1);
        if(score!=-1)
            Toast.makeText(getApplicationContext(),
                    "恭喜你上一轮比赛得分"+score+"分",
                    Toast.LENGTH_SHORT);
        hint = (TextView)findViewById(R.id.hint);
        roomnumber = (TextView)findViewById(R.id.roomnumber);
        roompicture = (ImageView)findViewById(R.id.roompicture);
        touxiang2 = (ImageView)findViewById(R.id.touxiang2);
        touxiang3 = (ImageView)findViewById(R.id.touxiang3);
        roomnumber.setText("ROOM "+room_id);
        switch (room_id){
            case "1":
                roompicture.setImageResource(R.drawable.room1);
                break;
            case "2":
                roompicture.setImageResource(R.drawable.room2);
                break;
            case "3":
                roompicture.setImageResource(R.drawable.room3);
                break;
            case "4":
                roompicture.setImageResource(R.drawable.room4);
                break;
            case "5":
                roompicture.setImageResource(R.drawable.room5);
                break;
            case "6":
                roompicture.setImageResource(R.drawable.room6);
                break;
        }
        start = (ImageButton) findViewById(R.id.game_start);
        start.setVisibility(View.GONE);
        hint.setVisibility(View.VISIBLE);
        setTitle(title);
        mHandle = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                init_gameroom();
                mHandle.postDelayed(this,4000);
            }
        };
        init_gameroom();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameRoom.this,Gaming.class);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_id",user_id);
                intent.putExtra("room_id",room_id);
                startActivity(intent);
            }
        });
        /*listview1 = (ListView) findViewById(R.id.playerlist);
        adapter = new PlayerAdapter(GameRoom.this, R.layout.player_item, playerList);
        adapter.notifyDataSetChanged();
        listview1.setAdapter(adapter);

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playerList.remove(position);
                adapter.notifyDataSetChanged();
                listview1.setAdapter(adapter);
                Toast.makeText(getBaseContext(),"你隐藏了一条信息",Toast.LENGTH_SHORT).show();
            }

        });*/
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatroom_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                playerList.clear();
                init_gameroom();
                break;
            default:
                Log.d(TAG, "onOptionsItemSelected: ");
                new BackgroundTask().execute("");
                Intent intent = new Intent(GameRoom.this, RoomListActivity.class);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
                finish();
        }
        return true;
    }
    public void init_gameroom() {
        playerList.clear();
        String path = "http://54.169.112.52:5000/get_player";
        new BackgroundTask().execute(path);
        mHandle.removeCallbacks(mRunnable);
        mHandle.postDelayed(mRunnable,10000);
    }
    class BackgroundTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            if (!"".equals(params[0])) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("room_id", room_id)
                            .build();
                    Request request = new Request.Builder()
                            .url(params[0])
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    return responseData;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_name", user_name)
                            .add("room_id", room_id)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://54.169.112.52:5000/leave_room")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "doInBackground: " +responseData);
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
//                adapter.notifyDataSetChanged();
                //              listview1.setAdapter(adapter);
                if ("OK".equals(status)) {
                    Log.d(TAG, "onPostExecute:1 ");
                    if (!json.isNull("data")) {
                        Log.d(TAG, "onPostExecute:2 ");
                        JSONArray player_list = json.getJSONArray("data");
                        for (int i = 0; i < player_list.length(); i++) {

                            JSONObject player = player_list.getJSONObject(i);
                            String name =player.getString("user_name");
                            Log.d(TAG, "onPostExecute: " + name);
                            Player player_ = new Player(name);
                            playerList.add(player_);
                        }

                    }
                    int nums = json.getInt("nums");
                    Log.d(TAG, "onPostExecute: "+ nums);
                    if (nums==1){
                        start.setVisibility(View.GONE);
                        hint.setVisibility(View.VISIBLE);
                        touxiang2.setImageResource(R.drawable.meiren);
                        touxiang3.setImageResource(R.drawable.meiren);
                    } else if (nums==2){
                        start.setVisibility(View.GONE);
                        hint.setVisibility(View.VISIBLE);
                        touxiang2.setImageResource(R.drawable.touxiang2);
                        touxiang3.setImageResource(R.drawable.meiren);
                    } else if(nums==3){
                        start.setVisibility(View.VISIBLE);
                        hint.setVisibility(View.GONE);
                        touxiang2.setImageResource(R.drawable.touxiang2);
                        touxiang3.setImageResource(R.drawable.touxiang3);
                    }

                    //Thread.sleep(20000);
                    //init_gameroom();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

