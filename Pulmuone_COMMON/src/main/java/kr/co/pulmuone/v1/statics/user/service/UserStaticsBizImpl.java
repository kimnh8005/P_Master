package kr.co.pulmuone.v1.statics.user.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.statics.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStaticsBizImpl implements UserStaticsBiz {

    private final UserStaticsService userStaticsService;

    @Override
    public ApiResult<?> getUserTypeStaticsList(UserTypeStaticsRequestDto dto) {
        return ApiResult.success(userStaticsService.getUserTypeStaticsList(dto));
    }

    @Override
    public ExcelDownloadDto getUserTypeStaticsExcelDownload(UserTypeStaticsRequestDto dto) {
        String excelFileName = "회원 유형별 판매/매출 현황 통계"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 150, 150, 150, 150, 150, 150, 150, 150
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
        String[] propertyListOfFirstWorksheet = { //
                "gubun", "standardUserCount", "standardPaidPrice", "standardOrderCount", "standardGoodsCount",
                "contrastUserCount", "contrastPaiPrice", "contrastOrderCount", "contrastGoodsCount"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet1 = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "구분", "기준기간", "기준기간", "기준기간", "기준기간", "대비기간", "대비기간", "대비기간", "대비기간"
        };
        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet2 = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "구분", "구매고객 수", "매출금액", "주문건수", "주문 상품수량", "구매고객 수", "매출금액", "주문건수", "주문 상품수량"
        };

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet1); // 헤더 컬럼
        firstWorkSheetDto.setHeaderList(1, firstHeaderListOfFirstWorksheet2); // 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        UserTypeStaticsResponseDto itemList = userStaticsService.getUserTypeStaticsList(dto);

        firstWorkSheetDto.setExcelDataList(itemList.getRows());

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("판매금액(원)/주문건수(건)/주문 상품수량(개)/구매고객 수(명)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public ApiResult<?> getUserGroupStaticsList(UserGroupStaticsRequestDto dto) {
        return ApiResult.success(userStaticsService.getUserGroupStaticsList(dto));
    }

    @Override
    public ExcelDownloadDto getUserGroupStaticsExcelDownload(UserGroupStaticsRequestDto dto) {
        String excelFileName = "일반 회원 등급별 판매현황 통계"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 150, 150, 150, 150
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "groupName", "userCount", "paidPrice", "orderCount", "goodsCount"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "구분", "구매고객 수", "판매금액", "주문건수", "주문 상품수량"
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
        UserGroupStaticsResponseDto itemList = userStaticsService.getUserGroupStaticsList(dto);

        firstWorkSheetDto.setExcelDataList(itemList.getRows());

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("판매금액(원)/주문건수(건)/주문 상품수량(개)/구매고객 수(명)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public ApiResult<?> getUserCountStaticsList(UserCountStaticsRequestDto dto) {
        return ApiResult.success(userStaticsService.getUserCountStaticsList(dto));
    }

    @Override
    public ExcelDownloadDto getUserCountStaticsExcelDownload(UserCountStaticsRequestDto dto) {
        String excelFileName = "회원 보유현황 통계"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 150, 150, 150
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "type", "subType", "standardCount", "contrastTotal"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "구분", "구분", "기준기간", "대비기간"
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
        UserCountStaticsResponseDto itemList = userStaticsService.getUserCountStaticsList(dto);

        firstWorkSheetDto.setExcelDataList(itemList.getRows());

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

}
