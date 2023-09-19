package com.heypets.heypetsserver.domain.user.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.heypets.heypetsserver.core.util.FirebaseDocumentIdGenUtil;
import com.heypets.heypetsserver.domain.user.entity.User;
import com.heypets.heypetsserver.domain.user.vo.SocialType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    public Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId) throws Exception{
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionRef = firestore.collection(User.TABLENAME);

        Query query = collectionRef
                .whereEqualTo("socialType", socialType)
                .whereEqualTo("socialId",socialId);
        return getUser(query);
    }

    public Optional<User> save(User user) throws Exception{
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionRef = firestore.collection(User.TABLENAME);
        String uuid = FirebaseDocumentIdGenUtil.generateUUID(collectionRef);
        user.set_id(uuid);
        ApiFuture<WriteResult> apiFuture = collectionRef.document(uuid).set(user);

        return Optional.of(user);
    }

    public Optional<User> findByEmail(String email) throws Exception{
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionRef = firestore.collection(User.TABLENAME);

        Query query = collectionRef
                .whereEqualTo("email", email);
        return getUser(query);
    }

    public Optional<User> findByRefreshToken(String refreshToken) throws Exception{
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionRef = firestore.collection(User.TABLENAME);

        Query query = collectionRef
                .whereEqualTo("refreshToken", refreshToken);
        return getUser(query);
    }

    public Optional<User> update(User user) throws Exception{
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionRef = firestore.collection(User.TABLENAME);
        ApiFuture<WriteResult> apiFuture = collectionRef.document(user.get_id()).set(user);

        return Optional.of(user);
    }

    public DocumentReference getUserRef(User user){
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference userDocumentRef = firestore.collection(User.TABLENAME).document(user.get_id());
        return userDocumentRef;
    }

    private Optional<User> getUser(Query query) throws InterruptedException, java.util.concurrent.ExecutionException {
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documentList = querySnapshot.get().getDocuments();

        if(!documentList.isEmpty()){
            QueryDocumentSnapshot document = documentList.get(0);
            User user = document.toObject(User.class);
            return Optional.of(user);
        }
        else{
            return Optional.ofNullable(null);
        }
    }


}

