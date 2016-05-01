package com.myservice.view.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
            String name = advertisement.getUser().getName() + " " + advertisement.getUser().getLastName();

            ((TextView) findViewById(R.id.txt_item_title)).setText(name);
            ((TextView) findViewById(R.id.txt_item_description)).setText(advertisement.getCategory().getName());
            ((TextView) findViewById(R.id.txt_toolbar_user_name)).setText(name);
            ((TextView) findViewById(R.id.txt_contact_descricao)).setText(advertisement.getDescription());

            findViewById(R.id.menu_left_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            findViewById(R.id.ic_contact_phone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (advertisement.getUser().getCelPhone() != null) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + advertisement.getUser().getCelPhone()));
                        startActivity(callIntent);
                    }
                }
            });

            findViewById(R.id.ic_contact_whatsapp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (advertisement.getUser().getCelPhone() != null) {

                        Uri uri = Uri.parse("smsto:" + advertisement.getUser().getCelPhone());
                        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                        i.setPackage("com.whatsapp");
                        startActivity(Intent.createChooser(i, ""));
                    }

                }
            });

            findViewById(R.id.ic_contact_location).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:37.827500,-122.481670"));
                    i.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                    startActivity(i);
                }
            });
            findViewById(R.id.ic_contact_email).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", advertisement.getUser().getEmail(), null));
                    startActivity(Intent.createChooser(emailIntent, "Contato"));
                }
            });


        }
    }
}
