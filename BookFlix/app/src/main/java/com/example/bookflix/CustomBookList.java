package com.example.bookflix;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/*
* klasi pou periexei ta stoixeia pou 8eloume na deixnoume gia ka8e ena biblio
* */
public class CustomBookList  extends ArrayAdapter {
    private String[] bookNames;
    private String[] bookWriter;
    private String[] bookRate;
    private String[] isbn;
    private AppCompatActivity context;

    public CustomBookList(AppCompatActivity context, String[] countryNames, String[] bookWriter, String[] capitalNames, String[] isbn) {
        super(context, R.layout.row_item, countryNames);
        this.context = context;
        this.bookNames = countryNames;
        this.bookWriter = bookWriter;
        this.bookRate = capitalNames;
        this.isbn = isbn;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.row_item, null, true);
        TextView textViewTitle = (TextView) row.findViewById(R.id.textViewTitle);
        TextView textViewWriter = (TextView) row.findViewById(R.id.textViewWriter);
        TextView textViewRate = (TextView) row.findViewById(R.id.textViewRate);
        TextView textViewISBNs = (TextView) row.findViewById(R.id.textViewISBNs);
        textViewTitle.setText("Τίτλος: "+bookNames[position]);
        textViewRate.setText("Βαθμολογία: "+bookRate[position]);
        textViewWriter.setText("Συγγραφέας: "+bookWriter[position]);
        textViewISBNs.setText(isbn[position]);
        return  row;
    }

}
