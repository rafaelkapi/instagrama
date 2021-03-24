package com.cactus.instagrama.main.presentation;

import com.cactus.instagrama.common.model.Feed;
import com.cactus.instagrama.common.model.Post;
import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.view.View;

import java.util.List;


public interface MainView extends View {

  void scrollToolbarEnabled(boolean enabled);

  void showProfile(String user);

  void disposeProfileDetail();

  void logout();

  public interface ProfileView extends View {

    void showPhoto(String photo);

    void showData(String name, String following, String followers, String posts, boolean editProfile, boolean follow);

    void showPosts(List<Post> posts);

  }

  public interface HomeView extends View {

    void showFeed(List<Feed> response);

  }

  public interface SearchView {

    void showUsers(List<User> users);

  }
}
