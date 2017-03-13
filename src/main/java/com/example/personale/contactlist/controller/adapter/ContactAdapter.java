package com.example.personale.contactlist.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.personale.contactlist.R;
import com.example.personale.contactlist.activities.AddNewContact;
import com.example.personale.contactlist.activities.MainActivity;
import com.example.personale.contactlist.controller.list.ContactList;
import com.example.personale.contactlist.controller.utility.Field;
import com.example.personale.contactlist.database.DbField;
import com.example.personale.contactlist.model.Contact;

/**
 * Created by personale on 09/03/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactVH> {

    private final ContactList contactList;

    public ContactAdapter(Context c) {
        this.contactList = new ContactList(c);
    }

    @Override
    public ContactVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_main, null));
    }

    @Override
    public void onBindViewHolder(ContactVH holder, int position) {
        Contact c = contactList.getContact(position);

        holder.name.setText(c.getName());
        holder.address.setText(c.getAddress());
    }

    @Override
    public int getItemCount() {
        return contactList.getSize();
    }

    public void addContact(Intent data){
        contactList.addContact(getContactByData(data));
        notifyItemInserted(0);
    }

    public void editContact(Intent data, int pos){
        if(pos != -1){
            contactList.editContact(pos, getContactByData(data));
            notifyItemChanged(pos);
        }
    }

    public Contact getContactByData(Intent data){
        return  new Contact(
                data.getStringExtra(DbField.NAME),
                data.getStringExtra(DbField.MAIL),
                data.getStringExtra(DbField.PHONE),
                data.getStringExtra(DbField.ADDRESS)
        );
    }

    public void loadFromDb() {
        contactList.loadContactFromDb();
    }

    class ContactVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView name, address;

        public ContactVH(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            itemView.findViewById(R.id.phone).setOnClickListener(this);
            itemView.findViewById(R.id.mail).setOnClickListener(this);
            itemView.findViewById(R.id.card_view).setOnCreateContextMenuListener(this);
            itemView.findViewById(R.id.card_view).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent();

            switch (v.getId()) {
                case R.id.phone:
                    i.setAction(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:" + contactList.getContact(getAdapterPosition()).getPhone()));
                    break;
                case R.id.mail:
                    i.setAction(Intent.ACTION_SENDTO);
                    i.setData(Uri.parse("mailto:" + contactList.getContact(getAdapterPosition()).getMail()));
                    break;
                case R.id.card_view:
                    i = new Intent(v.getContext(), AddNewContact.class);
                    i.setFlags(Field.EDIT);
                    i.putExtra(Field.OBJECT, contactList.getContact(getAdapterPosition()));
            }

            v.getContext().startActivity(i);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = ((MainActivity)v.getContext()).getMenuInflater();
            inflater.inflate(R.menu.item_menu, menu);
        }
    }
}
