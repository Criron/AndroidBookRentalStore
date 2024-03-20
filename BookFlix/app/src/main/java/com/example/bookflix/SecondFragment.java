package com.example.bookflix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
* to deftero tab
* idia leitourgia me to proto allazei mono i diadikasia gia tin emfanisi ton biblion
* */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    ListView listView;
    ArrayList<Book> books;
    ArrayList<User> users;
    User user;
    AppCompatActivity context;
    private FirebaseDatabase database;
    private DatabaseReference  mDatabase;
    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param text Parameter 1.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String text) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString("msg", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_second, container, false);
        UserActivity u = (UserActivity) getActivity();
        user=u.getUser();
        books = new ArrayList<Book>();
        users = new ArrayList<User>();
        context= (AppCompatActivity) getActivity();
        listView = (ListView) v.findViewById(R.id.book_selection);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                //to isbn tou bibliou pou epelexa
                Long isbnSelected = Long.parseLong(((TextView) view.findViewById(R.id.textViewISBNs)).getText().toString());
                showBookInfo(isbnSelected);
            }
        });
        showBooks();
        return v;
    }
    private void showBooks(){
        database = FirebaseDatabase.getInstance("https://bookflix-19a72-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {//get all categories
                getUsers(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getUsers(DataSnapshot snapshot){
        users.clear();
        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
            User u = postSnapshot.getValue(User.class);
            if (!u.getUsername().equals(user.getUsername())) {//gia na min pros8eso ton idio
                if (user.checkIfCategoriesEqual(u)){
                  if (user.checkIfWritersEqual(u)) {
                      users.add(u);
                  }
                }
            }
        }

        mDatabase = database.getReference("books");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {//get all categories
                callFunc(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void callFunc(DataSnapshot snapshot){
        ArrayList<String> bookTitle = new ArrayList<>();
        ArrayList<String>bookRate = new ArrayList<>();
        ArrayList<String> bookWriter = new ArrayList<>();
        ArrayList<String> isbn = new ArrayList<>();
        books.clear();
        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
            Book book = postSnapshot.getValue(Book.class);
            books.add(book);
            for (int i=0; i< users.size(); i++){
                if (users.get(i).checkIfIsbnIsBought(book.getIsbn13())){
                    bookTitle.add(book.getTitle());
                    bookRate.add(book.getAverage_rating()+"/"+book.getRatings_count());
                    bookWriter.add(book.getAuthors());
                    isbn.add(String.valueOf(book.getIsbn13()));
                }
            }

        }
        CustomBookList customΒοοκList = new CustomBookList(context, bookTitle.toArray(new String[bookTitle.size()]), bookWriter.toArray(new String[bookTitle.size()]), bookRate.toArray(new String[bookRate.size()]), isbn.toArray(new String[bookRate.size()]));
        listView.setAdapter(customΒοοκList);
    }
    @Override
    public void onResume() {
        super.onResume();
        UserActivity u = (UserActivity) getActivity();
        user=u.getUser();

        showBooks();
    }

    private void showBookInfo(Long isbnSelected){
        for (int i=0; i<books.size();i++){
            if (books.get(i).getIsbn13()==isbnSelected)
            {
                Book b=books.get(i);
                UserActivity u = (UserActivity) getActivity();
                u.showBook(b);
                break;
            }
        }

    }


}