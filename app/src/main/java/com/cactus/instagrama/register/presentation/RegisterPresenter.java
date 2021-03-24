package com.cactus.instagrama.register.presentation;

import android.net.Uri;

import com.cactus.instagrama.R;
import com.cactus.instagrama.common.presenter.Presenter;
import com.cactus.instagrama.common.util.Strings;
import com.cactus.instagrama.register.datasource.RegisterDataSource;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPresenter implements Presenter<FirebaseUser> {

  private RegisterView registerView;
  private RegisterView.EmailView emailView;
  private RegisterView.NamePasswordView namePasswordView;
  private RegisterView.PhotoView photoView;

  private final RegisterDataSource dataSource;

  private String email;
  private String name;
  private Uri uri;

  public RegisterPresenter(RegisterDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void setRegisterView(RegisterView registerView) {
    this.registerView = registerView;
  }

  public void setEmailView(RegisterView.EmailView emailView) {
    this.emailView = emailView;
  }

  public void setNamePasswordView(RegisterView.NamePasswordView namePasswordView) {
    this.namePasswordView = namePasswordView;
  }

  public void setPhotoView(RegisterView.PhotoView photoView) {
    this.photoView = photoView;
  }

  public void setEmail(String email) {
    if (!Strings.emailValid(email)) {
      emailView.onFailureForm(emailView.getContext().getString(R.string.invalid_email));
      return;
    }

    this.email = email;
    registerView.showNextView(RegisterSteps.NAME_PASSWORD);
  }

  public void setNameAndPassword(String name, String password, String confirmPassword) {
    if (!password.equals(confirmPassword)) {
      namePasswordView.onFailureForm(null, namePasswordView.getContext().getString(R.string.password_not_equal));
      return;
    }

    this.name = name;

    namePasswordView.showProgressBar();
    dataSource.createUser(name.toLowerCase(), email, password, this);
  }

  public String getName() {
    return name;
  }

  public void setUri(Uri uri) {
    this.uri = uri;
    if (photoView != null) {
      photoView.onImageCropped(uri);
      photoView.showProgressBar();

      dataSource.addPhoto(uri, new UpdatePhotoCallback());
    }
  }

  public void showPhotoView() {
    registerView.showNextView(RegisterSteps.PHOTO);
  }

  public void showCamera() {
    registerView.showCamera();
  }

  public void showGallery() {
    registerView.showGallery();
  }

  public void jumpRegistration() {
    registerView.onUserCreated();
  }

  @Override
  public void onSuccess(FirebaseUser response) {
    registerView.showNextView(RegisterSteps.WELCOME);
  }

  @Override
  public void onError(String message) {
    namePasswordView.onFailureCreateUser(message);
  }

  @Override
  public void onComplete() {
    namePasswordView.hideProgressBar();
  }

  private class UpdatePhotoCallback implements Presenter<Boolean> {

    @Override
    public void onSuccess(Boolean response) {
      registerView.onUserCreated();
    }

    @Override
    public void onError(String message) {
    }

    @Override
    public void onComplete() {
    }
  }

}
