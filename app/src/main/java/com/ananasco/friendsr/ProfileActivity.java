package com.ananasco.friendsr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    Friend retrievedFriend;
    int pbLeft;
    int mbLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        retrievedFriend = (Friend) intent.getSerializableExtra("clicked_friend");
        pbLeft = intent.getIntExtra("pBLeft", 0);
        mbLeft = intent.getIntExtra("mBLeft", 0);

        ((TextView)findViewById(R.id.bio)).setText("Bio: " + retrievedFriend.getBio());
        ((ImageView)findViewById(R.id.img)).setImageResource(retrievedFriend.getDrawableId());
        ((TextView)findViewById(R.id.encounterText))
                .setText("A wild " + retrievedFriend.getName() + " appeared!");
        ((TextView)findViewById(R.id.leftPb)).setText(" X"+pbLeft);
        ((TextView)findViewById(R.id.leftMb)).setText(" X"+mbLeft);
        ((RatingBar)findViewById(R.id.rating)).setRating(retrievedFriend.getRating());

        ((RatingBar)findViewById(R.id.rating))
                .setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                retrievedFriend.setRating(rating);
                save(retrievedFriend);
                Log.i(",","hoiiiii");
            }
        });


        /* Thanks https://stackoverflow.com/questions/4139288/
         * android-how-to-handle-right-to-left-swipe-gestures
         */
        findViewById(R.id.all)
                .setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
                    @Override
                    public void onSwipeLeft() {
                        retrievedFriend.setLiked(Boolean.FALSE);
                        retrievedFriend.setSuperLiked(Boolean.FALSE);
                        save(retrievedFriend);
                        Toast.makeText(ProfileActivity.this,
                                "Pff, who cares about a " + retrievedFriend.getName()
                                        + " anyway.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onSwipeRight() {
                        if (pbLeft > 0){
                            pbLeft --;
                            retrievedFriend.setLiked(Boolean.TRUE);
                            save(retrievedFriend);
                            Toast.makeText(ProfileActivity.this,
                                    "You caught " + retrievedFriend.getName()
                                            + " with a pokéball!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(ProfileActivity.this,
                                    "You don't have enough pokéballs!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSwipeUp() {
                        if (mbLeft > 0){
                            mbLeft --;
                            new AlertDialog.Builder(ProfileActivity.this)
                                    .setTitle("Are you sure")
                                    .setMessage("Do you really want to throw a masterball?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface
                                            .OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            retrievedFriend.setSuperLiked(Boolean.TRUE);
                                            retrievedFriend.setLiked(Boolean.TRUE);
                                            save(retrievedFriend);
                                            Toast.makeText(ProfileActivity.this,
                                                    "You caught " + retrievedFriend.getName()
                                                            + " with a masterball!",
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                        }})
                                    .setNegativeButton(android.R.string.no, null).show();
                        }
                        else {
                            Toast.makeText(ProfileActivity.this,
                                    "You don't have enough masterballs!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }

    private void save(Friend f){
        SharedPreferences.Editor editor =
                getSharedPreferences("saved", MODE_PRIVATE).edit();

        editor.putBoolean(f.getName() + "_liked", f.getLiked());
        editor.putBoolean(f.getName() + "_super", f.getSuperLiked());
        editor.putFloat(f.getName() + "_rating", f.getRating());
        editor.putInt("pBLeft", pbLeft);
        editor.putInt("mBLeft", mbLeft);
        editor.apply();
    }
}
