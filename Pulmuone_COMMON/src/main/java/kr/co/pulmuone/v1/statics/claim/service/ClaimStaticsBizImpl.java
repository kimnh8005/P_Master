package kr.co.pulmuone.v1.statics.claim.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsResponseDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimStaticsBizImpl implements ClaimStaticsBiz {

    private final ClaimStaticsService claimStaticsService;

    @Override
    public ApiResult<?> getClaimStaticsList(ClaimStaticsRequestDto dto) {
        return ApiResult.success(claimStaticsService.getClaimStaticsList(dto));
    }

    @Override
    public ExcelDownloadDto getClaimStaticsExcelDownload(ClaimStaticsRequestDto dto) {
        String excelFileName = "클레임 현황 통계"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 150, 150, 150, 150, 150, 150, 150, 150, 150,
                150, 150, 150, 150
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "urSupplierName", "sellersGroupName", "sellersName", "orderGoodsCount", "directCancelCompleteCount",
                "cancelApplyCount", "cancelWithdrawalCount", "cancelDenyCount", "applyCancelCompleteCount", "returnApplyCount",
                "returnWithdrawalCount", "returnDenyCount", "returnIngCount", "exchangeCompleteCount"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet1 = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "공급업체", "판매처그룹", "판매처", "총주문상품수", "즉시취소", "취소(승인건)", "취소(승인건)", "취소(승인건)", "취소(승인건)", "반품",
                "반품", "반품", "반품", "재배송"
        };
        String[] firstHeaderListOfFirstWorksheet2 = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "공급업체", "판매처그룹", "판매처", "총주문상품수", "즉시취소", "취소신청", "취소철회", "취소거절", "취소승인", "반품신청",
                "반품철회", "반품거절", "반품승인", "재배송"
        };

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet1); // 첫 번째 헤더 컬럼
        firstWorkSheetDto.setHeaderList(1, firstHeaderListOfFirstWorksheet2); // 첫 번째 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        ClaimStaticsResponseDto itemList = claimStaticsService.getClaimStaticsList(dto);

        firstWorkSheetDto.setExcelDataList(itemList.getRows());

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("(단위: 개)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public ApiResult<?> getClaimReasonStaticsList(ClaimReasonStaticsRequestDto dto) {
        return ApiResult.success(claimStaticsService.getClaimReasonStaticsList(dto));
    }

    @Override
    public ExcelDownloadDto getClaimReasonStaticsExcelDownload(ClaimReasonStaticsRequestDto dto) {
        String excelFileName = "클레임 사유별 현황 통계"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 150, 150, 150, 150, 150, 150, 150, 150, 150,
                150, 150, 150
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "bosClaimLargeName", "bosClaimMiddleName", "bosClaimSmallName", "targetName", "cancelCompletePrice",
                "cancelCompleteCount", "cancelClaimCount", "returnCompletePrice", "returnCompleteCount", "returnClaimCount",
                "exchangeCompletePrice", "exchangeCompleteCount", "exchangeClaimCount"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet1 = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "취소사유(대)", "취소사유(중)", "귀책처", "귀책구분", "취소", "취소", "취소", "반품", "반품", "반품",
                "재배송", "재배송", "재배송"
        };
        String[] firstHeaderListOfFirstWorksheet2 = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "취소사유(대)", "취소사유(중)", "귀책처", "귀책구분", "취소금액", "취소건수", "취소수량", "반품금액", "반품건수", "반품수량",
                "재배송금액", "재배송건수", "재배송수량"
        };

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet1); // 첫 번째 헤더 컬럼
        firstWorkSheetDto.setHeaderList(1, firstHeaderListOfFirstWorksheet2); // 첫 번째 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        ClaimReasonStaticsResponseDto itemList = claimStaticsService.getClaimReasonStaticsList(dto);

        firstWorkSheetDto.setExcelDataList(itemList.getRows());

        // 조회조건정보 추가
        List<String> addInfoList = new ArrayList<>();
        addInfoList.add(dto.getSearchInfo());
        addInfoList.add("(단위: 개)");
        firstWorkSheetDto.setAddInfoList(addInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

}
