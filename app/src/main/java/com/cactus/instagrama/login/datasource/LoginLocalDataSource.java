package com.cactus.instagrama.login.datasource;


import com.cactus.instagrama.common.model.Database;
import com.cactus.instagrama.common.model.UserAuth;
import com.cactus.instagrama.common.presenter.Presenter;

public class LoginLocalDataSource implements LoginDataSource {

  @Override
  public void login(String email, String password, Presenter presenter) {
    Database.getInstance().login(email, password)
            .addOnSuccessListener((Database.OnSuccessListener<UserAuth>) response -> presenter.onSuccess(response))
            .addOnFailureListener(e -> presenter.onError(e.getMessage()))
            .addOnCompleteListener(() -> presenter.onComplete());
  }

}
