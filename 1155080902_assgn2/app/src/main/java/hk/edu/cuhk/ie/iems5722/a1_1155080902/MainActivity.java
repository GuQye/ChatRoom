package hk.edu.cuhk.ie.iems5722.a1_1155080902;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private List<Room> chatroom_list = new ArrayList<Room>();
    private String name = "guquanye";
    private RoomAdapter chatroom;
    private ListView main_listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.activity_main_label);
        init_chatroom();
        main_listview = (ListView) findViewById(R.id.main_listview);
        chatroom = new RoomAdapter(MainActivity.this, R.layout.chatroom_item, chatroom_list);
        chatroom.notifyDataSetChanged();
        main_listview.setAdapter(chatroom);
        main_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = chatroom_list.get(position);
                Intent activity2 = new Intent(MainActivity.this, ChatRoom.class);
                String title = room.getName().toString();
                int room_id = room.getId();
                activity2.putExtra("title",title);
                activity2.putExtra("room_id",String.valueOf(room_id));
                startActivity(activity2);
            }
        });
    }
    public void init_chatroom() {
        String path = "http://iems5722.albertauyeung.com/api/asgn2/get_chatrooms";
        new BackgroundTask().execute(path);
    }
    class BackgroundTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String path = String.format(params[0]);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(path).build();
                Response response = null;
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                return responseData;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            chatroom.notifyDataSetChanged();
            main_listview.setAdapter(chatroom);
            try {
                JSONObject json = new JSONObject(jsonString);
                String status = json.getString("status");
                if("OK".equals(status)){
                    JSONArray chat_list = json.getJSONArray("data");
                    for (int i = 0; i < chat_list.length(); i++) {
                        JSONObject room = chat_list.getJSONObject(i);
                        String name =room.getString("name");
                        int id     = room.getInt("id");
                        Room room1 = new Room(id,name);
                        chatroom_list.add(room1);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
