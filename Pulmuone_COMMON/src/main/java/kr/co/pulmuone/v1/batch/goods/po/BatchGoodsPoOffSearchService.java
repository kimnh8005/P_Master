package kr.co.pulmuone.v1.batch.goods.po;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.goods.po.dto.ErpIfPoOffSearchRequestDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPoOffSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.po.BatchGoodsPoOffSearchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 올가오프라인발주상태 조회 배치 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.02.16     이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchGoodsPoOffSearchService {

    // 올가오프라인발주상태 조회 완료 인터페이스 ID
    private static final String PO_OFF_FLAG_INTERFACE_ID = "IF_ORGAOFFLINE_FLAG";

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

	@Autowired
    private BatchGoodsPoOffSearchMapper batchPoOffSearchMapper;

	protected void runGoodsPoOffSearchJob(List<ErpIfPoOffSearchResponseDto> getErpPoSearchList, long batchId) throws BaseException {

		List<ErpIfPoOffSearchRequestDto> ilItemCdList = new ArrayList<>();
		int size = getErpPoSearchList.size();

		for(int i = 0; i < size; i++) {
			String ilItemCd = getErpPoSearchList.get(i).getIlItemCd();//품목코드
			String poProRea = getErpPoSearchList.get(i).getPoProRea();//발주상태

			String orgIlItemCd = getIlItemCd(ilItemCd);

			// 올가 오프라인발주상태 수정
			if(orgIlItemCd != null && !orgIlItemCd.equals("")) {
				if(poProRea != null && !poProRea.equals("")) {
					putIlItem(orgIlItemCd, poProRea);
				}else {
					putIlItem(orgIlItemCd, poProRea);
				}

				ErpIfPoOffSearchRequestDto dto = new ErpIfPoOffSearchRequestDto();
				dto.setItmCd(orgIlItemCd);

				ilItemCdList.add(dto);
			}
		}

		try {
			if(!ilItemCdList.isEmpty()){
				putPoOffSearch(ilItemCdList);		//올가오프라인발주상태 조회 완료 Flag처리
			}
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
	}

	/**
     * @Desc 품목코드 조회
     * @param string
     * @return string
     */
	protected String getIlItemCd(String ilItemCd) {
		return batchPoOffSearchMapper.getIlItemCd(ilItemCd);
	}

	/**
     * @Desc 품목 발주상태 수정
     * @param string
     * @return int
     */
	protected int putIlItem(String orgIlItemCd, String poProRea) {
		return batchPoOffSearchMapper.putIlItem(orgIlItemCd, poProRea);
	}

	/**
     * 올가오프라인발주상태 조회 완료 Flag
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo putPoOffSearch(List<ErpIfPoOffSearchRequestDto> ilItemCdList) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(ilItemCdList, PO_OFF_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        // 별도 확인 필요 : BOS 상에 PO_PER_REA수정은 성공했으나, ERP API 올가오프라인발주상태 조회 완료 업데이트 API 호출 실패 case
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}

}
