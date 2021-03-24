package com.cactus.instagrama.main.profile.presentation;

import com.cactus.instagrama.common.model.Post;
import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.model.UserProfile;
import com.cactus.instagrama.common.presenter.Presenter;
import com.cactus.instagrama.main.presentation.MainView;
import com.cactus.instagrama.main.profile.datasource.ProfileDataSource;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class ProfilePresenter implements Presenter<UserProfile> {

  private final ProfileDataSource dataSource;
  private final String user;
  private MainView.ProfileView view;

  public ProfilePresenter(ProfileDataSource dataSource) {
    this(dataSource, FirebaseAuth.getInstance().getUid());
  }

  public ProfilePresenter(ProfileDataSource dataSource, String user) {
    this.dataSource = dataSource;
    this.user = user;
  }

  public void setView(MainView.ProfileView view) {
    this.view = view;
  }

  public String getUser() {
    return user;
  }

  public void findUser() {
    view.showProgressBar();
    dataSource.findUser(user, this);
  }

  public void follow(boolean follow) {
    if (follow)
      dataSource.follow(user);
    else
      dataSource.unfollow(user);
  }

  @Override
  public void onSuccess(UserProfile userProfile) {
    User user = userProfile.getUser();
    List<Post> posts = userProfile.getPosts();

    boolean editProfile = user.getUuid().equals(FirebaseAuth.getInstance().getUid());

    view.showData(
            user.getName(),
            String.valueOf(user.getFollowing()),
            String.valueOf(user.getFollowers()),
            String.valueOf(user.getPosts()),
            editProfile,
            userProfile.isFollowing());

    view.showPosts(posts);

    if (user.getPhotoUrl() != null)
      view.showPhoto(user.getPhotoUrl());
  }

  @Override
  public void onError(String message) {
    // TODO: 25/04/19
  }

  @Override
  public void onComplete() {
    view.hideProgressBar();
  }


}
