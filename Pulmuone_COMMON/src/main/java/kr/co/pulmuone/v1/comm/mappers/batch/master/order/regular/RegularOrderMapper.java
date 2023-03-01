package kr.co.pulmuone.v1.comm.mappers.batch.master.order.regular;

import java.util.List;

import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultPaymentListDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqOrderDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 배치 Mapper
 * </PRE>
 */

@Mapper
public interface RegularOrderMapper {

	/**
	 * 정기배송 주문서 생성 시 모든 상품이 건너뛰기 일 경우 해당 회차 완료 처리
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularResultReqRoundYn(String reqDetailStatusCd) throws Exception;

	/**
	 * 주문결제 마스터 정보 업데이트
	 * @param orderPaymentMasterVo
	 * @return
	 * @throws Exception
	 */
	int putOrderPaymentMasterInfo(OrderPaymentMasterVo orderPaymentMasterVo) throws Exception;

}
