package com.example.bookflix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FillProfilActivity extends AppCompatActivity {
    EditText surname, name, userAddress, userPhone;
    User user;
    private DatabaseReference mDatabase;
    String uid;
    FirebaseUser currentFirebaseUser;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_profil);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("userInfo"); // diabazo to bundle
        }
        surname=(EditText) findViewById(R.id.surname);
        name=(EditText) findViewById(R.id.name);
        userAddress=(EditText) findViewById(R.id.userAddress);
        userPhone=(EditText) findViewById(R.id.userPhone);
        surname.setText(user.getSurname());//emfanizo ta stoixeia (onoma, eponimo k.lp.)
        name.setText(user.getName());
        userAddress.setText(user.getAddress());
        userPhone.setText(user.getPhone());
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();//get the user id from firebase
        uid=currentFirebaseUser.getUid();//save to string
        database = FirebaseDatabase.getInstance("https://bookflix-19a72-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public void profilCancel(View view){
        closeActivity();
    }

    public void profilSubmit(View view){
        DatabaseReference mDatabase1 = database.getReference();
        user.setValid(false);//apo8ikevo ta stoixeia sto antikeimeno user
        user.setAddress(userAddress.getText().toString());
        user.setSurname(surname.getText().toString());
        user.setName(name.getText().toString());
        user.setPhone(userPhone.getText().toString());
        mDatabase1.child("users").child(uid).setValue(user);//to grafo sti firebae
        closeActivity();
    }

    public void closeActivity(){
        Bundle b = new Bundle();//to epistrefo me to bundle
        b.putParcelable("userInfo2", user);
        Intent in = new Intent();
        in.putExtras(b);
        setResult(RESULT_OK, in);
        finish();
    }

}

