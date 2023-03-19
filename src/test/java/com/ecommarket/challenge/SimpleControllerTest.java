package com.ecommarket.challenge;


import com.ecommarket.challenge.service.RateLimitServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"rate-limit.duration=1s", "rate-limit.permit=10"})
@AutoConfigureMockMvc
class SimpleControllerTest {

    private static final String URL = "/api/v1/greeting";

    @Autowired
    protected RateLimitServiceImpl service;

    @Autowired
    protected MockMvc mockMvc;


    @Test
    @DirtiesContext
    void dummyEndpoint() throws Exception {

        mockMvc.perform(get("/api/v1/greeting")
                        .header("X-Forwarded-For", "192.0.0.122"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[Success] 10 different ip make 10 requests async")
    @DirtiesContext
    public void test() {

        List<String> ips = IntStream.range(0, 10)
                .mapToObj(x -> format("11%d.0.0.10%d", x, x))
                .toList();

        List<Runnable> runnables = ips.stream()
                .map(ip -> {
                    RequestBuilder request = get(URL)
                            .header("X-Forwarded-For", ip);
                    return IntStream.range(0, 10)
                            .mapToObj(x -> (Runnable) () -> {
                                try {
                                    mockMvc.perform(request)
                                            .andExpect(status().isOk())
                                            .andDo(print());
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .toList();
                })
                .flatMap(Collection::stream)
                .toList();

        CompletableFuture<?>[] completableFutures = runnables.stream()
                .map(CompletableFuture::runAsync)
                .toArray(CompletableFuture<?>[]::new);

        CompletableFuture.allOf(completableFutures).join();
        Arrays.stream(completableFutures)
                .map(CompletableFuture::isDone)
                .forEach(Assertions::assertTrue);
    }


    @Test
    @DirtiesContext
    @DisplayName("[Success] 1 ip make 10 successively requests")
    public void testOneIpSuccess() throws Exception {

        RequestBuilder request = get(URL)
                .header("X-Forwarded-For", "119.0.0.111");

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    @DirtiesContext
    @DisplayName("[Fail] 1 ip make 11 successively requests")
    public void testOneIpFail() throws Exception {

        RequestBuilder request = get(URL)
                .header("X-Forwarded-For", "119.0.0.111");

        for (int i = 0; i < 11; i++) {
            if (i < 10) {
                mockMvc.perform(request)
                        .andExpect(status().isOk())
                        .andDo(print());
            }
            if (i == 10) {
                mockMvc.perform(request)
                        .andExpect(status().isBadGateway())
                        .andDo(print());
            }
        }
    }

    @Test
    @DirtiesContext
    @DisplayName("[Success] 1 ip make 11 successively requests")
    public void testOneIpWaitSuccess() throws Exception {
        RequestBuilder request = get(URL)
                .header("X-Forwarded-For", "119.0.0.111");

        for (int i = 0; i < 11; i++) {
            if (i < 10) {
                mockMvc.perform(request)
                        .andExpect(status().isOk())
                        .andDo(print());
            }
            if (i == 10) {
                sleep(1000);
                mockMvc.perform(request)
                        .andExpect(status().isOk())
                        .andDo(print());
            }
        }
    }
}
