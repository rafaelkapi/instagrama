package com.cactus.instagrama.main.home.presentation;

import com.cactus.instagrama.common.model.Feed;
import com.cactus.instagrama.common.presenter.Presenter;
import com.cactus.instagrama.main.home.datasource.HomeDataSource;
import com.cactus.instagrama.main.presentation.MainView;

import java.util.List;


public class HomePresenter implements Presenter<List<Feed>> {

  private final HomeDataSource dataSource;
  private MainView.HomeView view;

  public HomePresenter(HomeDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void setView(MainView.HomeView view) {
    this.view = view;
  }

  public void findFeed() {
    view.showProgressBar();
    dataSource.findFeed(this);
  }

  @Override
  public void onSuccess(List<Feed> response) {
    view.showFeed(response);
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
