package com.heypets.heypetsserver.config;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FirebaseConfigTest {
    @Test
    public void 파이어스토어연동(){
        Firestore firestore = FirestoreClient.getFirestore();
        System.out.println("Firestore 연동 테스트 성공");
    }
}
