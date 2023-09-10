package com.heypets.heypetsserver;

import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("production")
public class ProductionProfileIntegrationTest {

    @Value("${management.datadog.metrics.export.apiKey}")
    private String productionApiKey;

    @Test
    public void testProductionProfileConfiguration() {
        // 가져온 설정 값 확인
        assert productionApiKey.equals("111");
    }
}
