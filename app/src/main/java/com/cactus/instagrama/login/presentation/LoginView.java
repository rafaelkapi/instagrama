package com.cactus.instagrama.login.presentation;


import com.cactus.instagrama.common.view.View;

public interface LoginView extends View {

  void onFailureForm(String emailError, String passwordError);

  void onUserLogged();

}
