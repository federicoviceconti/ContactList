package com.example.personale.contactlist.controller.list;

import android.content.Context;

import com.example.personale.contactlist.database.DbHandler;
import com.example.personale.contactlist.model.Contact;

import java.util.ArrayList;

/**
 * Created by personale on 09/03/2017.
 */

public class ContactList {
    private ArrayList<Contact> contacts;
    private DbHandler dbHandler;

    public ContactList(Context c){
        createContactList(c);
    }

    private synchronized void createContactList(Context c){
        if(contacts == null){
            this.contacts = new ArrayList<>();
        }

        dbHandler = new DbHandler(c);
    }

    public ArrayList<Contact> getAllContacts(){
        return new ArrayList<>(contacts);
    }

    public void setAllContacts(ArrayList<Contact> contacts){
        this.contacts = contacts;
    }

    public Contact getContact(int position){
        return contacts.get(position);
    }

    public void setContact(int position, Contact newContact){
        contacts.set(position, newContact);
    }

    public int getSize() {
        return contacts.size();
    }

    public void addContact(Contact c) {
        contacts.add(0, c);
        dbHandler.insert(c);
    }

    public void editContact(int position, Contact c){
        contacts.set(position, c);
        dbHandler.edit(c);
    }

    public void deleteContact(int position){
        contacts.remove(position);
        dbHandler.delete(findContactById(position));
    }

    public int findContactById(int position){
        return contacts.get(position).getId();
    }

    public void loadContactFromDb() {
        this.contacts = dbHandler.getAllContacts();
    }
}
