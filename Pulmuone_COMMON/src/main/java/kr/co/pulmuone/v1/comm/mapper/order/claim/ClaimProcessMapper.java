package kr.co.pulmuone.v1.comm.mapper.order.claim;


import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsOrderAcceptDto;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.*;
import kr.co.pulmuone.v1.order.create.dto.OrderClaimCardPayRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderInfoDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetlDailySchArrivalInfoDto;
import kr.co.pulmuone.v1.order.order.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문클레임 생성, 수정, 삭제 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface ClaimProcessMapper {

	/**
	 * 주문 클레임 마스터 Seq
	 * @return
	 */
	long getOdClaimId();

	/**
	 * 주문 클레임 상세 Seq
	 * @return
	 */
	long getOdClaimDetlId();

	/**
	 * 주문클레임 상세 할인 SEQ
	 * @return
	 */
	long getOdClaimDetlDiscountId();

	/**
	 * 주문클레임 배송지 SEQ
	 * @return
	 */
	long getOdClaimShippingZoneId();

	/**
	 * 주문 클레임 마스터 입력
	 * @param claimVo
	 * @return
	 */
	int addOrderClaim(ClaimVo claimVo);

	/**
	 * 주문 클레임 상세 입력
	 * @param claimDetlVo
	 * @return
	 */
	int addOrderClaimDetl(ClaimDetlVo claimDetlVo);

	/**
	 * 주문 클레임 상세 수정
	 * @param claimDetlVo
	 * @return
	 */
	int putOrderClaimDetl(ClaimDetlVo claimDetlVo);

	/**
	 * 주문 클레임 상세 수정
	 * @param claimDetlVo
	 * @return
	 */
	int putOrderClaimDetlAddShippingPriceInfo(ClaimDetlVo claimDetlVo);

	/**
	 * 주문 거부 클레임 상세 수정
	 * @param claimDetlVo
	 * @return
	 */
	int putDenyDefeOrderClaimDetl(ClaimDetlVo claimDetlVo);

	/**
	 * 주문 클레임 상세 주문 상세번호 ID 수정
	 * @param claimDetlVo
	 * @return
	 */
	int putOrderClaimDetlOdOrderDetlId(ClaimDetlVo claimDetlVo);

	/**
	 * 주문클레임 상세 할인금액 정보 입력
	 * @param claimDetlDiscountVo
	 * @return
	 */
	int addOrderClaimDetlDiscount(ClaimDetlDiscountVo claimDetlDiscountVo);

	/**
	 * 주문클레임 상세 할인금액 목록조회
	 * @param odOrderId
	 * @param odClaimId
	 * @param odOrderDetlIds
	 * @return
	 */
	List<OrderClaimDetlDiscountListInfoDto> getOrderClaimDetlDiscountList(@Param("odOrderId") long odOrderId, @Param("odClaimId") long odClaimId, @Param("odOrderDetlIds") List<Long> odOrderDetlIds);

	/**
	 * 주문클레임 상세 할인금액 정보 입력 ALL
	 * @param orderClaimDetlDiscountList
	 * @return
	 */
	int addOrderClaimDetlDiscountAll(@Param("orderClaimDetlDiscountList") List<OrderClaimDetlDiscountListInfoDto> orderClaimDetlDiscountList);

	/**
	 * 주문 클레임 처리 이력 입력
	 * @param claimDetlHistVo
	 * @return
	 */
	int addOrderClaimDetlHist(ClaimDetlHistVo claimDetlHistVo);

	/**
	 * 주문 클레임 환불 계좌 SEQ
	 * @return
	 */
	long getOdClaimAccountId();

	/**
	 * 주문 클레임 환불 계좌 입력
	 * @param claimAccountVo
	 * @return
	 */
	int addOrderClaimAccount(ClaimAccountVo claimAccountVo);

	/**
	 * 주문 클레임 환불 계좌 수정
	 * @param claimAccountVo
	 * @return
	 */
	int putOrderClaimAccount(ClaimAccountVo claimAccountVo);

	/**
	 * CS 환불 승인 정보 변경
	 * @param claimVo
	 * @return
	 */
	int putOrderClaimCSRefundApproveCd(ClaimVo claimVo);

	/**
	 * 주문상세 원주문번호 취소 갯수 입력
	 * @param orderDetlVo
	 * @return
	 */
	int putOrderDetlCancel(OrderDetlVo orderDetlVo);

	/**
	 * 주문 클레임 첨부파일 SEQ
	 * @return
	 */
	long getOdClaimAttcId();

	/**
	 * 주문 클레임 첨부파일 입력
	 * @param claimAttcVo
	 * @return
	 */
	int addOrderClaimAttc(ClaimAttcVo claimAttcVo);

	/**
	 * 주문 클레임 첨부파일 수정
	 * @param claimAttcVo
	 * @return
	 */
	int putOrderClaimAttc(ClaimAttcVo claimAttcVo);

	/**
	 * 주문 클레임 배송지 입력
	 * @param claimShippingZoneVo
	 * @return
	 */
	int addOrderClaimShippingZone(ClaimShippingZoneVo claimShippingZoneVo);

	/**
	 * 주문 클레임 배송지 수정
	 * @param claimShippingZoneVo
	 * @return
	 */
	int putOrderClaimShippingZone(ClaimShippingZoneVo claimShippingZoneVo);

	/**
	 * 주문 클레임 보내는 배송지 입력
	 * @param claimSendShippingZoneVo
	 * @return
	 */
	int addOrderClaimSendShippingZone(ClaimSendShippingZoneVo claimSendShippingZoneVo);

	/**
	 * 보내는 배송지 PK 얻기
	 */
	long getOdClaimSendShippingZoneId();

	/**
	 * 주문 클레임 보내는 배송지 수정
	 * @param claimSendShippingZoneVo
	 * @return
	 */
	int putOrderClaimSendShippingZone(ClaimSendShippingZoneVo claimSendShippingZoneVo);

	/**
	 * 환불계좌 조회
	 * @param odOrderId
	 * @return
	 */
	OrderAccountDto getRefundBank(long odOrderId);

	/**
	 * 클레임 정보 조회
	 * @param odClaimId
	 * @return
	 */
	OrderClaimInfoDto getClaimInfo(long odClaimId);

	/**
	 * 클레임 상세 상품 정보 조회
	 * @param odClaimId
	 * @return
	 */
	List<OrderClaimDetlListInfoDto> getClaimDetlList(long odClaimId);

	/**
	 * 주문PK, 쿠폰발급PK로 주문클레임 상세 할인금액 조회
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @param odClaimId
	 * @param odClaimDetlIds
	 * @return int
	 */
	int getOrderClaimDetlDiscountPrice(@Param("odOrderId") Long odOrderId, @Param("pmCouponIssueId") Long pmCouponIssueId, @Param("odClaimId") Long odClaimId, @Param("odClaimDetlIds")List<Long> odClaimDetlIds);

	/**
	 * 주문클레임 마스터 조회
	 * @param orderClaimViewRequestDto
	 * @return OrderClaimMasterInfoDto
	 */
	OrderClaimMasterInfoDto getOrderClaimMasterInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

	/**
	 * 주문상세 주문상세 순번(라인번호) 주문번호에 대한 순번 구하기
	 * @param odOrderId
	 * @return
	 */
	int getOrderDetlSeq(long odOrderId);

	/**
	 * PG 또는 BANK 환불을 위한 결제 정보 조회결과
	 * @param orderClaimRegisterRequestDto
	 * @return
	 */
	RefundPaymentInfoDto getRefundPaymentInfo(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto);

	/**
	 * 취소 과세, 비과세 금액 정보 조회결과
	 * @param orderClaimRegisterRequestDto
	 * @return
	 */
	CancelPriceInfoDto getCancelPriceInfo(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto);

	/**
	 * 취소거부 일 때 주문상세에 주문상태 배송중 으로 변경
	 * @param orderDetlVo
	 * @return
	 */
	int putOrderStatusCdChange(OrderDetlVo orderDetlVo);

	/**
	 * 추가 결제 정보 조회
	 * @param odAddPaymentReqInfoId
	 * @return
	 */
	OdAddPaymentReqInfo getOdAddPaymentReqInfo(long odAddPaymentReqInfoId);

	/**
	 * 추가 결제 정보 등록
	 * @param odAddPaymentReqInfo
	 * @return
	 */
	int addOdAddPaymentReqInfo(OdAddPaymentReqInfo odAddPaymentReqInfo);

	/**
	 * 가상계좌 결제 정보 조회
	 * @param odOrderId
	 * @return
	 */
	OrderClaimVirtualPaymentInfoDto getOrderClaimVirtualPaymentInfo(long odOrderId);

	/**
	 * OD_PAYMENT_MASTER 입금전 취소 업데이트
	 * @param reqDto
	 * @return
	 */
	void putPaymentMasterStatus(OrderClaimRegisterRequestDto reqDto);

	/**
	 * 주문자 정보 및 상품 정보 조회
	 * @param orderClaimCardPayRequestDto
	 * @return
	 */
	OrderInfoDto getOrderBuyerInfo(OrderClaimCardPayRequestDto orderClaimCardPayRequestDto);

	/**
	 * 주문 클레임 상세 일일배송 스케쥴 정보 등록
	 * @param odClaimId
	 * @param odClaimDetlId
	 * @param odClaimDailySchList
	 * @return
	 */
	int selectAddOrderClaimDetl(@Param(value = "odClaimId") long odClaimId, @Param(value = "odClaimDetlId") long odClaimDetlId, @Param(value = "odClaimDailySchList") List<OrderClaimGoodsScheduleInfoDto> odClaimDailySchList);

	/**
	 * 주문상세 일일배송 스케쥴 정보 업데이트
	 * @param orderDetlDailySchArrivalInfo
	 * @return
	 */
	int putOdOrderDetlDailySch(OrderDetlDailySchArrivalInfoDto orderDetlDailySchArrivalInfo);

	/**
	 * 주문 I/F여부 체크
	 * @param odOrderDetlId
	 * @return
	 */
	String getOrderIsInterfaceCheck(@Param(value = "odOrderDetlId") long odOrderDetlId);

	/**
	 * 출고처 I/F여부 체크
	 * @param odOrderDetlId
	 * @return
	 */
	int getWarehouseIsInterfaceYnCheck(@Param(value = "odOrderDetlId") long odOrderDetlId, @Param(value = "urWarehouseIds") String[] urWarehouseIds);

	/**
	 * 주문 상세 클레임 상품 정보 > BOS 클레임 사유 변경
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
    int putOrderClaimDetlBosClaimReason(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

	/**
	 * 주문 상세 클레임 상품 정보 > BOS 클레임 사유 변경 이력 등록
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
    int addOrderClaimBosReasonHist(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

	/**
	 * 주문상세 일일배송 스케쥴 정보 사용여부 업데이트
	 * @param orderDetlDailySchArrivalInfoList
	 * @return
	 */
	int putOdOrderDetlDailySchToUseYn(@Param(value = "orderDetlDailySchArrivalInfoList") List<OrderDetlDailySchArrivalInfoDto> orderDetlDailySchArrivalInfoList);

	/**
	 * 주문상세 일일배송 재배송 스케쥴 정보 등록
	 * @param orderDetlDailySchArrivalInfo
	 * @return
	 */
	int addOdOrderDetlDailyExchangeSch(OrderDetlDailySchArrivalInfoDto orderDetlDailySchArrivalInfo);

	/**
	 * 회수 송장번호 등록
	 * @param orderClaimRegisterRequestDto
	 * @return
	 */
    int addReturnTrackingNumber(ReturnTrackingNumberVo returnTrackingNumberVo);

	/**
	 * CJ 주문접수
	 * @param dto
	 * @return
	 */
	int addCJLogisticsOrderAccept(CJLogisticsOrderAcceptDto dto);

	/**
	 * 출고처그룹이 온라인사업부 인지 체크
	 * @param urWarehouseId
	 * @return
	 */
	int selectUrWarehouseGrpIdCheck(@Param(value="urWarehouseId") long urWarehouseId);

	/**
	 * 출고처 용인물류/백암물류 값 가져오기
	 * @param urWarehouseId
	 * @return
	 */
	String selectGoodsWarehouseCode(@Param(value="urWarehouseId") long urWarehouseId);

	/**
	 * 주문 클레임 직접 결제여부 수정
	 * @param claimVo
	 * @return
	 */
	int putOrderClaimDirectPaymentYn(ClaimVo claimVo);

	/**
	 * 주문 클레임 상세 직접 결제 클레임상태 수정
	 * @param claimDetlVo
	 * @return
	 */
	int putOrderClaimDetlDirectPaymentClaimStatus(ClaimDetlVo claimDetlVo);

	/**
	 * 추가 배송비 정보 조회
	 * @param reqDto
	 * @return
	 */
	List<OrderClaimAddShippingPaymentInfoDto> putOrderClaimAddShippingPrice(OrderClaimRegisterRequestDto reqDto);

	/**
	 * CS환불 정보 등록
	 * @param odCsInfoVo
	 * @return
	 */
	int addOrderCSRefundInfo(OdCsInfoVo odCsInfoVo);

	/**
	 * CS환불 정보 조회
	 * @param reqDto
	 * @return
	 */
	OrderCSRefundInfoDto getCSRefundInfo(OrderCSRefundRegisterRequestDto reqDto);

	/**
	 * CS환불 정보 상세 조회
	 * @param reqDto
	 * @return
	 */
	List<OrderCSRefundGoodsInfoDto> getCSRefundInfoDetl(OrderCSRefundRegisterRequestDto reqDto);

	/**
	 * CS환불 정보 수정
	 * @param odCsInfoVo
	 * @return
	 */
	int putOrderCSRefundInfo(OdCsInfoVo odCsInfoVo);

	/**
	 * CS환불 상세 정보 등록
	 * @param odCsInfoDetlVo
	 * @return
	 */
	int addOrderCSRefundInfoDetl(OdCsInfoDetlVo odCsInfoDetlVo);
	
	/**
	 * CS환불 계좌 정보 등록
	 * @param odCsAccountVo
	 * @return
	 */
	int addOrderCSRefundAccountInfo(OdCsAccountVo odCsAccountVo);

	/**
	 * 회원 ID로 CS환불 역할그룹의 회계코드 조회
	 * @param urUserId
	 * @return
	 */
	OrderCSUseFinInfoDto getUserFinInfoByLoginId(long urUserId);

	/**
	 * CS환불 PG 정보 등록
	 * @param odCsInfoDetlVo
	 * @return
	 */
	int addOrderCSRefundPGInfo(OdCsPgInfoVo odCsInfoDetlVo);

	/**
	 * CS환불 PG 정보 등록
	 * @param odCsInfoDetlHistVo
	 * @return
	 */
	int addOrderCSRefundInfoDetlHist(OdCsInfoDetlHistVo odCsInfoDetlHistVo);

	/**
	 * 클레임 환불 대상 정보 조회
	 * @param odOrderId
	 * @param odClaimId
	 * @return
	 */
	OrderClaimRegisterRequestDto getClaimRefundPaymentInfo(@Param(value = "odOrderId") long odOrderId, @Param(value = "odClaimId") long odClaimId);

	/**
	 * 클레임 상세 환불 대상 정보 조회
	 * @param odOrderId
	 * @param odClaimId
	 * @return
	 */
	List<OrderClaimGoodsInfoDto> getClaimDetlRefundPaymentInfo(@Param(value = "odOrderId") long odOrderId, @Param(value = "odClaimId") long odClaimId);

	/**
	 * 추가 결제 클레임 정보 조회
	 * @param pgIntegratedInfo
	 * @return
	 */
	OrderClaimAddPaymentInfoDto getAddPaymentClaimInfo(OrderClaimAddPaymentPgInfoDto pgIntegratedInfo);

	/**
	 * 결제 마스터 상태 정보 업데이트
	 * @param orderPaymentMasterVo
	 * @return
	 */
	int putAddPaymentPayStatus(OrderPaymentMasterVo orderPaymentMasterVo);

	/**
	 * 클레임 상세 이력 등록
	 * @param claimDetlHistVo
	 * @return
	 */
	int addOdClaimDetlHistByOdClaimId(ClaimDetlHistVo claimDetlHistVo);

	/**
	 * PsShippingCompId 가져오기
	 * @param logisticsCode
	 * @return
	 */
	String getPsShippingCompId(@Param(value = "logisticsCode") String logisticsCode);
}
