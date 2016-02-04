package com.myservice.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myservice.R;
import com.myservice.helper.WebServiceHelper;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button mChangePasswdBtn;

    private EditText mNewPasswdTxt;
    private EditText mNewPasswdConfTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mNewPasswdTxt = (EditText) findViewById(R.id.edt_new_passwd);
        mNewPasswdConfTxt = (EditText) findViewById(R.id.edt_new_passwd_conf);

        mChangePasswdBtn = (Button) findViewById(R.id.bt_login);
        mChangePasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              
                if (mNewPasswdTxt.getText().toString().trim().length() > 0 
                        && mNewPasswdConfTxt.getText().toString().trim().length() > 0) {
                    if (mNewPasswdTxt.getText().toString().trim()
                            .equals(mNewPasswdConfTxt.getText().toString().trim())) {
                        new AsyncTask<String, Void, Boolean>() {
                            private ProgressDialog progressDialog;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog = ProgressDialog.show(ResetPasswordActivity.this,
                                        ResetPasswordActivity.this.getString(R.string.title_wait),
                                        ResetPasswordActivity.this.getString(R.string.msg_reset_passwd_request),
                                        true, false);
                                progressDialog.setCancelable(false);
                            }

                            @Override
                            protected void onPostExecute(Boolean result) {
                                super.onPostExecute(result);
                                progressDialog.dismiss();
                                if (result) {
                                    Intent intent = new Intent(ResetPasswordActivity.this,
                                            ServicesSearchActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this,
                                            ResetPasswordActivity.this.getString(
                                                    R.string.msg_reset_passwd_error),
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            protected Boolean doInBackground(String... passwd) {
                                boolean result = false;
                                try {
                                    result = WebServiceHelper.changePassword(passwd[0],
                                                                   ResetPasswordActivity.this);
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }

                                return result;
                            }
                        }.execute(mNewPasswdTxt.getText().toString());
                    } else {
                        Toast.makeText(ResetPasswordActivity.this,
                                ResetPasswordActivity.this.getString(R.string.forgot_wrong_passwd_reset),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}