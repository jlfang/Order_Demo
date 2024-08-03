package com.jlfang.demo.comon.vo.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRes implements Serializable {

    private Long id;

    private Long distance;

    private String status;

    private String error;
}
