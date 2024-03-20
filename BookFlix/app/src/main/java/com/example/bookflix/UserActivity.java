package com.example.bookflix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef, mDatabase;
    String uid;
    FirebaseUser currentFirebaseUser;
    User user;
    String username;
    ArrayList<Book> books;
    ArrayList<Book> boughtBooks;
    ViewPager2 viewPager;
    FragmentStateAdapter pagerAdapter;
    String[] titles = new String[]{"Προτάσεις", "Άλλων χρηστών", "Top-5", "Όλα"};//oi epiloges pou 8a emfanistoun sta tab

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        books = new ArrayList<>();
        boughtBooks = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();//diabazo to bundle pou lambano san eisodo
        if (bundle != null) {
            if (bundle.getString("login").equals("true")){
                user = bundle.getParcelable("userInfo"); // kai to apo8ikevo sto antikeimeno user, to true simainei oti exei kanei login
            }
            else{
                username=bundle.getString("userInfo");//an den exei kanei login den diabazo antikeimeno user giati ksero mono to username
                user=new User(username);//dimiourgo neo antikeimeno user me basi to username
            }
        }
        else{

        }
        viewPager = findViewById(R.id.mypager);
        pagerAdapter = new MyPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout =(TabLayout) findViewById(R.id.tab_layout);//gia ta tab
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titles[position])).attach();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();//get the user id from firebase
        uid=currentFirebaseUser.getUid();//save to string
        database = FirebaseDatabase.getInstance("https://bookflix-19a72-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference("categories");
        myRef = database.getReference("books");

        if (user.isValid()){//an den exei simplirosei to erotimatologio
            popupMessage();
        }
        showBooks();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//gia tis epiloges tou menou
        Bundle b;
        Intent in;
        switch (item.getItemId()) {
            case R.id.Question://erotimatologio
                b = new Bundle();
                b.putParcelable("userInfo", user);
                in = new Intent(getApplicationContext(), QuestionnaireActivity.class);
                in.putExtras(b);
                startActivityForResult(in,2);
                return true;
            case R.id.Profil://profil
                b = new Bundle();
                b.putParcelable("userInfo", user);
                in = new Intent(getApplicationContext(), FillProfilActivity.class);
                in.putExtras(b);
                startActivityForResult(in,3);
                return true;
            case R.id.Basket://kala8i
                //prepei na exei simplirosei to profil gia na paei sto kala8i, an den ta exei simplirosei emfanizei minima
                if (user.getAddress()==null || user.getName()==null || user.getPhone()==null || user.getSurname()==null){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Για να μπορέσετε να ολοκληρώσετε την αγορά πρέπει να έχετε συμπληρώσει όλα τα στοιχεία από το προφίλ.");
                    alertDialogBuilder.setTitle("Ενημερωτικό μήνυμα");
                    alertDialogBuilder.setNegativeButton("Επιστροφή", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {//an exei simplirosei to profil
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Θέλετε να ολοκληρώσετε την αγορά; Έχετε " + boughtBooks.size() + " βιβλία στο καλάθι σας");
                    alertDialogBuilder.setTitle("Ενημερωτικό μήνυμα");
                    alertDialogBuilder.setNegativeButton("Όχι τώρα", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    if (boughtBooks.size() >= 1) {//mono an exo biblia sto kala8i 8a emfaniso to NAI
                        alertDialogBuilder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                buyBooks();//kalo tin buyBooks gia na pros8eso ta biblia sta agorasmena
                            }
                        });
                    }
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                return true;
            case R.id.Logout://logout
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(UserActivity.this, ActivityLogin.class);
                startActivity(I);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    * me8odos gia na diabaso ola ta biblia apo ti firebase
    * */
    private void showBooks(){

        mDatabase = database.getReference("books");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {//get all categories

                callFunc(snapshot);//otan olokriro8ei i anagnosi kalo tin callFunc


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    /*
    * dimiourgo ta katallila arraylist gia ta biblia
    * */
    private void callFunc(DataSnapshot snapshot){
        books.clear();
        for (DataSnapshot postSnapshot : snapshot.getChildren()) {//gia ka8e mia eggrafi pou exo
            Book book = postSnapshot.getValue(Book.class);
            books.add(book);//to books periexei ola ta biblia

        }
    }

    /*
    * method gia na emfanisei minima an 8elei na simplirosei to erotimatologio
    * an patisei nai anoigei to QuestionnaireActivity
    * */

    public void popupMessage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Θέλετε να απαντήσετε σε ένα σύντομο ερωτηματολόγιο για τον καθορισμό του προφίλ σας;");
        alertDialogBuilder.setTitle("Ενημερωτικό μήνυμα");
        alertDialogBuilder.setNegativeButton("Όχι τώρα", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Θα μπορείτε αργότερα να το απαντήσετε μέσα από το μενού",Toast.LENGTH_LONG).show();
            }
        });
        alertDialogBuilder.setPositiveButton("Ναι", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Bundle b = new Bundle();
                b.putParcelable("userInfo", user);
                Intent in = new Intent(getApplicationContext(), QuestionnaireActivity.class);
                in.putExtras(b);
                startActivityForResult(in,2);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    /*
    * i me8odos kaleitai otan epistrepsei i klisi apo to erotimatologio i' to profil
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<String>bookTitle = new ArrayList<>();
        ArrayList<String>bookRate = new ArrayList<>();
        ArrayList<String> bookWriter = new ArrayList<>();
        ArrayList<String> isbn = new ArrayList<>();
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        user = bundle.getParcelable("userInfo2"); // Key
                        for (int i=0; i<books.size(); i++){
                            if (user.checkIfCategoryIsLiked(books.get(i).getCategories())){
                                bookTitle.add(books.get(i).getTitle());
                                bookRate.add(String.valueOf(books.get(i).getAverage_rating()));
                                bookWriter.add(books.get(i).getAuthors());
                                isbn.add(String.valueOf(books.get(i).getIsbn13()));

                            }
                            else if (user.checkIfWriterIsLiked(books.get(i).getAuthors())){
                                bookTitle.add(books.get(i).getTitle());
                                bookRate.add(String.valueOf(books.get(i).getAverage_rating()));
                                bookWriter.add(books.get(i).getAuthors());
                                isbn.add(String.valueOf(books.get(i).getIsbn13()));
                            }
                        }
                        CustomBookList customΒοοκList = new CustomBookList(this, bookTitle.toArray(new String[bookTitle.size()]), bookWriter.toArray(new String[bookTitle.size()]), bookRate.toArray(new String[bookRate.size()]), isbn.toArray(new String[bookRate.size()]));
                    }
                    //user.showList();
                }
                break;
            default:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        user = bundle.getParcelable("userInfo2"); // Key
                    }
                }
        }
    }

    public User getUser(){
        return user;
    }

    /*
    * me8odos gia na pros8eso ena biblio sta agorasmena
    * */
    public void addToBought(Book b){
        boolean found=false;
        for (int i=0; i<boughtBooks.size(); i++){//elegxo an iparxei idi to isbn tou bibliou sti lista me ta agorasmena
            if (b.getIsbn13()==boughtBooks.get(i).getIsbn13()){
                found=true;
                break;
            }
        }
        if (!found) {//an den iparxei to pros8eto
            boughtBooks.add(b);
        }
        Toast.makeText(this, "Έχετε "+boughtBooks.size()+" στο καλάθι σας. Για αγορά επιλέξτε αγορά από το μενού", Toast.LENGTH_LONG).show();
    }

    /*
    * me8odos gia na pros8esei osa exei agorasei sti firebase
    * */
    private void buyBooks(){
        ArrayList<String> selectedCategories = new ArrayList<>();
        DatabaseReference mDatabase1 = database.getReference();
        mDatabase1.child("users").child(uid).setValue(user);
        for (int i=0; i<boughtBooks.size(); i++){ //afta pou exo sto kala8i
            selectedCategories.add(String.valueOf(boughtBooks.get(i).getIsbn13()));
        }
        if (user.bought!=null) {
            for (int i = 0; i < user.bought.size(); i++) { //afta pou eixa agorasei
                selectedCategories.add(String.valueOf(user.bought.get(i)));
            }
        }
        user.bought=selectedCategories;//ta apo8ikevo kai sto user
        mDatabase1.child("users").child(uid).child("bought").setValue(selectedCategories);
        boughtBooks.clear();
    }

    /*
    * me8odos gia na emfanisei tin perigrafi enos bibliou
    * */
    public void showBook(Book b){
       AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle(b.getTitle());

        // set the custom layout
        View customLayout = LayoutInflater.from(this).inflate(R.layout.bookinfo_layout,null);
        builder.setView(customLayout);
        TextView  textViewDescription;
        textViewDescription = (TextView) customLayout.findViewById(R.id.textViewDescription);
        textViewDescription.setText(b.getDescription());

        if (user.bought==null){
            builder.setPositiveButton("Αγορά", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addToBought(b);

                }
            });
        }
        else {
            if (!user.bought.contains(String.valueOf(b.getIsbn13()))) {//an den to exei agorasei 8a pros8eso to button Αγορά, de mporo na agoraso afta pou exo agorasei idi
                builder.setPositiveButton("Αγορά", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addToBought(b);

                    }
                });
            }
        }
        builder.setNegativeButton("Επιστροφή", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which)
            {


            }
        });
        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
