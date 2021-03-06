package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.buonomo.cfpt.foodtrucktracker.Models.Owner;
import com.buonomo.cfpt.foodtrucktracker.Outils.OwnerService;
import com.buonomo.cfpt.foodtrucktracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccount extends AppCompatActivity implements OwnerService.Callbacks {

    @BindView(R.id.textfield_create_name)
    TextView name;
    @BindView(R.id.textfield_create_firstname)
    TextView firstname;
    @BindView(R.id.textfield_create_email)
    TextView email;
    @BindView(R.id.textfield_create_password)
    TextView password;
    @BindView(R.id.textfield_create_password_confirmation)
    TextView confirmation;
    @BindView(R.id.textfield_create_username)
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void registerAccount(View view) {
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name.getText())){
            cancel = true;
            name.setError(getString(R.string.error_field_required));
            focusView = name;
        }
        if (TextUtils.isEmpty(firstname.getText())){
            cancel = true;
            focusView = firstname;
            firstname.setError(getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(username.getText())){
            cancel = true;
            focusView = username;
            username.setError(getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(email.getText())){
            cancel = true;
            focusView = email;
            email.setError(getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(password.getText())){
            cancel = true;
            focusView = password;
            password.setError(getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(confirmation.getText())){
            cancel = true;
            focusView = confirmation;
            confirmation.setError(getString(R.string.error_field_required));
        }

        if (cancel){
            focusView.requestFocus();
        }else{
            if (!password.getText().equals(confirmation.getText())){
                focusView = confirmation;
                confirmation.setError("Les mots de passe ne correspondent pas");
                focusView.requestFocus();
            }else {
                this.executeAccountCreation(name.getText().toString(), firstname.getText().toString(), username.getText().toString(), email.getText().toString(), password.getText().toString());
            }
        }
    }

    public void executeAccountCreation(String name, String firstname, String username, String email, String password) {
        OwnerService.newOwner(name, firstname, username, email, password, this);
    }

    @Override
    public void onResponse() {

    }

    @Override
    public void onFailure() {

    }
}
