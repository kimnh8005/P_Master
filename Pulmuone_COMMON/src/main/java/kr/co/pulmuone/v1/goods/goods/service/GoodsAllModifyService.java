package kr.co.pulmuone.v1.goods.goods.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsRegistMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.goods.dto.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsAllModifyMapper;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsAllModifyRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistCategoryRequestDto;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* 상품일괄수정 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 01. 08.             정형진          최초작성
* =======================================================================
* </PRE>
*/

@Service
@RequiredArgsConstructor
public class GoodsAllModifyService {

	@Autowired
    private final GoodsAllModifyMapper goodsAllModifyMapper;

	@Autowired
	private final GoodsRegistMapper goodsRegistMapper;

	/**
	* @Desc 상품일괄수정 추가한 상품 목록 정보
	* @param GoodsRegistRequestDto
	* @return GoodsRegistVo
	*/
	protected List<GoodsAllModifyVo> getGoodsAllModifyList(GoodsAllModifyRequestDto paramDto) {
		return goodsAllModifyMapper.getGoodsAllModifyList(paramDto);
	}

	/**
     * @Desc 프로모션 수정
     *
     * @param GoodsAllModifyRequestDto : 마스터 품목 수정 request dto
     *
     * @throws BaseException
     */
    protected int putPromotionInfoModify(GoodsAllModifyRequestDto paramDto) {

    	int result = 0;
    	for(GoodsAllModifyVo goodsAllModifyVo : paramDto.getGoodsGridList()) {
    		paramDto.setIlItemCd(goodsAllModifyVo.getIlItemCd());
			paramDto.setIlGoodsId(goodsAllModifyVo.getIlGoodsId());

			GoodsRegistVo before = goodsAllModifyMapper.getGoodsInfoByGoodsId(goodsAllModifyVo.getIlGoodsId());

			result = goodsAllModifyMapper.putPromotionInfoModify(paramDto);

			// change log
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getPromotionName(), paramDto.getPromotionNm(), GoodsEnums.GoodsColumnComment.PROMOTION_NM);
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getPromotionNameStartDate(), paramDto.getPromotionNameStartDate(), GoodsEnums.GoodsColumnComment.PROMOTION_NM_START_DT);
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getPromotionNameEndDate(), paramDto.getPromotionNameEndDate(), GoodsEnums.GoodsColumnComment.PROMOTION_NM_END_DT);

    	}
        return result;

    }

	/**
     * @Desc 전시 카테고리 일괄수정
     *
     * @param GoodsRegistCategoryRequestDto
     *
     * @throws BaseException
     */
    protected ApiResult<?> putDispCategoryInfoBulkModify(GoodsAllModifyRequestDto paramDto) {

    	// [S] 1.일괄수정 요청 카테고리에 기본 설정이 있는지 확인
		int basicCnt = 0;
		for (GoodsRegistCategoryRequestDto categoryList : paramDto.getDisplayCategoryList()) {
			if ("Y".equals(categoryList.getBasicYn())) {
				basicCnt++;
			}
    	}

		if (basicCnt > 1) { // 일괄수정 요청 카테고리에 기본 설정 개수가 2 이상이면 실패처리
			return ApiResult.fail();
		}
		else if (basicCnt == 1) { // 일괄수정 요청 카테고리에 기본 설정이 있으면 기존 카테고리 데이터의 기본 설정 속성을 모두 N으로 초기화 - 일괄수정 요청 카테고리의 기본 설정값으로 설정하기 위함
			for (GoodsAllModifyVo goodsAllModifyVo : paramDto.getGoodsGridList()) {
				goodsAllModifyMapper.putResetBasicYnOfDispCategory(goodsAllModifyVo.getIlGoodsId());
			}
		}
    	// [E] 1.일괄수정 요청 카테고리에 기본 설정이 있는지 확인

    	// [S] 2. INSERT / UPDATE 실행
		for (GoodsAllModifyVo goodsAllModifyVo : paramDto.getGoodsGridList()) {
			String basicIlCtgryId = goodsAllModifyMapper.selectBasicDispCategory(goodsAllModifyVo.getIlGoodsId()); // 기존 설정의 기본 카테고리 정보 획득
    		paramDto.setIlItemCd(goodsAllModifyVo.getIlItemCd());
			paramDto.setIlGoodsId(goodsAllModifyVo.getIlGoodsId());
			for (GoodsRegistCategoryRequestDto categoryList : paramDto.getDisplayCategoryList()) {
				// 기존 설정의 기본 카테고리가 일괄 수정 요청 카테고리에 포함되어 있으면 해당 요청 카테고리의 기본 설정을 Y로 변경 - 기존에 설정된 기본 카테고리가 N으로 업데이트 됨을 방지
				if (basicIlCtgryId != null && basicIlCtgryId.equals(categoryList.getIlCtgryId())) {
					categoryList.setBasicYn("Y");
				}

				categoryList.setIlGoodsId(paramDto.getIlGoodsId());
				categoryList.setCreateId(paramDto.getUserVo().getUserId());
				goodsAllModifyMapper.putCategoryInfoModify(categoryList);
			}
		}
    	// [E] 2. INSERT / UPDATE 실행

		return ApiResult.success();
    }

	/**
     * @Desc 구매허용범위/쿠폰 수정
     *
     * @param GoodsAllModifyRequestDto
     *
     * @throws BaseException
     */
    protected int putPurchasModify(GoodsAllModifyRequestDto paramDto) {
    	int result = 0;
    	for(GoodsAllModifyVo goodsAllModifyVo : paramDto.getGoodsGridList()) {
    		paramDto.setIlItemCd(goodsAllModifyVo.getIlItemCd());
			paramDto.setIlGoodsId(goodsAllModifyVo.getIlGoodsId());

			paramDto.setDisplayWebPcYn("N");
			paramDto.setDisplayWebMobilePcYn("N");
			paramDto.setDisplayAppYn("N");
    		for(String purchaseTargetType : paramDto.getPurchaseTargetType()) {
				if(GoodsEnums.GoodsDisplayTypes.WEB_PC.getCode().equals(purchaseTargetType)) {		//WEB PC 전시여부 값 세팅
					paramDto.setDisplayWebPcYn("Y");
				}
				if(GoodsEnums.GoodsDisplayTypes.WEB_MOBILE.getCode().equals(purchaseTargetType)) {	//WEB MOBILE 전시여부 세팅
					paramDto.setDisplayWebMobilePcYn("Y");
				}
				if(GoodsEnums.GoodsDisplayTypes.APP.getCode().equals(purchaseTargetType)) {			//APP 전시여부 세팅
					paramDto.setDisplayAppYn("Y");
				}
			}

			GoodsRegistVo before = goodsAllModifyMapper.getGoodsInfoByGoodsId(goodsAllModifyVo.getIlGoodsId());
    		result = goodsAllModifyMapper.putPurchasModify(paramDto);


    		// change log
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getDisplayWebPcYn(), paramDto.getDisplayWebPcYn(), GoodsEnums.GoodsColumnComment.DISP_WEB_PC_YN);
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getDisplayWebMobileYn(), paramDto.getDisplayWebMobilePcYn(), GoodsEnums.GoodsColumnComment.DISP_WEB_MOBILE_YN);
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getDisplayAppYn(), paramDto.getDisplayAppYn(), GoodsEnums.GoodsColumnComment.DISP_APP_YN);
    	}
        return result;
    }

    /**
     * @Desc 상품 공지 정보 수정
     *
     * @param GoodsAllModifyRequestDto
     *
     * @throws BaseException
     */
    protected int putNoticeGoodsModify(GoodsAllModifyRequestDto paramDto) {
    	int result = 0;
    	for(GoodsAllModifyVo goodsAllModifyVo : paramDto.getGoodsGridList()) {
    		paramDto.setIlItemCd(goodsAllModifyVo.getIlItemCd());
			paramDto.setIlGoodsId(goodsAllModifyVo.getIlGoodsId());

			// 상세 하단 공지1 첨부 이미지 URL
			if(paramDto.getNoticeBelow1ImageUploadResultList() != null) {
				int noticeBelow1ImageCnt = paramDto.getNoticeBelow1ImageUploadResultList().size();

				if(noticeBelow1ImageCnt > 0) {
					for (UploadFileDto uploadFileDto : paramDto.getNoticeBelow1ImageUploadResultList()) {
						paramDto.setNoticeBelow1ImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
					}
				}
			}

			//상세 하단 공지2 첨부 이미지 URL
			if(paramDto.getNoticeBelow1ImageUploadResultList() != null) {
				int noticeBelow2ImageCnt = paramDto.getNoticeBelow2ImageUploadResultList().size();

				if(noticeBelow2ImageCnt > 0) {
					for (UploadFileDto uploadFileDto : paramDto.getNoticeBelow2ImageUploadResultList()) {
						paramDto.setNoticeBelow2ImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
					}
				}
			}

			GoodsRegistVo before = goodsAllModifyMapper.getGoodsInfoByGoodsId(goodsAllModifyVo.getIlGoodsId());
			result = goodsAllModifyMapper.putNoticeGoodsModify(paramDto);

			// change log
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getNoticeBelow1ImageUrl(), paramDto.getNoticeBelow1ImageUrl(), GoodsEnums.GoodsColumnComment.NOTICE_BELOW_1_IMG_URL);
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getNoticeBelow1StartDate(), paramDto.getNoticeBelow1StartDate(), GoodsEnums.GoodsColumnComment.NOTICE_BELOW_1_START_DT);
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getNoticeBelow1EndDate(), paramDto.getNoticeBelow1EndDate(), GoodsEnums.GoodsColumnComment.NOTICE_BELOW_1_END_DT);
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getNoticeBelow2ImageUrl(), paramDto.getNoticeBelow2ImageUrl(), GoodsEnums.GoodsColumnComment.NOTICE_BELOW_2_IMG_URL);
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getNoticeBelow2StartDate(), paramDto.getNoticeBelow2StartDate(), GoodsEnums.GoodsColumnComment.NOTICE_BELOW_2_START_DT);
			setGoodsChangeLogVo(goodsAllModifyVo.getIlGoodsId(), before.getNoticeBelow2EndDate(), paramDto.getNoticeBelow2EndDate(), GoodsEnums.GoodsColumnComment.NOTICE_BELOW_2_END_DT);

    	}
        return result;
    }

    /**
     * @Desc 상품 추가 상품 수정
     *
     * @param GoodsAllModifyRequestDto
     *
     * @throws BaseException
     */
    protected int putGoodsAddModify(GoodsAllModifyRequestDto paramDto) {
    	int result = 0;
    	for(GoodsAllModifyVo goodsAllModifyVo : paramDto.getGoodsGridList()) {
    		paramDto.setIlItemCd(goodsAllModifyVo.getIlItemCd());
			paramDto.setIlGoodsId(goodsAllModifyVo.getIlGoodsId());

			for(GoodsSearchVo goodsAdd : paramDto.getGoodsAdditionList()) {
				paramDto.setTargetGoodsId(String.valueOf(goodsAdd.getGoodsId()));
				paramDto.setSalePrice(goodsAdd.getSalePrice());

				int goodsAddCnt = goodsAllModifyMapper.getGoodsAdditionCount(paramDto);

				if(goodsAddCnt > 0) {
					result = goodsAllModifyMapper.putGoodsAddModify(paramDto);
				}else {
					result = goodsAllModifyMapper.createGoodsAddModify(paramDto);
				}


			}


    	}
        return result;
    }

    /**
     * @Desc 상품 추가 상품 삭제
     *
     * @param GoodsAllModifyRequestDto
     *
     * @throws BaseException
     */
    protected int delGoodsAddModify(GoodsAllModifyRequestDto paramDto) {
    	int result = 0;
    	for(GoodsAllModifyVo goodsAllModifyVo : paramDto.getGoodsGridList()) {
    		paramDto.setIlItemCd(goodsAllModifyVo.getIlItemCd());
			paramDto.setIlGoodsId(goodsAllModifyVo.getIlGoodsId());

			for(GoodsSearchVo goodsAdd : paramDto.getGoodsAdditionList()) {
				paramDto.setTargetGoodsId(String.valueOf(goodsAdd.getGoodsId()));
				paramDto.setSalePrice(goodsAdd.getSalePrice());

				result = goodsAllModifyMapper.delGoodsAddModify(paramDto);

			}


    	}
        return result;
    }

    /**
	* @Desc 추가상품 조회
	* @param GoodsAllModifyRequestDto
	* @return List<GoodsRegistAdditionalGoodsVo>
	*/
	protected List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList(GoodsAllModifyRequestDto paramDto) {
		return goodsAllModifyMapper.goodsAdditionalGoodsMappingList(paramDto);
	}

	/**
	* @Desc 상품 공지 정보
	* @param GoodsAllModifyRequestDto
	* @return GoodsRegistVo
	*/
	protected GoodsRegistVo getGoodsNoticeInfo(GoodsAllModifyRequestDto paramDto) {
		return goodsAllModifyMapper.getGoodsNoticeInfo(paramDto);
	}

	/**
     * @Desc 상품정보 일괄수정 - 엑셀
     * @param goodsListRequestDto
     * @return Page<GoodsVo>
     */
    protected List<GoodsAllModifyVo> getGoodsAllModifyListExcel(GoodsAllModifyRequestDto paramDto) {
        return goodsAllModifyMapper.getGoodsAllModifyListExcel(paramDto);
    }



	/**
	 * 상품 변경내역
	 * @param goodsId
	 * @param beforeData
	 * @param afterData
	 * @param goodsColumnComment
	 */
	protected void setGoodsChangeLogVo(String goodsId, String beforeData, String afterData, GoodsEnums.GoodsColumnComment goodsColumnComment) {
		if(afterData == null) return;
		if(!afterData.equals(beforeData)) {
			goodsRegistMapper.addGoodsChangeLog(GoodsChangeLogVo.builder()
					.ilGoodsId(goodsId)
					.tableIdNew(goodsId)
					.tableIdOrig(goodsId)
					.beforeData(beforeData)
					.afterData(afterData)
					.columnNm(goodsColumnComment.getCode())
					.columnLabel(goodsColumnComment.getCodeName())
					.tableNm("IL_GOODS")
					.createId("1")
					.createDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"))
					.build()
			);
		}
	}
}
