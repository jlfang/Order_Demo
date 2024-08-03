package com.jlfang.demo.service;

import com.jlfang.demo.comon.vo.google.map.distance.DistanceMatrixResponse;

import java.util.List;

public interface IThirdPartyService {

    DistanceMatrixResponse getDistanceMatrix(List<String> origin, List<String> destination);
}
