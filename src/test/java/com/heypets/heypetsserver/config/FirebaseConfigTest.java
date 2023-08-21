package com.heypets.heypetsserver.config;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FirebaseConfigTest {
    @Value("${firebase.service.bucket.url}")
    private String bucket_url;
    @Test
    public void 파이어스토어연동(){
        Firestore firestore = FirestoreClient.getFirestore();
        System.out.println("Firestore 연동 테스트 성공");
    }

    @Test
    public void 파이어스토리지연동(){
        Bucket bucket = StorageClient.getInstance().bucket(bucket_url);
        System.out.println("Firestorage 연동 테스트 성공");
    }
}
