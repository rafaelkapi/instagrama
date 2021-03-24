package com.cactus.instagrama.login.presentation;

import com.cactus.instagrama.R;
import com.cactus.instagrama.common.presenter.Presenter;
import com.cactus.instagrama.common.util.Strings;
import com.cactus.instagrama.login.datasource.LoginDataSource;
import com.google.firebase.auth.FirebaseUser;




class LoginPresenter implements Presenter<FirebaseUser> {

  private final LoginView view;
  private final LoginDataSource dataSource;

  LoginPresenter(LoginView view, LoginDataSource dataSource) {
    this.view = view;
    this.dataSource = dataSource;
  }

  void login(String email, String password) {
    if (!Strings.emailValid(email)) {
      view.onFailureForm(view.getContext().getString(R.string.invalid_email), null);
      return;
    }

    view.showProgressBar();
    dataSource.login(email, password, this);
  }

  @Override
  public void onSuccess(FirebaseUser userAuth) {
    view.onUserLogged();
  }

  @Override
  public void onError(String message) {
    view.onFailureForm(null, message);
  }

  @Override
  public void onComplete() {
    view.hideProgressBar();
  }

}
