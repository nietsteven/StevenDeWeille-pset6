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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends DialogFragment implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private EditText emailView;
    private EditText pw1View;
    private EditText pw2View;
    private DatabaseReference db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = mAuth = FirebaseAuth.getInstance();
        emailView = (EditText) view.findViewById(R.id.emailSignUp);
        pw1View = (EditText) view.findViewById(R.id.password1);
        pw2View = (EditText) view.findViewById(R.id.password2);
        Button signUpButton = (Button) view.findViewById(R.id.signup_confirm);
        db = FirebaseDatabase.getInstance().getReference();
        signUpButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        if (emailView.getText() != null && pw1View.getText() != null && pw2View.getText() != null &&
                emailView.getText().toString().length() > 0 && pw1View.getText().toString().length() > 0
                && pw2View.getText().toString().length() > 0) {
            String email = emailView.getText().toString();
            String pw1 = pw1View.getText().toString();
            String pw2 = pw2View.getText().toString();
            if (!pw1.equals(pw2)) {
                Toast.makeText(getContext(), "Passwords do not match.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                if (!(pw1.length() > 6)) {
                    Toast.makeText(getContext(), "Your password must be longer than 6 characters.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    createUser(email, pw1);
                }
            }
        }
        else {
            Toast.makeText(getContext(), "Please fill in all the fields.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void createUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("create user", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(getContext(), "Registration succesfull.",
                                    Toast.LENGTH_SHORT).show();
                            // Add to score database
                            db.child("users").child(user.getUid()).child("email").setValue(email);
                            db.child("users").child(user.getUid()).child("score").setValue(1);
                            closeFragment();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("create user", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
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
