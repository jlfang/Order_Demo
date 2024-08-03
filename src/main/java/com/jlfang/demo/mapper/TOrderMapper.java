package com.jlfang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jlfang.demo.common.entity.TOrder;
import com.jlfang.demo.comon.vo.order.OrderView;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TOrderMapper extends BaseMapper<TOrder> {

    Page<OrderView> queryOrderViewPageList(Page<OrderView> page);

    Long queryMinUnassignedOrderId();

    Long queryMaxAssignedOrderId();
}