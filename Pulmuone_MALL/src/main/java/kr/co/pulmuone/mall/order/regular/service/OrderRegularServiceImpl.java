package kr.co.pulmuone.mall.order.regular.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.ApplyRegularBatchKey;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.UidUtil;
import kr.co.pulmuone.v1.order.regular.dto.ApplyRegularBatchKeyResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.MallRegularReqInfoResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqGoodsListRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqShippingZoneDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultReqRoundListDto;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularBiz;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularDetailBiz;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyDataRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyDataResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.service.KcpPgService;
import lombok.extern.slf4j.Slf4j;

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
 *  1.0    20200929   	 홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class OrderRegularServiceImpl implements OrderRegularService {

	@Autowired
	public PgBiz pgBiz;

	@Autowired
	public KcpPgService kcpPgService;

	@Autowired
	public OrderRegularBiz orderRegularBiz;

	@Autowired
	public OrderRegularDetailBiz orderRegularDetailBiz;

	/**
	 * 정기 결제 카드 등록
	 *
	 * @param goodsSearchRequestDto
	 * @return GetSearchgoodsListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> applyRegularBatchKey(int paymentPrice, String orderInputUrl) throws Exception {

		ApplyRegularBatchKeyResponseDto resDto = new ApplyRegularBatchKeyResponseDto();

		KcpApplyRegularBatchKeyDataRequestDto batchKeyReqDto = new KcpApplyRegularBatchKeyDataRequestDto();

        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
			// DOTO 에러 코드 정의 필요
			return ApiResult.result(ApplyRegularBatchKey.NEED_LOGIN);
		}

		RegularReqGoodsListRequestDto regularReqGoodsListRequestDto = new RegularReqGoodsListRequestDto();
		regularReqGoodsListRequestDto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));

		// 회차 정보 얻어서 결제 금액 조회
		MallRegularReqInfoResponseDto mallRegularReqInfoResponseDto = orderRegularBiz.getOrderRegularReqInfo(regularReqGoodsListRequestDto);
		if(!Objects.isNull(mallRegularReqInfoResponseDto)) {
			List<RegularResultReqRoundListDto> reqRoundList = mallRegularReqInfoResponseDto.getReqRoundList();
			if(!reqRoundList.isEmpty()) {
				paymentPrice = reqRoundList.get(0).getPaidPrice();
			}
		}

		batchKeyReqDto.setOdid(UidUtil.randomUUID().toString());
		batchKeyReqDto.setUrUserId(buyerVo.getUrUserId());
		batchKeyReqDto.setBuyerName(buyerVo.getUserName());
		batchKeyReqDto.setPaymentPrice(paymentPrice);
		batchKeyReqDto.setOrderInputUrl(orderInputUrl);
		batchKeyReqDto.setGoodsName("정기결제 키발급");

		KcpApplyRegularBatchKeyDataResponseDto batchKeyResDto = kcpPgService.applyRegularBatchKeyData(batchKeyReqDto);

		resDto.setExeScriptType(batchKeyResDto.getExeScriptType());
		resDto.setPgFormDataList(batchKeyResDto.getPgFormDataList());

		return ApiResult.success(resDto);
	}

	/**
	 * 정기결제 카드 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getRegularPaymentCardInfo(long urUserId) throws Exception {
		return ApiResult.success(orderRegularDetailBiz.getRegularPaymentCardInfo(urUserId));
	}

	/**
	 * 정기결제 카드 삭제
	 * @param odRegularPaymentKeyId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> delOrderRegularPaymentCardInfo(long odRegularPaymentKeyId, long urUserId) throws Exception {
		orderRegularDetailBiz.delOrderRegularPaymentCardInfo(odRegularPaymentKeyId, urUserId);
		return ApiResult.success();
	}

	/**
	 * 정기배송 주문 목록 조회
	 * @param regularReqGoodsListRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularList(RegularReqGoodsListRequestDto regularReqGoodsListRequestDto) throws Exception {
		return ApiResult.success(orderRegularBiz.getOrderRegularReqInfo(regularReqGoodsListRequestDto));
	}

	/**
	 * 정기배송 주기 요일 변경 정보 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularDaysInfo(long odRegularReqId) throws Exception {
		return ApiResult.success(orderRegularDetailBiz.getOrderRegularDaysInfo(odRegularReqId));
	}

	/**
	 * 정기배송 주기 요일 변경 도착일 목록 조회
	 * @param odRegularReqId
	 * @param goodsCycleTp
	 * @param weekCd
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularArriveDtList(long odRegularReqId, String goodsCycleTp, String weekCd) throws Exception {
		return ApiResult.success(orderRegularDetailBiz.getOrderRegularArriveDtList(odRegularReqId, goodsCycleTp, weekCd));
	}

	/**
	 * 정기배송 주기 요일 변경
	 * @param odRegularReqId
	 * @param goodsCycleTp
	 * @param weekCd
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularCycleDays(long odRegularReqId, String goodsCycleTp, String weekCd, long urUserId) throws Exception {
		orderRegularDetailBiz.putOrderRegularCycleDays(odRegularReqId, goodsCycleTp, weekCd, urUserId);
		return ApiResult.success();
	}

	/**
	 * 정기배송 배송지 변경
	 * @param regularReqShippingZoneDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto, long urUserId) throws Exception {
		orderRegularDetailBiz.putOrderRegularShippingZone(regularReqShippingZoneDto, urUserId);
		return ApiResult.success();
	}

	/**
	 * 정기배송 기간연장
	 * @param odRegularReqId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularGoodsCycleTermExtension(long odRegularReqId, long urUserId) throws Exception {
		orderRegularDetailBiz.putOrderRegularGoodsCycleTermExtension(odRegularReqId, urUserId);
		return ApiResult.success();
	}

	/**
	 * 정기배송 상품 취소/건너뛰기 정보 조회
	 * @param odRegularResultDetlId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularGoodsSkipCancelInfo(long odRegularResultDetlId) throws Exception {
		return ApiResult.success(orderRegularDetailBiz.getOrderRegularGoodsSkipCancelInfo(odRegularResultDetlId));
	}

	/**
	 * 정기배송 회차 건너뛰기
	 * @param odRegularResultId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularReqRoundSkip(long odRegularResultId, long urUserId) throws Exception {
		orderRegularDetailBiz.putOrderRegularReqRoundSkip(odRegularResultId, urUserId);
		return ApiResult.success();
	}

	/**
	 * 정기배송 회차 건너뛰기 철회
	 * @param odRegularResultId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularReqRoundSkipCancel(long odRegularResultId, long urUserId) throws Exception {
		orderRegularDetailBiz.putOrderRegularReqRoundSkipCancel(odRegularResultId, urUserId);
		return ApiResult.success();
	}

	/**
	 * 정기배송 상품 건너뛰기
	 * @param odRegularResultDetlId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularGoodsSkip(long odRegularResultDetlId, long urUserId) throws Exception {
		orderRegularDetailBiz.putOrderRegularGoodsSkip(odRegularResultDetlId, urUserId);
		return ApiResult.success();
	}

	/**
	 * 정기배송 상품 건너뛰기 철회
	 * @param odRegularResultDetlId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularGoodsSkipCancel(long odRegularResultDetlId, long urUserId) throws Exception {
		orderRegularDetailBiz.putOrderRegularGoodsSkipCancel(odRegularResultDetlId, urUserId);
		return ApiResult.success();
	}

	/**
	 * 정기배송 상품 취소
	 * @param odRegularResultDetlId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularGoodsCancel(long odRegularResultDetlId, long urUserId) throws Exception {
		orderRegularDetailBiz.putOrderRegularGoodsCancel(odRegularResultDetlId, urUserId);
		return ApiResult.success();
	}
}
