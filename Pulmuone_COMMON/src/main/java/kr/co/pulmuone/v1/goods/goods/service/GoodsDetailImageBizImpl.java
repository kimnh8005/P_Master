package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsDetailImageVo;
import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistApprVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemModifyRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemNutritionDetailDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecValueRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemNutritionDetailVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecValueVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GoodsDetailImageBizImpl implements GoodsDetailImageBiz{

    @Autowired
    GoodsDetailImageService goodsDetailImageService;


    /**
     * 상품 상세 이미지 다운로드 리스트 조회
     * @param goodsDetailImageListRequestDto
     * @return
     */
    @Override
    public ApiResult<?> getGoodsDetailImageList(GoodsDetailImageListRequestDto goodsDetailImageListRequestDto) {
        return ApiResult.success(goodsDetailImageService.getGoodsDetailImageList(goodsDetailImageListRequestDto));
    }

    @Override
    public ExcelDownloadDto getGoodsDetailImageListExportExcel(GoodsDetailImageListRequestDto goodsDetailImageListRequestDto) {
        String excelFileName = "상품 상세 이미지 다운로드 내역" + "_" + DateUtil.getCurrentDate(); // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        ArrayList<String> ilItemCdArray = new ArrayList<String>();
        String codeStrFlag = "Y";

        if (!StringUtil.isEmpty(goodsDetailImageListRequestDto.getFindKeyword())) {
            // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = goodsDetailImageListRequestDto.getFindKeyword().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");

            String regExp = "^[0-9]+$";
            String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
            for(int i = 0; i < ilItemCodeListArray.length; i++) {
                String ilItemCodeSearchVal = ilItemCodeListArray[i];
                if(ilItemCodeSearchVal.isEmpty()) {
                    continue;
                }
                ilItemCdArray.add(ilItemCodeSearchVal);
            }
        }

        goodsDetailImageListRequestDto.setFindKeywordList(ilItemCdArray); // 검색어
        goodsDetailImageListRequestDto.setFindKeywordStrFlag(codeStrFlag);

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                300, 300, 600, 200, 400, 300, 400
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //GOODS_UPDATE_STATUS_EXCEL
                "modifyDate", "goodsId", "itemCodeExcel", "goodsTypeName", "goodsName", "chargeNameExcel", "goodsUpdateStatusExcel"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "업데이트 일자", "상품코드", "마스터 품목코드/품목바코드", "상품유형", "상품명", "수정 담당자", "업데이트 항목"
        };

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        goodsDetailImageListRequestDto.setExcelYn("Y");
        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        GoodsDetailImageListResponseDto goodsDetailImageList = goodsDetailImageService.getGoodsDetailImageList(goodsDetailImageListRequestDto);

        firstWorkSheetDto.setExcelDataList(goodsDetailImageList.getRows());

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }


    /**
     * @Desc 업데이트 상품정보 조회 및 이미지 정보 등록
     * @param specificsFieldRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putUpdateGoodsIdInfoForDetailImage(SpecificsFieldRequestDto specificsFieldRequestDto) throws Exception{

        GoodsDetailImageListResponseDto goodsDetailImageListResponseDto = new GoodsDetailImageListResponseDto();

        if(specificsFieldRequestDto.getSpecificsFieldId() != null){
            List<GoodsDetailImageVo> goodsDetailImageVoList = goodsDetailImageService.getSpecGoodsIdList(specificsFieldRequestDto);
            goodsDetailImageListResponseDto.setGoodsDetailImageVoList(goodsDetailImageVoList);
        }

        if(specificsFieldRequestDto.getSpecificsMasterId() != null){
            List<GoodsDetailImageVo> goodsDetailImageVoList = goodsDetailImageService.getDetailGoodsIdList(specificsFieldRequestDto);
            goodsDetailImageListResponseDto.setGoodsDetailImageVoList(goodsDetailImageVoList);
        }

        List<String> goodsIdList = new ArrayList<>();
        for( GoodsDetailImageVo goodsDetailImageVo : goodsDetailImageListResponseDto.getGoodsDetailImageVoList() ){
            goodsIdList.add(goodsDetailImageVo.getIlGoodsId().toString());
        }

        // 상품고시정보 등록
        addUpdateGoodsIdInfoForDetailImage(goodsIdList);

        return ApiResult.success();
    }


    /**
     * @Desc 상품 상세 이미지 관리 등록
     * @param goodsIdList
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addUpdateGoodsIdInfoForDetailImage(List<String> goodsIdList) throws Exception{

        // 상품상세 이미지 관리 테이블 Insert : 업데이트 된 상품고시정보 등록
        for( String goodsId : goodsIdList){
            GoodsDetailImageVo goodsDetailImageVo = new GoodsDetailImageVo();
            goodsDetailImageVo.setIlGoodsId(Long.parseLong(goodsId));
            boolean chkImgGenYn = goodsDetailImageService.getImageBatchYn(goodsDetailImageVo);
            if(!chkImgGenYn){
                goodsDetailImageVo.setChgGoodsDetlYn(GoodsEnums.GoodsImageDetailYn.UPDATE_N.getCode());
                goodsDetailImageVo.setChgSpceYn(GoodsEnums.GoodsImageDetailYn.UPDATE_Y.getCode());
                goodsDetailImageVo.setImgGenYn(GoodsEnums.GoodsImageDetailYn.UPDATE_N.getCode());
                goodsDetailImageVo.setCreateId(SessionUtil.getBosUserVO().getUserId());
                goodsDetailImageService.putUpdateGoodsIdInfoForDetailImage(goodsDetailImageVo);
            }
        }
        return ApiResult.success();
    }


    /**
     * @Desc 품목 업데이트 후 상품코드 조회
     * @param
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> getUpdateItemInfoForDetailImage(ItemModifyRequestDto itemModifyRequestDto, MasterItemVo beforeItemDetail, MasterItemVo afterMasterItemVo,
                                         List<ItemNutritionDetailVo> beforeItemNurList, List<ItemNutritionDetailVo> afterItemNurList,List<ItemSpecValueVo> beforeItemSpecList, List<ItemSpecValueVo> afterItemSpecList) throws Exception{

        int differentCount = checkItemInfoForItemDetailImage(itemModifyRequestDto, beforeItemDetail, afterMasterItemVo, beforeItemNurList, afterItemNurList, beforeItemSpecList, afterItemSpecList);


        if(differentCount > 0){
            List<GoodsDetailImageVo> goodsDetailImageVoList = goodsDetailImageService.getUpdateItemInfo(beforeItemDetail.getIlItemCode());

            List<String> goodsIdList = new ArrayList<>();
            for( GoodsDetailImageVo goodsDetailImageVo : goodsDetailImageVoList ){
                goodsIdList.add(goodsDetailImageVo.getIlGoodsId().toString());
            }

            // 상품고시정보 등록
            addUpdateGoodsIdInfoForDetailImage(goodsIdList);
        }

        return ApiResult.success();
    }

    /**
     * @Desc 품목정보 업데이트 여부 확인
     * @param
     * @throws Exception
     */
    public int checkItemInfoForItemDetailImage(ItemModifyRequestDto itemModifyRequestDto, MasterItemVo beforeItemDetail, MasterItemVo afterMasterItemVo,
                            List<ItemNutritionDetailVo> beforeItemNurList, List<ItemNutritionDetailVo> afterItemNurList, List<ItemSpecValueVo> beforeItemSpecList, List<ItemSpecValueVo> afterItemSpecList) throws Exception{
        int differentCount = 0;

        if(itemModifyRequestDto != null){ // 품목 직접 수정

            // 상품상세 기본 정보
            if(!beforeItemDetail.getBasicDescription().equals(itemModifyRequestDto.getBasicDescription() )) {
                differentCount++;
            }

            // 상품상세 주요 정보
            if(!beforeItemDetail.getDetaillDescription().equals(itemModifyRequestDto.getDetaillDescription() )) {
                differentCount++;
            }

            // 영양정보 표시여부
            if(beforeItemDetail.isNutritionDisplayYn() != itemModifyRequestDto.isNutritionDisplayYn() ) {
                differentCount++;
            }

            // 영양정보 표시 기본
            if(!beforeItemDetail.getNutritionDisplayDefalut().equals(itemModifyRequestDto.getNutritionDisplayDefalut()) ) {
                differentCount++;
            }

            // 영양분석 단위 1회 분량
            if(!beforeItemDetail.getNutritionQuantityPerOnce().equals(itemModifyRequestDto.getNutritionQuantityPerOnce()) ) {
                differentCount++;
            }

            // 영양분석 단위 총 분량
            if(!beforeItemDetail.getNutritionQuantityTotal().equals(itemModifyRequestDto.getNutritionQuantityTotal()) ) {
                differentCount++;
            }

            // 영양성분 기타
            if(!beforeItemDetail.getNutritionEtc().equals(itemModifyRequestDto.getNutritionEtc()) ) {
                differentCount++;
            }

            // 상품정보제공
            if(!beforeItemDetail.getIlSpecMasterId().equals(itemModifyRequestDto.getIlSpecMasterId()) ) {
                differentCount++;
            }

            // 영양 정보
            if(beforeItemNurList.size() != itemModifyRequestDto.getAddItemNutritionDetailList().size()) {
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
                        differentCount++;
                        break;
                    }
                }
            }

            // 고시정보
            if(beforeItemSpecList.size() != itemModifyRequestDto.getAddItemSpecValueList().size()) {
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
                        differentCount++;
                        break;
                    }
                }
            }
        }else{ // 승인에 의한 품목 수정
            // 상품상세 기본 정보
            if(!beforeItemDetail.getBasicDescription().equals(afterMasterItemVo.getBasicDescription() )) {
                differentCount++;
            }

            // 상품상세 주요 정보
            if(!beforeItemDetail.getDetaillDescription().equals(afterMasterItemVo.getDetaillDescription() )) {
                differentCount++;
            }

            // 영양정보 표시여부
            if(beforeItemDetail.isNutritionDisplayYn() != afterMasterItemVo.isNutritionDisplayYn() ) {
                differentCount++;
            }

            // 영양정보 표시 기본
            if(!beforeItemDetail.getNutritionDisplayDefalut().equals(afterMasterItemVo.getNutritionDisplayDefalut()) ) {
                differentCount++;
            }

            // 영양분석 단위 1회 분량
            if(!beforeItemDetail.getNutritionQuantityPerOnce().equals(afterMasterItemVo.getNutritionQuantityPerOnce()) ) {
                differentCount++;
            }

            // 영양분석 단위 총 분량
            if(!beforeItemDetail.getNutritionQuantityTotal().equals(afterMasterItemVo.getNutritionQuantityTotal()) ) {
                differentCount++;
            }

            // 영양성분 기타
            if(!beforeItemDetail.getNutritionEtc().equals(afterMasterItemVo.getNutritionEtc()) ) {
                differentCount++;
            }

            // 상품정보제공
            if(!beforeItemDetail.getIlSpecMasterId().equals(afterMasterItemVo.getIlSpecMasterId()) ) {
                differentCount++;
            }

            // 영양 정보
            if(beforeItemNurList.size() != afterItemNurList.size()) {
                differentCount++;
            }else {
                for(ItemNutritionDetailVo beforeItemNurInfo : beforeItemNurList) {
                    String beforeNurCode = beforeItemNurInfo.getNutritionCode();
                    Double beforeNurPercnt = beforeItemNurInfo.getNutritionPercent() == null ? new Double(0.0) : beforeItemNurInfo.getNutritionPercent();
                    Double beforeNurQty	   = beforeItemNurInfo.getNutritionQuantity() == null ? new Double(0.0) : beforeItemNurInfo.getNutritionQuantity();

                    boolean isSame = false;
                    for(ItemNutritionDetailVo afterItemNurInfo : afterItemNurList) {
                        String afterNurCode = afterItemNurInfo.getNutritionCode();
                        Double afterNurPercnt = afterItemNurInfo.getNutritionPercent() == null ? new Double(0.0) : afterItemNurInfo.getNutritionPercent();
                        Double afterNurQty	   = afterItemNurInfo.getNutritionQuantity() == null ? new Double(0.0) : afterItemNurInfo.getNutritionQuantity();

                        if(beforeNurCode.equals(afterNurCode) && beforeNurPercnt.compareTo(afterNurPercnt) == 0 && beforeNurQty.compareTo(afterNurQty) == 0 ) {
                            isSame = true;
                            break;
                        }
                    }

                    if (isSame == false) {
                        differentCount++;
                        break;
                    }
                }
            }


            // 고시정보
            if(beforeItemSpecList.size() != afterItemSpecList.size()) {
                differentCount++;
            }else {
                for(ItemSpecValueVo beforeItemSpecInfo : beforeItemSpecList) {
                    String beforeSpecId = String.valueOf(beforeItemSpecInfo.getIlSpecFieldId());
                    Boolean beforeDirectYn = beforeItemSpecInfo.getDirectYn() == null ? false : beforeItemSpecInfo.getDirectYn();
                    String beforeSpecFieldValue = beforeItemSpecInfo.getSpecFieldValue();

                    boolean isSame = false;
                    for(ItemSpecValueVo afterItemSpecInfo : afterItemSpecList) {
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
                        differentCount++;
                        break;
                    }
                }
            }

        }

        return differentCount;
    }




    /**
     * @Desc 품목 업데이트 후 상품코드 조회
     * @param
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> getUpdateGoodsInfoForDetailImage(GoodsRegistResponseDto beforeGoodsDatas, GoodsRegistRequestDto goodsRegistRequestDto, GoodsRegistApprVo goodsRegistApprVo) throws Exception{

        int differentCount = checkGoodsInfoForItemDetailImage(beforeGoodsDatas, goodsRegistRequestDto, goodsRegistApprVo);

        if(differentCount > 0){
            List<GoodsDetailImageVo> goodsDetailImageVoList = goodsDetailImageService.getUpdateGoodsInfoForDetailImage(beforeGoodsDatas.getIlGoodsDetail().getIlGoodsId());

            List<String> goodsIdList = new ArrayList<>();
            for( GoodsDetailImageVo goodsDetailImageVo : goodsDetailImageVoList ){
                goodsIdList.add(goodsDetailImageVo.getIlGoodsId().toString());
            }

            // 상품고시정보 등록
            addUpdateGoodsIdInfoForDetailImage(goodsIdList);
        }

        return ApiResult.success();
    }

    /**
     * @Desc 상품정보 업데이트 여부 확인
     * @param
     * @throws Exception
     */
    public int checkGoodsInfoForItemDetailImage(GoodsRegistResponseDto beforeGoodsDatas, GoodsRegistRequestDto goodsRegistRequestDto, GoodsRegistApprVo goodsRegistApprVo) throws Exception {
        int differentCount = 0;

        if(goodsRegistRequestDto != null){ // 상품 직접 수정
            // 상품 명
            if(!beforeGoodsDatas.getIlGoodsDetail().getGoodsName().equals(goodsRegistRequestDto.getGoodsName()) ) {
                differentCount++;
            }
            if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsRegistRequestDto.getGoodsType())){

                // (묶음상품)상품상세 기본정보 직접등록 여부(Y:묶음상품전용, M:묶음상품전용+구성상품상세, N:구성상품상세)
                if(!beforeGoodsDatas.getIlGoodsDetail().getGoodsPackageBasicDescYn().equals(goodsRegistRequestDto.getGoodsPackageBasicDescYn()) ) {
                    differentCount++;
                }

                // (묶음상품)상품상세 기본정보
                if(!beforeGoodsDatas.getIlGoodsDetail().getGoodsPackageBasicDesc().equals(goodsRegistRequestDto.getGoodsPackageBasicDesc()) ) {
                    differentCount++;
                }
            }
        }else{ // 승인에 의한 상품 수정
            // 상품 명
            if(!beforeGoodsDatas.getIlGoodsDetail().getGoodsName().equals(goodsRegistApprVo.getGoodsName()) ) {
                differentCount++;
            }
        }

        return differentCount;
    }
}
