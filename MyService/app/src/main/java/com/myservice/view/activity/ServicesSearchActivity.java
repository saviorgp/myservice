package com.myservice.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.myservice.R;
import com.myservice.model.component.Advertisement;
import com.myservice.model.component.UserVO;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.model.transaction.ITransaction;
import com.myservice.model.transaction.TransactionTask;
import com.myservice.utils.AndroidUtils;
import com.myservice.utils.EndlessScrollListener;
import com.myservice.view.adapter.AdvertisementAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServicesSearchActivity extends AppCompatActivity implements ITransaction {

    private JSONObject resultObject = null;
    private String query = "";
    private Integer current_page = 1;
    private ArrayList<Advertisement> advertisementArrayList  = null;
    private AdvertisementAdapter adapter = null;
    private Integer total = 0;
    public boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_services_search);

        query = getIntent().getStringExtra("QUERY");

        ((TextView)findViewById(R.id.txt_query)).setText(query);

        advertisementArrayList  = new ArrayList<>();
        adapter = new AdvertisementAdapter(this, advertisementArrayList);

        final ListView listView = (ListView) findViewById(R.id.listService);


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView  view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int lastIndexInScreen = visibleItemCount + firstVisibleItem;

                if (lastIndexInScreen>= totalItemCount && !isLoading) {

                    if(adapter.getCount() <  total){

                        isLoading = true;
                        startTransacao(ServicesSearchActivity.this);
                    }
                }

            }
        });

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent(ServicesSearchActivity.this, ContactActivity.class);
                it.putExtra("ADVERTISEMENT", advertisementArrayList.get(position));

                startActivity(it);
            }
        });

        startTransacao(this);
    }

    @Override
    public void execute() throws Exception {
        resultObject = WebServiceWrapper.search(query, current_page);
    }

    @Override
    public void updateView() {

        if(resultObject != null) {

            try {
                total = resultObject.getInt("total");
                current_page = resultObject.getInt("current_page");
                JSONArray data = resultObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {

                    UserVO userVO = new UserVO(data.getJSONObject(i).getJSONObject("user"));
                    Advertisement advertisement = new Advertisement();

                    advertisement.setUser(userVO);
                    advertisement.setTitle(data.getJSONObject(i).getString("title"));
                    advertisement.setDescription(data.getJSONObject(i).getString("description"));

                    adapter.add(advertisement);
                }

                adapter.notifyDataSetChanged();
                isLoading=false;

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
