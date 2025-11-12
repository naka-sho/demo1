package com.example.demo;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    ErrorReportService errorReportService;

    @Test
    void invalidIdTriggersAsyncErrorReport() throws Exception {
        // Trigger index out of bounds
        mockMvc.perform(get("/id").param("id", "99"))
                .andExpect(status().is5xxServerError());

        // Verify async error reporting invoked within 2 seconds
        Mockito.verify(errorReportService, timeout(2000)).reportError(any(Exception.class));
    }
}

