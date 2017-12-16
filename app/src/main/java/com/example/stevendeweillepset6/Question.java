package com.example.stevendeweillepset6;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */

public class Question {
    int category;
    String difficulty;
    static String text;
    static String correct;
    static String wrong1;
    static String wrong2;
    static String wrong3;

    /*
    Constructor with category and difficulty from spinners in StartActivity
     */
    public Question(Context context, String category, String difficulty) {
        // Find category id's used by API
        switch (category) {
            case "art":
                this.category = 25;
                break;
            case "celebrities":
                this.category = 26;
                break;
            case "history":
                this.category = 23;
                break;
            case "sports":
                this.category = 21;
                break;
        }
        this.difficulty = difficulty;
        //Get Question and Answer from API
        getQuestion(context);
        System.out.println(wrong1 + " "+wrong2);
    }

    public void getQuestion(Context context) {
        String url = "https://opentdb.com/api.php?amount=1&category="+this.category+"&difficulty="+
                this.difficulty+"&type=multiple";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i=0; i<results.length(); i++) {
                                JSONObject result = results.getJSONObject(i);
                                text = (String) result.get("question");
                                //System.out.println(Question.text);
                                correct = (String) result.get("correct_answer");

                                JSONArray wrong = result.getJSONArray("incorrect_answers");
                                wrong1 = wrong.getString(0);
                                wrong2 = wrong.getString(1);
                                wrong3 = wrong.getString(2);
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
}
