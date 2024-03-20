package com.example.bookflix;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class QuestionnaireActivity extends AppCompatActivity {
    ListView listView;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    String uid;
    FirebaseUser currentFirebaseUser;
    User user;
    ArrayList<String> categories, writers;
    ArrayList<Book> top5Books;
    private boolean isWriters=false;
    private  boolean isCategories=true;
    private TextView textViewChoiceTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        listView = (ListView) findViewById(R.id.categories_selection);
        textViewChoiceTitle = (TextView) findViewById(R.id.textViewChoiceTitle);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("userInfo"); // Key
        }

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();//get the user id from firebase
        uid=currentFirebaseUser.getUid();//save to string
        database = FirebaseDatabase.getInstance("https://bookflix-19a72-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference("categories");
        showCategories();
        mDatabase = database.getReference("books");
        createWriters();
    }

    public void submitCategories(View view){
        if (isCategories) {
            boolean isNew = user.isValid();

            if (isNew) {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                DatabaseReference mDatabase1 = database.getReference();
                ArrayList<String> selectedCategories = new ArrayList<>();
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    if (checked.get(i)) {
                        selectedCategories.add(listView.getAdapter().getItem(i).toString());

                    }
                }
                user.setCategory(selectedCategories);
                user.setValid(false);
                mDatabase1.child("users").child(uid).setValue(user);
                mDatabase1.child("users").child(uid).child("category").setValue(selectedCategories);
                showListWriters(); //8a emfaniso tora tous siggrafeis
            } else {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                DatabaseReference mDatabase1 = database.getReference();
                ArrayList<String> selectedCategories = new ArrayList<>();
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    if (checked.get(i)) {
                        selectedCategories.add(listView.getAdapter().getItem(i).toString());

                    }
                }
                user.setCategory(selectedCategories);
                //User user = new User(currentFirebaseUser.getEmail(), selectedCategories);
                mDatabase1.child("users").child(uid).child("category").setValue(selectedCategories);
                showListWriters();
            }
        }
        else if (isWriters){
            boolean isNew = user.isValid();
            textViewChoiceTitle.setText("Επιλογή Συγγραφέων");
            if (isNew) {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                DatabaseReference mDatabase1 = database.getReference();
                ArrayList<String> selectedCategories = new ArrayList<>();
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    if (checked.get(i)) {
                        selectedCategories.add(listView.getAdapter().getItem(i).toString());
                    }
                }
                user.setWriters(selectedCategories);
                user.setValid(false);
                mDatabase1.child("users").child(uid).setValue(user);
                mDatabase1.child("users").child(uid).child("writers").setValue(selectedCategories);
                closeActivity();
            } else {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                DatabaseReference mDatabase1 = database.getReference();
                ArrayList<String> selectedCategories = new ArrayList<>();
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    if (checked.get(i)) {
                        selectedCategories.add(listView.getAdapter().getItem(i).toString());
                    }
                }

                user.setWriters(selectedCategories);
                //User user = new User(currentFirebaseUser.getEmail(), selectedCategories);
                mDatabase1.child("users").child(uid).child("writers").setValue(selectedCategories);
                closeActivity();
            }
        }
    }

    public void cancelCategories(View view){
        closeActivity();
    }

    /*
    * methodos gia na grafei se ena bundle to antikeimeno user na kleinei to activity
    * kai na epistrefei sto proigoumeno activity
    * */
    public void closeActivity(){
        Bundle b = new Bundle();
        b.putParcelable("userInfo2", user);
        Intent in = new Intent();
        in.putExtras(b);
        setResult(RESULT_OK, in);
        finish();
    }

    private void showCategories() {
        categories = new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {//get all categories
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String post = postSnapshot.getValue(String.class);
                    categories.add(post);
                }
                Collections.sort(categories);
                showList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /*
    * me8odos pou apo ola ta biblia 8a dimourgisi ena arraylist apo tous siggrafeis
    * kai ena arraylist apo ta top-5 biblia
    * */
    private void createWriters(){
        writers = new ArrayList<>();
        top5Books = new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {//get all categories
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Book post = postSnapshot.getValue(Book.class);
                    writers.add(post.getAuthors());
                    top5Books.add(post);
                }
                Set<String> set = new HashSet<>(writers);
                writers.clear();
                writers.addAll(set);//gia na exo monadikes times stous writers
                Collections.sort(writers);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /*
     * methodos pou to arraylist ton katigorion to deinei san eisodo sto listview
     * */
    private void showList() {
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, categories);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        for (int i=0; i<categories.size();i++){
            {
                if (user.checkIfCategoryIsLiked(categories.get(i)))
                    listView.setItemChecked(i, true);
            }
        }
    }

    /*
    * methodos pou to arraylist ton siggrfeon to deinei san eisodo sto listview
    * */
    private void showListWriters(){
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, writers);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        for (int i=0; i<writers.size();i++){
            {
                if (user.checkIfWriterIsLiked(writers.get(i)))
                    listView.setItemChecked(i, true);
            }
        }
        isWriters=true;
        isCategories=false;
    }
}