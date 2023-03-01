package kr.co.pulmuone.v1.system.log.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogDetailResponseDto;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogRequestDto;
import kr.co.pulmuone.v1.system.log.dto.vo.IllegalDetectLogResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class IllegalDetectLogBizImpl implements IllegalDetectLogBiz{

    @Autowired
    private IllegalDetectLogService service;

    /**
     * @Desc 부정거래 탐지 목록
     * @param
     * @return
     */
    @Override
    public ApiResult<?> getIllegalDetectLogList(IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        List<String> searchKeyList = new ArrayList<String>();

        String status = illegalDetectLogRequestDto.getSearchIllegalStatusType();

        if( StringUtils.isNotEmpty(status) && status.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(status.split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));
        }

        illegalDetectLogRequestDto.setSearchIllegalStatusTypeList(searchKeyList);

        return ApiResult.success(service.getIllegalDetectLogList(illegalDetectLogRequestDto));
    }



    /**
     * 부정거래탐지 상태변경
     *
     * @param requestDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putCompleteRequest(IllegalDetectLogRequestDto requestDto) throws Exception {

        if(CollectionUtils.isNotEmpty(requestDto.getStIllegalLogIdList())) {
            IllegalDetectLogResultVo vo = new IllegalDetectLogResultVo();
            for(String stIllegalLogId : requestDto.getStIllegalLogIdList()) {
                requestDto.setStIllegalLogId(stIllegalLogId);
                service.putIllegalDetectDetailInfo(requestDto);
            }
        }
        else return ApiResult.fail();

        return ApiResult.success();
    }


    /**
     * 부정거래탐지 상세 조회
     *
     * @param illegalDetectLogRequestDto
     * @return CouponDetailResponseDto
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system="BOS")
    public ApiResult<?> getIllegalDetectLogDetail(IllegalDetectLogRequestDto illegalDetectLogRequestDto) throws Exception {

        IllegalDetectLogDetailResponseDto result = new IllegalDetectLogDetailResponseDto();
        IllegalDetectLogResultVo vo = new IllegalDetectLogResultVo();

        vo = service.getIllegalDetectLogDetail(illegalDetectLogRequestDto);

        result.setRows(vo);

        return ApiResult.success(result);
    }


    
    /**
     * 부정거래탐지 내용 수정
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putIllegalDetectDetailInfo(IllegalDetectLogRequestDto illegalDetectLogRequestDto) throws Exception {

        service.putIllegalDetectDetailInfo(illegalDetectLogRequestDto);

        return ApiResult.success();
    }




    /**
     * @param illegalDetectLogRequestDto : 부정거래 탐지 리스트 검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     * @Desc 부정거래 탐지 리스트 엑셀 다운로드 목록 조회
     */
    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ExcelDownloadDto illegalDetectListExportExcel(IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        String excelFileName = "부정거래 탐지 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                100, 200, 200, 400, 800, 400, 200, 200, 200};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "left", "left", "center", "center", "center", "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "stIllegalLogId", "illegalStatusTypeName", "illegalTypeName", "illegalDetailTypeName", "illegalDetectCmt", "urPcidCd", "loginId", "odid", "createDt"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "FDS CODE", "상태", "부정거래 분류", "부정거래 유형", "부정거래 탐지 내용", "Device ID", "회원 ID", "주문번호", "탐지일시"};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼


        List<String> searchKeyList = new ArrayList<String>();

        String status = illegalDetectLogRequestDto.getSearchIllegalStatusType();

        if( StringUtils.isNotEmpty(status) && status.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(status.split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));
        }

        illegalDetectLogRequestDto.setSearchIllegalStatusTypeList(searchKeyList);

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<IllegalDetectLogResultVo> itemList = service.illegalDetectListExportExcel(illegalDetectLogRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }




    /**
     * @param illegalDetectLogRequestDto : 부정거래 탐지 회원ID 검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     * @Desc 부정거래 탐지 회원ID 엑셀 다운로드 목록 조회
     */
    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ExcelDownloadDto illegalDetectUserIdxportExcel(IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        String excelFileName = "부정거래 탐지 회원ID"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                200};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "loginId"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "회원 ID"};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼


        List<String> searchKeyList = new ArrayList<String>();

        String status = illegalDetectLogRequestDto.getSearchIllegalStatusType();

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<IllegalDetectLogResultVo> itemList = service.illegalDetectUserIdxportExcel(illegalDetectLogRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }


    /**
     * @param illegalDetectLogRequestDto : 부정거래 탐지 주문번호 검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     * @Desc 부정거래 탐지 주문번호 엑셀 다운로드 목록 조회
     */
    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ExcelDownloadDto illegalDetectOrderExportExcel(IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        String excelFileName = "부정거래 탐지 주문번호"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                200};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "odid"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "주문번호"};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼


        List<String> searchKeyList = new ArrayList<String>();

        String status = illegalDetectLogRequestDto.getSearchIllegalStatusType();

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<IllegalDetectLogResultVo> itemList = service.illegalDetectOrderExportExcel(illegalDetectLogRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }
}
