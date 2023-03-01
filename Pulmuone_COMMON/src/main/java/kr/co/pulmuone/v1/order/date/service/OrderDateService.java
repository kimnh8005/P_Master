package kr.co.pulmuone.v1.order.date.service;

import kr.co.pulmuone.v1.comm.mapper.order.order.OrderDateMapper;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlDtVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* <PRE>
* Forbiz Korea
* 일괄송장 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 01. 08.        이규한          	  최초작성
*  1.1    2021. 01. 13.        김명진             클레임, 클레임 상세 상태별 일자 수정 추가
* =======================================================================
* </PRE>
*/

@Service
@RequiredArgsConstructor
public class OrderDateService {

    @Autowired
    private OrderDateMapper orderDateMapper;

    /**
     * @Desc 주문 상태별 일자 수정
     * @param orderDtVo
     * @throws Exception
     * @return int
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int putOrderDt(OrderDtVo orderDtVo) {
   		return orderDateMapper.putOrderDt(orderDtVo);
	}

    /**
     * @Desc 주문 상세 상태별 일자 수정
     * @param orderDetlVo
     * @throws Exception
     * @return int
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int putOrderDetailDt(OrderDetlVo orderDetlVo) {
   		return orderDateMapper.putOrderDetailDt(orderDetlVo);
	}

   	/**
   	 * @Desc 클레임 상세 상태별 일자 수정
   	 * @param claimDetlDtVo
   	 * @throws Exception
   	 * @return int
   	 */
   	@Transactional(rollbackFor = Exception.class)
   	protected int putClaimDetailDt(ClaimDetlDtVo claimDetlDtVo) {
   		return orderDateMapper.putClaimDetailDt(claimDetlDtVo);
   	}
}