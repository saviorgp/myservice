package com.myservice.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.myservice.R;
import com.myservice.helper.WebServiceHelper;
import com.myservice.model.component.UserVO;
import com.myservice.model.transaction.ITransaction;
import com.myservice.model.transaction.TransactionTask;
import com.myservice.utils.AndroidUtils;
import com.myservice.utils.WizardValidate;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.List;


public class AddUserStepTwoActivity extends WizardStep implements Validator.ValidationListener, WizardValidate, ITransaction {

    @ContextVariable
    private UserVO user;

    private Validator validator;

    @NotEmpty
    private EditText cep;
    @NotEmpty
    private EditText rua;
    private EditText bairro;
    private EditText complemento;

    @NotEmpty
    private EditText estado;

    @NotEmpty
    private EditText cidade;

    private EditText numero;


    public AddUserStepTwoActivity() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_add_service_two, container, false);

        cep = (EditText)v.findViewById(R.id.edt_cep_add_user);
        rua = (EditText)v.findViewById(R.id.edt_rua_add_user);
        bairro = (EditText)v.findViewById(R.id.edt_bairro_add_user);
        complemento = (EditText)v.findViewById(R.id.edt_complemento_add_user);
        estado = (EditText)v.findViewById(R.id.edt_estado_add_user);
        cidade = (EditText)v.findViewById(R.id.edt_cidade_add_user);
        numero = (EditText)v.findViewById(R.id.edt_numero_add_user);

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
    public void onValidationSucceeded() {
        startTransacao(this);
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

    @Override
    public void validate() {
        validator.validate();
    }

    @Override
    public void execute() throws Exception {
        try {

            user.setAddress(rua.getText().toString());
            user.setCity(cidade.getText().toString());
            user.setState(estado.getText().toString());
            user.setNeighborhood(bairro.getText().toString());
            user.setZipCode(cep.getText().toString());
            user.setNumber(numero.getText().toString());
            user.setComplement(complemento.getText().toString());

            WebServiceHelper.signupUser(user);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void updateView() {
        ((MyServiceApplication)getActivity().getApplicationContext()).getWizard().goNext();;
    }


    public void startTransacao(ITransaction transacao) {

        boolean redeOk = AndroidUtils.isNetworkAvailable(getActivity());

        if (redeOk) {
            TransactionTask task = new TransactionTask(getActivity(), transacao, R.string.wait);
            task.execute();
        } else {
            AndroidUtils.alertDialog(getActivity(), "Erro de net");
        }
    }
}
