package com.myservice.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myservice.R;
import com.myservice.exceptions.LoginException;
import com.myservice.helper.MailSenderHelper;
import com.myservice.helper.WebServiceHelper;
import com.myservice.model.Preferences;
import com.myservice.model.component.UserVO;
import com.myservice.utils.Constants;

public class LoginActivity extends Activity{

    private Button mLoginBtn;
    private Button mCreateLoginBtn;
    private Button mForgotPasswdBtn;

    private EditText mEmailTxt;
    private EditText mPasswdTxt;

    private UserVO mUserVO = new UserVO();
    private String mErroMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_block).getBackground().setAlpha(127);

        mEmailTxt = (EditText) findViewById(R.id.edt_username);
        mPasswdTxt = (EditText) findViewById(R.id.edt_password);

        mCreateLoginBtn = (Button) findViewById(R.id.button2);
        mCreateLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });

        mLoginBtn = (Button) findViewById(R.id.bt_login);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mErroMsg = "";
                new AsyncTask<Boolean, Void, Boolean>(){
                    private ProgressDialog progressDialog;
                    
                    @Override
                    protected void onPreExecute() {
                        progressDialog = ProgressDialog.show(LoginActivity.this,
                                LoginActivity.this.getString(R.string.title_wait),
                                LoginActivity.this.getString(R.string.msg_login_in),
                                true, false);
                        progressDialog.setCancelable(false);
                        
                        mUserVO.setEmail(mEmailTxt.getText().toString());
                        mUserVO.setPassword(mPasswdTxt.getText().toString());
                    }

                    @Override
                    protected Boolean doInBackground(Boolean... params) {
                        boolean result = false;
                        try{
                            WebServiceHelper.authenticateUser(mUserVO, LoginActivity.this);
                            result = true;
                        } catch (LoginException l){
                            mErroMsg = l.getMessage();
                        } catch(Throwable t){
                        }

                        return result;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        progressDialog.dismiss();
                        if(result){
                            boolean needToReset = Preferences.getPreferences(LoginActivity.this)
                                 .getSharedPreference(Constants.PREF_NEED_TO_RESET_PASSWD) != null ?
                                  ((Boolean)Preferences.getPreferences(LoginActivity.this)
                                      .getSharedPreference(Constants.PREF_NEED_TO_RESET_PASSWD))
                                         .booleanValue() : false;
                            
                            Intent intent = null;
                            if(needToReset) {
                                intent = new Intent(LoginActivity.this,
                                                    ResetPasswordActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this,
                                                    ServicesSearchActivity.class);
                            }
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this,mErroMsg,Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
            }
        });

        mCreateLoginBtn = (Button) findViewById(R.id.button2);
        mCreateLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });

        mForgotPasswdBtn = (Button) findViewById(R.id.button);
        mForgotPasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, Boolean>() {
                    private ProgressDialog progressDialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = ProgressDialog.show(LoginActivity.this,
                                LoginActivity.this.getString(R.string.title_wait),
                                LoginActivity.this.getString(R.string.msg_send_token_mail),
                                true, false);
                        progressDialog.setCancelable(false);
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        super.onPostExecute(result);
                        progressDialog.dismiss();
                        if(result){
                            Intent intent = new Intent(LoginActivity.this, ForgotPasswdActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    LoginActivity.this.getString(
                                            R.string.msg_forgot_passwd_gen_token_error),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    protected Boolean doInBackground(String... email) {
                        boolean result = false;
                        try {
                            MailSenderHelper.getInstance().sendResetPasswdMail(email[0],
                                                                               LoginActivity.this);
                            result = true;
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }

                        return result;
                    }
                }.execute(mEmailTxt.getText().toString());
            }
        });
    }
}