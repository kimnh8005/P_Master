package kr.co.pulmuone.v1.batch.order.order;

import kr.co.pulmuone.v1.batch.order.order.dto.OrderClaimAddShippingPriceListDto;
import kr.co.pulmuone.v1.batch.order.order.dto.VirtualBankOrderCancelDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimViewRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimViewResponseDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.claim.service.ClaimRequestBiz;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordSearchResponseDto;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <PRE>
 * Forbiz Korea
 * 녹즙 동기화 배치 Biz Impl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GreenJuiceSyncBizImpl implements GreenJuiceSyncBiz {

	@Autowired
	private GreenJuiceSyncService greenJuiceSyncService;

	/**
	 * 녹즙 동기화
	 * @param
	 * @return void
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, BaseException.class})
	public void runGreenJuiceSync() throws Exception {

		log.debug("######## 녹즙 동기화 START ########");
		log.debug("======== 녹즙 변경 내역 정보 조회 ========");
		// 1. 녹즙 변경 내역 정보 조회
		List<ErpIfCustordSearchResponseDto> erpCustordApiList = greenJuiceSyncService.getErpCustordApiList();

		// 녹즙 변경 내역 정보가 존재 할 경우
		if(!erpCustordApiList.isEmpty()) {
			log.debug("-------- 녹즙 변경 내역 정보 존재 --------");
			log.debug("======== 녹즙 변경 내역 완료 처리 ========");
			BaseApiResponseVo baseApiResponseVo = greenJuiceSyncService.putErpCustordApiComplete(erpCustordApiList);
			if(baseApiResponseVo.isSuccess()) {
				log.debug("-------- 녹즙 변경 내역 완료 처리 성공 --------");
				log.debug("======== 녹즙 변경 내역 동기화 처리 ========");
				greenJuiceSyncService.putGreenJuiceSync(erpCustordApiList);
			}
		}
		log.debug("######## 녹즙 동기화 END ########");
	}
}