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
import org.codepond.wizardroid.layouts.BasicWizardLayout;

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
    }

    @Override
    public WizardFlow onSetup() {
        return new WizardFlow.Builder()
                .addStep(AddUserStepOneActivity.class)
                .addStep(AddUserStepTwoActivity.class)
                .addStep(AddUserStepThreeActivity.class)
                .create();
    }

    /**
     * Triggered when the wizard is completed.
     * Overriding this method is optional.
     */
    @Override
    public void onWizardComplete() {
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.wizard_next_button:
                ((WizardValidate)wizard.getCurrentStep()).validate();
                break;
            case R.id.wizard_previous_button:
                wizard.goBack();
                break;
        }
        updateWizardControls();
    }

    /**
     * Updates the UI according to current step position
     */
    private void updateWizardControls() {
        previousButton.setEnabled(!wizard.isFirstStep());
        nextButton.setText(wizard.isLastStep()
                ? R.string.action_finish
                : R.string.action_next);
    }
}
