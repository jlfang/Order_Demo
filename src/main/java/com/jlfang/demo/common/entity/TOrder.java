package com.jlfang.demo.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Builder;
import lombok.Data;

/**
 * t_order
 * @author 
 */
@Data
@Builder
public class TOrder implements Serializable {
    /**
     * primary key
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * origin latitude
     */
    private BigDecimal oriLat;

    /**
     * origin longitude
     */
    private BigDecimal oriLng;

    /**
     * destination latitude
     */
    private BigDecimal destLat;

    /**
     * destination longitude
     */
    private BigDecimal destLng;

    /**
     * distance
     */
    private Long distance;

    /**
     * order status (UNASSIGNED,ASSIGNED)
     */
    private String status;

    /**
     * creator
     */
    private String creator;

    /**
     * create time
     */
    private LocalDateTime createTime;

    /**
     * modifier
     */
    private String modifier;

    /**
     * modify time
     */
    private LocalDateTime modifyTime;

    /**
     * version number
     */
    @Version
    private Integer version;

    /**
     * order deleted (0-no 1-yes)
     */
    private Boolean deleted;


    private static final long serialVersionUID = 1L;
}