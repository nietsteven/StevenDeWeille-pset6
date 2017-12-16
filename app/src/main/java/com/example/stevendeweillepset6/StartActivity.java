package com.example.stevendeweillepset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

/*
 * Choose difficulty and category for the game
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    /*
     * Get category and difficulty information from Spinners when play button is clicked
     */
    public void startPlaying(View view) {
        Spinner catSpinner = (Spinner) findViewById(R.id.category_spinner);
        Spinner difSpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        String catChoice = catSpinner.getSelectedItem().toString().toLowerCase();
        String difChoice = difSpinner.getSelectedItem().toString().toLowerCase();

        // Load playing activity
        Intent intent = new Intent(StartActivity.this, PlayActivity.class);
        intent.putExtra("difficulty", difChoice);
        intent.putExtra("category", catChoice);
        startActivity(intent);
    }

}
