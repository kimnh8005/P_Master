package kr.co.pulmuone.v1.comm.mapper.order.regular;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultPaymentListDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqOrderDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderRegularPaymentMapper {

	/**
	 * 정기배송 주문 결제 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	List<RegularResultPaymentListDto> getRegularOrderResultPaymentList(@Param(value = "regularStatusCd") String regularStatusCd) throws Exception;

	/**
	 * 주문날짜 정보 업데이트
	 * @param orderDtVo
	 * @return
	 * @throws Exception
	 */
	int putOrderDtInfo(OrderDtVo orderDtVo) throws Exception;

	/**
	 * 주문 상세 주문상태코드 업데이트
	 * @param orderDetlVo
	 * @return
	 * @throws Exception
	 */
	int putOrderDetlInfo(OrderDetlVo orderDetlVo) throws Exception;

	/**
	 * 정기배송 결과 테이블 회차 완료 여부 업데이트
	 * @param orderRegularResultVo
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularResultReqRoundYnInfo(OrderRegularResultVo orderRegularResultVo) throws Exception;

	/**
	 * 정기배송 결과 상세 테이블 해지 처리
	 * @param orderRegularResultDetlVo
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularDetlStatusCancel(OrderRegularResultDetlVo orderRegularResultDetlVo) throws Exception;

	/**
	 * 정기배송 결과 테이블 해지 처리
	 * @param orderRegularResultVo
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularStatusCancel(OrderRegularResultVo orderRegularResultVo) throws Exception;

	/**
	 * 정기배송 신청 상세 테이블 해지 처리
	 * @param orderRegularReqOrderDetlVo
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularReqDetlStatusCancel(OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo) throws Exception;

	/**
	 * 정기배송 신청 테이블 해지 처리
	 * @param orderRegularReqVo
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularReqStatusCancel(OrderRegularReqVo orderRegularReqVo) throws Exception;

	/**
	 * 정기배송 주문 상세 상품PK 목록 조회
	 * @param odRegularResultId
	 * @return
	 * @throws Exception
	 */
	List<Long> getOdRegularOrderDetlGoodsList(@Param(value = "odRegularResultId") long odRegularResultId);

	/**
	 * 정기배송 결과 상세 테이블 건너뛰기 처리
	 * @param orderRegularResultDetlVo
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularDetlStatusSkip(OrderRegularResultDetlVo orderRegularResultDetlVo) throws Exception;
}
