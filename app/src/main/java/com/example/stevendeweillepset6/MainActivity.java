package com.example.stevendeweillepset6;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
Home screen, with SignUp and LogIn fragments
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static Button logInButton;
    public static boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logInButton = findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            // User is signed in
            loggedIn = true;
            logInButton.setText("LOG OUT");
            Log.d("signed in", "onAuthStateChanged:signed_in:" + currentUser.getUid());
        } else {
            // User is signed out
            loggedIn = false;
            Log.d("signed out", "onAuthStateChanged:signed_out:");
        }
    }

    /*
    Handle button clicks: open right fragment or activity
     */
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.login_button:
                // Log in if not already logged in
                if (!loggedIn) {
                    // Open login fragment
                    FragmentManager fm = getSupportFragmentManager();
                    LogInFragment fragment = new LogInFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, fragment, "login");
                    ft.addToBackStack(null);
                    ft.commit();
                    break;
                }
                else {
                    // Log out
                    FirebaseAuth.getInstance().signOut();
                    logInButton.setText("LOG IN");
                    loggedIn = false;
                    Toast.makeText(this, "Logged out.",
                            Toast.LENGTH_SHORT).show();
                    break;
                }

            case R.id.signup_button:
                // Open sign up fragment
                FragmentManager fm = getSupportFragmentManager();
                SignUpFragment fragment = new SignUpFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment, "signup");
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.highscores_button:
                startActivity(new Intent(this, HighscoresActivity.class));
                break;

            case R.id.help_button:
                Toast.makeText(MainActivity.this, "Not implemented yet.",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.play_button:
                Toast.makeText(MainActivity.this, "Have fun.",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, StartActivity.class));
                break;
        }
    }

}
