package com.cactus.instagrama.main.profile.datasource;


import com.cactus.instagrama.common.model.UserProfile;
import com.cactus.instagrama.common.presenter.Presenter;

public interface ProfileDataSource {

  void findUser(String user, Presenter<UserProfile> presenter);

  void follow(String user);

  void unfollow(String user);

}
