package kr.co.pulmuone.v1.order.registration.service;

import java.util.List;

import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;

public interface OrderBindBiz<T> {
    List<OrderBindDto> orderDataBind(T obj) throws Exception ;
}
