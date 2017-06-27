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
        TextView tx = (TextView) view.findViewById(R.id.chatroom_item);
        tx.setText(room.getName().toString());
        return view;
    }


}
