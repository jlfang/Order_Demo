package com.jlfang.demo.comon.vo.google.map.distance;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DistanceMatrixResponse implements Serializable {
    @JSONField(name = "destination_addresses")
    private List<String> destinationAddresses;
    @JSONField(name = "origin_addresses")
    private List<String> originAddresses;
    private List<Row> rows;
    /**
     * OK indicates the response contains a valid result.
     * INVALID_REQUEST indicates that the provided request was invalid.
     * MAX_ELEMENTS_EXCEEDED indicates that the product of origins and destinations exceeds the per-query limit.
     * MAX_DIMENSIONS_EXCEEDED indicates that the number of origins or destinations exceeds the per-query limit.
     * OVER_DAILY_LIMIT indicates any of the following:
     * The API key is missing or invalid.
     * Billing has not been enabled on your account.
     * A self-imposed usage cap has been exceeded.
     * The provided method of payment is no longer valid (for example, a credit card has expired).
     * OVER_QUERY_LIMIT indicates the service has received too many requests from your application within the allowed time period.
     * REQUEST_DENIED indicates that the service denied use of the Distance Matrix service by your application.
     * UNKNOWN_ERROR indicates a Distance Matrix request could not be processed due to a server error. The request may succeed if you try again.
     */
    private String status;
    @JSONField(name = "error_message")
    private String errorMessage;

    @Data
    public static class Row {
        private List<Element> elements;

    }

    @Data
    public static class Element {
        private Distance distance;
        private Duration duration;
        private String status;

    }

    @Data
    public static class Distance {
        private String text;
        private Long value;

    }


    @Data
    public static class Duration {
        private String text;
        private Long value;

    }
}
