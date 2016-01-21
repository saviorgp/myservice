package com.myservice.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myservice.R;
import com.myservice.exceptions.LoginException;
import com.myservice.helper.WebServiceHelper;
import com.myservice.model.component.UserVO;


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

                    @Override
                    protected void onPreExecute() {
                        mUserVO.setEmail(mEmailTxt.getText().toString());
                        mUserVO.setPassword(mPasswdTxt.getText().toString());
                    }

                    @Override
                    protected Boolean doInBackground(Boolean... params) {
                        boolean result = false;
                        try{
                            WebServiceHelper.authenticateUser(mUserVO);
                            result = true;
                        } catch (LoginException l){
                            mErroMsg = l.getMessage();
                        } catch(Throwable t){
                        }

                        return result;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if(result){
                            Intent intent = new Intent(LoginActivity.this,
                                                       ServicesSearchActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this,mErroMsg,Toast.LENGTH_LONG).show();
                        }
                    }
                };
            }
        });
    }
}

