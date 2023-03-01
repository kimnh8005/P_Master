package kr.co.pulmuone.v1.order.shipping.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.user.buyer.dto.GetRecentlyShippingAddressListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
class OrderShippingServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private OrderShippingService orderShippingService;

    @Test
    void 장바구니_최근배송지_목록_조회내역있음() throws Exception {

        GetRecentlyShippingAddressListRequestDto getRecentlyShippingAddressListRequestDto = new GetRecentlyShippingAddressListRequestDto();
        getRecentlyShippingAddressListRequestDto.setUrUserId("1647304");
        getRecentlyShippingAddressListRequestDto.setLimitCount(10);

        List<GetShippingAddressListResultVo> resultVoList = orderShippingService.getRecentlyShippingAddressList(getRecentlyShippingAddressListRequestDto);

        Assertions.assertTrue(resultVoList.size() > 0);

    }

    @Test
    void 장바구니_최근배송지_목록_조회내역없음() throws Exception {

        GetRecentlyShippingAddressListRequestDto getRecentlyShippingAddressListRequestDto = new GetRecentlyShippingAddressListRequestDto();
        getRecentlyShippingAddressListRequestDto.setUrUserId(null);
        getRecentlyShippingAddressListRequestDto.setLimitCount(10);

        List<GetShippingAddressListResultVo> resultVoList = orderShippingService.getRecentlyShippingAddressList(getRecentlyShippingAddressListRequestDto);

        Assertions.assertTrue(resultVoList.size() == 0);

    }
}