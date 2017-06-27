package com.iems5722.Group2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//问题
class ConfirmDialogHelper {
    private static Dialog dialog;

    public static void dismiss(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
    }



    public static void showDialog(Context context, String title, String summary,
                                  View.OnClickListener okListener,View.OnClickListener cancelListen){
        if (dialog == null)
            dialog = new Dialog(context,R.style.MyDialog);
        View view = View.inflate(context,R.layout.layout_window,null);
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        tvTitle.setText(title);
        TextView tvSummary = (TextView) view.findViewById(R.id.summary);
        tvSummary.setText(summary);
        Button okBtn = (Button) view.findViewById(R.id.id_ok);
        okBtn.setOnClickListener(okListener);
        Button cancelBtn = (Button) view.findViewById(R.id.id_cancel);
        cancelBtn.setOnClickListener(cancelListen);
        dialog.setContentView(view);
        dialog.show();
    }
}
//初始化
class InitDialogHelper {
    private static Dialog dialog;

    public static void dismiss(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void showDialog(Context context, String title, String summary,
                                  View.OnClickListener okListener){
        if (dialog == null)
            dialog = new Dialog(context,R.style.MyDialog);
        View view = View.inflate(context,R.layout.layout_init_window,null);
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        tvTitle.setText(title);
        TextView tvSummary = (TextView) view.findViewById(R.id.summary);
        tvSummary.setText(summary);

        dialog.setContentView(view);

        dialog.show();
    }
}





public class Gaming extends AppCompatActivity {

    private String user_name;
    private String room_id;
    private static final String TAG = "guquanye";
    private Button show;
    private Button question;
    private String title;
    private String text;
    private String question_title="";
    private String question_text="";
    private String word;
    private Map<Integer,String> users;
    private int user_id =1;
    private int master_id;
    private ListView listview1;
    private List<GameMessage> messageList = new ArrayList<GameMessage>();
    private GameMessageAdapter adapter;
    private Handler mHandle;
    private Runnable mRunnable;
    private int Count = 1;
    private int Round = 1;
    private int message_id=0;
    private ImageButton help;
    private ImageButton send;
    private EditText editText;
    private int score=0;
    private int winner=-1;
    String init_url = "http://54.169.112.52:5000/init_room";
    String update_url = "http://54.169.112.52:5000/show_message";
    String send_url = "http://54.169.112.52:5000/message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);
        setTitle(R.string.activity_main_label);
        send = (ImageButton) findViewById(R.id.send); //发消息
        help = (ImageButton) findViewById(R.id.help); //发问题
        editText = (EditText) findViewById(R.id.game_text); //输入框
        show = (Button) findViewById(R.id.show); //显示信息框
        question = (Button) findViewById(R.id.question);//显示问题框
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id",0);
        user_name = intent.getStringExtra("user_name");
        room_id = intent.getStringExtra("room_id");
        Log.d(TAG, "onCreate: init" + room_id);
        //发送type为1的消息
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_message("1",editText.getText().toString());
                editText.setText("");
                //即时更新数据
                update_message();
            }
        });
        //发送type为0的消息
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Count++<20)
                    send_message("0",editText.getText().toString());
                else
                    Toast.makeText(getApplicationContext(),
                            "已经达到提问次数",
                            Toast.LENGTH_LONG);
                editText.setText("");
                //即时更新数据
                update_message();
            }
        });
        listview1 = (ListView) findViewById(R.id.game_listview);
        adapter = new GameMessageAdapter(Gaming.this, R.layout.gamelistview, messageList);
        adapter.notifyDataSetChanged();
        listview1.setAdapter(adapter);
        mHandle = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                update_message();
                mHandle.postDelayed(this,1000);
            }
        };
        mHandle.removeCallbacks(mRunnable);
        mHandle.postDelayed(mRunnable,1000);
        init_gameroom();
    }



    public void init_gameroom(){

        question.setVisibility(View.GONE);
        show.setVisibility(View.GONE);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init_showDialog(v,title,text);
            }
        });
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v,question_title,question_text);
            }
        });

        new BackgroundTask().execute(init_url);
    }



    public void update_message(){
        //messageList.clear();
        new BackgroundTask().execute(update_url);
    }

    private void send_message(String i, String text) {

        if(user_id!=master_id||i=="3"||i=="4")
            new BackgroundTask().execute(send_url,i,text);
    }




    private void start_game() {
        title="";
        text="";
        Log.d(TAG, "start_game: winner" + winner);
        if (winner != -1) {
            if(winner!=user_id) {
                title="上一轮答对的是"+winner+"号玩家";
            }
            else {
                score+=5;
                title = "恭喜你获得上一轮胜利";
            }
        }
        if(master_id == user_id){
            title = title+"\n"+"恭喜你成为第"+Round+"轮的出题者";
            text = "游戏规则:出题者将收到一个人物词语，你将回答答题者提出的问题\n本轮词语为：" + word;
            show.performClick();
        }
        else{
            title = title+"\n"+"开始第"+Round+"轮游戏";
            if(Round==1) {
                text = "游戏规则:出题者将收到一个人物词语，你可以有最多20次提问机会，出题者只能回答是或否 例如:是否是男性，点击左侧按钮提出问题，右侧按钮提交答案，答题次数不限\n本轮你是答题者，请答题";
            }
            else
                text = "本轮你是答题者，请答题";
            show.performClick();
        }

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
                //messageList.clear();
                update_message();
                break;
            default:
                finish();
        }
        return true;
    }
    public void showDialog(View v,String title, String text){
        ConfirmDialogHelper.showDialog(this, title, text,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //回复正确
                        send_message("3",question_text);
                        editText.setText("");
                        //update_message();
                        ConfirmDialogHelper.dismiss();
                    }
                },new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //回复错误
                        send_message("4",question_text);
                        editText.setText("");
                        //update_message();
                        ConfirmDialogHelper.dismiss();
                    }
                });

        };



    public void init_showDialog(View v,String title, String text){
        InitDialogHelper.showDialog(this, title, text,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //回复正确
                        if(Round<=6)
                            init_gameroom();
                        else{
                            Intent intent = new Intent(Gaming.this,GameRoom.class);
                            intent.putExtra("user_id",user_id);
                        intent.putExtra("room_id",room_id);
                        startActivity(intent);}
                        ConfirmDialogHelper.dismiss();
                    }
                });
    }


    class BackgroundTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            //Log.d(TAG, "doInBackground1: ");

            if (params[0].equals(init_url)) {
                try {
                    Log.d(TAG, "doInBackground: " + params[0]);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("room_id", room_id)
                            .add("round",String.valueOf(Round))
                            .build();
                    Request request = new Request.Builder()
                            .url(init_url)
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
            else if(params[0].equals(update_url)){
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("room_id", room_id)
                            .add("message_id",String.valueOf(message_id))
                            .build();
                    Request request = new Request.Builder()
                            .url(update_url)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    return responseData;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("room_id", room_id)
                            .add("user_id",String.valueOf(user_id))
                            .add("type",params[1])
                            .add("message",params[2])
                            .build();
                    Request request = new Request.Builder()
                            .url(send_url)
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
                if ("OK".equals(status)) {
                    if (!json.isNull("users")) {
                        JSONArray user_list = json.getJSONArray("users");
                        for (int i = 0; i < user_list.length(); i++) {
                            JSONObject player = user_list.getJSONObject(i);
                            String user =player.getString("user_name");
                            int id = player.getInt("id");
                        }
                        word = json.getString("word");
                        master_id = json.getInt("master_id");
                        Log.d(TAG, "master_id: " + master_id);
                        start_game();
                    }

                    else{
                        JSONArray message_list = json.getJSONArray("messages");
                        for (int i = 0; i < message_list.length(); i++) {
                            JSONObject Message = message_list.getJSONObject(i);
                            String message =Message.getString("message");
                            int id = Message.getInt("user_id");
                            int type = Message.getInt("type");
                            message_id = Message.getInt("id");
                            GameMessage gameMessage = new GameMessage(message,id,type);
                            if(message.equals(word)){
                                Round=Round+1;
                                winner = id;
                                //Log.d(TAG, "onPostExecute: winnner" +winner) ;
                                if(Round>6){
                                    Intent intent = new Intent(Gaming.this,GameRoom.class);
                                    intent.putExtra("user_name",user_name);
                                    intent.putExtra("room_id",room_id);
                                    intent.putExtra("user_id",user_id);
                                    intent.putExtra("score",score);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                else{
                                    init_gameroom();
                                    break;
                                }
                            }
                            messageList.add(gameMessage);
                            //Log.d(TAG, "onPostExecute: " + user);
                            if(type==0&&master_id==user_id){
                                question_text=message;
                                question_title="编号为"+id+"的玩家发来一个问题";
                                question.performClick();
                            }
                            if(type==3&&master_id!=user_id){
                                title="from master!!!";
                                text = "\""+message + "\""+ " is true";
                                show.performClick();
                            }
                            if(type==4&&master_id!=user_id){
                                title="from master!!!";
                                text = "\""+message + "\""+ " is wrong";
                                show.performClick();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




}
