package com.myservice.view.activity;


import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;

public class AddUserFlowActivity extends BasicWizardLayout {


    public AddUserFlowActivity(){
        super();
    }

    @Override
    public WizardFlow onSetup() {

        return new WizardFlow.Builder()
                .addStep(AddUserStepOneActivity.class)
                .addStep(AddUserStepTwoActivity.class)
                .addStep(AddUserStepThreeActivity.class)
                .create();
    }
}
