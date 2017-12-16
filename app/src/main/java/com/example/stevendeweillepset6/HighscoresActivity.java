package com.example.stevendeweillepset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/*
 * Show list of all users with their scores. Updated real-time.
 */
public class HighscoresActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private String uid;
    private ScoreAdapter adapter;
    private ArrayList<String> emails;
    private ArrayList<Long> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        // Load database
        db = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
        }

        // Add button listeners
        Button homeButton = (Button) findViewById(R.id.home_button);
        Button playButton = (Button) findViewById(R.id.start_play_button);
        Button resetButton = (Button) findViewById(R.id.reset_button);
        homeButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        // Initialize list of all users and scores
        emails = new ArrayList<String>();
        scores = new ArrayList<Long>();

        // Set adapter and show list of highscores
        ListView list = (ListView) findViewById(R.id.scorelist);
        adapter = new ScoreAdapter(HighscoresActivity.this, emails, scores);
        list.setAdapter(adapter);

        // Load all users and scores from database (and update realtime)
        db.child("users").addValueEventListener (
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        getScores((Map<String,Object>) snapshot.getValue());
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println(databaseError.toString());
                    }
                }
        );
    }

    /*
     * Fill emails and scores lists from info from database
     */
    public void getScores(Map<String,Object> users) {
        emails.clear();
        scores.clear();
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            //System.out.println(singleUser.toString());
            emails.add((String) singleUser.get("email"));
            scores.add((Long) singleUser.get("score"));
        }
    }

    /*
     * Handle button clicks
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_button:
                finish();
                break;
            case R.id.start_play_button:
                Toast.makeText(HighscoresActivity.this, "Have fun.",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, StartActivity.class));
                break;
            case R.id.reset_button:
                // Check if logged in
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    db.child("users").child(uid).child("score").setValue(0);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

}
