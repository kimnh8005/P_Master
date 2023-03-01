package kr.co.pulmuone.v1.goods.discount.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDiscountApprVo;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPriceSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mapper.goods.discount.GoodsDiscountMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsRegistMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.itemprice.ItemDiscountMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.itemprice.ItemPriceOrigMapper;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDiscountUploadRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.vo.DiscountInfoVo;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountUploadListVo;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;
import kr.co.pulmuone.v1.goods.itemprice.service.GoodsItemPriceBiz;
import kr.co.pulmuone.v1.goods.price.service.GoodsPriceBiz;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* 상품할인 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 8.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class GoodsDiscountService {

	private static final Logger log = LoggerFactory.getLogger(GoodsDiscountService.class);

    @Autowired
    private GoodsItemPriceBiz itemPriceBiz;

    @Autowired
    private GoodsPriceBiz goodsPriceBiz;

    @Autowired
    private GoodsDiscountMapper goodsDiscountMapper;

    @Autowired
    private ItemPriceOrigMapper itemPriceOrigMapper;

    @Autowired
    private GoodsItemMapper goodsItemMapper;

    @Autowired
    private ItemDiscountMapper itemDiscountMapper;

    private GoodsRegistMapper goodsRegistMapper;

    /**
     * @Desc 상품할인 리스트 조회
     * @param goodsId
     * @param discountTypeCode
     * @throws Exception
     * @return List<GoodsDiscountVo>
     */
    protected List<GoodsDiscountVo> getGoodsDiscountList(Long goodsId, String discountTypeCode) {
        return goodsDiscountMapper.getGoodsDiscountList(goodsId, discountTypeCode);
    }

    /**
     * @Desc 상품할인 삭제
     * @param goodsDiscountId
     * @return int
     */
    protected int deleteGoodsDiscount(Long goodsDiscountId, String userId) {
        return this.updateGoodsDiscount(goodsDiscountId, userId);
    }

	/**
	 * @Desc 상품할인 사용안함 처리(삭제 처리)
	 * @param goodsDiscountId
	 * @return int
	 */
	protected int updateGoodsDiscount(Long goodsDiscountId, String userId) {
		return goodsDiscountMapper.updateGoodsDiscount(goodsDiscountId, userId);
	}

	protected int putGoodsDiscount(Long ilGoodsId, String discountTypeCode, String discountStartDateTime) {
		return goodsDiscountMapper.putGoodsDiscount(ilGoodsId, discountTypeCode, discountStartDateTime);
	}

	/**
	 * @Desc 상품할인 사용안함 처리(삭제 처리)
	 * @param goodsDiscountApprId
	 * @return int
	 */
	protected int updateGoodsDiscountApprStat(Long goodsDiscountApprId, String apprStat, String userId) {
		return goodsDiscountMapper.updateGoodsDiscountApprStat(goodsDiscountApprId, apprStat, userId);
	}

	/**
	 * @Desc 상품할인 사용안함 처리(삭제 처리)
	 * @param goodsDiscountApprId
	 * @return int
	 */
	protected int insertGoodsDiscountApprStatusHistoryStat(Long goodsDiscountApprId, String apprStat, String userId) {
		return goodsDiscountMapper.insertGoodsDiscountApprStatusHistoryStat(goodsDiscountApprId, apprStat, userId);
	}

	/**
	 * @Desc 상품 할인 승인 내역 확인, 승인 내역 존재시 요청 자격 확인
	 * @param goodsDiscountApprId
	 * @return int
	 */
	protected GoodsDiscountApprVo goodsDiscountApprInfo(@Param("goodsDiscountApprId") Long goodsDiscountApprId) throws Exception{
		return goodsDiscountMapper.goodsDiscountApprInfo(goodsDiscountApprId);
	}


    /**
     * @Desc 묶음상품 개별품목 내역을 삭제
     * @param goodsDiscountId
     * @return int
     */
    protected int deleteIlGoodsPackageItemFixedDiscountPrice(Long goodsDiscountId) {
    	return goodsDiscountMapper.deleteIlGoodsPackageItemFixedDiscountPrice(goodsDiscountId);
    }

    /**
     * 가격조회 ERP 연동 후 상품 할인 데이터 처리
     * @param
     * @return int
     */
//    protected int putGoodsDiscountWithErpIfPriceBatch() throws Exception {
//		//조회 기간
//		String fromDate = DateUtil.getCurrentDate() + "000000";
//		String toDate = DateUtil.getCurrentDate() + "235959";
//
//        int addCount = 0;
//
//        //배치 Erp 데이터 param
//        Map<String, String> parameterMap = new HashMap<>();
//        parameterMap.put("updFlg", "Y");    //정보 업데이트 여부(Y / N)
//        parameterMap.put("salTyp", "행사");    //행사구분 (정상 / 행사)
//        parameterMap.put("legCd", LegalTypes.ORGA.getCode());    //올가만   20200929 현재 올가만 행사가 있다고 함 추후 다른 법인도 행사가 발생 시 작업 필요
//        parameterMap.put("updDat", fromDate +"~" + toDate);   //수정날짜(당일기준)
//        //parameterMap.put("itfFlg", "N");   //인터페이스 Flag 20200929 현재 ERP 인터페이스 중계서버에서 미작업 상태
//        //parameterMap.put("itmNo", "0921239");    //행사구분 (정상 / 행사)
//
//        //배치데이터 받기
//        Map<String, ?> returnMap = itemPriceBiz.getErpIfPriceSrchApi(parameterMap);
//
//        //배치 Erp 데이터 Insert
//        List<ErpIfPriceSearchResponseDto> erpIfPriceSearchResponseDtoList = (List<ErpIfPriceSearchResponseDto>)returnMap.get("erpIfPriceSearchResponseDtoList");
//
//        BaseApiResponseVo baseApiResponseVo = (BaseApiResponseVo)returnMap.get("baseApiResponseVo");
//        int totalPageCount = baseApiResponseVo.getTotalPage(); // 전체 페이지 수
//
//        if (totalPageCount > 1) { // 전체 페이지 수가 1 보다 큰 경우
//            for (int page = 2; page <= totalPageCount; page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회
//                parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가
//                //배치데이터 받기
//                returnMap = itemPriceBiz.getErpIfPriceSrchApi(parameterMap);
//
//                erpIfPriceSearchResponseDtoList.addAll((List<ErpIfPriceSearchResponseDto>)returnMap.get("erpIfPriceSearchResponseDtoList"));
//            }
//        }
//
//        //품목코드 오름차순 정렬
//        Collections.sort(erpIfPriceSearchResponseDtoList, (a, b) -> a.getErpItemNo().compareTo(b.getErpItemNo()));
//
//        //상품 할인 데이터 처리
//        for(ErpIfPriceSearchResponseDto erpIfPriceSearchResponseDto : erpIfPriceSearchResponseDtoList) {
//        	try {
//        		addCount = putGoodsDiscountWithItemDiscount(erpIfPriceSearchResponseDto);
//        	} catch(Exception e) {
//        		log.info("putGoodsDiscountWithErpIfPriceBatch.putGoodsDiscountWithItemDiscount e:" + e.toString());
//        	}
//        }
//
//        //상품 가격의 변경 건이 존재 할 경우 가격생성 호출
//        if(addCount > 0) {
//        	goodsPriceBiz.genGoodsPriceScheduleForOrgaBatch(null);
//        }
//        return addCount;
//    }


	/**
	 * 배치 Erp 데이터로 상품 할인가 정보를 처리한다
	 * @param erpIfPriceSearchResponseDtoList
	 * @return
	 */
//    @Transactional(rollbackFor = Exception.class)
//	private int putGoodsDiscountWithItemDiscount(ErpIfPriceSearchResponseDto erpIfPriceSearchResponseDto) throws Exception {
//		int successCount = 0;
//		//빈값일 경우 오늘+1
//		String startDate = StringUtil.nvl(erpIfPriceSearchResponseDto.getErpPriceApplyStartDate(),DateUtil.getDate(1, "yyyy-MM-dd"));
//
//		//품목가격원본 저장(IL_ITEM_PRICE_ORIG)
//		//행사일 경우는 원가 -> norPurPrc   정상가-> norSelPrc (20200929 uniLstPrc 가 원가랑 다른 경우가 존재할 수 있음)
//		ItemPriceOrigVo itemPriceOrigVo = new ItemPriceOrigVo();
//		itemPriceOrigVo.setIlItemCode(erpIfPriceSearchResponseDto.getErpItemNo());
//		itemPriceOrigVo.setStartDate(startDate);	//빈값 존재
//		itemPriceOrigVo.setStandardPrice(String.valueOf(erpIfPriceSearchResponseDto.getNormalPurPrice() == null ? 0 : erpIfPriceSearchResponseDto.getNormalPurPrice()));	//빈값 존재
//		itemPriceOrigVo.setRecommendedPrice(String.valueOf(erpIfPriceSearchResponseDto.getNormalSelPrice() == null ? 0: erpIfPriceSearchResponseDto.getNormalSelPrice())); //빈값 존재
//		itemPriceOrigVo.setCreateId(0);	//배치용 ID 추후 확정 예정
//		itemPriceOrigVo.setModifyId(0);	//배치용 ID 추후 확정 예정
//		itemPriceOrigMapper.addItemPriceOrig(itemPriceOrigVo);
//
//
//		//품목 할인 저장(IL_ITEM_DISCOUNT)
//		DiscountInfoVo discountInfoVo = new DiscountInfoVo();
//		discountInfoVo.setIlItemCode(erpIfPriceSearchResponseDto.getErpItemNo());
//		discountInfoVo.setDiscountType(GoodsEnums.GoodsDiscountType.ERP_EVENT.getCode());
//		discountInfoVo.setDiscountStartDate(startDate);
//		discountInfoVo.setDiscountEndDate(erpIfPriceSearchResponseDto.getErpPriceApplyEndDate()+"235959");
//		discountInfoVo.setDiscountMethodType(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode());
//		discountInfoVo.setDiscountSalePrice(String.valueOf(erpIfPriceSearchResponseDto.getErpRecommendedPrice() == null ? 0: erpIfPriceSearchResponseDto.getErpRecommendedPrice()));
//		discountInfoVo.setCreateId(0);  //배치용 ID 추후 확정 예정
//		itemDiscountMapper.addItemDiscount(discountInfoVo);
//
//		//품목에 해당 하는 상품을 조회한다
//		List<ItemInfoVo> goodsIdList = goodsItemMapper.getGoodsIdListByItemCd(erpIfPriceSearchResponseDto.getErpItemNo());
//		for(ItemInfoVo itemInfoVo : goodsIdList) {
//			//해당 상품을 가격 정보를 변경한다
//			successCount += putGoodsDiscount(itemInfoVo.getGoodsId(), startDate, erpIfPriceSearchResponseDto);
//		}
//
//		return successCount;
//	}


    /**
	 * 상품할인 정보 등록
	 * @param ilGoodsId
	 * @param erpIfPriceSearchResponseDto
	 * @return
	 */
//	private int putGoodsDiscount(long ilGoodsId, String startDate, ErpIfPriceSearchResponseDto erpIfPriceSearchResponseDto) throws Exception {
//
//		//현재 시작일보다 큰 데이터 존재 할 경우 endData 조회 --> 20200929 ERP 에서 동일기간으로 데이터가 2개 생성 안됨
//		//String endDate = goodsDiscountMapper.getGoodsDiscountMinEndDt(ilGoodsId, startDate);
//		//if(!"".equals(endDate)) discountInfoVo.setDiscountEndDate(endDate);
//
//		//상품 할인 등록/수정(IL_GOODS_DISCOUNT)
//		DiscountInfoVo discountInfoVo = new DiscountInfoVo();
//		discountInfoVo.setIlGoodsId(ilGoodsId);
//		discountInfoVo.setDiscountType(GoodsEnums.GoodsDiscountType.ERP_EVENT.getCode());
//		discountInfoVo.setDiscountStartDate(startDate);
//		discountInfoVo.setDiscountEndDate(erpIfPriceSearchResponseDto.getErpPriceApplyEndDate()+"235959");
//		discountInfoVo.setDiscountMethodType(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode());
//		discountInfoVo.setDiscountSalePrice(erpIfPriceSearchResponseDto.getErpRecommendedPrice());
//		discountInfoVo.setCreateId(888);	//배치용 ID 확인 필요
//		discountInfoVo.setModifyId(888);	//배치용 ID 확인 필요
//
//		goodsDiscountMapper.addGoodsDiscountByBatch(discountInfoVo);
//
//		//할인 배치에 의한 배치여부, 변경여부 변경(IL_GOODS)
//		return goodsDiscountMapper.putGoodsBatchChange(ilGoodsId);
//	}

	/**
	* 상품 등록시 올가ERP 연동 할인가격정보 INSERT 처리
	* @param
	* @return int
	*/
	protected int addGoodsDiscountWithErpIfPriceBatch(String ilGoodsId, String ilItemCode) throws Exception {

		int addCount = 0;

		//배치 Erp 데이터 param
		Map<String, String> parameterMap = new HashMap<>();
		parameterMap.put("salTyp", "행사");			//행사구분 (정상 / 행사)
		parameterMap.put("itmNo", ilItemCode);		//아이템CODE
		parameterMap.put("legCd", "OGH");			//법인코드

		//배치데이터 받기
		Map<String, ?> returnMap = itemPriceBiz.getErpIfPriceSrchApi(parameterMap);

		//배치 Erp 데이터 Insert
		List<ErpIfPriceSearchResponseDto> erpIfPriceSearchResponseDtoList = (List<ErpIfPriceSearchResponseDto>)returnMap.get("erpIfPriceSearchResponseDtoList");

		BaseApiResponseVo baseApiResponseVo = (BaseApiResponseVo)returnMap.get("baseApiResponseVo");
		int totalPageCount = baseApiResponseVo.getTotalPage(); // 전체 페이지 수

		if (totalPageCount > 1) { // 전체 페이지 수가 1 보다 큰 경우
			for (int page = 2; page <= totalPageCount; page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회
				parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가
				//배치데이터 받기
				returnMap = itemPriceBiz.getErpIfPriceSrchApi(parameterMap);

				erpIfPriceSearchResponseDtoList.addAll((List<ErpIfPriceSearchResponseDto>)returnMap.get("erpIfPriceSearchResponseDtoList"));
			}
		}
		//품목코드 오름차순 정렬
		Collections.sort(erpIfPriceSearchResponseDtoList, (a, b) -> a.getErpItemNo().compareTo(b.getErpItemNo()));

		//상품 할인 데이터 처리
		for(ErpIfPriceSearchResponseDto erpIfPriceSearchResponseDto : erpIfPriceSearchResponseDtoList) {
			int currentDate = Integer.parseInt(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			int erpPriceApplyStartDate = Integer.parseInt((erpIfPriceSearchResponseDto.getErpPriceApplyStartDate() != null ? erpIfPriceSearchResponseDto.getErpPriceApplyStartDate() : "29991231"));
			int erpPriceApplyEndDate = Integer.parseInt((erpIfPriceSearchResponseDto.getErpPriceApplyEndDate() != null ? erpIfPriceSearchResponseDto.getErpPriceApplyEndDate() : "29991231"));

			if(erpPriceApplyStartDate <= currentDate && erpPriceApplyEndDate >= currentDate) {
				try {
					addCount = addGoodsDiscount(ilGoodsId, erpIfPriceSearchResponseDto);
				} catch(Exception e) {
					log.info("addGoodsDiscountWithErpIfPriceBatch.addGoodsDiscountWithErpIfPriceBatch e:" + e.toString());
				}
			}
		}

		return addCount;
	}

	/**
	* 상품 등록시 올가ERP 연동 할인가격정보 IL_GOODS_DISCOUNT에 입력 처리
	* @param
	* @return int
	*/
	@Transactional(rollbackFor = Exception.class)
	private int addGoodsDiscount(String ilGoodsId, ErpIfPriceSearchResponseDto erpIfPriceSearchResponseDto) throws Exception {
		int successCount = 0;

		//현재 시작일보다 큰 데이터 존재 할 경우 endData 조회
		DiscountInfoVo discountInfoVo = new DiscountInfoVo();
		String startDate = erpIfPriceSearchResponseDto.getErpPriceApplyStartDate();
		String endDate = erpIfPriceSearchResponseDto.getErpPriceApplyEndDate() + "235959";
		long ilGoodsIdLong = Long.parseLong(ilGoodsId);

		//상품 할인 등록/수정(IL_GOODS_DISCOUNT)
		discountInfoVo.setIlGoodsId(ilGoodsIdLong);
		discountInfoVo.setDiscountType(GoodsEnums.GoodsDiscountType.ERP_EVENT.getCode());
		discountInfoVo.setDiscountStartDate(startDate);
		discountInfoVo.setDiscountEndDate(endDate);
		discountInfoVo.setDiscountMethodType(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode());
		discountInfoVo.setDiscountSalePrice(erpIfPriceSearchResponseDto.getErpRecommendedPrice());
		discountInfoVo.setCreateId(0);	//배치용 ID 확인 필요
		discountInfoVo.setModifyId(0);	//배치용 ID 확인 필요

		successCount = goodsDiscountMapper.addGoodsDiscountByAddGoods(discountInfoVo);

		return successCount;
	}

	 /**
     * @Desc 상품 정보 조회
     * @param goodsId
     * @param GoodsDiscountVo
     * @throws Exception
     * @return DetailSelectGoodsVo
     */
    protected DetailSelectGoodsVo getGoodsInfo(GoodsDiscountVo paramDto) {
        return goodsDiscountMapper.getGoodsInfo(paramDto);
    }

    /**
     * @Desc 상품 할인 정보 조회
     * @param goodsId
     * @param GoodsDiscountVo
     * @throws Exception
     * @return DetailSelectGoodsVo
     */
    protected GoodsDiscountVo getGoodsDiscountInfo(GoodsDiscountVo paramDto) {
        return goodsDiscountMapper.getGoodsDiscountInfo(paramDto);
    }

    /**
   	* 상품 할인 엑셀 업로드 등록
   	* @param
   	* @return int
   	*/
   	@Transactional(rollbackFor = Exception.class)
   	protected int addGoodsDiscountExcelUpload(GoodsDiscountVo paramDto) {
   		int successCount = 0;
   		successCount = goodsDiscountMapper.addGoodsDiscountExcelUpload(paramDto);

   		return successCount;
   	}


	/**
	* 상품 할인 엑셀 로그 등록
	* @param
	* @return int
	*/
	@Transactional(rollbackFor = Exception.class)
	protected int addGoodsDiscountExcelLog(GoodsDiscountVo paramDto) {
		int logId = 0;
		logId = goodsDiscountMapper.addGoodsDiscountExcelLog(paramDto);
		return logId;
	}

	/**
	* 상품 할인 엑셀 상세 로그 등록
	* @param
	* @return int
	*/
	@Transactional(rollbackFor = Exception.class)
	protected int addGoodsDiscountExcelDtlLog(GoodsDiscountVo paramDto) {
		int result = 0;
		result = goodsDiscountMapper.addGoodsDiscountExcelDtlLog(paramDto);
		return result;
	}

	/**
	* 상품 할인 엑셀 상세 로그 등록
	* @param
	* @return int
	*/
	@Transactional(rollbackFor = Exception.class)
	protected int putGoodsDiscountExcelLog(GoodsDiscountVo paramDto) {
		int result = 0;
		result = goodsDiscountMapper.putGoodsDiscountExcelLog(paramDto);
		return result;
	}

	/**
     * @Desc 상품할인 업데이트 목록 조회
     * @param GoodsDiscountUploadRequestDto
     * @return Page<GoodsDiscountUploadListVo>
     */
    protected Page<GoodsDiscountUploadListVo> getGoodsDiscountUploadList(GoodsDiscountUploadRequestDto paramDto) {
    	paramDto.setUploadTp("IL_DISC_UPLOAD_TP.GOODS_DISC");
       	PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
        return goodsDiscountMapper.getGoodsDiscountUploadList(paramDto);
    }

	/**
     * @Desc 상품할인 업데이트 목록 조회
     * @param GoodsDiscountUploadRequestDto
     * @return Page<GoodsDiscountUploadListVo>
     */
    protected Page<GoodsDiscountUploadListVo> getGoodsDisEmpUploadList(GoodsDiscountUploadRequestDto paramDto) {
    	paramDto.setUploadTp("IL_DISC_UPLOAD_TP.EMPLOYEE_DISC");
       	PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
        return goodsDiscountMapper.getGoodsDiscountUploadList(paramDto);
    }



    /**
     * @Desc 상품 할인 일괄 업로드 실패 내역 - 엑셀다운로드
     * @param GoodsDiscountUploadRequestDto
     * @return List<GoodsDiscountUploadListVo>
     */
    protected List<GoodsDiscountUploadListVo> getGoodsDiscountUploadFailList(GoodsDiscountUploadRequestDto paramDto) {
        return goodsDiscountMapper.getGoodsDiscountUploadFailList(paramDto);
    }

    /**
     * @Desc 상품할인 업데이트 목록 상세 조회
     * @param GoodsDiscountUploadRequestDto
     * @return Page<GoodsDiscountUploadListVo>
     */
    protected Page<GoodsDiscountUploadListVo> getGoodsDiscountUploadDtlList(GoodsDiscountUploadRequestDto paramDto) {
       	PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
        return goodsDiscountMapper.getGoodsDiscountUploadDtlList(paramDto);
    }

    /**
     * @Desc 상품 할인 일괄 업로드 상세 내역 - 엑셀다운로드
     * @param GoodsDiscountUploadRequestDto
     * @return List<GoodsDiscountUploadListVo>
     */
    protected List<GoodsDiscountUploadListVo> getGoodsDiscountUploadDtlListExcel(GoodsDiscountUploadRequestDto paramDto) {
        return goodsDiscountMapper.getGoodsDiscountUploadDtlListExcel(paramDto);
    }

    /**
     * @Desc 임직원 상품 할인 일괄 업로드 실패 내역 - 엑셀다운로드
     * @param GoodsDiscountUploadRequestDto
     * @return List<GoodsDiscountUploadListVo>
     */
    protected List<GoodsDiscountUploadListVo> getGoodsDiscountEmpUploadFailList(GoodsDiscountUploadRequestDto paramDto) {
        return goodsDiscountMapper.getGoodsDiscountEmpUploadFailList(paramDto);
    }

    /**
     * @Desc 임직원 상품 할인 일괄 업로드 상세 조회
     * @param GoodsDiscountUploadRequestDto
     * @return Page<GoodsDiscountUploadListVo>
     */
    protected Page<GoodsDiscountUploadListVo> getGoodsDiscountEmpUploadDtlList(GoodsDiscountUploadRequestDto paramDto) {
       	PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
        return goodsDiscountMapper.getGoodsDiscountEmpUploadDtlList(paramDto);
    }

    /**
     * @Desc 임직원 상품 할인 일괄 업로드 상세 내역 - 엑셀다운로드
     * @param GoodsDiscountUploadRequestDto
     * @return List<GoodsDiscountUploadListVo>
     */
    protected List<GoodsDiscountUploadListVo> getGoodsDiscountEmpUploadDtlListExcel(GoodsDiscountUploadRequestDto paramDto) {
        return goodsDiscountMapper.getGoodsDiscountEmpUploadDtlListExcel(paramDto);
    }

    /**
     * @Desc 묶음상품 기본할인 삭제 전 이전날짜 UPDATE 대상 찾기
     * @param goodsId
     * @param discountTypeCode
     * @throws Exception
     * @return GoodsDiscountVo
     */
    protected GoodsDiscountVo getGoodsPackageBaseDiscountUpdateList(Long goodsDiscountId, String discountTypeCode) {
        return goodsDiscountMapper.getGoodsPackageBaseDiscountUpdateList(goodsDiscountId, discountTypeCode);
    }

    /**
     * @Desc 묶음상품 기본할인 삭제 전 이전날짜 UPDATE
     * @param discountInfoVo
     * @throws Exception
     * @return int
     */
	protected int putPastGoodsDiscountByBatch(DiscountInfoVo discountInfoVo) {
		return goodsDiscountMapper.putPastGoodsDiscountByBatch(discountInfoVo);
	}

	/**
	 * @Desc 상품 기본할인 승인 삭제 전 이전날짜 UPDATE
	 * @param discountInfoVo
	 * @throws Exception
	 * @return int
	 */
	protected int putPastGoodsDiscountApprByBatch(DiscountInfoVo discountInfoVo) {
		return goodsDiscountMapper.putPastGoodsDiscountApprByBatch(discountInfoVo);
	}

	/**
	* @Desc 묶음 상품 > 묶음 상품 할인 내역 삭제 시 (Procedure 호출)
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int spGoodsPriceUpdateWhenPackageGoodsChanges(GoodsRegistRequestDto goodsRegistRequestDto) {
		log.info("goodsRegistRequestDto : " + goodsRegistRequestDto.toString());

		return goodsDiscountMapper.spGoodsPriceUpdateWhenPackageGoodsChanges(goodsRegistRequestDto);
	}

	/**
	* @Desc 상품 할인 변경에 의한 일반 상품 가격 프로시져(삭제포함) (Procedure 호출)
	* @param GoodsRegistRequestDto
	* @return void
	*/
	protected void spGoodsPriceUpdateWhenGoodsDiscountChanges(String ilGoodsId) {
		boolean isDebugFlag = false;
		goodsDiscountMapper.spGoodsPriceUpdateWhenGoodsDiscountChanges(ilGoodsId, isDebugFlag);
	}




}
