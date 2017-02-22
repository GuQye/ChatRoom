package hk.edu.cuhk.ie.iems5722.a1_1155080902;

/**
 * Created by wataxiwahuohuo on 2017/1/20.
 */


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatRoom extends AppCompatActivity {
    private List<ChatMessage> messageList = new ArrayList<ChatMessage>();
    private MessageAdapter adapter;
    private ListView listview1;
    private String name = "Swaggy";
    private String title;
    private String chatroom_id;
    private String user_id = "1155080902";
    private int page = 1;
    private int position = 0;
    private int total;
    private String total_pages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        chatroom_id = intent.getStringExtra("room_id");
        setTitle(title);
        init_chatroom();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new MessageAdapter(ChatRoom.this,R.layout.listview,messageList);
        adapter.notifyDataSetChanged();
        listview1 = (ListView) findViewById(R.id.listviiew1);
        listview1.setAdapter(adapter);

        ImageButton send = (ImageButton) findViewById(R.id.button2);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText1);
                String info = editText.getText().toString();
                if(!"".equals(info)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                    String currentDateandTime = sdf.format(new Date());
                    ChatMessage chatMessage = new ChatMessage(info,currentDateandTime,name,ChatMessage.Sent);
                    messageList.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    post_message(info);
                    listview1.setSelection(messageList.size());
                    editText.setText("");
                }
            }
        });

        listview1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(total != totalItemCount)
                {
                    total = totalItemCount;
                    position = totalItemCount - position;
                }
                if(firstVisibleItem == 0){
                    View firstItem = listview1.getChildAt(0);
                    if(firstItem != null && firstItem.getTop() == 0){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int temp = page + 1;
                                    if(temp <= Integer.parseInt(total_pages)) {
                                        String path = String.format("http://iems5722.albertauyeung.com/api/asgn2/get_messages?chatroom_id=%s&page=%d",chatroom_id,++page);
                                        OkHttpClient client = new OkHttpClient();
                                        Request request = new Request.Builder().url(path).build();
                                        Response response = client.newCall(request).execute();
                                        String responseData = response.body().string();
                                        parseJson(responseData);
                                    }
                                    else{
                                    }




                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatroom_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                messageList.clear();
                init_chatroom();
                Log.d("swaggy", "refresh");
                break;
            default:
                Intent intent = new Intent(ChatRoom.this,MainActivity.class);
                startActivity(intent);
        }
        return true;
    }
    public void post_message(final String info){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("chatroom_id",chatroom_id)
                            .add("user_id",user_id)
                            .add("name",name)
                            .add("message",info)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://iems5722.albertauyeung.com/api/asgn2/send_message")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("sss", responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void init_chatroom() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = String.format("http://iems5722.albertauyeung.com/api/asgn2/get_messages?chatroom_id=%s&page=%d",chatroom_id,page);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(path).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJson(responseData);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void parseJson(final String jsonString){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(jsonString);
                    String status = json.getString("status");
                    adapter.notifyDataSetChanged();
                    listview1 = (ListView) findViewById(R.id.listviiew1);
                    listview1.setAdapter(adapter);
                    if("OK".equals(status)){
                        JSONArray chat_list = json.getJSONArray("data");
                        total_pages = json.getString("total_pages");
                        for (int i = 0; i < chat_list.length(); i++) {
                            JSONObject Message = chat_list.getJSONObject(i);
                            String content = Message.getString("message");
                            String timestamp = Message.getString("timestamp");
                            String name = Message.getString("name");

                            if("Swaggy".equals(name)){
                                ChatMessage history = new ChatMessage(content,timestamp,name,ChatMessage.Sent);
                                messageList.add(0,history);
                            }
                            else{
                                ChatMessage history = new ChatMessage(content,timestamp,name,ChatMessage.Receive);
                                messageList.add(0,history);
                            }
                        }
                        if(position != 0){
                            listview1.setSelection(position);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}

