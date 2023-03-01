package kr.co.pulmuone.v1.order.shipping.service;

import kr.co.pulmuone.v1.user.buyer.dto.GetRecentlyShippingAddressListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;

import java.util.List;

public interface OrderShippingBiz {

    /**
     * 최근 배송지 목록
     *
     * @param
     * @return List<GetShippingAddressListResultVo>
     * @throws Exception
     */
    List<GetShippingAddressListResultVo> getRecentlyShippingAddressList(GetRecentlyShippingAddressListRequestDto getRecentlyShippingAddressListRequestDto) throws Exception;
}
