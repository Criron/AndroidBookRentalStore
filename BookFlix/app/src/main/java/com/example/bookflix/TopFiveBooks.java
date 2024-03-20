package com.example.bookflix;

import java.util.Comparator;

/*
* gia na mporo na kano sigkriseis me antikeimena book
* */
public class TopFiveBooks  implements Comparator<Book> {

    /*
    * otan sigkrino dio books kaleitai i me8odos afti
    * */
    public int compare(Book a, Book b)
    {
        return Math.round((b.getAverage_rating() - a.getAverage_rating())*100);

    }
}
