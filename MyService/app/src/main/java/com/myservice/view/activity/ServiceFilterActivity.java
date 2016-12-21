package com.myservice.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.myservice.R;
import com.myservice.model.component.Category;
import com.myservice.model.component.FilterVO;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.model.transaction.ITransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceFilterActivity extends BaseActivity implements ITransaction {

    public static final String FILTER = "FILTER";
    private static final int LOAD_PARENT_CATEGORY = 0;
    private static final int LOAD_SUB_CATEGORY = 1;
    private int op = LOAD_PARENT_CATEGORY;
    private JSONObject resultObject = null;
    private Integer selectedCategory = null;

    private Spinner categoria;
    private Spinner subcateria;
    private EditText cidade;
    private Boolean orderDate;
    private Boolean orderPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_filter);

        orderDate = false;
        orderPrice = false;

        categoria = (Spinner)findViewById(R.id.sp_adv_categoria);
        subcateria = (Spinner)findViewById(R.id.sp_adv_subcategoria);
        subcateria.setVisibility(View.GONE);

        cidade = (EditText)findViewById(R.id.edt_adv_filter_city);

        findViewById(R.id.bt_adv_filer_apply).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                createFilter();
            }
        });

        findViewById(R.id.bt_adv_filter_close).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.bt_order_filter_less).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderDate = false;
                ((ImageButton)findViewById(R.id.bt_order_filter_less)).setImageResource(R.drawable.bt_filter_less_recent_enable);
                ((ImageButton)findViewById(R.id.bt_order_filter_more)).setImageResource(R.drawable.bt_filter_less_recent_disable);
            }
        });

        findViewById(R.id.bt_order_filter_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderDate = true;
                ((ImageButton)findViewById(R.id.bt_order_filter_less)).setImageResource(R.drawable.bt_filter_less_recent_disable);
                ((ImageButton)findViewById(R.id.bt_order_filter_more)).setImageResource(R.drawable.bt_filter_less_recent_enable);
            }
        });

        findViewById(R.id.bt_order_low_price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderPrice = false;
                ((ImageButton)findViewById(R.id.bt_order_low_price)).setImageResource(R.drawable.bt_filter_lowest_price_enable);
                ((ImageButton)findViewById(R.id.bt_order_big_price)).setImageResource(R.drawable.bt_filter_lowest_price_disable);
            }
        });

        findViewById(R.id.bt_order_big_price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderPrice = true;
                ((ImageButton) findViewById(R.id.bt_order_low_price)).setImageResource(R.drawable.bt_filter_lowest_price_disable);
                ((ImageButton) findViewById(R.id.bt_order_big_price)).setImageResource(R.drawable.bt_filter_lowest_price_enable);
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

                if(this.op == LOAD_SUB_CATEGORY && data.length() > 0 ){
                    subcateria.setVisibility(View.VISIBLE);
                }
                else{
                    subcateria.setVisibility(View.GONE);
                }

                if(spinner.equals(categoria)){
                    categories.add(new Category("Todas as categorias",0));
                }

                if(spinner.equals(subcateria)){
                    categories.add(new Category("Selecione uma subcategoria",0));
                }

                for (int i = 0; i < data.length(); i++) {

                    categories.add(new Category(data.getJSONObject(i)));
                }

                spinner.setAdapter(null);
                ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(ServiceFilterActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);

                spinner.setAdapter(adapter);

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

                if(selectedCategory != 0){
                    setOP(LOAD_SUB_CATEGORY);
                    startTransacao(ServiceFilterActivity.this);
                }
                else{
                    subcateria.setAdapter(null);
                    subcateria.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void createFilter(){

        Intent returnIntent = new Intent();
        FilterVO filterVO = new FilterVO();

        if(((Category)categoria.getSelectedItem()).getId() != 0){
            filterVO.setCategoriaID(((Category) categoria.getSelectedItem()).getId());
        }
        if(((Category)subcateria.getSelectedItem()).getId() != 0){
            filterVO.setSubCategoriaID(((Category) subcateria.getSelectedItem()).getId());
        }

        filterVO.setLocalizacao(cidade.getText().toString());
        filterVO.setDataOrder(orderDate);
        filterVO.setPrecoOrder(orderPrice);

        returnIntent.putExtra(FILTER, filterVO);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }
}
