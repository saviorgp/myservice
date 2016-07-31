package com.myservice.view.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.myservice.R;
import com.myservice.model.component.Advertisement;
import com.myservice.model.component.Category;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.model.transaction.ITransaction;
import com.myservice.model.transaction.TransactionTask;
import com.myservice.utils.AndroidUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddServiceActivity extends AppCompatActivity implements ITransaction {

    private static final int LOAD_PARENT_CATEGORY = 0;
    private static final int LOAD_SUB_CATEGORY = 1;
    private static final int CREATE_AD = 2;

    private JSONObject resultObject = null;
    private Spinner spinner = null;
    private Spinner spinnerSub = null;
    private Integer selectedCategory = null;
    private EditText title;
    private EditText description;
    private EditText phone;
    private int op = LOAD_PARENT_CATEGORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.action_add_service);
        spinner = (Spinner)findViewById(R.id.sp_category_add_service);
        spinnerSub = (Spinner)findViewById(R.id.sp_sub_category_add_service);

        title = (EditText)findViewById(R.id.edt_title_add_service);
        description = (EditText)findViewById(R.id.edt_description_add_service);
        phone = (EditText)findViewById(R.id.edt_telefone_add_service);

        findViewById(R.id.bt_add_anuncio_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOP(CREATE_AD);
                startTransacao(AddServiceActivity.this);
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
                      ad.setPhone(phone.getText().toString());
                      ad.setCategory((Category) spinnerSub.getSelectedItem());

        return ad;
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
                resultObject = WebServiceWrapper.createService(createAd(), getApplicationContext());
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
                    Log.i("ad", resultObject.getString("id"));
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
}
