package com.cactus.instagrama.main.home.datasource;

import com.cactus.instagrama.common.model.Feed;
import com.cactus.instagrama.common.presenter.Presenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class HomeFireDataSource implements HomeDataSource {

  @Override
  public void findFeed(Presenter<List<Feed>> presenter) {
    String uid = FirebaseAuth.getInstance().getUid();
    FirebaseFirestore.getInstance()
            .collection("feed")
            .document(uid)
            .collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
              List<Feed> feed = new ArrayList<>();
              List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
              for(DocumentSnapshot doc : documents) {
                Feed f = doc.toObject(Feed.class);
                feed.add(f);
              }
              presenter.onSuccess(feed);
            })
            .addOnFailureListener(e -> presenter.onError(e.getMessage()))
            .addOnCompleteListener(task -> presenter.onComplete());
  }

}
