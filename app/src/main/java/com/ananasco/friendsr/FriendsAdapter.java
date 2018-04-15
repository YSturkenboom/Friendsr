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
 * Just the modified ArrayAdapter explained in the tutorial. Where the code is unique for this
 * project there are some additional comments
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

        // moved drawing functions to a separate function to keep things tidy
        drawFriend(convertView, friends.get(position));

        return convertView;
    }

    /**
     * Draws all the Views in the GridView items. Takes a baseView defining the View in which
     * Android should look for the Views, and a Friend to define which Friend's data to use
     * to draw the Views with.
     */
    private void drawFriend(View baseView, Friend friend){
        ((TextView)baseView.findViewById(R.id.distance)).setText(friend.getDistance() + " km away");
        ((TextView)baseView.findViewById(R.id.name)).setText(friend.getName());
        ((ImageView)baseView.findViewById(R.id.img)).setImageResource(friend.getDrawableId());

        // A Tinder 'superlike' is shown by a sprite of a masterball :)
        if (friend.getSuperLiked()) {
            ((ImageView) baseView.findViewById(R.id.pokeball))
                    .setImageResource(R.drawable.masterball);
        }
        else {

            // A normal like is shown by a sprite of a pokeball
            if (friend.getLiked()) {
                ((ImageView) baseView.findViewById(R.id.pokeball))
                        .setImageResource(R.drawable.pokeball);

                // No like is shown by a greyed out pokeball
            } else {
                ((ImageView) baseView.findViewById(R.id.pokeball))
                        .setImageResource(R.drawable.nopokeball);
            }
        }
    }
}
