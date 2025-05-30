package com.office.reservation.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    public String month;
    public long revenue;
    public int unreservedCapacity;
}
