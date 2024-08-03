package com.jlfang.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jlfang.demo.common.entity.TOrder;
import com.jlfang.demo.common.enums.DistanceMatrixStatusEnum;
import com.jlfang.demo.common.enums.OrderStatusEnum;
import com.jlfang.demo.comon.vo.google.map.distance.DistanceMatrixResponse;
import com.jlfang.demo.comon.vo.order.*;
import com.jlfang.demo.mapper.TOrderMapper;
import com.jlfang.demo.service.IOrderService;
import com.jlfang.demo.service.IThirdPartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<TOrderMapper,TOrder> implements IOrderService {

    @Resource
    private IThirdPartyService thirdPartyService;

    @Override
    public ResponseEntity<OrderCreateRes> createOrder(OrderCreateReq req) {
        log.info("create order request: {}",req);
        String validationRes = validateOrderCreateReq(req);
        if(StringUtils.hasLength(validationRes)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(OrderCreateRes.builder().error(validationRes).build());
        }
        //calculate distance
        DistanceMatrixResponse distanceMatrix = thirdPartyService.getDistanceMatrix(req.getOrigin(), req.getDestination());
        if(distanceMatrix == null
                || CollectionUtils.isEmpty(distanceMatrix.getRows())
                || CollectionUtils.isEmpty(distanceMatrix.getRows().get(0).getElements())
                || !DistanceMatrixStatusEnum.OK.getValue().equals(distanceMatrix.getRows().get(0).getElements().get(0).getStatus())
                || Objects.isNull(distanceMatrix.getRows().get(0).getElements().get(0).getDistance())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(OrderCreateRes.builder().error("unable to calculate the distance").build());
        }
        DistanceMatrixResponse.Distance distance = distanceMatrix.getRows().get(0).getElements().get(0).getDistance();
        Long respDistance = distance.getValue();

        TOrder order = TOrder.builder()
                .oriLat(new BigDecimal(req.getOrigin().get(0)))
                .oriLng(new BigDecimal(req.getOrigin().get(1)))
                .destLat(new BigDecimal(req.getDestination().get(0)))
                .destLng(new BigDecimal(req.getDestination().get(1)))
                .distance(respDistance)
                .status(OrderStatusEnum.UNASSIGNED.getValue())
                .build();
        boolean saveSuccess = this.save(order);
        if(!saveSuccess){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(OrderCreateRes.builder().error("create order failed").build());
        }
        OrderCreateRes createRes = OrderCreateRes.builder()
                .id(order.getId())
                .distance(order.getDistance())
                .status(order.getStatus())
                .build();
        log.info("create order success, order id: {}",order.getId());
        return ResponseEntity.ok(createRes);
    }

    @Override
    public ResponseEntity<OrderTakeRes> takeOrder(Long id) {
        log.info("get order request: {}",id);
        if(id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(OrderTakeRes.builder().error("order id is null").build());
        }
        TOrder order = this.getById(id);
        if(null == order){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(OrderTakeRes.builder().error("order not found").build());
        }
        if(OrderStatusEnum.ASSIGNED.getValue().equals(order.getStatus())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(OrderTakeRes.builder().error("order has already been taken").build());
        }
        order.setStatus(OrderStatusEnum.ASSIGNED.getValue());
        boolean takeOrderSuccess = this.updateById(order);
        log.info("take order success: {}, order id: {}",takeOrderSuccess, order.getId());
        if(!takeOrderSuccess){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(OrderTakeRes.builder().error("take order failed, please try again").build());
        }
        OrderTakeRes orderTakeRes = OrderTakeRes.builder().status(order.getStatus()).build();
        return ResponseEntity.ok(orderTakeRes);
    }

    /**
     * query orders by page
     * @param page
     * @param limit
     * @return
     */
    @Override
    public ResponseEntity<List<OrderView>> getOrdersPage(Integer page, Integer limit) {
        Page<OrderView> pageCondition = new Page<>(page, limit);
        IPage<OrderView> pageList = baseMapper.queryOrderViewPageList(pageCondition);
        if(pageList != null && !CollectionUtils.isEmpty(pageList.getRecords())){
            return ResponseEntity.ok(pageList.getRecords());
        }
        return ResponseEntity.ok(new ArrayList<>());
    }


    /**
     * validate the request params
     * @param req
     * @return
     */
    private String validateOrderCreateReq(OrderCreateReq req){
        if(req == null){
            return "request is null";
        }

        List<String> origin = req.getOrigin();
        List<String> destination = req.getDestination();
        if(CollectionUtils.isEmpty(origin) || CollectionUtils.isEmpty(destination)){
            return "origin or destination is empty";
        }
        if(origin.size() != 2 || destination.size() != 2){
            return "origin or destination size is incorrect";
        }
        boolean isValidLat = isCoordinateValid(origin.get(0),origin.get(1));
        boolean isValidLng = isCoordinateValid(destination.get(0),destination.get(1));
        if(!isValidLat || !isValidLng){
            return "origin or destination is invalid";
        }
        return "";
    }

    /**
     * validate latitude and longitude
     * @param lat
     * @param lng
     * @return
     */
    public boolean isCoordinateValid(String lat, String lng) {
        try {
            double latitude = Double.parseDouble(lat.trim());
            double longitude = Double.parseDouble(lng.trim());

            return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            log.error("Params:{},{}, invalid coordinate: {}",lat,lng, e.getMessage());
            return false;
        }
    }
}
