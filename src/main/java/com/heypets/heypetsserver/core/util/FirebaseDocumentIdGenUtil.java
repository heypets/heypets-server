package com.heypets.heypetsserver.core.util;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.heypets.heypetsserver.domain.user.entity.User;

import java.util.UUID;

public class FirebaseDocumentIdGenUtil {

    public static String generateUUID(CollectionReference collectionRef) throws Exception{

        while (true){
            String documentId = UUID.randomUUID().toString();
            DocumentReference documentReference = collectionRef.document(documentId);
            ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
            DocumentSnapshot documentSnapshot = apiFuture.get();

            if (!documentSnapshot.exists()){
                return documentId;
            }
        }
    }
}


