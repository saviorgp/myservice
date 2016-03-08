package com.myservice.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.myservice.R;
import com.myservice.model.component.Advertisement;
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

    private TransactionTask task;
    private JSONObject resultObject = null;
    private ListView listView;
    private String query = "";
    private Integer current_page = 1;
    private ArrayList<Advertisement> advertisementArrayList  = null;
    private AdvertisementAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_services_search);

        query = getIntent().getStringExtra("QUERY");

        ((TextView)findViewById(R.id.txt_query)).setText(query);



        advertisementArrayList  = new ArrayList<>();
        adapter = new AdvertisementAdapter(this, advertisementArrayList);

        listView = (ListView)findViewById(R.id.listService);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new EndlessScrollListener(10) {

            @Override
            public void loadMore(int page, int totalItemsCount) {
                if(totalItemsCount > 0){

                    current_page = page;
                    startTransacao(ServicesSearchActivity.this);
                }
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
                current_page = resultObject.getInt("current_page");
                JSONArray data = resultObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {

                    Advertisement advertisement = new Advertisement();

                    advertisement.setTitle(data.getJSONObject(i).getString("title"));
                    advertisement.setDescription(data.getJSONObject(i).getString("description"));

                    advertisementArrayList.add(advertisement);
                }

                adapter.addAll(advertisementArrayList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void startTransacao(ITransaction transacao) {

        boolean redeOk = AndroidUtils.isNetworkAvailable(this);

        if (redeOk) {
            task = new TransactionTask(this, transacao, R.string.wait);
            task.execute();
        } else {
            AndroidUtils.alertDialog(this, "Erro de net");
        }
    }
}
