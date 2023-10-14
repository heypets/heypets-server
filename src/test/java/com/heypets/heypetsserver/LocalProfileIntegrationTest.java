package com.heypets.heypetsserver;

import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class LocalProfileIntegrationTest {

    @Value("${management.datadog.metrics.export.enabled}")
    private boolean datadogEnabled;

    @Test
    public void testLocalProfileConfiguration() {
        // 가져온 설정 값 확인
        assert datadogEnabled == false;
    }
}
