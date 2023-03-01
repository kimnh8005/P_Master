package kr.co.pulmuone.v1.order.shipping.service;

import kr.co.pulmuone.v1.comm.mapper.order.shipping.OrderShippingMapper;
import kr.co.pulmuone.v1.order.shipping.service.OrderShippingService;
import kr.co.pulmuone.v1.user.buyer.dto.GetRecentlyShippingAddressListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderShippingBizImpl implements OrderShippingBiz {

    @Autowired
    private OrderShippingService orderShippingService;

    @Override
    public List<GetShippingAddressListResultVo> getRecentlyShippingAddressList(GetRecentlyShippingAddressListRequestDto getRecentlyShippingAddressListRequestDto) throws Exception {
        return orderShippingService.getRecentlyShippingAddressList(getRecentlyShippingAddressListRequestDto);
    }
}
