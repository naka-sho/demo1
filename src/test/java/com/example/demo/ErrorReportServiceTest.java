package com.example.demo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

public class ErrorReportServiceTest {

    @Test
    void reportErrorPostsQueryWithStackTrace() {
        ErrorReportService service = new ErrorReportService(new RestTemplateBuilder());
        MockRestServiceServer server = MockRestServiceServer.createServer(service.getRestTemplate());

        server.expect(MockRestRequestMatchers.requestTo("http://localhost:8080/mcp/demo"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().string(Matchers.containsString("query=")))
                .andExpect(MockRestRequestMatchers.content().string(Matchers.containsString("RuntimeException")))
                .andExpect(MockRestRequestMatchers.content().string(Matchers.containsString("boom")))
                .andRespond(MockRestResponseCreators.withSuccess());

        service.reportError(new RuntimeException("boom")).join();

        server.verify();
    }
}
