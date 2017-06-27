package com.iems5722.Group2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wataxiwahuohuo on 2017/2/6.
 */

public class RoomAdapter extends ArrayAdapter<Room> {

    private int resourceId;
    public RoomAdapter(Context context, int resource, List<Room> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room room = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        /*TextView tx = (TextView) view.findViewById(R.id.room_title);
        TextView tx2 = (TextView) view.findViewById(R.id.room_num);
        TextView tx3 = (TextView) view.findViewById(R.id.room_content);
        tx.setText(room.getName().toString());
        Log.d("guquanaye", "getView: " + room.getName());
        tx2.setText(room.getPlayerNums()+"/3");
        tx3.setText("你值得拥有你值得拥有你值得拥有你值得拥有你值得拥有你值得拥有你值得拥有你值得拥有你值得拥有你值得拥有你值得拥有你值得拥有");*/
        return view;
    }


}
