package kr.co.pulmuone.v1.goods.itemprice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.api.constant.ProductTypes;
import kr.co.pulmuone.v1.comm.api.constant.SalesTypes;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPriceSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.mapper.goods.itemprice.ItemPriceMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.itemprice.ItemPriceOrigMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceListVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;
import kr.co.pulmuone.v1.goods.price.service.GoodsPriceBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 품목 가격조회 ERP 연동 및 가격 로직 구현
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200902   	ykk       최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class GoodsItemPriceService {

	private static final Logger log = LoggerFactory.getLogger(GoodsItemPriceService.class);

    // ERP API 에서 가격 정보 조회 인터페이스 ID
    private static final String PRICE_SEARCH_INTERFACE_ID = "IF_PRICE_SRCH";

    @Autowired
    ErpApiExchangeService erpApiExchangeService;

    @Autowired
    private GoodsPriceBiz goodsPriceBiz;

    @Autowired
    ItemPriceOrigMapper itemPriceOrigMapper;

    @Autowired
    ItemPriceMapper itemPriceMapper;


	/**
     * ERP API 가격정보  조회 인터페이스 호출
     *
	 * @return
	 */
//	protected int addItemPriceOrigByErpIfPriceSrchApi() throws Exception {
//		//조회 기간
//		String fromDate = DateUtil.getCurrentDate() + "000000";
//		String toDate = DateUtil.getCurrentDate() + "235959";
//
//		//배치 Erp 데이터 param
//        Map<String, String> parameterMap = new HashMap<>();
//        parameterMap.put("updFlg", "Y");	//정보 업데이트 여부(Y / N)
//        parameterMap.put("salTyp", "정상");    //행사구분 (정상 / 행사) -> 정상 행사 따로 호출 할 경우
//        parameterMap.put("updDat", fromDate +"~" + toDate);   //수정날짜(당일기준)
//        //parameterMap.put("itfFlg", "N");   //인터페이스 Flag 20200929 현재 ERP 인터페이스 중계서버에서 미작업 상태
//
//		//배치데이터 받기
//        Map<String, ?> returnMap = getErpIfPriceSrchApi(parameterMap);
//        List<ErpIfPriceSearchResponseDto> erpIfPriceSearchResponseDtoList = (List<ErpIfPriceSearchResponseDto>)returnMap.get("erpIfPriceSearchResponseDtoList");
//
//        BaseApiResponseVo baseApiResponseVo = (BaseApiResponseVo)returnMap.get("baseApiResponseVo");
//        int totalPageCount = baseApiResponseVo.getTotalPage(); // 전체 페이지 수
//
//        if (totalPageCount > 1) { // 전체 페이지 수가 1 보다 큰 경우
//            for (int page = 2; page <= totalPageCount; page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회
//                parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가
//                //배치데이터 받기
//                returnMap = getErpIfPriceSrchApi(parameterMap);
//
//                erpIfPriceSearchResponseDtoList.addAll((List<ErpIfPriceSearchResponseDto>)returnMap.get("erpIfPriceSearchResponseDtoList"));
//
//            }
//        }
//
//        //배치 Erp 데이터 Insert (배치 성공여부 return 방법 확인 필요-> 전체일경우)
//        int addCount = addItemPriceOrig(erpIfPriceSearchResponseDtoList);
//
//        //가격생성 호출
//        goodsPriceBiz.genGoodsPriceScheduleForErpBatch(null);
//
//        return addCount;
//	}

	/**
	 * 배치 Erp 데이터 받기
	 * @param parameterMap
	 * @return
	 */
	protected Map<String,?> getErpIfPriceSrchApi(Map<String, String> parameterMap) throws Exception {
		//time 체크
		//log.info("before getErpIfPriceSrchApi : %s\r" +  DateUtil.getCurrentDate("yyyyMMddHHmmss"));

		Map<String,Object> returnMap = new HashMap<>();
		// baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, PRICE_SEARCH_INTERFACE_ID);

        // baseApiResponseVo => List<T> 역직렬화
        List<ErpIfPriceSearchResponseDto> erpIfPriceSearchResponseDtoList = baseApiResponseVo.deserialize(ErpIfPriceSearchResponseDto.class);

        returnMap.put("erpIfPriceSearchResponseDtoList", erpIfPriceSearchResponseDtoList);
        returnMap.put("baseApiResponseVo", baseApiResponseVo);

        return returnMap;
	}


	/**
	 * 배치 Erp 데이터 Insert
	 * @param erpIfPriceSearchResponseDtoList
	 * @return
	 */
	private int addItemPriceOrig(List<ErpIfPriceSearchResponseDto> erpIfPriceSearchResponseDtoList) throws Exception {
		//배치 Erp 데이터 Insert
        int successCount = 0;	//batch success count
        for(ErpIfPriceSearchResponseDto erpIfPriceSearchResponseDto : erpIfPriceSearchResponseDtoList) {
        	try {
        		String itemCode = erpIfPriceSearchResponseDto.getErpItemNo();

        		ItemPriceOrigVo itemPriceOrigVo = new ItemPriceOrigVo();
        		itemPriceOrigVo.setIlItemCode(itemCode);
        		itemPriceOrigVo.setStartDate(erpIfPriceSearchResponseDto.getErpPriceApplyStartDate());	//빈값 존재
        		itemPriceOrigVo.setStandardPrice(String.valueOf(erpIfPriceSearchResponseDto.getErpStandardPrice() == null ? 0 : erpIfPriceSearchResponseDto.getErpStandardPrice()));	//빈값 존재
        		itemPriceOrigVo.setRecommendedPrice(String.valueOf(erpIfPriceSearchResponseDto.getErpRecommendedPrice() == null ? 0: erpIfPriceSearchResponseDto.getErpRecommendedPrice())); //빈값 존재
        		itemPriceOrigVo.setCreateId(888);	//배치용 ID 추후 확정 예정
        		itemPriceOrigVo.setModifyId(888);	//배치용 ID 추후 확정 예정

        		/* FDM & PGS 정상가 = IL_ITEM_PRICE_ORIG 최신 정상가 처리 (배치 data 사용x) */
        		//FDM 푸드머스
        		if(LegalTypes.FOODMERCE.getCode().equals(erpIfPriceSearchResponseDto.getLegalType().getCode())) {
        			//IL_ITEM_PRICE_ORIG 데이터 조회
        			ItemPriceOrigVo reItemPriceOrigVo = itemPriceOrigMapper.getItemPriceOrigLastly(itemCode);
        			if(reItemPriceOrigVo != null) {
        				itemPriceOrigVo.setRecommendedPrice(reItemPriceOrigVo.getRecommendedPrice());
        			}

        		//PGS 건강생활
        		} else if(LegalTypes.LOHAS.getCode().equals(erpIfPriceSearchResponseDto.getLegalType().getCode())) {
        			//상품인 경우만 원가/정상가 처리
        			ItemPriceOrigVo reItemPriceOrigVo = itemPriceOrigMapper.getItemPriceOrigLastlyByErpProductType(itemCode, ProductTypes.GOODS.getCode());

        			//제품은 처리 x
        			if(reItemPriceOrigVo == null) {
	        			continue;
        			}
        			itemPriceOrigVo.setRecommendedPrice(reItemPriceOrigVo.getRecommendedPrice());

        		}

        		itemPriceOrigMapper.addItemPriceOrig(itemPriceOrigVo);

        		successCount++;
        	} catch(Exception e) {
        		log.info("itemPriceOrigMapper.addItemPriceOrig e:" + e.toString());
        	}
        }

        return successCount;
	}



	/**
     * ITEM_PRICE 정보 동기화
     *
	 * @return
	 */
	protected int addItemPriceByOrig() throws Exception {
		//품목가격원본 테이블 조회
		List<ItemPriceOrigVo> ItemPriceOrigVoList = itemPriceOrigMapper.getItemPriceOrigListNoBatchDate();
		int addCount = 0;
		for(ItemPriceOrigVo itemPriceOrigVo : ItemPriceOrigVoList) {
			addCount += addItemPriceWithItem(itemPriceOrigVo);
		}

		//품목가격 변경건이 있는 경우 가격생성 호출
		if(addCount > 0)  {
			goodsPriceBiz.genGoodsPriceScheduleForErpBatch(null);
		}

		return addCount;
	}



	/**
	 * 품목가격 정보 등록
	 * 배치 시간 update 품목, 품목가격원본
	 * @param itemPriceOrigVo
	 * @return
	 */
	//@Transactional(rollbackFor = Exception.class)
	private int addItemPriceWithItem(ItemPriceOrigVo itemPriceOrigVo) throws Exception {

		//현재 시작일보다 큰 데이터 존재 할 경우 endData 조회
		ItemPriceListVo itemPriceVo = new ItemPriceListVo();
		String endDate = itemPriceMapper.getItemPriceMinEndDt(itemPriceOrigVo);
		if(!"".equals(endDate)) itemPriceVo.setEndDate(endDate);

		//품목가격 정보 등록/수정(IL_ITEM_PRICE)
		itemPriceVo.setIlItemCode(itemPriceOrigVo.getIlItemCode());
		itemPriceVo.setStartDate(itemPriceOrigVo.getStartDate());
		itemPriceVo.setStandardPrice(itemPriceOrigVo.getStandardPrice());
		itemPriceVo.setRecommendedPrice(itemPriceOrigVo.getRecommendedPrice());
		itemPriceVo.setCreateId("1");	//배치용 ID 확인 필요
		itemPriceVo.setModifyId("1");	//배치용 ID 확인 필요
		itemPriceMapper.addItemPriceByOrig(itemPriceVo);

		//적용시작일 이전 data
		List<ItemPriceListVo> itemPriceList = itemPriceMapper.getItemPriceStartDateUnderList(itemPriceOrigVo);
		for(ItemPriceListVo itemPriceListVo : itemPriceList) {
			//이전 시작일의 min(startDate) 로 종료일 구하기
			itemPriceOrigVo.setStartDate(itemPriceListVo.getStartDate());
			String pastEndDate = itemPriceMapper.getItemPriceMinEndDt(itemPriceOrigVo);

			itemPriceListVo.setEndDate(pastEndDate);
			itemPriceMapper.putPastItemPrice(itemPriceListVo);
	    }

		//품목가격원본 테이블 배치 시간 수정
		itemPriceOrigMapper.putItemPriceOrigBatchDate(itemPriceOrigVo.getIlItemPriceOriginalId());


		//품목 배치에 의한 가격, 할인 변경 시간 수정
		int addCount = itemPriceMapper.putItemBatchDate(itemPriceOrigVo.getIlItemCode());

		return addCount;
	}


}
