package com.example.stevendeweillepset6;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends DialogFragment implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailView;
    private EditText pwView;
    //private boolean loggedIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //loggedIn = false;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        mAuth = mAuth = FirebaseAuth.getInstance();
        emailView = (EditText) view.findViewById(R.id.email);
        pwView = (EditText) view.findViewById(R.id.password);
        Button loginButton = (Button) view.findViewById(R.id.login_confirm);
        loginButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        if (emailView.getText() != null && pwView.getText() != null &&
                emailView.getText().toString().length() > 0 && pwView.getText().toString().length() > 0) {
            String email = emailView.getText().toString();
            String password = pwView.getText().toString();
            logIn(email, password);
        }
        else {
            Toast.makeText(getContext(), "Please enter a valid email and password.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Log in", "signInWithEmail:success");
                            Toast.makeText(getContext(), "Log-in succesfull.",
                                    Toast.LENGTH_SHORT).show();
                            MainActivity.loggedIn = true;
                            MainActivity.logInButton.setText("LOG OUT");
                            closeFragment();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Log in", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void closeFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(this);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

}
