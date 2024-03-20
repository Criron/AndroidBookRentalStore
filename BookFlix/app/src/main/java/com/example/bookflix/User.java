package com.example.bookflix;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/*
* i klasi tou xristi kanei implement to parceable gia na mporei na perasei apo activity se activity
* */
public class User implements Parcelable  {
    private String username;
    public ArrayList<String> category;
    public ArrayList<String> writers;
    public ArrayList<String> bought;
    private boolean isValid;//if the user object is valid or a new user
    private String surname;
    private String name;
    private String address;
    private String phone;

    public User() {
        isValid=false;
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(Parcel parcel){
        this.username=parcel.readString();//gemizo tis metablites apo to parcel
        this.category=parcel.readArrayList(String.class.getClassLoader());
        this.writers=parcel.readArrayList(String.class.getClassLoader());
        this.bought=parcel.readArrayList(String.class.getClassLoader());
        this.isValid= parcel.readString().equals("true");
        this.surname= parcel.readString();
        this.name= parcel.readString();
        this.address= parcel.readString();
        this.phone= parcel.readString();
        //isValid=true;
    }

    public User(String username) {
        this.username = username;
        this.category=null;
        this.writers=null;
        this.bought=null;
        surname=null;
        name=null;
        address=null;
        phone=null;
        isValid=true;
    }

    public User(String username, ArrayList<String>category, ArrayList<String>writers, ArrayList<String>bought) {
        this.username = username;
        this.category=new ArrayList<String>(category);
        this.writers=new ArrayList<String>(writers);
        this.bought=new ArrayList<String>(bought);
        isValid=true;
        this.surname="";
        this.name="";
        this.address="";
        this.phone="";
    }
    public User(String username, ArrayList<String>category, ArrayList<String>writers, ArrayList<String>bought, String surname, String name, String address, String phone) {
        this.username = username;
        this.category=new ArrayList<String>(category);
        this.writers=new ArrayList<String>(writers);
        this.bought=new ArrayList<String>(bought);
        isValid=true;
        this.surname=surname;
        this.name=name;
        this.address=address;
        this.phone=phone;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public void setWriters(ArrayList<String> writers) {
        this.writers = writers;
    }

    public void setBought(ArrayList<String> bought) {
        this.bought = bought;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /*
    * gia na grapso se parcel, prepei na grafo me tin idia seira pou diabazo
    * */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeList(category);
        parcel.writeList(writers);
        parcel.writeList(bought);
        if (isValid)
            parcel.writeString("true");
        else
            parcel.writeString("false");
        parcel.writeString(surname);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(phone);
    }


    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    /*
    * elegxei an mia katigoria anikei stis katigories pou aresoun sto xristi (category arraylist)
    * */
    public boolean checkIfCategoryIsLiked(String cat){
        if (category==null)
            return false;
        for (int i=0; i<category.size(); i++){
            if (cat.equals(category.get(i))){
                return true;
            }
        }
        return false;
    }

    /*
    * elegxos an o writer wri einai sti lista writers pou aresoun ston user (writers arraylist)
    * */
    public boolean checkIfWriterIsLiked(String wri){
        if (writers==null)
            return false;
        for (int i=0; i<writers.size(); i++){
            if (wri.equals(writers.get(i))){
                return true;
            }
        }
        return false;
    }

    /*
    * an dio xristes exoun idies katigories
    * */
    public boolean checkIfCategoriesEqual(User u){
        if (category==null)
            return false;
        if (u.category==null)
            return false;
        if (u.category.size()!=category.size())
            return false;
        for (int i=0;i<category.size();i++){
            if (!u.checkIfCategoryIsLiked(category.get(i)))
                return false;
        }
        return true;
    }

    /*
     * an dio xristes exoun idious writers
     * */
    public boolean checkIfWritersEqual(User u){
        if (writers==null)
            return false;
        if (u.writers==null)
            return false;
        if (u.writers.size()!=writers.size())
            return false;
        for (int i=0;i<writers.size();i++){
            if (!u.checkIfWriterIsLiked(writers.get(i)))
                return false;
        }
        return true;
    }

    /*
    * elegxei an kapoio isbn exei agorastei apo to xristi (bought arraylist)
    * */
    public boolean checkIfIsbnIsBought(Long isbn){
        if (bought==null)
            return false;
        for (int i=0; i<bought.size(); i++){
            if (bought.get(i).equals(String.valueOf(isbn)))
                return true;
        }
        return false;
    }
/*
    public void showList(){
        for (int i=0; i<category.size(); i++)
            Log.d("$$$$$$$$$", category.get(i));
    }*/
}
