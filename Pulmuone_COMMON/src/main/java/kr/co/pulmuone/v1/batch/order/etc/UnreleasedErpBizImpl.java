package kr.co.pulmuone.v1.batch.order.etc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.order.etc.dto.vo.UnreleasedInfoVo;
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
 * 미출조회 ERP API배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UnreleasedErpBizImpl implements UnreleasedErpBiz {

	@Autowired
	private UnreleasedErpService unreleasedErpService;

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

    /**
	 * 미출 조회/저장 배치
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runUnreleasedSetUp() throws Exception {
		List<UnreleasedInfoVo> unreleasedList = (List<UnreleasedInfoVo>) getUnreleasedList();
		unreleasedErpService.putUnreleasedJob(unreleasedList);
	}

	/**
	 * 미출정보 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	private Object getUnreleasedList() throws Exception {
		// 전체 송장정보 리스트
		List<UnreleasedInfoVo> totalUnreleasedList = new ArrayList<UnreleasedInfoVo>();

		BaseApiResponseVo baseApiResponseVo = getUnreleasedPage(null);
		totalUnreleasedList.addAll(baseApiResponseVo.deserialize(UnreleasedInfoVo.class));

		int startPage = baseApiResponseVo.getCurrentPage();	// 최초 조회한 페이지 (1 페이지)
		int totalPage = baseApiResponseVo.getTotalPage(); 	// 해당 검색조건으로 조회시 전체 페이지 수

		if (totalPage > 1) {
			// 최초 조회한 페이지의 다음 페이지부터 조회
			for (int pageNo = startPage + 1; pageNo <= totalPage; pageNo++) {
				baseApiResponseVo = getUnreleasedPage(String.valueOf(pageNo));
				totalUnreleasedList.addAll(baseApiResponseVo.deserialize(UnreleasedInfoVo.class));
			}
		}

		return totalUnreleasedList;
	}

	/**
	 * 페이지별 미출정보 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	private BaseApiResponseVo getUnreleasedPage(String pageNo) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("page", pageNo != null ? String.valueOf(pageNo) : null);
		paramMap.put("srcSvr"	, SourceServerTypes.ESHOP.getCode());	// 입력 시스템 코드 ESHOP
		paramMap.put("itfMisFlg", BatchConstants.ITF_DLV_FLG);			// 미출 인터페이스 수신여부
		paramMap.put("misUpdDat",
				DateUtil.addDays(DateUtil.getCurrentDate(), BatchConstants.ITF_MIS_DATE_FROM_TERM, "yyyyMMdd") + BatchConstants.ITF_MIS_DATE_FROM_TIME + "~" +
				DateUtil.getCurrentDate() + BatchConstants.ITF_MIS_DATE_TO_TIME); // I/F 조회기간
		return erpApiExchangeService.get(paramMap, ErpApiEnums.ErpInterfaceId.UNRELEASED_SEARCH_INTERFACE_ID.getCode());
	}
}