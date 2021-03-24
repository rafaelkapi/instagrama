package com.cactus.instagrama.main.search.datasource;


import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.presenter.Presenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchFireDataSource implements SearchDataSource {



    @Override
    public void findUser(String query, Presenter<List<User>> presenter) {
        FirebaseFirestore.getInstance().collection("user")
                .whereEqualTo("name", query)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = new ArrayList<>();
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot doc : documents) {
                        User user = doc.toObject(User.class);
                        if (!user.getUuid().equals(FirebaseAuth.getInstance().getUid()))
                            users.add(user);
                    }
                    presenter.onSuccess(users);
                })
                .addOnFailureListener(e -> presenter.onError(e.getMessage()))
                .addOnCompleteListener(task -> presenter.onComplete());
    }
}
