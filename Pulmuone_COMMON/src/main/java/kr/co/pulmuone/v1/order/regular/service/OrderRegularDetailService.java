package kr.co.pulmuone.v1.order.regular.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.order.regular.OrderRegularDetailMapper;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송주문상세 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.02.07	  김명진           최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRegularDetailService {

	private final OrderRegularDetailMapper orderRegularDetailMapper;

	/**
	 * 정기배송주문신청 내역 상품 리스트 조회
	 *
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected RegularReqListDto getOrderRegularReqInfo(long odRegularReqId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularReqInfo(odRegularReqId);
	}

	/**
	 * 정기배송 주문 결과 상세 PK 조회
	 * @param odRegularReqId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	protected long getOrderRegularResultDetlId(long odRegularReqId, long ilGoodsId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultDetlId(odRegularReqId, ilGoodsId);
	}

	/**
	 * 정기배송주문신청 내역 배송지 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected RegularReqShippingZoneDto getOrderRegularReqDetailShippingZone(long odRegularReqId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularReqDetailShippingZone(odRegularReqId);
	}

	/**
	 * 정기배송주문신청 내역 신청 결제정보 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected List<RegularReqPaymentListDto> getOrderRegularReqDetailPaymentList(long odRegularReqId, List<String> regularStatusCdList) throws Exception {
		return orderRegularDetailMapper.getOrderRegularReqDetailPaymentList(odRegularReqId, regularStatusCdList);
	}

	/**
	 * 정기배송주문신청 내역 신청 결제예정 금액 상품 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	List<RegularResultReqRoundGoodsListDto> getOrderRegularReqDetailPaymentGoods(long odRegularReqId, long odRegularResultId, List<String> reqDetailStatusCdList) throws Exception {
		return orderRegularDetailMapper.getOrderRegularReqDetailPaymentGoods(odRegularReqId, odRegularResultId, reqDetailStatusCdList);
	}

	/**
	 * 정기배송주문신청 신청상담 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected List<RegularReqConsultListDto> getOrderRegularReqDetailConsultList(long odRegularReqId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularReqDetailConsultList(odRegularReqId);
	}

	/**
	 * 정기배송 주문 신청 메모 수정
     * @param orderRegularReqVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularReqMemo(OrderRegularReqMemoVo orderRegularReqMemoVo) throws Exception {
		return orderRegularDetailMapper.putOrderRegularReqMemo(orderRegularReqMemoVo);
	}

	/**
	 * 정기배송 주문 신청 메모 삭제
     * @param orderRegularReqVo
	 * @return
	 * @throws Exception
	 */
	protected int delOrderRegularReqMemo(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception {
		return orderRegularDetailMapper.delOrderRegularReqMemo(regularReqReqConsultDto);
	}

	/**
	 * 정기배송주문신청 내역 처리 이력 목록 조회
	 * @param odRegularReqId
	 * @param batchId
	 * @return
	 * @throws Exception
	 */
	protected List<RegularReqHistoryListDto> getOrderRegularReqDetailHistoryList(long odRegularReqId, long batchId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularReqDetailHistoryList(odRegularReqId, batchId);
	}

	/**
	 * 정기배송주문신청 회차별 상품 목록 조회
	 * @param odRegularReqId
	 * @param regularStatusCd
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	protected List<RegularReqRoundGoodsListResultDto> getOrderRegularReqDetailRoundGoodsList(long odRegularReqId, String reqDetailStatusCd) throws Exception {
		return orderRegularDetailMapper.getOrderRegularReqDetailRoundGoodsList(odRegularReqId, reqDetailStatusCd);
	}

	/**
	 * 정기배송주문신청 배송 내역 조회
	 * @param odRegularReqId
	 * @param regularStatusCd
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	protected List<RegularReqRoundGoodsListResultDto> getOrderRegularReqDetailDeliveryGoodsList(long odRegularReqId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularReqDetailDeliveryGoodsList(odRegularReqId);
	}

	/**
	 * 정기배송주문신청 내역 상세 목록 조회
	 * @param odRegularReqId
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	protected List<RegularResultReqRoundGoodsListDto> getOrderRegularReqDetailGoodsList(long odRegularReqId, String regularDetlStatusCd) throws Exception {
		return orderRegularDetailMapper.getOrderRegularReqDetailGoodsList(odRegularReqId, regularDetlStatusCd);
	}

	/**
	 * 정기배송주문결과 정기배송주문경과PK 리스트 조회
	 * @param odRegularReqId
	 * @param orderCreateDt
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	protected List<RegularResultListDto> getOrderRegularResultIdList(long odRegularReqId, LocalDate orderCreateDt, String regularStatusCd) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultIdList(odRegularReqId, orderCreateDt, regularStatusCd);
	}

	/**
	 * 정기배송주문결과 상세 목록 조회
	 * @param odRegularReqId
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	protected List<RegularResultDetailGoodsListDto> getOrderRegularResultDetailGoodsList(long odRegularReqId, String regularDetlStatusCd) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultDetailGoodsList(odRegularReqId, regularDetlStatusCd);
	}

	/**
	 * 정기배송 주문신청 업데이트
	 * @param orderRegularReqVo
	 * @param termExtensionYn
	 * @param termExtensionResetYn
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularReq(OrderRegularReqVo orderRegularReqVo, String termExtensionYn, String termExtensionResetYn) throws Exception {
		return orderRegularDetailMapper.putOrderRegularReq(orderRegularReqVo, termExtensionYn, termExtensionResetYn);
	}

	/**
	 * 정기배송 마지막 회차 주문 도착 예정일 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected RegularResultLastInfoDto getRegularResultLastOrderArriveDt(long odRegularReqId) throws Exception {
		return orderRegularDetailMapper.getRegularResultLastOrderArriveDt(odRegularReqId);
	}

	/**
	 * 정기배송 주기 요일 변경 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected RegularReqCycleDaysInfoResponseDto getOrderRegularCycleDaysInfo(long odRegularReqId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularCycleDaysInfo(odRegularReqId);
	}

	/**
	 * 정기배송 도착예정일 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected List<RegularReqArriveDtListDto> getOrderRegularArriveDays(long odRegularReqId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularArriveDays(odRegularReqId);
	}

	/**
	 * 정기배송 결과 상세 미완료 회차 정보 삭제
	 * @param odRegularReqId
	 * @param standArriveDt
	 * @throws Exception
	 */
	protected void deleteOrderRegularResultDetl(long odRegularReqId, String standArriveDt) throws Exception {
		orderRegularDetailMapper.deleteOrderRegularResultDetl(odRegularReqId, standArriveDt);
	}

	/**
	 * 정기배송 결과 미완료 회차 정보 삭제
	 * @param odRegularReqId
	 * @throws Exception
	 */
	protected void deleteOrderRegularResult(long odRegularReqId, String standArriveDt) throws Exception {
		orderRegularDetailMapper.deleteOrderRegularResult(odRegularReqId, standArriveDt);
	}

	/**
	 * 정기배송 결과 회차 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected int getOrderRegularResultReqRound(long odRegularReqId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultReqRound(odRegularReqId);
	}

	/**
	 * 정기배송 배송지 변경
	 * @param regularReqShippingZoneDto
	 * @throws Exception
	 */
	protected void putOrderRegularShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto) throws Exception {
		orderRegularDetailMapper.putOrderRegularShippingZone(regularReqShippingZoneDto);
	}

	/**
	 * 정기배송 결과 상품 정보 얻기
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	protected RegularReqListDto getOrderRegularResultGoodsInfo(long odRegularResultId, long ilGoodsId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultGoodsInfo(odRegularResultId, ilGoodsId);
	}

	/**
	 * 정기배송 결과 상품 주문 상태 확인
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	protected RegularResultOrderStatusResultDto getOrderRegularResultGoodsOrderStatusCd(long odRegularResultId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultGoodsOrderStatusCd(odRegularResultId);
	}

	/**
	 * 정기배송 상세 상태 변경
	 * @param odRegularResultId
	 * @throws Exception
	 */
	protected int putOrderRegularDetailStatusCd(long odRegularResultId, String reqDetailStatusCd, String changeReqDetailStatusCd, long ilGoodsId) throws Exception {
		return orderRegularDetailMapper.putOrderRegularDetailStatusCd(odRegularResultId, reqDetailStatusCd, changeReqDetailStatusCd, ilGoodsId);
	}

	/**
	 * 정기배송 결과 상세 상품 취소 처리
	 * @param odregularReqId
	 * @param reqDetailStatusCd
	 * @param ilGoodsId
	 * @throws Exception
	 */
	protected int putOrderRegularResultDetailGoodsCancel(long odregularReqId, String reqDetailStatusCd, long ilGoodsId) throws Exception {
		return orderRegularDetailMapper.putOrderRegularResultDetailGoodsCancel(odregularReqId, reqDetailStatusCd, ilGoodsId);
	}

	/**
	 * 정기배송 결과 상태 해지 처리
	 * @param odRegularReqId
	 * @param regularStatusCd
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularResultStatusCancel(long odRegularReqId, String regularStatusCd, String reqDetailStatusCd) throws Exception {
		return orderRegularDetailMapper.putOrderRegularResultStatusCancel(odRegularReqId, regularStatusCd, reqDetailStatusCd);
	}

	/**
	 * 정기배송 결과 상태 진행중 처리
	 * @param odRegularReqId
	 * @param regularStatusCd
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularResultStatusIng(long odRegularReqId, String regularStatusCd, String reqDetailStatusCd) throws Exception {
		return orderRegularDetailMapper.putOrderRegularResultStatusIng(odRegularReqId, regularStatusCd, reqDetailStatusCd);
	}

	/**
	 * 정기배송 상세 상품 취소 처리
	 * @param odregularReqId
	 * @param reqDetailStatusCd
	 * @param ilGoodsId
	 * @throws Exception
	 */
	protected int putOrderRegularReqDetailGoodsCancel(long odregularReqId, String reqDetailStatusCd, long ilGoodsId) throws Exception {
		return orderRegularDetailMapper.putOrderRegularReqDetailGoodsCancel(odregularReqId, reqDetailStatusCd, ilGoodsId);
	}

	/**
	 * 정기배송 신청 상태 해지 처리
	 * @param odRegularReqId
	 * @param regularStatusCd
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularReqStatusCancel(long odRegularReqId, String regularStatusCd, String reqDetailStatusCd) throws Exception {
		return orderRegularDetailMapper.putOrderRegularReqStatusCancel(odRegularReqId, regularStatusCd, reqDetailStatusCd);
	}

	/**
	 * 정기결제 카드 정보 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	protected RegularReqPaymentCardDto getRegularPaymentCardInfo(long urUserId) throws Exception {
		return orderRegularDetailMapper.getRegularPaymentCardInfo(urUserId);
	}

	/**
	 * 정기결제 카드 정보 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	protected RegularReqPaymentCardDto getRegularNextPaymentDtInfo(long urUserId, String regularStatusCd) throws Exception {
		return orderRegularDetailMapper.getRegularNextPaymentDtInfo(urUserId, regularStatusCd);
	}

	/**
	 * 정기결제 카드 정보 삭제
	 * @param odRegularPaymentKeyId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	protected int delOrderRegularPaymentCardInfo(long odRegularPaymentKeyId, long urUserId) throws Exception {
		return orderRegularDetailMapper.delOrderRegularPaymentCardInfo(odRegularPaymentKeyId, urUserId);
	}

	/**
	 * 정기배송 다음 회차 내역 중 건너뛰기 내역 정보 존재 여부 확인
	 * @param odRegularReqId
	 * @param reqDetailStatusCd
	 * @return
	 * @throws Exception
	 */
	protected RegularResultNextReqRoundSkipResultDto getOrderRegularResultNextGoodsSkipInfo(long odRegularReqId, String reqDetailStatusCd) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultNextGoodsSkipInfo(odRegularReqId, reqDetailStatusCd);
	}

	/**
	 * 정기배송 결과 상세 정보 얻기
	 * @param odRegularResultDetlId
	 * @return
	 * @throws Exception
	 */
	protected RegularResultGoodsDetailInfoDto getOrderRegularResultDetailGoodsInfo(long odRegularResultDetlId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultDetailGoodsInfo(odRegularResultDetlId);
	}

	/**
	 * 정기배송 결과 다음 회차 PK 얻기
	 * @param regularResultGoodsDetailInfo
	 * @return
	 * @throws Exception
	 */
	protected RegularResultGoodsDetailInfoDto getOrderRegularResultNextResultId(RegularResultGoodsDetailInfoDto regularResultGoodsDetailInfo) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultNextResultId(regularResultGoodsDetailInfo);
	}

	/**
	 * 정기배송 추가 할인 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected RegularResultGoodsDetailInfoDto getOrderRegularResultAddDiscountInfo(long odRegularReqId) throws Exception {
		return orderRegularDetailMapper.getOrderRegularResultAddDiscountInfo(odRegularReqId);
	}
}
