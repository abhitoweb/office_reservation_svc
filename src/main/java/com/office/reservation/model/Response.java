package com.office.reservation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    public String month;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public long revenue;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int unreservedCapacity;
}
