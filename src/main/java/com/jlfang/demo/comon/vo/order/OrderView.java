package com.jlfang.demo.comon.vo.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderView implements Serializable {

    private Long id;

    private Integer distance;

    private String status;
}
