package com.jlfang.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jlfang.demo.common.entity.TOrder;
import com.jlfang.demo.comon.vo.order.*;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface IOrderService extends IService<TOrder> {

    /**
     * place an order
     * @param req order create vo
     * @return
     */
    ResponseEntity<OrderCreateRes> createOrder(OrderCreateReq req);

    /**
     * take an order
     * @param id order id
     * @param req order take request body
     * @return
     */
    ResponseEntity<OrderTakeRes> takeOrder(Long id, OrderTakeReq req);

    /**
     * get orders by page
     * @param page current page
     * @param limit page size
     * @return
     */
    ResponseEntity<List<OrderView>> getOrdersPage(Integer page, Integer limit);



}
