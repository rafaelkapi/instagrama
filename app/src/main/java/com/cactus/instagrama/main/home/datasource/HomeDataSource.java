package com.cactus.instagrama.main.home.datasource;


import com.cactus.instagrama.common.model.Feed;
import com.cactus.instagrama.common.presenter.Presenter;

import java.util.List;

public interface HomeDataSource {

  void findFeed(Presenter<List<Feed>> presenter);

}
