package com.example.stevendeweillepset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
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
    //@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                // Log in if not already logged in
                if (!loggedIn) {
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
                //String email = "hoi2@test.com";
                //String password = "hoitest";
                //createUser(email, password);
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
    public void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("create user", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("create user", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }
}
