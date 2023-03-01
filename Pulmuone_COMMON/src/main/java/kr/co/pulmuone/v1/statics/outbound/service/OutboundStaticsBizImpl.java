package kr.co.pulmuone.v1.statics.outbound.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.StaticsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsResponseDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsResponseDto;
import kr.co.pulmuone.v1.statics.outbound.dto.vo.MissOutboundStaticsVo;
import kr.co.pulmuone.v1.statics.outbound.dto.vo.OutboundStaticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutboundStaticsBizImpl implements OutboundStaticsBiz {

    @Autowired
    private OutboundStaticsService outboundStaticsService;

    @Override
    public ApiResult<?> getOutboundStaticsList(OutboundStaticsRequestDto dto) throws BaseException {
        return ApiResult.success(outboundStaticsService.getOutboundStaticsList(dto));
    }

    @Override
    public ExcelDownloadDto getExportExcelOutboundStaticsList(OutboundStaticsRequestDto dto) throws BaseException {
        String excelFileName = "출고처/판매처 별 출고통계"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 150,
                150, 150, 150, 150, 150, 150, 150, 150, 150, 150,
                150, 150, 150, 150, 150, 150, 150, 150, 150, 150,
                150, 150, 150, 150, 150, 150, 150, 150, 150, 150,
                150, 150, 150
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center",
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {
                "div1", "div2",
                "day1Cnt", "day2Cnt", "day3Cnt", "day4Cnt", "day5Cnt", "day6Cnt", "day7Cnt", "day8Cnt", "day9Cnt", "day10Cnt",
                "day11Cnt", "day12Cnt", "day13Cnt", "day14Cnt", "day15Cnt", "day16Cnt", "day17Cnt", "day18Cnt", "day19Cnt", "day20Cnt",
                "day21Cnt", "day22Cnt", "day23Cnt", "day24Cnt", "day25Cnt", "day26Cnt", "day27Cnt", "day28Cnt", "day29Cnt", "day30Cnt",
                "day31Cnt", "sumCnt", "avgCnt"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = null;
        if (StaticsEnums.OutboundSearchType.WAREHOUSE.getCode().equals(dto.getSearchType())) {
            firstHeaderListOfFirstWorksheet = new String[]{ // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                    "출고처", "배송유형",
                    "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일",
                    "11일", "12일", "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일",
                    "21일", "22일", "23일", "24일", "25일", "26일", "27일", "28일", "29일", "30일",
                    "31일", "합계", "평균"
            };
        } else {
            firstHeaderListOfFirstWorksheet = new String[]{ // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                    "판매처그룹", "판매처",
                    "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일",
                    "11일", "12일", "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일",
                    "21일", "22일", "23일", "24일", "25일", "26일", "27일", "28일", "29일", "30일",
                    "31일", "합계", "평균"
            };
        }

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
        OutboundStaticsResponseDto responseDto = outboundStaticsService.getOutboundStaticsList(dto);
        List<OutboundStaticsVo> itemList = responseDto.getRows();
        firstWorkSheetDto.setExcelDataList(itemList);

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("주문수량(건)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public ApiResult<?> getMissOutboundStaticsList(MissOutboundStaticsRequestDto dto) throws BaseException {
        return ApiResult.success(outboundStaticsService.getMissOutboundStaticsList(dto));
    }

    @Override
    public ExcelDownloadDto getExportExcelMissOutboundStaticsList(MissOutboundStaticsRequestDto dto) throws BaseException {
        String excelFileName = "미출 통계"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 150, 150, 150, 150, 150, 150, 150
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {
                "dt", "deliveryGoodsCnt", "missGoodsCnt", "missGoodsRateName", "missGoodsPriceName", "deliveryOrderCnt", "missOrderCnt", "missOrderRateName"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "구분", "출고 지시상품", "미출 발생상품", "상품 미출률(%)", "미출 발생금액", "출고 지시주문", "미출 발생주문", "주문 미출률%"
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
        MissOutboundStaticsResponseDto responseDto = outboundStaticsService.getMissOutboundStaticsList(dto);
        List<MissOutboundStaticsVo> itemList = responseDto.getRows();
        firstWorkSheetDto.setExcelDataList(itemList);

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("출고 대상상품(개) / 미출 발생상품(개) / 미출 발생금액(원) / 출고 대상주문(건) / 미출 발생주문(건)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

}
