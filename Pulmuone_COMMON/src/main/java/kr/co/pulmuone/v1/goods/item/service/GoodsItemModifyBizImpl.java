package kr.co.pulmuone.v1.goods.item.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.ItemColumnComment;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.ItemEtcColumnComment;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.UndeliverableAreaTypes;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.goods.service.GoodsDetailImageBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsRegistBiz;
import kr.co.pulmuone.v1.goods.item.dto.*;
import kr.co.pulmuone.v1.goods.item.dto.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 수정 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 11. 05.               박주형         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsItemModifyBizImpl implements GoodsItemModifyBiz {

    @Autowired
    private final GoodsItemModifyService goodsItemModifyService;

    @Autowired
    private final GoodsItemRegisterService goodsItemRegisterService;

    @Autowired
    private final GoodsRegistBiz goodsRegistBiz;

    @Autowired
    private final GoodsItemRegisterBiz goodsItemRegisterBiz;

	@Autowired
	GoodsDetailImageBiz goodsDetailImageBiz;

    int changeTranNum = 0;								//상품 변경이력 Transaction 갯수
	int sameValue = 0;									//beforeData, afterData가 동일한 갯수

	ItemEtcColumnComment itemEtcColumnComment = null;	//테이블별 ID , DATA , COLUMN COMMENT 컬럼을 지정

    /*
     * 마스터 품목 정보 조회 로직 Start
     */

    /**
     * @Desc 해당 품목코드로 IL_ITEM 테이블의 마스터 품목 정보 조회
     *
     * @param ilItemCode : 검색할 품목 코드
     *
     * @return ApiResult : 마스터 품목 정보 ApiResult
     * @throws BaseException
     */
    @Override
    public ApiResult<?> getMasterItem(String ilItemCode, String ilItemApprId) throws BaseException {

    	UserVo userVo = SessionUtil.getBosUserVO();
		String apprKindTp = null;
		if(userVo.getCompanyType().equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//본사라면
			apprKindTp = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode();
		}
		else if(userVo.getCompanyType().equals(CompanyEnums.CompanyType.CLIENT.getCode()) && userVo.getClientType().equals(CompanyEnums.ClientType.CLIENT.getCode())) {
			apprKindTp = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode();
		}

        // 해당 품목 코드로 IL_ITEM 테이블의 마스터 품목 정보 조회
        MasterItemVo masterItemVo = goodsItemModifyService.getMasterItem(ilItemCode, apprKindTp, ilItemApprId);

		//판매/전시 > 승인 상태 내역
		List<ItemApprovalResultVo> itemApprStatusList = goodsItemModifyService.itemApprStatusList(ilItemCode);

        if (masterItemVo == null) { // 해당 품목코드의 마스터 품목 정보 미조회시
            throw new BaseException(ItemEnums.Item.ITEM_INFO_NOT_EXIST);
        }

        MasterItemResponseDto masterItemResponseDto = MasterItemResponseDto.builder() //
                .rows(masterItemVo) // 마스터 품목 정보
                .undeliverableAreaResponseDto( // 배송불가지역 정보
                        new UndeliverableAreaResponseDto(masterItemVo.getUndeliverableAreaType()) //
                ) //
                .itemApprStatusList(itemApprStatusList)
                .build();

        return ApiResult.success(masterItemResponseDto);

    }

    /*
     * 마스터 품목 정보 조회 로직 End
     */

    /*
     * 해당 품목의 가격정보 조회 로직 Start
     */

    /**
     * @Desc 해당 품목의 현재/예정 가격정보 목록 조회 : 과거 이력은 제외
     *
     * @param ilItemCode : 검색할 품목 코드
     *
     * @return ApiResult : 해당 품목의 현재/예정 가격정보 목록 ApiResult
     */
    @Override
    public ApiResult<?> getItemPriceSchedule(String ilItemCode) {

        ItemPriceHistoryResponseDto result = new ItemPriceHistoryResponseDto();

        result.setRows(goodsItemModifyService.getItemPriceSchedule(ilItemCode));

		result.setRowsPopup(goodsItemModifyService.getItemPricePopup(ilItemCode));

        return ApiResult.success(result);

    }

    /**
     * @Desc 해당 품목의 가격정보 전체 이력 목록 조회 : 페이지네이션 적용
     *
     * @param ItemPriceHistoryRequestDto : 가격정보 이력 조회 request dto
     *
     * @return ApiResult : 해당 품목의 전체 이력 ApiResult
     */
    @Override
    public ApiResult<?> getItemPriceHistory(ItemPriceHistoryRequestDto itemPriceHistoryRequestDto) {

        ItemPriceHistoryResponseDto result = new ItemPriceHistoryResponseDto();

        Page<ItemPriceVo> rows = goodsItemModifyService.getItemPriceHistory(itemPriceHistoryRequestDto);

        result.setTotal(rows.getTotal());
        result.setRows(rows.getResult());

        return ApiResult.success(result);

    }

    @Override
    public ApiResult<?> getItemErpPriceSchedule(String ilItemCode) {
        // TODO Auto-generated method stub
        ItemDiscountResponseDto result = new ItemDiscountResponseDto();

        result.setRows(goodsItemModifyService.getItemErpPriceSchedule(ilItemCode));

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> getItemDiscountPriceHistory(ItemPriceHistoryRequestDto itemPriceHistoryRequestDto) {
        // TODO Auto-generated method stub
        ItemDiscountResponseDto result = new ItemDiscountResponseDto();

        Page<ItemDiscountVo> rows = goodsItemModifyService.getItemDiscountPriceHistory(itemPriceHistoryRequestDto);

        result.setTotal(rows.getTotal());
        result.setRows(rows.getResult());

        return ApiResult.success(result);

    }

    @Override
    public ApiResult<?> getPoTpDetailInfoList(MasterItemListRequestDto masterItemListRequestDto) {
        // TODO Auto-generated method stub
        ItemPoTypeListResponseDto result = new ItemPoTypeListResponseDto();

        Page<ItemPoTypeVo> rows = goodsItemModifyService.getPoTpDetailInfoList(masterItemListRequestDto);

        result.setTotal(rows.getTotal());
        result.setRows(rows.getResult());

        return ApiResult.success(result);

    }

    /*
     * 해당 품목의 가격정보 조회 로직 End
     */

    /*
     * 마스터 품목 정보 수정 관련 로직 Start
     */

    /**
     * @desc 마스터 품목 정보 수정
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 Request dto
     *
     * @return ApiResult : 마스터 품목 수정 ApiResult
     * @throws BaseException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BaseException.class, Exception.class })
    public ApiResult<?> modifyMasterItem(ItemModifyRequestDto itemModifyRequestDto) throws Exception {

    	UserVo userVo = SessionUtil.getBosUserVO();
		String apprKindTp = null;
		if(userVo.getCompanyType().equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//본사라면
			apprKindTp = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode();
		}
		else if(userVo.getCompanyType().equals(CompanyEnums.CompanyType.CLIENT.getCode()) && userVo.getClientType().equals(CompanyEnums.ClientType.CLIENT.getCode())) {
			apprKindTp = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode();
		}

        // 추후 확장성을 위해 response dto 사용
        ItemModifyResponseDto itemModifyResponseDto = ItemModifyResponseDto.builder() //
                .build();

		//품목 기존 마스터정보
        MasterItemVo beforeItemMasterData = goodsItemModifyService.getMasterItem(itemModifyRequestDto.getIlItemCode(), apprKindTp, null);

  		// 품목 정보제공고시
        List<ItemSpecValueVo> beforeItemSpecList = goodsItemRegisterService.getItemSpecValueList(itemModifyRequestDto.getIlItemCode(), null);
  		List<ItemSpecValueRequestDto> afterItemSpecList = new ArrayList<ItemSpecValueRequestDto>();
  		afterItemSpecList.addAll(itemModifyRequestDto.getAddItemSpecValueList());

//		// 인증정보
  		List<ItemCertificationListVo> beforeItemCertList = goodsItemRegisterService.getItemCertificationList(itemModifyRequestDto.getIlItemCode(), null);
		List<ItemCertificationDto> afterItemCertList = new ArrayList<ItemCertificationDto>();
		afterItemCertList.addAll(itemModifyRequestDto.getAddItemCertificationList());

  		// 출고처 정보
		List<ItemWarehouseVo> beforeItemWarehouseList = goodsItemRegisterService.getItemWarehouseList(itemModifyRequestDto.getIlItemCode());
  		List<ItemWarehouseDto> afterItemWarehouseList = new ArrayList<ItemWarehouseDto>();
  		afterItemWarehouseList.addAll(itemModifyRequestDto.getAddItemWarehouseList());

  		// 이미지 정보
  		List<ItemImageVo> beforeItemImageList = goodsItemRegisterService.getItemImage(itemModifyRequestDto.getIlItemCode(), null);

  		// 영양정보
  		List<ItemNutritionDetailVo> beforeItemNurList = goodsItemRegisterService.getItemNutritionDetailList(itemModifyRequestDto.getIlItemCode(), null);

        // 승인
		ApiResult<?> itemApprProcApi =  itemApprProc(itemModifyRequestDto, beforeItemMasterData, beforeItemCertList, beforeItemImageList, beforeItemNurList, beforeItemSpecList);
		if(!itemApprProcApi.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
			return ApiResult.result(itemModifyResponseDto, itemApprProcApi.getMessageEnum());
		}
		else {
			// 품목 정보 수정 전 validation 체크
	        goodsItemModifyService.validationCheckBeforeModifyItem(itemModifyRequestDto);

	        // 품목 수정시 USER ID 세팅
	        itemModifyRequestDto.setModifyId((Long.valueOf((SessionUtil.getBosUserVO()).getUserId())));

	        /*
	         * 배송불가 지역 코드 세팅
	         */
	        itemModifyRequestDto.setUndeliverableAreaType( //
	                UndeliverableAreaTypes.getUndeliverableAreaTypeCode( //
	                        itemModifyRequestDto.isIslandShippingYn() // 도서산간지역 (1권역) 배송여부 ( true : 배송가능 )
	                        , itemModifyRequestDto.isJejuShippingYn()) // 제주지역 (2권역) 배송여부 ( true : 배송가능 )
	        );

	        /*
	         * 품목 코드, 등록/수정자 ID 를 각 상세 항목 정보 ( 상품 영양 정보, 출고처 ... ) 에 일괄 세팅
	         */
	        goodsItemModifyService.setItemCodeInDetailInfo(itemModifyRequestDto);

	        /*
	         * 단종여부 - 예 처리할시 상품 판매정지 서비스 호출.
	         */
	        if(itemModifyRequestDto.isExtinctionYn()) {
	        	goodsRegistBiz.updateGoodsStopSale(itemModifyRequestDto.getIlItemCode());
	        }


	        /*
	         * IL_ITEM 테이블에서 해당 마스터 품목 정보 수정
	         */
	        goodsItemModifyService.modifyItem(itemModifyRequestDto);

	        /*
	         * 품목별 가격정보 수정
	         */
	        goodsItemModifyService.modifyItemPrice(itemModifyRequestDto);

	        /*
	         * 품목별 상품정보 제공고시 수정
	         */
	        goodsItemModifyService.modifyItemSpecValue(itemModifyRequestDto);

	        /*
	         * 품목별 영양정보 수정
	         */
	        goodsItemModifyService.modifyItemNutrition(itemModifyRequestDto);

	        /*
	         * 품목별 상품 인증정보 수정
	         */
	        goodsItemModifyService.modifyItemCertification(itemModifyRequestDto);

	        /*
	         * 품목별 출고처 정보 수정
	         */
	        goodsItemModifyService.modifyItemWarehouse(itemModifyRequestDto);

	        /*
	         * 품목별 이미지 재등록 실행
	         */
	        goodsItemModifyService.modifyItemImage(itemModifyRequestDto);


	        // 상품 이미지 등록 처리 수정
			goodsDetailImageBiz.getUpdateItemInfoForDetailImage(itemModifyRequestDto, beforeItemMasterData, null,  beforeItemNurList, null, beforeItemSpecList,  null);


			//품목변경 내역
			int chgLogNum = 0;
			Map<String, String> beforeItemData = BeanUtils.describe(beforeItemMasterData);
	  		Map<String, String> afterItemData = BeanUtils.describe(itemModifyRequestDto);

	  		String timestamp = DateUtil.getCurrentDate("yyyyMMddHHmmss");

	  		chgLogNum = addItemMasterChangeLog("UPDATE", itemModifyRequestDto.getIlItemCode(), beforeItemData, afterItemData, timestamp);

	  		chgLogNum += addItemSubChangeLog("UPDATE", "IL_ITEM_SPEC_VALUE", itemModifyRequestDto.getIlItemCode(), beforeItemSpecList, afterItemSpecList, timestamp);

			chgLogNum += addItemSubChangeLog("UPDATE", "IL_ITEM_CERTIFICATION", itemModifyRequestDto.getIlItemCode(), beforeItemCertList, afterItemCertList, timestamp);

	  		chgLogNum += addItemSubChangeLog("UPDATE", "IL_ITEM_WAREHOUSE", itemModifyRequestDto.getIlItemCode(), beforeItemWarehouseList, afterItemWarehouseList, timestamp);
		}

		return ApiResult.success(itemModifyResponseDto);
    }

  //모든 상품 > 상품 승인 처리
  	private ApiResult<?> itemApprProc(ItemModifyRequestDto itemModifyRequestDto, MasterItemVo beforeItemDetail, List<ItemCertificationListVo> beforeItemCertList
  										, List<ItemImageVo> beforeItemImageList, List<ItemNutritionDetailVo> beforeItemNurList,  List<ItemSpecValueVo> beforeItemSpecList) throws Exception {
  		MessageCommEnum enums = BaseEnums.Default.SUCCESS;
  		UserVo userVo = SessionUtil.getBosUserVO();
  		String userId = userVo.getUserId();					// USER ID
  		String companyType = userVo.getCompanyType();		// 회사타입
  		String clientType = userVo.getClientType();			// 거래처 타입

  		if(!itemModifyRequestDto.getItemApprList().isEmpty()) {

  			ItemRegistApprVo itemRegistApprVo = new ItemRegistApprVo();

  			itemRegistApprVo.setItemNm(itemModifyRequestDto.getItemName());
  			itemRegistApprVo.setErpIfYn(itemModifyRequestDto.isErpLinkIfYn());
  			itemRegistApprVo.setErpStockIfYn(itemModifyRequestDto.getErpStockIfYn());
  			itemRegistApprVo.setIlCtgryStdId(itemModifyRequestDto.getIlCategoryStandardId());
  			itemRegistApprVo.setUrSupplierId(itemModifyRequestDto.getUrSupplierId());
  			itemRegistApprVo.setUrBrandId(itemModifyRequestDto.getUrBrandId());
  			itemRegistApprVo.setStorageMethodTp(itemModifyRequestDto.getStorageMethodType());
  			itemRegistApprVo.setOriginTp(itemModifyRequestDto.getOriginType());
  			itemRegistApprVo.setDistributionPeriod(itemModifyRequestDto.getDistributionPeriod());
  			itemRegistApprVo.setSizePerPackage(itemModifyRequestDto.getSizePerPackage());
  			itemRegistApprVo.setSizeUnit(itemModifyRequestDto.getSizeUnit());
  			itemRegistApprVo.setIlSpecMasterId(itemModifyRequestDto.getIlSpecMasterId());
  			itemRegistApprVo.setItemGrp(itemModifyRequestDto.getBosItemGroup());

  			for(ItemRegistApprRequestDto itemRegistApprRequestDto : itemModifyRequestDto.getItemApprList()) {
  				if(itemRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode())) {
  					itemRegistApprVo.setApprSubUserId(itemRegistApprRequestDto.getApprUserId());
  				}

  				if(itemRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode())) {
  					itemRegistApprVo.setIlItemCd(itemModifyRequestDto.getIlItemCode());
  					itemRegistApprVo.setApprKindTp(itemRegistApprRequestDto.getApprKindTp());
  					itemRegistApprVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
  					itemRegistApprVo.setApprReqUserId(userId);
  					itemRegistApprVo.setApprUserId(itemRegistApprRequestDto.getApprUserId());
  				}
  			}

  			if(companyType.equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//관리자 권한이라면
  				itemModifyRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode());
  				ItemRegistApprVo itemApprInfo = goodsItemRegisterService.itemApprInfo(userId, itemModifyRequestDto.getIlItemCode(), null, itemModifyRequestDto.getApprKindTp());

  				if(itemApprInfo == null || itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
  						|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())
  						|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode())) {		//승인 내역이 없거나, 반려/요청철회/폐기 상태일때

  					goodsItemRegisterService.addItemAppr(itemRegistApprVo);

  					itemRegistApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
  					itemRegistApprVo.setStatusCmnt(null);

  					goodsItemRegisterService.addItemApprStatusHistory(itemRegistApprVo);

  				}
  				else {
  					enums = ItemEnums.ItemApprProcStatus.APPR_DUPLICATE;
  				}
  			}
  			else if(companyType.equals(CompanyEnums.CompanyType.CLIENT.getCode()) && clientType.equals(CompanyEnums.ClientType.CLIENT.getCode())) {	//거래처 권한이라면
  				itemModifyRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode());
  				ItemRegistApprVo itemApprInfo = goodsItemRegisterService.itemApprInfo(userId, itemModifyRequestDto.getIlItemCode(), null, itemModifyRequestDto.getApprKindTp());

  				int differentCount = 0;																//거래처 상품 변경 카운트

  				if(beforeItemDetail != null) {

  		  			itemRegistApprVo.setItemNm(itemModifyRequestDto.getItemName());
  		  			itemRegistApprVo.setErpIfYn(itemModifyRequestDto.isErpLinkIfYn());
  		  			itemRegistApprVo.setErpStockIfYn(itemModifyRequestDto.getErpStockIfYn());
  		  			itemRegistApprVo.setIlCtgryStdId(itemModifyRequestDto.getIlCategoryStandardId());
  		  			itemRegistApprVo.setUrSupplierId(itemModifyRequestDto.getUrSupplierId());

  					int dateCompare = DateUtil.string2Date(beforeItemDetail.getModifyDt(), "yyyy-MM-dd HH:mm:ss").compareTo(DateUtil.string2Date(itemModifyRequestDto.getLoadDateTime(), "yyyy-MM-dd HH:mm:ss"));

  					if(dateCompare > 0) {
  						enums = ItemEnums.ItemApprProcStatus.ADMIN_DIFFERENT_ITEM;
  						itemModifyRequestDto.setLoadDateTime(null);
  					}
  					else {
  						// 상품군
  						if (
  							(beforeItemDetail.getBosItemGroup() == null && itemModifyRequestDto.getBosItemGroup() != null)
  							|| (beforeItemDetail.getBosItemGroup() != null && !beforeItemDetail.getBosItemGroup().equals(itemModifyRequestDto.getBosItemGroup()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setItemGrp(itemModifyRequestDto.getBosItemGroup());

  						// 보관방법
  						if (
  							(beforeItemDetail.getStorageMethodType() == null && itemModifyRequestDto.getStorageMethodType() != null)
							|| (beforeItemDetail.getStorageMethodType() != null && !beforeItemDetail.getStorageMethodType().equals(itemModifyRequestDto.getStorageMethodType()))
						) {
  							differentCount++;
  						}
  	  		  			itemRegistApprVo.setStorageMethodTp(itemModifyRequestDto.getStorageMethodType());

  						// 원산지
  						if (
  							(beforeItemDetail.getOriginType() == null && itemModifyRequestDto.getOriginType() != null)
							|| (beforeItemDetail.getOriginType() != null && !beforeItemDetail.getOriginType().equals(itemModifyRequestDto.getOriginType()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setOriginTp(itemModifyRequestDto.getOriginType());

  						// 원산지 상세
  						if (
  							(beforeItemDetail.getOriginDetail() == null && itemModifyRequestDto.getOriginDetail() != null)
							|| (beforeItemDetail.getOriginDetail() != null && !beforeItemDetail.getOriginDetail().equals(itemModifyRequestDto.getOriginDetail()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setOriginDetl(itemModifyRequestDto.getOriginDetail());

  						// 박스체적 - 가로
  						if (
  							(beforeItemDetail.getBoxWidth() == null && itemModifyRequestDto.getBoxWidth() != null)
  							|| (beforeItemDetail.getBoxWidth() != null && !beforeItemDetail.getBoxWidth().equals(itemModifyRequestDto.getBoxWidth()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setBoxWidth(itemModifyRequestDto.getBoxWidth());

  						// 박스체적 - 세로
  						if (
  							(beforeItemDetail.getBoxDepth() == null && itemModifyRequestDto.getBoxDepth() != null)
  							|| (beforeItemDetail.getBoxDepth() != null && !beforeItemDetail.getBoxDepth().equals(itemModifyRequestDto.getBoxDepth()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setBoxDepth(itemModifyRequestDto.getBoxDepth());

  						// 박스체적 - 높이
  						if (
  							(beforeItemDetail.getBoxHeight() == null && itemModifyRequestDto.getBoxHeight() != null)
  							|| (beforeItemDetail.getBoxHeight() != null && !beforeItemDetail.getBoxHeight().equals(itemModifyRequestDto.getBoxHeight()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setBoxHeight(itemModifyRequestDto.getBoxHeight());

  						// 박스입수량
  						if(beforeItemDetail.getPiecesPerBox() != itemModifyRequestDto.getPiecesPerBox()) {
  							differentCount++;
  						}
  						itemRegistApprVo.setPiecesPerBox(itemModifyRequestDto.getPiecesPerBox());

  						// UOM
  						if (
  							(beforeItemDetail.getUnitOfMeasurement() == null && itemModifyRequestDto.getUnitOfMeasurement() != null)
							|| (beforeItemDetail.getUnitOfMeasurement() != null && !beforeItemDetail.getUnitOfMeasurement().equals(itemModifyRequestDto.getUnitOfMeasurement()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setOms(String.valueOf(itemModifyRequestDto.getUnitOfMeasurement()));

  						// 유통기간
  						if(beforeItemDetail.getDistributionPeriod() != itemModifyRequestDto.getDistributionPeriod()) {
  							differentCount++;
  						}
  						itemRegistApprVo.setDistributionPeriod(itemModifyRequestDto.getDistributionPeriod());

  						// 포장단위별 용량
  						if (
  							(beforeItemDetail.getSizePerPackage() == null && itemModifyRequestDto.getSizePerPackage() != null)
  							|| (beforeItemDetail.getSizePerPackage() != null && !beforeItemDetail.getSizePerPackage().equals(itemModifyRequestDto.getSizePerPackage()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setSizePerPackage(itemModifyRequestDto.getSizePerPackage());

  						// 용량(중량) 단위
  						if (
  							(beforeItemDetail.getSizeUnit() == null && itemModifyRequestDto.getSizeUnit() != null)
							|| (beforeItemDetail.getSizeUnit() != null && !beforeItemDetail.getSizeUnit().equals(itemModifyRequestDto.getSizeUnit()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setSizeUnit(itemModifyRequestDto.getSizeUnit());

  						// 용량(중량) 단위 - 직접입력
  						if (
  							(beforeItemDetail.getSizeUnitEtc() == null && itemModifyRequestDto.getSizeUnitEtc() != null)
							|| (beforeItemDetail.getSizeUnitEtc() != null && !beforeItemDetail.getSizeUnitEtc().equals(itemModifyRequestDto.getSizeUnitEtc()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setSizeUnitEtc(itemModifyRequestDto.getSizeUnitEtc());

  						// 포장구성 수량
  						if (
  							(beforeItemDetail.getQuantityPerPackage() == null && itemModifyRequestDto.getQuantityPerPackage() != null)
  							|| (beforeItemDetail.getQuantityPerPackage() != null && !beforeItemDetail.getQuantityPerPackage().equals(itemModifyRequestDto.getQuantityPerPackage()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setQtyPerPackage(itemModifyRequestDto.getQuantityPerPackage());

  						// 포장구성 단위
  						if (
  							(beforeItemDetail.getPackageUnit() == null && itemModifyRequestDto.getPackageUnit() != null)
							|| (beforeItemDetail.getPackageUnit() != null && !beforeItemDetail.getPackageUnit().equals(itemModifyRequestDto.getPackageUnit()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setPackageUnit(itemModifyRequestDto.getPackageUnit());

  						// 포장구성 단위 - 직접입력
  						if (
  							(beforeItemDetail.getPackageUnitEtc() == null && itemModifyRequestDto.getPackageUnitEtc() != null)
							|| (beforeItemDetail.getPackageUnitEtc() != null && !beforeItemDetail.getPackageUnitEtc().equals(itemModifyRequestDto.getPackageUnitEtc()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setPackageUnitEtc(itemModifyRequestDto.getPackageUnitEtc());

  						// 표준브랜드
  						if (
  							(beforeItemDetail.getUrBrandId() == null && itemModifyRequestDto.getUrBrandId() != null)
							|| (beforeItemDetail.getUrBrandId() != null && !beforeItemDetail.getUrBrandId().equals(itemModifyRequestDto.getUrBrandId()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setUrBrandId(itemModifyRequestDto.getUrBrandId());

  						// 전시 브랜드
  						if (
  							(beforeItemDetail.getDpBrandId() == null && itemModifyRequestDto.getDpBrandId() != null)
							|| (beforeItemDetail.getDpBrandId() != null && !beforeItemDetail.getDpBrandId().equals(itemModifyRequestDto.getDpBrandId()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setDpBrandId(itemModifyRequestDto.getDpBrandId());

  						// 상품정보제공
  						if (
  							(beforeItemDetail.getIlSpecMasterId() == null && itemModifyRequestDto.getIlSpecMasterId() != null)
							|| (beforeItemDetail.getIlSpecMasterId() != null && !beforeItemDetail.getIlSpecMasterId().equals(itemModifyRequestDto.getIlSpecMasterId()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setIlSpecMasterId(itemModifyRequestDto.getIlSpecMasterId());

  						// 영양정보 표시여부
  						if(beforeItemDetail.isNutritionDisplayYn() != itemModifyRequestDto.isNutritionDisplayYn() ) {
  							differentCount++;
  						}
  						itemRegistApprVo.setNutritionDispYn(itemModifyRequestDto.isNutritionDisplayYn());

  						// 영양정보 표시 기본
  						if (
  							(beforeItemDetail.getNutritionDisplayDefalut() == null && itemModifyRequestDto.getNutritionDisplayDefalut() != null)
							|| (beforeItemDetail.getNutritionDisplayDefalut() != null && !beforeItemDetail.getNutritionDisplayDefalut().equals(itemModifyRequestDto.getNutritionDisplayDefalut()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setNutritionDispDefault(itemModifyRequestDto.getNutritionDisplayDefalut());

  						// 영양분석 단위 1회 분량
  						if (
  							(beforeItemDetail.getNutritionQuantityPerOnce() == null && itemModifyRequestDto.getNutritionQuantityPerOnce() != null)
							|| (beforeItemDetail.getNutritionQuantityPerOnce() != null && !beforeItemDetail.getNutritionQuantityPerOnce().equals(itemModifyRequestDto.getNutritionQuantityPerOnce()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setNutritionQtyPerOnce(itemModifyRequestDto.getNutritionQuantityPerOnce());

  						// 영양분석 단위 총 분량
  						if (
  							(beforeItemDetail.getNutritionQuantityTotal() == null && itemModifyRequestDto.getNutritionQuantityTotal() != null)
							|| (beforeItemDetail.getNutritionQuantityTotal() != null && !beforeItemDetail.getNutritionQuantityTotal().equals(itemModifyRequestDto.getNutritionQuantityTotal()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setNutritionQtyTotal(itemModifyRequestDto.getNutritionQuantityTotal());

  						// 영양성분 기타
  						if (
  							(beforeItemDetail.getNutritionEtc() == null && itemModifyRequestDto.getNutritionEtc() != null)
							|| (beforeItemDetail.getNutritionEtc() != null && !beforeItemDetail.getNutritionEtc().equals(itemModifyRequestDto.getNutritionEtc()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setNutritionEtc(itemModifyRequestDto.getNutritionEtc());

  						// 동영상 URL
  						if (
  							(beforeItemDetail.getVideoUrl() == null && itemModifyRequestDto.getVideoUrl() != null)
							|| (beforeItemDetail.getVideoUrl() != null && !beforeItemDetail.getVideoUrl().equals(itemModifyRequestDto.getVideoUrl()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setVideoUrl(itemModifyRequestDto.getVideoUrl());

  						// 동영상 자동재생여부
  						if(beforeItemDetail.isVideoAutoplayYn() != itemModifyRequestDto.isVideoAutoplayYn() ) {
  							differentCount++;
  						}
  						itemRegistApprVo.setVideoAutoplayYn(itemModifyRequestDto.isVideoAutoplayYn());

  						// 상품상세 기본 정보
  						if (
  							(beforeItemDetail.getBasicDescription() == null && itemModifyRequestDto.getBasicDescription() != null)
							|| (beforeItemDetail.getBasicDescription() != null && !beforeItemDetail.getBasicDescription().equals(itemModifyRequestDto.getBasicDescription()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setBasicDesc(itemModifyRequestDto.getBasicDescription());

  						// 상품상세 주요 정보
  						if (
  							(beforeItemDetail.getDetaillDescription() == null && itemModifyRequestDto.getDetaillDescription() != null)
							|| (beforeItemDetail.getDetaillDescription() != null && !beforeItemDetail.getDetaillDescription().equals(itemModifyRequestDto.getDetaillDescription()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setDetlDesc(itemModifyRequestDto.getDetaillDescription());

  						// 기타정보
  						if (
  							(beforeItemDetail.getEtcDescription() == null && itemModifyRequestDto.getEtcDescription() != null)
							|| (beforeItemDetail.getEtcDescription() != null && !beforeItemDetail.getEtcDescription().equals(itemModifyRequestDto.getEtcDescription()))
  						) {
  							differentCount++;
  						}
  						itemRegistApprVo.setEtcDesc(itemModifyRequestDto.getEtcDescription());

  						// 인증정보
  						boolean certFlag = true; // 인증정보는 항상 저장 - 모두 삭제인 경우와 변경이 안된 경우 구분이 안되기 때문에. differentCount만 체크
  						if(beforeItemCertList.size() != itemModifyRequestDto.getAddItemCertificationList().size()) {
//  							certFlag = true;
  							differentCount++;
  						}else {
  							for(ItemCertificationListVo beforeItemCertInfo : beforeItemCertList) {
  	  							String beforeCertInfoId = beforeItemCertInfo.getIlCertificationId();

  	  							boolean isSame = false;
  	  							for(ItemCertificationDto afterItemCertInfo : itemModifyRequestDto.getAddItemCertificationList()) {
  	  								String afterCertInfoId = afterItemCertInfo.getIlCertificationId();

  	  								if(beforeCertInfoId.equals(afterCertInfoId)) {
  	  									isSame = true;
  	  									break;
  	  								}
  	  							}

  	  							if (isSame == false) {
//  	  								certFlag = true;
  									differentCount++;
  									break;
  								}
  	  						}
  						}

  						// 이미지 정보
  						boolean imageFlag = false;
  						if(beforeItemImageList.size() != itemModifyRequestDto.getItemImageOrderList().size()) {
  							imageFlag = true;
  							differentCount++;
  						}else {
  					        if (
  					        	!itemModifyRequestDto.getItemImageNameListToDelete().isEmpty() // 삭제할 파일 목록 없음
								|| !itemModifyRequestDto.getItemImageUploadResultList().isEmpty() // 신규 업로드 파일 목록 없음
								|| itemModifyRequestDto.isImageSortOrderChanged() // 정렬 순서 변경 없음
  					        )
  					        {
								imageFlag = true;
								differentCount++;
  					        }
  						}

  						// 영양 정보
  						boolean nurFlag = true; // 영양정보는 항상 저장 - 모두 삭제인 경우와 변경이 안된 경우 구분이 안되기 때문에. differentCount만 체크
  						if(beforeItemNurList.size() != itemModifyRequestDto.getAddItemNutritionDetailList().size()) {
//  							nurFlag = true;
  							differentCount++;
  						}else {
  							for(ItemNutritionDetailVo beforeItemNurInfo : beforeItemNurList) {
  	  							String beforeNurCode = beforeItemNurInfo.getNutritionCode();
  	  							Double beforeNurPercnt = beforeItemNurInfo.getNutritionPercent() == null ? new Double(0.0) : beforeItemNurInfo.getNutritionPercent();
  	  							Double beforeNurQty	   = beforeItemNurInfo.getNutritionQuantity() == null ? new Double(0.0) : beforeItemNurInfo.getNutritionQuantity();

  	  							boolean isSame = false;
  	  							for(ItemNutritionDetailDto afterItemNurInfo : itemModifyRequestDto.getAddItemNutritionDetailList()) {
  	  								String afterNurCode = afterItemNurInfo.getNutritionCode();
  	  								Double afterNurPercnt = afterItemNurInfo.getNutritionPercent() == null ? new Double(0.0) : afterItemNurInfo.getNutritionPercent();
  	  								Double afterNurQty	   = afterItemNurInfo.getNutritionQuantity() == null ? new Double(0.0) : afterItemNurInfo.getNutritionQuantity();

  	  								if(beforeNurCode.equals(afterNurCode) && beforeNurPercnt.compareTo(afterNurPercnt) == 0 && beforeNurQty.compareTo(afterNurQty) == 0 ) {
  	  									isSame = true;
  	  									break;
  	  								}
  	  							}

  	  							if (isSame == false) {
//  	  								nurFlag = true;
  									differentCount++;
  									break;
  								}
  	  						}
  						}

  						// 고시정보
  						boolean specFlag = true; // 고시정보는 항상 저장 - insert / delete로 처리하기 때문. differentCount만 체크
  						if(beforeItemSpecList.size() != itemModifyRequestDto.getAddItemSpecValueList().size()) {
//  							specFlag = true;
  							differentCount++;
  						}else {
  							for(ItemSpecValueVo beforeItemSpecInfo : beforeItemSpecList) {
  	  							String beforeSpecId = String.valueOf(beforeItemSpecInfo.getIlSpecFieldId());
  	  							Boolean beforeDirectYn = beforeItemSpecInfo.getDirectYn() == null ? false : beforeItemSpecInfo.getDirectYn();
  	  							String beforeSpecFieldValue = beforeItemSpecInfo.getSpecFieldValue();

  	  							boolean isSame = false;
  	  							for(ItemSpecValueRequestDto afterItemSpecInfo : itemModifyRequestDto.getAddItemSpecValueList()) {
  	  								String afterSpecId = String.valueOf(afterItemSpecInfo.getIlSpecFieldId());
  	  	  							Boolean afterDirectYn = afterItemSpecInfo.getDirectYn() == null ? false : afterItemSpecInfo.getDirectYn();
  	  	  							String afterSpecFieldValue = afterItemSpecInfo.getSpecFieldValue();

  	  								if(beforeSpecId.equals(afterSpecId)) {
  	  									if (beforeDirectYn) {
  	  										if (afterDirectYn && beforeSpecFieldValue.equals(afterSpecFieldValue)) {
  	  	  	  									isSame = true;
  	  	  	  									break;
  	  										}
  	  									}
  	  									else {
  	  	  									isSame = true;
  	  	  									break;
  	  									}
  	  								}
  	  							}

  	  							if (isSame == false) {
//  	  								specFlag = true;
  									differentCount++;
  									break;
  								}
  	  						}
  						}


  						if(differentCount == 0) {
  							enums = ItemEnums.ItemApprProcStatus.NOT_DIFFERENT_ITEM;
  						}
  						else if(differentCount > 0){
  							if(itemApprInfo == null || itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
  									|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())
  									|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode())
  									|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED_BY_SYSTEM.getCode())
  									|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {	//승인 내역이 없거나, 반려/요청철회/폐기/승인완료(시스템)/승인완료 상태일때
  								goodsItemRegisterService.addItemAppr(itemRegistApprVo);
  								String ilItemApprId = itemRegistApprVo.getIlItemApprId();

  								System.out.println("#### ilItemApprId --->"+ itemRegistApprVo.getIlItemApprId());
  								itemRegistApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
  								itemRegistApprVo.setStatusCmnt(null);
  								goodsItemRegisterService.addItemApprStatusHistory(itemRegistApprVo);

  								if(certFlag) {
  									for(ItemCertificationDto afterItemCertInfo : itemModifyRequestDto.getAddItemCertificationList()) {
  										ItemCertificationApprVo itemCertificationApprVo = new ItemCertificationApprVo();
  										itemCertificationApprVo.setIlItemApprId(ilItemApprId);
  										itemCertificationApprVo.setIlItemCd(itemModifyRequestDto.getIlItemCode());
  										itemCertificationApprVo.setIlCertificationId(afterItemCertInfo.getIlCertificationId());
  										itemCertificationApprVo.setCertificationDesc(afterItemCertInfo.getCertificationDescription());
  										itemCertificationApprVo.setCreateId(userId);

  										goodsItemRegisterService.addItemCertificationAppr(itemCertificationApprVo);
  									}
  								}

  								if(imageFlag) {
  							        // 상품 이미지 정렬 순서 배열의 길이가 0 인 경우 : 모두 삭제로 간주함
//  							        if (itemModifyRequestDto.getItemImageOrderList().isEmpty()) {
//  							        	goodsItemModifyService.removeItemImage(beforeItemImageList, null, true, itemModifyRequestDto.getImageRootStoragePath()); // 관련 품목 이미지 파일 모두 삭제
//  							        }

  							        // 마스터 품목 수정 화면에서 삭제 지시한 이미지 파일명 목록 존재시
//  							        if (!itemModifyRequestDto.getItemImageNameListToDelete().isEmpty()) {
//
//  							            // 삭제 대상 품목 이미지 파일들을 일괄 삭제 ( 리사이징된 파일 포함 )
//  							            // 이미지 파일 삭제 후 oldItemImageList 내에서 관련 Vo 도 삭제
//  							        	goodsItemModifyService.removeItemImage(beforeItemImageList, itemModifyRequestDto.getItemImageNameListToDelete(), false, itemModifyRequestDto.getImageRootStoragePath());
//
//  							        }

  									List<ItemImageRegisterVo> totalItemImageRegisterList = new ArrayList<>(); // 기존 데이터 삭제 후 신규 저장할 품목 이미지 Vo 목록

  							        // 신규 업로드 품목 이미지 존재시 : 해당 품목 이미지 등록 VO 와 해당 품목 이미지의 리사이징 파일 생성
  							        if (!itemModifyRequestDto.getItemImageUploadResultList().isEmpty()) {
  							            totalItemImageRegisterList = goodsItemModifyService.generateNewItemImageList(itemModifyRequestDto);
  							        }

  							        // 화면에서 전송한 상품 이미지 정렬 순서 배열 존재시 : 삭제된 이미지 제외한 기존 등록된 이미지 Data 참조하여 품목 이미지 Vo 목록 생성
  							        if (!itemModifyRequestDto.getItemImageOrderList().isEmpty()) {

  							            totalItemImageRegisterList.addAll(goodsItemModifyService.generateReSortedItemImageList(itemModifyRequestDto, beforeItemImageList));

  							        }

  							        /*
  							         * 신규 품목 이미지 VO Insert
  							         */
  							        for (ItemImageRegisterVo itemImageRegisterVo : totalItemImageRegisterList) {
  							        	itemImageRegisterVo.setIlItemApprId(ilItemApprId);
  							        	itemImageRegisterVo.setCreateId(Long.valueOf(userId));
  							        	goodsItemRegisterService.addItemImageAppr(itemImageRegisterVo);
  							        }
  								}

  								if(nurFlag) {
  									int i = 0;
  									for(ItemNutritionDetailDto afterItemNurInfo : itemModifyRequestDto.getAddItemNutritionDetailList()) {
  										ItemNutritionApprVo itemNutritionApprVo = new ItemNutritionApprVo();
  										itemNutritionApprVo.setIlItemApprId(ilItemApprId);
  										itemNutritionApprVo.setIlItemCd(itemModifyRequestDto.getIlItemCode());
  										itemNutritionApprVo.setErpNutritionPercent(afterItemNurInfo.getErpNutritionPercent());
  										itemNutritionApprVo.setErpNutritionQuantity(afterItemNurInfo.getErpNutritionQuantity());
  										itemNutritionApprVo.setNutritionCode(afterItemNurInfo.getNutritionCode());
  										itemNutritionApprVo.setNutritionPercent(afterItemNurInfo.getNutritionPercent());
  										itemNutritionApprVo.setNutritionQuantity(afterItemNurInfo.getNutritionQuantity());
  										itemNutritionApprVo.setSort(i);
  										i++;
  										itemNutritionApprVo.setCreateId(userId);

  										goodsItemRegisterService.addItemNutritionAppr(itemNutritionApprVo);
  									}
  								}

  								if(specFlag) {
  									for(ItemSpecValueRequestDto afterItemSpecInfo : itemModifyRequestDto.getAddItemSpecValueList()) {


  										ItemSpecApprVo itemSpecApprVo = new ItemSpecApprVo();
  										itemSpecApprVo.setIlItemApprId(ilItemApprId);
  										itemSpecApprVo.setIlItemCode(itemModifyRequestDto.getIlItemCode());
  										itemSpecApprVo.setIlSpecFieldId(afterItemSpecInfo.getIlSpecFieldId());
  										itemSpecApprVo.setDirectYn(afterItemSpecInfo.getDirectYn());
  										itemSpecApprVo.setSpecFieldValue(afterItemSpecInfo.getSpecFieldValue());
  										itemSpecApprVo.setCreateId(Long.valueOf(userId));

  										goodsItemRegisterService.addItemSpecAppr(itemSpecApprVo);
  									}
  								}

  								if(beforeItemDetail.getItemStatusTp().equals("ITEM_STATUS_TP.REGISTER")) {
  									goodsRegistBiz.updateGoodsSaleStatusToWaitByItemAppr(itemModifyRequestDto.getIlItemCode());
  								}

  								enums = ItemEnums.ItemApprProcStatus.CLINET_APPR_REQUEST;
  							}
  							else {
  								enums = ItemEnums.ItemApprProcStatus.APPR_DUPLICATE;
  							}
  						}
  					}
  				}
  				else {
  					enums = GoodsEnums.GoodsApprProcStatus.NONE_GOODS_ID;
  				}
  			}
  		}
  		else {
  			if(companyType.equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//관리자 권한이라면
  				itemModifyRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode());
  				ItemRegistApprVo itemApprInfo = goodsItemRegisterService.itemApprInfo(userId, itemModifyRequestDto.getIlItemCode(), null, itemModifyRequestDto.getApprKindTp());

  				if(itemApprInfo != null && itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())) {		//거래처 상품 수정 승인 요청 내역이 있다면
  					enums = ItemEnums.ItemApprProcStatus.CLIENT_APPR_DUPLICATE;
  				}
  			}
  			else if(companyType.equals(CompanyEnums.CompanyType.CLIENT.getCode()) && clientType.equals(CompanyEnums.ClientType.CLIENT.getCode())) {	//거래처 권한이라면
  				enums = ItemEnums.ItemApprProcStatus.NONE_APPR;
  			}
  		}

  		return ApiResult.result(enums);
  	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BaseException.class, Exception.class })
    public ApiResult<?> refactoringImage(ItemModifyRequestDto itemModifyRequestDto) throws Exception {
        /*
         * 품목별 이미지 재등록 실행
         */
        goodsItemModifyService.refactoringImage(itemModifyRequestDto);

        return ApiResult.success();

    }

  	/**
	 * 품목 마스터 변경내역 저장
	 * tranMode : INSERT, UPDATE 구분
	 * beforeItemDatas : 현재 DB에 저장되어 있는 값
	 * afterItemDatas : 화면에서 가져온 RequestDto 값
	 **/
	private int addItemMasterChangeLog(String tranMode, String ilItemCode, Map<String, String> beforeItemDatas, Map<String, String> afterItemDatas, String timestamp) throws BaseException, Exception {

		changeTranNum = 0;
		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();

		if(!afterItemDatas.isEmpty()) {
			afterItemDatas.forEach((afterKey, afterValue)-> {
				ItemColumnComment itemColumnComment = ItemColumnComment.findByComment(afterKey);

				if("INSERT".equals(tranMode) && afterValue != null && itemColumnComment != null) {
					ItemChangeLogVo itemChangeLogVo = ItemChangeLogVo.builder()
						.ilItemCd(ilItemCode)
						.tableNm("IL_ITEM")
						.tableIdOrig("0")
						.tableIdNew(ilItemCode)
						.beforeData("")		//저장시에는 beforeData는 없음
						.afterData(afterValue)
						.columnNm(afterKey)
						.columnLabel(itemColumnComment.getCodeName())
						.createId(userId)
						.createDt(timestamp)
						.build();

					//람다식 사용으로 인해서 Class 단위 전역변수로 선언 해야함
					changeTranNum += goodsItemRegisterService.addItemChangeLog(itemChangeLogVo);
				}
				else if("UPDATE".equals(tranMode) && itemColumnComment != null) {

					beforeItemDatas.forEach((beforeKey, beforeValue)-> {

						if(afterValue != null && afterKey.equals(beforeKey) && !afterValue.equals(beforeValue)) {

							log.info("beforeKey : " + beforeKey + "  beforeValue : " + beforeValue + "// afterKey : " + afterKey + "  afterValue : " + afterValue);

							ItemChangeLogVo itemChangeLogVo = ItemChangeLogVo.builder()
								.ilItemCd(ilItemCode)
								.tableNm("IL_ITEM")
								.tableIdOrig(ilItemCode)
								.tableIdNew(ilItemCode)
								.beforeData(beforeValue)
								.afterData(afterValue)
								.columnNm(afterKey)
								.columnLabel(itemColumnComment.getCodeName())
								.createId(userId)
								.createDt(timestamp)
								.build();

							//람다식 사용으로 인해서 Class 단위 전역변수로 선언 해야함
							changeTranNum += goodsItemRegisterService.addItemChangeLog(itemChangeLogVo);
						}
					});
				}
			});
		}
		return changeTranNum;
	}

	/**
	 * 품목서브 항목 변경내역 저장
	 * tranMode : INSERT, UPDATE 구분
	 * tableKind : 변경내역 저장 테이블명
	 * beforeItemDatas : 현재 DB에 저장되어 있는 값
	 * afterItemDatas : 화면에서 가져온 RequestDto 값
	 **/
	private int addItemSubChangeLog(String tranMode, String tableKind, String ilItemCode, List<?> beforeItemDatas, List<?> afterItemDatas, String timestamp) throws BaseException, Exception {
		//테이블별 ID , DATA , COLUMN COMMENT 컬럼을 지정
		itemEtcColumnComment = ItemEtcColumnComment.findByInfo(tableKind);
		sameValue = 0;

		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();

		if(tableKind.equals("IL_ITEM_NUTRITION_UNIT")) {
			tableKind = "IL_ITEM_NUTRITION";
		}

		if("INSERT".equals(tranMode) && afterItemDatas != null) {
			for (Object afterGoodsValue : afterItemDatas) {
				Map<String, String> afterDataMap = BeanUtils.describe(afterGoodsValue);

				ItemChangeLogVo itemChangeLogVo = new ItemChangeLogVo();
				itemChangeLogVo.setIlItemCd(ilItemCode);
				itemChangeLogVo.setTableNm(tableKind);
				itemChangeLogVo.setColumnLabel(itemEtcColumnComment.getComment());
				itemChangeLogVo.setCreateId(userId);
				itemChangeLogVo.setCreateDt(timestamp);

				afterDataMap.forEach((afterKey, afterValue)-> {
					if(itemEtcColumnComment != null) {
						if(itemEtcColumnComment.getIdColumn().equals(afterKey)){
							itemChangeLogVo.setTableIdOrig("0");
							itemChangeLogVo.setTableIdNew(afterValue);
							itemChangeLogVo.setColumnNm(afterKey);
						}

						if(itemEtcColumnComment.getDataColumn().equals(afterKey)){
							itemChangeLogVo.setBeforeData("");
							itemChangeLogVo.setAfterData(afterValue);
						}
					}
				});

				changeTranNum += goodsItemRegisterService.addItemChangeLog(itemChangeLogVo);
			}
		}else if("UPDATE".equals(tranMode) && beforeItemDatas != null && afterItemDatas != null) {

				//기존과 동일한 값인지 비교
				for (Object beforeGoodsValue : beforeItemDatas) {
					Map<String, String> beforeDataMap = BeanUtils.describe(beforeGoodsValue);

					beforeDataMap.forEach((beforeKey, beforeValue)-> {

						if(itemEtcColumnComment != null && itemEtcColumnComment.getIdColumn().equals(beforeKey)) {
							for (Object afterGoodsValue : afterItemDatas) {
								try {
									Map<String, String> afterDataMap = BeanUtils.describe(afterGoodsValue);

									afterDataMap.forEach((afterKey, afterValue)-> {
										log.info("beforeKey : " + beforeKey + "  beforeValue : " + beforeValue + "// afterKey : " + afterKey + "  afterValue : " + afterValue);
										if(afterValue != null && beforeKey.equals(afterKey) && beforeValue.equals(afterValue)) {

											sameValue++;
										}
									});
								} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
									log.error("변경이력 sameValue Error Log", e);
								}
							}
						}
					});
				}

//				log.info("sameValue : " + sameValue);
//				log.info("beforeItemDatas.size() : " + beforeItemDatas.size());
//				log.info("afterItemDatas.size() : " + afterItemDatas.size());

				if(sameValue != beforeItemDatas.size() || sameValue != afterItemDatas.size()) {

					for (Object beforeGoodsValue : beforeItemDatas) {
						Map<String, String> beforeDataMap = BeanUtils.describe(beforeGoodsValue);

						ItemChangeLogVo itemChangeLogVo = new ItemChangeLogVo();
						itemChangeLogVo.setIlItemCd(ilItemCode);
						itemChangeLogVo.setTableNm(tableKind);
						itemChangeLogVo.setColumnLabel(itemEtcColumnComment.getComment());
						itemChangeLogVo.setCreateId(userId);
						itemChangeLogVo.setCreateDt(timestamp);

						beforeDataMap.forEach((beforeKey, beforeValue)-> {
							if(itemEtcColumnComment != null) {
								if(itemEtcColumnComment.getIdColumn().equals(beforeKey)){
									itemChangeLogVo.setTableIdOrig(beforeValue);
									itemChangeLogVo.setTableIdNew("0");
									itemChangeLogVo.setColumnNm(beforeKey);
								}

								if(itemEtcColumnComment.getDataColumn().equals(beforeKey)){
									itemChangeLogVo.setBeforeData(beforeValue);
									itemChangeLogVo.setAfterData("");
								}
							}
						});

						changeTranNum += goodsItemRegisterService.addItemChangeLog(itemChangeLogVo);
					}

					for (Object afterGoodsValue : afterItemDatas) {
						Map<String, String> afterDataMap = BeanUtils.describe(afterGoodsValue);

						ItemChangeLogVo itemChangeLogVo = new ItemChangeLogVo();
						itemChangeLogVo.setIlItemCd(ilItemCode);
						itemChangeLogVo.setTableNm(tableKind);
						itemChangeLogVo.setColumnLabel(itemEtcColumnComment.getComment());
						itemChangeLogVo.setCreateId(userId);
						itemChangeLogVo.setCreateDt(timestamp);

						afterDataMap.forEach((afterKey, afterValue)-> {
							if(itemEtcColumnComment != null) {
								if(itemEtcColumnComment.getIdColumn().equals(afterKey)){
									itemChangeLogVo.setTableIdOrig("0");
									itemChangeLogVo.setTableIdNew(afterValue);
									itemChangeLogVo.setColumnNm(afterKey);
								}

								if(itemEtcColumnComment.getDataColumn().equals(afterKey)){
									itemChangeLogVo.setBeforeData("");
									itemChangeLogVo.setAfterData(afterValue);
								}
							}
						});

						changeTranNum += goodsItemRegisterService.addItemChangeLog(itemChangeLogVo);
					}
				}

		}

		return changeTranNum;
	}

	@Override
	public MessageCommEnum itemApprAfterProc(String ilItemApprId, String ilItemCd, String apprKindTp) throws Exception {
		// TODO Auto-generated method stub
		MessageCommEnum enums = BaseEnums.Default.FAIL;

		ItemRegistApprVo itemApprInfo = goodsItemModifyService.getItemApprInfo(ilItemApprId, ilItemCd, apprKindTp);

		if(itemApprInfo != null) {

			if(itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {				//최종 승인일때만 상품 변경내역 저장을 진행

				//품목 기존 마스터정보
		        MasterItemVo beforeItemMasterData = goodsItemModifyService.getMasterItem(ilItemCd, apprKindTp, null);

		  		// 품목 정보제공고시
		        List<ItemSpecValueVo> beforeItemSpecList = goodsItemRegisterService.getItemSpecValueList(ilItemCd, null);

				// 인증정보
		  		List<ItemCertificationListVo> beforeItemCertList = goodsItemRegisterService.getItemCertificationList(ilItemCd, null);

		  		// 영양정보
		  		List<ItemNutritionDetailVo> beforeItemNurList = goodsItemRegisterService.getItemNutritionDetailList(ilItemCd, null);

		        // 품목 수정정보 업데이트
		        goodsItemModifyService.putItemMasterApprInfo(itemApprInfo);

		        // 품목 인증 정보 업데이트
		        List<ItemCertificationApprVo> itemCertApprList = goodsItemModifyService.getItemCertApprList(ilItemApprId, ilItemCd);
	        	goodsItemModifyService.putItemCertApprInfo(ilItemCd, itemCertApprList);

		        // 이미지 정보
		        List<ItemImageApprVo> itemImageApprList = goodsItemModifyService.getItemImageApprList(ilItemApprId, ilItemCd);
		        if(itemImageApprList.size() > 0) {
		        	goodsItemModifyService.putItemImageApprInfo(ilItemCd, itemImageApprList);
		        }

		        //영양정보
		        List<ItemNutritionApprVo> itemNutrApprList = goodsItemModifyService.getItemNutrApprList(ilItemApprId, ilItemCd);
		        goodsItemModifyService.putItemNutrApprInfo(ilItemCd, itemNutrApprList);

		        // 정보제공
		        List<ItemSpecApprVo> itemSpecApprList = goodsItemModifyService.getItemSpecApprList(ilItemApprId, ilItemCd);
		        goodsItemModifyService.putItemSpecApprInfo(ilItemCd, itemSpecApprList);


		        /////////////////////////////////////////////////////////////// 변경내역 기록
		        // 정보제공 수정 데이터
		        MasterItemVo afterItemMasterData = goodsItemModifyService.getMasterItem(ilItemCd, apprKindTp, null);
		        List<ItemSpecValueVo> afterItemSpecList = goodsItemRegisterService.getItemSpecValueList(ilItemCd, null);
		  		List<ItemCertificationListVo> afterItemCertList = goodsItemRegisterService.getItemCertificationList(ilItemCd, null);
				List<ItemNutritionDetailVo> afterItemNurList = goodsItemRegisterService.getItemNutritionDetailList(ilItemCd, null);

				//품목변경 내역
				int chgLogNum = 0;
				Map<String, String> beforeItemData = BeanUtils.describe(beforeItemMasterData);
		  		Map<String, String> afterItemData = BeanUtils.describe(afterItemMasterData);

		  		String timestamp = DateUtil.getCurrentDate("yyyyMMddHHmmss");

		  		chgLogNum = addItemMasterChangeLog("UPDATE", ilItemCd, beforeItemData, afterItemData, timestamp);

		  		chgLogNum += addItemSubChangeLog("UPDATE", "IL_ITEM_SPEC_VALUE", ilItemCd, beforeItemSpecList, afterItemSpecList, timestamp);

				chgLogNum += addItemSubChangeLog("UPDATE", "IL_ITEM_CERTIFICATION", ilItemCd, beforeItemCertList, afterItemCertList, timestamp);

		        enums = BaseEnums.Default.SUCCESS;


		        // 상품 이미지 등록 처리 수정
				goodsDetailImageBiz.getUpdateItemInfoForDetailImage(null, beforeItemMasterData, afterItemMasterData, beforeItemNurList ,afterItemNurList, beforeItemSpecList, afterItemSpecList);

			}

		}else {
			enums = GoodsEnums.GoodsApprProcStatus.NONE_APPR;
		}

		log.info("enums : " + enums);

		return enums;
	}

	@Override
	public int deleteItemPriceOrigin(String ilItemCode, String priceApplyStartDate) throws Exception {
		int result = goodsItemModifyService.deleteItemPriceOrigin(ilItemCode, priceApplyStartDate);
		if(result > 0){
			goodsRegistBiz.spGoodsPriceUpdateWhenItemPriceChanges(ilItemCode);
		}
		return result;
	}

	@Override
	public List<ItemPriceVo> getItemPriceListByDate(String ilItemCode, String targetDate) {
		return goodsItemModifyService.getItemPriceListByDate(ilItemCode, targetDate);
	}

	/*
     * 마스터 품목 정보 수정 관련 로직 End
     */

}
