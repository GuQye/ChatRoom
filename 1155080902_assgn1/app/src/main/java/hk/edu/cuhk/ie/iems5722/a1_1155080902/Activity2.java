package hk.edu.cuhk.ie.iems5722.a1_1155080902;

/**
 * Created by wataxiwahuohuo on 2017/1/20.
 */


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import hk.edu.cuhk.ie.iems5722.a1_1155080902.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Activity2 extends AppCompatActivity {
    private List<ChatMessage> messageList = new ArrayList<ChatMessage>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final MessageAdapter adapter = new MessageAdapter(Activity2.this,R.layout.listview,messageList);
        final ListView listview1 = (ListView) findViewById(R.id.listviiew1);
        listview1.setAdapter(adapter);

        ImageButton send = (ImageButton) findViewById(R.id.button2);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText1);
                String info = editText.getText().toString();
                if(!"".equals(info)){
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());
                    ChatMessage chatMessage = new ChatMessage(info,currentDateandTime,ChatMessage.Sent);
                    ChatMessage reply = new ChatMessage("哦",currentDateandTime,ChatMessage.Receive);
                    messageList.add(chatMessage);
                    messageList.add(reply);
                    adapter.notifyDataSetChanged();
                    listview1.setSelection(messageList.size());
                    editText.setText("");
                }
            }
        });
    }
}
