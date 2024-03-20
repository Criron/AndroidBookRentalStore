package com.example.bookflix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityLogin extends AppCompatActivity {
    public EditText loginEmailId, logInpasswd;
    Button btnLogIn;
    TextView signup;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        loginEmailId = findViewById(R.id.loginEmail);
        logInpasswd = findViewById(R.id.loginpaswd);
        btnLogIn = findViewById(R.id.btnLogIn);
        signup = findViewById(R.id.TVSignIn);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(ActivityLogin.this, "User logged in ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityLogin.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
        signup.setOnClickListener(new View.OnClickListener() {// an pati8ei to Sign up epistrefo sto MainActivity
            @Override
            public void onClick(View view) {
                Intent I = new Intent(ActivityLogin.this, MainActivity.class);
                startActivity(I);
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {//an pati8ei to login
            @Override
            public void onClick(View view) {
                String userEmail = loginEmailId.getText().toString();
                String userPaswd = logInpasswd.getText().toString();
                if (userEmail.isEmpty()) {//elegxo an exei simplirosei ta stoixeia
                    loginEmailId.setError("Provide your Email first!");
                    loginEmailId.requestFocus();
                } else if (userPaswd.isEmpty()) {
                    logInpasswd.setError("Enter Password!");
                    logInpasswd.requestFocus();
                } else if (userEmail.isEmpty() && userPaswd.isEmpty()) {
                    Toast.makeText(ActivityLogin.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) {//an exei simplirosei kai ta dio prospa8o na kano login
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(ActivityLogin.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {//apotixia login
                                Toast.makeText(ActivityLogin.this, "Not sucessfull", Toast.LENGTH_SHORT).show();
                            } else {//epitixia login
                                FirebaseDatabase database;
                                DatabaseReference mDatabase;
                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();//get the user id from firebase
                                String uid=currentFirebaseUser.getUid();//save to string
                                database = FirebaseDatabase.getInstance("https://bookflix-19a72-default-rtdb.europe-west1.firebasedatabase.app/");
                                mDatabase = database.getReference("users").child(uid);//8elo na diabaso olo to antikeimeno user apo ti firebae
                                ValueEventListener eventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {//otan to diabaso
                                        User user = dataSnapshot.getValue(User.class);//to apo8ikevo se ena antikeimeno user

                                        if (user.getUsername().equals(userEmail)) {
                                            Bundle b = new Bundle();//dimiourgo bundle
                                            b.putParcelable("userInfo", user);//apo8ikevo to antikeimeno kai to "true"
                                            b.putString("login","true");
                                            Intent in = new Intent(getApplicationContext(), UserActivity.class);//kalo to UserActivity
                                            in.putExtras(b);
                                            startActivity(in);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                };
                                mDatabase.addListenerForSingleValueEvent(eventListener);
                            }
                        }
                    });
                } else {
                    Toast.makeText(ActivityLogin.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
