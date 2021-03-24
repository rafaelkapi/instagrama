package com.cactus.instagrama.login.datasource;


import com.cactus.instagrama.common.presenter.Presenter;

public interface LoginDataSource {

  void login(String email, String password, Presenter presenter);

}
