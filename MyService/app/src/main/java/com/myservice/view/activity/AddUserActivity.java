package com.myservice.view.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myservice.R;
import com.myservice.exceptions.CreateUserException;
import com.myservice.helper.WebServiceHelper;
import com.myservice.model.component.UserVO;

public class AddUserActivity extends AppCompatActivity {

    private Button mOkBtn;
    private Button mCancelBtn;
    private EditText mNomeTxt;
    private EditText mSobrenomeTxt;
    private EditText mEmailTxt;
    private EditText mSenhaTxt;
    private EditText mCelTxt;
    private EditText mFixoTxt;
    private EditText mEnderecoTxt;
    private EditText mCidadeTxt;
    private EditText mEstadoTxt;
    private UserVO mUserVO = new UserVO();

    private String mErroMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mNomeTxt = (EditText) findViewById(R.id.nome_txt);
        mSobrenomeTxt = (EditText) findViewById(R.id.sobrenome_txt);
        mEmailTxt = (EditText) findViewById(R.id.email_txt);
        mSenhaTxt = (EditText) findViewById(R.id.senha_txt);
        mCelTxt = (EditText) findViewById(R.id.celular_txt);
        mFixoTxt = (EditText) findViewById(R.id.fixo_txt);
        mEnderecoTxt = (EditText) findViewById(R.id.endereco_txt);
        mCidadeTxt = (EditText) findViewById(R.id.cidade_txt);
        mEstadoTxt = (EditText) findViewById(R.id.estado_txt);

        mOkBtn = (Button) findViewById(R.id.ok_btn);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mErroMsg = "";
                new AsyncTask<Boolean, Void, Boolean>() {

                    @Override
                    protected void onPreExecute() {
                        mUserVO.setName(mNomeTxt.getText().toString());
                        mUserVO.setLastName(mSobrenomeTxt.getText().toString());
                        mUserVO.setEmail(mEmailTxt.getText().toString());
                        mUserVO.setPassword(mSenhaTxt.getText().toString());
                        mUserVO.setCelPhone(mCelTxt.getText().toString());
                        mUserVO.setPhone(mFixoTxt.getText().toString());
                        mUserVO.setAddress(mEnderecoTxt.getText().toString());
                        mUserVO.setCity(mCidadeTxt.getText().toString());
                        mUserVO.setState(mEstadoTxt.getText().toString());
                    }

                    @Override
                    protected Boolean doInBackground(Boolean... booleans) {
                        boolean result = false;
                        try {
                            WebServiceHelper.signupUser(mUserVO);
                            result = true;
                        } catch(CreateUserException c){
                        } catch (Throwable t) {

                        }

                        return result;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (result) {
                            finish();
                        } else {
                            if(!mErroMsg.isEmpty()) {
                                Toast.makeText(AddUserActivity.this,
                                               mErroMsg, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddUserActivity.this,
                                   AddUserActivity.this.getString(R.string.msg_criacao_login_error),
                                   Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                };
            }
        });

        mCancelBtn = (Button) findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}