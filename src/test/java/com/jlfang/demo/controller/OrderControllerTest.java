package com.jlfang.demo.controller;

import com.alibaba.fastjson.JSON;
import com.jlfang.demo.common.enums.OrderStatusEnum;
import com.jlfang.demo.comon.vo.order.*;
import com.jlfang.demo.mapper.TOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class OrderControllerTest {

    @Resource
    private TestRestTemplate testRestTemplate;
    @Resource
    private TOrderMapper orderMapper;

    private String baseUrl = "http://localhost:8080/orders";

    @BeforeEach
    void setUp() {

    }

    /**
     * create order
     * order create success
     *
     * @throws Exception
     */
    @Order(1)
    @RepeatedTest(10)
    void testCreateOrderSuccess() throws Exception {
        // Given
        OrderCreateReq orderCreateReq = new OrderCreateReq();
        orderCreateReq.setOrigin(Arrays.asList("37.7663444", "-122.4412006"));
        orderCreateReq.setDestination(Arrays.asList("37.7681296", "-122.4379126"));
        log.info("order create success request: {}", JSON.toJSONString(orderCreateReq));
        // When & Then
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(orderCreateReq), headers);
        ResponseEntity<OrderCreateRes> response =
                testRestTemplate.exchange(baseUrl, HttpMethod.POST, entity, OrderCreateRes.class);
        log.info("success response: {}", response);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertNotNull(response.getBody().getDistance());
        Assertions.assertEquals(response.getBody().getStatus(), OrderStatusEnum.UNASSIGNED.getValue());
    }

    /**
     * create order
     * create order without origins
     *
     * @throws Exception
     */
    @Test
    void testCreateOrderFail1() throws Exception {
        // Given
        OrderCreateReq orderCreateReq = new OrderCreateReq();
        orderCreateReq.setDestination(Arrays.asList("37.7681296", "-122.4379126"));
        log.info("order create fail1 request: {}", orderCreateReq);
        // When & Then
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(orderCreateReq), headers);
        ResponseEntity<OrderCreateRes> response =
                testRestTemplate.exchange(baseUrl, HttpMethod.POST, entity, OrderCreateRes.class);
        log.info("order create fail1 response: {}", response);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody(), "response body should not be null");
        Assertions.assertNotNull(response.getBody().getError(), "response body should contains error message");
    }

    /**
     * create order
     * create order without destinations
     *
     * @throws Exception
     */
    @Test
    void testCreateOrderFail2() throws Exception {
        // Given
        OrderCreateReq orderCreateReq = new OrderCreateReq();
        orderCreateReq.setOrigin(Arrays.asList("37.7681296", "-122.4379126"));
        log.info("order create fail2 request: {}", orderCreateReq);
        // When & Then
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(orderCreateReq), headers);
        ResponseEntity<OrderCreateRes> response =
                testRestTemplate.exchange(baseUrl, HttpMethod.POST, entity, OrderCreateRes.class);
        log.info("order create fail2 response: {}", response);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody(), "response body should not be null");
        Assertions.assertNotNull(response.getBody().getError(), "response body should contains error message");
    }

    /**
     * create order
     * create order with wrong number of origins
     *
     * @throws Exception
     */
    @Test
    void testCreateOrderFail3() throws Exception {
        // Given
        OrderCreateReq orderCreateReq = new OrderCreateReq();
        orderCreateReq.setOrigin(Arrays.asList("37.7681296", "-122.4379126", "-122.4379126"));
        orderCreateReq.setDestination(Arrays.asList("37.7681296", "-122.4379126"));
        log.info("order create fail3 request: {}", orderCreateReq);
        // When & Then
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(orderCreateReq), headers);
        ResponseEntity<OrderCreateRes> response =
                testRestTemplate.exchange(baseUrl, HttpMethod.POST, entity, OrderCreateRes.class);
        log.info("order create fail3 response: {}", response);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody(), "response body should not be null");
        Assertions.assertNotNull(response.getBody().getError(), "response body should contains error message");
    }

    /**
     * create order
     * create order with wrong format of origins
     *
     * @throws Exception
     */
    @Test
    void testCreateOrderFail4() throws Exception {
        // Given
        OrderCreateReq orderCreateReq = new OrderCreateReq();
        orderCreateReq.setOrigin(Arrays.asList("this is a latitude", "this is a longitude"));
        orderCreateReq.setDestination(Arrays.asList("37.7681296", "-122.4379126"));
        log.info("order create fail4 request: {}", orderCreateReq);
        // When & Then
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(orderCreateReq), headers);
        ResponseEntity<OrderCreateRes> response =
                testRestTemplate.exchange(baseUrl, HttpMethod.POST, entity, OrderCreateRes.class);
        log.info("order create fail4 response: {}", response);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody(), "response body should not be null");
        Assertions.assertNotNull(response.getBody().getError(), "response body should contains error message");
    }

    /**
     * take order
     * take order success
     *
     * @throws Exception
     */
    @Test
    void testTakeOrderSuccess() throws Exception {
        // Given
        Long orderId = orderMapper.queryMinUnassignedOrderId();
        if (null == orderId) {
            log.error("not enough unassigned orders, create more");
            return;
        }
        OrderTakeReq orderTakeReq = new OrderTakeReq();
        orderTakeReq.setStatus("taken");

        log.info("order take success request: {}", orderId);
        // When & Then
        // TestRestTemplate does not support PATCH, so here we use HttpClient instead
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(baseUrl + "/" + orderId);
        httpPatch.setHeader("Content-Type", "application/json");
        httpPatch.setEntity(new StringEntity(JSON.toJSONString(orderTakeReq)));
        try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {
            log.info("order take success response: {}", response);
            int statusCode = response.getStatusLine().getStatusCode();
            String respStr = EntityUtils.toString(response.getEntity());
            OrderTakeRes orderTakeRes = JSON.parseObject(respStr, OrderTakeRes.class);
            Assertions.assertEquals(HttpStatus.OK.value(), statusCode);
            Assertions.assertNotNull(orderTakeRes, "response body should not be null");
            Assertions.assertEquals("SUCCESS", orderTakeRes.getStatus());
        }
    }


    /**
     * take order
     * take order race condition for 20 requests
     *
     * @throws Exception
     */
    @Test
    void testTakeOrderRaceCondition() throws Exception {
        // Given
        Long orderId = orderMapper.queryMinUnassignedOrderId();
        log.info("order take race condition request: {}", orderId);
        String url = baseUrl + "/" + orderId;
        int threadNum = 20;
        OrderTakeReq orderTakeReq = new OrderTakeReq();
        orderTakeReq.setStatus("taken");
        // When & Then
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        Callable<CloseableHttpResponse> task = () -> {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPatch httpPatch = new HttpPatch(url);
            httpPatch.setHeader("Content-Type", "application/json");
            httpPatch.setEntity(new StringEntity(JSON.toJSONString(orderTakeReq)));
            return httpClient.execute(httpPatch);
        };
        Future<CloseableHttpResponse>[] futures = new Future[threadNum];
        for (int i = 0; i < threadNum; i++) {
            futures[i] = executor.submit(task);
        }
        int i = 1;
        int successCnt = 0;
        for (Future<CloseableHttpResponse> future : futures) {
            CloseableHttpResponse response = future.get();
            int statusCode = response.getStatusLine().getStatusCode();
            String respStr = EntityUtils.toString(response.getEntity());
            log.info("request:{},response:{} ", i++, respStr);
            OrderTakeRes orderTakeRes = JSON.parseObject(respStr, OrderTakeRes.class);
            if (HttpStatus.OK.value() == statusCode
                    && "SUCCESS".equals(orderTakeRes.getStatus())) {
                successCnt++;
            }
        }
        Assertions.assertEquals(1, successCnt);
    }


    /**
     * take order
     * take order fail for order has already been taken
     *
     * @throws Exception
     */
    @Test
    void testTakeOrderFail1() throws Exception {
        // Given
        Long orderId = orderMapper.queryMaxAssignedOrderId();
        if (null == orderId) {
            log.error("not enough assigned orders, create more");
            return;
        }
        OrderTakeReq orderTakeReq = new OrderTakeReq();
        orderTakeReq.setStatus("taken");
        log.info("order take fail1 request: {}", orderId);
        // When & Then
        // TestRestTemplate does not support PATCH, so here we use HttpClient instead
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(baseUrl + "/" + orderId);
        httpPatch.setHeader("Content-Type", "application/json");
        httpPatch.setEntity(new StringEntity(JSON.toJSONString(orderTakeReq)));
        CloseableHttpResponse response = httpClient.execute(httpPatch);
        int statusCode = response.getStatusLine().getStatusCode();
        String respStr = EntityUtils.toString(response.getEntity());
        log.info("order take fail1 response: {}", respStr);
        OrderTakeRes orderTakeRes = JSON.parseObject(respStr, OrderTakeRes.class);
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), statusCode);
        Assertions.assertNotNull(orderTakeRes, "response body should not be null");
        Assertions.assertNotNull(orderTakeRes.getError(), "response error message should not be null");
    }


    /**
     * take order
     * take order fail for order not exists
     *
     * @throws Exception
     */
    @Test
    void testTakeOrderFail2() throws Exception {
        // Given
        Long orderId = Long.MAX_VALUE;
        OrderTakeReq orderTakeReq = new OrderTakeReq();
        orderTakeReq.setStatus("taken");
        log.info("order take fail2 request: {}", orderId);
        // When & Then
        // TestRestTemplate does not support PATCH, so here we use HttpClient instead
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(baseUrl + "/" + orderId);
        httpPatch.setHeader("Content-Type", "application/json");
        httpPatch.setEntity(new StringEntity(JSON.toJSONString(orderTakeReq)));
        try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {
            log.info("order take fail2 response: {}", response);
            int statusCode = response.getStatusLine().getStatusCode();
            String respStr = EntityUtils.toString(response.getEntity());
            OrderTakeRes orderTakeRes = JSON.parseObject(respStr, OrderTakeRes.class);
            Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), statusCode);
            Assertions.assertNotNull(orderTakeRes, "response body should not be null");
            Assertions.assertNotNull(orderTakeRes.getError(), "response error message should not be null");
        }
    }

    /**
     * take order
     * take order fail for invalid request body
     *
     * @throws Exception
     */
    @Test
    void testTakeOrderFail3() throws Exception {
        // Given
        Long orderId = Long.MAX_VALUE;

        log.info("order take fail3 request: {}", orderId);
        // When & Then
        // TestRestTemplate does not support PATCH, so here we use HttpClient instead
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(baseUrl + "/" + orderId);
        httpPatch.setHeader("Content-Type", "application/json");
        try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {
            log.info("order take fail3 response: {}", response);
            int statusCode = response.getStatusLine().getStatusCode();
            String respStr = EntityUtils.toString(response.getEntity());
            OrderTakeRes orderTakeRes = JSON.parseObject(respStr, OrderTakeRes.class);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), statusCode);
            Assertions.assertNotNull(orderTakeRes, "response body should not be null");
            Assertions.assertNotNull(orderTakeRes.getError(), "response error message should not be null");
        }
    }

    /**
     * get orders
     * get orders success
     *
     * @throws Exception
     */

    @Test
    void testGetOrderSuccess1() throws Exception {

        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("page", "1")
                        .queryParam("limit", "10")
                        .build();
        // When & Then
        ResponseEntity<List<OrderView>> response =
                testRestTemplate
                        .exchange(uriComponents.toString(), HttpMethod.GET,
                                null, new ParameterizedTypeReference<List<OrderView>>() {
                                });
        log.info("get order success1 response: {}", response);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
        response.getBody().forEach(orderView -> {
            Assertions.assertNotNull(orderView.getId());
            Assertions.assertNotNull(orderView.getStatus());
            Assertions.assertNotNull(orderView.getDistance());
        });
    }

    /**
     * get order
     * get orders success with empty array
     *
     * @throws Exception
     */
    @Test
    void testGetOrderSuccess2() throws Exception {

        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("page", Integer.MAX_VALUE)
                        .queryParam("limit", "10")
                        .build();
        // When & Then
        ResponseEntity<List<OrderView>> response =
                testRestTemplate
                        .exchange(uriComponents.toString(), HttpMethod.GET,
                                null, new ParameterizedTypeReference<List<OrderView>>() {
                                });
        log.info("get order success2 response: {}", response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isEmpty());
    }

    /**
     * get order
     * get orders fail for invalid inputs
     *
     * @throws Exception
     */
    @Test
    void testGetOrderFail1() throws Exception {

        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("page", "abc")
                        .queryParam("limit", "10")
                        .build();
        // When & Then
        ResponseEntity<String> response =
                testRestTemplate
                        .exchange(uriComponents.toString(), HttpMethod.GET,
                                null, String.class);
        log.info("success response: {}", response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * test create take and get orders
     * @throws Exception
     */
    @Test
    void testCreateTakeAndGetOrder() throws Exception{
        testGetOrderSuccess2();
        log.info("get orders before create successfully");

        OrderCreateReq orderCreateReq = new OrderCreateReq();
        orderCreateReq.setOrigin(Arrays.asList("37.7663444", "-122.4412006"));
        orderCreateReq.setDestination(Arrays.asList("37.7681296", "-122.4379126"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(orderCreateReq), headers);
        log.info("order create start: {}",orderCreateReq);
        ResponseEntity<OrderCreateRes> createOrderResp =
                testRestTemplate.exchange(baseUrl, HttpMethod.POST, entity, OrderCreateRes.class);
        Assertions.assertEquals(HttpStatus.OK, createOrderResp.getStatusCode());
        Assertions.assertNotNull(createOrderResp.getBody());
        Assertions.assertNotNull(createOrderResp.getBody().getDistance());
        Assertions.assertEquals(createOrderResp.getBody().getStatus(), OrderStatusEnum.UNASSIGNED.getValue());
        Long id = createOrderResp.getBody().getId();
        log.info("order create successfully, order id : {}",id);

        testGetOrderSuccess1();
        log.info("get orders after create successfully");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        OrderTakeReq orderTakeReq = new OrderTakeReq();
        orderTakeReq.setStatus("taken");
        HttpPatch httpPatch = new HttpPatch(baseUrl + "/" + id);
        httpPatch.setHeader("Content-Type", "application/json");
        httpPatch.setEntity(new StringEntity(JSON.toJSONString(orderTakeReq)));
        try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {
            log.info("order take success response: {}", response);
            int statusCode = response.getStatusLine().getStatusCode();
            String respStr = EntityUtils.toString(response.getEntity());
            OrderTakeRes orderTakeRes = JSON.parseObject(respStr, OrderTakeRes.class);
            Assertions.assertEquals(HttpStatus.OK.value(), statusCode);
            Assertions.assertNotNull(orderTakeRes, "response body should not be null");
            Assertions.assertEquals("SUCCESS", orderTakeRes.getStatus());
            log.info("order take successfully: {}", orderTakeRes);
        }

        testGetOrderSuccess1();
        log.info("get orders after order been taken successfully");
    }

}


