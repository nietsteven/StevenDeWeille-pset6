package com.example.stevendeweillepset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        // Get question and answers
        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        String correctAnswer = intent.getStringExtra("correct");
        //String[] wrongAnswers = intent.getStringArrayExtra("wrong");
        String wrong1 = intent.getStringExtra("wrong1");
        String wrong2 = intent.getStringExtra("wrong2");
        String wrong3 = intent.getStringExtra("wrong3");

        // Get question and answers views
        TextView questionView = (TextView) findViewById(R.id.question);
        Button answer1 = (Button) findViewById(R.id.answer1);
        Button answer2 = (Button) findViewById(R.id.answer2);
        Button answer3 = (Button) findViewById(R.id.answer3);
        Button answer4 = (Button) findViewById(R.id.answer4);

        // Set text in views
        // TODO: Randomize answers
        questionView.setText(question);
        answer1.setText(correctAnswer);
        answer2.setText(wrong1);
        answer3.setText(wrong2);
        answer4.setText(wrong3);
    }
}
