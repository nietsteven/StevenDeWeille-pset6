package com.example.stevendeweillepset6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Loads email and score lists into listview
 */

public class ScoreAdapter extends ArrayAdapter {
    Context context;
    ArrayList<String> emails;
    ArrayList<Long> scores;
    LayoutInflater inflater;

    // Constructor
    public ScoreAdapter(Context context, ArrayList<String> emails, ArrayList<Long> scores) {
        super(context, R.layout.mylistlayout);

        this.context= context;
        this.emails = emails;
        this.scores = scores;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.mylistlayout, parent, false);

        // Get views and data
        TextView nameView = (TextView) view.findViewById(R.id.nameview);
        TextView scoreView = (TextView) view.findViewById(R.id.scoreview);

        String userEmail = this.emails.get(index);
        Long userScore = this.scores.get(index);

        // Fill textviews
        nameView.setText(userEmail);
        scoreView.setText(userScore.toString());

        return view;
    }

    @Override
    public int getCount() {
        return emails.size();
    }

}
