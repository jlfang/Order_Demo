package com.jlfang.demo.controller;


import com.jlfang.demo.comon.vo.order.*;
import com.jlfang.demo.service.IOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Resource
    private IOrderService orderService;
    @PostMapping
    public ResponseEntity<OrderCreateRes> createOrder(@RequestBody OrderCreateReq createReq) {
        return orderService.createOrder(createReq);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderTakeRes> takeOrder(@PathVariable("id") Long id, @RequestBody OrderTakeReq req) {
        return orderService.takeOrder(id,req);
    }

    @GetMapping
    public ResponseEntity<List<OrderView>> getOrdersPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                         @RequestParam("limit") Integer limit) {
        return orderService.getOrdersPage(page, limit);
    }
}
