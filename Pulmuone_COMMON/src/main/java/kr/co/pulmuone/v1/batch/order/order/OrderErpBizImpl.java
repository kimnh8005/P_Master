package kr.co.pulmuone.v1.batch.order.order;

import kr.co.pulmuone.v1.batch.order.dto.ErpIfRequestDto;
import kr.co.pulmuone.v1.batch.order.factory.ErpApiServiceFactory;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderErpBizImpl implements OrderErpBiz {

	@Autowired
	private final ErpApiExchangeService erpApiExchangeService;

	@Autowired
	private final ErpApiServiceFactory erpServiceFactory;

	@Autowired
	private final OrderErpService orderErpService;

	/**
	 * 주문|취소 입력 ERP API 송신
	 * @param erpApiEnums
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType erpApiEnums) throws BaseException {

		// 주문 데이터 리스트 조회
		List<?> headerGroupList = erpServiceFactory.getErpOrderList(erpApiEnums);
		log.info("=====headerGroupList: " + headerGroupList);

		// 주문별 헤더 LOOP 처리
		headerGroupListLoopProcess(erpApiEnums, headerGroupList);

	}

	/**
	 * 주문|취소 입력 ERP API 송신 (외부몰 분리)
	 * @param erpApiEnums
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType erpApiEnums, String omSellersId) throws BaseException {

		String[] omSellersIds = omSellersId.split(",");
		List<String> omSellersIdList = Arrays.asList(omSellersIds);

		// 주문 데이터 리스트 조회
		List<?> headerGroupList = erpServiceFactory.getErpOrderList(erpApiEnums, omSellersIdList);
		log.info("=====headerGroupList: " + headerGroupList);

		// 주문별 헤더 LOOP 처리
		headerGroupListLoopProcess(erpApiEnums, headerGroupList);

	}

	/**
	 * 주문|취소 입력 ERP API 송신 (외부몰 분리)
	 * @param erpApiEnums
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType erpApiEnums, List<String> omSellersIdList) throws BaseException {

		// 주문 데이터 리스트 조회
		List<?> headerGroupList = erpServiceFactory.getErpOrderList(erpApiEnums, omSellersIdList);
		log.info("=====headerGroupList: " + headerGroupList);

		// 주문별 헤더 LOOP 처리
		headerGroupListLoopProcess(erpApiEnums, headerGroupList);

	}

	/**
	 * 주문별 헤더 LOOP 처리
	 * @param erpApiEnums
	 * @param headerGroupList
	 * @throws BaseException
	 */
	private void headerGroupListLoopProcess(ErpApiEnums.ErpServiceType erpApiEnums, List<?> headerGroupList) throws BaseException {

		// ERP INTERFACE ID
		String erpInterfaceId = erpServiceFactory.getErpInterfaceId(erpApiEnums);

		// 주문별 LOOP
		for(int i=0; i<headerGroupList.size(); i++){

			Map<String, Object> headerList = (Map) headerGroupList.get(i);
			List<?> linelist = (List<?>) headerList.get(i);
			log.info("=====linelist: " + linelist);

			// line 생성
			List<?> lineBindList = erpServiceFactory.getErpApiLineList(erpApiEnums, linelist);
			log.info("=====lineBindList: " + lineBindList);

			if(lineBindList != null && lineBindList.size() > 0) {

				// header 생성
				List<?> headerBindList = erpServiceFactory.getErpApiHeaderList(erpApiEnums, linelist.get(0), lineBindList);
				log.info("=====headerBindList: " + headerBindList);

                // ERP API 송신
                sendErpApi(headerBindList, erpInterfaceId, erpApiEnums, linelist);

			}
		}
	}

    /**
     * 기타주문 입력 ERP API 송신
     * @param erpApiEnums
     * @return void
     * @throws BaseException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
    public void addEtcOrderIfInpByErp(ErpApiEnums.ErpServiceType erpApiEnums) throws BaseException {

        // 주문 데이터 리스트 조회
        List<?> headerList = erpServiceFactory.getErpOrderList(erpApiEnums);
        log.info("=====headerList: " + headerList);

		// 기타 주문별 헤더 LOOP 처리
		etcHeaderGroupListLoopProcess(erpApiEnums, headerList);

    }

	/**
	 * 기타주문 입력 ERP API 송신 (외부몰 분리)
	 * @param erpApiEnums
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void addEtcOrderIfInpByErp(ErpApiEnums.ErpServiceType erpApiEnums, String omSellersId) throws BaseException {

		String[] omSellersIds = omSellersId.split(",");
		List<String> omSellersIdList = Arrays.asList(omSellersIds);

		// 주문 데이터 리스트 조회
		List<?> headerList = erpServiceFactory.getErpOrderList(erpApiEnums, omSellersIdList);
		log.info("=====headerList: " + headerList);

		// 기타 주문별 헤더 LOOP 처리
		etcHeaderGroupListLoopProcess(erpApiEnums, headerList);

	}

	/**
	 * 기타주문 입력 ERP API 송신 (외부몰 분리)
	 * @param erpApiEnums
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void addEtcOrderIfInpByErp(ErpApiEnums.ErpServiceType erpApiEnums, List<String> omSellersIdList) throws BaseException {

		// 주문 데이터 리스트 조회
		List<?> headerList = erpServiceFactory.getErpOrderList(erpApiEnums, omSellersIdList);
		log.info("=====headerList: " + headerList);

		// 기타 주문별 헤더 LOOP 처리
		etcHeaderGroupListLoopProcess(erpApiEnums, headerList);

	}

	/**
	 * 기타 주문별 헤더 LOOP 처리
	 * @param erpApiEnums
	 * @param headerGroupList
	 * @throws BaseException
	 */
	private void etcHeaderGroupListLoopProcess(ErpApiEnums.ErpServiceType erpApiEnums, List<?> headerList) throws BaseException {

		// ERP INTERFACE ID
		String erpInterfaceId = erpServiceFactory.getErpInterfaceId(erpApiEnums);

		// 주문별 LOOP
		for(int i=0; i<headerList.size(); i++) {

			// header 생성
			List<?> headerBindList = erpServiceFactory.getErpApiHeaderList(erpApiEnums, headerList.get(i), null);
			log.info("=====headerBindList: " + headerBindList);

			// ERP API 송신
			etcSendErpApi(headerBindList, erpInterfaceId, erpApiEnums, headerList.get(i));
		}
	}

	/**
	 * ERP API 송신
	 * @param headerBindList
	 * @param erpInterfaceId
	 * @param erpApiEnums
	 * @return void
	 * @throws
	 */
	protected void sendErpApi(List<?> headerBindList, String erpInterfaceId, ErpApiEnums.ErpServiceType erpApiEnums, List<?> linelist) throws BaseException {

		// request 생성
		ErpIfRequestDto requestDto =  ErpIfRequestDto.builder()
				.totalPage(1)
				.currentPage(1)
				.header(headerBindList)
				.build();

		// ERP API 송신
		BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.post(requestDto, erpInterfaceId);
		log.info("=====baseApiResponseVo: " + baseApiResponseVo);

		// 실패시 재시도 총 3회
		boolean isSuccess = erpApiFailRetry(baseApiResponseVo, requestDto, erpInterfaceId);

		// 성공 시 주문 배치완료 업데이트
		if(isSuccess) erpServiceFactory.putErpOrderBatchCompleteUpdate(erpApiEnums, linelist);

	}

    /**
     * 기타 주문 ERP API 송신
     * @param headerBindList
     * @param erpInterfaceId
     * @param erpApiEnums
     * @return void
     * @throws
     */
    protected void etcSendErpApi(List<?> headerBindList, String erpInterfaceId, ErpApiEnums.ErpServiceType erpApiEnums, Object lineItem) throws BaseException {

        // request 생성
        ErpIfRequestDto requestDto =  ErpIfRequestDto.builder()
                                                     .totalPage(1)
                                                     .currentPage(1)
                                                     .header(headerBindList)
                                                     .build();

        // ERP API 송신
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.post(requestDto, erpInterfaceId);
        log.info("=====baseApiResponseVo: " + baseApiResponseVo);

        // 실패시 재시도 총 3회
        boolean isSuccess = erpApiFailRetry(baseApiResponseVo, requestDto, erpInterfaceId);

        // 성공 시 주문 배치완료 업데이트
        if(isSuccess) erpServiceFactory.putErpOrderBatchCompleteOneUpdate(erpApiEnums, lineItem);

    }

	/**
	 * ERP API 송신 실패시 재시도 총 3회
	 * @param baseApiResponseVo
	 * @param custOrdRequestDto
	 * @param erpInterfaceId
	 * @return void
	 * @throws
	 */
	protected boolean erpApiFailRetry(BaseApiResponseVo baseApiResponseVo, Object custOrdRequestDto, String erpInterfaceId) {

		boolean isSuccess = baseApiResponseVo.isSuccess();

		// 실패
		if(!baseApiResponseVo.isSuccess()) {

			for (int failCnt = 0; failCnt < BatchConstants.BATCH_FAIL_RETRY_COUNT; failCnt++) {

				BaseApiResponseVo retryBaseApiResponseVo = erpApiExchangeService.post(custOrdRequestDto, erpInterfaceId);

				isSuccess = retryBaseApiResponseVo.isSuccess();

				// 성공
				if (retryBaseApiResponseVo.isSuccess()) {
					log.info("=====baseApiResponseVo: " + baseApiResponseVo);
					break;
				}
				// 3번 재시도 모두 실패
				else {
					log.info("=====baseApiResponseVo: "+ failCnt + " " + baseApiResponseVo);
					if(failCnt == BatchConstants.BATCH_FAIL_RETRY_COUNT-1) {
						// 실패알람
						// TODO SMS, Slack 작업필요
					}
				}

			}



		}

		return isSuccess;
	}

	/**
	 * 연동 셀러 리스
	 * @return string
	 * @throws
	 */
	public List<String> getErpOutMallIfSellerList(String erpInterFaceYn) throws BaseException {

		return orderErpService.getErpOutMallIfSellerList(erpInterFaceYn);
	}
}