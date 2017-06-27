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

public class PlayerAdapter extends ArrayAdapter<Player> {

    private int resourceId;
    private String TAG = "guquanye";
    public PlayerAdapter(Context context, int resource, List<Player> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Player player = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView tx = (TextView) view.findViewById(R.id.player_item);
        Log.d(TAG, "getView: " + player.getName().toString());
        tx.setText(player.getName().toString());
        return view;
    }


}
