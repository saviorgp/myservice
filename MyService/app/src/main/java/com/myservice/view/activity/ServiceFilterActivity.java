package com.myservice.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.myservice.R;
import com.myservice.model.component.Category;
import com.myservice.model.component.FilterVO;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.model.transaction.ITransaction;
import com.myservice.model.transaction.TransactionTask;
import com.myservice.utils.AndroidUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceFilterActivity extends AppCompatActivity implements ITransaction {

    public static final String FILTER = "FILTER";
    private static final int LOAD_PARENT_CATEGORY = 0;
    private static final int LOAD_SUB_CATEGORY = 1;
    private int op = LOAD_PARENT_CATEGORY;
    private JSONObject resultObject = null;
    private Integer selectedCategory = null;

    private Spinner categoria;
    private Spinner subcateria;
    private SeekBar preco;
    private EditText localizacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_filter);

        categoria = (Spinner)findViewById(R.id.sp_adv_categoria);
        subcateria = (Spinner)findViewById(R.id.sp_adv_subcategoria);
        localizacao = (EditText)findViewById(R.id.edt_adv_filter_location);
        preco = (SeekBar) findViewById(R.id.seek_preco);

        preco.setProgressDrawable(getResources().getDrawable(R.drawable.seek_progress));

        findViewById(R.id.bt_adv_filer_apply).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                createFilter();
            }
        });

        startTransacao(this);
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

            default: break;
        }
    }

    @Override
    public void updateView() {
        switch (op){

            case 0:{
                loadSpinnerDate(categoria);
                findCategoryByParent();
                break;
            }

            case 1:{

                loadSpinnerDate(subcateria);
                break;
            }

            case 2:{
                try {

                    if(resultObject.has("id")){
                        int adId = resultObject.getInt("id");
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            default: break;
        }
    }

    private void setOP(int value){
        this.op = value;
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
                spinner.setAdapter(new ArrayAdapter<Category>(ServiceFilterActivity.this, android.R.layout.simple_spinner_dropdown_item, categories));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private void findCategoryByParent(){

        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = ((Category) categoria.getSelectedItem()).getId();

                setOP(LOAD_SUB_CATEGORY);
                startTransacao(ServiceFilterActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void createFilter(){

        Intent returnIntent = new Intent();
        FilterVO filterVO = new FilterVO();

        filterVO.setCategoriaID(((Category)subcateria.getSelectedItem()).getId());
        filterVO.setLocalizacao(localizacao.getText().toString());
        filterVO.setValor(preco.getProgress());

        returnIntent.putExtra(FILTER, filterVO);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
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
