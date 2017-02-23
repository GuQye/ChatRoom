package hk.edu.cuhk.ie.iems5722.a1_1155080902;

/**
 * Created by wataxiwahuohuo on 2017/1/20.
 */


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
    private String year = "";
    private String month = "";
    private String day = "";
    private String TAG = "test";

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
        adapter = new MessageAdapter(ChatRoom.this, R.layout.listview, messageList);
        adapter.notifyDataSetChanged();
        listview1 = (ListView) findViewById(R.id.listviiew1);
        listview1.setAdapter(adapter);
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageList.remove(position);
                adapter.notifyDataSetChanged();
                listview1.setAdapter(adapter);
                Toast.makeText(getBaseContext(),"你隐藏了一条信息",Toast.LENGTH_SHORT).show();

            }
        });
        ImageButton send = (ImageButton) findViewById(R.id.button2);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText1);
                String info = editText.getText().toString();
                if (!"".equals(info)) {
                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                    //String currentDateandTime = sdf.format(new Date());
                    SimpleDateFormat sdf_post = new SimpleDateFormat("HH:mm");
                    String currentDateandTime_post = sdf_post.format(new Date());
                    ChatMessage chatMessage = new ChatMessage(info, currentDateandTime_post, name, ChatMessage.Sent, "");
                    messageList.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    new BackgroundTask().execute("", info);
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
                Log.d(TAG, "onScroll: "+firstVisibleItem);
                if (total != totalItemCount) {
                    position = totalItemCount - total;
                    total = totalItemCount;
                }
                if (firstVisibleItem == 0) {
                    View firstItem = listview1.getChildAt(0);
                    if (firstItem != null && firstItem.getTop() == 0) {
                        int temp = page + 1;
                        if (temp <= Integer.parseInt(total_pages)) {
                            String path = String.format("http://iems5722.albertauyeung.com/api/asgn2/get_messages?chatroom_id=%s&page=%d", chatroom_id, ++page);
                            Toast.makeText(getApplicationContext(), "正在加载下一页", Toast.LENGTH_SHORT).show();
                            new BackgroundTask().execute(path);

                        } else {
                            Toast.makeText(getApplicationContext(), "已经是最后一页", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //listview 长按菜单
        registerForContextMenu(listview1);
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
                messageList.clear();
                init_chatroom();
                break;
            default:
                Intent intent = new Intent(ChatRoom.this, MainActivity.class);
                startActivity(intent);
        }
        return true;
    }
    public void init_chatroom() {
        page = 1;
        String path = String.format("http://iems5722.albertauyeung.com/api/asgn2/get_messages?chatroom_id=%s&page=%d", chatroom_id,page);
        new BackgroundTask().execute(path);
    }
    class BackgroundTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            if (!"".equals(params[0])) {
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
            } else {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("chatroom_id", chatroom_id)
                            .add("user_id", user_id)
                            .add("name", name)
                            .add("message", params[1])
                            .build();
                    Request request = new Request.Builder()
                            .url("http://iems5722.albertauyeung.com/api/asgn2/send_message")
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

                if ("OK".equals(status)) {
                    if (!json.isNull("data")) {
                        JSONArray chat_list = json.getJSONArray("data");
                        total_pages = json.getString("total_pages");
                        for (int i = 0; i < chat_list.length(); i++) {
                            String next_day = "";
                            String sys_time = "";
                            JSONObject Message = chat_list.getJSONObject(i);
                            if (i + 1 <= chat_list.length()) {
                                JSONObject next = chat_list.getJSONObject(i + 1);
                                String next_timestamp = next.getString("timestamp");
                                next_day = next_timestamp.split("-")[2].split(" ")[0];
                            }
                            String content = Message.getString("message");
                            String timestamp = Message.getString("timestamp");
                            String time = timestamp.split(" ")[1];
                            String name = Message.getString("name");
                            String data[] = timestamp.split("-");
                            year = data[0];
                            month = data[1];
                            day = data[2].split(" ")[0];

                            if (!next_day.equals(day)) {
                                sys_time = year + "-" + month + "-" + day;
                            }
                            if ("Swaggy".equals(name)) {
                                ChatMessage history = new ChatMessage(content, time, name, ChatMessage.Sent, sys_time);
                                messageList.add(0, history);
                            } else {
                                ChatMessage history = new ChatMessage(content, time, name, ChatMessage.Receive, sys_time);
                                messageList.add(0, history);
                            }
                            if (position != 0) {
                                listview1.setSelection(position);
                                Log.d(TAG, "position" + position);
                            }
                        }
                    }
                    else{
                        Toast.makeText(getBaseContext(),"发送成功",Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

