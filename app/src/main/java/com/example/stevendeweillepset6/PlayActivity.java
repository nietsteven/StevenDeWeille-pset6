package com.example.stevendeweillepset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/*
Playing the game. Given a category and difficulty loads question from API into app and updates
user's score if logged in. Currently handling one question at a time, can be expanded to more
questions to save on phone in case of no internet connection. Saves current question and answers
locally to handle screen rotation.
Currently correct answer is the first answer, answer list sequence to be randomized in future.
 */
public class PlayActivity extends AppCompatActivity implements View.OnClickListener{

    private String correctAnswer;
    private String difficulty;
    private String category;
    private String question;
    private String wrong1;
    private String wrong2;
    private String wrong3;

    private View answered;
    private TextView questionView;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Get difficulty and category from intent
        Intent intent = getIntent();
        difficulty = intent.getStringExtra("difficulty");
        category = intent.getStringExtra("category");

        // Get question and answers views
        questionView = (TextView) findViewById(R.id.question);
        answer1 = (Button) findViewById(R.id.answer1);
        answer2 = (Button) findViewById(R.id.answer2);
        answer3 = (Button) findViewById(R.id.answer3);
        answer4 = (Button) findViewById(R.id.answer4);
        Button back = (Button) findViewById(R.id.back_button);
        Button next = (Button) findViewById(R.id.next_button);
        answered = findViewById(R.id.answered_layout);
        answered.setVisibility(View.INVISIBLE);

        // Add listeners to answer buttons
        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);
        back.setOnClickListener(this);
        next.setOnClickListener(this);

        // Load question
        if (savedInstanceState == null) {
            nextQuestion();
        }
    }

    /*
    Check if the button with the correct answer is clicked and sets result layout to visible.
    Increase user's score if question is answered correctly.
     */
    public void onClick(View view) {
        // Return to StartActivity when back button clicked
        if (view.getId() == R.id.back_button) {
            finish();
        }
        // Load new question when next button clicked
        if (view.getId() == R.id.next_button) {
            nextQuestion();
            return;
        }

        // Get answer from button
        Button button = (Button) view;
        boolean correct;
        int earned = 0;

        // Check if answered correctly
        if (button.getText().toString().equals(Html.fromHtml(correctAnswer).toString())) {
            correct = true;
        }
        else {
            correct = false;
            ((Button) view).setTextColor(getResources().getColor(R.color.red));
        }

        // Show correct answer and earned score
        TextView correctAnswerField = findViewById(R.id.correct_answer);
        TextView pointsEarned = findViewById(R.id.points_earned);
        correctAnswerField.setText(Html.fromHtml(correctAnswer));
        if (correct) {
            // Easy: 1 point, Medium: 2 points, Hard: 3 points
            switch (difficulty){
                case "easy":
                    earned = 1;
                    break;
                case "medium":
                    earned = 2;
                    break;
                case "hard":
                    earned = 3;
                    break;
            }
            // Increase user score in database
            updateScore(earned);
        }
        pointsEarned.setText(""+earned);

        // Set layout visible
        answered.setVisibility(View.VISIBLE);

        // Make answer buttons unclickable to avoid answering same question again
        answer1.setClickable(false);
        answer2.setClickable(false);
        answer3.setClickable(false);
        answer4.setClickable(false);
    }

    /*
    Load new question and add answers and question to the existing views.
    Make answered layout invisible again.
     */
    private void nextQuestion() {
        // Hide result view
        answered.setVisibility(View.INVISIBLE);

        // Get category id from string
        int categoryId = 25;
        switch (category) {
            case "art":
                categoryId = 25;
                break;
            case "celebrities":
                categoryId = 26;
                break;
            case "history":
                categoryId = 23;
                break;
            case "sports":
                categoryId = 21;
                break;
        }

        // Load data from API and add to views
        String url = "https://opentdb.com/api.php?amount=1&category="+categoryId+"&difficulty="+
                difficulty+"&type=multiple";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Load result JSONArray (can expand to list of 10 questions in future)
                            JSONArray results = response.getJSONArray("results");
                            for (int i=0; i<results.length(); i++) {
                                // Store all data
                                JSONObject result = results.getJSONObject(i);
                                String text = (String) result.get("question");
                                String correct = (String) result.get("correct_answer");
                                JSONArray wrong = result.getJSONArray("incorrect_answers");
                                wrong1 = wrong.getString(0);
                                wrong2 = wrong.getString(1);
                                wrong3 = wrong.getString(2);

                                // Put data in views (not randomized yet)
                                correctAnswer = correct;
                                question = text;
                                answer1.setText(Html.fromHtml(correctAnswer));
                                answer2.setText(Html.fromHtml(wrong1));
                                answer3.setText(Html.fromHtml(wrong2));
                                answer4.setText(Html.fromHtml(wrong3));
                                questionView.setText(Html.fromHtml(question));

                                // Restore answer buttons (clickable and textcolor)
                                answer1.setClickable(true);
                                answer2.setClickable(true);
                                answer3.setClickable(true);
                                answer4.setClickable(true);
                                answer1.setTextColor(getResources().getColor(R.color.grayBlue));
                                answer2.setTextColor(getResources().getColor(R.color.grayBlue));
                                answer3.setTextColor(getResources().getColor(R.color.grayBlue));
                                answer4.setTextColor(getResources().getColor(R.color.grayBlue));
                            }
                        } catch (JSONException e) {
                            System.out.println("Oops. Something went wrong.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error. No internet connection found.");
            }
        });
        queue.add(jsonRequest);
    }

    /*
    Increase user's score in database with earned points
     */
    public void updateScore(final int points) {
        // Load instance of database
        final String uid;
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Check if logged in
        if (currentUser != null) {
            uid = currentUser.getUid();
            // Update score
            db.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Map singleUser = (Map) snapshot.getValue();
                    Long oldScore = (Long) singleUser.get("score");
                    Long newScore = oldScore + points;
                    db.child("users").child(uid).child("score").setValue(newScore);
                    // Show updated score
                    Toast.makeText(PlayActivity.this, "New score: "+newScore,
                            Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.w("A", "Failed to retrieve score.");
                }
            });
        }
        // If not logged in show message
        else {
            Toast.makeText(PlayActivity.this, "You are not logged in. Your score will not be saved.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Save current question info
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("question", question);
        savedInstanceState.putString("correct", correctAnswer);
        savedInstanceState.putString("wrong1", wrong1);
        savedInstanceState.putString("wrong2", wrong2);
        savedInstanceState.putString("wrong3", wrong3);
    }

    /*
    Restore saved question info and load into views
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore info
        question = savedInstanceState.getString("question");
        correctAnswer = savedInstanceState.getString("correct");
        wrong1 = savedInstanceState.getString("wrong1");
        wrong2 = savedInstanceState.getString("wrong2");
        wrong3 = savedInstanceState.getString("wrong3");

        // Load info into views
        questionView.setText(Html.fromHtml(question));
        answer1.setText(Html.fromHtml(correctAnswer));
        answer2.setText(Html.fromHtml(wrong1));
        answer3.setText(Html.fromHtml(wrong2));
        answer4.setText(Html.fromHtml(wrong3));
    }

}