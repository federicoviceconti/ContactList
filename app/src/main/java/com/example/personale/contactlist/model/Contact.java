package com.example.personale.contactlist.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by personale on 09/03/2017.
 */

public class Contact implements Parcelable{
    private int id;
    private String name, mail, phone, address;

    public Contact(String name, String mail, String phone, String address){
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.address = address;
    }

    protected Contact(Parcel in) {
        id = in.readInt();
        name = in.readString();
        mail = in.readString();
        phone = in.readString();
        address = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.mail);
        dest.writeString(this.phone);
        dest.writeString(this.address);
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public int getId() {
        return id;
    }

    public Contact setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Contact setName(String name) {
        this.name = name;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public Contact setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Contact setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Contact setAddress(String address) {
        this.address = address;
        return this;
    }
}
