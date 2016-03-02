package com.myservice.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.myservice.R;
import com.myservice.model.component.Advertisement;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.model.transaction.ITransaction;
import com.myservice.model.transaction.TransactionTask;
import com.myservice.utils.AndroidUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_search);
        listView = (ListView)findViewById(R.id.listService);

        query = getIntent().getStringExtra("QUERY");

        ((TextView)findViewById(R.id.txt_query)).setText(query);

        startTransacao(this);
    }

    @Override
    public void execute() throws Exception {

        resultObject = WebServiceWrapper.search(query);
    }

    @Override
    public void updateView() {

        ArrayList<Advertisement> advertisementArrayList  = new ArrayList<>();
        if(resultObject != null) {

            try {
                JSONArray data = resultObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {

                    Advertisement advertisement = new Advertisement();

                    advertisement.setTitle(data.getJSONObject(i).getString("title"));
                    advertisement.setDescription(data.getJSONObject(i).getString("description"));

                    advertisementArrayList.add(advertisement);
                }

                listView.setAdapter(new AdvertisementAdapter(this, R.id.listService, advertisementArrayList));
                listView.invalidateViews();

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
