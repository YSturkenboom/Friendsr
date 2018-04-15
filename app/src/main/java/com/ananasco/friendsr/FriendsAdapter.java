package com.ananasco.friendsr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ananasco.friendsr.Friend;
import com.ananasco.friendsr.R;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuri on 10-4-18.
 */

public class FriendsAdapter extends ArrayAdapter<Friend> {

    private ArrayList<Friend> friends;

    public FriendsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Friend> objects) {
        super(context, resource, objects);
        friends = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }
        drawFriend(convertView, friends.get(position));
        return convertView;
    }

    private void drawFriend(View baseView, Friend friend){
        ((TextView)baseView.findViewById(R.id.distance)).setText(friend.getDistance() + " km away");
        ((TextView)baseView.findViewById(R.id.name)).setText(friend.getName());
        ((ImageView)baseView.findViewById(R.id.img)).setImageResource(friend.getDrawableId());
        //((TextView)baseView.findViewById(R.id.liked)).setText(friend.getLiked().toString());


        if (friend.getSuperLiked()) {
            ((ImageView) baseView.findViewById(R.id.pokeball))
                    .setImageResource(R.drawable.masterball);
        }
        else {
            if (friend.getLiked()) {
                ((ImageView) baseView.findViewById(R.id.pokeball))
                        .setImageResource(R.drawable.pokeball);
            } else {
                ((ImageView) baseView.findViewById(R.id.pokeball))
                        .setImageResource(R.drawable.nopokeball);
            }
        }
    }
}
