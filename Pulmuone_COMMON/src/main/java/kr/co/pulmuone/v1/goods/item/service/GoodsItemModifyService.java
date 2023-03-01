package kr.co.pulmuone.v1.goods.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.ItemImagePrefixBySize;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemModifyMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemRegisterMapper;
import kr.co.pulmuone.v1.comm.util.ImageUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.service.GoodsRegistBiz;
import kr.co.pulmuone.v1.goods.item.dto.*;
import kr.co.pulmuone.v1.goods.item.dto.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 수정 Service
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
public class GoodsItemModifyService {

    @Autowired
    private final GoodsItemRegisterMapper goodsItemRegisterMapper;

    @Autowired
    private final GoodsItemModifyMapper goodsItemModifyMapper;

    // 마스터 품목 등록 서비스의 resizeItemImage 메서드 호출 위해 @Autowired 로 가져옴
    @Autowired
    private final GoodsItemRegisterService goodsItemRegisterService;

	@Autowired
	private GoodsRegistBiz goodsRegistBiz;

    /*
     * 마스터 품목 정보 조회 로직 Start
     */

    /**
     * @Desc ( 마스터 품목 정보 조회 ) 해당 품목코드로 IL_ITEM 테이블의 마스터 품목 정보 조회
     *
     * @param String : 검색할 품목 코드
     *
     * @return MasterItemVo : 마스터 품목 정보 Vo
     */
    @UserMaskingRun
    protected MasterItemVo getMasterItem(String ilItemCode, String apprKindTp, String ilItemApprId) {

    	if (ilItemApprId != null && !"".equals(ilItemApprId)) { // 승인 요청 내역의 품목 상품 인증정보 리스트
    		return getMasterItemForClientAppr(ilItemCode, apprKindTp, ilItemApprId);
    	}
    	else {
    		return goodsItemModifyMapper.getMasterItem(ilItemCode, apprKindTp);
    	}
    }

    private MasterItemVo getMasterItemForClientAppr(String ilItemCode, String apprKindTp, String ilItemApprId) {
    	MasterItemVo masterItemOrig = goodsItemModifyMapper.getMasterItem(ilItemCode, apprKindTp);
    	MasterItemVo masterItemAppr = goodsItemModifyMapper.getMasterItemApprClientDetail(ilItemApprId);

    	if (masterItemOrig != null && masterItemAppr != null) {
        	// 필수 항목이 String인 경우는 isEmpty() 추가 체크
    		// goodsItemModify.putItemMasterApprInfo 쿼리와 Sync를 맞춰야함.
			if (masterItemAppr.getIlSpecMasterId() != null && !masterItemOrig.getIlSpecMasterId().equals(masterItemAppr.getIlSpecMasterId())) { // 상품정보제공고시분류 PK
    			masterItemOrig.setIlSpecMasterId(masterItemAppr.getIlSpecMasterId());
    			masterItemOrig.setChangedSpecMaster(true);
			}

			if (!StringUtil.nvl(masterItemOrig.getUrBrandId()).equals(StringUtil.nvl(masterItemAppr.getUrBrandId()))) { // 표준 브랜드
    			masterItemOrig.setUrBrandId(masterItemAppr.getUrBrandId());
    			masterItemOrig.setChangedStdBrand(true);
			}

			if (!StringUtil.nvl(masterItemOrig.getDpBrandId()).equals(StringUtil.nvl(masterItemAppr.getDpBrandId()))) { // 전시 브랜드
    			masterItemOrig.setDpBrandId(masterItemAppr.getDpBrandId());
    			masterItemOrig.setChangedDpBrand(true);
			}

			if (!StringUtil.nvl(masterItemOrig.getStorageMethodType()).equals(StringUtil.nvl(masterItemAppr.getStorageMethodType()))) { // BOS 보관방법
    			masterItemOrig.setStorageMethodType(masterItemAppr.getStorageMethodType());
    			masterItemOrig.setChangedStorageTp(true);
			}

			if (!StringUtil.nvl(masterItemOrig.getOriginType()).equals(StringUtil.nvl(masterItemAppr.getOriginType()))) { // BOS 원산지
    			masterItemOrig.setOriginType(masterItemAppr.getOriginType());
    			masterItemOrig.setChangedOrigin(true);
			}

			if (!StringUtil.nvl(masterItemOrig.getOriginDetail()).equals(StringUtil.nvl(masterItemAppr.getOriginDetail()))) { // 원산지 상세
				masterItemOrig.setOriginDetail(masterItemAppr.getOriginDetail());
    			masterItemOrig.setChangedOrigin(true);
			}

             if((masterItemOrig.getSizePerPackage() == null && masterItemAppr.getSizePerPackage() != null)
                        || (masterItemOrig.getSizePerPackage() != null  && masterItemAppr.getSizePerPackage() == null)
                            || (masterItemOrig.getSizePerPackage() != null) && masterItemAppr.getSizePerPackage() != null
                                                                        && Double.compare(masterItemOrig.getSizePerPackage(), masterItemAppr.getSizePerPackage()) != 0) { // 포장단위별 용량 (Double)
                    masterItemOrig.setSizePerPackage(masterItemAppr.getSizePerPackage());
                    masterItemOrig.setChangedSizePerPackage(true);
             }

    		if (!StringUtil.nvl(masterItemOrig.getSizeUnit()).equals(StringUtil.nvl(masterItemAppr.getSizeUnit()))) { // 용량(중량) 단위
    			masterItemOrig.setSizeUnit(masterItemAppr.getSizeUnit());
    			masterItemOrig.setChangedSizeUnit(true);
    		}

    		if( !masterItemOrig.getSizeUnitEtc().equals(masterItemAppr.getSizeUnitEtc())) { // 용량(중량) 단위 기타 - 입력값이 없을 수 있으므로 null 확인 하지 않는다.
    		    masterItemOrig.setSizeUnitEtc(masterItemAppr.getSizeUnitEtc());
                masterItemOrig.setChangedSizeUnit(true);
            }

    		if((masterItemOrig.getQuantityPerPackage() == null && masterItemAppr.getQuantityPerPackage() != null)
                        || (masterItemOrig.getQuantityPerPackage() != null  && masterItemAppr.getQuantityPerPackage() == null)
                            || (masterItemOrig.getQuantityPerPackage() != null) && masterItemAppr.getQuantityPerPackage() != null
                                                                        && Double.compare(masterItemOrig.getQuantityPerPackage(), masterItemAppr.getQuantityPerPackage()) != 0) { // 포장 구성수량(Integer) - 입력값이 없을 수 있으므로 null 확인 하지 않는다.
                    masterItemOrig.setQuantityPerPackage(masterItemAppr.getQuantityPerPackage());
                    masterItemOrig.setChangedQtyPerPackage(true);
            }

    		if (!StringUtil.nvl(masterItemOrig.getPackageUnit()).equals(StringUtil.nvl(masterItemAppr.getPackageUnit()))) { // 포장 구성단위
    			masterItemOrig.setPackageUnit(masterItemAppr.getPackageUnit());
    			masterItemOrig.setChangedPackageUnit(true);
    		}

    		if (!StringUtil.nvl(masterItemOrig.getPackageUnitEtc()).equals(StringUtil.nvl(masterItemAppr.getPackageUnitEtc()))) { // 포장 구성단위 단위 기타 - 입력값이 없을 수 있으므로 null 확인 하지 않는다.
                masterItemOrig.setPackageUnitEtc(masterItemAppr.getPackageUnitEtc());
                masterItemOrig.setChangedPackageUnit(true);
            }

    		if(masterItemOrig.isNutritionDisplayYn() != masterItemAppr.isNutritionDisplayYn()){ // 영양정보 표시여부
                masterItemOrig.setNutritionDisplayYn(masterItemAppr.isNutritionDisplayYn());
                masterItemOrig.setChangedItemNutrition(true);
                masterItemOrig.setChangedItemNutritionDisplayYn(true);
            }

    		if (!StringUtil.nvl(masterItemOrig.getNutritionDisplayDefalut()).equals(StringUtil.nvl(masterItemAppr.getNutritionDisplayDefalut()))) { // 영양정보 기본정보
                masterItemOrig.setNutritionDisplayDefalut(masterItemAppr.getNutritionDisplayDefalut());
                masterItemOrig.setChangedItemNutrition(true);
                masterItemOrig.setChangedItemNutritionDisplayYn(true);
            }

    		if (!StringUtil.nvl(masterItemOrig.getVideoUrl()).equals(StringUtil.nvl(masterItemAppr.getVideoUrl()))) {  // 동영상 url
                masterItemOrig.setVideoUrl(masterItemAppr.getVideoUrl());
                masterItemOrig.setChangedVideoInfo(true);
                masterItemOrig.setChangedVideoInfo(true);
            }

    		if(masterItemOrig.isVideoAutoplayYn() != masterItemAppr.isVideoAutoplayYn()){ // 동영상 자동재생 여부
                masterItemOrig.setVideoAutoplayYn(masterItemAppr.isVideoAutoplayYn());
                masterItemOrig.setChangedVideoInfo(true);
                masterItemOrig.setChangedVideoInfo(true);
            }

    		if (!StringUtil.nvl(masterItemOrig.getBasicDescription()).equals(StringUtil.nvl(masterItemAppr.getBasicDescription()))) {  // 상품상세 기본정보
                masterItemOrig.setBasicDescription(masterItemAppr.getBasicDescription());
                masterItemOrig.setChangedBasicDesc(true);
            }

    		if (!StringUtil.nvl(masterItemOrig.getDetaillDescription()).equals(StringUtil.nvl(masterItemAppr.getDetaillDescription()))) { // 상품상세 주요정보
                masterItemOrig.setDetaillDescription(masterItemAppr.getDetaillDescription());
                masterItemOrig.setChangedDetlDesc(true);
            }

    		if (!StringUtil.nvl(masterItemOrig.getUnitOfMeasurement()).equals(StringUtil.nvl(masterItemAppr.getUnitOfMeasurement()))) { // UOM/OMS ( 측정단위 )
                masterItemOrig.setUnitOfMeasurement(masterItemAppr.getUnitOfMeasurement());
                masterItemOrig.setChangedPiecesPerBox(true);
            }

    		if (masterItemOrig.isErpLinkIfYn()) { // ERP 연동 품목인 경우
    		}
    		else { // ERP 미연동 품목인 경우
    		    if (!StringUtil.nvl(masterItemOrig.getBosItemGroup()).equals(StringUtil.nvl(masterItemAppr.getBosItemGroup()))) {  // BOS 상품군
                    masterItemOrig.setBosItemGroup(masterItemAppr.getBosItemGroup());
                    masterItemOrig.setChangedItemGroup(true);
                }

        		if((Integer.valueOf(masterItemOrig.getDistributionPeriod()) != null) && (Integer.valueOf(masterItemAppr.getDistributionPeriod()) != null)) { // BOS 유통기간 (int)
                    if(masterItemOrig.getDistributionPeriod() != masterItemAppr.getDistributionPeriod()) {
                        masterItemOrig.setDistributionPeriod(masterItemAppr.getDistributionPeriod());
                        masterItemOrig.setChangedDistributionPeriod(true);
                    }
                }

        		if((masterItemOrig.getBoxWidth() == null && masterItemAppr.getBoxWidth() != null)
                        || (masterItemOrig.getBoxWidth() != null  && masterItemAppr.getBoxWidth() == null)
                            || (masterItemOrig.getBoxWidth() != null) && masterItemAppr.getBoxWidth() != null
                                                                        && Double.compare(masterItemOrig.getBoxWidth(), masterItemAppr.getBoxWidth()) != 0) { // 박스체적 (가로)
                    masterItemOrig.setBoxWidth(masterItemAppr.getBoxWidth());
                    masterItemOrig.setChangedBoxVolume(true);
                }

        		if((masterItemOrig.getBoxDepth() == null && masterItemAppr.getBoxDepth() != null)
                        || (masterItemOrig.getBoxDepth() != null  && masterItemAppr.getBoxDepth() == null)
                            || (masterItemOrig.getBoxDepth() != null) && masterItemAppr.getBoxDepth() != null
                                                                        && Double.compare(masterItemOrig.getBoxDepth(), masterItemAppr.getBoxDepth()) != 0) { // 박스체적 (세로)
                    masterItemOrig.setBoxDepth(masterItemAppr.getBoxDepth());
                    masterItemOrig.setChangedBoxVolume(true);
                }

        		if((masterItemOrig.getBoxHeight() == null && masterItemAppr.getBoxHeight() != null)
                        || (masterItemOrig.getBoxHeight() != null  && masterItemAppr.getBoxHeight() == null)
                            || (masterItemOrig.getBoxHeight() != null) && masterItemAppr.getBoxHeight() != null
                                                                        && Double.compare(masterItemOrig.getBoxHeight(), masterItemAppr.getBoxHeight()) != 0) { // 박스체적 (높이)
                    masterItemOrig.setBoxHeight(masterItemAppr.getBoxHeight());
                    masterItemOrig.setChangedBoxVolume(true);
                }

        		if((Integer.valueOf(masterItemOrig.getPiecesPerBox()) != null) && (Integer.valueOf(masterItemAppr.getPiecesPerBox()) != null)) { // BOS 박스 입수량 (int)
                    if(masterItemOrig.getPiecesPerBox() != masterItemAppr.getPiecesPerBox()) {
                        masterItemOrig.setPiecesPerBox(masterItemAppr.getPiecesPerBox());
                        masterItemOrig.setChangedPiecesPerBox(true);
                    }
                }

        		if (!StringUtil.nvl(masterItemOrig.getNutritionQuantityPerOnce()).equals(StringUtil.nvl(masterItemAppr.getNutritionQuantityPerOnce()))) {  // 영양분석 단위 (1회 분량)
                    masterItemOrig.setNutritionQuantityPerOnce(masterItemAppr.getNutritionQuantityPerOnce());
                    masterItemOrig.setChangedItemNutrition(true);
                    masterItemOrig.setChangedItemNutritionQuantity(true);
                }

        		if (!StringUtil.nvl(masterItemOrig.getNutritionQuantityTotal()).equals(StringUtil.nvl(masterItemAppr.getNutritionQuantityTotal()))) { // 영양분석 단위 (총분량)
                    masterItemOrig.setNutritionQuantityTotal(masterItemAppr.getNutritionQuantityPerOnce());
                    masterItemOrig.setChangedItemNutrition(true);
                    masterItemOrig.setChangedItemNutritionQuantity(true);
                }

        		if (!StringUtil.nvl(masterItemOrig.getNutritionEtc()).equals(StringUtil.nvl(masterItemAppr.getNutritionEtc()))) { // 영양성분 기타
                    masterItemOrig.setNutritionEtc(masterItemAppr.getNutritionEtc());
                    masterItemOrig.setChangedItemNutrition(true);
                    masterItemOrig.setChangedItemNutritionEtc(true);
                }

    		}
    	}

    	return masterItemOrig;
    }

    /**
	* @Desc  상품 승인 상태 항목
	* @param String
	* @return GoodsApprovalResultVo
	*/
	protected List<ItemApprovalResultVo> itemApprStatusList(String ilItemCd) {
		return goodsItemModifyMapper.itemApprStatusList(ilItemCd);
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
     * @param String : 품목 코드
     *
     * @return List<ItemPriceVo> : 해당 품목의 현재/예정 가격정보 목록
     */
    protected List<ItemPriceVo> getItemPriceSchedule(String ilItemCode) {

        // 2번째 parameter includePastPriceHistory ( 과거 가격정보 이력을 조회 결과에 포함할지 여부 ) 를 "N" ( 포함 안함 ) 으로 전달
        return goodsItemModifyMapper.getItemPriceHistory(ilItemCode);

    }

    protected List<ItemPriceVo> getItemPricePopup(String ilItemCode) {
        return goodsItemModifyMapper.getItemPricePopup(ilItemCode, "N");
    }

    protected List<ItemPriceVo> getItemPriceListByDate(String ilItemCode, String targetDate) {
        return goodsItemModifyMapper.getItemPriceListByDate(ilItemCode, targetDate);
    }

    /**
     * @Desc 해당 품목의 가격정보 전체 이력 목록 조회 : 페이지네이션 적용
     *
     * @param ItemPriceHistoryRequestDto : 가격정보 이력 조회 request dto
     *
     * @return Page<ItemPriceVo> : 해당 품목의 가격정보 전체 이력 목록
     */
    protected Page<ItemPriceVo> getItemPriceHistory(ItemPriceHistoryRequestDto itemPriceHistoryRequestDto) {
        PageMethod.startPage(itemPriceHistoryRequestDto.getPage(), itemPriceHistoryRequestDto.getPageSize());

        return goodsItemModifyMapper.getItemPricePopup(itemPriceHistoryRequestDto);
    }

    /**
     * @Desc 해당 품목의 ERP 가격 목록 조회
     *
     * @param String : 품목 코드
     *
     * @return List<ItemPriceVo> : 해당 품목의 ERP 가격 목록 조회
     */
    protected List<ItemDiscountVo> getItemErpPriceSchedule(String ilItemCode) {
        return goodsItemModifyMapper.getItemErpPriceSchedule(ilItemCode);
    }

    /**
     * @Desc 해당 품목의 Discount 가격정보 전체 이력 목록 조회 : 페이지네이션 적용
     *
     * @param ItemPriceHistoryRequestDto : 가격정보 이력 조회 request dto
     *
     * @return Page<ItemPriceVo> : 해당 품목의 Discount 가격정보 전체 이력 목록
     */
    protected Page<ItemDiscountVo> getItemDiscountPriceHistory(ItemPriceHistoryRequestDto itemPriceHistoryRequestDto) {

        PageMethod.startPage(itemPriceHistoryRequestDto.getPage(), itemPriceHistoryRequestDto.getPageSize());

        return goodsItemModifyMapper.getItemDiscountPriceHistory(itemPriceHistoryRequestDto);

    }

    /**
     * @Desc 마스터 품목 리스트 조회
     * @param MasterItemListRequestDto : 마스터 품목 리스트 검색 조건 request dto
     * @return Page<MasterItemListVo> : 마스터 품목 리스트 검색 결과 목록 ( 페이지네이션 적용 )
     */
    protected Page<ItemPoTypeVo> getPoTpDetailInfoList(MasterItemListRequestDto masterItemListRequestDto) {
        PageMethod.startPage(masterItemListRequestDto.getPage(), masterItemListRequestDto.getPageSize());
        return goodsItemModifyMapper.getPoTpDetailInfoList(masterItemListRequestDto);
    }

    /*
     * 해당 품목의 가격정보 조회 로직 End
     */

    /*
     * 마스터 품목 정보 수정 관련 로직 Start
     */

    /**
     * @Desc 해당 품목 정보 수정 전 validaion 체크
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     *
     * @throws BaseException
     */
    protected void validationCheckBeforeModifyItem(ItemModifyRequestDto itemModifyRequestDto) throws BaseException {

        // 영양정보 정렬 순서 배열의 길이와 실제 영양정보 상세 항목 목록의 길이가 다른 경우
        if (itemModifyRequestDto.getItemNutritionDetailOrderList() != null //
                && itemModifyRequestDto.getItemNutritionDetailOrderList().size() != itemModifyRequestDto.getAddItemNutritionDetailList().size()) {

            throw new BaseException(ItemEnums.Item.INVALID_ITEM_NUTRITION_DETAIL_ORDER);

        }

    }

    /**
     * @Desc 품목코드, 등록자 ID 를 각 상세 항목 정보 ( 상품 영양 정보, 출고처 .. ) 에 일괄 세팅
     *
     *       관련 항목들은 기존 정보 삭제 후 재등록 : 해당 품목 정보의 수정자 ID 를 사용함
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     *
     * @return ItemModifyRequestDto : 마스터 품목 수정 request dto
     */
    protected ItemModifyRequestDto setItemCodeInDetailInfo(ItemModifyRequestDto itemModifyRequestDto) {

        // 품목별 상품정보 제공고시 상세항목에 품목 코드, 등록자 ID 세팅
        for (ItemSpecValueRequestDto addItemSpecValueRequestDto : itemModifyRequestDto.getAddItemSpecValueList()) {
            addItemSpecValueRequestDto.setIlItemCode(itemModifyRequestDto.getIlItemCode());
            addItemSpecValueRequestDto.setCreateId(itemModifyRequestDto.getModifyId());
        }

        // 품목별 상품 영양정보 상세항목에 품목 코드, 등록자 ID 세팅
        for (ItemNutritionDetailDto addItemNutritionDetailDto : itemModifyRequestDto.getAddItemNutritionDetailList()) {
            addItemNutritionDetailDto.setIlItemCode(itemModifyRequestDto.getIlItemCode());
            addItemNutritionDetailDto.setCreateId(itemModifyRequestDto.getModifyId());
        }

        // 품목별 상품 인증정보 목록에 품목 코드, 등록자 ID 세팅
        for (ItemCertificationDto addItemCertificationDto : itemModifyRequestDto.getAddItemCertificationList()) {
            addItemCertificationDto.setIlItemCode(itemModifyRequestDto.getIlItemCode());
            addItemCertificationDto.setCreateId(itemModifyRequestDto.getModifyId());
        }

        // 품목별 출고처 목록에 품목 코드, 등록자 ID 세팅
        for (ItemWarehouseDto addItemWarehouseDto : itemModifyRequestDto.getAddItemWarehouseList()) {
            addItemWarehouseDto.setIlItemCode(itemModifyRequestDto.getIlItemCode());
            addItemWarehouseDto.setCreateId(itemModifyRequestDto.getModifyId());
        }

        return itemModifyRequestDto;

    }

    /**
     * @Desc IL_ITEM 테이블에서 해당 품목 정보 수정
     *
     * @param ItemModifyRequestDto : : 마스터 품목 수정 request dto
     *
     * @throws BaseException
     */
    protected int modifyItem(ItemModifyRequestDto itemModifyRequestDto) {

        return goodsItemModifyMapper.modifyItem(itemModifyRequestDto);

    }

    /**
     * @Desc 해당 품목의 기존 상품정보 제공고시 삭제 / 재등록
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     */
    protected void modifyItemSpecValue(ItemModifyRequestDto itemModifyRequestDto) {

        // 기존 상품정보 제공고시 삭제
        goodsItemModifyMapper.deleteItemSpecValue(itemModifyRequestDto.getIlItemCode());

        for (ItemSpecValueRequestDto addItemSpecValueRequestDto : itemModifyRequestDto.getAddItemSpecValueList()) {
            goodsItemRegisterMapper.addItemSpecValue(addItemSpecValueRequestDto);
        }

    }

    /**
     * @Desc 해당 품목의 기존 품목별 영양정보 삭제 / 재등록
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     */
    protected void modifyItemNutrition(ItemModifyRequestDto itemModifyRequestDto) {

        // 기존 영양정보 삭제
        goodsItemModifyMapper.deleteItemNutrition(itemModifyRequestDto.getIlItemCode());

        // 화면에서 전송한 영양정보 정렬 순서 배열 : [ "영양정보 분류코드", .. ] 형식의 List, List 의 index 가 정렬 순서가 됨
        List<String> itemNutritionDetailOrderList = itemModifyRequestDto.getItemNutritionDetailOrderList();

        for (ItemNutritionDetailDto itemNutritionDetailDto : itemModifyRequestDto.getAddItemNutritionDetailList()) {

            // 해당 영양정보 분류코드에 해당하는 정렬 순서 지정
            itemNutritionDetailDto.setSort(itemNutritionDetailOrderList.indexOf(itemNutritionDetailDto.getNutritionCode()));

            // 영양정보 상세항목 Insert
            goodsItemRegisterMapper.addItemNutritionDetail(itemNutritionDetailDto);

        }

    }

    /**
     * @Desc 해당 품목의 기존 품목별 인증정보 삭제 / 재등록
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     */
    protected void modifyItemCertification(ItemModifyRequestDto itemModifyRequestDto) {

        // 기존 인증정보 삭제
        goodsItemModifyMapper.deleteItemCertification(itemModifyRequestDto.getIlItemCode());

        for (ItemCertificationDto addItemCertificationDto : itemModifyRequestDto.getAddItemCertificationList()) {
            goodsItemRegisterMapper.addItemCertification(addItemCertificationDto);
        }

    }

    /**
     * @Desc 해당 품목의 기존 품목별 출고처 정보 삭제 / 재등록
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     */
    protected void modifyItemWarehouse(ItemModifyRequestDto itemModifyRequestDto) {
        // 기존 출고처 정보 삭제
//        goodsItemModifyMapper.deleteItemWarehouse(itemModifyRequestDto.getIlItemCode());

    	for (ItemWarehouseDto addItemWarehouseDto : itemModifyRequestDto.getDelItemWarehouseList()) {
        	goodsItemModifyMapper.deleteItemWarehouseId(addItemWarehouseDto.getIlItemWarehouseId());
        }

        for (ItemWarehouseDto addItemWarehouseDto : itemModifyRequestDto.getAddItemWarehouseList()) {

        	if(addItemWarehouseDto.getUpdateFlag().equals("UPDATE")) {
        		goodsItemRegisterMapper.putItemWarehouse(addItemWarehouseDto);
        	}else {
        		goodsItemRegisterMapper.addItemWarehouse(addItemWarehouseDto);
        	}
        }


    }

    /**
     * @Desc 품목별 가격 정보 신규 추가 / 해당 품목 코드의 가격 적용 시작일에 해당하는 가격정보 삭제
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     */
    protected void modifyItemPrice(ItemModifyRequestDto itemModifyRequestDto) throws Exception {

    	boolean isItemPriceChanged = false;
    	/*
         * 해당 품목 코드의 가격 적용 시작일에 해당하는 가격정보 삭제
         */
        if (!itemModifyRequestDto.getPriceApplyStartDateListToDelete().isEmpty()) { // 삭제할 가격 적용 시작일 목록 존재시

            for (String priceApplyStartDate : itemModifyRequestDto.getPriceApplyStartDateListToDelete()) {

                goodsItemModifyMapper.deleteItemPriceOrigin(itemModifyRequestDto.getIlItemCode(), priceApplyStartDate);

            }
            isItemPriceChanged = true;
        }

        /*
         * 품목별 가격정보 신규 추가
         */
        if (itemModifyRequestDto.isNewPriceYn()) { // 신규 가격정보 존재시

            ItemPriceOriginVo itemPriceOriginVo = ItemPriceOriginVo.builder() //
                    .ilItemCode(itemModifyRequestDto.getIlItemCode()) //
                    .priceApplyStartDate(itemModifyRequestDto.getPriceApplyStartDate()) //
                    .standardPrice(itemModifyRequestDto.getStandardPrice()) //
                    .recommendedPrice(itemModifyRequestDto.getRecommendedPrice()) //
                    .createId(itemModifyRequestDto.getModifyId()) //
                    .systemUpdateYn("N")    // 관리자 modify 이므로 managerUpdateYn = Y
                    .managerUpdateYn("Y")
                    .build();

            goodsItemRegisterMapper.addItemPriceOrigin(itemPriceOriginVo);
            isItemPriceChanged = true;
        }


        if (isItemPriceChanged) { // 품목가격 변동 시, 상품가격 프로시저 호출
			goodsRegistBiz.spGoodsPriceUpdateWhenItemPriceChanges(itemModifyRequestDto.getIlItemCode()); // 품목가격 업데이트 프로시저 호출
	    	goodsRegistBiz.spPackageGoodsPriceUpdateWhenItemPriceChanges(); // 묶음상품 가격 정보 업데이트 프로시저 호출
        }
    }

    /**
     * @Desc 품목별 이미지 재등록
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     */
    protected void modifyItemImage(ItemModifyRequestDto itemModifyRequestDto) {

        if (itemModifyRequestDto.getItemImageNameListToDelete().isEmpty() // 삭제할 파일 목록 없음
                && itemModifyRequestDto.getItemImageUploadResultList().isEmpty() // 신규 업로드 파일 목록 없음
                && !itemModifyRequestDto.isImageSortOrderChanged() // 정렬 순서 변경 없음
        ) {
            return; // 품목 이미지 관련 변경사항 없는 경우 return
        }

        // 마스터 품목 수정 화면에서 삭제 지시한 이미지 파일명 목록
        List<String> itemImageNameListToDelete = itemModifyRequestDto.getItemImageNameListToDelete();

        // 화면에서 전송한 상품 이미지 정렬 순서 배열 : [ "파일명", .. ] 형식의 List, List 의 index 가 정렬 순서가 됨
        // 해당 목록의 길이가 0 인 경우 해당 품목의 모든 이미지 정보 / 관련 파일 삭제로 간주함
        List<String> itemImageOrderList = itemModifyRequestDto.getItemImageOrderList();

        // 해당 품목코드로 등록된 기존 품목 이미지 목록 조회
        List<ItemImageVo> oldItemImageList = goodsItemRegisterMapper.getItemImage(itemModifyRequestDto.getIlItemCode());

        // 상품 이미지 정렬 순서 배열의 길이가 0 인 경우 : 모두 삭제로 간주함
        if (itemModifyRequestDto.getItemImageOrderList().isEmpty()) {

            removeItemImage(oldItemImageList, null, true, itemModifyRequestDto.getImageRootStoragePath()); // 관련 품목 이미지 파일 모두 삭제
            goodsItemModifyMapper.deleteItemImage(itemModifyRequestDto.getIlItemCode()); // 관련 품목 이미지 데이터 모두 삭제

            return;
        }

        // 마스터 품목 수정 화면에서 삭제 지시한 이미지 파일명 목록 존재시
        if (!itemModifyRequestDto.getItemImageNameListToDelete().isEmpty()) {

            // 삭제 대상 품목 이미지 파일들을 일괄 삭제 ( 리사이징된 파일 포함 )
            // 이미지 파일 삭제 후 oldItemImageList 내에서 관련 Vo 도 삭제
            removeItemImage(oldItemImageList, itemImageNameListToDelete, false, itemModifyRequestDto.getImageRootStoragePath());

        }

        List<ItemImageRegisterVo> totalItemImageRegisterList = new ArrayList<>(); // 기존 데이터 삭제 후 신규 저장할 품목 이미지 Vo 목록

        // 신규 업로드 품목 이미지 존재시 : 해당 품목 이미지 등록 VO 와 해당 품목 이미지의 리사이징 파일 생성
        if (!itemModifyRequestDto.getItemImageUploadResultList().isEmpty()) {

            totalItemImageRegisterList = generateNewItemImageList(itemModifyRequestDto);

        }

        // 화면에서 전송한 상품 이미지 정렬 순서 배열 존재시 : 삭제된 이미지 제외한 기존 등록된 이미지 Data 참조하여 품목 이미지 Vo 목록 생성
        if (!itemImageOrderList.isEmpty()) {

            totalItemImageRegisterList.addAll(generateReSortedItemImageList(itemModifyRequestDto, oldItemImageList));

        }

        /*
         * 원본 / 리사이징 품목 이미지 모두 삭제 후 IL_ITEM_IMG 테이블에서 해당 품목 이미지 기존 정보 삭제
         */
        goodsItemModifyMapper.deleteItemImage(itemModifyRequestDto.getIlItemCode());

        /*
         * 신규 품목 이미지 VO Insert
         */
        for (ItemImageRegisterVo itemImageRegisterVo : totalItemImageRegisterList) {

            goodsItemRegisterMapper.addItemImage(itemImageRegisterVo);

        }
    }

    protected void refactoringImage(ItemModifyRequestDto itemModifyRequestDto) {

        //---------------------
    	String publicRootStoragePath = itemModifyRequestDto.getImageRootStoragePath(); // 저장소 위치

    	// 1. 현재 저장된 이미지 정보를 가져온다.
//    	itemModifyRequestDto.setIlItemCode("BOS20200829193817");
//    	List<ItemImageVo> oldItemImageList = goodsItemRegisterMapper.getItemImage(itemModifyRequestDto.getIlItemCode());
    	List<ItemImageVo> oldItemImageList = goodsItemRegisterMapper.getRegenImageList(itemModifyRequestDto.getIlItemCode());

    	// 2. 원본 이미지를 제외한 모든 이미지를 삭제 후 새로이 생성한다.
        for (int i = 0; i < oldItemImageList.size(); i++) { // 기존 품목 이미지 목록 반복문 시작
            ItemImageVo itemImageVo = oldItemImageList.get(i);
//            if ("BOS20200829193817".equals(itemImageVo.getIlItemCode())) log.info("===== IL_ITEM_CD : " + itemImageVo.getIlItemCode());

            String serverSubPath = null; // 해당 리사이징 이미지 파일의 상대 경로
            String fullFilePath = null; // 삭제할 파일의 전체 경로 : 물리적 파일명 포함
            int size = 0;
            String prefix = "";

            for (ItemImagePrefixBySize itemImagePrefixBySize : ItemImagePrefixBySize.values()) { // 품목 이미지 Size / Prefix 반복문 시작

                switch (itemImagePrefixBySize) {

	                case PREFIX_640:
	                    serverSubPath = itemImageVo.getSize640ImagePath();
	                    size = ItemImagePrefixBySize.PREFIX_640.getImageSize();
	                    prefix = ItemImagePrefixBySize.PREFIX_640.getPrefix();
	                    break;

	                case PREFIX_320:
	                    serverSubPath = itemImageVo.getSize320ImagePath();
	                    size = ItemImagePrefixBySize.PREFIX_320.getImageSize();
	                    prefix = ItemImagePrefixBySize.PREFIX_320.getPrefix();
	                    break;

	                case PREFIX_216:
	                    serverSubPath = itemImageVo.getSize216ImagePath();
	                    size = ItemImagePrefixBySize.PREFIX_216.getImageSize();
	                    prefix = ItemImagePrefixBySize.PREFIX_216.getPrefix();
	                    break;

	                case PREFIX_180:
	                    serverSubPath = itemImageVo.getSize180ImagePath();
	                    size = ItemImagePrefixBySize.PREFIX_180.getImageSize();
	                    prefix = ItemImagePrefixBySize.PREFIX_180.getPrefix();
	                    break;

	                case PREFIX_75:
	                    serverSubPath = itemImageVo.getSize75ImagePath();
	                    size = ItemImagePrefixBySize.PREFIX_75.getImageSize();
	                    prefix = ItemImagePrefixBySize.PREFIX_75.getPrefix();
	                    break;

                } // 품목 이미지 Size / Prefix 반복문 끝

                fullFilePath = publicRootStoragePath + // 상품 상세 이미지가 저장되는 public 저장소의 최상위 저장 디렉토리 경로
                        UriUtils.decode(serverSubPath, "UTF-8") // 리사이징 이미지의 하위 경로 : URI decoding
                ;

                try {
                    Files.delete(FileSystems.getDefault().getPath(fullFilePath)); // 기존 이미지 삭제
                } catch (IOException e) {
                    // 로직 없음 : 해당 이미지가 존재하지 않는 경우 무시함
//                	log.info("===== delete error : " + prefix);
                }

                try {
                    String path= itemImageVo.getImagePath();
                	String[] parts = path.split("/");
                	String filename;
                	if(parts.length>0) {
                	    filename= parts[parts.length-1];
                    	String filePath = path.substring(0, path.lastIndexOf("/") + 1);
                        ImageUtil.createResizedImage(publicRootStoragePath + UriUtils.decode(filePath, "UTF-8"), filename, prefix, size); //이미지 새로이 생성
                	}
//                	log.info("===== success : " + prefix);
                } catch (RuntimeException e) {
	                // 로직 없음 : 해당 이미지가 존재하지 않는 경우 무시함
//                	log.info("===== create error : " + prefix);
	            }

            }

        } // 기존 품목 이미지 목록 반복문 끝
    }

    /**
     * @Desc 마스터 품목 수정 화면에서 업로드한 신규 품목 이미지 Vo 생성
     *
     * @param ItemModifyRequestDto      : 마스터 품목 수정 request dto
     *
     * @param List<ItemImageRegisterVo> : 신규 품목 이미지 Vo 목록
     *
     */
    protected List<ItemImageRegisterVo> generateNewItemImageList(ItemModifyRequestDto itemModifyRequestDto) {

        // 신규 저장할 품목 이미지 Vo 목록
        List<ItemImageRegisterVo> newItemImageRegisterList = new ArrayList<>();

        // 화면에서 전송한 상품 이미지 정렬 순서 배열 : [ "파일명", .. ] 형식의 List, List 의 index 가 정렬 순서가 됨
        // 해당 목록의 길이가 0 인 경우 해당 품목의 모든 이미지 정보 / 관련 파일 삭제로 간주함
        List<String> itemImageOrderList = itemModifyRequestDto.getItemImageOrderList();

        int sort = 0;

        for (UploadFileDto uploadFileDto : itemModifyRequestDto.getItemImageUploadResultList()) {

            // 정렬 순서 : 화면에서 보낸 정렬 순서 목록에서 업로드 정보의 원본 파일명 index 를 검색
            sort = itemImageOrderList.indexOf(uploadFileDto.getOriginalFileName());

            if (sort == -1) {
                continue;
            }

            // 품목 이미지 VO 생성
            ItemImageRegisterVo newItemImageVo = ItemImageRegisterVo.builder() //
                    .ilItemCode(itemModifyRequestDto.getIlItemCode()) // 품목 코드
                    .imagePath(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName()) // 물리적 파일명 ( 저장경로 포함 )
                    .imageOriginalName(uploadFileDto.getOriginalFileName()) // 원본 파일명
                    .createId(itemModifyRequestDto.getModifyId()) // 등록자 : 수정자 ID 와 동일
                    .build();

            // 해당 품목 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
            goodsItemRegisterService.resizeItemImage(newItemImageVo, uploadFileDto, itemModifyRequestDto.getImageRootStoragePath());

            // 해당 이미지의 원본 파일명이 대표 이미지명에 해당하는 경우 기본 이미지 여부를 true 로 세팅
            newItemImageVo.setBasicYn(itemModifyRequestDto.getRepresentativeImageName());

            // 해당 이미지의 정렬순서 지정 : 대표 이미지인 경우 setBasicYn 내에서 0 으로 세팅, 아닌 경우 itemImageOrder 에 지정된 index 값으로 세팅
            if (!newItemImageVo.isBasicYn()) { // 대표 이미지가 아닌 경우
                newItemImageVo.setSort(sort);
            }

            newItemImageRegisterList.add(newItemImageVo);

        }

        return newItemImageRegisterList;

    }

    /**
     * @Desc 기등록된 품목 이미지 데이터와 화면에서 전송한 이미지 정렬 순서 참조하여 새로운 품목 이미지 VO 목록 생성
     *
     * @param itemModifyRequestDto : 마스터 품목 수정 request dto
     * @param oldItemImageList     : 기존 품목 이미지 Data 목록
     *
     * @return List<ItemImageRegisterVo> : 신규 품목 이미지 Vo 목록
     *
     */
    protected List<ItemImageRegisterVo> generateReSortedItemImageList(ItemModifyRequestDto itemModifyRequestDto, List<ItemImageVo> oldItemImageList) {

        // 신규 저장할 품목 이미지 VO 목록
        List<ItemImageRegisterVo> reSortedItemImageRegisterList = new ArrayList<>();

        // 화면에서 전송한 상품 이미지 정렬 순서 배열 : [ "파일명", .. ] 형식의 List, List 의 index 가 정렬 순서가 됨
        // 해당 목록의 길이가 0 인 경우 해당 품목의 모든 이미지 정보 / 관련 파일 삭제로 간주함
        List<String> itemImageOrderList = itemModifyRequestDto.getItemImageOrderList();

        int sort = 0;

        // 기존 이미지 정보 VO 데이터를 수정자 ID 로 신규 등록 : 전 단계 로직에서 삭제된 품목 이미지 관련 Vo 는 이미 제거됨
        for (ItemImageVo oldItemImageVo : oldItemImageList) {

            // 정렬 순서 : 화면에서 보낸 정렬 순서 목록에서 기등록된 이미지의 물리적 파일명의 index 를 검색
            sort = itemImageOrderList.indexOf(oldItemImageVo.getImagePhysicalName());

            if (sort == -1) {
                continue; // 화면에서 보낸 정렬 순서 목록에 없는 경우 삭제된 이미지로 간주함
            }

            // 품목 이미지 VO 생성
            ItemImageRegisterVo newItemImageVo = ItemImageRegisterVo.builder() //
                    .ilItemCode(itemModifyRequestDto.getIlItemCode()) // 품목 코드
                    .imagePath(oldItemImageVo.getImagePath()) // 물리적 파일명 ( 저장경로 포함 )
                    .imageOriginalName(oldItemImageVo.getImageOriginalName()) // 원본 파일명
                    .size640imagePath(oldItemImageVo.getSize640ImagePath()) //
                    .size320imagePath(oldItemImageVo.getSize320ImagePath()) //
                    .size216imagePath(oldItemImageVo.getSize216ImagePath()) //
                    .size180imagePath(oldItemImageVo.getSize180ImagePath()) //
                    .size75imagePath(oldItemImageVo.getSize75ImagePath()) //
                    .basicYn((sort == 0)) // 기등록된 이미지의 순서가 첫 번째인 경우 수동으로 대표이미지로 지정
                    .sort(sort) // 새로운 정렬 순서 지정
                    .createId(itemModifyRequestDto.getModifyId()) // 등록자 : 수정자 ID 와 동일
                    .build();

            reSortedItemImageRegisterList.add(newItemImageVo);

        }

        return reSortedItemImageRegisterList;

    }

    /**
     * @Desc 해당 품목의 삭제 대상 품목 이미지 원본 / 리사이징 파일들을 물리적으로 삭제
     *
     * @param oldItemImageList          : IL_ITEM_IMG 테이블에서 조회된 기존 품목 이미지 목록
     * @param itemImageNameListToDelete : 화면에서 삭제 지시한 이미지의 물리적 파일명 목록
     * @param isAllDeleted              : 모두 삭제 여부, 해당 flag 값이 true 인 경우 해당 품목의 관련 이미지 / 리사이징 파일 모두 삭제
     * @param publicRootStoragePath     : 상품 상세 이미지가 저장되는 public 저장소의 최상위 저장 디렉토리 경로
     */
    protected void removeItemImage(List<ItemImageVo> oldItemImageList, List<String> itemImageNameListToDelete, boolean isAllDeleted, String publicRootStoragePath) {

        /*
         * 해당 품목의 삭제 대상 품목 이미지 원본 / 리사이징 파일들을 물리적으로 삭제
         */
        for (int i = 0; i < oldItemImageList.size(); i++) { // 기존 품목 이미지 목록 반복문 시작

            // 모두 삭제 flag 값 false / 삭제 대상 이미지 파일명 목록에 포함되지 않은 이미지인 경우 continue / 다음 반복문 실행
            if (!isAllDeleted && itemImageNameListToDelete != null && itemImageNameListToDelete.indexOf(oldItemImageList.get(i).getImagePhysicalName()) < 0) {
                continue;
            }

            ItemImageVo itemImageVo = oldItemImageList.get(i);

            String serverSubPath = null; // 해당 리사이징 이미지 파일의 상대 경로
            String fullFilePath = null; // 삭제할 파일의 전체 경로 : 물리적 파일명 포함

            for (ItemImagePrefixBySize itemImagePrefixBySize : ItemImagePrefixBySize.values()) { // 품목 이미지 Size / Prefix 반복문 시작

                switch (itemImagePrefixBySize) {

                case PREFIX_640:

                    serverSubPath = itemImageVo.getSize640ImagePath();

                    break;

                case PREFIX_320:

                    serverSubPath = itemImageVo.getSize320ImagePath();

                    break;

                case PREFIX_216:

                    serverSubPath = itemImageVo.getSize216ImagePath();

                    break;

                case PREFIX_180:

                    serverSubPath = itemImageVo.getSize180ImagePath();

                    break;

                case PREFIX_75:

                    serverSubPath = itemImageVo.getSize75ImagePath();

                    break;

                } // 품목 이미지 Size / Prefix 반복문 끝

                /*
                 * 해당 경로의 리사이징된 이미지 삭제
                 */
                try {

                    fullFilePath = publicRootStoragePath + // 상품 상세 이미지가 저장되는 public 저장소의 최상위 저장 디렉토리 경로
                            UriUtils.decode(serverSubPath, "UTF-8") // 리사이징 이미지의 하위 경로 : URI decoding
                    ;

                    Files.delete(FileSystems.getDefault().getPath(fullFilePath));

                } catch (IOException e) {
                    // 로직 없음 : 해당 이미지가 존재하지 않는 경우 무시함
                }

            }

            /*
             * 해당 품목 이미지의 원본 파일 삭제
             */
            fullFilePath = publicRootStoragePath + // 상품 상세 이미지가 저장되는 public 저장소의 최상위 저장 디렉토리 경로
                    UriUtils.decode(itemImageVo.getImagePath(), "UTF-8") // 원본 품목 이미지의 하위 경로 : URI decoding
            ;

            try {

                Files.delete(FileSystems.getDefault().getPath(fullFilePath));

            } catch (IOException e) {
                // 로직 없음 : 해당 이미지가 존재하지 않는 경우 무시함
            }

            oldItemImageList.remove(i); // 해당 품목 이미지 파일 모두 삭제 후 관련 Vo 삭제

        } // 기존 품목 이미지 목록 반복문 끝

    }
    protected ItemRegistApprVo getItemApprInfo(String ilItemApprId, String ilItemCd, String apprKindTp) {
    	return goodsItemModifyMapper.getItemApprInfo(ilItemApprId, ilItemCd, apprKindTp);
    }

    protected int putItemMasterApprInfo(ItemRegistApprVo itemRegistApprVo) {

        return goodsItemModifyMapper.putItemMasterApprInfo(itemRegistApprVo);

    }

    protected List<ItemCertificationApprVo> getItemCertApprList(String ilItemApprId, String ilItemCd) {
        return goodsItemModifyMapper.getItemCertApprList(ilItemApprId, ilItemCd);
    }

    protected void putItemCertApprInfo(String ilItemCd, List<ItemCertificationApprVo> itemCertApprList) {

        // 기존 인증정보 삭제
        goodsItemModifyMapper.deleteItemCertification(ilItemCd);

        for (ItemCertificationApprVo itemCertApprInfo : itemCertApprList) {

        	ItemCertificationDto itemCertDto = new ItemCertificationDto();
        	itemCertDto.setIlItemCode(itemCertApprInfo.getIlItemCd());
        	itemCertDto.setIlCertificationId(itemCertApprInfo.getIlCertificationId());
        	itemCertDto.setCertificationDescription(itemCertApprInfo.getCertificationDesc());
        	itemCertDto.setCreateId(Long.valueOf(itemCertApprInfo.getCreateId()));

            goodsItemRegisterMapper.addItemCertification(itemCertDto);
        }

    }

    protected List<ItemImageApprVo> getItemImageApprList(String ilItemApprId, String ilItemCd) {
        return goodsItemModifyMapper.getItemImageApprList(ilItemApprId, ilItemCd);
    }

    protected void putItemImageApprInfo(String ilItemCd, List<ItemImageApprVo> itemImageApprList) {

    	if(itemImageApprList.size() == 0) {
    		goodsItemModifyMapper.deleteItemImage(ilItemCd); // 관련 품목 이미지 데이터 모두 삭제
    	}else {
    		goodsItemModifyMapper.deleteItemImage(ilItemCd); // 관련 품목 이미지 데이터 모두 삭제

    		for (ItemImageApprVo itemImageApprInfo : itemImageApprList) {
                goodsItemRegisterMapper.addItemImageUpdate(itemImageApprInfo);
            }

    	}




    }

    protected List<ItemNutritionApprVo> getItemNutrApprList(String ilItemApprId, String ilItemCd) {
        return goodsItemModifyMapper.getItemNutrApprList(ilItemApprId, ilItemCd);
    }

    protected void putItemNutrApprInfo(String ilItemCd, List<ItemNutritionApprVo> itemNutrApprList) {
        // 기존 영양정보 삭제
        goodsItemModifyMapper.deleteItemNutrition(ilItemCd);

        for (ItemNutritionApprVo itemNutrApprInfo : itemNutrApprList) {
        	ItemNutritionDetailDto itemNutrInfo = new ItemNutritionDetailDto();

        	itemNutrInfo.setIlItemCode(ilItemCd);
        	itemNutrInfo.setErpNutritionQuantity(itemNutrApprInfo.getErpNutritionQuantity());
        	itemNutrInfo.setErpNutritionPercent(itemNutrApprInfo.getErpNutritionPercent());
        	itemNutrInfo.setNutritionCode(itemNutrApprInfo.getNutritionCode());
        	itemNutrInfo.setNutritionPercent(itemNutrApprInfo.getNutritionPercent());
        	itemNutrInfo.setNutritionQuantity(itemNutrApprInfo.getNutritionQuantity());
        	itemNutrInfo.setSort(itemNutrApprInfo.getSort());
        	itemNutrInfo.setCreateId(Long.valueOf(itemNutrApprInfo.getCreateId()));

            // 영양정보 상세항목 Insert
            goodsItemRegisterMapper.addItemNutritionDetail(itemNutrInfo);

        }

    }

    protected List<ItemSpecApprVo> getItemSpecApprList(String ilItemApprId, String ilItemCd) {
        return goodsItemModifyMapper.getItemSpecApprList(ilItemApprId, ilItemCd);
    }

    protected void putItemSpecApprInfo(String ilItemCd, List<ItemSpecApprVo> itemSpecApprList) {
        // 기존 상품정보 제공고시 삭제
        goodsItemModifyMapper.deleteItemSpecValue(ilItemCd);

        for (ItemSpecApprVo itemSpecApprInfo : itemSpecApprList) {

        	ItemSpecValueRequestDto itemSpecInfo = new ItemSpecValueRequestDto();
        	itemSpecInfo.setIlItemCode(itemSpecApprInfo.getIlItemCode());
        	itemSpecInfo.setIlSpecFieldId(itemSpecApprInfo.getIlSpecFieldId());
        	itemSpecInfo.setSpecFieldValue(itemSpecApprInfo.getSpecFieldValue());
        	itemSpecInfo.setDirectYn(itemSpecApprInfo.getDirectYn());
        	itemSpecInfo.setCreateId(itemSpecApprInfo.getCreateId());

            goodsItemRegisterMapper.addItemSpecValue(itemSpecInfo);
        }
    }

    /*
     * 마스터 품목 정보 수정 관련 로직 End
     */

    /**
     * 가격 오리진 삭제
     * @param ilItemCode String
     * @param priceApplyStartDate String
     * @return int
     */
    protected int deleteItemPriceOrigin(String ilItemCode, String priceApplyStartDate) {
        return goodsItemModifyMapper.deleteItemPriceOrigin(ilItemCode, priceApplyStartDate);
    }

}
