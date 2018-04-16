package com.ananasco.friendsr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.media.MediaPlayer;
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
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // play some battle music >:)
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.battle);

        // retrieve the data that was passed to this event in the Intent
        Intent intent = getIntent();
        retrievedFriend = (Friend) intent.getSerializableExtra("clicked_friend");
        pbLeft = intent.getIntExtra("pBLeft", 0);
        mbLeft = intent.getIntExtra("mBLeft", 0);

        // do a bunch of drawing of info and UI based on data passed by the Intent
        ((TextView)findViewById(R.id.bio)).setText("Bio: " + retrievedFriend.getBio());
        ((ImageView)findViewById(R.id.img)).setImageResource(retrievedFriend.getDrawableId());
        ((TextView)findViewById(R.id.encounterText))
                .setText("A wild " + retrievedFriend.getName() + " appeared!");
        ((TextView)findViewById(R.id.leftPb)).setText(" X"+pbLeft);
        ((TextView)findViewById(R.id.leftMb)).setText(" X"+mbLeft);
        ((RatingBar)findViewById(R.id.rating)).setRating(retrievedFriend.getRating());

        // whenever the rating bar is changed, the rating is updated for the friend and then saved
        // to the SharedPreferences in save()
        ((RatingBar)findViewById(R.id.rating))
                .setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                retrievedFriend.setRating(rating);
                save(retrievedFriend);
            }
        });


        /**
         * This code handles the Tinder-eqsue swipe gestures, I couldn't resist. The UI clarifies
         * the gestures, but swiping left means leaving the pokemon, swiping right means capturing
         * it (liking it) and swiping up means throwing a masterball (a superlike ;))
         * The game will only allow the user to throw a ball if the user still has them.
         * Throwing the masterball will result in an additional confirmation prompt.
         * The swipe gestures only work on the swipe area as indicated by the UI.
         * All succesful swipe action end with the closing of the ProfileActivity to return to the
         * MainActivity.
         *
         * Thanks https://stackoverflow.com/questions/4139288/
         * android-how-to-handle-right-to-left-swipe-gestures
         */
        findViewById(R.id.all)
                .setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
                    @Override
                    // Let the pokemon go
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
                    // Capture (like) the pokemon, if the user still has pokeballs. Otherwise, show a
                    // Toast.
                    public void onSwipeRight() {
                        if (pbLeft > 0){

                            // make sure to decrease total pokeballs!
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
                    // Capture and superlike the pokemon if the user still has masterballs.
                    // Otherwise, show a Toast. Requires additional confirmation.
                    public void onSwipeUp() {
                        if (mbLeft > 0){

                            // make sure to decrease total masterballs!
                            mbLeft --;

                            // Create a dialogprompt with a yes and a no button: if the user answers
                            // yes, the pokemon is superliked and one ball removed. If not, nothing
                            // happens.
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

    // Helper function to save all information about a Friend in the SharedPreferences. Variables
    // of a Friend are stored in the preferences under the name of that Friend.
    private void save(Friend f){
        SharedPreferences.Editor editor =
                getSharedPreferences("saved", MODE_PRIVATE).edit();

        editor.putBoolean(f.getName() + "_liked", f.getLiked());
        editor.putBoolean(f.getName() + "_super", f.getSuperLiked());
        editor.putFloat(f.getName() + "_rating", f.getRating());

        // amount of poke- and masterballs left
        editor.putInt("pBLeft", pbLeft);
        editor.putInt("mBLeft", mbLeft);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
    }
}
