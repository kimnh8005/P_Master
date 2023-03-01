package kr.co.pulmuone.v1.comm.mapper.order.shipping;

import kr.co.pulmuone.v1.user.buyer.dto.GetRecentlyShippingAddressListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderShippingMapper {

    // 최근배송지 목록
    List<GetShippingAddressListResultVo> getRecentlyShippingAddressList(GetRecentlyShippingAddressListRequestDto getRecentlyShippingAddressListRequestDto) throws Exception;
}
