package kr.co.pulmuone.v1.statics.pm.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.statics.pm.dto.PromotionStaticsRequestDto;
import kr.co.pulmuone.v1.statics.pm.dto.PromotionStaticsResponseDto;
import kr.co.pulmuone.v1.statics.pm.dto.vo.PromotionStaticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionStaticsBizImpl implements PromotionStaticsBiz {

    @Autowired
    private PromotionStaticsService promotionStaticsService;

    @Override
    public ApiResult<?> getStaticsInternalAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException {
        return ApiResult.success(promotionStaticsService.getStaticsInternalAdvertisingList(dto));
    }

    @Override
    public ExcelDownloadDto getExportExcelStaticsInternalAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException {
        String excelFileName = "내부광고코드별 매출현황통계"+ "_" + DateUtil.getCurrentDate();; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                400, 400, 200, 200, 200, 200, 200
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
        String[] propertyListOfFirstWorksheet = {
                "pageNm", "contentNm", "paidPriceFm", "orderCntFm", "orderUnitPriceFm", "userCntFm", "userUnitPriceFm"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = null;
        firstHeaderListOfFirstWorksheet = new String[]{
                "LEVEL1_페이지", "LEVEL2_영역/키워드", "고객매출", "주문건수", "주문단가", "구매고객수", "인단가"
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

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        PromotionStaticsResponseDto responseDto = promotionStaticsService.getStaticsInternalAdvertisingList(dto);
        List<PromotionStaticsVo> itemList = responseDto.getRows();
        firstWorkSheetDto.setExcelDataList(itemList);

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("금액 (원) / 건수 (건) / 구매고객수(명)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }


    @Override
    public ApiResult<?> getStaticsAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException {
        return ApiResult.success(promotionStaticsService.getStaticsAdvertisingList(dto));
    }

    @Override
    public ExcelDownloadDto getExportExcelStaticsAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException {
        String excelFileName = "외부광고코드별 매출현황통계"+ "_" + DateUtil.getCurrentDate();; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                300, 300, 300, 300, 200, 200, 200, 200, 200
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {
                "source", "medium", "campaign", "content", "paidPriceFm", "orderCntFm", "orderUnitPriceFm", "userCntFm", "userUnitPriceFm"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = null;
        firstHeaderListOfFirstWorksheet = new String[]{
                "대분류(매체)", "중분류(구좌)", "소분류(캠페인)", "세분류(콘텐츠)", "고객매출", "주문건수", "주문단가", "구매고객수", "인단가"
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

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        PromotionStaticsResponseDto responseDto = promotionStaticsService.getStaticsAdvertisingList(dto);
        List<PromotionStaticsVo> itemList = responseDto.getRows();
        firstWorkSheetDto.setExcelDataList(itemList);

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("금액 (원) / 건수 (건) / 구매고객수(명)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }


    @Override
    public ApiResult<?> getStaticsCouponSaleStatusList(PromotionStaticsRequestDto dto) throws BaseException {
        return ApiResult.success(promotionStaticsService.getStaticsCouponSaleStatusList(dto));
    }





    @Override
    public ExcelDownloadDto getExportExcelStaticsCouponSaleStatusList(PromotionStaticsRequestDto dto) throws BaseException {
        String excelFileName = "쿠폰별 매출현황 통계"+ "_" + DateUtil.getCurrentDate();; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                400, 200, 200, 400, 400, 400, 200, 200, 200, 200, 200, 200, 200
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center","center","center","center", "center", "right", "right", "right", "right", "right", "right", "right"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {
                "pmCouponId", "couponTp", "issuePurpose", "displayCouponNm", "bosCouponNm", "useStr", "issueQtyFm", "issueCntFm", "issuePriceFm", "orderCntFm", "discountPriceFm", "paidPriceFm", "userCntFm"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = null;
        firstHeaderListOfFirstWorksheet = new String[]{
                "쿠폰번호", "쿠폰종류", "발급목적", "전시쿠폰명", "관리자 쿠폰명", "사용(PC/모바일)", "생성수량", "발급수량", "발급금액", "주문건수", "쿠폰할인금액", "매출기여", "구매자수"
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

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        PromotionStaticsResponseDto responseDto = promotionStaticsService.getStaticsCouponSaleStatusList(dto);
        List<PromotionStaticsVo> itemList = responseDto.getRows();
        firstWorkSheetDto.setExcelDataList(itemList);

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("금액(원), 수량(건), 구매자 수(명)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public ApiResult<?> getStaticsUserGroupCouponStatusList(PromotionStaticsRequestDto dto) throws BaseException {
        return ApiResult.success(promotionStaticsService.getStaticsUserGroupCouponStatusList(dto));
    }

    @Override
    public ExcelDownloadDto getExportExcelStaticsUserGroupCouponStatusList(PromotionStaticsRequestDto dto) throws BaseException {
        String excelFileName = "회원등급 쿠폰현황 통계"+ "_" + DateUtil.getCurrentDate();; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                400, 400, 400, 400, 400, 400, 400
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "right", "right", "right", "right", "right", "right"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {
                "groupMasterNm", "issueCntFm", "issuePriceFm", "useCntFm", "usePriceFm", "expirationCntFm", "expirationPriceFm"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = null;
        firstHeaderListOfFirstWorksheet = new String[]{
                "회원등급", "발급수량", "발급금액", "사용수량", "사용금액", "소멸수량", "소멸금액"
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

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        PromotionStaticsResponseDto responseDto = promotionStaticsService.getStaticsUserGroupCouponStatusList(dto);
        List<PromotionStaticsVo> itemList = responseDto.getRows();
        firstWorkSheetDto.setExcelDataList(itemList);

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("금액 (원)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }


    @Override
    public ApiResult<?> getStaticsPointStatusList(PromotionStaticsRequestDto dto) throws BaseException {
        return ApiResult.success(promotionStaticsService.getStaticsPointStatusList(dto));
    }

    @Override
    public ExcelDownloadDto getExportExcelStaticsPointStatusList(PromotionStaticsRequestDto dto) throws BaseException {
        String excelFileName = "적립금 현황 통계"+ "_" + DateUtil.getCurrentDate();; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                400, 400, 400, 400
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "right"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {
                    "issueDeptName", "pointType", "statics", "amountFm"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = null;
        firstHeaderListOfFirstWorksheet = new String[]{
                "분담조직", "적립금 유형", "금액내역", "합계"
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

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        PromotionStaticsResponseDto responseDto = promotionStaticsService.getStaticsPointStatusList(dto);
        List<PromotionStaticsVo> itemList = responseDto.getRows();
        firstWorkSheetDto.setExcelDataList(itemList);

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("금액 (원)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public PromotionStaticsResponseDto getAdvertisingType(PromotionStaticsRequestDto dto) throws Exception {
        return promotionStaticsService.getAdvertisingType(dto);
    }





    /**
     * 외부광고코드별 매출현황 상품별 통계
     */
    @Override
    public ApiResult<?> getStaticsAdvertisingGoodsList(PromotionStaticsRequestDto dto) throws BaseException {
        return ApiResult.success(promotionStaticsService.getStaticsAdvertisingGoodsList(dto));
    }

    /**
     * 외부광고코드별 매출현황 통계 상품별 엑셀 다운로드
     */
    @Override
    public ExcelDownloadDto getExportExcelStaticsAdvertisingGoodsList(PromotionStaticsRequestDto dto) throws BaseException {
        String excelFileName = "외부광고코드별 매출현황 통계 상품별"+ "_" + DateUtil.getCurrentDate();; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                300, 300, 300, 300, 200, 300, 200, 300, 200, 200, 200, 200, 200
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "left", "center", "left", "center", "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {
                "source", "medium", "campaign", "content", "pakageGoodsId", "pakageGoodsNm", "ilGoodsId", "goodsNm", "paidPriceFm", "orderCntFm", "orderUnitPriceFm", "userCntFm", "userUnitPriceFm"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = null;
        firstHeaderListOfFirstWorksheet = new String[]{
                "대분류(매체)", "중분류(구좌)", "소분류(캠페인)", "세분류(콘텐츠)", "묶음상품아이디", "묶음상품명", "상품아이디", "상품명", "총매출", "주문건수", "주문단가", "구매고객수", "인단가"
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

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        PromotionStaticsResponseDto responseDto = promotionStaticsService.getStaticsAdvertisingGoodsList(dto);
        List<PromotionStaticsVo> itemList = responseDto.getRows();
        firstWorkSheetDto.setExcelDataList(itemList);

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("금액 (원) / 건수 (건) / 구매고객수(명)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }
}
