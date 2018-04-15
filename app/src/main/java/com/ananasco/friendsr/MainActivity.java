package com.ananasco.friendsr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Friend> friends = new ArrayList<>();
    FriendsAdapter friendsAdapter;
    Boolean initialised = false;
    int pokeballsLeft = 2;
    int masterballsLeft = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*for (int i = 0; i < 30; i++){
            friends.add(i, new Friend("Ditto", "Pink blob", R.drawable.ditto));
        }*/

        if (!initialised) {
            // Add some friends when the app is opened for the first time.
            // Currently names are treated as id's, a real app would have proper uids.
            friends.add(0, new Friend("Ditto", "Pink blob",
                    R.drawable.ditto));
            friends.add(1, new Friend("Pikachu", "Simply Shocking",
                    R.drawable.pikachuf));
            friends.add(2, new Friend("Charmander", "One fiery boi",
                    R.drawable.charmander));
            friends.add(3, new Friend("Diglett", "I'm diggin' it",
                    R.drawable.diglett));
            friends.add(4, new Friend("Dragonair", "Water snek",
                    R.drawable.dragonair));
            friends.add(5, new Friend("Dragonite", "Will @#&! you up",
                    R.drawable.dragonite));
            friends.add(6, new Friend("Bulbasaur", "Is that a vegetable on his back",
                    R.drawable.bulbasaur));
            friends.add(7, new Friend("Gengar", "Scary boi",
                    R.drawable.gengar));
            friends.add(8, new Friend("Gyarados", "Holy shit it's a freaking water dragon",
                    R.drawable.gyaradosf));
            friends.add(9, new Friend("Mew", "Aww, it's so cute",
                    R.drawable.mew));
            friends.add(10, new Friend("Mewtwo", "Not so cute now, is he",
                    R.drawable.mewtwo));
            friends.add(11, new Friend("Pidgey", "Not another pidgey :(",
                    R.drawable.pidgey));
            friends.add(12, new Friend("Squirtle", "Coolest guy in the squirtle gang",
                    R.drawable.squirtle));
            initialised = true;
        }

        else {
            SharedPreferences prefs = getSharedPreferences("saved", MODE_PRIVATE);

            // load all friends' data if we have run the app before
            for (int i = 0; i < friends.size(); i++){
                Friend f = friends.get(i);
                f.setLiked(prefs.getBoolean(friends.get(i).getName()+"_liked", false));
                f.setSuperLiked(prefs.getBoolean(friends.get(i).getName()+"_super", false));
                f.setRating(prefs.getInt(friends.get(i).getName()+"_rating", -1));
            }
        }

        friendsAdapter = new FriendsAdapter(getApplicationContext(), R.layout.grid_item, friends);
        ((GridView) findViewById(R.id.grid)).setAdapter(friendsAdapter);

        GridView gv = findViewById(R.id.grid);
        gv.setOnItemClickListener(new GridItemClickListener());
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        SharedPreferences prefs = getSharedPreferences("saved", MODE_PRIVATE);

        // load all friends' data if we have run the app before
        for (int i = 0; i < friends.size(); i++){
            Friend f = friends.get(i);
            f.setLiked(prefs.getBoolean(friends.get(i).getName()+"_liked", false));
            f.setSuperLiked(prefs.getBoolean(friends.get(i).getName()+"_super", false));
            f.setRating(prefs.getFloat(friends.get(i).getName()+"_rating", -1));
            pokeballsLeft = prefs.getInt("pBLeft", 2);
            masterballsLeft = prefs.getInt("mBLeft", 1);
        }

        // actually update the adapter
        friendsAdapter.notifyDataSetChanged();
    }

    private class GridItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Friend clickedFriend = (Friend) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("clicked_friend", clickedFriend);
            intent.putExtra("pBLeft", pokeballsLeft);
            intent.putExtra("mBLeft", masterballsLeft);
            startActivity(intent);
        }
    }

    public void reset(View view){

        getSharedPreferences("saved", MODE_PRIVATE).edit().clear().apply();

        for (int i = 0; i < friends.size(); i++){
            Friend f = friends.get(i);
            f.setLiked(false);
            f.setSuperLiked(false);
            f.setRating(-1);
        }

        pokeballsLeft = 2;
        masterballsLeft = 1;

        // actually update the adapter
        friendsAdapter.notifyDataSetChanged();
    }
}
