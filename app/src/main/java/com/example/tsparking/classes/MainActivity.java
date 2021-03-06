package com.example.tsparking.classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tsparking.R;
import com.example.tsparking.fragments.Login_frag;
import com.example.tsparking.fragments.Profile_frag;
import com.example.tsparking.fragments.Register_frag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        Login_frag login_frag = new Login_frag();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentCont, login_frag).commit();
    }

    public void LoadPageReg() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentCont, new Register_frag()).addToBackStack(null).commit();
    }


    public void SignUpFunc() {
        EditText emailText = findViewById(R.id.EmailRText);
        String email = emailText.getText().toString();

        EditText passwordText = findViewById(R.id.PasswordRText);
        String password = passwordText.getText().toString();

        EditText firstNameText = findViewById(R.id.FirstNameText);
        String first_name = firstNameText.getText().toString();

        EditText lastNameText = findViewById(R.id.LastNameText);
        String last_name = lastNameText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Authentication successed.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Users").child(uid);

                            User u = new User(email, first_name, last_name);
                            myRef.setValue(u);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentCont, new Register_frag()).addToBackStack(null).commit();
    }

    public void SignInFunc(View view) {
        EditText emailText = findViewById(R.id.EmailText);
        String email = emailText.getText().toString();

        EditText passwordText = findViewById(R.id.PasswordText);
        String password = passwordText.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(MainActivity.this, "Login OK",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                LoadPageProf();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Login failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

    public void LoadPageProf() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentCont, new Profile_frag()).addToBackStack(null).commit();
    }
}

