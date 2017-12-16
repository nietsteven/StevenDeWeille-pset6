package com.example.stevendeweillepset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String difficulty;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Spinner catSpinner = (Spinner) findViewById(R.id.category_spinner);
        Spinner difSpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.difficulties_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        catSpinner.setAdapter(adapter);
        difSpinner.setAdapter(adapter2);

        this.difficulty = "easy";
        this.category = "art";
    }

    public void startPlaying(View view) {
        String diffChoice = this.difficulty.toLowerCase();
        String catChoice = this.category.toLowerCase();
        Question question = new Question(getApplicationContext(), catChoice, diffChoice);
        System.out.println(question);
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("question", question.text);
        intent.putExtra("correct", question.correct);
        intent.putExtra("wrong1", question.wrong1);
        intent.putExtra("wrong1", question.wrong2);
        intent.putExtra("wrong1", question.wrong3);
        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        if (parent.getId() == R.id.category_spinner) {
            this.category = (String) parent.getItemAtPosition(pos);
        }
        if (parent.getId() == R.id.difficulty_spinner) {
            this.difficulty = (String) parent.getItemAtPosition(pos);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Use default
        this.category = "art";
        this.difficulty = "easy";
    }
}
