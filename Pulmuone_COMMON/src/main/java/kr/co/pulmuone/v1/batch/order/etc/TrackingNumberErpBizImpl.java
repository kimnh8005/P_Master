package kr.co.pulmuone.v1.batch.order.etc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.order.etc.dto.vo.TrackingNumberEtcInfoVo;
import kr.co.pulmuone.v1.batch.order.etc.dto.vo.TrackingNumberInfoVo;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 송장조회 ERP API배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingNumberErpBizImpl implements TrackingNumberErpBiz {

	@Autowired
    private TrackingNumberErpService trackingNumberErpService;

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

	/**
	 * 송장 조회/저장 배치
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runTrackingNumberSetUp() throws BaseException {
		List<TrackingNumberInfoVo> trackingNumberList = (List<TrackingNumberInfoVo>) getTrackingNumberList(ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_SEARCH_INTERFACE_ID.getCode());
		trackingNumberErpService.putTrackingNumberJob(trackingNumberList);
	}
	
	/**
	 * 하이톡 확정가맹점 송장 조회/저장 배치
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runHitokTrackingNumberSetUp() throws BaseException {
		List<TrackingNumberInfoVo> trackingNumberList = (List<TrackingNumberInfoVo>) getHitokTrackingNumberList(ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_SEARCH_INTERFACE_ID.getCode());
		trackingNumberErpService.putTrackingNumberJob(trackingNumberList);
	}

	/**
	 * 기타송장 조회/저장 배치
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runEtcTrackingNumberSetUp() throws BaseException {
		List<TrackingNumberEtcInfoVo> trackingNumberList = (List<TrackingNumberEtcInfoVo>) getTrackingNumberList(ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_ETC_SEARCH_INTERFACE_ID.getCode());
		trackingNumberErpService.putTrackingNumberEtcJob(trackingNumberList);
	}

	/**
	 * 송장정보 or 기타송장정보 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@SuppressWarnings("unchecked")
	private Object getTrackingNumberList(String ifId) throws BaseException {
		// 전체 송장정보 리스트
		List<?> totalTrackingNumberList = null;

		try {
			BaseApiResponseVo baseApiResponseVo = getTrackingNumberPage(ifId, null, SourceServerTypes.ESHOP.getCode());
			
			// 송장조회
			if (ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_SEARCH_INTERFACE_ID.getCode().equals(ifId)) {
				totalTrackingNumberList = new ArrayList<TrackingNumberInfoVo>();
				((List<TrackingNumberInfoVo>) totalTrackingNumberList).addAll(baseApiResponseVo.deserialize(TrackingNumberInfoVo.class));
			// 기타송장 조회
			} else if (ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_ETC_SEARCH_INTERFACE_ID.getCode().equals(ifId)) {
				totalTrackingNumberList = new ArrayList<TrackingNumberEtcInfoVo>();
				((List<TrackingNumberEtcInfoVo>) totalTrackingNumberList).addAll(baseApiResponseVo.deserialize(TrackingNumberEtcInfoVo.class));
			}

			int startPage = baseApiResponseVo.getCurrentPage();	// 최초 조회한 페이지 (1 페이지)
			int totalPage = baseApiResponseVo.getTotalPage(); 	// 해당 검색조건으로 조회시 전체 페이지 수

			if (totalPage > 1) {
				// 최초 조회한 페이지의 다음 페이지부터 조회
				for (int pageNo = startPage + 1; pageNo <= totalPage; pageNo++) {
					baseApiResponseVo = getTrackingNumberPage(ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_SEARCH_INTERFACE_ID.getCode(),String.valueOf(pageNo), SourceServerTypes.ESHOP.getCode());

					// 송장/기타 송장 분기처리
					if (ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_SEARCH_INTERFACE_ID.getCode().equals(ifId)) {
						((List<TrackingNumberInfoVo>) totalTrackingNumberList).addAll(baseApiResponseVo.deserialize(TrackingNumberInfoVo.class));
					} else if (ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_ETC_SEARCH_INTERFACE_ID.getCode().equals(ifId)) {
						((List<TrackingNumberEtcInfoVo>) totalTrackingNumberList).addAll(baseApiResponseVo.deserialize(TrackingNumberEtcInfoVo.class));
					}
				}
			}
		} catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
			throw new BaseException(e.getMessage());
		}
		return totalTrackingNumberList;
	}
	
	/**
	 * 송장정보 or 기타송장정보 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	private Object getHitokTrackingNumberList(String ifId) throws BaseException {
		// 전체 송장정보 리스트
		List<?> totalTrackingNumberList = null;
		
		try {
			BaseApiResponseVo baseApiResponseVo = getTrackingNumberPage(ifId, null, SourceServerTypes.HITOK.getCode());
			
			totalTrackingNumberList = new ArrayList<TrackingNumberInfoVo>();
			((List<TrackingNumberInfoVo>) totalTrackingNumberList).addAll(baseApiResponseVo.deserialize(TrackingNumberInfoVo.class));
			
			int startPage = baseApiResponseVo.getCurrentPage();	// 최초 조회한 페이지 (1 페이지)
			int totalPage = baseApiResponseVo.getTotalPage(); 	// 해당 검색조건으로 조회시 전체 페이지 수
			
			if (totalPage > 1) {
				// 최초 조회한 페이지의 다음 페이지부터 조회
				for (int pageNo = startPage + 1; pageNo <= totalPage; pageNo++) {
					baseApiResponseVo = getTrackingNumberPage(ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_SEARCH_INTERFACE_ID.getCode(),String.valueOf(pageNo), SourceServerTypes.HITOK.getCode());
					
					((List<TrackingNumberInfoVo>) totalTrackingNumberList).addAll(baseApiResponseVo.deserialize(TrackingNumberInfoVo.class));
					
				}
			}
		} catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
			throw new BaseException(e.getMessage());
		}
		return totalTrackingNumberList;
	}

	/**
	 * 페이지별 송장정보or기타송장정보 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	private BaseApiResponseVo getTrackingNumberPage(String ifId, String pageNo, String srcSvrCd) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("page", pageNo != null ? String.valueOf(pageNo) : null);
		paramMap.put("srcSvr"	, srcSvrCd);	// 입력 시스템 코드 ESHOP
		paramMap.put("itfDlvFlg", BatchConstants.ITF_DLV_FLG);			// 송장처리 인터페이스 수신여부
		paramMap.put("dlvUpdDat",
				DateUtil.addDays(DateUtil.getCurrentDate(), BatchConstants.ITF_DLV_DATE_FROM_TERM, "yyyyMMdd") + BatchConstants.ITF_DLV_DATE_FROM_TIME + "~" +
				DateUtil.getCurrentDate() + BatchConstants.ITF_DLV_DATE_TO_TIME); // I/F 조회기간
		return erpApiExchangeService.get(paramMap, ifId);
	}

	/**
	 * 클레임 취소거부 처리
	 * @param odid
	 * @param odClaimId
	 * @throws Exception
	 */
	@Override
	public void procClaimDenyDefer(String odid, long odClaimId) throws Exception {
		trackingNumberErpService.procClaimDenyDefer(odid, odClaimId);
	}
}