package com.cactus.instagrama.login.presentation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.widget.EditText;

import com.cactus.instagrama.R;
import com.cactus.instagrama.common.component.LoadingButton;
import com.cactus.instagrama.common.view.AbstractActivity;
import com.cactus.instagrama.login.datasource.LoginDataSource;
import com.cactus.instagrama.login.datasource.LoginFireDataSource;
import com.cactus.instagrama.main.presentation.MainActivity;
import com.cactus.instagrama.register.presentation.RegisterActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;


import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class LoginActivity extends AbstractActivity implements LoginView  {

  @BindView(R.id.login_edit_text_email)
  EditText editTextEmail;
  @BindView(R.id.login_edit_text_password)
  EditText editTextPassword;
  @BindView(R.id.login_edit_text_email_input)
  TextInputLayout inputLayoutEmail;
  @BindView(R.id.login_edit_text_password_input) TextInputLayout inputLayoutPassword;
  @BindView(R.id.login_button_enter)
  LoadingButton buttonEnter;

  LoginPresenter presenter;

  public static void launch(Context context) {
    Intent intent = new Intent(context, LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }



  @Override
  protected void onInject() {



    setStatusBarDark();

    String user = FirebaseAuth.getInstance().getUid();
    if (user != null)
      onUserLogged();

    LoginDataSource dataSource = new LoginFireDataSource();
    presenter = new LoginPresenter(this, dataSource);
  }

  @Override
  public void showProgressBar() {
    buttonEnter.showProgress(true);
  }

  @Override
  public void hideProgressBar() {
    buttonEnter.showProgress(false);
  }

  @Override
  public void onFailureForm(String emailError, String passwordError) {
    if (emailError != null) {
      inputLayoutEmail.setError(emailError);
      editTextEmail.setBackground(findDrawable(R.drawable.edit_text_background_error));
    }
    if (passwordError != null) {
      inputLayoutPassword.setError(passwordError);
      editTextPassword.setBackground(findDrawable(R.drawable.edit_text_background_error));
    }
  }

  @Override
  public void onUserLogged() {
    MainActivity.launch(this, MainActivity.LOGIN_ACTIVITY);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  @OnClick(R.id.login_button_enter)
  public void onButtonEnterClick() {
    presenter.login(editTextEmail.getText().toString(), editTextPassword.getText().toString());
  }

  @OnClick(R.id.login_text_view_register)
  public void onTextViewRegisterClick() {
    RegisterActivity.launch(this);
  }

  @OnTextChanged({R.id.login_edit_text_email, R.id.login_edit_text_password})
  public void onTextChanged(CharSequence s) {
    buttonEnter.setEnabled(
            !editTextEmail.getText().toString().isEmpty() &&
                    !editTextPassword.getText().toString().isEmpty());

    if (s.hashCode() == editTextEmail.getText().hashCode()) {
      editTextEmail.setBackground(findDrawable(R.drawable.edit_text_background));
      inputLayoutEmail.setError(null);
      inputLayoutEmail.setErrorEnabled(false);
    } else if (s.hashCode() == editTextPassword.getText().hashCode()) {
      editTextPassword.setBackground(findDrawable(R.drawable.edit_text_background));
      inputLayoutPassword.setError(null);
      inputLayoutPassword.setErrorEnabled(false);
    }
  }

  @Override
  protected int getLayout() {
    return R.layout.activity_login;
  }



}
