package com.myservice.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.myservice.R;
import com.myservice.model.component.Advertisement;

public class ContactActivity extends AppCompatActivity {


    private Advertisement advertisement = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent it = getIntent();
        advertisement = (Advertisement) it.getSerializableExtra("ADVERTISEMENT");

        if (advertisement != null) {
            ((TextView) findViewById(R.id.txt_item_title)).setText(advertisement.getUser().getName() + " " + advertisement.getUser().getLastName());
            ((TextView) findViewById(R.id.txt_contact_email)).setText(advertisement.getUser().getEmail());
            ((TextView) findViewById(R.id.txt_contact_phone)).setText(advertisement.getUser().getPhone());
            ((TextView) findViewById(R.id.txt_contact_whatsapp)).setText(advertisement.getUser().getCelPhone());
            ((TextView) findViewById(R.id.txt_contact_descricao)).setText(advertisement.getDescription());
            ((TextView) findViewById(R.id.txt_contact_location)).setText(advertisement.getUser().getAddress());
        }
    }
}
