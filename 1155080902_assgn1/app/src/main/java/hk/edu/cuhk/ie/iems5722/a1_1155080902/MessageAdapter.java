package hk.edu.cuhk.ie.iems5722.a1_1155080902;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hk.edu.cuhk.ie.iems5722.a1_1155080902.R;

import java.util.List;

/**
 * Created by wataxiwahuohuo on 2017/2/6.
 */

public class MessageAdapter extends ArrayAdapter<ChatMessage> {

    private int resourceId;
    public MessageAdapter(Context context, int resource, List<ChatMessage> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatmess = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        LinearLayout layout_left = (LinearLayout) view.findViewById(R.id.layout_left);
        LinearLayout layout_right = (LinearLayout) view.findViewById(R.id.layout_right);
        TextView info_left = (TextView) view.findViewById(R.id.listview_info_left);
        TextView info_right = (TextView) view.findViewById(R.id.listview_info_right);
        TextView time_left = (TextView) view.findViewById(R.id.listview_time_left);
        TextView time_right = (TextView) view.findViewById(R.id.listview_time_right);



        if(chatmess.getType()==ChatMessage.Receive){
            layout_left.setVisibility(View.VISIBLE);
            layout_right.setVisibility(View.GONE);
            info_left.setText(chatmess.getMessage());
            time_left.setText(chatmess.getTime());
        }
        if(chatmess.getType()==ChatMessage.Sent){
            layout_right.setVisibility(View.VISIBLE);
            layout_left.setVisibility(View.GONE);
            info_right.setText(chatmess.getMessage());
            time_right.setText(chatmess.getTime());
        }
        return view;
    }


}
