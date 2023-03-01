package kr.co.pulmuone.v1.batch.goods.stock;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ItemErpStock3PlSearchResultVo;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfStock3PlSearchRequestDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfStock3PlSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.enums.StockEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.stock.BatchItemErpStock3PlSearchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 3PL재고수량 조회 API 조회 후 BOS 재고 계산 배치 Service
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.11.17    이성준              최초작성
* =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchGoodsStock3PlSearchService {

	//3PL재고수량 조회 완료 인터페이스 ID
    private static final String STOCK_3PL_SEARCH_FLAG_INTERFACE_ID = "IF_STOCK_3PL_FLAG";

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

	@Autowired
    private BatchItemErpStock3PlSearchMapper batchItemErpStock3PlSearchMapper;

	/**
	 * 3PL재고수량 조회
	 * @param erpStock3PlSearchList
	 * @param batchId
	 * @throws BaseException
	 */
	protected void runGoodsStock3PlSearchJob(List<ErpIfStock3PlSearchResponseDto> erpStock3PlSearchList, long batchId) throws BaseException {

		List<ItemErpStock3PlSearchResultVo> resultList = null;

        String psKey = StockEnums.UrWarehouseId.WAREHOUSE_BAEKAM_ID.getCode();

        String urWarehouseId = getConfigValue(psKey);//백암 출고처 조회

			for(ErpIfStock3PlSearchResponseDto dto : erpStock3PlSearchList) {

				if(dto.getIlItemCd() != null && !dto.getIlItemCd().equals("")
					  && dto.getExpirationDt() != null && !dto.getExpirationDt().equals("")
						 && dto.getStockQty() != null && !dto.getStockQty().equals("")) {

			        dto.setUrWarehouseId(Integer.parseInt(urWarehouseId)); //백암 출고처

			        resultList = getStockInfoList(dto);//품목정보 조회

			        for(ItemErpStock3PlSearchResultVo vo : resultList) {

			            addStock3PlSearch(vo); //저장

			        }
				}
			}
	}

	protected void runGoodsStock3PlSearchFlagJob(List<ErpIfStock3PlSearchResponseDto> erpStock3PlSearchList) throws BaseException {

		List<ErpIfStock3PlSearchRequestDto> erpItmNoList = new ArrayList<>();

		List<ItemErpStock3PlSearchResultVo> resultList = null;

        String psKey = StockEnums.UrWarehouseId.WAREHOUSE_BAEKAM_ID.getCode();

        String urWarehouseId = getConfigValue(psKey);//백암 출고처 조회

			for(ErpIfStock3PlSearchResponseDto dto : erpStock3PlSearchList) {

				if(dto.getIlItemCd() != null && !dto.getIlItemCd().equals("")
					  && dto.getExpirationDt() != null && !dto.getExpirationDt().equals("")
						 && dto.getStockQty() != null && !dto.getStockQty().equals("")) {

			        dto.setUrWarehouseId(Integer.parseInt(urWarehouseId)); //백암 출고처

			        resultList = getStockInfoList(dto);//품목정보 조회

			        for(ItemErpStock3PlSearchResultVo vo : resultList) {
			        	ErpIfStock3PlSearchRequestDto flagDto = new ErpIfStock3PlSearchRequestDto();
			        	flagDto.setStrKey(vo.getStrKey());
			        	flagDto.setErpItmNo(vo.getIlItemCd());

			        	erpItmNoList.add(flagDto);
			        }
				}
			}

		 try {
			 //3PL재고 조회 완료 Flag처리
			 if(!erpItmNoList.isEmpty()) {
				put3PlSearchFlag(erpItmNoList);
			 }
			} catch (Exception e) {
				throw new BaseException(e.getMessage());
			}
	}

	/**
     * 3PL재고 수량 조회 완료 Flag
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo put3PlSearchFlag(List<ErpIfStock3PlSearchRequestDto> erpItmNoList) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(erpItmNoList, STOCK_3PL_SEARCH_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo-3PL:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}


	/**
     * @Desc 출고처 조회
     * @param string
     * @return string
     */
	protected String getConfigValue(String psKey) {
		return batchItemErpStock3PlSearchMapper.getConfigValue(psKey);
	}

	/**
     * @Desc 품목정보 조회
     * @param ErpIfStock3PlSearchResponseDto dto
     * @return List<ItemErpStock3PlSearchResultVo>
     */
	protected List<ItemErpStock3PlSearchResultVo> getStockInfoList(ErpIfStock3PlSearchResponseDto dto){
		return batchItemErpStock3PlSearchMapper.getStockInfoList(dto);
	}

  	/**
  	 * @Desc  품목 유통기한별 재고 저장
  	 * @param ItemErpStock3PlSearchResultVo
  	 * @return int
  	 */
  	protected int addStock3PlSearch(ItemErpStock3PlSearchResultVo vo) {
  		return batchItemErpStock3PlSearchMapper.addStock3PlSearch(vo);
  	}

}
