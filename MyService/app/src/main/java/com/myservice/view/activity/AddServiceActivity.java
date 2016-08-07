package com.myservice.view.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.myservice.R;
import com.myservice.model.component.Advertisement;
import com.myservice.model.component.Category;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.model.transaction.ITransaction;
import com.myservice.model.transaction.TransactionTask;
import com.myservice.utils.AndroidUtils;
import com.myservice.utils.EditMask;
import com.myservice.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AddServiceActivity extends AppCompatActivity implements ITransaction, Validator.ValidationListener {

    private static final int LOAD_PARENT_CATEGORY = 0;
    private static final int LOAD_SUB_CATEGORY = 1;
    private static final int CREATE_AD = 2;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 2;
    public static final String TIRAR_UMA_FOTO = "Tirar uma Foto";
    public static final String ESCOLHER_DA_GALERIA = "Escolher da Galeria";
    public static final String CANCELAR = "Cancelar";

    private JSONObject resultObject = null;
    private Spinner spinner = null;
    private Spinner spinnerSub = null;
    private Integer selectedCategory = null;

    @NotEmpty
    private EditText title;

    @NotEmpty
    private EditText description;

    @NotEmpty
    private EditText price;

    @NotEmpty
    @Pattern(regex = "[0-9]{2}/[0-9]{2}/[0-9]{4}")
    private EditText expirationDate;

    private int op = LOAD_PARENT_CATEGORY;

    private ImageView imgAnuncio = null;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.action_add_service);
        spinner = (Spinner)findViewById(R.id.sp_category_add_service);
        spinnerSub = (Spinner)findViewById(R.id.sp_sub_category_add_service);

        title = (EditText)findViewById(R.id.edt_title_add_service);
        description = (EditText)findViewById(R.id.edt_description_add_service);
        price = (EditText)findViewById(R.id.edt_price_add_service);
        imgAnuncio =  (ImageView)findViewById(R.id.img_add_anuncio);

        expirationDate = (EditText)findViewById(R.id.edt_expiration_date_add_service);
        expirationDate.addTextChangedListener(EditMask.insert("##/##/####", expirationDate));

        validator = new Validator(this);
        validator.setValidationListener(this);

        findViewById(R.id.bt_add_anuncio_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOP(CREATE_AD);
                startTransacao(AddServiceActivity.this);
            }
        });

        imgAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionPhoto();
            }
        });


        startTransacao(this);
    }

    private void findCategoryByParent(){

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = ((Category) spinner.getSelectedItem()).getId();

                setOP(LOAD_SUB_CATEGORY);
                startTransacao(AddServiceActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setOP(int value){
        this.op = value;
    }

    private Advertisement createAd(){

        Advertisement ad = new Advertisement();

                      ad.setTitle(title.getText().toString());
                      ad.setDescription(description.getText().toString());
                      ad.setCategory((Category) spinnerSub.getSelectedItem());
                      ad.setPrice(BigDecimal.valueOf(Double.parseDouble(price.getText().toString())));
                      ad.setPhoto(getImageFromImageView());
        try {
            ad.setExpiration_at(Utils.toDate(expirationDate.getText().toString(), "dd/MM/yyyy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ad;
    }

    private Bitmap getImageFromImageView(){

        if(imgAnuncio.getDrawable() == null){
            return null;
        }

        BitmapDrawable drawable = (BitmapDrawable) imgAnuncio.getDrawable();


        return drawable.getBitmap();
    }

    @Override
    public void execute() throws Exception {


        switch (op){

            case 0:{
                resultObject = WebServiceWrapper.categories(null);
                break;
            }

            case 1:{

                resultObject = WebServiceWrapper.categories(selectedCategory.toString());
                break;
            }

            case 2:{
                validator.validate();
                break;
            }

            default: break;
        }
    }

    @Override
    public void updateView() {

        switch (op){

            case 0:{
                loadSpinnerDate(spinner);
                findCategoryByParent();
                break;
            }

            case 1:{

                loadSpinnerDate(spinnerSub);
                break;
            }

            case 2:{
                try {

                    if(resultObject.has("id")){
                       int adId = resultObject.getInt("id");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            default: break;
        }
    }

    private void loadSpinnerDate(Spinner spinner){

        if(resultObject != null){

            try {

                ArrayList<Category> categories = new ArrayList<Category>();
                JSONArray data = resultObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {

                    categories.add(new Category(data.getJSONObject(i)));
                }

                spinner.setAdapter(null);
                spinner.setAdapter(new ArrayAdapter<Category>(AddServiceActivity.this, android.R.layout.simple_spinner_dropdown_item, categories));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void startTransacao(ITransaction transacao) {

        boolean redeOk = AndroidUtils.isNetworkAvailable(this);

        if (redeOk) {
            TransactionTask task = new TransactionTask(this, transacao, R.string.wait);
            task.execute();
        } else {
            AndroidUtils.alertDialog(this, "Erro de net");
        }
    }

    private void showOptionPhoto(){

        final CharSequence[] items = {TIRAR_UMA_FOTO, ESCOLHER_DA_GALERIA, CANCELAR};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(AddServiceActivity.this);
        
        builder.setTitle("Adicionar Foto");
        
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                
                if (items[item].equals(TIRAR_UMA_FOTO)) {
                        cameraIntent();
                } else if (items[item].equals(ESCOLHER_DA_GALERIA)) {
                        galleryIntent();
                } else if (items[item].equals(CANCELAR)) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecionar Arquivo"), SELECT_FILE);
    }

    private void cameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgAnuncio.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgAnuncio.setImageBitmap(bm);
    }

    @Override
    public void onValidationSucceeded() {
          if(op == 2){
              resultObject = WebServiceWrapper.createAd(createAd(), getApplicationContext());
          }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();

            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }
}
