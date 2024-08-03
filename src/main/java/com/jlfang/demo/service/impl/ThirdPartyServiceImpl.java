package com.jlfang.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.jlfang.demo.common.config.GoogleConfig;
import com.jlfang.demo.common.enums.DistanceMatrixStatusEnum;
import com.jlfang.demo.comon.vo.google.map.distance.DistanceMatrixResponse;
import com.jlfang.demo.service.IThirdPartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ThirdPartyServiceImpl implements IThirdPartyService {

    @Resource
    private GoogleConfig googleConfig;

    @Override
    public DistanceMatrixResponse getDistanceMatrix(List<String> origin, List<String> destination) {

        String url = googleConfig.getBaseUrl()+"distancematrix/json"
                + "?origins=" + origin.get(0)+","+origin.get(1)
                + "&destinations=" + destination.get(0)+","+destination.get(1)
                + "&key=" + googleConfig.getKey();

        RestTemplate restTemplate = new RestTemplate();
        DistanceMatrixResponse response;
        try{
            log.info("request for google maps distance matrix: {}",url);
            String respStr = restTemplate.getForObject(url, String.class);
            log.info("response from google maps distance matrix: {}", respStr);
            response = JSON.parseObject(respStr, DistanceMatrixResponse.class);
        }catch (Exception e){
            log.error("getDistanceMatrix error:{}", e.getMessage());
            response = new DistanceMatrixResponse();
            response.setStatus(DistanceMatrixStatusEnum.APPLICATION_INNER_ERROR.getValue());
            return response;
        }
        return response;
    }
}



