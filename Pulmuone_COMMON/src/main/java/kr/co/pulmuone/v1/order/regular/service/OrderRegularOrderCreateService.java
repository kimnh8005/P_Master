package kr.co.pulmuone.v1.order.regular.service;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.order.regular.OrderRegularOrderCreateMapper;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문생성 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.09.23	  김명진           최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRegularOrderCreateService {

	private final OrderRegularOrderCreateMapper orderRegularOrderCreateMapper;

	/**
	 * 정기배송 주문 결과 주문 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	protected List<RegularResultCreateOrderListDto> getRegularOrderResultCreateGoodsList(RegularResultCreateOrderListRequestDto regularResultCreateOrderListRequestDto) throws BaseException {
		return orderRegularOrderCreateMapper.getRegularOrderResultCreateGoodsList(regularResultCreateOrderListRequestDto);
	}

	/**
	 * 정기배송 추가할인 기준 회차 정보 조회
	 * @param odRegularResultId
	 * @return
	 * @throws Exception
	 */
	protected int getAddDiscountStdReqRound(long odRegularResultId) {
		return orderRegularOrderCreateMapper.getAddDiscountStdReqRound(odRegularResultId);
	}

	/**
	 * 정기배송 상세 상품 판매상태 업데이트
	 * @param odRegularResultId
	 * @param saleStatus
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularDetlGoodsSaleStatus(long odRegularResultId, String saleStatus) throws Exception {
		return orderRegularOrderCreateMapper.putOrderRegularDetlGoodsSaleStatus(odRegularResultId, saleStatus);
	}

    /**
     * 정기배송 주문 신청 상태 변경
     * @param odRegularResultId
     * @param regularStatusCd
     * @return
     * @throws Exception
     */
    protected int putRegularOrderReqStatus(long odRegularResultId, String regularStatusCd) throws Exception {
    	return orderRegularOrderCreateMapper.putRegularOrderReqStatus(odRegularResultId, regularStatusCd);
    }

    /**
     * 정기배송 결과 상태 변경
     * @param odRegularResultId
     * @param regularStatusCd
     * @return
     * @throws Exception
     */
    protected int putRegularOrderResultStatus(long odRegularResultId, String regularStatusCd) throws Exception {
    	return orderRegularOrderCreateMapper.putRegularOrderResultStatus(odRegularResultId, regularStatusCd);
    }

	/**
	 * 정기배송 상세 상품 판매상태 업데이트
	 * @param odRegularResultId
	 * @param notOnSaleGoodsList
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularDetlGoodsSaleStatusByGoodsList(long odRegularResultId, List<RegularResultCreateOrderListDto> notOnSaleGoodsList) throws Exception {
		return orderRegularOrderCreateMapper.putOrderRegularDetlGoodsSaleStatusByGoodsList(odRegularResultId, notOnSaleGoodsList);
	}

    /**
     * 정기배송 주문 결과 주문서생성 여부 변경
     * @return
     * @throws Exception
     */
    protected int putRegularResultOrderCreateYn(long odRegularResultId, String odid) throws BaseException {
    	return orderRegularOrderCreateMapper.putRegularResultOrderCreateYn(odRegularResultId, odid);
    }

    /**
     * 정기배송 신청정보 조회 - ODID 기준
     * @return
     * @throws Exception
     */
    protected RegularReqCreateOrderListDto getRegularOrderReqInfo(long odRegularResultId) throws BaseException {
    	return orderRegularOrderCreateMapper.getRegularOrderReqInfo(odRegularResultId);
    }


	/**
	 * 정기배송 입금전 취소 처리 파라미터 Set
	 * @param ilGoodsIds
	 * @param odid
	 * @throws Exception
	 */
	protected OrderClaimRegisterRequestDto putOdRegularIncomBeforeCancelComplete(List<Long> ilGoodsIds, String odid, String cancelType) throws Exception {
		OrderClaimRegisterRequestDto claimReqInfo = null;
		if(CollectionUtils.isNotEmpty(ilGoodsIds)) {
			// 주문정보 조회
			claimReqInfo = orderRegularOrderCreateMapper.getOdOrderInfoByOdid(odid);
			// 나머지 정보 Set
			claimReqInfo.setTargetTp(OrderClaimEnums.ClaimTargetTp.TARGET_SELLER.getCode());                    // 귀책 구분 : 판매자귀책
			claimReqInfo.setReturnsYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());                        	// 회수 여부 : 회수안함
			claimReqInfo.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode());                      // 클레임상태구분 취소
			claimReqInfo.setClaimStatusCd(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode());       // 클레임상태코드 입금전취소
			claimReqInfo.setPsClaimMallId(0);
			// 주문 상세정보 조회
			List<OrderClaimGoodsInfoDto> goodsInfoList = orderRegularOrderCreateMapper.getOrderDetlInfoListByNotOnSaleGoodsList(odid, ilGoodsIds);
			claimReqInfo.setGoodsInfoList(goodsInfoList);
			claimReqInfo.setUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
			claimReqInfo.setCustomUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
			claimReqInfo.setFrontTp(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BATCH.getCodeValue());
			claimReqInfo.setRegularOrderIbFlag((OrderEnums.RegularOrderBatchTypeCd.CREATE_ORDER.getCode().equals(cancelType) ? OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode() : OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode()));
		}
		return claimReqInfo;
	}
}
