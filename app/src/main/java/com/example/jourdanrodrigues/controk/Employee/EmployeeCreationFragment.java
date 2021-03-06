package com.example.jourdanrodrigues.controk.Employee;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jourdanrodrigues.controk.BaseFragmentCreation;
import com.example.jourdanrodrigues.controk.Contact.ContactCreationFragment;
import com.example.jourdanrodrigues.controk.R;

public class EmployeeCreationFragment extends BaseFragmentCreation {
    private EditText mName;
    private EditText mEmail;
    private EditText mCpf;
    private EditText mRole;
    private EditText mObs;

    public EmployeeCreationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        assert view != null;

        view.findViewById(R.id.fab_create_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!errorInFields()) {
                    continueCreation();
                }
            }
        });

        return view;
    }

    private void continueCreation() {
        EmployeeCreationActivity activity = (EmployeeCreationActivity) getActivity();
        activity.mEmployee = new Employee(
            mName.getText().toString(),
            mEmail.getText().toString(),
            mCpf.getText().toString(),
            mRole.getText().toString(),
            mObs.getText().toString()
        );
        activity.updateFragment(new ContactCreationFragment(), "EmployeeCreationFragment");
    }

    protected Boolean errorInFields() {
        return mName.getError() != null || mEmail.getError() != null || mCpf.getError() != null || mObs.getError() != null;
    }

    protected void initializeFields(View view) {
        mName = ((TextInputLayout) view.findViewById(R.id.employee_name_field)).getEditText();
        mEmail = ((TextInputLayout) view.findViewById(R.id.employee_email_field)).getEditText();
        mCpf = ((TextInputLayout) view.findViewById(R.id.employee_cpf_field)).getEditText();
        mRole = ((TextInputLayout) view.findViewById(R.id.employee_role_field)).getEditText();
        mObs = ((TextInputLayout) view.findViewById(R.id.employee_obs_field)).getEditText();

        setEmptyFieldValidations(mName);
        setEmptyFieldValidations(mEmail);
        setEmptyFieldValidations(mCpf);
        setEmptyFieldValidations(mRole);
        setEmptyFieldValidations(mObs);
    }

    @Override
    public int getFragment() {
        return R.layout.fragment_employee_creation;
    }

    @Override
    public int getTitle() {
        return R.string.employee_creation_title;
    }
}
