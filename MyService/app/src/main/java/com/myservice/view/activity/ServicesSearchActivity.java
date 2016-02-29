package com.myservice.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.myservice.R;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.model.transaction.ITransaction;
import com.myservice.model.transaction.TransactionTask;
import com.myservice.utils.AndroidUtils;

public class ServicesSearchActivity extends AppCompatActivity implements ITransaction {

    private TransactionTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_search);

        startTransacao(this);
    }

    @Override
    public void execute() throws Exception {
        WebServiceWrapper.search("");
    }

    @Override
    public void updateView() {
        Log.i("SERACH", "EXECUTOU");
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
