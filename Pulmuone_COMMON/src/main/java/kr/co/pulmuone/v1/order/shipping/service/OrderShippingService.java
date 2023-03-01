package kr.co.pulmuone.v1.order.shipping.service;

import kr.co.pulmuone.v1.comm.mapper.order.shipping.OrderShippingMapper;
import kr.co.pulmuone.v1.user.buyer.dto.GetRecentlyShippingAddressListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200911   	   김만환            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderShippingService {

    private final OrderShippingMapper orderShippingMapper;

    /**
     * 장바구니 최근 배송지 목록
     *
     * @param getRecentlyShippingAddressListRequestDto
     * @return List<GetShippingAddressListResultVo>
     * @throws Exception
     */

    protected List<GetShippingAddressListResultVo> getRecentlyShippingAddressList(GetRecentlyShippingAddressListRequestDto getRecentlyShippingAddressListRequestDto) throws Exception
    {
        return orderShippingMapper.getRecentlyShippingAddressList(getRecentlyShippingAddressListRequestDto);
    }

}
