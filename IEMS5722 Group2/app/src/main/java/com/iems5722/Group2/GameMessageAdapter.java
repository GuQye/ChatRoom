package com.iems5722.Group2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wataxiwahuohuo on 2017/2/6.
 */

public class GameMessageAdapter extends ArrayAdapter<GameMessage> {

    private int resourceId;
    public GameMessageAdapter(Context context, int resource, List<GameMessage> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameMessage gameMessage = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.info = (TextView) view.findViewById(R.id.game_info);
            viewHolder.name = (TextView) view.findViewById(R.id.game_name);
            viewHolder.type = (TextView) view.findViewById(R.id.game_type);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        if(gameMessage.getType()==1) {
            viewHolder.info.setText(gameMessage.getMessage());
            viewHolder.name.setText(gameMessage.getName());
            //viewHolder.type.setText(String.valueOf(gameMessage.getType()));
        }

        return view;
    }
    class ViewHolder{
        LinearLayout layout;
        TextView info;
        TextView name;
        TextView type;
    }

}
