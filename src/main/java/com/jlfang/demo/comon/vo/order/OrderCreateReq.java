package com.jlfang.demo.comon.vo.order;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderCreateReq implements Serializable {

    List<String> origin;

    List<String> destination;
}
