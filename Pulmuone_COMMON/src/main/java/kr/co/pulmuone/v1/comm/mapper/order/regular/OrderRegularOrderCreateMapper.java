package kr.co.pulmuone.v1.comm.mapper.order.regular;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderRegularOrderCreateMapper {

	/**
	 * 정기배송 주문 생성 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	List<RegularResultCreateOrderListDto> getRegularOrderResultCreateGoodsList(RegularResultCreateOrderListRequestDto regularResultCreateOrderListRequestDto) throws BaseException;

	/**
	 * 정기배송 추가할인 기준 회차 정보 조회
	 * @param odRegularResultId
	 * @return
	 * @throws Exception
	 */
	int getAddDiscountStdReqRound(@Param(value = "odRegularResultId") long odRegularResultId);

	/**
	 * 정기배송 상세 상품 판매상태 업데이트
	 * @param odRegularResultId
	 * @param saleStatus
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularDetlGoodsSaleStatus(@Param(value = "odRegularResultId") long odRegularResultId,
										   @Param(value = "saleStatus") String saleStatus) throws Exception;

	/**
	 * 정기배송 주문 신청 상태 변경
	 * @param odRegularResultId
	 * @param regularStatusCd
	 * @return
	 * @throws Exception
	 */
	int putRegularOrderReqStatus(@Param(value = "odRegularResultId") long odRegularResultId, @Param(value = "regularStatusCd") String regularStatusCd) throws Exception;

	/**
	 * 정기배송 주문 결과 상태 변경
	 * @param odRegularResultId
	 * @param regularStatusCd
	 * @return
	 * @throws Exception
	 */
	int putRegularOrderResultStatus(@Param(value = "odRegularResultId") long odRegularResultId, @Param(value = "regularStatusCd") String regularStatusCd) throws Exception;

	/**
	 * 정기배송 상세 상품 판매상태 업데이트
	 * @param odRegularResultId
	 * @param notOnSaleGoodsList
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularDetlGoodsSaleStatusByGoodsList(@Param(value = "odRegularResultId") long odRegularResultId,
														@Param(value = "notOnSaleGoodsList") List<RegularResultCreateOrderListDto> notOnSaleGoodsList) throws Exception;

	/**
	 * 정기배송 주문 결과 주문서생성 여부 변경
	 * @return
	 * @throws Exception
	 */
	int putRegularResultOrderCreateYn(@Param(value = "odRegularResultId") long odRegularResultId, @Param(value = "odid") String odid) throws BaseException;

	/**
	 * 정기배송 신청정보 조회
	 * @param odRegularResultId
	 * @return
	 * @throws BaseException
	 */
	RegularReqCreateOrderListDto getRegularOrderReqInfo(@Param(value = "odRegularResultId") long odRegularResultId) throws BaseException;

	/**
	 * 주문번호로 주문정보 조회
	 * @param odid
	 * @return
	 * @throws Exception
	 */
	OrderClaimRegisterRequestDto getOdOrderInfoByOdid(String odid) throws Exception;

	/**
	 * 판매중이 아닌 상품 주문 상세 조회
	 * @param odid
	 * @param ilGoodsIds
	 * @return
	 * @throws Exception
	 */
	List<OrderClaimGoodsInfoDto> getOrderDetlInfoListByNotOnSaleGoodsList(@Param(value = "odid") String odid,
																		  @Param(value = "ilGoodsIds") List<Long> ilGoodsIds) throws Exception;

}
