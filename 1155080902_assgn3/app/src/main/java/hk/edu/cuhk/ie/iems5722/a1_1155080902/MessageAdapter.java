package hk.edu.cuhk.ie.iems5722.a1_1155080902;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
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
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.layout_left = (LinearLayout) view.findViewById(R.id.layout_left);
            viewHolder.layout_right = (LinearLayout) view.findViewById(R.id.layout_right);
            viewHolder.info_left = (TextView) view.findViewById(R.id.listview_info_left);
            viewHolder.info_right = (TextView) view.findViewById(R.id.listview_info_right);
            viewHolder.time_left = (TextView) view.findViewById(R.id.listview_time_left);
            viewHolder.time_right = (TextView) view.findViewById(R.id.listview_time_right);
            viewHolder.name_left = (TextView) view.findViewById(R.id.name_left);
            viewHolder.name_right = (TextView) view.findViewById(R.id.name_right);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }



        if(chatmess.getType()==ChatMessage.Receive){
            viewHolder.layout_left.setVisibility(View.VISIBLE);
            viewHolder.layout_right.setVisibility(View.GONE);
            viewHolder.info_left.setText(chatmess.getMessage());
            viewHolder.time_left.setText(chatmess.getTime());
            viewHolder.name_left.setText(chatmess.getName());
        }
        if(chatmess.getType()==ChatMessage.Sent){
            viewHolder.layout_right.setVisibility(View.VISIBLE);
            viewHolder.layout_left.setVisibility(View.GONE);
            viewHolder.info_right.setText(chatmess.getMessage());
            viewHolder.time_right.setText(chatmess.getTime());
            viewHolder.name_right.setText(chatmess.getName());
        }
        return view;
    }
    class ViewHolder{
        LinearLayout layout_left;
        LinearLayout layout_right;
        TextView info_left;
        TextView info_right;
        TextView time_left;
        TextView time_right;
        TextView name_left;
        TextView name_right;
    }

}
