package com.myservice.view.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.myservice.R;
import com.myservice.utils.WizardValidate;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.WizardFragment;

public class AddUserFlowActivity extends WizardFragment implements View.OnClickListener {


    private Button nextButton;
    private Button previousButton;

    public AddUserFlowActivity(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View wizardLayout = inflater.inflate(R.layout.wizart_layout, container, false);

        nextButton = (Button) wizardLayout.findViewById(R.id.wizard_next_button);
        nextButton.setOnClickListener(this);

        previousButton = (Button) wizardLayout.findViewById(R.id.wizard_previous_button);
        previousButton.setOnClickListener(this);


        return wizardLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MyServiceApplication)getActivity().getApplicationContext()).setWizard(wizard);
        ((MyServiceApplication)getActivity().getApplicationContext()).setCurrentPage(0);

    }

    @Override
    public WizardFlow onSetup() {
        return new WizardFlow.Builder()
                .addStep(AddUserStepOneActivity.class)
                .addStep(AddUserStepTwoActivity.class)
                .addStep(AddUserStepThreeActivity.class)
                .create();
    }


    @Override
    public void onWizardComplete() {
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        int page = ((MyServiceApplication)getActivity().getApplicationContext()).getCurrentPage();
        switch(v.getId()) {
            case R.id.wizard_next_button:
                ((WizardValidate)wizard.getCurrentStep()).validate();
                break;
            case R.id.wizard_previous_button:{
                ((MyServiceApplication)getActivity().getApplicationContext()).setCurrentPage(page - 1);
                wizard.goBack();
            }
            break;
        }
        updateWizardControls();
    }

    private void updateWizardControls() {

        previousButton.setText(R.string.anterior);
        previousButton.setEnabled(!wizard.isFirstStep());

        nextButton.setEnabled(!wizard.isLastStep());
        nextButton.setText(wizard.isLastStep() ? R.string.finalizar: R.string.proximo);

        int page = ((MyServiceApplication)getActivity().getApplicationContext()).getCurrentPage();

        if(((MyServiceApplication)getActivity().getApplicationContext()).getCurrentPage() == 2){
            nextButton.setEnabled(false);
            previousButton.setEnabled(false);
        }
    }
}
