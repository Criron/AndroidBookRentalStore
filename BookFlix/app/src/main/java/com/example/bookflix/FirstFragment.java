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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    ListView listView;
    ArrayList<Book> books;
    User user;
    AppCompatActivity context;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment FirstFragment.
     */

    public static FirstFragment newInstance() {
        FirstFragment fragment1 = new FirstFragment();
        return fragment1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_first, container, false);
        UserActivity u = (UserActivity) getActivity();
        user=u.getUser();//diabazo to xristi san antikeimeno
        books = new ArrayList<>();//dimiourgo keni lista me ta biblia
        context= (AppCompatActivity) getActivity();
        listView = (ListView) v.findViewById(R.id.book_selection);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //to isbn tou bibliou pou epelexa
                Long isbnSelected = Long.parseLong(((TextView) view.findViewById(R.id.textViewISBNs)).getText().toString());
                showBookInfo(isbnSelected);//an pati8ei click se ena biblio kalo ti showBookInfo
            }
        });
        showBooks();//emfanizo ta biblia pou einai protaseis gia to xristi me basi afta pou exei simplirosei sto erotimatologio
        return v;
    }

    /*
    * me8odos gia na emfaniso ta biblia
    * */
    private void showBooks(){
        database = FirebaseDatabase.getInstance("https://bookflix-19a72-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference("books");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {//get all categories

                callFunc(snapshot);//otan olokliro8ei i anagnosi kalo tin callFunc


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    /*
    * brisko poia biblia prepei na emfaniso kai ta emfanizo
    * */
    private void callFunc(DataSnapshot snapshot){
        ArrayList<String> bookTitle = new ArrayList<>();
        ArrayList<String>bookRate = new ArrayList<>();
        ArrayList<String> bookWriter = new ArrayList<>();
        ArrayList<String> isbn = new ArrayList<>();
        books.clear();
        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
            Book book = postSnapshot.getValue(Book.class);
            books.add(book);//to pros8ero sti lista ton biblion
            //8a emfanistei to biblio mono an tou aresei i katigoria i' o author
            if (user.checkIfCategoryIsLiked(book.getCategories())){
                bookTitle.add(book.getTitle());
                bookRate.add(book.getAverage_rating()+"/"+book.getRatings_count());
                bookWriter.add(book.getAuthors());
                isbn.add(String.valueOf(book.getIsbn13()));
            }
            else if (user.checkIfWriterIsLiked(book.getAuthors())){
                bookTitle.add(book.getTitle());
                bookRate.add(book.getAverage_rating()+"/"+book.getRatings_count());
                bookWriter.add(book.getAuthors());
                isbn.add(String.valueOf(book.getIsbn13()));
            }

        }
        CustomBookList customΒοοκList = new CustomBookList(context, bookTitle.toArray(new String[bookTitle.size()]), bookWriter.toArray(new String[bookTitle.size()]), bookRate.toArray(new String[bookRate.size()]), isbn.toArray(new String[bookRate.size()]));
        listView.setAdapter(customΒοοκList);//ti lista pou eftiaksa tin pros8eto sto listview
    }

    @Override
    public void onResume() {//an epanel8o
        super.onResume();
        UserActivity u = (UserActivity) getActivity();
        user=u.getUser();//diabazo to xristi se periptosi allagon
        showBooks();//emfanizo ta biblia
    }

    /*
    * me8odos gia na brei to biblio pou exei epilexei o xristis kai na kalesei ti showBook
    * */
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
/*
    private void addTobooks(Long isbnSelected){
        int i;
        for (i=0; i<books.size(); i++)
        {
            if (books.get(i).getIsbn13()==isbnSelected){
                UserActivity u = (UserActivity) getActivity();
                u.addToBought(books.get(i));
                break;
            }
        }

    }*/
}