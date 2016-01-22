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
import com.myservice.helper.MailSenderHelper;
import com.myservice.helper.WebServiceHelper;
import com.myservice.model.Preferences;
import com.myservice.utils.Constants;

public class ForgotPasswdActivity extends AppCompatActivity {

    private Button mChangePasswdBtn;

    private EditText mEmailTxt;
    private EditText mCodeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwd);

        mEmailTxt = (EditText) findViewById(R.id.edt_username);
        mCodeTxt = (EditText) findViewById(R.id.edt_password);

        mChangePasswdBtn = (Button) findViewById(R.id.bt_login);
        mChangePasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object token =
                        Preferences.getPreferences(ForgotPasswdActivity.this)
                                .getSharedPreference(Constants.PREF_RESET_PASSWD_TOKEN);

                if(token != null){
                    if(token.toString().equals(mCodeTxt.getText().toString())){
                        new AsyncTask<String, Void, Boolean>() {
                            private ProgressDialog progressDialog;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                        progressDialog = ProgressDialog.show(ForgotPasswdActivity.this,
                                        ForgotPasswdActivity.this.getString(R.string.title_wait),
                                        ForgotPasswdActivity.this.getString(R.string.msg_send_new_passwd_mail),
                                        true, false);
                                        progressDialog.setCancelable(false);
                            }

                            @Override
                            protected void onPostExecute(Boolean result) {
                                super.onPostExecute(result);
                                progressDialog.dismiss();
                                if(result){
                                    Intent intent = new Intent(ForgotPasswdActivity.this,
                                                               LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPasswdActivity.this,
                                                   ForgotPasswdActivity.this.getString(
                                                   R.string.msg_forgot_passwd_gen_token_error),
                                                   Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            protected Boolean doInBackground(String... email) {
                                boolean result = false;
                                try {
                                    String newPasswd = WebServiceHelper.resetPassword(email[0]);
                                    MailSenderHelper.getInstance().sendNewPasswdMail(email[0],
                                                              newPasswd, ForgotPasswdActivity.this);
                                    result = true;
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }

                                return result;
                            }
                        }.execute(mEmailTxt.getText().toString());
                    } else {
                        Toast.makeText(ForgotPasswdActivity.this,
                               ForgotPasswdActivity.this.getString(R.string.forgot_wrong_token_msg),
                               Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}