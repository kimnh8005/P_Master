package kr.co.pulmuone.v1.comm.mapper.order.regular;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.order.regular.dto.RegularReqArriveDtListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqConsultListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqCycleDaysInfoResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqHistoryListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqPaymentCardDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqPaymentListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqReqConsultDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqRoundGoodsListResultDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqShippingZoneDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultDetailGoodsListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultGoodsDetailInfoDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultLastInfoDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultNextReqRoundSkipResultDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultOrderStatusResultDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultReqRoundGoodsListDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqMemoVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqVo;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 상세 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 07.	김명진 		최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderRegularDetailMapper {

	/**
	 * 정기배송주문신청 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	RegularReqListDto getOrderRegularReqInfo(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주문 결과 상세 PK 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	long getOrderRegularResultDetlId(@Param(value = "odRegularReqId") long odRegularReqId, @Param(value = "ilGoodsId") long ilGoodsId) throws Exception;

	/**
	 * 정기배송주문신청 배송정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	RegularReqShippingZoneDto getOrderRegularReqDetailShippingZone(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;

	/**
	 * 정기배송주문신청 내역 신청 결제정보 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	List<RegularReqPaymentListDto> getOrderRegularReqDetailPaymentList(@Param(value = "odRegularReqId") long odRegularReqId,
																		@Param(value = "regularStatusCdList")  List<String> regularStatusCdList) throws Exception;

	/**
	 * 정기배송주문신청 내역 신청 결제예정 금액 상품 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	List<RegularResultReqRoundGoodsListDto> getOrderRegularReqDetailPaymentGoods(
																				@Param(value = "odRegularReqId") long odRegularReqId,
																				@Param(value = "odRegularResultId") long odRegularResultId,
																				@Param(value = "reqDetailStatusCdList") List<String> reqDetailStatusCdList
																				) throws Exception;

	/**
	 * 정기배송주문신청 신청상담 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	List<RegularReqConsultListDto> getOrderRegularReqDetailConsultList(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주문 신청 메모 수정
	 * @param orderRegularReqMemoVo
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularReqMemo(OrderRegularReqMemoVo orderRegularReqMemoVo) throws Exception;

	/**
	 * 정기배송 주문 신청 메모 삭제
	 * @param regularReqReqConsultDto
	 * @return
	 * @throws Exception
	 */
	int delOrderRegularReqMemo(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception;

	/**
	 * 정기배송주문신청 내역 처리 이력 목록 조회
	 * @param odRegularReqId
	 * @param batchId
	 * @return
	 * @throws Exception
	 */
	List<RegularReqHistoryListDto> getOrderRegularReqDetailHistoryList(@Param(value = "odRegularReqId") long odRegularReqId, @Param(value = "batchId") long batchId) throws Exception;

	/**
	 * 정기배송주문신청 회차별 상품 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	List<RegularReqRoundGoodsListResultDto> getOrderRegularReqDetailRoundGoodsList(@Param(value = "odRegularReqId") long odRegularReqId,
																					@Param(value = "reqDetailStatusCd") String reqDetailStatusCd) throws Exception;

	/**
	 * 정기배송주문신청 배송 내역 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	List<RegularReqRoundGoodsListResultDto> getOrderRegularReqDetailDeliveryGoodsList(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;

	/**
	 * 정기배송주문신청 내역 상품 리스트 조회
	 * @param odRegularReqId
	 * @param regularReqDetailStatus
	 * @return
	 * @throws Exception
	 */
	List<RegularResultReqRoundGoodsListDto> getOrderRegularReqDetailGoodsList(@Param(value = "odRegularReqId") long odRegularReqId, @Param(value = "regularDetlStatusCd")  String regularDetlStatusCd) throws Exception;

	/**
	 * 정기배송주문결과 정기배송주문경과PK 리스트 조회
	 * @param odRegularReqId
	 * @param orderCreateDt
	 * @param regularStatusCd
	 * @return
	 * @throws Exception
	 */
	List<RegularResultListDto> getOrderRegularResultIdList(	@Param(value = "odRegularReqId") long odRegularReqId,
															@Param(value = "orderCreateDt")  LocalDate orderCreateDt,
															@Param(value = "regularStatusCd")  String regularStatusCd) throws Exception;

	/**
	 * 정기배송주문결과 상세 상품 리스트 조회
	 * @param odRegularReqId
	 * @param regularReqDetailStatus
	 * @return
	 * @throws Exception
	 */
	List<RegularResultDetailGoodsListDto> getOrderRegularResultDetailGoodsList(@Param(value = "odRegularReqId") long odRegularReqId, @Param(value = "regularDetlStatusCd")  String regularDetlStatusCd) throws Exception;

	/**
	 * 정기배송 주문신청 업데이트
	 * @param orderRegularReqVo
	 * @param termExtensionYn
	 * @param termExtensionResetYn
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularReq(@Param(value = "orderRegularReqVo") OrderRegularReqVo orderRegularReqVo,
							@Param(value = "termExtensionYn") String termExtensionYn,
							@Param(value = "termExtensionResetYn") String termExtensionResetYn) throws Exception;

	/**
	 * 정기배송 마지막 회차 주문 도착 예정일 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	RegularResultLastInfoDto getRegularResultLastOrderArriveDt(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주기 요일 변경 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	RegularReqCycleDaysInfoResponseDto getOrderRegularCycleDaysInfo(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;

	/**
	 * 정기배송 도착예정일 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	List<RegularReqArriveDtListDto> getOrderRegularArriveDays(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;

	/**
	 * 정기배송 결과 상세 미완료 회차 정보 삭제
	 * @param odRegularReqId
	 * @param standArriveDt
	 * @throws Exception
	 */
	void deleteOrderRegularResultDetl(@Param(value = "odRegularReqId") long odRegularReqId, @Param(value = "standArriveDt") String standArriveDt) throws Exception;

	/**
	 * 정기배송 결과 미완료 회차 정보 삭제
	 * @param odRegularReqId
	 * @param standArriveDt
	 * @throws Exception
	 */
	void deleteOrderRegularResult(@Param(value = "odRegularReqId") long odRegularReqId, @Param(value = "standArriveDt") String standArriveDt) throws Exception;

	/**
	 * 정기배송 회차 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	int getOrderRegularResultReqRound(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;

	/**
	 * 정기배송 결과 상품 정보 얻기
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	RegularReqListDto getOrderRegularResultGoodsInfo(@Param(value = "odRegularResultId") long odRegularResultId, @Param(value = "ilGoodsId") long ilGoodsId) throws Exception;

	/**
	 * 정기배송 배송지 변경
	 * @param regularReqShippingZoneDto
	 * @throws Exception
	 */
	void putOrderRegularShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto) throws Exception;

	/**
	 * 정기배송 결과 상품 주문 상태 확인
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	RegularResultOrderStatusResultDto getOrderRegularResultGoodsOrderStatusCd(@Param(value = "odRegularResultId") long odRegularResultId) throws Exception;

	/**
	 * 정기배송 상세 상태 변경
	 * @param odRegularResultId
	 * @param reqDetailStatusCd
	 * @throws Exception
	 */
	int putOrderRegularDetailStatusCd(@Param(value = "odRegularResultId") long odRegularResultId
									, @Param(value = "reqDetailStatusCd") String reqDetailStatusCd
									, @Param(value = "changeReqDetailStatusCd") String changeReqDetailStatusCd
									, @Param(value = "ilGoodsId") long ilGoodsId) throws Exception;

	/**
	 * 정기배송 결과 상세 상품 취소 처리
	 * @param odregularReqId
	 * @param reqDetailStatusCd
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularResultDetailGoodsCancel(@Param(value = "odRegularReqId") long odRegularReqId
											, @Param(value = "reqDetailStatusCd") String reqDetailStatusCd
											, @Param(value = "ilGoodsId") long ilGoodsId) throws Exception;

	/**
	 * 정기배송 결과 상태 해지 처리
	 * @param odRegularReqId
	 * @param reqDetailStatusCd
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularResultStatusCancel(@Param(value = "odRegularReqId") long odRegularReqId
										, @Param(value = "regularStatusCd") String regularStatusCd
										, @Param(value = "reqDetailStatusCd") String reqDetailStatusCd) throws Exception;

	/**
	 * 정기배송 결과 상태 진행중 처리
	 * @param odRegularReqId
	 * @param reqDetailStatusCd
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularResultStatusIng(@Param(value = "odRegularReqId") long odRegularReqId
										, @Param(value = "regularStatusCd") String regularStatusCd
										, @Param(value = "reqDetailStatusCd") String reqDetailStatusCd) throws Exception;

	/**
	 * 정기배송 상세 상품 취소 처리
	 * @param odregularReqId
	 * @param reqDetailStatusCd
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularReqDetailGoodsCancel(@Param(value = "odRegularReqId") long odRegularReqId
										, @Param(value = "reqDetailStatusCd") String reqDetailStatusCd
										, @Param(value = "ilGoodsId") long ilGoodsId) throws Exception;

	/**
	 * 정기배송 신청 상태 해지 처리
	 * @param odregularReqId
	 * @param reqDetailStatusCd
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularReqStatusCancel(@Param(value = "odRegularReqId") long odRegularReqId
									, @Param(value = "regularStatusCd") String regularStatusCd
									, @Param(value = "reqDetailStatusCd") String reqDetailStatusCd) throws Exception;

	/**
	 * 정기결제 카드 정보 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	RegularReqPaymentCardDto getRegularPaymentCardInfo(@Param(value = "urUserId") long urUserId) throws Exception;

	/**
	 * 정기결제 다음 결제일 정보 조회
	 * @param urUserId
	 * @param regularStatusCd
	 * @return
	 * @throws Exception
	 */
	RegularReqPaymentCardDto getRegularNextPaymentDtInfo(@Param(value = "urUserId") long urUserId, @Param(value = "regularStatusCd") String regularStatusCd) throws Exception;

	/**
	 * 정기결제 카드 삭제
	 * @param odRegularPaymentKeyId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	int delOrderRegularPaymentCardInfo(@Param(value = "odRegularPaymentKeyId") long odRegularPaymentKeyId, @Param(value = "urUserId") long urUserId) throws Exception;

	/**
	 * 정기배송 다음 회차 내역 중 건너뛰기 내역 정보 존재 여부 확인
	 * @param odRegularReqId
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	RegularResultNextReqRoundSkipResultDto getOrderRegularResultNextGoodsSkipInfo(@Param(value = "odRegularReqId") long odRegularReqId, @Param(value = "reqDetailStatusCd") String reqDetailStatusCd) throws Exception;

	/**
	 * 정기배송 결과 상세 정보 얻기
	 * @param odRegularResultDetlId
	 * @return
	 * @throws Exception
	 */
	RegularResultGoodsDetailInfoDto getOrderRegularResultDetailGoodsInfo(@Param(value = "odRegularResultDetlId") long odRegularResultDetlId) throws Exception;

	/**
	 * 정기배송 결과 다음 회차 PK 얻기
	 * @param regularResultGoodsDetailInfo
	 * @return
	 * @throws Exception
	 */
	RegularResultGoodsDetailInfoDto getOrderRegularResultNextResultId(RegularResultGoodsDetailInfoDto regularResultGoodsDetailInfo) throws Exception;

	/**
	 * 정기배송 추가 할인 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	RegularResultGoodsDetailInfoDto getOrderRegularResultAddDiscountInfo(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;
}
