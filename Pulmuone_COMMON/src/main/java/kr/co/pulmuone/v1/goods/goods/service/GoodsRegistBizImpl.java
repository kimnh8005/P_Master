package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.approval.auth.service.ApprovalAuthBiz;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.*;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.ItemImagePrefixBySize;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsRegistCategoryMapper;
import kr.co.pulmuone.v1.comm.mapper.policy.benefit.PolicyBenefitEmployeeMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.ImageUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.discount.service.GoodsDiscountBiz;
import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.*;
import kr.co.pulmuone.v1.goods.item.dto.ItemReturnPeriodResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternListVo;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemRegisterBiz;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemStoreBiz;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsVo;
import kr.co.pulmuone.v1.goods.price.service.GoodsPriceBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;


/**
* <PRE>
* 상품 등록/수정 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자	  :  작성내역
* -----------------------------------------------------------------------
*  0.1	2020. 10. 13.			   임상건		  최초작성
* =======================================================================
* </PRE>
*/
@Service
@Slf4j
public class GoodsRegistBizImpl  implements GoodsRegistBiz {

	@Autowired
	GoodsRegistService goodsRegistService;

	@Autowired
	public GoodsPriceBiz goodsPriceBiz;

	@Autowired
	public GoodsDiscountBiz goodsDiscountBiz;

	@Autowired
	public ApprovalAuthBiz approvalAuthBiz;

	@Autowired
	public GoodsRegistCategoryMapper goodsRegistCategoryMapper;

	@Autowired
	private GoodsItemRegisterBiz goodsItemRegisterBiz;

	@Autowired
	private GoodsItemStoreBiz goodsItemStoreBiz;

	@Autowired
	public PolicyBenefitEmployeeMapper policyBenefitEmployeeMapper;

	@Autowired
	private GoodsDetailImageBiz goodsDetailImageBiz;

	int changeTranNum = 0;								//상품 변경이력 Transaction 갯수
	int sameValue = 0;									//beforeData, afterData가 동일한 갯수
	GoodsEtcColumnComment goodsEtcColumnComment = null;	//테이블별 ID , DATA , COLUMN COMMENT 컬럼을 지정


	/**
	* @Desc 마스터품목 내역
	* @param goodsRegistRequestDto
	* @return ApiResult
	 * @throws Exception
	*/
	@Override
	public ApiResult<?> getItemDetail(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception{
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		//마스터품목 기본내역
		GoodsRegistVo itemDetail = goodsRegistService.getItemDetail(goodsRegistRequestDto);
		goodsRegistResponseDto.setRow(itemDetail);

		//임직원 할인 정보 > 임직원 기본할인 정보
		GoodsPriceInfoResultVo goodsBaseDiscountEmployeeList = goodsRegistService.goodsBaseDiscountEmployeeList(goodsRegistRequestDto.getIlItemCode());
		goodsRegistResponseDto.setGoodsBaseDiscountEmployeeList(goodsBaseDiscountEmployeeList);

		//판매/전시 > 재고운영형태, 전알미감재고 정보
		GoodsRegistStockInfoVo goodsStockInfo = goodsRegistService.goodsStockInfo(goodsRegistRequestDto);
		goodsRegistResponseDto.setGoodsStockInfo(goodsStockInfo);

		// 매장판매 가능시 품목 매장정보 조회
		if("Y".equals(itemDetail.getStoreYn())) {
			goodsRegistResponseDto.setItemStoreList(goodsItemStoreBiz.getStoreList(itemDetail.getIlItemCode()));
		}

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	* @Desc  상품정보 제공고시(품목내역)
	* @param GoodsRegistRequestDto
	* @return ApiResult
	* @throws Exception
	*/
	@Override
	public ApiResult<?> getItemSpecValueList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		List<GoodsRegistVo> itemSpecValueList = goodsRegistService.getItemSpecValueList(goodsRegistRequestDto);
		goodsRegistResponseDto.setRows(itemSpecValueList);

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	* @Desc  상품 영양정보(품목내역)
	* @param String
	* @return ApiResult
	* @throws Exception
	*/
	@Override
	public ApiResult<?> getItemNutritionList(String ilItemCode) throws Exception {
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		List<GoodsRegistVo> iItemNutritionList = goodsRegistService.getItemNutritionList(ilItemCode);
		goodsRegistResponseDto.setRows(iItemNutritionList);

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	* @Desc  상품 인증정보(품목내역)
	* @param String
	* @return ApiResult
	* @throws Exception
	*/
	@Override
	public ApiResult<?> getItemCertificationList(String ilItemCode) throws Exception {
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		List<GoodsRegistVo> itemCertificationList = goodsRegistService.getItemCertificationList(ilItemCode);
		goodsRegistResponseDto.setRows(itemCertificationList);

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	 * @Desc  상품 상세 이미지(품목내역)
	 * @param String
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getItemImageList(String ilItemCode) throws Exception {
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		List<GoodsRegistVo> itemImageList = goodsRegistService.getItemImageList(ilItemCode);
		goodsRegistResponseDto.setRows(itemImageList);

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	* @Desc  상품 등록
	* @param goodsRegistRequestDto
	* @return ApiResult
	* @throws Exception
	*/
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addGoods(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception, BaseException {

		GoodsRegistVo duplicateGoods = goodsRegistService.duplicateGoods(goodsRegistRequestDto);
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		if(duplicateGoods != null && !GoodsType.PACKAGE.getCode().equals(goodsRegistRequestDto.getGoodsType())) {
			goodsRegistResponseDto.setIlGoodsId(duplicateGoods.getIlGoodsId());
			goodsRegistResponseDto.setGoodsType(duplicateGoods.getGoodsType());
			goodsRegistResponseDto.setSaleStatus(duplicateGoods.getSaleStatus());

			return ApiResult.result(goodsRegistResponseDto, GoodsEnums.GoodsAddStatusTypes.DUPLICATE_GOODS);
		}
		else {
			//구매 허용 체크박스 값 세팅
			goodsRegistRequestDto.setPurchaseMemberYn("N");
			goodsRegistRequestDto.setPurchaseEmployeeYn("N");
			goodsRegistRequestDto.setPurchaseNonmemberYn("N");

			for(String purchaseTargetType : goodsRegistRequestDto.getPurchaseTargetType()) {

				if(GoodsEnums.GoodsPurchaseTypes.MEMBER.getCode().equals(purchaseTargetType)) {	//회원 구매여부 세팅
					goodsRegistRequestDto.setPurchaseMemberYn("Y");
				}

				if(GoodsEnums.GoodsPurchaseTypes.EMPLOYEE.getCode().equals(purchaseTargetType)) {	//임직원 구매여부 세팅
					goodsRegistRequestDto.setPurchaseEmployeeYn("Y");
				}

				if(GoodsEnums.GoodsPurchaseTypes.NONMEMBER.getCode().equals(purchaseTargetType)) {	//비회원 구매여부 세팅
					if(GoodsEnums.GoodsType.DAILY.getCode().equals(goodsRegistRequestDto.getGoodsType())) {	//일일상품이라면
						goodsRegistRequestDto.setPurchaseNonmemberYn("N");							// 비회원은 무조건 'N'(비회원이 존재하지 않음)
					}
					else {
						goodsRegistRequestDto.setPurchaseNonmemberYn("Y");
						goodsRegistRequestDto.setLimitMaximumCnt(null);
					}
				}
			}

			//판매허용범위(PC/Mobile) 체크박스 값 세팅
			goodsRegistRequestDto.setDisplayWebPcYn("N");
			goodsRegistRequestDto.setDisplayWebMobileYn("N");
			goodsRegistRequestDto.setDisplayAppYn("N");

			for(String goodsDisplayType : goodsRegistRequestDto.getGoodsDisplayType()) {
				if(GoodsEnums.GoodsDisplayTypes.WEB_PC.getCode().equals(goodsDisplayType)) {		//WEB PC 전시여부 값 세팅
					goodsRegistRequestDto.setDisplayWebPcYn("Y");
				}

				if(GoodsEnums.GoodsDisplayTypes.WEB_MOBILE.getCode().equals(goodsDisplayType)) {	//WEB MOBILE 전시여부 세팅
					goodsRegistRequestDto.setDisplayWebMobileYn("Y");
				}

				if(GoodsEnums.GoodsDisplayTypes.APP.getCode().equals(goodsDisplayType)) {			//APP 전시여부 세팅
					goodsRegistRequestDto.setDisplayAppYn("Y");
				}
			}

			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsRegistRequestDto.getGoodsType()) && "N".equals(goodsRegistRequestDto.getGoodsPackageBasicDescYn())) {	//묶음상품이고 상품 상세 기본 정보 옵션이 '구성상품 상세' 라면
				goodsRegistRequestDto.setGoodsPackageBasicDesc("");	//상품 상세 기본 정보 내용을 저장하지 않는다.
			}

			//상세 하단 공지1 첨부 이미지 URL
			if(goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList() != null) {
				int noticeBelow1ImageCnt = goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList().size();

				if(noticeBelow1ImageCnt > 0) {
					for (UploadFileDto uploadFileDto : goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList()) {
						goodsRegistRequestDto.setNoticeBelow1ImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
					}
				}
			}

			//상세 하단 공지2 첨부 이미지 URL
			if(goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList() != null) {
				int noticeBelow2ImageCnt = goodsRegistRequestDto.getNoticeBelow2ImageUploadResultList().size();

				if(noticeBelow2ImageCnt > 0) {
					for (UploadFileDto uploadFileDto : goodsRegistRequestDto.getNoticeBelow2ImageUploadResultList()) {
						goodsRegistRequestDto.setNoticeBelow2ImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
					}
				}
			}

			//일일 상품 녹즙 클렌즈 옵션 사용 여부
			if(goodsRegistRequestDto.isGreenJuiceCleanseOpt()) {
				goodsRegistRequestDto.setGreenJuiceCleanseOptYn("Y");
			}
			else {
				goodsRegistRequestDto.setGreenJuiceCleanseOptYn("N");
			}

		// 일반, 묶음 상품이 아니면 선물하기 미대상 NA
		if(!GoodsType.PACKAGE.getCode().equals(goodsRegistRequestDto.getGoodsType()) && !GoodsType.NORMAL.getCode().equals(goodsRegistRequestDto.getGoodsType())) {
			goodsRegistRequestDto.setPresentYn(PresentYn.NA.getCode());
		}

		//세션 정보 불러오기
		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();
		goodsRegistRequestDto.setCreateId(userId);

			//상품 마스터 항목 등록
			int addGoodsInt = goodsRegistService.addGoods(goodsRegistRequestDto);

			if (addGoodsInt > 0) {
				//상품변경 내역
				GoodsRegistResponseDto beforeGoodsDatas = new GoodsRegistResponseDto();
				int chgLogNum = 0;
				chgLogNum = goodsChangeAssemble("INSERT", beforeGoodsDatas, goodsRegistRequestDto);

				//기본 정보 > 전시 카테고리 저장
				if(goodsRegistRequestDto.getDisplayCategoryList() != null) {
					for(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto : goodsRegistRequestDto.getDisplayCategoryList()) {
						goodsRegistCategoryRequestDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRegistCategoryRequestDto.setCreateId(goodsRegistRequestDto.getCreateId());

						goodsRegistService.addGoodsCategory(goodsRegistCategoryRequestDto);
					}

//					log.info("goodsRegistRequestDto.getDisplayCategoryList() : " + goodsRegistRequestDto.getDisplayCategoryList());
				}

				//기본 정보 > 몰인몰 카테고리 저장
				if(goodsRegistRequestDto.getMallInMallCategoryList() != null) {
					for(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto : goodsRegistRequestDto.getMallInMallCategoryList()) {
						goodsRegistCategoryRequestDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRegistCategoryRequestDto.setCreateId(goodsRegistRequestDto.getCreateId());
						goodsRegistCategoryRequestDto.setBasicYn(goodsRegistCategoryRequestDto.getMallInMallBasicYn());

						goodsRegistService.addGoodsCategory(goodsRegistCategoryRequestDto);
					}

//					log.info("goodsRegistRequestDto.getMallInMallCategoryList() : " + goodsRegistRequestDto.getMallInMallCategoryList());
				}


//			log.info("할인정보 연동 준비!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			//공급사가 올가홀푸드이고, ERP품목연동 여부가 'Y'일때(올가ERP 상품일때), ERP에서 할인정보를 연동해서 IL_GOODS_DISCOUNT에 입력처리 한다.
			if(goodsRegistRequestDto.getIlItemCode() != null && "2".equals(goodsRegistRequestDto.getUrSupplierId()) && "Y".equals(goodsRegistRequestDto.getErpItemLinkYn())) {
//				log.info("할인정보 연동 Start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				String ilItemCode = goodsRegistRequestDto.getIlItemCode();
				String ilGoodsId = goodsRegistRequestDto.getIlGoodsId();
				goodsDiscountBiz.addGoodsDiscountWithErpIfPriceBatch(ilGoodsId, ilItemCode);
//				log.info("할인정보 연동 End!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}

				//판매 정보 > 판매 유형(예약판매) > 예약 판매 옵션 설정 등록
				if(goodsRegistRequestDto.getGoodsReservationOptionList() != null) {
					for(GoodsRegistReservationOptionDto goodsRegistReservationOptionDto : goodsRegistRequestDto.getGoodsReservationOptionList()) {
						goodsRegistReservationOptionDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRegistReservationOptionDto.setCreateId(goodsRegistRequestDto.getCreateId());

						goodsRegistService.addGoodsReservationOption(goodsRegistReservationOptionDto);
					}

					//회차 순서 변경(1순위 : 도착예정일, 2순위 출고예정일, 3순위 : 주문수집I/F일, 4순위 : 예약주문가능기간 종료일)
					goodsRegistService.updateGoodsReservationOptionSaleSeq(goodsRegistRequestDto.getIlGoodsId());
				}

				//배송/발주 정보 > 배송정책 등록
				if(goodsRegistRequestDto.getItemWarehouseShippingTemplateList() != null) {
					for(GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto : goodsRegistRequestDto.getItemWarehouseShippingTemplateList()) {
						goodsRegistShippingTemplateDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRegistShippingTemplateDto.setCreateId(goodsRegistRequestDto.getCreateId());

						goodsRegistService.addGoodsShippingTemplate(goodsRegistShippingTemplateDto);
					}

					log.info("goodsRegistRequestDto.getItemWarehouseShippingTemplateList() : " + goodsRegistRequestDto.getItemWarehouseShippingTemplateList().toString());
				}

				//혜택/구매 정보 > 추가상품 등록
				if(goodsRegistRequestDto.getGoodsAdditionalGoodsMappingList() != null) {
					for(GoodsRegistAdditionalGoodsDto goodsRegistAdditionalGoodsDto : goodsRegistRequestDto.getGoodsAdditionalGoodsMappingList()) {
						goodsRegistAdditionalGoodsDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRegistAdditionalGoodsDto.setCreateId(goodsRegistRequestDto.getCreateId());

						goodsRegistService.addGoodsAdditionalGoodsMapping(goodsRegistAdditionalGoodsDto);
					}

					log.info("goodsRegistRequestDto.getGoodsAdditionalGoodsMappingList() : " + goodsRegistRequestDto.getGoodsAdditionalGoodsMappingList().toString());
				}

				//추천상품 등록 > 추천상품 등록
				if(goodsRegistRequestDto.getGoodsRecommendList() != null) {
					for(GoodsRegistAdditionalGoodsDto goodsRecommendDto : goodsRegistRequestDto.getGoodsRecommendList()) {
						goodsRecommendDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRecommendDto.setCreateId(goodsRegistRequestDto.getCreateId());

						goodsRegistService.addGoodsRecommend(goodsRecommendDto);
					}
				}

				Map<String, List<GoodsDailyCycleBulkVo>> goodsDailyCycleBulkList = new HashMap<String, List<GoodsDailyCycleBulkVo>>();

				if(GoodsEnums.GoodsType.DAILY.getCode().equals(goodsRegistRequestDto.getGoodsType())) {	//상품유형이 '일일 상품'일 경우에만 입력
					goodsDailyCycleBulkList =  addGoodsDailyMapping(goodsRegistRequestDto);	//식단주기, 일괄 배달 설정 입력

					if(goodsDailyCycleBulkList != null && goodsDailyCycleBulkList.size() == 2) {
						goodsRegistRequestDto.setGoodsDailyCycleList(goodsDailyCycleBulkList.get("goodsDailyCycleList"));
						goodsRegistRequestDto.setGoodsDailyBulkList(goodsDailyCycleBulkList.get("goodsDailyBulkList"));
					}
				}

				if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsRegistRequestDto.getGoodsType())) {	//상품유형이 '묶음 상품'일 경우에만 입력
					//묶음 상품은 기본적으로 상품을 묶어서 할인가격을 지정하므로 저장시에 기본 할인가 한개를 무조건 저장한다.
					// 1.묶음 상품 기본 할인가 저장(판매가 정보에 대해서는 화면에서 입력한 값으로 처리), 할인 승인 내역도 승인완료(시스템) 처리로 입력
					GoodsDiscountApprVo goodsDiscountApprVo = addGoodsPackageDiscount(goodsRegistRequestDto);

					// 2.묶음 상품/증정품 구성목록 저장, 묶음상품 원본 가격 저장, 묶음상품 개별품목 고정가 할인가격 저장(고정가 할인일 경우에만)
					addPackageGoodsMapping(goodsDiscountApprVo.getIlGoodsDiscountId(), goodsDiscountApprVo.getIlGoodsDiscountApprId(), goodsRegistRequestDto);

					// 3.묶음 상품 이미지 저장
					addGoodsImage(goodsRegistRequestDto);

					// 4.묶음 상품 가격정보 저장(Mysql Procedure)
					goodsRegistRequestDto.setInDebugFlag(false);
					goodsRegistService.spGoodsPriceUpdateWhenPackageGoodsChanges(goodsRegistRequestDto);	//가격정보 > 판매 가격정보, 임직원 할인 정보 > 임직원 할인가격정보 처리

				}
				else {	// '묶음 상품' 외에는 해당 상품의 품목 기준으로 계산
					// 기본 상품 가격 정보 저장
					this.spGoodsPriceUpdateWhenGoodsRegist(goodsRegistRequestDto.getIlGoodsId());	//가격정보 > 판매 가격정보, 임직원 할인 정보 > 임직원 할인가격정보 처리
				}

				/* 풀무원샵 상품코드 시작 */
				goodsRegistService.setGoodsCodeList(goodsRegistRequestDto.getIlGoodsId(), goodsRegistRequestDto.getGoodsCodeList(), goodsRegistRequestDto.getCreateId());
				/* 풀무원샵 상품코드 종료 */

				List<String> goodsId = new ArrayList<>();
				goodsId.add(goodsRegistRequestDto.getIlGoodsId());

				// 상품 이미지 등록 처리 소스 추가
				goodsDetailImageBiz.addUpdateGoodsIdInfoForDetailImage(goodsId);

				//승인관련 처리
				goodsApprProc(goodsRegistRequestDto);

				try {
					if(chgLogNum == 0) {
						throw new Exception("상품 변경 내역이 하나도 없습니다. 관리자에게 문의 하세요.");
					}
				}
				catch (Exception e) {
					log.error("상품변경 내역 오류 ", e);
				}
			}

		}

		goodsRegistResponseDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
		goodsRegistResponseDto.setGoodsName(goodsRegistRequestDto.getGoodsName());

		return ApiResult.success(goodsRegistResponseDto);
	}

	//상품변경내역 취합
	private int goodsChangeAssemble(String tranMode, GoodsRegistResponseDto beforeGoodsDatas, GoodsRegistRequestDto afterGoodsDatas) throws BaseException, Exception {

		int chgLogNum = 0;

		if(afterGoodsDatas.getPromotionNameStartDate() != null && !"".equals(afterGoodsDatas.getPromotionNameStartDate())) {
			afterGoodsDatas.setPromotionNameStartDate(afterGoodsDatas.getPromotionNameStartDate()+":00");
		}

		if(afterGoodsDatas.getPromotionNameEndDate() != null && !"".equals(afterGoodsDatas.getPromotionNameEndDate())) {
			afterGoodsDatas.setPromotionNameEndDate(afterGoodsDatas.getPromotionNameEndDate()+":59");
		}

		if(afterGoodsDatas.getNoticeBelow1StartDate() != null) {
			afterGoodsDatas.setNoticeBelow1StartDate(afterGoodsDatas.getNoticeBelow1StartDate()+":00");
		}

		if(afterGoodsDatas.getNoticeBelow1EndDate() != null) {
			afterGoodsDatas.setNoticeBelow1EndDate(afterGoodsDatas.getNoticeBelow1EndDate()+":59");
		}

		if(afterGoodsDatas.getNoticeBelow2StartDate() != null) {
			afterGoodsDatas.setNoticeBelow2StartDate(afterGoodsDatas.getNoticeBelow2StartDate()+":00");
		}

		if(afterGoodsDatas.getNoticeBelow2EndDate() != null) {
			afterGoodsDatas.setNoticeBelow2EndDate(afterGoodsDatas.getNoticeBelow2EndDate()+":59");
		}

		if(afterGoodsDatas.getSaleStartDate() != null) {
			afterGoodsDatas.setSaleStartDate(afterGoodsDatas.getSaleStartDate()+":00");
		}

		if(afterGoodsDatas.getSaleEndDate() != null) {
			afterGoodsDatas.setSaleEndDate(afterGoodsDatas.getSaleEndDate()+":59");
		}

		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();

		//상품 마스터 변경 내역
		String createDate = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		Map<String, String> beforeGoodsData = BeanUtils.describe(beforeGoodsDatas.getIlGoodsDetail());
		Map<String, String> afterGoodsData = BeanUtils.describe(afterGoodsDatas);

		chgLogNum += addGoodsMasterChangeLog(tranMode, afterGoodsDatas.getIlGoodsId(), userId, beforeGoodsData, afterGoodsData, createDate);

		if(!GoodsEnums.GoodsType.ADDITIONAL.getCode().equals(afterGoodsDatas.getGoodsType())
				&& !GoodsEnums.GoodsType.GIFT.getCode().equals(afterGoodsDatas.getGoodsType())
				&& !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(afterGoodsDatas.getGoodsType())) {		//추가/증정 상품은 카테고리가 존재하지 않으므로 제외
			//상품 카테고리 변경 내역
			List<GoodsRegistCategoryRequestDto> afterGoodsCategoryList = new ArrayList<GoodsRegistCategoryRequestDto>();
			if (afterGoodsDatas.getDisplayCategoryList() != null && afterGoodsDatas.getDisplayCategoryList().size() > 0) {
				afterGoodsCategoryList.addAll(afterGoodsDatas.getDisplayCategoryList());
			}
			if (afterGoodsDatas.getMallInMallCategoryList() != null && afterGoodsDatas.getMallInMallCategoryList().size() > 0) {
				afterGoodsCategoryList.addAll(afterGoodsDatas.getMallInMallCategoryList());
			}

			List<GoodsRegistCategoryVo> beforeGoodsCategoryList = new ArrayList<>();
			if("UPDATE".equals(tranMode)) {
				if (beforeGoodsDatas.getIlGoodsDisplayCategoryList() != null && beforeGoodsDatas.getIlGoodsDisplayCategoryList().size() > 0) {
					beforeGoodsCategoryList.addAll(beforeGoodsDatas.getIlGoodsDisplayCategoryList());
				}
				if (beforeGoodsDatas.getIlGoodsMallinmallCategoryList() != null && beforeGoodsDatas.getIlGoodsMallinmallCategoryList().size() > 0) {
					beforeGoodsCategoryList.addAll(beforeGoodsDatas.getIlGoodsMallinmallCategoryList());
				}
			}
			chgLogNum += addGoodsSubChangeLog(tranMode, "IL_GOODS_CTGRY", afterGoodsDatas.getIlGoodsId(), userId, beforeGoodsCategoryList, afterGoodsCategoryList, createDate);
		}

		//출고처별 배송정책 변경 내역
		log.info("beforeGoodsDatas.getGoodsShippingTemplateList() : " + beforeGoodsDatas.getGoodsShippingTemplateList());
		log.info("afterGoodsDatas.getItemWarehouseShippingTemplateList() : " + afterGoodsDatas.getItemWarehouseShippingTemplateList().toString());

		chgLogNum += addGoodsSubChangeLog(tranMode, "IL_GOODS_SHIPPING_TEMPLATE", afterGoodsDatas.getIlGoodsId(), userId,
				beforeGoodsDatas.getGoodsShippingTemplateList(), afterGoodsDatas.getItemWarehouseShippingTemplateList(), createDate);

		//추가상품 변경 내역
		chgLogNum += addGoodsSubChangeLog(tranMode, "IL_GOODS_ADDITIONAL_GOODS_MAPPING", afterGoodsDatas.getIlGoodsId(), userId,
				beforeGoodsDatas.getGoodsAdditionalGoodsMappingList(), afterGoodsDatas.getGoodsAdditionalGoodsMappingList(), createDate);

		//추천상품 변경 내역
		chgLogNum += addGoodsSubChangeLog(tranMode, "IL_GOODS_RECOMMEND", afterGoodsDatas.getIlGoodsId(), userId,
				beforeGoodsDatas.getGoodsRecommendList(), afterGoodsDatas.getGoodsRecommendList(), createDate);


		if(GoodsEnums.GoodsType.DAILY.getCode().equals(afterGoodsDatas.getGoodsType())) {		//일일 상품 일때만 처리
			//일일상품 > 식단주기 > 식단주기 유형 변경 내역
			log.info("beforeGoodsDatas.getGoodsDailyCyclelist() : " + beforeGoodsDatas.getGoodsDailyCyclelist());
			log.info("afterGoodsDatas.getGoodsDailyCycleList() : " + afterGoodsDatas.getGoodsDailyCycleList(), createDate);

			chgLogNum += addGoodsSubChangeLog(tranMode, "IL_GOODS_DAILY_CYCLE", afterGoodsDatas.getIlGoodsId(), userId,
					beforeGoodsDatas.getGoodsDailyCyclelist(), afterGoodsDatas.getGoodsDailyCycleList(), createDate);

			//일일상품 > 식단주기 > 식단주기 기간 변경 내역
			chgLogNum += addGoodsSubChangeLog(tranMode, "IL_GOODS_DAILY_CYCLE_TERM", afterGoodsDatas.getIlGoodsId(), userId,
					beforeGoodsDatas.getGoodsDailyCyclelist(), afterGoodsDatas.getGoodsDailyCycleList(), createDate);

			//일일상품 > 일괄배달 설정 변경 내역
			chgLogNum += addGoodsSubChangeLog(tranMode, "IL_GOODS_DAILY_BULK", afterGoodsDatas.getIlGoodsId(), userId,
					beforeGoodsDatas.getGoodsDailyBulklist(), afterGoodsDatas.getGoodsDailyBulkList(), createDate);
		}

		/* 풀무원샵 상품코드 시작 */

		chgLogNum += addGoodsSubChangeLog(tranMode, "IF_GOODS_MAPPING", afterGoodsDatas.getIlGoodsId(), userId,
				beforeGoodsDatas.getGoodsCodeList(), afterGoodsDatas.getGoodsCodeList(), createDate);

		/* 풀무원샵 상품코드 종료 */

		return chgLogNum;
	}

	/**
	 * 상품마스터 변경내역 저장
	 * tranMode : INSERT, UPDATE 구분
	 * ilGoodsId : 상품 ID
	 * createId : 변경 처리를 요청한 사람(거래처 승인요청자 저장 Proc로 인해서 추가됨)
	 * beforeGoodsDatas : 현재 DB에 저장되어 있는 값
	 * afterGoodsDatas : 화면에서 가져온 RequestDto 값
	 **/
	public int addGoodsMasterChangeLog(String tranMode, String ilGoodsId, String createId, Map<String, String> beforeGoodsDatas, Map<String, String> afterGoodsDatas, String createDate) throws BaseException, Exception {

		changeTranNum = 0;

		if(!afterGoodsDatas.isEmpty()) {
			afterGoodsDatas.forEach((afterKey, afterValue)-> {
				GoodsColumnComment goodsColumnComment = GoodsColumnComment.findByComment(afterKey);

				if("INSERT".equals(tranMode) && afterValue != null && goodsColumnComment != null) {
					GoodsChangeLogVo goodsChangeLogVo = GoodsChangeLogVo.builder()
						.ilGoodsId(ilGoodsId)
						.tableNm("IL_GOODS")
						.tableIdOrig("0")
						.tableIdNew(ilGoodsId)
						.beforeData("")		//저장시에는 beforeData는 없음
						.afterData(afterValue)
						.columnNm(afterKey)
						.columnLabel(goodsColumnComment.getCodeName())
						.createId(createId)
						.createDate(createDate)
						.build();

					//람다식 사용으로 인해서 Class 단위 전역변수로 선언 해야함
					changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
				}
				else if("UPDATE".equals(tranMode) && goodsColumnComment != null) {

					beforeGoodsDatas.forEach((beforeKey, beforeValue)-> {

						if(afterValue != null && afterKey.equals(beforeKey) && !afterValue.equals(beforeValue)) {

							log.info("beforeKey : " + beforeKey + "  beforeValue : " + beforeValue + "// afterKey : " + afterKey + "  afterValue : " + afterValue);

							GoodsChangeLogVo goodsChangeLogVo = GoodsChangeLogVo.builder()
								.ilGoodsId(ilGoodsId)
								.tableNm("IL_GOODS")
								.tableIdOrig(ilGoodsId)
								.tableIdNew(ilGoodsId)
								.beforeData(beforeValue)
								.afterData(afterValue)
								.columnNm(afterKey)
								.columnLabel(goodsColumnComment.getCodeName())
								.createId(createId)
								.createDate(createDate)
								.build();

							//람다식 사용으로 인해서 Class 단위 전역변수로 선언 해야함
							changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
						}
					});
				}
			});
		}
		return changeTranNum;
	}

	/**
	 * 상품서브 항목 변경내역 저장
	 * tranMode : INSERT, UPDATE 구분
	 * tableKind : 변경내역 저장 테이블명
	 * ilGoodsId : 상품ID
	 * createId : 변경 처리를 요청한 사람
	 * beforeGoodsDatas : 현재 DB에 저장되어 있는 값
	 * afterGoodsDatas : 화면에서 가져온 RequestDto 값
	 **/
	private int addGoodsSubChangeLog(String tranMode, String tableKind, String ilGoodsId, String createId, List<?> beforeGoodsDatas, List<?> afterGoodsDatas, String createDate) throws BaseException, Exception {
		//테이블별 ID , DATA , COLUMN COMMENT 컬럼을 지정
		goodsEtcColumnComment = GoodsEtcColumnComment.findByInfo(tableKind);
		sameValue = 0;

		if("INSERT".equals(tranMode) && afterGoodsDatas != null) {
			for (Object afterGoodsValue : afterGoodsDatas) {
				Map<String, String> afterDataMap = BeanUtils.describe(afterGoodsValue);

				GoodsChangeLogVo goodsChangeLogVo = new GoodsChangeLogVo();
				goodsChangeLogVo.setIlGoodsId(ilGoodsId);
				goodsChangeLogVo.setTableNm(tableKind);
				goodsChangeLogVo.setColumnLabel(goodsEtcColumnComment.getComment());
				goodsChangeLogVo.setCreateId(createId);
				goodsChangeLogVo.setCreateDate(createDate);

				afterDataMap.forEach((afterKey, afterValue)-> {
					if(goodsEtcColumnComment != null) {
						if(goodsEtcColumnComment.getIdColumn().equals(afterKey)){
							goodsChangeLogVo.setTableIdOrig("0");
							goodsChangeLogVo.setTableIdNew(afterValue);
							goodsChangeLogVo.setColumnNm(afterKey);
						}

						if(goodsEtcColumnComment.getDataColumn().equals(afterKey)){
							goodsChangeLogVo.setBeforeData("");
							goodsChangeLogVo.setAfterData(afterValue);
						}
					}
				});

				changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
			}
		}
		else if("UPDATE".equals(tranMode) && beforeGoodsDatas != null && afterGoodsDatas != null) {
			//기존과 동일한 값인지 비교
			for (Object beforeGoodsValue : beforeGoodsDatas) {
				Map<String, String> beforeDataMap = BeanUtils.describe(beforeGoodsValue);

				beforeDataMap.forEach((beforeKey, beforeValue)-> {
					if(goodsEtcColumnComment != null && goodsEtcColumnComment.getIdColumn().equals(beforeKey)) {
						for (Object afterGoodsValue : afterGoodsDatas) {
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

			log.info("===================================================================================================================");
			log.info("tableKind : " + tableKind);
			log.info("sameValue : " + sameValue);
			log.info("beforeGoodsDatas.size() : " + beforeGoodsDatas.size());
			log.info("afterGoodsDatas.size() : " + afterGoodsDatas.size());
			log.info("===================================================================================================================");

			if(sameValue != afterGoodsDatas.size() || sameValue != beforeGoodsDatas.size()) {
				for (Object beforeGoodsValue : beforeGoodsDatas) {
					Map<String, String> beforeDataMap = BeanUtils.describe(beforeGoodsValue);

					GoodsChangeLogVo goodsChangeLogVo = new GoodsChangeLogVo();
					goodsChangeLogVo.setIlGoodsId(ilGoodsId);
					goodsChangeLogVo.setTableNm(tableKind);
					goodsChangeLogVo.setColumnLabel(goodsEtcColumnComment.getComment());
					goodsChangeLogVo.setCreateId(createId);
					goodsChangeLogVo.setCreateDate(createDate);

					System.out.println("beforeDataMap : " + beforeDataMap);

					beforeDataMap.forEach((beforeKey, beforeValue)-> {
						if(goodsEtcColumnComment != null) {
							if(goodsEtcColumnComment.getIdColumn().equals(beforeKey)){
								goodsChangeLogVo.setTableIdOrig(beforeValue);
								goodsChangeLogVo.setTableIdNew("0");
								goodsChangeLogVo.setColumnNm(beforeKey);
							}

							if(goodsEtcColumnComment.getDataColumn().equals(beforeKey)){
								goodsChangeLogVo.setBeforeData(beforeValue);
								goodsChangeLogVo.setAfterData("");
							}
						}
					});

					changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
				}

				for (Object afterGoodsValue : afterGoodsDatas) {
					Map<String, String> afterDataMap = BeanUtils.describe(afterGoodsValue);

					GoodsChangeLogVo goodsChangeLogVo = new GoodsChangeLogVo();
					goodsChangeLogVo.setIlGoodsId(ilGoodsId);
					goodsChangeLogVo.setTableNm(tableKind);
					goodsChangeLogVo.setColumnLabel(goodsEtcColumnComment.getComment());
					goodsChangeLogVo.setCreateId(createId);
					goodsChangeLogVo.setCreateDate(createDate);
					System.out.println("afterDataMap : " + afterDataMap);
					afterDataMap.forEach((afterKey, afterValue)-> {
						if(goodsEtcColumnComment != null) {
							if(goodsEtcColumnComment.getIdColumn().equals(afterKey)){
								goodsChangeLogVo.setTableIdOrig("0");
								goodsChangeLogVo.setTableIdNew(afterValue);
								goodsChangeLogVo.setColumnNm(afterKey);
							}

							if(goodsEtcColumnComment.getDataColumn().equals(afterKey)){
								goodsChangeLogVo.setBeforeData("");
								goodsChangeLogVo.setAfterData(afterValue);
							}
						}
					});

					changeTranNum += goodsRegistService.addGoodsChangeLog(goodsChangeLogVo);
				}
			}
		}

		return changeTranNum;
	}

	//일일상품 > 식단주기, 일괄배달 입력
	private Map<String, List<GoodsDailyCycleBulkVo>> addGoodsDailyMapping(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {

		goodsRegistService.delGoodsDailyCycle(goodsRegistRequestDto.getIlGoodsId());
		goodsRegistService.delGoodsDailyBulk(goodsRegistRequestDto.getIlGoodsId());

		//변경내역 Log Data를 위한 List<Vo> 선언
		List<GoodsDailyCycleBulkVo> goodsDailyCycleList = new ArrayList<GoodsDailyCycleBulkVo>();
		List<GoodsDailyCycleBulkVo> goodsDailyBulkList = new ArrayList<GoodsDailyCycleBulkVo>();

		//판매정보 > 일일 판매 옵션 설정 > 식단주기선택 입력
		if(goodsRegistRequestDto.getGoodsCycleType7Day() != null && goodsRegistRequestDto.getGoodsCycleTermType7Day() != null) {	//주7일 식단
			for(String goodsCycleTermType7Day : goodsRegistRequestDto.getGoodsCycleTermType7Day()) {
				GoodsDailyCycleBulkVo addGoodsDailyCycleVo = GoodsDailyCycleBulkVo.builder()
						.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())									// 상품ID
						.goodsCycleType(goodsRegistRequestDto.getGoodsCycleType7Day()[0])					// 식단주기 유형 공통코드
						.goodsCycleTermType(goodsCycleTermType7Day)											// 식단주기 기간 유형 공통코드
						.createId(goodsRegistRequestDto.getCreateId())										// 등록자
						.build();

				goodsRegistService.addGoodsDailyCycle(addGoodsDailyCycleVo);
				goodsDailyCycleList.add(addGoodsDailyCycleVo);
				log.info("7Day addGoodsDailyCycleVo : " + addGoodsDailyCycleVo);
			}
		}

		if(goodsRegistRequestDto.getGoodsCycleType6Day() != null && goodsRegistRequestDto.getGoodsCycleTermType6Day() != null) {	//주6일 식단
			for(String goodsCycleTermType6Day : goodsRegistRequestDto.getGoodsCycleTermType6Day()) {
				GoodsDailyCycleBulkVo addGoodsDailyCycleVo = GoodsDailyCycleBulkVo.builder()
						.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())									// 상품ID
						.goodsCycleType(goodsRegistRequestDto.getGoodsCycleType6Day()[0])					// 식단주기 유형 공통코드
						.goodsCycleTermType(goodsCycleTermType6Day)											// 식단주기 기간 유형 공통코드
						.createId(goodsRegistRequestDto.getCreateId())										// 등록자
						.build();

				goodsRegistService.addGoodsDailyCycle(addGoodsDailyCycleVo);
				goodsDailyCycleList.add(addGoodsDailyCycleVo);
				log.info("6Day addGoodsDailyCycleVo : " + addGoodsDailyCycleVo);
			}
		}

		if(goodsRegistRequestDto.getGoodsCycleType5Day() != null && goodsRegistRequestDto.getGoodsCycleTermType5Day() != null) {	//주5일 식단
			for(String goodsCycleTermType5Day : goodsRegistRequestDto.getGoodsCycleTermType5Day()) {
				GoodsDailyCycleBulkVo addGoodsDailyCycleVo = GoodsDailyCycleBulkVo.builder()
						.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())									// 상품ID
						.goodsCycleType(goodsRegistRequestDto.getGoodsCycleType5Day()[0])					// 식단주기 유형 공통코드
						.goodsCycleTermType(goodsCycleTermType5Day)											// 식단주기 기간 유형 공통코드
						.createId(goodsRegistRequestDto.getCreateId())										// 등록자
						.build();

				goodsRegistService.addGoodsDailyCycle(addGoodsDailyCycleVo);
				goodsDailyCycleList.add(addGoodsDailyCycleVo);
				log.info("5Day addGoodsDailyCycleVo : " + addGoodsDailyCycleVo);
			}
		}

		if(goodsRegistRequestDto.getGoodsCycleType3Day() != null && goodsRegistRequestDto.getGoodsCycleTermType3Day() != null) {	//주3일 식단
			for(String goodsCycleTermType3Day : goodsRegistRequestDto.getGoodsCycleTermType3Day()) {
				GoodsDailyCycleBulkVo addGoodsDailyCycleVo = GoodsDailyCycleBulkVo.builder()
						.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())									// 상품ID
						.goodsCycleType(goodsRegistRequestDto.getGoodsCycleType3Day()[0])					// 식단주기 유형 공통코드
						.goodsCycleTermType(goodsCycleTermType3Day)											// 식단주기 기간 유형 공통코드
						.createId(goodsRegistRequestDto.getCreateId())										// 등록자
						.build();

				goodsRegistService.addGoodsDailyCycle(addGoodsDailyCycleVo);
				goodsDailyCycleList.add(addGoodsDailyCycleVo);
				log.info("3Day addGoodsDailyCycleVo : " + addGoodsDailyCycleVo);
			}
		}

		if(goodsRegistRequestDto.getGoodsCycleTypeEatslim3Day() != null && goodsRegistRequestDto.getGoodsCycleTermTypeEatslim3Day() != null) {	//주3일 식단(잇슬림)
			for(String goodsCycleTermType3Day : goodsRegistRequestDto.getGoodsCycleTermTypeEatslim3Day()) {
				GoodsDailyCycleBulkVo addGoodsDailyCycleVo = GoodsDailyCycleBulkVo.builder()
						.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())									// 상품ID
						.goodsCycleType(goodsRegistRequestDto.getGoodsCycleTypeEatslim3Day()[0])			// 식단주기 유형 공통코드
						.goodsCycleTermType(goodsCycleTermType3Day)											// 식단주기 기간 유형 공통코드
						.createId(goodsRegistRequestDto.getCreateId())										// 등록자
						.build();

				goodsRegistService.addGoodsDailyCycle(addGoodsDailyCycleVo);
				goodsDailyCycleList.add(addGoodsDailyCycleVo);
				log.info("EatSlim 3Day addGoodsDailyCycleVo : " + addGoodsDailyCycleVo);
			}
		}

		//판매정보 > 일일 판매 옵션 설정 > 일괄배달설정 입력
		if("Y".equals(goodsRegistRequestDto.getGoodsDailyBulkYn()) && goodsRegistRequestDto.getGoodsBulkType() != null) {
			for(String goodsBulkType : goodsRegistRequestDto.getGoodsBulkType()) {
				GoodsDailyCycleBulkVo addGoodsDailyBulkVo = GoodsDailyCycleBulkVo.builder()
						.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())									// 상품ID
						.goodsBulkType(goodsBulkType)														// 일괄배달 유형
						.createId(goodsRegistRequestDto.getCreateId())										// 등록자
						.build();

				goodsRegistService.addGoodsDailyBulk(addGoodsDailyBulkVo);
				goodsDailyBulkList.add(addGoodsDailyBulkVo);
				log.info("addGoodsDailyBulkVo : " + addGoodsDailyBulkVo);
			}
		}

		Map<String, List<GoodsDailyCycleBulkVo>> goodsDailyCycleBulkList = new HashMap<String, List<GoodsDailyCycleBulkVo>>();

		goodsDailyCycleBulkList.put("goodsDailyCycleList", goodsDailyCycleList);
		goodsDailyCycleBulkList.put("goodsDailyBulkList", goodsDailyBulkList);

		return goodsDailyCycleBulkList;
	}

	// 묶음 상품 기본 할인가 내역 저장/수정, 묶음상품 개별 품목 고정가 할인가격 저장(고정가 할인의 경우에만)
	@Deprecated
	private void updateGoodsPackageDiscount(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception, BaseException {

		if(!goodsRegistRequestDto.getGoodsPackagePriceList().isEmpty()) {
			String newGoodsDiscountStartTime = null;

			for(GoodsDiscountRequestDto goodsDiscountRequestDto : goodsRegistRequestDto.getGoodsPackagePriceList()) {
				goodsDiscountRequestDto.setGoodsId(goodsRegistRequestDto.getIlGoodsId());

				String ilGoodsDiscountId = goodsDiscountRequestDto.getGoodsDiscountId();
				String statusName = goodsDiscountRequestDto.getApprovalStatusCodeName();

				if(GoodsDiscountMethodType.FIXED_RATE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {			//할인 유형이 '정률할인' 이라면 판매가를 저장하지 않는다.
					goodsDiscountRequestDto.setDiscountSalePrice(0);
				}
				else if(GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {	//할인 유형이 '고정가할인' 이라면 할인율을 저장하지 않는다.
					goodsDiscountRequestDto.setDiscountRatio(0);
				}

				if((ilGoodsDiscountId == null || "".equals(ilGoodsDiscountId)) && "승인요청".equals(statusName)) {						//승인 요청 내역이고 할인내역 ID가 없다면 저장
					goodsDiscountRequestDto.setCreateId(goodsRegistRequestDto.getCreateId());
//					goodsRegistService.addGoodsDiscount(goodsDiscountRequestDto);
					this.addGoodsDiscountWithValidation(goodsDiscountRequestDto);
					newGoodsDiscountStartTime = goodsDiscountRequestDto.getDiscountStartDateTime();

					updatePackageItemFixDiscountPrice(goodsDiscountRequestDto, goodsRegistRequestDto);			//묶음상품 개별품목 고정가 할인가격 저장
				}
				else if(!"".equals(ilGoodsDiscountId) && "승인요청".equals(statusName)) {												//승인 요청 내역이고 할인내역 ID가 있다면 수정
					goodsDiscountRequestDto.setModifyId(goodsRegistRequestDto.getCreateId());
					goodsRegistService.modifyGoodsDiscount(goodsDiscountRequestDto);
				}
			}

			//승인완료 처리가 되면 새로 저장된 할인 내역의, 바로 전 할인 내역의 종료일(종료일이 '2999-12-31 23:59:59'인 할인내역)을 새로 저장된 시작일 - 1초 로 변경 한다.
			if(newGoodsDiscountStartTime != null) {
				goodsRegistService.updateGoodsDiscountEndTime(goodsRegistRequestDto.getIlGoodsId(), newGoodsDiscountStartTime);
			}
		}
	}

	//묶음 상품 > 임직원 할인 정보 > 임직원 개별할인 정보 저장/수정, 묶음상품 개별 품목 할인율 저장(임직원 개별 할인의 경우에만)
	@Deprecated
	private void updateGoodsPackageDiscountEmployee(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception, BaseException {

		if(!goodsRegistRequestDto.getGoodsPackageDiscountEmployeeList().isEmpty()) {

			int goodsDiscountSeq = 0;
			String compareIlGoodsDiscountId = null;
			String ilGoodsDiscountId = null;

			for(GoodsDiscountRequestDto goodsDiscountRequestDto : goodsRegistRequestDto.getGoodsPackageDiscountEmployeeList()) {

				if(goodsDiscountRequestDto.getGoodsDiscountId() == null) {	//신규저장일때

					double discountRatio = goodsDiscountRequestDto.getDiscountRatio();	////개별할인 정보 할인율 저장을 위해서 임시 저장

					if(goodsDiscountSeq == 0) {	//새로 저장할 개별할인 정보 리스트의 첫번째 행의 할인 시작일, 할인 종료일을 가지고, IL_GOODS_DISCOUNT(할인 마스터 정보) 테이블을 저장
						//묶음상품 임직원 개별할인은 개별품목별로 할인율을 저정하므로 IL_GOODS_DISCOUNT 테이블의 할인율이나 할인판매가는 다 저장하지 않는다.
						goodsDiscountRequestDto.setDiscountSalePrice(0);
						goodsDiscountRequestDto.setDiscountRatio(0);
						goodsDiscountRequestDto.setCreateId(goodsRegistRequestDto.getCreateId());
//						goodsRegistService.addGoodsDiscount(goodsDiscountRequestDto);
						this.addGoodsDiscountWithValidation(goodsDiscountRequestDto);
						ilGoodsDiscountId = goodsDiscountRequestDto.getGoodsDiscountId();
					}

					if(goodsDiscountSeq != goodsDiscountRequestDto.getRowCount()) {		//합계 행은 계산하지 않는다.
						GoodsRegistPackageGoodsPriceDto goodsRegistPackageGoodsPriceDto = new GoodsRegistPackageGoodsPriceDto();	//개별 할인율 정보 저장을 위한 DTO

						goodsRegistPackageGoodsPriceDto.setIlGoodsDiscountId(ilGoodsDiscountId);
						goodsRegistPackageGoodsPriceDto.setIlGoodsPackageGoodsMappingId(goodsDiscountRequestDto.getIlGoodsPackageGoodsMappingId());
						goodsRegistPackageGoodsPriceDto.setSalePrice(0);
						goodsRegistPackageGoodsPriceDto.setDiscountRatio(discountRatio);

						goodsRegistService.addGoodsPackageItemFixedDiscountPrice(goodsRegistPackageGoodsPriceDto);
					}

					goodsDiscountSeq++;
				}
				else {	//수정일때
					if(!goodsDiscountRequestDto.getGoodsDiscountId().equals(compareIlGoodsDiscountId)) {	//각 개별할인 DiscountId 그룹별 첫번째 행 정보로만 수정 처리
						//묶음상품 임직원 개별할인은 개별품목별로 할인율을 저정하므로 IL_GOODS_DISCOUNT 테이블의 할인율이나 할인판매가는 다 저장하지 않는다.
						goodsDiscountRequestDto.setDiscountSalePrice(0);
						goodsDiscountRequestDto.setDiscountRatio(0);
						goodsDiscountRequestDto.setModifyId(goodsRegistRequestDto.getCreateId());

						goodsRegistService.modifyGoodsDiscount(goodsDiscountRequestDto);
					}
					compareIlGoodsDiscountId = goodsDiscountRequestDto.getGoodsDiscountId();

					//추후 승인 Proc가 정해지면 임직원 개별할인 할인율(IL_GOODS_PACKAGE_ITEM_FIXED_DISCOUNT_PRICE) 저장 처리를 진행 한다.
				}
			}
		}
	}

	//묶음상품 기본할인가 저장
	private GoodsDiscountApprVo addGoodsPackageDiscount(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception, BaseException {

		String goodsDiscountId = null;
		String today = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");

		//묶음상품 기본 판매가 저장
		GoodsDiscountRequestDto goodsDiscountRequestDto = new GoodsDiscountRequestDto();

		goodsDiscountRequestDto.setGoodsId(goodsRegistRequestDto.getIlGoodsId());
		goodsDiscountRequestDto.setDiscountTypeCode(GoodsEnums.GoodsDiscountType.PACKAGE.getCode());
		goodsDiscountRequestDto.setDiscountStartDateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		goodsDiscountRequestDto.setDiscountEndDateTime("2999-12-31 23:59");
		goodsDiscountRequestDto.setDiscountMethodTypeCode(goodsRegistRequestDto.getPackageSaleType());

		if(GoodsDiscountMethodType.FIXED_RATE.getCode().equals(goodsRegistRequestDto.getPackageSaleType())) {		//할인 유형이 '정률할인' 이라면 판매가를 저장하지 않는다.
			goodsDiscountRequestDto.setDiscountSalePrice(0);
			goodsDiscountRequestDto.setDiscountRatio(goodsRegistRequestDto.getSaleRate());
		}
		else if(GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(goodsRegistRequestDto.getPackageSaleType())) {	//할인 유형이 '고정가할인' 이라면 할인율을 저장하지 않는다.
			goodsDiscountRequestDto.setDiscountSalePrice(goodsRegistRequestDto.getTotalSalePriceGoods());
			goodsDiscountRequestDto.setDiscountRatio(0);
		}
		goodsDiscountRequestDto.setCreateId(goodsRegistRequestDto.getCreateId());

//		goodsRegistService.addGoodsDiscount(goodsDiscountRequestDto);
		this.addGoodsDiscountWithValidation(goodsDiscountRequestDto);

		goodsDiscountId = goodsDiscountRequestDto.getGoodsDiscountId();

		//묶음상품 기본 판매가 승인 내역 저장(기본 생성되는 항목이므로 자동 승인 처리로 진행)
		GoodsDiscountApprVo goodsDiscountApprVo = new GoodsDiscountApprVo();

		goodsDiscountApprVo.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
		goodsDiscountApprVo.setDiscountTp(goodsDiscountRequestDto.getDiscountTypeCode());
		goodsDiscountApprVo.setApprStat(ApprovalEnums.ApprovalStatus.APPROVED_BY_SYSTEM.getCode());
		goodsDiscountApprVo.setDiscountStartDt(goodsDiscountRequestDto.getDiscountStartDateTime());
		goodsDiscountApprVo.setDiscountEndDt(goodsDiscountRequestDto.getDiscountEndDateTime()+":59");
		goodsDiscountApprVo.setDiscountMethodTp(goodsDiscountRequestDto.getDiscountMethodTypeCode());

		goodsDiscountApprVo.setStandardPrice(goodsDiscountRequestDto.getItemStandardPrice());
		goodsDiscountApprVo.setRecommendedPrice(goodsDiscountRequestDto.getItemRecommendedPrice());

		if(GoodsDiscountMethodType.FIXED_RATE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {			//할인 유형이 '정률할인' 이라면 판매가를 저장하지 않는다.
			goodsDiscountApprVo.setDiscountSalePrice(0);
			goodsDiscountApprVo.setDiscountRatio(goodsDiscountRequestDto.getDiscountRatio());
		}
		else if(GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {	//할인 유형이 '고정가할인' 이라면 할인율을 저장하지 않는다.
			goodsDiscountApprVo.setDiscountSalePrice(goodsDiscountRequestDto.getDiscountSalePrice());
			goodsDiscountApprVo.setDiscountRatio(0);
		}

		goodsDiscountApprVo.setApprSubUserId(null);
		goodsDiscountApprVo.setApprSubChgUserId(null);
		goodsDiscountApprVo.setApprSubChgDt(null);
		goodsDiscountApprVo.setApprReqUserId(goodsRegistRequestDto.getCreateId());
		goodsDiscountApprVo.setApprUserId("0");
		goodsDiscountApprVo.setApprChgUserId("0");
		goodsDiscountApprVo.setApprChgDt(today);

		goodsDiscountApprVo.setApprReqDt(today);
		goodsDiscountApprVo.setCreateId(goodsRegistRequestDto.getCreateId());
		goodsDiscountApprVo.setCreateDt(today);
		goodsDiscountApprVo.setIlGoodsDiscountId(goodsDiscountId);

		goodsRegistService.addGoodsDiscountAppr(goodsDiscountApprVo);

		goodsDiscountApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
		goodsDiscountApprVo.setStatusCmnt("묶음상품 기본 판매가 자동승인");
		goodsRegistService.addGoodsDiscountApprStatusHistory(goodsDiscountApprVo);

		return goodsDiscountApprVo;
	}

	private void addGoodsPackagePriceOrig(String ilGoodsId, int standardPriceSum, int recommendedPriceSum) throws Exception{
		//묶음상품 원본 가격 저장
		GoodsRegistPackageGoodsPriceDto goodsRegistPackageGoodsPriceDto = new GoodsRegistPackageGoodsPriceDto();

		goodsRegistPackageGoodsPriceDto.setIlGoodsId(ilGoodsId);												//상품 ID
		goodsRegistPackageGoodsPriceDto.setPriceStartDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));		//가격 시작일
		goodsRegistPackageGoodsPriceDto.setPriceEndDate("2999-12-31 23:59:59");									//가격 종료일
		goodsRegistPackageGoodsPriceDto.setStandardPrice(standardPriceSum);										//(원가*수량) 총합
		goodsRegistPackageGoodsPriceDto.setRecommendedPrice(recommendedPriceSum);								//(정상가*수량) 총합

		goodsRegistService.addGoodsPackagePriceOrig(goodsRegistPackageGoodsPriceDto);
	}

	private void addPackageGoodsMapping(String ilGoodsDiscountId, String ilGoodsDiscountApprId, GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {

		// 상품별 대표 이미지 정렬 순서
		List<String> goodsImageOrderList = goodsRegistRequestDto.getGoodsImageOrderList();

		if(!goodsRegistRequestDto.getAssemblePackageGoodsList().isEmpty()) {

			int standardPriceSum = 0;
			int recommendedPriceSum = 0;

			//묶음상품 개별품목 고정가 할인가격저장을 위한 List Dto
			List<GoodsRegistPackageGoodsPriceDto> goodsPackageItemFixedDiscountPriceList = new ArrayList<GoodsRegistPackageGoodsPriceDto>();

			for(GoodsRegistPackageGoodsMappingDto goodsRegistPackageGoodsMappingDto : goodsRegistRequestDto.getAssemblePackageGoodsList()) {

				goodsRegistPackageGoodsMappingDto.setTargetGoodsId(goodsRegistPackageGoodsMappingDto.getIlGoodsId());
				goodsRegistPackageGoodsMappingDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
				goodsRegistPackageGoodsMappingDto.setCreateId(goodsRegistRequestDto.getCreateId());

				int salePrice = goodsRegistPackageGoodsMappingDto.getSalePricePerUnit() * goodsRegistPackageGoodsMappingDto.getGoodsQuantity();
				goodsRegistPackageGoodsMappingDto.setSalePrice(salePrice);

				//묶움상품 구성목록 저장
				goodsRegistPackageGoodsMappingDto.setImageSort(goodsImageOrderList.indexOf(goodsRegistPackageGoodsMappingDto.getTargetGoodsId()));	//상품ID 별로 정렬순서 매핑
				goodsRegistService.addPackageGoodsMapping(goodsRegistPackageGoodsMappingDto);

				//묶음상품 저장하는 시점에 구성 상품의 품목에 대한 원가, 정상가를 불러온다.
				List<GoodsPriceInfoResultVo> itemPriceList = goodsRegistService.itemPriceList(goodsRegistPackageGoodsMappingDto.getIlItemCode());

				int standardPrice = 0;
				int recommendedPrice = 0;

				if(!itemPriceList.isEmpty()) {
					standardPrice = itemPriceList.get(0).getStandardPrice() * goodsRegistPackageGoodsMappingDto.getGoodsQuantity();
					recommendedPrice = itemPriceList.get(0).getRecommendedPrice() * goodsRegistPackageGoodsMappingDto.getGoodsQuantity();

					standardPriceSum += standardPrice;
					recommendedPriceSum += recommendedPrice;
				}

				GoodsRegistPackageGoodsPriceDto goodsRegistPackageGoodsPriceDto = new GoodsRegistPackageGoodsPriceDto();

				goodsRegistPackageGoodsPriceDto.setIlGoodsDiscountId(ilGoodsDiscountId);
				goodsRegistPackageGoodsPriceDto.setIlGoodsPackageGoodsMappingId(goodsRegistPackageGoodsMappingDto.getIlGoodsPackageGoodsMappingId());
				goodsRegistPackageGoodsPriceDto.setSalePrice(salePrice);
				goodsRegistPackageGoodsPriceDto.setSalePricePerUnit(goodsRegistPackageGoodsMappingDto.getSalePricePerUnit());

				goodsPackageItemFixedDiscountPriceList.add(goodsRegistPackageGoodsPriceDto);
			}

			//묶음상품 원본 가격 저장
			addGoodsPackagePriceOrig(goodsRegistRequestDto.getIlGoodsId(), standardPriceSum, recommendedPriceSum);

			//묶음상품 개별품목 고정가 할인가격 저장, 묶음상품 개별품목 할인정보 승인 저장
			if(!goodsPackageItemFixedDiscountPriceList.isEmpty() && GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(goodsRegistRequestDto.getPackageSaleType())) { //할인 유형이 고정가 할인일 경우에만
				GoodsPackageItemFixedDiscountPriceApprVo goodsPackageItemFixedDiscountPriceApprVo = new GoodsPackageItemFixedDiscountPriceApprVo();

				for(GoodsRegistPackageGoodsPriceDto goodsPackageItemFixedDiscountPrice : goodsPackageItemFixedDiscountPriceList) {
					//묶음상품 개별품목 고정가 할인가격 저장
					goodsRegistService.addGoodsPackageItemFixedDiscountPrice(goodsPackageItemFixedDiscountPrice);

					goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsDiscountApprId(ilGoodsDiscountApprId);
					goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsPackageGoodsMappingId(goodsPackageItemFixedDiscountPrice.getIlGoodsPackageGoodsMappingId());
					goodsPackageItemFixedDiscountPriceApprVo.setSalePrice(goodsPackageItemFixedDiscountPrice.getSalePrice());
					goodsPackageItemFixedDiscountPriceApprVo.setUnitSalePrice(goodsPackageItemFixedDiscountPrice.getSalePricePerUnit());
					//묶음상품 개별품목 할인정보 승인 저장
					goodsRegistService.addGoodsPackageItemFixedDiscountPriceAppr(goodsPackageItemFixedDiscountPriceApprVo);
				}
			}
		}

		//증정품 목록 저장
		if(!goodsRegistRequestDto.getAssembleGiftGoodsList().isEmpty()) {
			for(GoodsRegistPackageGoodsMappingDto goodsRegistPackageGoodsMappingDto : goodsRegistRequestDto.getAssembleGiftGoodsList()) {

				goodsRegistPackageGoodsMappingDto.setTargetGoodsId(goodsRegistPackageGoodsMappingDto.getIlGoodsId());
				goodsRegistPackageGoodsMappingDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
				goodsRegistPackageGoodsMappingDto.setSalePricePerUnit(goodsRegistPackageGoodsMappingDto.getRecommendedPrice());	//정상가(개당)
				goodsRegistPackageGoodsMappingDto.setSalePrice(goodsRegistPackageGoodsMappingDto.getRecommendedTotalPrice());	//정상가총액
				goodsRegistPackageGoodsMappingDto.setGoodsQuantity(goodsRegistPackageGoodsMappingDto.getPurchaseQuanity());		//구성수량
				goodsRegistPackageGoodsMappingDto.setSalePrice(0);																//증정품은 판매가 0원
				goodsRegistPackageGoodsMappingDto.setSalePricePerUnit(0);														//증정품은 개당 판매가도 0원
				goodsRegistPackageGoodsMappingDto.setBaseGoodsYn("N");															//증정품은 기본상품 개념이 없으므로 다 'N'으로 고정
				goodsRegistPackageGoodsMappingDto.setCreateId(goodsRegistRequestDto.getCreateId());

				int salePrice = goodsRegistPackageGoodsMappingDto.getSalePricePerUnit() * goodsRegistPackageGoodsMappingDto.getGoodsQuantity();
				goodsRegistPackageGoodsMappingDto.setSalePrice(salePrice);

				goodsRegistService.addPackageGoodsMapping(goodsRegistPackageGoodsMappingDto);
			}
		}
	}

	//묶음 상품 이미지 저장
	private void addGoodsImage(GoodsRegistRequestDto goodsRegistRequestDto) {
		List<String> packageImageOrderList = goodsRegistRequestDto.getPackageImageOrderList();

		if(packageImageOrderList != null && packageImageOrderList.size() == goodsRegistRequestDto.getPackageImageUploadResultList().size()) {
			for(UploadFileDto uploadFileDto : goodsRegistRequestDto.getPackageImageUploadResultList()) {
				GoodsImageVo addGoodsImageVo = GoodsImageVo.builder()
					.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())									// 상품ID
					.imagePath(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName())	// 물리적 파일명 ( 저장경로 포함 )
					.imageOriginalName(uploadFileDto.getOriginalFileName())								// 원본 파일명
					.createId(goodsRegistRequestDto.getCreateId())										// 등록자
					.build();

				// 해당 품목 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
				this.resizeGoodsImage(addGoodsImageVo, uploadFileDto, goodsRegistRequestDto.getImageRootStoragePath());

				if(addGoodsImageVo.getImageOriginalName().equals(goodsRegistRequestDto.getRepresentativeImageName())) {
					addGoodsImageVo.setBasicYn("Y");
					addGoodsImageVo.setSort(0);
				}
				else {
					addGoodsImageVo.setBasicYn("N");
					addGoodsImageVo.setSort(packageImageOrderList.indexOf(uploadFileDto.getOriginalFileName()));
				}

				goodsRegistService.addGoodsImage(addGoodsImageVo);
			}
		}
	}

	/**
	 * @Desc ( 묶음 상품 등록 ) 묶음 상품 이미지 등록시 해당 상품 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
	 *
	 * @param GoodsImageVo	: Insert 할 품목 이미지 Vo
	 * @param UploadFileDto	: 해당 품목 이미지 업로드 결과 Dto
	 * @param String		: 품목 이미지를 저장할 Public 저장소의 최상위 저장 디렉토리 경로
	 */
	protected void resizeGoodsImage(GoodsImageVo addGoodsImageVo, UploadFileDto uploadFileDto, String publicRootStoragePath) {

		// 리사이징 원본 파일의 전체 경로 : 최상위 저장 디렉토리 경로 + 하위 경로
		String imageFilePath = publicRootStoragePath + uploadFileDto.getServerSubPath();

		for (ItemImagePrefixBySize itemImagePrefixBySize : ItemImagePrefixBySize.values()) { // 품목 이미지 Size / Prefix 반복문 시작

			switch (itemImagePrefixBySize) {

			case PREFIX_640:

				// 640 X 640 리사이즈 이미지 생성
				ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_640.getPrefix(), ItemImagePrefixBySize.PREFIX_640.getImageSize());

				// 640*640 파일명 ( 저장경로 포함 )
				addGoodsImageVo.setBImage(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_640.getPrefix() + uploadFileDto.getPhysicalFileName());

				break;

			case PREFIX_320:

				// 320 X 320 리사이즈 이미지 생성
				ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_320.getPrefix(), ItemImagePrefixBySize.PREFIX_320.getImageSize());

				// 320*320 파일명 ( 저장경로 포함 )
				addGoodsImageVo.setMImage(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_320.getPrefix() + uploadFileDto.getPhysicalFileName());

				break;

			case PREFIX_216:

				// 216 X 216 리사이즈 이미지 생성
				ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_216.getPrefix(), ItemImagePrefixBySize.PREFIX_216.getImageSize());

				// 216*216 파일명 ( 저장경로 포함 )
				addGoodsImageVo.setMsImage(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_216.getPrefix() + uploadFileDto.getPhysicalFileName());

				break;

			case PREFIX_180:

				// 180 X 180 리사이즈 이미지 생성
				ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_180.getPrefix(), ItemImagePrefixBySize.PREFIX_180.getImageSize());

				// 180*180 파일명 ( 저장경로 포함 )
				addGoodsImageVo.setSImage(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_180.getPrefix() + uploadFileDto.getPhysicalFileName());

				break;

			case PREFIX_75:

				// 75 X 75 리사이즈 이미지 생성
				ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_75.getPrefix(), ItemImagePrefixBySize.PREFIX_75.getImageSize());

				// 75*75 파일명 ( 저장경로 포함 )
				addGoodsImageVo.setCImage(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_75.getPrefix() + uploadFileDto.getPhysicalFileName());

				break;

			}

		} // 품목 이미지 Size / Prefix 반복문 끝

	}

	/**
	 * @Desc  상품수정
	 * @param GoodsRegistRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = {Exception.class, BaseException.class})
	public ApiResult<?> modifyGoods(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception, BaseException {

		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		// 일반, 묶음 상품이 아니면 선물하기 미대상 NA - 승인 프로세스 전 기본값 설정
		if(!GoodsType.PACKAGE.getCode().equals(goodsRegistRequestDto.getGoodsType()) && !GoodsType.NORMAL.getCode().equals(goodsRegistRequestDto.getGoodsType())) {
			goodsRegistRequestDto.setPresentYn(PresentYn.NA.getCode());
		}

		ApiResult<?> goodsApprProc = goodsApprProc(goodsRegistRequestDto);

		if(!goodsApprProc.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
			goodsRegistResponseDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
			goodsRegistResponseDto.setGoodsName(goodsRegistRequestDto.getGoodsName());

			return ApiResult.result(goodsRegistResponseDto, goodsApprProc.getMessageEnum());
		}
		else {
			//구매 허용 체크박스 값 세팅
			goodsRegistRequestDto.setPurchaseMemberYn("N");
			goodsRegistRequestDto.setPurchaseEmployeeYn("N");
			goodsRegistRequestDto.setPurchaseNonmemberYn("N");

			for(String purchaseTargetType : goodsRegistRequestDto.getPurchaseTargetType()) {

				if(GoodsEnums.GoodsPurchaseTypes.MEMBER.getCode().equals(purchaseTargetType)) {	//회원 구매여부 세팅
					goodsRegistRequestDto.setPurchaseMemberYn("Y");
				}

				if(GoodsEnums.GoodsPurchaseTypes.EMPLOYEE.getCode().equals(purchaseTargetType)) {	//임직원 구매여부 세팅
					goodsRegistRequestDto.setPurchaseEmployeeYn("Y");
				}

				if(GoodsEnums.GoodsPurchaseTypes.NONMEMBER.getCode().equals(purchaseTargetType)) {	//비회원 구매여부 세팅

					if(GoodsEnums.GoodsType.DAILY.getCode().equals(goodsRegistRequestDto.getGoodsType())) {	//일일상품이라면
						goodsRegistRequestDto.setPurchaseNonmemberYn("N");							// 비회원은 무조건 'N'(비회원이 존재하지 않음)
					}
					else {
						goodsRegistRequestDto.setPurchaseNonmemberYn("Y");
					}
				}
			}

			//판매허용범위(PC/Mobile) 체크박스 값 세팅
			goodsRegistRequestDto.setDisplayWebPcYn("N");
			goodsRegistRequestDto.setDisplayWebMobileYn("N");
			goodsRegistRequestDto.setDisplayAppYn("N");

			for(String goodsDisplayType : goodsRegistRequestDto.getGoodsDisplayType()) {
				if(GoodsEnums.GoodsDisplayTypes.WEB_PC.getCode().equals(goodsDisplayType)) {		//WEB PC 전시여부 값 세팅
					goodsRegistRequestDto.setDisplayWebPcYn("Y");
				}

				if(GoodsEnums.GoodsDisplayTypes.WEB_MOBILE.getCode().equals(goodsDisplayType)) {	//WEB MOBILE 전시여부 세팅
					goodsRegistRequestDto.setDisplayWebMobileYn("Y");
				}

				if(GoodsEnums.GoodsDisplayTypes.APP.getCode().equals(goodsDisplayType)) {			//APP 전시여부 세팅
					goodsRegistRequestDto.setDisplayAppYn("Y");
				}
			}

			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsRegistRequestDto.getGoodsType()) && "N".equals(goodsRegistRequestDto.getGoodsPackageBasicDescYn())) {	//묶음상품이고 상품 상세 기본 정보 옵션이 '구성상품 상세' 라면
				goodsRegistRequestDto.setGoodsPackageBasicDesc("");	//상품 상세 기본 정보 내용을 저장하지 않는다.
			}

			//상세 하단 공지1 첨부 이미지 URL
			if(goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList() != null) {
				int noticeBelow1ImageCnt = goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList().size();

				if(noticeBelow1ImageCnt > 0) {
					for (UploadFileDto uploadFileDto : goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList()) {
						goodsRegistRequestDto.setNoticeBelow1ImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
					}
				}
			}

			//상세 하단 공지2 첨부 이미지 URL
			if(goodsRegistRequestDto.getNoticeBelow1ImageUploadResultList() != null) {
				int noticeBelow2ImageCnt = goodsRegistRequestDto.getNoticeBelow2ImageUploadResultList().size();

				if(noticeBelow2ImageCnt > 0) {
					for (UploadFileDto uploadFileDto : goodsRegistRequestDto.getNoticeBelow2ImageUploadResultList()) {
						goodsRegistRequestDto.setNoticeBelow2ImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
					}
				}
			}

			//일일 상품 녹즙 클렌즈 옵션 사용 여부
			if(goodsRegistRequestDto.isGreenJuiceCleanseOpt()) {
				goodsRegistRequestDto.setGreenJuiceCleanseOptYn("Y");
			}
			else {
				goodsRegistRequestDto.setGreenJuiceCleanseOptYn("N");
			}

			//세션 정보 불러오기
			UserVo userVo = SessionUtil.getBosUserVO();
			String userId = userVo.getUserId();
			goodsRegistRequestDto.setCreateId(userId);
			goodsRegistRequestDto.setModifyId(userId);

			//상품의 모든 내역을 UPDATE 하기 전에 데이터를 취합한다.(변경 내역 남기기 위한 작업)
			GoodsRegistResponseDto beforeGoodsDatas = goodsDatas(goodsRegistRequestDto);

			//상품 마스터 항목 수정
			int modifyGoodsInt = goodsRegistService.modifyGoods(goodsRegistRequestDto);

		if (modifyGoodsInt > 0) {
			//상품변경 내역
			int chgLogNum = 0;
			chgLogNum = goodsChangeAssemble("UPDATE", beforeGoodsDatas, goodsRegistRequestDto);

			if (chgLogNum > 0){
				goodsRegistService.modifyDateGoods(Long.parseLong(goodsRegistRequestDto.getIlGoodsId()), Long.parseLong(userId));
			}

			//기본 정보 > 전시 카테고리 Delete and Insert
			goodsRegistService.deleteGoodsCategory(goodsRegistRequestDto);

				if(goodsRegistRequestDto.getDisplayCategoryList() != null) {
					for(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto : goodsRegistRequestDto.getDisplayCategoryList()) {
						goodsRegistCategoryRequestDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRegistCategoryRequestDto.setCreateId(goodsRegistRequestDto.getCreateId());

						goodsRegistService.addGoodsCategory(goodsRegistCategoryRequestDto);
					}

//					log.info("goodsRegistRequestDto.getDisplayCategoryList() : " + goodsRegistRequestDto.getDisplayCategoryList());
				}

				//기본 정보 > 몰인몰 카테고리 Delete and Insert
				if(goodsRegistRequestDto.getMallInMallCategoryList() != null) {
					for(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto : goodsRegistRequestDto.getMallInMallCategoryList()) {
						goodsRegistCategoryRequestDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRegistCategoryRequestDto.setCreateId(goodsRegistRequestDto.getCreateId());
						goodsRegistCategoryRequestDto.setBasicYn(goodsRegistCategoryRequestDto.getMallInMallBasicYn());

						goodsRegistService.addGoodsCategory(goodsRegistCategoryRequestDto);
					}

//					log.info("goodsRegistRequestDto.getMallInMallCategoryList() : " + goodsRegistRequestDto.getMallInMallCategoryList());
				}

				//가격 정보 > 행사/할인 내역 > 우선할인/즉시할인, 임직원 할인 정보 > 임직원 개별할인 정보 승인요청(묶음상품 제외)
				ApiResult<?> goodsDiscountApprProc = goodsDiscountApprProc(goodsRegistRequestDto);

				if(!goodsDiscountApprProc.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
					throw new BaseException(goodsDiscountApprProc.getMessageEnum());
				}

				int orderCount = 0;
				if (goodsRegistRequestDto.getDeleteGoodsReservationOptionList() != null && !goodsRegistRequestDto.getDeleteGoodsReservationOptionList().isEmpty()) {
          	 		orderCount = goodsRegistService.getGoodsReservationOptionOrderCount(goodsRegistRequestDto.getDeleteGoodsReservationOptionList());
				}

				//판매 정보 > 판매 유형(예약판매) > 예약 판매 옵션 설정 Delete and Insert
				// 삭제처리한 항목 먼저 DB에서 삭제 처리 -> DEL_YN = 'Y' 업데이트 처리
				// 추가 : 현재일이 예약옵션에 걸려있을 경우 주문체크 한다.
				if(goodsRegistRequestDto.getDeleteGoodsReservationOptionList() != null && !goodsRegistRequestDto.getDeleteGoodsReservationOptionList().isEmpty()) {
					// 주문수량이 없는 경우에 예약옵션 삭제(update)
					if(orderCount == 0){
						goodsRegistService.deleteGoodsReservationOption(goodsRegistRequestDto.getDeleteGoodsReservationOptionList(), Long.parseLong(goodsRegistRequestDto.getModifyId()));
					} else {
						throw new BaseException(GoodsEnums.GoodsAddStatusTypes.DELETE_GOODS_RESERVATION_OPTION_FAIL);
					}
				}

				// 삭제 처리한 항목 외에 Insert, Update 처리
				if(goodsRegistRequestDto.getGoodsReservationOptionList() != null) {
					for(GoodsRegistReservationOptionDto goodsRegistReservationOptionDto : goodsRegistRequestDto.getGoodsReservationOptionList()) {
						goodsRegistReservationOptionDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRegistReservationOptionDto.setCreateId(goodsRegistRequestDto.getCreateId());
						if(goodsRegistReservationOptionDto.getIlGoodsReserveOptionId() == null) {
							goodsRegistService.addGoodsReservationOption(goodsRegistReservationOptionDto);
						}
						else {
							goodsRegistService.updateGoodsReservationOption(goodsRegistReservationOptionDto);
						}
					}

					//회차 순서 변경(1순위 : 도착예정일, 2순위 출고예정일, 3순위 : 주문수집I/F일, 4순위 : 예약주문가능기간 종료일)
					goodsRegistService.updateGoodsReservationOptionSaleSeq(goodsRegistRequestDto.getIlGoodsId());
				}

				//배송/발주 정보 > 배송정책 Update
				if(goodsRegistRequestDto.getItemWarehouseShippingTemplateList() != null) {
					for(GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto : goodsRegistRequestDto.getItemWarehouseShippingTemplateList()) {
						goodsRegistShippingTemplateDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());

						//매장 배송/픽업 배송 정책이 추가 되면서, 이미 저장된 상품배송정책인지 신규 상품배송정책인지 체크해서 insert OR update 해야 함
						int goodsShippingTemplateChkNum = goodsRegistService.goodsShippingTemplateChkNum(goodsRegistShippingTemplateDto);
						if(goodsShippingTemplateChkNum == 0) {
							goodsRegistShippingTemplateDto.setCreateId(goodsRegistRequestDto.getCreateId());

							goodsRegistShippingTemplateDto.getItemWarehouseShippingTemplateList();

							goodsRegistService.addGoodsShippingTemplate(goodsRegistShippingTemplateDto);
						}
						else {
							goodsRegistShippingTemplateDto.setModifyId(goodsRegistRequestDto.getCreateId());
							goodsRegistService.updateGoodsShippingTemplate(goodsRegistShippingTemplateDto);
						}
					}

					log.info("goodsRegistRequestDto.getItemWarehouseShippingTemplateList() : " + goodsRegistRequestDto.getItemWarehouseShippingTemplateList().toString());
				}

				//혜택/구매 정보 > 추가상품 Delete and Insert
				goodsRegistService.deleteGoodsAdditionalGoodsMapping(goodsRegistRequestDto);

				if(goodsRegistRequestDto.getGoodsAdditionalGoodsMappingList() != null) {
					for(GoodsRegistAdditionalGoodsDto goodsRegistAdditionalGoodsDto : goodsRegistRequestDto.getGoodsAdditionalGoodsMappingList()) {
						goodsRegistAdditionalGoodsDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRegistAdditionalGoodsDto.setCreateId(goodsRegistRequestDto.getCreateId());

						goodsRegistService.addGoodsAdditionalGoodsMapping(goodsRegistAdditionalGoodsDto);
					}

					log.info("goodsRegistRequestDto.getGoodsAdditionalGoodsMappingList() : " + goodsRegistRequestDto.getGoodsAdditionalGoodsMappingList().toString());
				}

				//추천상품 등록 > 추천상품 Delete and Insert
				goodsRegistService.deleteGoodsRecommend(goodsRegistRequestDto);

				if(goodsRegistRequestDto.getGoodsRecommendList() != null) {
					for(GoodsRegistAdditionalGoodsDto goodsRecommendDto : goodsRegistRequestDto.getGoodsRecommendList()) {
						goodsRecommendDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
						goodsRecommendDto.setCreateId(goodsRegistRequestDto.getCreateId());

						goodsRegistService.addGoodsRecommend(goodsRecommendDto);
					}
				}

				Map<String, List<GoodsDailyCycleBulkVo>> goodsDailyCycleBulkList = new HashMap<String, List<GoodsDailyCycleBulkVo>>();

				if(GoodsEnums.GoodsType.DAILY.getCode().equals(goodsRegistRequestDto.getGoodsType())) {	//상품유형이 '일일 상품'일 경우에만 입력
					goodsDailyCycleBulkList =  addGoodsDailyMapping(goodsRegistRequestDto);	//식단주기, 일괄 배달 설정 입력

					if(goodsDailyCycleBulkList != null && goodsDailyCycleBulkList.size() == 2) {
						goodsRegistRequestDto.setGoodsDailyCycleList(goodsDailyCycleBulkList.get("goodsDailyCycleList"));
						goodsRegistRequestDto.setGoodsDailyBulkList(goodsDailyCycleBulkList.get("goodsDailyBulkList"));
					}
				}

				if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsRegistRequestDto.getGoodsType())) {	//상품유형이 '묶음 상품'일 경우에만 입력
					// 1.묶음 상품 이미지 수정
					modifyGoodsPackageImage(goodsRegistRequestDto);

					// 2.개별상품 상품별 대표 이미지 순서 변경
					updateGoodsImageSortUpdate(goodsRegistRequestDto);
				}

				// 상품 상세 이미지 등록 처리
				goodsDetailImageBiz.getUpdateGoodsInfoForDetailImage(beforeGoodsDatas, goodsRegistRequestDto, null);


			}

			//상품변경 내역
			goodsChangeAssemble("UPDATE", beforeGoodsDatas, goodsRegistRequestDto);

			goodsRegistResponseDto.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
			goodsRegistResponseDto.setGoodsName(goodsRegistRequestDto.getGoodsName());

			return ApiResult.success(goodsRegistResponseDto);
		}
	}

	//우선할인, 즉시할인, 임직원 개별 할인 승인 내역 저장
	private void goodsDisocuntApprTran(String ilGoodsId, String goodsType, List<GoodsDiscountRequestDto> goodsDiscountRequestDtoList
			, List<GoodsRegistApprRequestDto> goodsRegistApprRequestDtoList, List<GoodsRegistPackageGoodsPriceDto> goodsRegistPackageGoodsPriceDto, GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();					// USER ID
		String today = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");

		if(goodsDiscountRequestDtoList != null && goodsRegistApprRequestDtoList != null
				&& !goodsDiscountRequestDtoList.isEmpty() && !goodsRegistApprRequestDtoList.isEmpty()){
			GoodsDiscountApprVo goodsDiscountApprVo = new GoodsDiscountApprVo();
			int goodsDiscountSeq = 0;

			for(GoodsDiscountRequestDto goodsDiscountRequestDto : goodsDiscountRequestDtoList) {
				if("APPR_STAT.NONE".equals(goodsDiscountRequestDto.getApprovalStatusCode())
					&& goodsDiscountRequestDto.getGoodsDiscountId() == null){		//최초 가격 승인 등록인것만 승인 요청 진행
					goodsDiscountApprVo.setIlGoodsId(ilGoodsId);
					goodsDiscountApprVo.setDiscountTp(goodsDiscountRequestDto.getDiscountTypeCode());
					goodsDiscountApprVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
					goodsDiscountApprVo.setDiscountStartDt(goodsDiscountRequestDto.getDiscountStartDateTime());
					goodsDiscountApprVo.setDiscountEndDt(goodsDiscountRequestDto.getDiscountEndDateTime()+":59");
					goodsDiscountApprVo.setDiscountMethodTp(goodsDiscountRequestDto.getDiscountMethodTypeCode());

					if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)) { // 묶음상품 할인요청의 경우 원가/정상가를 담는 객체가 다르다.
						goodsDiscountApprVo.setStandardPrice(goodsDiscountRequestDto.getStandardPrice());
						goodsDiscountApprVo.setRecommendedPrice(goodsDiscountRequestDto.getRecommendedPrice());
					}
					else {
						goodsDiscountApprVo.setStandardPrice(goodsDiscountRequestDto.getItemStandardPrice());
						goodsDiscountApprVo.setRecommendedPrice(goodsDiscountRequestDto.getItemRecommendedPrice());
					}

					if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)
							&& GoodsDiscountType.EMPLOYEE.getCode().equals(goodsDiscountRequestDto.getDiscountTypeCode())){
						//묶음상품이고 임직원 개별할인이라면 개별품목별로 할인율을 저장하므로 정률, 고정가에 관계없이 마스터 할인승인 내역에 할인금액, 할인율 다 0으로 입력 처리
						goodsDiscountApprVo.setDiscountSalePrice(0);
						goodsDiscountApprVo.setDiscountRatio(0);
					}
					else {
						if (GoodsDiscountMethodType.FIXED_RATE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {
							//할인 유형이 '정률할인' 이라면 판매가를 저장하지 않는다.
							goodsDiscountApprVo.setDiscountSalePrice(0);
							goodsDiscountApprVo.setDiscountRatio(goodsDiscountRequestDto.getDiscountRatio());
						} else if (GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {
							//할인 유형이 '고정가할인' 이라면 할인율을 저장하지 않는다.
							goodsDiscountApprVo.setDiscountSalePrice(goodsDiscountRequestDto.getDiscountSalePrice());
							goodsDiscountApprVo.setDiscountRatio(0);
						}
					}

					for(GoodsRegistApprRequestDto goodsRegistApprRequestDto : goodsRegistApprRequestDtoList) {	//승인 관리자
						if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode())) {
							goodsDiscountApprVo.setApprSubUserId(goodsRegistApprRequestDto.getApprUserId());
						}

						if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode())) {
							goodsDiscountApprVo.setApprReqUserId(userId);
							goodsDiscountApprVo.setApprUserId(goodsRegistApprRequestDto.getApprUserId());
						}
					}

					goodsDiscountApprVo.setApprReqDt(today);
					goodsDiscountApprVo.setCreateId(userId);
					goodsDiscountApprVo.setCreateDt(today);

					/*
					묶음상품 임직원 개별할인의 경우 첫번째 행의 할인 시작일/종료일을 가지고 할인승인 마스터 정보를 생성하고, 다른 할인은 첫번째 행만 할인 내역으로 넘어오기 때문에
					goodsDiscountSeq = 0 일때만 할인 승인 마스터, 할인 승인 마스터 히스토리 저장 처리
					*/
					if(goodsDiscountSeq == 0) {
						if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)
								&& GoodsDiscountType.EMPLOYEE.getCode().equals(goodsDiscountRequestDto.getDiscountTypeCode())){
							GoodsDiscountRequestDto targetDto = goodsDiscountRequestDtoList.stream()
									.filter(vo -> ApprovalEnums.ApprovalStatus.NONE.getCode().equals(vo.getApprovalStatusCode()))
									.max(Comparator.comparingInt(GoodsDiscountRequestDto::getRowNum))
									.orElse(new GoodsDiscountRequestDto());

							goodsDiscountApprVo.setStandardPrice(goodsRegistRequestDto.getGoodsPackagePriceList().get(0).getStandardPrice());
							goodsDiscountApprVo.setRecommendedPrice(goodsRegistRequestDto.getGoodsPackagePriceList().get(0).getRecommendedPrice());
							goodsDiscountApprVo.setDiscountRatio(targetDto.getDiscountRatio());
							goodsDiscountApprVo.setDiscountSalePrice(targetDto.getDiscountSalePrice());
						}
						goodsRegistService.addGoodsDiscountAppr(goodsDiscountApprVo);

						goodsDiscountApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
						goodsDiscountApprVo.setStatusCmnt(null);
						goodsRegistService.addGoodsDiscountApprStatusHistory(goodsDiscountApprVo);
					}

					if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)
							&& GoodsDiscountType.EMPLOYEE.getCode().equals(goodsDiscountRequestDto.getDiscountTypeCode())){
						//묶음상품이고 임직원 개별할인이라면 개별품목 고정가 할인가격 리스트가 따로 존재하지 않고 할인내역 자체가 리스트 이므로
						if(goodsDiscountSeq != goodsDiscountRequestDto.getRowCount()) {		//합계 행은 계산하지 않는다.
							//개별품목 고정가 할인가격 리스트 승인 저장 처리
							GoodsPackageItemFixedDiscountPriceApprVo goodsPackageItemFixedDiscountPriceApprVo = new GoodsPackageItemFixedDiscountPriceApprVo();

							goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsDiscountApprId(goodsDiscountApprVo.getIlGoodsDiscountApprId());
							goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsPackageGoodsMappingId(goodsDiscountRequestDto.getIlGoodsPackageGoodsMappingId());
							goodsPackageItemFixedDiscountPriceApprVo.setSalePrice(0);
							goodsPackageItemFixedDiscountPriceApprVo.setUnitSalePrice(0);
							goodsPackageItemFixedDiscountPriceApprVo.setDiscountRatio(goodsDiscountRequestDto.getDiscountRatio());

							goodsRegistService.addGoodsPackageItemFixedDiscountPriceAppr(goodsPackageItemFixedDiscountPriceApprVo);
						}
					}
					else if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType) && goodsRegistPackageGoodsPriceDto != null
							&& !goodsRegistPackageGoodsPriceDto.isEmpty()) {		//묶음상품이고 개별품목 고정가 할인가격 리스트가 있다면
						//개별품목 고정가 할인가격 리스트 승인 저장 처리
						for(GoodsRegistPackageGoodsPriceDto goodsPackageItemFixedDiscountPrice : goodsRegistPackageGoodsPriceDto) {
							GoodsPackageItemFixedDiscountPriceApprVo goodsPackageItemFixedDiscountPriceApprVo = new GoodsPackageItemFixedDiscountPriceApprVo();

							goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsDiscountApprId(goodsDiscountApprVo.getIlGoodsDiscountApprId());
							goodsPackageItemFixedDiscountPriceApprVo.setIlGoodsPackageGoodsMappingId(goodsPackageItemFixedDiscountPrice.getIlGoodsPackageGoodsMappingId());
							if(GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(goodsDiscountRequestDto.getDiscountMethodTypeCode())) {
								goodsPackageItemFixedDiscountPriceApprVo.setSalePrice(goodsPackageItemFixedDiscountPrice.getSalePricePerUnit() * goodsPackageItemFixedDiscountPrice.getGoodsQuantity());
								goodsPackageItemFixedDiscountPriceApprVo.setUnitSalePrice(goodsPackageItemFixedDiscountPrice.getSalePricePerUnit());
								goodsPackageItemFixedDiscountPriceApprVo.setDiscountRatio(0);
							}
							else{
								goodsPackageItemFixedDiscountPriceApprVo.setSalePrice(0);
								goodsPackageItemFixedDiscountPriceApprVo.setUnitSalePrice(0);
								goodsPackageItemFixedDiscountPriceApprVo.setDiscountRatio(goodsDiscountRequestDto.getDiscountRatio());
							}

							goodsRegistService.addGoodsPackageItemFixedDiscountPriceAppr(goodsPackageItemFixedDiscountPriceApprVo);
						}
					}
					goodsDiscountSeq++;
				}
			}
		}
	}

	//상품할인승인내역 저장(상품 할인 일괄 업로드에서 사용)
	public int addGoodsDiscountAppr(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception {
		return goodsRegistService.addGoodsDiscountAppr(goodsDiscountApprVo);
	}

	//상품할인승인내역 히스토리 저장(상품 할인 일괄 업로드에서 사용)
	public int addGoodsDiscountApprStatusHistory(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception {
		return goodsRegistService.addGoodsDiscountApprStatusHistory(goodsDiscountApprVo);
	}

	//상품 할인 승인 내역 확인, 승인 내역 존재시 요청 자격 확인(상품 할인 일괄 업로드에서 사용)
	public GoodsDiscountApprVo goodsDiscountApprInfo(String ilGoodsId, String ilGoodsDiscountApprId, String goodsDiscountType) throws Exception {
		return goodsRegistService.goodsDiscountApprInfo(ilGoodsId, ilGoodsDiscountApprId, goodsDiscountType);
	}

	//우선할인, 즉시할인, 임직원 개별 할인 승인 처리
	private ApiResult<?> goodsDiscountApprProc(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		MessageCommEnum enums = BaseEnums.Default.SUCCESS;
		String ilGoodsId = goodsRegistRequestDto.getIlGoodsId();
		String goodsType = goodsRegistRequestDto.getGoodsType();

		UserVo userVo = SessionUtil.getBosUserVO();
		String companyType = userVo.getCompanyType();		// 회사타입

		if(companyType.equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//관리자 권한이라면
			//우선 할인
			if (goodsRegistRequestDto.getGoodsDiscountPriorityList() != null && goodsRegistRequestDto.getGoodsDiscountPriorityApproList() != null) {
				GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.PRIORITY.getCode());

				if(goodsDiscountApprInfo != null &&
						(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
							|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
						)){		//현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
					enums = GoodsEnums.GoodsDiscountApprProcStatus.PRIORITY_APPR_DUPLICATE;
				}
				else{
					goodsDisocuntApprTran(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsDiscountPriorityList()
							, goodsRegistRequestDto.getGoodsDiscountPriorityApproList(), goodsRegistRequestDto.getGoodsDiscountPriorityCalcList(), null);
				}
			}

			//즉시 할인
			if (goodsRegistRequestDto.getGoodsDiscountImmediateList() != null && goodsRegistRequestDto.getGoodsDiscountImmediateApproList() != null) {
				GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.IMMEDIATE.getCode());

				if(goodsDiscountApprInfo != null &&
						(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
								|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
						)){		//현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
					enums = GoodsEnums.GoodsDiscountApprProcStatus.IMMEDIATE_APPR_DUPLICATE;
				}
				else{
					goodsDisocuntApprTran(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsDiscountImmediateList()
							, goodsRegistRequestDto.getGoodsDiscountImmediateApproList(), goodsRegistRequestDto.getGoodsDiscountImmediateCalcList(), null);
				}
			}

			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)){		//묶음 상품이라면
				//묶음 상품 기본 판매가
				if (goodsRegistRequestDto.getGoodsPackagePriceList() != null && goodsRegistRequestDto.getGoodsPackagePriceApproList() != null) {
					GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.PACKAGE.getCode());

					if(goodsDiscountApprInfo != null &&
							   (goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
										|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
							   )){		//현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
						enums = GoodsEnums.GoodsDiscountApprProcStatus.PACKAGE_APPR_DUPLICATE;
					}
					else{
						goodsDisocuntApprTran(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsPackagePriceList()
								, goodsRegistRequestDto.getGoodsPackagePriceApproList(), goodsRegistRequestDto.getGoodsPackageCalcList(), null);
					}
				}

				//임직원 개별 할인
				if (goodsRegistRequestDto.getGoodsPackageDiscountEmployeeList() != null && goodsRegistRequestDto.getGoodsPackageDiscountEmployeeApproList() != null) {
					GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.EMPLOYEE.getCode());

					if (goodsDiscountApprInfo != null &&
								(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
										 || goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
								)) {        //현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
						enums = GoodsEnums.GoodsDiscountApprProcStatus.EMPLOYEE_APPR_DUPLICATE;
					} else {
						goodsDisocuntApprTran(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsPackageDiscountEmployeeList()
								, goodsRegistRequestDto.getGoodsPackageDiscountEmployeeApproList(), null, goodsRegistRequestDto);
					}
				}
			}
			else {
				//임직원 개별할인
				if (goodsRegistRequestDto.getGoodsDiscountEmployeeList() != null && goodsRegistRequestDto.getGoodsDiscountEmployeeApproList() != null) {
					GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(ilGoodsId, null, GoodsDiscountType.EMPLOYEE.getCode());

					if (goodsDiscountApprInfo != null &&
								(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
										 || goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
								)) {        //현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
						enums = GoodsEnums.GoodsDiscountApprProcStatus.EMPLOYEE_APPR_DUPLICATE;
					} else {
						goodsDisocuntApprTran(ilGoodsId, goodsType, goodsRegistRequestDto.getGoodsDiscountEmployeeList()
								, goodsRegistRequestDto.getGoodsDiscountEmployeeApproList(), null, null);
					}
				}
			}
		}
		else {
			enums = GoodsEnums.GoodsDiscountApprProcStatus.NOT_HEADQUART;
		}

		return ApiResult.result(enums);
	}

	//상품 할인 > 승인완료, 반려, 요청철회 이후 처리(승인 관리 메뉴에서 호출해서 처리)
	public MessageCommEnum goodsDiscountApprAfterProc(String ilGoodsDiscountApprId, String discountType) throws Exception {
		MessageCommEnum enums = BaseEnums.Default.FAIL;

		GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistService.goodsDiscountApprInfo(null, ilGoodsDiscountApprId, discountType);

		if(goodsDiscountApprInfo != null){
			if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {		//현재 상태가 승인완료 상태라면
				//상품 할인 테이블 저장
				GoodsDiscountApprVo goodsDiscountApprVo = new GoodsDiscountApprVo();
				goodsDiscountApprVo.setIlGoodsDiscountApprId(ilGoodsDiscountApprId);

				int discountTranNum = goodsRegistService.addGoodsDiscountByAppr(goodsDiscountApprVo);
				goodsRegistService.addGoodsPackageItemFixedDiscountPriceByAppr(goodsDiscountApprVo);// 묶음상품의 경우 각 상품의 할인 판매가격 설정값 저장. 묶음 상품이 아닌 경우 쿼리에서 SELECT 항목이 없기 때문에 따로 묶음 상품에만 해당 로직이 적용되도록 조건 처리 하지 않아도 된다.

				//상품 할인 승인 테이블에 상품할인ID UPDATE
				goodsRegistService.updateGoodsDiscountAppr(goodsDiscountApprVo);

				//묶음상품 기본 판매가 등록이고 할인 시작일이 존재 할때
				if(GoodsDiscountType.PACKAGE.getCode().equals(discountType) && goodsDiscountApprInfo.getDiscountStartDt() != null){
					//승인완료 처리가 되면 새로 저장된 할인 내역의, 바로 전 할인 내역의 종료일(종료일이 '2999-12-31 23:59:59'인 할인내역)을 새로 저장된 시작일 - 1초 로 변경 한다.
					goodsRegistService.updateGoodsDiscountApprEndTime(goodsDiscountApprInfo.getIlGoodsId(), goodsDiscountApprInfo.getDiscountStartDt());
					goodsRegistService.updateGoodsDiscountEndTime(goodsDiscountApprInfo.getIlGoodsId(), goodsDiscountApprInfo.getDiscountStartDt());
				}

				if(discountTranNum > 0) {
					//가격정보 > 판매 가격정보 등록
					GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

					goodsRegistRequestDto.setInDebugFlag(false);
					goodsRegistRequestDto.setIlGoodsId(goodsDiscountApprInfo.getIlGoodsId());
					goodsDiscountBiz.spGoodsPriceUpdateWhenGoodsDiscountChanges(goodsRegistRequestDto.getIlGoodsId());

					enums = BaseEnums.Default.SUCCESS;
				}
			}
			else if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
					|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())){	//현재 상태가 반려or요청철회 상태라면
				enums = BaseEnums.Default.SUCCESS;
			}
		}

		return enums;
	}

	//모든 상품 > 상품 승인완료(APPR_STAT.APPROVED), 반려, 요청철회 이후 처리(승인 관리 메뉴에서 호출해서 처리)
	public MessageCommEnum goodsApprAfterProc(String ilGoodsApprId, String apprKindTp) throws Exception {
		MessageCommEnum enums = BaseEnums.Default.FAIL;

		GoodsRegistApprVo goodsApprInfo = goodsRegistService.goodsApprInfo(null, null, ilGoodsApprId, apprKindTp);
		if(goodsApprInfo != null) {
			GoodsRegistApprVo goodsRegistApprVo = new GoodsRegistApprVo();
			goodsRegistApprVo.setApprKindTp(apprKindTp);

			if( apprKindTp.equals(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode())
					&& goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode()) ) {	//관리자 상품 등록 요청 승인이고 승인완료 상태라면
				goodsRegistApprVo.setSaleStatus(GoodsEnums.SaleStatus.WAIT.getCode());
				goodsRegistApprVo.setSavedSaleStatus(null);
				goodsRegistApprVo.setIlGoodsId(goodsApprInfo.getIlGoodsId());

				goodsRegistService.updateGoodsApprovedSaleStatus(goodsRegistApprVo);						//상품의 판매 상태를 '판매대기'으로 UPDATE
				enums = BaseEnums.Default.SUCCESS;
			}
			else if( apprKindTp.equals(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode())
					&& ( goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
					|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode()) ) ) {	//관리자 상품 등록 요청 승인이고 반려or요청철회 상태라면
				enums = BaseEnums.Default.SUCCESS;
			}
			else if(apprKindTp.equals(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode())
					&& ( goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())
					|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
					|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode()) ) ) {	//거래처 상품 변경 요청 승인이고 승인완료or반려or요청철회 상태라면

				if(goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {				//최종 승인일때만 상품 변경내역 저장을 진행
					if(goodsApprInfo.getGoodsName() != null) {
						goodsRegistApprVo.setGoodsName(goodsApprInfo.getGoodsName());
					}

					goodsRegistApprVo.setGoodsDesc(goodsApprInfo.getGoodsDesc());

					goodsRegistApprVo.setSearchKeyword(goodsApprInfo.getSearchKeyword());

					if(goodsApprInfo.getDisplayYn() != null) {
						goodsRegistApprVo.setDisplayYn(goodsApprInfo.getDisplayYn());
					}

					if(goodsApprInfo.getSaleStartDate() != null) {
						goodsRegistApprVo.setSaleStartDate(goodsApprInfo.getSaleStartDate());
					}

					if(goodsApprInfo.getSaleEndDate() != null) {
						goodsRegistApprVo.setSaleEndDate(goodsApprInfo.getSaleEndDate());
					}

//					if(goodsApprInfo.getGoodsMemo() != null) {
					goodsRegistApprVo.setGoodsMemo(goodsApprInfo.getGoodsMemo());
//					}

					if(goodsApprInfo.getSaleStatus() != null) {
						goodsRegistApprVo.setSaleStatus(goodsApprInfo.getSaleStatus());
					} else {
						goodsRegistApprVo.setSaleStatus(goodsApprInfo.getSavedSaleStatus());
					}

					//상품의 모든 내역을 UPDATE 하기 전에 데이터를 취합한다.(변경 내역 남기기 위한 작업)
					GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();
					goodsRegistRequestDto.setIlGoodsId(goodsApprInfo.getIlGoodsId());

					GoodsRegistResponseDto beforeGoodsDatas = goodsDatas(goodsRegistRequestDto);

					//상품 마스터 변경 내역
					String createDate = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
					Map<String, String> beforeGoodsData = BeanUtils.describe(beforeGoodsDatas.getIlGoodsDetail());
					Map<String, String> afterGoodsData = BeanUtils.describe(goodsRegistApprVo);

					addGoodsMasterChangeLog("UPDATE", goodsApprInfo.getIlGoodsId(), goodsApprInfo.getApprReqUserId(), beforeGoodsData, afterGoodsData, createDate);
					goodsRegistApprVo.setIlGoodsId(goodsApprInfo.getIlGoodsId());
					goodsRegistApprVo.setModifyId(goodsApprInfo.getApprReqUserId());

					goodsRegistService.updateGoodsApprovedSaleStatus(goodsRegistApprVo);

					// 상품 상세 이미지 등록 처리
					goodsDetailImageBiz.getUpdateGoodsInfoForDetailImage(beforeGoodsDatas, null, goodsRegistApprVo);

				}
				else if(goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
						|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())){	// 반려, 요청철회시 상품 상태 원복

					goodsRegistApprVo.setSaleStatus(goodsApprInfo.getSavedSaleStatus());
					goodsRegistApprVo.setSavedSaleStatus("");
					goodsRegistApprVo.setIlGoodsId(goodsApprInfo.getIlGoodsId());
					goodsRegistApprVo.setModifyId(goodsApprInfo.getApprReqUserId());
					goodsRegistService.updateGoodsApprovedSaleStatus(goodsRegistApprVo);

					goodsRegistService.addGoodsChangeLog(GoodsChangeLogVo.builder()
							.ilGoodsId(goodsRegistApprVo.getIlGoodsId())
							.tableNm("IL_GOODS")
							.tableIdNew(goodsRegistApprVo.getIlGoodsId())
							.tableIdOrig(goodsRegistApprVo.getIlGoodsId())
							.beforeData(SaleStatus.WAIT.getCode())
							.afterData(goodsRegistApprVo.getSaleStatus())
							.columnNm(GoodsColumnComment.SALE_STATUS.getCode())
							.columnLabel(GoodsColumnComment.SALE_STATUS.getCodeName())
							.createId("0")
							.createDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"))
							.build());
				}
				enums = BaseEnums.Default.SUCCESS;
			}
		}
		else {
			enums = GoodsEnums.GoodsApprProcStatus.NONE_APPR;
		}

		log.info("enums : " + enums);

		return enums;
	}

	//모든 상품 > 상품 승인 처리
	private ApiResult<?> goodsApprProc(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		MessageCommEnum enums = BaseEnums.Default.SUCCESS;
		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();					// USER ID
		String companyType = userVo.getCompanyType();		// 회사타입
		String clientType = userVo.getClientType();			// 거래처 타입

		if(!goodsRegistRequestDto.getGoodsApprList().isEmpty()) {

			GoodsRegistApprVo goodsRegistApprVo = new GoodsRegistApprVo();

			for(GoodsRegistApprRequestDto goodsRegistApprRequestDto : goodsRegistRequestDto.getGoodsApprList()) {
				if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode())) {
					goodsRegistApprVo.setApprSubUserId(goodsRegistApprRequestDto.getApprUserId());
				}

				if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode())) {
					goodsRegistApprVo.setIlGoodsId(goodsRegistRequestDto.getIlGoodsId());
					goodsRegistApprVo.setApprKindTp(goodsRegistApprRequestDto.getApprKindTp());
					goodsRegistApprVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
					goodsRegistApprVo.setApprReqUserId(userId);
					goodsRegistApprVo.setApprUserId(goodsRegistApprRequestDto.getApprUserId());
				}
			}

			if(companyType.equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//관리자 권한이라면
				goodsRegistRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode());
				GoodsRegistApprVo goodsApprInfo = goodsRegistService.goodsApprInfo(userId, goodsRegistRequestDto.getIlGoodsId(), null, goodsRegistRequestDto.getApprKindTp());

				if(goodsApprInfo == null || goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
						|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())
						|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode())) {		//승인 내역이 없거나, 반려/요청철회/폐기 상태일때
					goodsRegistService.addGoodsAppr(goodsRegistApprVo);

					goodsRegistApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
					goodsRegistApprVo.setStatusCmnt(null);

					goodsRegistService.addGoodsApprStatusHistory(goodsRegistApprVo);

					if(goodsRegistRequestDto.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {		//승인 요청 시에 상품의 판매 상태가 '판매중'이라면
						goodsRegistApprVo.setSaleStatus(GoodsEnums.SaleStatus.WAIT.getCode());						//판매 상태를 '판매 대기'로 변경
						goodsRegistApprVo.setSavedSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());				//현재 판매 상태를 저장
						goodsRegistService.updateGoodsApprovedSaleStatus(goodsRegistApprVo);
					}
				}
				else {
					enums = GoodsEnums.GoodsApprProcStatus.APPR_DUPLICATE;
				}
			}
			else if(companyType.equals(CompanyEnums.CompanyType.CLIENT.getCode()) && clientType.equals(CompanyEnums.ClientType.CLIENT.getCode())) {	//거래처 권한이라면
				goodsRegistRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode());
				GoodsRegistApprVo goodsApprInfo = goodsRegistService.goodsApprInfo(userId, goodsRegistRequestDto.getIlGoodsId(), null, goodsRegistRequestDto.getApprKindTp());

				GoodsRegistVo goodsDetail = goodsRegistService.goodsDetail(goodsRegistRequestDto);	//상품 내역
				int differentCount = 0;																//거래처 상품 변경 카운트

				if(goodsDetail != null) {

					int dateCompare = DateUtil.string2Date(goodsDetail.getModifyDate(), "yyyy-MM-dd HH:mm:ss").compareTo(DateUtil.string2Date(goodsRegistRequestDto.getLoadDateTime(), "yyyy-MM-dd HH:mm:ss"));

					if(dateCompare > 0
							&& !goodsDetail.getSaleStatus().equals(goodsRegistRequestDto.getSaleStatus())) {	//DB의 상품 마스터 수정 시간이 거래처에서 페이지 Load한 시간 보다 크고, 거래처에서 입력한 판매 상태 값과 DB의 판매 상태 값이 다르다면
						enums = GoodsEnums.GoodsApprProcStatus.ADMIN_DIFFERENT_GOODS;
						goodsRegistRequestDto.setLoadDateTime(null);
					}
					else {
						if(!goodsDetail.getGoodsName().equals(goodsRegistRequestDto.getGoodsName())) {
							differentCount++;
							goodsRegistApprVo.setGoodsName(goodsRegistRequestDto.getGoodsName());
						}

						if(!GoodsEnums.GoodsType.ADDITIONAL.getCode().equals(goodsRegistRequestDto.getGoodsType())
								&& !GoodsEnums.GoodsType.GIFT.getCode().equals(goodsRegistRequestDto.getGoodsType())
								&& !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(goodsRegistRequestDto.getGoodsType())) {		//추가/증정 상품은 아래 항목들이 존재하지 않으므로 제외
							if(!StringUtil.nvl(goodsDetail.getGoodsDesc()).equals(StringUtil.nvl(goodsRegistRequestDto.getGoodsDesc()))) {
								differentCount++;
							}
							goodsRegistApprVo.setGoodsDesc(goodsRegistRequestDto.getGoodsDesc()); // 입력한 값을 제거 후 저장할 수 있으므로, 항상저장

							if(!StringUtil.nvl(goodsDetail.getSearchKeyword()).equals(StringUtil.nvl(goodsRegistRequestDto.getSearchKeyword()))) {
								differentCount++;
							}
							goodsRegistApprVo.setSearchKeyword(goodsRegistRequestDto.getSearchKeyword()); // 입력한 값을 제거 후 저장할 수 있으므로, 항상저장

							if(!goodsDetail.getDisplayYn().equals(goodsRegistRequestDto.getDisplayYn())) {
								differentCount++;
							}
							goodsRegistApprVo.setDisplayYn(goodsRegistRequestDto.getDisplayYn());

							if(!goodsDetail.getSaleStartDate().equals(goodsRegistRequestDto.getSaleStartDate()+":00")) {
								differentCount++;
								goodsRegistApprVo.setSaleStartDate(goodsRegistRequestDto.getSaleStartDate());
							}

							if(!goodsDetail.getSaleEndDate().equals(goodsRegistRequestDto.getSaleEndDate()+":59")) {
								differentCount++;
								goodsRegistApprVo.setSaleEndDate(goodsRegistRequestDto.getSaleEndDate());
							}
						}

						if(!goodsDetail.getSaleStatus().equals(goodsRegistRequestDto.getSaleStatus())) {
							differentCount++;
							goodsRegistApprVo.setSaleStatus(goodsRegistRequestDto.getSaleStatus());
							goodsRegistApprVo.setSavedSaleStatus(goodsDetail.getSaleStatus());
						}

						if(!goodsDetail.getGoodsMemo().equals(goodsRegistRequestDto.getGoodsMemo())) {
							differentCount++;
						}
						goodsRegistApprVo.setGoodsMemo(goodsRegistRequestDto.getGoodsMemo()); // 입력한 값을 제거 후 저장할 수 있으므로, 항상저장

						if(differentCount == 0) {
							enums = GoodsEnums.GoodsApprProcStatus.NOT_DIFFERENT_GOODS;
						}
						else if(differentCount > 0){
							if(goodsApprInfo == null || goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
									|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())
									|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode())
									|| goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {	//승인 내역이 없거나, 반려/요청철회/폐기/승인완료 상태일때
								goodsRegistService.addGoodsAppr(goodsRegistApprVo);

								goodsRegistApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
								goodsRegistApprVo.setStatusCmnt(null);

								goodsRegistService.addGoodsApprStatusHistory(goodsRegistApprVo);

								// 현재 판매 상태 저장 and 판매 대기로 변경
								if (!SaleStatus.WAIT.getCode().equals(goodsDetail.getSaleStatus()) && !SaleStatus.STOP_PERMANENT_SALE.getCode().equals(goodsDetail.getSaleStatus()) && !SaleStatus.SAVE.getCode().equals(goodsDetail.getSaleStatus())){
									goodsRegistApprVo.setSavedSaleStatus(goodsDetail.getSaleStatus());
									goodsRegistApprVo.setSaleStatus(GoodsEnums.SaleStatus.WAIT.getCode());
									goodsRegistService.updateGoodsRequestSaleStatus(goodsRegistApprVo);
									goodsRegistService.addGoodsChangeLog(GoodsChangeLogVo.builder()
											.ilGoodsId(goodsDetail.getIlGoodsId())
											.tableNm("IL_GOODS")
											.tableIdNew(goodsDetail.getIlGoodsId())
											.tableIdOrig(goodsDetail.getIlGoodsId())
											.beforeData(goodsDetail.getSaleStatus())
											.afterData(SaleStatus.WAIT.getCode())
											.columnNm(GoodsColumnComment.SALE_STATUS.getCode())
											.columnLabel(GoodsColumnComment.SALE_STATUS.getCodeName())
											.createId("0")
											.createDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"))
											.build());
								}
								enums = GoodsEnums.GoodsApprProcStatus.CLINET_APPR_REQUEST;
							}
							else {
								enums = GoodsEnums.GoodsApprProcStatus.APPR_DUPLICATE;
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
				goodsRegistRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode());
				GoodsRegistApprVo goodsApprInfo = goodsRegistService.goodsApprInfo(userId, goodsRegistRequestDto.getIlGoodsId(), null, goodsRegistRequestDto.getApprKindTp());

				if(goodsApprInfo != null && goodsApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())) {		//거래처 상품 수정 승인 요청 내역이 있다면
					enums = GoodsEnums.GoodsApprProcStatus.CLIENT_APPR_DUPLICATE;
				}
			}
			else if(companyType.equals(CompanyEnums.CompanyType.CLIENT.getCode()) && clientType.equals(CompanyEnums.ClientType.CLIENT.getCode())) {	//거래처 권한이라면
				enums = GoodsEnums.GoodsApprProcStatus.NONE_APPR;
			}

			// 해당 상품이 묶음 상품의 구성상품일 경우
			List<GoodsVo> packageGoodsList = goodsRegistService.getPackageGoodsListByMappingGoods(goodsRegistRequestDto.getIlGoodsId());

			// 묶음 상품 선물하기 허용 여부 변경
			packageGoodsList.stream()
					.filter(packageGoods -> !goodsRegistRequestDto.getPresentYn().equals(packageGoods.getPresentYn()))
					.forEach(goodsVo -> {
						String presentYnResult = "";
						List<String> presentYns = goodsRegistService.getPresentYnsByPackageGoodsId(goodsVo.getIlGoodsId());
						if(presentYns.size() == 1 && presentYns.stream().anyMatch(p -> PresentYn.Y.getCode().equals(p))) presentYnResult = PresentYn.Y.getCode();
						else if(presentYns.size() == 1 && presentYns.stream().anyMatch(p -> PresentYn.NA.getCode().equals(p))) presentYnResult = PresentYn.NA.getCode();
						else presentYnResult = PresentYn.N.getCode();

						if(!presentYnResult.isEmpty() && !presentYnResult.equals(goodsVo.getPresentYn())) goodsRegistService.updateGoodsPresentYn(goodsVo.getIlGoodsId(), presentYnResult);
					});

			// 품절(관리자), 판매중지로 판매 상태 변경 > 해당 상품이 묶음 상품의 구성상품일 경우 묶음 상품도 판매 상태 변경
			if(SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(goodsRegistRequestDto.getSaleStatus()) || SaleStatus.STOP_SALE.getCode().equals(goodsRegistRequestDto.getSaleStatus())) {
				List<GoodsVo> filteringStopSaleGoodsList = packageGoodsList.stream()
						.filter(pgl -> pgl.getSaleStatus() != SaleStatus.STOP_SALE.getCode() && pgl.getSaleStatus() != SaleStatus.STOP_PERMANENT_SALE.getCode())
						.collect(Collectors.toList());

				if(filteringStopSaleGoodsList != null && !filteringStopSaleGoodsList.isEmpty()) {
					goodsRegistService.updateGoodsStopSale(filteringStopSaleGoodsList.stream().map(GoodsVo::getIlGoodsId).collect(Collectors.toList()), goodsRegistRequestDto.getSaleStatus());

					// Change Log
					filteringStopSaleGoodsList.forEach(goods -> {
						goodsRegistService.addGoodsChangeLog(GoodsChangeLogVo.builder()
								.ilGoodsId(goods.getIlGoodsId())
								.tableNm("IL_GOODS")
								.tableIdNew(goods.getIlGoodsId())
								.tableIdOrig(goods.getIlGoodsId())
								.beforeData(goods.getSaleStatus())
								.afterData(goodsRegistRequestDto.getSaleStatus())
								.columnNm("saleStatus")
								.columnLabel(GoodsColumnComment.SALE_STATUS.getCodeName())
								.createId("0")
								.createDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"))
								.build());
					});
				}
			}

			/* 풀무원샵 상품코드 시작 */
			goodsRegistService.setGoodsCodeList(goodsRegistRequestDto.getIlGoodsId(), goodsRegistRequestDto.getGoodsCodeList(), goodsRegistRequestDto.getCreateId());
			/* 풀무원샵 상품코드 종료 */
		}

		return ApiResult.result(enums);
	}

	/**
	 * 품목 승인 요청시 상품 판매 대기 상태
	 * ilItemCode : 품목 ID
	 **/
	@Override
	public int updateGoodsSaleStatusToWaitByItemAppr(String ilItemCode) throws Exception {
		goodsRegistService.addGoodsChangeLogUpdateByItemAppr(GoodsChangeLogVo.builder()
				.tableNm("IL_GOODS")
				.afterData(SaleStatus.WAIT.getCode())
				.columnNm(GoodsColumnComment.SALE_STATUS.getCode())
				.columnLabel(GoodsColumnComment.SALE_STATUS.getCodeName())
				.createId("0")
				.createDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"))
				.build(), ilItemCode);
		return goodsRegistService.updateGoodsSaleStatusToWaitByItemAppr(ilItemCode);
	}


	/**
	 * 품목 수정 승인시 상품의 판매 상태 원상 복귀
	 * ilItemCode : 품목 ID
	 **/
	@Override
	public int updateGoodsSaleStatusToBackByItemAppr(String ilItemCode) throws Exception {
		goodsRegistService.addGoodsChangeLogBackByItemAppr(GoodsChangeLogVo.builder()
				.tableNm("IL_GOODS")
				.beforeData(SaleStatus.WAIT.getCode())
				.columnNm(GoodsColumnComment.SALE_STATUS.getCode())
				.columnLabel(GoodsColumnComment.SALE_STATUS.getCodeName())
				.createId("0")
				.createDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"))
				.build(), ilItemCode);
		return goodsRegistService.updateGoodsSaleStatusToBackByItemAppr(ilItemCode);
	}

	//묶음상품 개별품목 고정가 할인가격 입력
	protected void updatePackageItemFixDiscountPrice(GoodsDiscountRequestDto goodsDiscountRequestDto, GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {

		log.info("goodsDiscountRequestDto : " + goodsDiscountRequestDto);

		if(goodsDiscountRequestDto != null) {
			String ilGoodsDiscountId = goodsDiscountRequestDto.getGoodsDiscountId();
			String discountMethodTypeCode = goodsDiscountRequestDto.getDiscountMethodTypeCode();
			double discountRatio = goodsDiscountRequestDto.getDiscountRatio();

			List<GoodsRegistPackageGoodsPriceDto> itemCalList = null;
			if (GoodsDiscountType.PRIORITY.getCode().equals(goodsDiscountRequestDto.getDiscountTypeCode())) {
				itemCalList = goodsRegistRequestDto.getGoodsDiscountPriorityCalcList();
			}
			else if (GoodsDiscountType.IMMEDIATE.getCode().equals(goodsDiscountRequestDto.getDiscountTypeCode())) {
				itemCalList = goodsRegistRequestDto.getGoodsDiscountImmediateCalcList();
			}
			else if (GoodsDiscountType.PACKAGE.getCode().equals(goodsDiscountRequestDto.getDiscountTypeCode())) {
				itemCalList = goodsRegistRequestDto.getGoodsPackageCalcList();
			}

			if(itemCalList != null && !itemCalList.isEmpty()) {

				for(GoodsRegistPackageGoodsPriceDto goodsPackageItemFixedDiscountPrice : itemCalList) {
					goodsPackageItemFixedDiscountPrice.setIlGoodsDiscountId(ilGoodsDiscountId);
					if(GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(discountMethodTypeCode)) {
						goodsPackageItemFixedDiscountPrice.setSalePrice(goodsPackageItemFixedDiscountPrice.getSalePricePerUnit() * goodsPackageItemFixedDiscountPrice.getGoodsQuantity());
						goodsPackageItemFixedDiscountPrice.setDiscountRatio(0);
					}
					else {
						goodsPackageItemFixedDiscountPrice.setSalePrice(0);
						goodsPackageItemFixedDiscountPrice.setSalePricePerUnit(0);
						goodsPackageItemFixedDiscountPrice.setDiscountRatio(discountRatio);
					}

					goodsRegistService.addGoodsPackageItemFixedDiscountPrice(goodsPackageItemFixedDiscountPrice);
				}
			}
		}
	}

	protected void updateGoodsImageSortUpdate(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		if(!goodsRegistRequestDto.isGoodsImageSortOrderChanged()) {
			return;
		}

		List<String> goodsImageOrderLIist = goodsRegistRequestDto.getGoodsImageOrderList();

		if(!goodsImageOrderLIist.isEmpty()) {
			int imageSortSeq = 0;
			for(String targetGoodsId : goodsImageOrderLIist) {
				//묶음상품 > 상품 이미지 > 개별상품 대표이미지 정렬 순서 UPDATE
				goodsRegistService.updateGoodsPackageGoodsMappingImageOrderUpdate(targetGoodsId, imageSortSeq, goodsRegistRequestDto.getIlGoodsId());
				imageSortSeq++;
			}
		}
	}

	protected void modifyGoodsPackageImage(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		//삭제할 파일목록 없음, 신규 업로드 파일 없음, 순서 변경 없음 이면
		if(goodsRegistRequestDto.getPackageImageNameListToDelete().isEmpty() && goodsRegistRequestDto.getPackageImageUploadResultList().isEmpty() && !goodsRegistRequestDto.isImageSortOrderChanged()) {
			return;
		}

		// 묶음 수정 화면에서 삭제 지시한 이미지 파일명 목록
		List<String> packageImageNameListToDelete = goodsRegistRequestDto.getPackageImageNameListToDelete();

		// 화면에서 전송한 상품 이미지 정렬 순서 배열 : [ "파일명", .. ] 형식의 List, List 의 index 가 정렬 순서가 됨
		// 해당 목록의 길이가 0 인 경우 해당 품목의 모든 이미지 정보 / 관련 파일 삭제로 간주함
		List<String> packageImageOrderList = goodsRegistRequestDto.getPackageImageOrderList();

		// 해당 상품코드로 등록된 기존 상품 이미지 목록 조회
		List<GoodsRegistImageVo> oldGoodsImageList = goodsRegistService.goodsImageList(goodsRegistRequestDto.getIlGoodsId());

		// 상품 이미지 정렬 순서 배열의 길이가 0 인 경우 : 모두 삭제로 간주함
		if (packageImageOrderList.isEmpty()) {
			removeItemImage(oldGoodsImageList, null, true, goodsRegistRequestDto.getImageRootStoragePath()); // 관련 품목 이미지 파일 모두 삭제
			goodsRegistService.delGoodsImage(goodsRegistRequestDto.getIlGoodsId());
			return;
		}

		// 상품 수정 화면에서 삭제 지시한 이미지 파일명 목록 존재시
		if (!packageImageNameListToDelete.isEmpty()) {
			// 삭제 대상 상품 이미지 파일들을 일괄 삭제 ( 리사이징된 파일 포함 )
			// 이미지 파일 삭제 후 oldGoodsImageList 내에서 관련 Vo 도 삭제
			removeItemImage(oldGoodsImageList, packageImageNameListToDelete, false, goodsRegistRequestDto.getImageRootStoragePath());
		}

		List<GoodsImageVo> totalPackageImageRegList = new ArrayList<>();

		// 신규 업로드 상품 이미지 존재시 : 해당 상품 이미지 등록 VO 와 해당 품목 이미지의 리사이징 파일 생성
		if (!goodsRegistRequestDto.getPackageImageUploadResultList().isEmpty()) {
			totalPackageImageRegList = generateNewGoodsImageList(goodsRegistRequestDto);
		}

		// 화면에서 전송한 상품 이미지 정렬 순서 배열 존재시 : 삭제된 이미지 제외한 기존 등록된 이미지 Data 참조하여 품목 이미지 Vo 목록 생성
		if (!packageImageOrderList.isEmpty()) {
			totalPackageImageRegList.addAll(generateReSortedGoodsImageList(goodsRegistRequestDto, oldGoodsImageList));
		}

		//원본 / 리사이징 품목 이미지 모두 삭제 후 IL_GOODS_IMG 테이블에서 해당 품목 이미지 기존 정보 삭제
		goodsRegistService.delGoodsImage(goodsRegistRequestDto.getIlGoodsId());

		// 신규 품목 이미지 VO Insert
		for (GoodsImageVo goodsImageVo : totalPackageImageRegList) {
			goodsRegistService.addGoodsImage(goodsImageVo);
		}
	}

	/**
	 * @Desc 해당 품목의 삭제 대상 품목 이미지 원본 / 리사이징 파일들을 물리적으로 삭제
	 *
	 * @param oldGoodsImageList				: IL_GOODS_IMG 테이블에서 조회된 기존 품목 이미지 목록
	 * @param packageImageNameListToDelete	: 화면에서 삭제 지시한 이미지의 물리적 파일명 목록
	 * @param isAllDeleted					: 모두 삭제 여부, 해당 flag 값이 true 인 경우 해당 품목의 관련 이미지 / 리사이징 파일 모두 삭제
	 * @param publicRootStoragePath			: 상품 상세 이미지가 저장되는 public 저장소의 최상위 저장 디렉토리 경로
	 */
	protected void removeItemImage(List<GoodsRegistImageVo> oldGoodsImageList, List<String> packageImageNameListToDelete, boolean isAllDeleted, String publicRootStoragePath) {

		//해당 상품의 삭제 대상 상품 이미지 원본 / 리사이징 파일들을 물리적으로 삭제
		for (int i = 0; i < oldGoodsImageList.size(); i++) { // 기존 상품 이미지 목록 반복문 시작

			// 모두 삭제 flag 값 false / 삭제 대상 이미지 파일명 목록에 포함되지 않은 이미지인 경우 continue / 다음 반복문 실행
			if (!isAllDeleted && packageImageNameListToDelete != null && packageImageNameListToDelete.indexOf(oldGoodsImageList.get(i).getImagePhysicalName()) < 0) {
				continue;
			}

			GoodsRegistImageVo goodsRegistImageVo = oldGoodsImageList.get(i);

			String serverSubPath = null;	// 해당 리사이징 이미지 파일의 상대 경로
			String fullFilePath = null;		// 삭제할 파일의 전체 경로 : 물리적 파일명 포함

			for (GoodsImagePrefixBySize goodsImagePrefixBySize : GoodsImagePrefixBySize.values()) { // 상품 이미지 Size / Prefix 반복문 시작

				switch (goodsImagePrefixBySize) {
					case PREFIX_640:
						serverSubPath = goodsRegistImageVo.getSize640ImagePath();
						break;
					case PREFIX_320:
						serverSubPath = goodsRegistImageVo.getSize320ImagePath();
						break;
					case PREFIX_216:
						serverSubPath = goodsRegistImageVo.getSize216ImagePath();
						break;
					case PREFIX_180:
						serverSubPath = goodsRegistImageVo.getSize180ImagePath();
						break;
					case PREFIX_75:
						serverSubPath = goodsRegistImageVo.getSize75ImagePath();
						break;
				} // 상품 이미지 Size / Prefix 반복문 끝

				// 해당 경로의 리사이징된 이미지 삭제
				try {
					fullFilePath = publicRootStoragePath + // 상품 상세 이미지가 저장되는 public 저장소의 최상위 저장 디렉토리 경로
							UriUtils.decode(serverSubPath, "UTF-8") // 리사이징 이미지의 하위 경로 : URI decoding
					;

					Files.delete(FileSystems.getDefault().getPath(fullFilePath));

				} catch (IOException e) {
					// 로직 없음 : 해당 이미지가 존재하지 않는 경우 무시함
				}
			}

			// 해당 상품 이미지의 원본 파일 삭제
			fullFilePath = publicRootStoragePath + // 상품 상세 이미지가 저장되는 public 저장소의 최상위 저장 디렉토리 경로
					UriUtils.decode(goodsRegistImageVo.getImagePath(), "UTF-8") // 원본 상품 이미지의 하위 경로 : URI decoding
			;

			try {
				Files.delete(FileSystems.getDefault().getPath(fullFilePath));
			} catch (IOException e) {
				// 로직 없음 : 해당 이미지가 존재하지 않는 경우 무시함
			}

			oldGoodsImageList.remove(i); // 해당 상품 이미지 파일 모두 삭제 후 관련 Vo 삭제
		} // 기존 상품 이미지 목록 반복문 끝
	}

	/**
	 * @Desc 마스터 품목 수정 화면에서 업로드한 신규 품목 이미지 Vo 생성
	 *
	 * @param goodsRegistRequestDto	  : 상품 request dto
	 *
	 * @param List<GoodsImageVo> : 신규 품목 이미지 Vo 목록
	 *
	 */
	protected List<GoodsImageVo> generateNewGoodsImageList(GoodsRegistRequestDto goodsRegistRequestDto) {

		// 신규 저장할 상품 이미지 Vo 목록
		List<GoodsImageVo> newGoodsRegistImageList = new ArrayList<>();

		// 화면에서 전송한 상품 이미지 정렬 순서 배열 : [ "파일명", .. ] 형식의 List, List 의 index 가 정렬 순서가 됨
		// 해당 목록의 길이가 0 인 경우 해당 상품의 모든 이미지 정보 / 관련 파일 삭제로 간주함
		List<String> packageImageOrderList = goodsRegistRequestDto.getPackageImageOrderList();

		int sort = 0;
		String basicYn = "N";

		for (UploadFileDto uploadFileDto : goodsRegistRequestDto.getPackageImageUploadResultList()) {

			// 정렬 순서 : 화면에서 보낸 정렬 순서 목록에서 업로드 정보의 원본 파일명 index 를 검색
			sort = packageImageOrderList.indexOf(uploadFileDto.getOriginalFileName());

			if (sort == -1) {
				continue;
			}

			if (sort == 0) {
				basicYn = "Y";
			}
			else {
				basicYn = "N";
			}

			// 상품 이미지 VO 생성
			GoodsImageVo newGoodsImageVo = GoodsImageVo.builder()
					.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())									// 상품ID
					.imagePath(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName())	// 물리적 파일명 ( 저장경로 포함 )
					.imageOriginalName(uploadFileDto.getOriginalFileName())								// 원본 파일명
					.basicYn(basicYn)																	// 기본 이미지
					.createId(goodsRegistRequestDto.getModifyId())										// 등록자
					.build();

			// 해당 상품 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
			resizeGoodsImage(newGoodsImageVo, uploadFileDto, goodsRegistRequestDto.getImageRootStoragePath());

			// 해당 이미지의 정렬순서 지정 : 대표 이미지인 경우 setBasicYn 내에서 0 으로 세팅, 아닌 경우 itemImageOrder 에 지정된 index 값으로 세팅
			if (!"Y".equals(newGoodsImageVo.getBasicYn())) { // 대표 이미지가 아닌 경우
				newGoodsImageVo.setSort(sort);
			}

			newGoodsRegistImageList.add(newGoodsImageVo);

		}

		return newGoodsRegistImageList;
	}

	/**
	 * @Desc 기등록된 품목 이미지 데이터와 화면에서 전송한 이미지 정렬 순서 참조하여 새로운 품목 이미지 VO 목록 생성
	 *
	 * @param GoodsRegistRequestDto :상품 request dto
	 * @param oldGoodsImageList	 : 기존 상품 이미지 Data 목록
	 *
	 * @return List<GoodsImageVo> : 신규 품목 이미지 Vo 목록
	 *
	 */
	protected List<GoodsImageVo> generateReSortedGoodsImageList(GoodsRegistRequestDto goodsRegistRequestDto, List<GoodsRegistImageVo> oldGoodsImageList) {

		// 신규 저장할 상품 이미지 VO 목록
		List<GoodsImageVo> reSortedGoodsImageRegisterList = new ArrayList<>();

		// 화면에서 전송한 상품 이미지 정렬 순서 배열 : [ "파일명", .. ] 형식의 List, List 의 index 가 정렬 순서가 됨
		// 해당 목록의 길이가 0 인 경우 해당 상품의 모든 이미지 정보 / 관련 파일 삭제로 간주함
		List<String> packageImageOrderList = goodsRegistRequestDto.getPackageImageOrderList();

		int sort = 0;
		String basicYn = "N";

		// 기존 이미지 정보 VO 데이터를 수정자 ID 로 신규 등록 : 전 단계 로직에서 삭제된 상품 이미지 관련 Vo 는 이미 제거됨
		for (GoodsRegistImageVo oldGoodsRegistImageVo : oldGoodsImageList) {

			// 정렬 순서 : 화면에서 보낸 정렬 순서 목록에서 기등록된 이미지의 물리적 파일명의 index 를 검색
			sort = packageImageOrderList.indexOf(oldGoodsRegistImageVo.getImagePhysicalName());

			if (sort == -1) {
				continue; // 화면에서 보낸 정렬 순서 목록에 없는 경우 삭제된 이미지로 간주함
			}

			if(sort == 0) {	// 기등록된 이미지의 순서가 첫 번째인 경우 수동으로 대표이미지로 지정
				basicYn = "Y";
			}

			// 상품 이미지 VO 생성
			GoodsImageVo newGoodsImageVo = GoodsImageVo.builder()
					.ilGoodsId(goodsRegistRequestDto.getIlGoodsId())					// 상품 코드
					.imagePath(oldGoodsRegistImageVo.getImagePath())					// 물리적 파일명 ( 저장경로 포함 )
					.imageOriginalName(oldGoodsRegistImageVo.getImageOriginalName())	// 원본 파일명
					.bImage(oldGoodsRegistImageVo.getSize640ImagePath())
					.mImage(oldGoodsRegistImageVo.getSize320ImagePath())
					.msImage(oldGoodsRegistImageVo.getSize216ImagePath())
					.sImage(oldGoodsRegistImageVo.getSize180ImagePath())
					.cImage(oldGoodsRegistImageVo.getSize75ImagePath())
					.basicYn(basicYn)
					.sort(sort)															// 새로운 정렬 순서 지정
					.createId(goodsRegistRequestDto.getModifyId())						// 등록자 : 수정자 ID 와 동일
					.build();

			reSortedGoodsImageRegisterList.add(newGoodsImageVo);
		}

		return reSortedGoodsImageRegisterList;
	}

	/**
	 * @Desc  배송/발주 정보 > 배송유형 > 품목별 출고처별 배송유형 리스트
	 * @param GoodsRegistItemWarehouseRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> itemWarehouseList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto) throws Exception {
		GoodsRegistItemWarehouseResponseDto goodsRegistItemWarehouseResponseDto = new GoodsRegistItemWarehouseResponseDto();

		List<GoodsRegistItemWarehouseVo> itemWarehouseList = new ArrayList<>();

		if(goodsRegistItemWarehouseRequestDto.getGoodsType() != null) {
			if(GoodsEnums.GoodsType.DAILY.getCode().equals(goodsRegistItemWarehouseRequestDto.getGoodsType())) {
				itemWarehouseList = goodsRegistService.dailyGoodsItemWarehouseList(goodsRegistItemWarehouseRequestDto);
			}
		}
		else {
			itemWarehouseList = goodsRegistService.itemWarehouseList(goodsRegistItemWarehouseRequestDto);
		}


		goodsRegistItemWarehouseResponseDto.setRows(itemWarehouseList);

		return ApiResult.success(goodsRegistItemWarehouseResponseDto);
	}

	/**
	 * @Desc  배송/발주 정보 > 배송유형 > 품목별 출고처별 배송유형 > 배송 정책 리스트
	 * @param GoodsRegistItemWarehouseRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> itemWarehouseShippingTemplateList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto) throws Exception {
		GoodsRegistItemWarehouseResponseDto goodsRegistItemWarehouseResponseDto = new GoodsRegistItemWarehouseResponseDto();

		List<GoodsRegistItemWarehouseVo> itemWarehouseShippingTemplateList = goodsRegistService.itemWarehouseShippingTemplateList(goodsRegistItemWarehouseRequestDto);
		goodsRegistItemWarehouseResponseDto.setRows(itemWarehouseShippingTemplateList);

		return ApiResult.success(goodsRegistItemWarehouseResponseDto);
	}

	/**
	 * @Desc  가격 정보 > 마스터 품목 가격정보
	 * @param String
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> itemPriceList(String ilItemCode) throws Exception{
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		List<GoodsPriceInfoResultVo> itemPriceList = goodsRegistService.itemPriceList(ilItemCode);
		goodsRegistResponseDto.setItemPriceList(itemPriceList);

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	 * @Desc  상품 내역 > 판매/전시 > 구매 허용 상태값 처리
	 * @param GoodsRegistVo
	 * @return String[]
	 */
	public String[] getPurchaseTargetTypeArray(GoodsRegistVo ilGoodsDetail) {
		//판매/전시 > 구매 허용 상태값 처리
		String[] purchaseTargetTypeArray = null;
		String purchaseTargetTypeValue = "";

		if(!GoodsEnums.GoodsType.DAILY.getCode().equals(ilGoodsDetail.getGoodsType())) {	//일일상품이 아니라면
			if("Y".equals(ilGoodsDetail.getPurchaseMemberYn()) && "Y".equals(ilGoodsDetail.getPurchaseEmployeeYn()) && "Y".equals(ilGoodsDetail.getPurchaseNonmemberYn())) {
				purchaseTargetTypeValue += "ALL,";
			}
		}
		else {
			if("Y".equals(ilGoodsDetail.getPurchaseMemberYn()) && "Y".equals(ilGoodsDetail.getPurchaseEmployeeYn())) {
				purchaseTargetTypeValue += "ALL,";
			}
		}

		if("Y".equals(ilGoodsDetail.getPurchaseMemberYn())) {
			purchaseTargetTypeValue += GoodsEnums.GoodsPurchaseTypes.MEMBER.getCode() + ",";
		}

		if("Y".equals(ilGoodsDetail.getPurchaseEmployeeYn())) {
			purchaseTargetTypeValue += GoodsEnums.GoodsPurchaseTypes.EMPLOYEE.getCode() + ",";
		}

		if(!GoodsEnums.GoodsType.DAILY.getCode().equals(ilGoodsDetail.getGoodsType()) && "Y".equals(ilGoodsDetail.getPurchaseNonmemberYn())) {	//일일상품이 아니라면
			purchaseTargetTypeValue += GoodsEnums.GoodsPurchaseTypes.NONMEMBER.getCode() + ",";		//비회원 추가
		}

		if(!"".equals(purchaseTargetTypeValue) && purchaseTargetTypeValue.length() > 0) {
			purchaseTargetTypeArray = purchaseTargetTypeValue.split(",");
		}

		return purchaseTargetTypeArray;
	}

	public String[] getGoodsDisplayTypeArray(GoodsRegistVo ilGoodsDetail) {
		String[] goodsDisplayTypeArray = null;
		String goodsDisplayTypeValue = "";

		if("Y".equals(ilGoodsDetail.getDisplayAppYn()) && "Y".equals(ilGoodsDetail.getDisplayWebMobileYn()) && "Y".equals(ilGoodsDetail.getDisplayWebPcYn())) {
			goodsDisplayTypeValue += "ALL,";
		}

		if("Y".equals(ilGoodsDetail.getDisplayAppYn())) {
			goodsDisplayTypeValue += GoodsEnums.GoodsDisplayTypes.APP.getCode() + ",";
		}

		if("Y".equals(ilGoodsDetail.getDisplayWebMobileYn())) {
			goodsDisplayTypeValue += GoodsEnums.GoodsDisplayTypes.WEB_MOBILE.getCode() + ",";
		}

		if("Y".equals(ilGoodsDetail.getDisplayWebPcYn())) {
			goodsDisplayTypeValue += GoodsEnums.GoodsDisplayTypes.WEB_PC.getCode() + ",";
		}

		if(!"".equals(goodsDisplayTypeValue) && goodsDisplayTypeValue.length() > 0) {
			goodsDisplayTypeArray = goodsDisplayTypeValue.split(",");
		}
		return goodsDisplayTypeArray;
	}

	/**
	 * @Desc  상품 내역
	 * @param GoodsRegistRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	@UserMaskingRun(system = "MUST_MASKING")
	public ApiResult<?> goodsDetail(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		//세션 정보 불러오기
		UserVo userVo = SessionUtil.getBosUserVO();
		String apprKindTp = null;
		if(userVo.getCompanyType().equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//본사라면
			apprKindTp = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode();
		}
		else if(userVo.getCompanyType().equals(CompanyEnums.CompanyType.CLIENT.getCode()) && userVo.getClientType().equals(CompanyEnums.ClientType.CLIENT.getCode())) {
			apprKindTp = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode();
		}
		goodsRegistRequestDto.setApprKindTp(apprKindTp);

		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		GoodsRegistVo ilGoodsDetail = goodsRegistService.goodsDetail(goodsRegistRequestDto);	//상품 내역

		ilGoodsDetail.setConfirmStatus("");														//기본정보 > 최근수정일(추후 상품 수정에 대한 히스토리가 생기면 변경처리 해야함)

		//판매/전시 > 구매 허용 상태값 처리
		ilGoodsDetail.setPurchaseTargetType(this.getPurchaseTargetTypeArray(ilGoodsDetail));

		//판매/전시 > 판매허용범위 (PC/Mobile) 상태값 처리
		ilGoodsDetail.setGoodsDisplayType(this.getGoodsDisplayTypeArray(ilGoodsDetail));

		goodsRegistResponseDto.setIlGoodsDetail(ilGoodsDetail);

		//판매/전시 > 승인 상태 내역
		List<GoodsApprovalResultVo> goodsApprStatusList = goodsRegistService.goodsApprStatusList(ilGoodsDetail.getIlGoodsId());
		goodsRegistResponseDto.setGoodsApprStatusList(goodsApprStatusList);

		if(!ObjectUtils.isEmpty(ilGoodsDetail)) {

			if("Y".equals(ilGoodsDetail.getGreenJuiceCleanseOptYn())) {
				ilGoodsDetail.setGreenJuiceCleanseOpt(true);
			}
			else {
				ilGoodsDetail.setGreenJuiceCleanseOpt(false);
			}

			//가격 정보 > 판매 가격정보 내역
			List<GoodsPriceInfoResultVo> goodsPrice = goodsRegistService.goodsPrice(ilGoodsDetail.getIlGoodsId(), ilGoodsDetail.getTaxYn());
			goodsRegistResponseDto.setGoodsPrice(goodsPrice);

			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(ilGoodsDetail.getGoodsType())) {	//묶음 상품이라면
				List<GoodsPackageGoodsMappingVo> goodsPackageGoodsMappingList = goodsRegistService.goodsPackageGoodsMappingList(goodsRegistRequestDto.getIlGoodsId(), "N");
				goodsPackageGoodsMappingList.forEach( gp -> {
						GoodsEnums.SaleStatus saleStatus = GoodsEnums.SaleStatus.findByCode(gp.getSaleStatusCode());
						if (!ObjectUtils.isEmpty(saleStatus)) gp.setSaleStatus(saleStatus.getCodeName());
				});
				goodsRegistResponseDto.setGoodsPackageGoodsMappingList(goodsPackageGoodsMappingList);

				//상품 이미지 > 상품별 대표 이미지 리스트를 불러온다.(IMG_SORT 순서대로)
				List<GoodsPackageGoodsMappingVo> goodsImageList = goodsRegistService.goodsPackageGoodsMappingList(goodsRegistRequestDto.getIlGoodsId(), "Y");
				goodsRegistResponseDto.setGoodsImageList(goodsImageList);

				//상품 이미지 > 묶음상품 전용 이미지 리스트
				List<GoodsRegistImageVo> goodsPackageImageList = goodsRegistService.goodsImageList(goodsRegistRequestDto.getIlGoodsId());
				goodsRegistResponseDto.setGoodsPackageImageList(goodsPackageImageList);

				//가격 정보 > 묶음상품 기본 힐인가
				List<GoodsPriceInfoResultVo> goodsPackagePriceList = goodsRegistService.goodsPackageDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.PACKAGE.getCode());
				goodsRegistResponseDto.setGoodsPackagePriceList(goodsPackagePriceList);

				//가격 정보 > 행사/할인 내역 > 우선할인
				List<GoodsPriceInfoResultVo> goodsDiscountPriorityList = goodsRegistService.goodsPackageDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.PRIORITY.getCode());
				goodsRegistResponseDto.setGoodsDiscountPriorityList(goodsDiscountPriorityList);

				//가격 정보 > 행사/할인 내역 > ERP행사
				List<GoodsPriceInfoResultVo> goodsDiscountErpEventList = goodsRegistService.goodsPackageDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.ERP_EVENT.getCode());
				goodsRegistResponseDto.setGoodsDiscountErpEventList(goodsDiscountErpEventList);

				//가격 정보 > 행사/할인 내역 > 즉시할인
				List<GoodsPriceInfoResultVo> goodsDiscountImmediateList = goodsRegistService.goodsPackageDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.IMMEDIATE.getCode());
				goodsRegistResponseDto.setGoodsDiscountImmediateList(goodsDiscountImmediateList);

				//임직원 할인 정보 > 임직원 할인 가격정보
				List<GoodsPriceInfoResultVo> goodsPackageEmployeePriceList = goodsRegistService.goodsPackageEmployeePriceList(ilGoodsDetail.getIlGoodsId());
				goodsRegistResponseDto.setGoodsPackageEmployeePriceList(goodsPackageEmployeePriceList);

				//임직원 할인 정보 > 임직원 기본할인 정보
				List<GoodsPriceInfoResultVo> goodsPackageBaseDiscountEmployeeList = goodsRegistService.goodsPackageBaseDiscountEmployeeList(ilGoodsDetail.getIlGoodsId());
				goodsRegistResponseDto.setGoodsPackageBaseDiscountEmployeeList(goodsPackageBaseDiscountEmployeeList);

				//임직원 할인 정보 > 임직원 개별할인 정보
				List<GoodsPriceInfoResultVo> goodsPackageDiscountEmployeeList = goodsRegistService.goodsPackageDiscountEmployeeList(ilGoodsDetail.getIlGoodsId());
				goodsRegistResponseDto.setGoodsPackageDiscountEmployeeList(goodsPackageDiscountEmployeeList);
			}
			else {
				//가격 정보 > 행사/할인 내역 > 우선할인
				List<GoodsPriceInfoResultVo> goodsDiscountPriorityList = goodsRegistService.goodsDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.PRIORITY.getCode());
				goodsRegistResponseDto.setGoodsDiscountPriorityList(goodsDiscountPriorityList);

				//가격 정보 > 행사/할인 내역 > ERP행사
				List<GoodsPriceInfoResultVo> goodsDiscountErpEventList = goodsRegistService.goodsDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.ERP_EVENT.getCode());
				goodsRegistResponseDto.setGoodsDiscountErpEventList(goodsDiscountErpEventList);

				//가격 정보 > 행사/할인 내역 > 즉시할인
				List<GoodsPriceInfoResultVo> goodsDiscountImmediateList = goodsRegistService.goodsDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.IMMEDIATE.getCode());
				goodsRegistResponseDto.setGoodsDiscountImmediateList(goodsDiscountImmediateList);

				//임직원 할인 정보 > 임직원 할인 가격정보
				List<GoodsPriceInfoResultVo> goodsEmployeePrice = goodsRegistService.goodsEmployeePrice(ilGoodsDetail.getIlItemCode(), ilGoodsDetail.getIlGoodsId(), ilGoodsDetail.getTaxYn());
				goodsRegistResponseDto.setGoodsEmployeePrice(goodsEmployeePrice);

				//임직원 할인 정보 > 임직원 개별할인 정보
				List<GoodsPriceInfoResultVo> goodsDiscountEmployeeList = goodsRegistService.goodsDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.EMPLOYEE.getCode());
				goodsRegistResponseDto.setGoodsDiscountEmployeeList(goodsDiscountEmployeeList);
			}

			//기본정보 > 전시 카테고리 리스트
			goodsRegistRequestDto.setMallDiv("MALL_DIV.PULMUONE");
			List<GoodsRegistCategoryVo> ilGoodsDisplayCategoryList = goodsRegistCategoryMapper.ilGoodsDisplayCategoryList(goodsRegistRequestDto);
			goodsRegistResponseDto.setIlGoodsDisplayCategoryList(ilGoodsDisplayCategoryList);

			//기본정보 > 몰인몰 카테고리 리스트
			if("2".equals(ilGoodsDetail.getUrSupplierId())) {			//공급처가 올가홀푸드이면
				goodsRegistRequestDto.setMallDiv("MALL_DIV.ORGA");
			}
			else if("5".equals(ilGoodsDetail.getUrSupplierId()) && ilGoodsDetail.getMallinmallCategoryId() != null){
				goodsRegistRequestDto.setMallDiv(ilGoodsDetail.getMallinmallCategoryId());
			}
			else {
				goodsRegistRequestDto.setMallDiv("MALL_DIV.NONE");
			}

			List<GoodsRegistCategoryVo> ilGoodsMallinmallCategoryList = goodsRegistCategoryMapper.ilGoodsDisplayCategoryList(goodsRegistRequestDto);
			goodsRegistResponseDto.setIlGoodsMallinmallCategoryList(ilGoodsMallinmallCategoryList);

			//판매 정보 > 일일 판매 옵션 설정 > 식단 주기 리스트
			List<GoodsDailyCycleBulkVo> goodsDailyCyclelist = goodsRegistService.goodsDailyCycleList(ilGoodsDetail.getIlGoodsId());
			goodsRegistResponseDto.setGoodsDailyCyclelist(goodsDailyCyclelist);

			//판매 정보 > 일일 판매 옵션 설정 > 일괄배달설정 리스트
			List<GoodsDailyCycleBulkVo> goodsDailyBulklist = goodsRegistService.goodsDailyBulkList(ilGoodsDetail.getIlGoodsId());
			goodsRegistResponseDto.setGoodsDailyBulklist(goodsDailyBulklist);

			//예약상품 옵션 설정 리스트
			List<GoodsRegistReserveOptionVo> goodsReservationOptionList = goodsRegistService.goodsReservationOptionList(goodsRegistRequestDto);
			goodsRegistResponseDto.setGoodsReservationOptionList(goodsReservationOptionList);

			//배송정책 설정 값
			goodsRegistRequestDto.setUrWarehouseId(ilGoodsDetail.getUrWarehouseId());
			List<GoodsRegistShippingTemplateVo> goodsShippingTemplateList = goodsRegistService.goodsShippingTemplateList(goodsRegistRequestDto);

			log.info("goodsShippingTemplateList : " + goodsShippingTemplateList.toString());

			goodsRegistResponseDto.setGoodsShippingTemplateList(goodsShippingTemplateList);

			//혜택/구매 정보 > 추가상품 리스트
			List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList = goodsRegistService.goodsAdditionalGoodsMappingList(goodsRegistRequestDto);
			goodsRegistResponseDto.setGoodsAdditionalGoodsMappingList(goodsAdditionalGoodsMappingList);

			//추천상품 등록 > 추천상품 리스트
			List<GoodsRegistAdditionalGoodsVo> goodsRecommendList = goodsRegistService.goodsRecommendList(goodsRegistRequestDto);
			goodsRegistResponseDto.setGoodsRecommendList(goodsRecommendList);

			if(GoodsEnums.GoodsType.NORMAL.getCode().equals(ilGoodsDetail.getGoodsType()) || GoodsEnums.GoodsType.DAILY.getCode().equals(ilGoodsDetail.getGoodsType())) {	//일반, 일일 상품만 증정행사를 불러온다.
				//혜택/구매 정보 > 증정 행사 리스트
				List<ExhibitGiftResultVo> exhibitGiftList = goodsRegistService.exhibitGiftList(goodsRegistRequestDto.getIlGoodsId());
				goodsRegistResponseDto.setExhibitGiftList(exhibitGiftList);
			}
			/* 풀무원샵 상품코드 시작 */
			goodsRegistResponseDto.setGoodsCodeList(goodsRegistService.getGoodsCodeList(goodsRegistRequestDto.getIlGoodsId()));
			/* 풀무원샵 상품코드 종료 */

			if(GoodsEnums.GoodsType.DAILY.getCode().equals(ilGoodsDetail.getGoodsType())) {	//일반, 일일 상품만 증정행사를 불러온다.
				//식단 정보 > 식단 스케쥴 리스트
				List<MealPatternListVo> mealScheduleList = goodsRegistService.getMealScheduleList(goodsRegistRequestDto.getIlGoodsId());
				goodsRegistResponseDto.setMealScheduleList(mealScheduleList);
			}
		}

			return ApiResult.success(goodsRegistResponseDto);
	}

	public GoodsRegistResponseDto goodsDatas(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		GoodsRegistVo ilGoodsDetail = goodsRegistService.goodsDetail(goodsRegistRequestDto);	//상품 내역

		ilGoodsDetail.setConfirmStatus("");														//기본정보 > 최근수정일(추후 상품 수정에 대한 히스토리가 생기면 변경처리 해야함)

		//판매/전시 > 구매 허용 상태값 처리
		ilGoodsDetail.setPurchaseTargetType(this.getPurchaseTargetTypeArray(ilGoodsDetail));

		//판매/전시 > 판매허용범위 (PC/Mobile) 상태값 처리
		ilGoodsDetail.setGoodsDisplayType(this.getGoodsDisplayTypeArray(ilGoodsDetail));

		goodsRegistResponseDto.setIlGoodsDetail(ilGoodsDetail);

		if(!ObjectUtils.isEmpty(ilGoodsDetail)) {

			//가격 정보 > 판매 가격정보 내역
			List<GoodsPriceInfoResultVo> goodsPrice = goodsRegistService.goodsPrice(ilGoodsDetail.getIlGoodsId(), ilGoodsDetail.getTaxYn());
			goodsRegistResponseDto.setGoodsPrice(goodsPrice);

			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(ilGoodsDetail.getGoodsType())) {	//묶음 상품이라면
				List<GoodsPackageGoodsMappingVo> goodsPackageGoodsMappingList = goodsRegistService.goodsPackageGoodsMappingList(goodsRegistRequestDto.getIlGoodsId(), "N");
				goodsRegistResponseDto.setGoodsPackageGoodsMappingList(goodsPackageGoodsMappingList);

				//상품 이미지 > 상품별 대표 이미지 리스트를 불러온다.(IMG_SORT 순서대로)
				List<GoodsPackageGoodsMappingVo> goodsImageList = goodsRegistService.goodsPackageGoodsMappingList(goodsRegistRequestDto.getIlGoodsId(), "Y");
				goodsRegistResponseDto.setGoodsImageList(goodsImageList);

				//상품 이미지 > 묶음상품 전용 이미지 리스트
				List<GoodsRegistImageVo> goodsPackageImageList = goodsRegistService.goodsImageList(goodsRegistRequestDto.getIlGoodsId());
				goodsRegistResponseDto.setGoodsPackageImageList(goodsPackageImageList);

				//가격 정보 > 묶음상품 기본 힐인가
				List<GoodsPriceInfoResultVo> goodsPackagePriceList = goodsRegistService.goodsPackageDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.PACKAGE.getCode());
				goodsRegistResponseDto.setGoodsPackagePriceList(goodsPackagePriceList);

				//가격 정보 > 행사/할인 내역 > 우선할인
				List<GoodsPriceInfoResultVo> goodsDiscountPriorityList = goodsRegistService.goodsPackageDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.PRIORITY.getCode());
				goodsRegistResponseDto.setGoodsDiscountPriorityList(goodsDiscountPriorityList);

				//가격 정보 > 행사/할인 내역 > ERP행사
				List<GoodsPriceInfoResultVo> goodsDiscountErpEventList = goodsRegistService.goodsPackageDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.ERP_EVENT.getCode());
				goodsRegistResponseDto.setGoodsDiscountErpEventList(goodsDiscountErpEventList);

				//가격 정보 > 행사/할인 내역 > 즉시할인
				List<GoodsPriceInfoResultVo> goodsDiscountImmediateList = goodsRegistService.goodsPackageDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.IMMEDIATE.getCode());
				goodsRegistResponseDto.setGoodsDiscountImmediateList(goodsDiscountImmediateList);

				//임직원 할인 정보 > 임직원 할인 가격정보
				List<GoodsPriceInfoResultVo> goodsPackageEmployeePriceList = goodsRegistService.goodsPackageEmployeePriceList(ilGoodsDetail.getIlGoodsId());
				goodsRegistResponseDto.setGoodsPackageEmployeePriceList(goodsPackageEmployeePriceList);

				//임직원 할인 정보 > 임직원 기본할인 정보
				List<GoodsPriceInfoResultVo> goodsPackageBaseDiscountEmployeeList = goodsRegistService.goodsPackageBaseDiscountEmployeeList(ilGoodsDetail.getIlGoodsId());
				goodsRegistResponseDto.setGoodsPackageBaseDiscountEmployeeList(goodsPackageBaseDiscountEmployeeList);

				//임직원 할인 정보 > 임직원 개별할인 정보
				List<GoodsPriceInfoResultVo> goodsPackageDiscountEmployeeList = goodsRegistService.goodsPackageDiscountEmployeeList(ilGoodsDetail.getIlGoodsId());
				goodsRegistResponseDto.setGoodsPackageDiscountEmployeeList(goodsPackageDiscountEmployeeList);
			}
			else {
				//가격 정보 > 행사/할인 내역 > 우선할인
				List<GoodsPriceInfoResultVo> goodsDiscountPriorityList = goodsRegistService.goodsDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.PRIORITY.getCode());
				goodsRegistResponseDto.setGoodsDiscountPriorityList(goodsDiscountPriorityList);

				//가격 정보 > 행사/할인 내역 > ERP행사
				List<GoodsPriceInfoResultVo> goodsDiscountErpEventList = goodsRegistService.goodsDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.ERP_EVENT.getCode());
				goodsRegistResponseDto.setGoodsDiscountErpEventList(goodsDiscountErpEventList);

				//가격 정보 > 행사/할인 내역 > 즉시할인
				List<GoodsPriceInfoResultVo> goodsDiscountImmediateList = goodsRegistService.goodsDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.IMMEDIATE.getCode());
				goodsRegistResponseDto.setGoodsDiscountImmediateList(goodsDiscountImmediateList);

				//임직원 할인 정보 > 임직원 할인 가격정보
				List<GoodsPriceInfoResultVo> goodsEmployeePrice = goodsRegistService.goodsEmployeePrice(ilGoodsDetail.getIlItemCode(), ilGoodsDetail.getIlGoodsId(), ilGoodsDetail.getTaxYn());
				goodsRegistResponseDto.setGoodsEmployeePrice(goodsEmployeePrice);

				//임직원 할인 정보 > 임직원 개별할인 정보
				List<GoodsPriceInfoResultVo> goodsDiscountEmployeeList = goodsRegistService.goodsDiscountList(ilGoodsDetail.getIlGoodsId(), GoodsDiscountType.EMPLOYEE.getCode());
				goodsRegistResponseDto.setGoodsDiscountEmployeeList(goodsDiscountEmployeeList);
			}

			//기본정보 > 전시 카테고리 리스트
			goodsRegistRequestDto.setMallDiv("MALL_DIV.PULMUONE");
			List<GoodsRegistCategoryVo> ilGoodsDisplayCategoryList = goodsRegistCategoryMapper.ilGoodsDisplayCategoryList(goodsRegistRequestDto);
			goodsRegistResponseDto.setIlGoodsDisplayCategoryList(ilGoodsDisplayCategoryList);

			//기본정보 > 몰인몰 카테고리 리스트
			if("2".equals(ilGoodsDetail.getUrSupplierId())) {			//공급처가 올가홀푸드이면
				goodsRegistRequestDto.setMallDiv("MALL_DIV.ORGA");
			}
			else if("5".equals(ilGoodsDetail.getUrSupplierId()) && ilGoodsDetail.getMallinmallCategoryId() != null){
				goodsRegistRequestDto.setMallDiv(ilGoodsDetail.getMallinmallCategoryId());
			}
			else {
				goodsRegistRequestDto.setMallDiv("MALL_DIV.NONE");
			}

			List<GoodsRegistCategoryVo> ilGoodsMallinmallCategoryList = goodsRegistCategoryMapper.ilGoodsDisplayCategoryList(goodsRegistRequestDto);
			goodsRegistResponseDto.setIlGoodsMallinmallCategoryList(ilGoodsMallinmallCategoryList);

			//판매 정보 > 일일 판매 옵션 설정 > 식단 주기 리스트
			List<GoodsDailyCycleBulkVo> goodsDailyCyclelist = goodsRegistService.goodsDailyCycleList(ilGoodsDetail.getIlGoodsId());
			goodsRegistResponseDto.setGoodsDailyCyclelist(goodsDailyCyclelist);

			//판매 정보 > 일일 판매 옵션 설정 > 일괄배달설정 리스트
			List<GoodsDailyCycleBulkVo> goodsDailyBulklist = goodsRegistService.goodsDailyBulkList(ilGoodsDetail.getIlGoodsId());
			goodsRegistResponseDto.setGoodsDailyBulklist(goodsDailyBulklist);

			//예약상품 옵션 설정 리스트
			List<GoodsRegistReserveOptionVo> goodsReservationOptionList = goodsRegistService.goodsReservationOptionList(goodsRegistRequestDto);
			goodsRegistResponseDto.setGoodsReservationOptionList(goodsReservationOptionList);

			//배송정책 설정 값
			goodsRegistRequestDto.setUrWarehouseId(ilGoodsDetail.getUrWarehouseId());
			List<GoodsRegistShippingTemplateVo> goodsShippingTemplateList = goodsRegistService.goodsShippingTemplateList(goodsRegistRequestDto);

			log.info("goodsShippingTemplateList : " + goodsShippingTemplateList.toString());

			goodsRegistResponseDto.setGoodsShippingTemplateList(goodsShippingTemplateList);

			//혜택/구매 정보 > 추가상품 리스트
			List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList = goodsRegistService.goodsAdditionalGoodsMappingList(goodsRegistRequestDto);
			goodsRegistResponseDto.setGoodsAdditionalGoodsMappingList(goodsAdditionalGoodsMappingList);

			//추천상품 등록 > 추천상품 리스트
			List<GoodsRegistAdditionalGoodsVo> goodsRecommendList = goodsRegistService.goodsRecommendList(goodsRegistRequestDto);
			goodsRegistResponseDto.setGoodsRecommendList(goodsRecommendList);

			if(GoodsEnums.GoodsType.NORMAL.getCode().equals(ilGoodsDetail.getGoodsType()) || GoodsEnums.GoodsType.DAILY.getCode().equals(ilGoodsDetail.getGoodsType())) {	//일반, 일일 상품만 증정행사를 불러온다.
				//혜택/구매 정보 > 증정 행사 리스트
				List<ExhibitGiftResultVo> exhibitGiftList = goodsRegistService.exhibitGiftList(goodsRegistRequestDto.getIlGoodsId());
				goodsRegistResponseDto.setExhibitGiftList(exhibitGiftList);
			}

			/* 풀무원샵 상품코드 시작 */
			goodsRegistResponseDto.setGoodsCodeList(goodsRegistService.getGoodsCodeList(goodsRegistRequestDto.getIlGoodsId()));
			/* 풀무원샵 상품코드 종료 */
		}

		return goodsRegistResponseDto;
	}

	/**
	 * 묶음상품 정보
	 */
	@Override
	public List<GoodsPackageGoodsMappingVo> goodsPackageGoodsMappingList(String ilGoodsId, String imageSortYn)
			throws Exception {
		return goodsRegistService.goodsPackageGoodsMappingList(ilGoodsId, imageSortYn);
	}

	/**
	 * @Desc  상품 중복 확인(품목코드, 출고처 기준)
	 * @param GoodsRegistRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> duplicateGoods(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		//세션 정보 불러오기
//		UserVo userVo = SessionUtil.getBosUserVO();
//		List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
//		String authSupplierId = userVo.getAuthSupplierId();
//
//		if(!listAuthWarehouseId.isEmpty() && listAuthWarehouseId.size() == 1 && !"".equals(listAuthWarehouseId.get(0))) {
//			goodsRegistRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
//		}
//		goodsRegistRequestDto.setAuthSupplierId(authSupplierId);

		GoodsRegistVo duplicateGoods = goodsRegistService.duplicateGoods(goodsRegistRequestDto);	//상품 내역

		if(duplicateGoods != null) {
			goodsRegistResponseDto.setIlGoodsId(duplicateGoods.getIlGoodsId());
			goodsRegistResponseDto.setGoodsType(duplicateGoods.getGoodsType());
			goodsRegistResponseDto.setSaleStatus(duplicateGoods.getSaleStatus());
			goodsRegistResponseDto.setReturnMessage(GoodsEnums.GoodsAddStatusTypes.DUPLICATE_GOODS.getMessage());
		}

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	 * @Desc  전시/몰인몰 카테고리 불러오기
	 * @param GoodsRegistCategoryRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getDisplayCategoryList(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto) throws Exception {
		GoodsRegistCategoryResponseDto goodsRegistCategoryResponseDto = new GoodsRegistCategoryResponseDto();

		String depth = StringUtil.nvl(goodsRegistCategoryRequestDto.getDepth());

		if("1".equals(depth)) {	//대분류 카테고리 일때만 해당하는 카테고리 내역을 불러온다.
			//전시 or 몰인몰 카테고리 최신(USE_YN = 'Y') 대분류 카테고리 불러오기(연도별 관리 대응)
			List<GoodsRegistCategoryVo> recentCategory1DepthIdList = goodsRegistCategoryMapper.recentCategory1DepthIdList(goodsRegistCategoryRequestDto);

			ArrayList<String> arrayList = new ArrayList<>();

			if(!recentCategory1DepthIdList.isEmpty()) {
				for(GoodsRegistCategoryVo getDisplayCategoryResultVo : recentCategory1DepthIdList) {
					arrayList.add(getDisplayCategoryResultVo.getIlCtgryId());
				}
			}
			goodsRegistCategoryRequestDto.setMasterCategoryIdArray(arrayList);
		}

		List<GoodsRegistCategoryVo> rows = goodsRegistCategoryMapper.getDisplayCategoryList(goodsRegistCategoryRequestDto);
		goodsRegistCategoryResponseDto.setRows(rows);

		return ApiResult.success(goodsRegistCategoryResponseDto);
	}

	/**
	 * @Desc  묶음 상품 > 기준상품, 묶음 상품 정보 불러오기
	 * @param GoodsRegistRequestDto
	 * @return goodsRegistResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getGoodsList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception{
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		List<GoodsRegistVo> getGoodsList = goodsRegistService.getGoodsList(goodsRegistRequestDto);	//상품 내역

		goodsRegistResponseDto.setRows(getGoodsList);

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	 * @Desc  묶음 상품 > 상품정보 제공고시, 상품 영양정보 불러오기
	 * @param GoodsRegistRequestDto
	 * @return goodsRegistResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getGoodsInfo(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception{
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		List<GoodsRegistVo> goodsInfoAnnounceList = goodsRegistService.getGoodsInfoAnnounceList(goodsRegistRequestDto);
		List<GoodsRegistVo> goodsNutritionList = goodsRegistService.getGoodsNutritionList(goodsRegistRequestDto);

		goodsRegistResponseDto.setGoodsInfoAnnounceList(goodsInfoAnnounceList);
		goodsRegistResponseDto.setGoodsNutritionList(goodsNutritionList);

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	 * @Desc  묶음 상품 > 묶음 상품 리스트 조합의 배송 불가 지역, 반품 가능 기간 산출
	 * @param GoodsRegistRequestDto
	 * @return goodsRegistResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> etcAssemble(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception{
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		List<GoodsRegistVo> getGoodsList = goodsRegistService.getGoodsList(goodsRegistRequestDto);	//상품 내역

		LinkedHashSet<String> undeliverableAreaTypearray = new LinkedHashSet<>();	//배송 불가 지역 중복 제거를 위해서 LinkedHashSet 선언
		LinkedHashSet<Integer> returnPeriodArray = new LinkedHashSet<>();
		int returnPeriod = 0;

		if(!getGoodsList.isEmpty()) {
			for(GoodsRegistVo goodsRegistVo : getGoodsList) {
				undeliverableAreaTypearray.add(goodsRegistVo.getUndeliverableAreaType());	//배송 불가 지역 코드 중복 제거

				ApiResult<?> returnPeriodList = goodsItemRegisterBiz.getReturnPeriod(goodsRegistVo.getIlCategoryStadardId(), goodsRegistVo.getStorageMethodType());	//반품 가능 기간 정보
				ItemReturnPeriodResponseDto itemReturnPeriodResponseDto = (ItemReturnPeriodResponseDto)returnPeriodList.getData();

				if(itemReturnPeriodResponseDto.getItemReturnPeriodVo().getReturnPeriod() == null) {
					returnPeriod = 0;
				}
				else {
					returnPeriod = itemReturnPeriodResponseDto.getItemReturnPeriodVo().getReturnPeriod();
				}

				returnPeriodArray.add(returnPeriod);
			}

			int undeliverableAreaTypeAssembleScore = 0;			//묶음 상품 배송 불가 지역 조합 점수
			String undeliverableAreaTypeAssembleCode = null;		//묶음 상품 배송 불가 지역 조합 코드
			String undeliverableAreaTypeAssembleCodeName = null;	//묶음 상품 배송 불가 지역 조합명

			for(String undeliverableAreaTypeScore : undeliverableAreaTypearray) {
				if(GoodsEnums.UndeliverableAreaType.NONE.getCode().equals(undeliverableAreaTypeScore)) {		//'없음' 일때 0
					undeliverableAreaTypeAssembleScore += 0;
				}
				else if(GoodsEnums.UndeliverableAreaType.A1.getCode().equals(undeliverableAreaTypeScore)) {		//'도서산간(1권역)' 일때 1
					undeliverableAreaTypeAssembleScore += 1;
				}
				else if(GoodsEnums.UndeliverableAreaType.A2.getCode().equals(undeliverableAreaTypeScore)) {		//'제주(2권역)' 일때 2
					undeliverableAreaTypeAssembleScore += 2;
				}
				else if(GoodsEnums.UndeliverableAreaType.A1_A2.getCode().equals(undeliverableAreaTypeScore)) {	//'1권역/2권역' 일때 3
					undeliverableAreaTypeAssembleScore += 3;
				}
			}

			if(undeliverableAreaTypeAssembleScore >= 3) {		//누적 값이 3 이상이면 1권역/2권역
				undeliverableAreaTypeAssembleCode = GoodsEnums.UndeliverableAreaType.A1_A2.getCode();
				undeliverableAreaTypeAssembleCodeName = GoodsEnums.UndeliverableAreaType.A1_A2.getCodeName();
			}
			else if(undeliverableAreaTypeAssembleScore == 2) {	//누적 값이 2면 제주(2권역)
				undeliverableAreaTypeAssembleCode = GoodsEnums.UndeliverableAreaType.A2.getCode();
				undeliverableAreaTypeAssembleCodeName = GoodsEnums.UndeliverableAreaType.A2.getCodeName();
			}
			else if(undeliverableAreaTypeAssembleScore == 1) {	//누적 값이 1면 도서산간(1권역)
				undeliverableAreaTypeAssembleCode = GoodsEnums.UndeliverableAreaType.A1.getCode();
				undeliverableAreaTypeAssembleCodeName = GoodsEnums.UndeliverableAreaType.A1.getCodeName();
			}
			else if(undeliverableAreaTypeAssembleScore == 0) {	//누적 값이 0면 없음
				undeliverableAreaTypeAssembleCode = GoodsEnums.UndeliverableAreaType.NONE.getCode();
				undeliverableAreaTypeAssembleCodeName = GoodsEnums.UndeliverableAreaType.NONE.getCodeName();
			}


			int assembleReturnPeriod = Collections.min(returnPeriodArray);		//묶음 상품 중에 가장 작은 반품 가능 기간을 가져온다.(0이면 불가)
			String assembleReturnPeriodValue = null;

			if(assembleReturnPeriod == 0) {
				assembleReturnPeriodValue = "불가";
			}
			else {
				assembleReturnPeriodValue = Integer.toString(assembleReturnPeriod) + "일";
			}

			goodsRegistResponseDto.setUndeliverableAreaTypeAssembleCode(undeliverableAreaTypeAssembleCode);
			goodsRegistResponseDto.setUndeliverableAreaTypeAssembleCodeName(undeliverableAreaTypeAssembleCodeName);
			goodsRegistResponseDto.setAssembleReturnPeriodValue(assembleReturnPeriodValue);
		}

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	 * @Desc  묶음 상품 > 기본할인 등록 > 가격 계산 상품 리스트
	 * @param GoodsRegistRequestDto
	 * @return goodsRegistResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> goodsPackageCalcList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception{
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		List<GoodsPackageCalcListVo> goodsPackageCalcList = goodsRegistService.goodsPackageCalcList(goodsRegistRequestDto);	//가격 계산 상품 리스트

		goodsRegistResponseDto.setGoodsPackageCalcList(goodsPackageCalcList);

		return ApiResult.success(goodsRegistResponseDto);
	}

	@Override
	public List<GoodsPackageCalcListVo> getGoodsPackageCalcList(GoodsRegistRequestDto goodsRegistRequestDto)
			throws Exception {
		return goodsRegistService.goodsPackageCalcList(goodsRegistRequestDto);	//가격 계산 상품 리스트;
	}

	/**
	 * @Desc  일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일, 출고예정일, 도착예정일 산출
	 * @param GoodsRegistRequestDto
	 * @return goodsRegistResponseDto
	 * @throws Exception
	 */
	public ApiResult<?> goodsReservationDateCalcList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception{
		List<GoodsRegistReserveOptionVo> goodsReservationDateCalcList = new ArrayList<GoodsRegistReserveOptionVo>();

		if(goodsRegistRequestDto.getPatternStandardDateArray() != null) {
			for(String patternStandardDate : goodsRegistRequestDto.getPatternStandardDateArray()) {
				goodsRegistRequestDto.setPatternStandardDate(patternStandardDate);
				GoodsRegistReserveOptionVo goodsReservationDateCalc = goodsRegistService.goodsReservationDateCalc(goodsRegistRequestDto);
				goodsReservationDateCalcList.add(goodsReservationDateCalc);
			}
		}

		return ApiResult.success(goodsReservationDateCalcList);
	}

	/**
	 * @Desc  일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일 배송패턴에 따른 요일 날짜 제한
	 * @param String
	 * @return goodsRegistResponseDto
	 * @throws Exception
	 */
	public ApiResult<?> orderIfDateLimitList(String urWarehouseId) throws Exception {
		List<GoodsRegistReserveOptionVo> orderIfDateLimitList = goodsRegistService.orderIfDateLimitList(urWarehouseId);

		String[] ifOrderDatesToDisableArray = {"MO","TU","WE","TH","FR","SA","SU"};

		final List<String> ifOrderDatesToDisableList = new ArrayList<String>();
		Collections.addAll(ifOrderDatesToDisableList, ifOrderDatesToDisableArray);

		if(orderIfDateLimitList != null && !orderIfDateLimitList.isEmpty()) {
			for(GoodsRegistReserveOptionVo orderIfDateLimit : orderIfDateLimitList) {
				ifOrderDatesToDisableList.remove(orderIfDateLimit.getWeekCode());
			}
		}

		GoodsRegistReserveOptionVo responseResult = new GoodsRegistReserveOptionVo();
		responseResult.setIfOrderDatesToDisableList(ifOrderDatesToDisableList);
		if(orderIfDateLimitList != null && !orderIfDateLimitList.isEmpty()) {
			responseResult.setCutoffTime(orderIfDateLimitList.get(0).getCutoffTime()); // 주문 마감 시간은 모두 동일하기 때문에 첫번째 값의 주문 마감 시간을 가지고 설정
		}
		else {
			responseResult.setCutoffTime("00:00:00"); // 주문수집 가능요일이 없을 경우 기본 주문 마감 시간 설정
		}

		return ApiResult.success(responseResult);
	}

	/**
	 * @Desc  품목 판매불가에 따른 상품 판매상태 판매중지 처리
	 * @param String
	 * @return List<GoodsRegistVo>
	 * @throws Exception
	 */
	public ApiResult<?> updateGoodsStopSale(String ilItemCode) throws Exception {
		String changeSaleStatus = GoodsEnums.SaleStatus.STOP_SALE.getCode();

		List<GoodsRegistVo> itemExtinctionGoodsStopSaleList = goodsRegistService.itemExtinctionGoodsStopSaleList(ilItemCode);	//품목 판매불가에 따른 상품 판매중지 대상 찾기
		List<String> ilGoodsIds = new ArrayList<String>();

		if(!itemExtinctionGoodsStopSaleList.isEmpty()) {
			for(GoodsRegistVo goodsRegistVo : itemExtinctionGoodsStopSaleList) {

				String saleStatus = goodsRegistVo.getSaleStatus();

				if(saleStatus.equals(GoodsEnums.SaleStatus.WAIT.getCode()) || saleStatus.equals(GoodsEnums.SaleStatus.ON_SALE.getCode())	//판매상태가 판매대기, 판매중, 품절(시스템), 품절(관리자)인 경우에만
						|| saleStatus.equals(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode()) || saleStatus.equals(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode())) {
					ilGoodsIds.add(goodsRegistVo.getIlGoodsId());
				}
			}

			if (!ilGoodsIds.isEmpty()) {
				goodsRegistService.updateGoodsStopSale(ilGoodsIds, changeSaleStatus);
			}
		}

		return ApiResult.success(ilGoodsIds);
	}

	/**
	 * @Desc  묶음 상품 > 해당 구성 상품으로 구성된 묶음상품 정보가 있는지 체크
	 * @param List<GoodsRegistPackageGoodsMappingDto>
	 * @return GoodsRegistVo
	 * @throws Exception
	 */
	public ApiResult<?> goodsPackageExistChk(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		// HGRM-4246 기존 묶음상품 구성 체크 로직 변경 / 2021.01.31 이명수

		int ilGoodsIdsCount= 0;
		GoodsRegistVo goodsPackageExistChk = new GoodsRegistVo();
		if(goodsRegistRequestDto.getSelectPackageGoodsList() != null && !goodsRegistRequestDto.getSelectPackageGoodsList().isEmpty()) {

			ilGoodsIdsCount = goodsRegistRequestDto.getIlGoodsIds().length;
			Map<String, Object> params = new HashMap<>();
			params.put("list", goodsRegistRequestDto.getSelectPackageGoodsList());
			params.put("ilGoodsIdsCount", ilGoodsIdsCount);
			params.put("ilGoodsId", goodsRegistRequestDto.getIlGoodsIds()[0]);
			goodsPackageExistChk = goodsRegistService.goodsPackageExistChk(params);    //ilGoodsIds의 상품으로 구성된 묶음상품이 있는지 체크
		}
/*
		GoodsRegistVo goodsPackageExistChk = new GoodsRegistVo();
		int goodsPackageQuantityExistChkSum = 0;		//수량 비교 갯수
		int ilGoodsIdsCount= 0;							//구성 상품 갯수

		if(goodsRegistRequestDto.getSelectPackageGoodsList() != null && !goodsRegistRequestDto.getSelectPackageGoodsList().isEmpty()) {

			ilGoodsIdsCount = goodsRegistRequestDto.getIlGoodsIds().length;

			goodsPackageExistChk = goodsRegistService.goodsPackageExistChk(goodsRegistRequestDto.getIlGoodsIds(), ilGoodsIdsCount);	//ilGoodsIds의 상품으로 구성된 묶음상품이 있는지 체크

			if(goodsPackageExistChk != null) {
				String ilGoodsId = goodsPackageExistChk.getIlGoodsId();												//같은 구성을 가진 묶음상품 ID

				for(GoodsRegistPackageGoodsMappingDto goodsRegistPackageGoodsMappingDto : goodsRegistRequestDto.getSelectPackageGoodsList()) {
					goodsRegistPackageGoodsMappingDto.setIlGoodsId(ilGoodsId);

					//각각의 구성품에 대한 수량 정보를 비교한다.(같은 상품 같은 수량 정보가 있으면 Count = 1을 반환 없으면 0을 반환)
					int goodsPackageQuantityExistChk = goodsRegistService.goodsPackageQuantityExistChk(goodsRegistPackageGoodsMappingDto);

					goodsPackageQuantityExistChkSum = goodsPackageQuantityExistChkSum + goodsPackageQuantityExistChk;
				}
			}
		}

		if(goodsPackageQuantityExistChkSum != ilGoodsIdsCount) {	//구성 상품 갯수와 수량 비교 갯수가 맞지 않으면 묶음상품 생성가능, 같으면 생성 불가
			goodsPackageExistChk = null;
		}
*/
		return ApiResult.success(goodsPackageExistChk);
	}

	/**
	 * @Desc  품목가격 변동에 의한 상품 가격 수정 업데이트 프로시저 호출
	 * @param String
	 * @return void
	 * @throws Exception
	 */
	public void spGoodsPriceUpdateWhenItemPriceChanges(String ilItemCode) throws Exception {
		goodsRegistService.spGoodsPriceUpdateWhenItemPriceChanges(ilItemCode);
	}

	/**
	 * @Desc  품목가격 변동에 의한 묶음상품 가격 수정 업데이트 프로시저 호출
	 * @param
	 * @return void
	 * @throws Exception
	 */
	public void spPackageGoodsPriceUpdateWhenItemPriceChanges() throws Exception {
		goodsRegistService.spPackageGoodsPriceUpdateWhenItemPriceChanges();
	}

	/**
	 * @Desc  상품등록에 의한 가격 수정 업데이트 프로시저 호출
	 * @param
	 * @return void
	 * @throws Exception
	 */
	public void spGoodsPriceUpdateWhenGoodsRegist(String ilGoodsId) throws Exception {
		goodsRegistService.spGoodsPriceUpdateWhenGoodsRegist(ilGoodsId);
	}

	/**
	 * @Desc  할인기간수정, 할인삭제에 따른 가격정보 > 판매 가격정보 새로고침 처리
	 * @param goodsRegistRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	public ApiResult<?> goodsPriceRefresh(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();

		GoodsRegistVo ilGoodsDetail = goodsRegistService.goodsDetail(goodsRegistRequestDto);	//상품 내역

		if (!ObjectUtils.isEmpty(ilGoodsDetail)) {
			//가격 정보 > 판매 가격정보 내역
			List<GoodsPriceInfoResultVo> goodsPrice = goodsRegistService.goodsPrice(ilGoodsDetail.getIlGoodsId(), ilGoodsDetail.getTaxYn());
			goodsRegistResponseDto.setGoodsPrice(goodsPrice);
		}

		return ApiResult.success(goodsRegistResponseDto);
	}

	/**
	 * 풀무원샵 상품코드 정보가 있는지 체크
	 * @param goodsRegistRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> goodsCodeExistChk(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {

		GoodsRegistResponseDto goodsRegistResponseDto = new GoodsRegistResponseDto();
		List<GoodsCodeVo> goodsCodeList = new ArrayList<>();
		if(goodsRegistRequestDto.getGoodsCodeList() != null && !goodsRegistRequestDto.getGoodsCodeList().isEmpty()) {

			goodsCodeList = goodsRegistService.goodsCodeExistChk(goodsRegistRequestDto.getGoodsCodeList());    //ilGoodsIds의 상품으로 구성된 묶음상품이 있는지 체크
		}

		System.out.println("goodsCodeList : " + goodsCodeList);
		for(GoodsCodeVo item : goodsCodeList){
			System.out.println("item.getGoodsNo() : " + item.getGoodsNo());
		}

		goodsRegistResponseDto.setGoodsCodeList(goodsCodeList);
		return ApiResult.success(goodsRegistResponseDto);
	}

	@Override
	public List<GoodsPriceInfoResultVo> getGoodsPriceList(Long ilGoodsId, String dateTime) throws Exception {
		return goodsRegistService.getGoodsPriceList(ilGoodsId, dateTime);
	}

	/**
	 * 상품 할인 저장
	 *   - 할인기간이 가능한지 Validation 체크
	 * @param goodsRegistRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> addGoodsDiscountWithValidation(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception, BaseException {

//		if (goodsRegistService.isValidStartTimeAddGoodsDiscount(goodsDiscountRequestDto) > 0) {
//			throw new BaseException(GoodsEnums.GoodsAddStatusTypes.LOCAL_DEFINE_INVALID_GOODS_DISCOUNT_START_TIME);
//		}
//		else
		if (goodsRegistService.isValidAddGoodsDiscount(goodsDiscountRequestDto) > 0) {
			throw new BaseException(GoodsEnums.GoodsAddStatusTypes.LOCAL_DEFINE_INVALID_GOODS_DISCOUNT_PERIOD);
		}
		else {
			goodsRegistService.addGoodsDiscount(goodsDiscountRequestDto);
		}
		return ApiResult.success(goodsDiscountRequestDto);
	}

}
