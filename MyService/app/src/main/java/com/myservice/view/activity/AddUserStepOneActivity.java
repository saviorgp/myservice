package com.myservice.view.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.myservice.R;
import com.myservice.model.component.UserVO;
import com.myservice.model.transaction.ITransaction;
import com.myservice.model.transaction.TransactionTask;
import com.myservice.utils.AndroidUtils;
import com.myservice.utils.WizardValidate;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.List;

public class AddUserStepOneActivity extends WizardStep implements Validator.ValidationListener, WizardValidate {

    @ContextVariable
    private UserVO user;

    @NotEmpty
    private EditText nome;

    @NotEmpty
    @Email
    private EditText email;

    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC)
    private EditText password;

    @ConfirmPassword
    private EditText confirmPassword;

    private Validator validator;

    public AddUserStepOneActivity(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_add_service_one, container, false);

        nome = (EditText)v.findViewById(R.id.edt_nome_add_user);
        email = (EditText)v.findViewById(R.id.edt_email_add_user);
        password = (EditText)v.findViewById(R.id.edt_senha_add_user);
        confirmPassword = (EditText)v.findViewById(R.id.edt_senha_confirm_add_user);

        v.findViewById(R.id.bt_add_anuncio_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);

        return v;
    }

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                bindDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                break;
        }
    }

    private void bindDataFields() {

        user = new UserVO();

        user.setName(nome.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    @Override
    public void validate(){
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        ((MyServiceApplication)getActivity().getApplicationContext()).getWizard().goNext();;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();

            String message = error.getCollatedErrorMessage(getActivity());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }

}
