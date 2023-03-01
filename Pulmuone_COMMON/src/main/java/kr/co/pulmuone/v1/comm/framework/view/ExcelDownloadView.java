package kr.co.pulmuone.v1.comm.framework.view;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/*
 * 별도 ContentNegotiationConfigurer 설정 없이 필요한 controller 에서 바로 @Autowired 하여 사용할 수 있도록 @Component 로 등록
 *
 */
@Slf4j
@Component
public class ExcelDownloadView extends AbstractXlsxStreamingView {

    // Controller 에서 ModelAndView 에 excelDownloadDto 를 세팅시 Key 값
    public static String excelDownloadDtoKeyInModel = "excelDownload";

    // 엑셀 다운로드시 확장자
    private static final String EXCEL_FILE_EXTENSION = "xlsx";

    // 엑셀 다운로드시 Response Header 에 인코딩된 파일명을 지정할 Key 값
    private static final String EXCEL_DOWNLOAD_NAME_RESPONSE_HEADER_KEY = "file-name";

    private static final int DEFAULT_COLUMN_PIXEL = 150; // 컬럼 너비 미지정시 기본 pixel
    private static final String DEFAULT_HORIZONTAL_ALIGNMENT = "left"; // 정렬 옵션 미지정시 기본 정렬

    /* 헤더 셀 스타일 설정 */
    private static final IndexedColors HEADER_CELL_COLOR = IndexedColors.BLUE_GREY; // 헤더 배경색
    private static final FillPatternType HEADER_CELL_FILL_PATTERN = FillPatternType.SOLID_FOREGROUND; // 헤더 배경색 패턴
    private static final short HEADER_CELL_HEIGHT = 40; // 헤더 행 높이 ( 단위 : pixel )

    private static final IndexedColors HEADER_CELL_FONT_COLOR = IndexedColors.WHITE; // 헤더 폰트 색
    private static final short HEADER_CELL_FONT_SIZE = 12; // 헤더 폰트 크기

    /* 본문 데이터 셀 스타일 공통 설정 */
    private static final IndexedColors DATA_CELL_COLOR = IndexedColors.WHITE; // 본문 데이터 배경색
    private static final FillPatternType dataCellFillPattern = FillPatternType.SOLID_FOREGROUND; // 본문 데이터 배경색 패턴
    private static final short DATA_CELL_HEIGHT = 35; // 본문 데이터 행 높이 ( 단위 : pixel )

    private static final IndexedColors DATA_CELL_FONT_COLOR = IndexedColors.BLACK; // 본문 데이터 폰트 색
    private static final short DATA_CELL_FONT_SIZE = 12; // 본문 데이터 폰트 크기

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // controller 에서 model 객체에 담은 excelDownloadDto 형변환
        ExcelDownloadDto excelDownloadDto = (ExcelDownloadDto) model.get(excelDownloadDtoKeyInModel);

        Sheet worksheet = null;

        // 각 워크시트별 반복문 시작
        for (ExcelWorkSheetDto workSheetDto : excelDownloadDto.getExcelWorkSheetList()) {

            // 워크시트 생성
            worksheet = workbook.createSheet(workSheetDto.getWorkSheetName());

            // 해당 워크시트에 헤더 1차 세팅 : 헤더 좌우 머지, 제목 값 세팅
            setWorkSheetHeaderWithHorizontalMerge(workSheetDto, workbook, worksheet);

            // 해당 워크시트에 헤더 2차 세팅 : 헤더 상하 머지
            setWorkSheetHeaderWithVerticalMerge(workSheetDto, worksheet);

            // 헤더 마지막 행의 모든 컬럼에 필터 생성
            setFilterInLastWorkSheetHeadeHeader(workSheetDto, worksheet);

            // 해당 워크시트에 데이터 세팅
            setWorkSheetData(workSheetDto, workbook, worksheet);

        } // 워크시트별 반복문 끝

        /*
         * 엑셀 다운로드 response 정보 세팅
         *
         */
        setExcelDownloadResponse(excelDownloadDto, response);

    }

    /*
     * 엑셀 제목 헤더 1차 세팅 : 반복되는 컬럼은 좌우 셀 병합, 제목 값 세팅
     *
     */
    private void setWorkSheetHeaderWithHorizontalMerge(ExcelWorkSheetDto workSheetDto, Workbook workbook, Sheet worksheet) {

        // 헤더에 세팅할 CellStyle 생성 : 헤더 내 컬럼은 모두 같은 cellStyle, 폰트 공유
        CellStyle headerCellStyle = setHeaderCellStyle(workbook);

        /*
         * 워크시트 상단의 컬럼 헤더 생성
         *
         */
        int headerRowCount = workSheetDto.getHeaderRowCount();

        Row headerRow = null;
        Cell headerCell = null;

        for (int headerRowIndex = 0; headerRowIndex < headerRowCount; headerRowIndex++) {

            String[] headerList = workSheetDto.getHeaderList(headerRowIndex);

            // 헤더 행 생성
            headerRow = worksheet.createRow(headerRowIndex);

            // 헤더 행 높이 지정
            headerRow.setHeight(calculatePixelToRowHeight(HEADER_CELL_HEIGHT));

            /*
             * 컬럼별 너비 설정 : 첫 번째 헤더일 때 한 번만 실행
             *
             */
            if (headerRowIndex == 0) {
                setColumnWidth(workSheetDto, worksheet);
            } // headerRowIndex == 0 조건문 끝

            /*
             * 해당 엑셀 제목 헤더 라인에서 동일 이름 컬럼명 반복 여부 확인 : 반복시 반복 횟수만큼 좌우 셀 병합
             *
             */
            mergeWorkSheetHeaderHorizontal(worksheet, headerList, headerRowIndex);

            /*
             * 헤더 행의 컬럼별 제목 값 세팅 반복문 시작
             *
             */
            for (int columnIndex = 0; columnIndex < headerList.length; columnIndex++) {

                headerCell = headerRow.createCell(columnIndex); // 셀 생성

                /*
                 * 헤더 컬럼별 제목 설정
                 */
                String headerName = headerList[columnIndex];

                headerCell.setCellValue(headerName);

                /*
                 * 헤더 컬럼별 셀 스타일 && 폰트 설정
                 */
                headerCell.setCellStyle(headerCellStyle);

            } // 헤더 행의 컬럼별 헤더 세팅 반복문 끝

        } // 전체 헤더 세팅 반복문 끝

    }

    /*
     * 헤더 셀 스타일 생성 : 헤더는 모두 같은 CellStyle 공유
     *
     */
    private CellStyle setHeaderCellStyle(Workbook workbook) {

        CellStyle headerCellStyle = workbook.createCellStyle();

        headerCellStyle.setAlignment(HorizontalAlignment.CENTER); // 헤더는 모두 가운데 정렬
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 수직 가운데 정렬
        headerCellStyle.setFillForegroundColor(HEADER_CELL_COLOR.getIndex()); // 헤더 배경색
        headerCellStyle.setFillPattern(HEADER_CELL_FILL_PATTERN); // 헤더 배경색 패턴

        // 헤더 셀 테두리 지정
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);

        headerCellStyle.setWrapText(true); // 여러줄 표시

        // 헤더 셀 폰트 생성
        Font headerCellFont = workbook.createFont();
        headerCellFont.setColor(HEADER_CELL_FONT_COLOR.getIndex());
        headerCellFont.setFontHeightInPoints(HEADER_CELL_FONT_SIZE);
        // headerCellFont.setBold(true); // 굵게 표시

        headerCellStyle.setFont(headerCellFont);

        return headerCellStyle;

    }

    /*
     * 컬럼별 너비 설정 : 첫 번째 헤더일 때 한 번만 실행
     *
     */
    private void setColumnWidth(ExcelWorkSheetDto workSheetDto, Sheet worksheet) {

        // 전체 컬럼 개수 : 첫 번째 헤더의 컬럼 개수로 판단함
        int headerColumnLength = workSheetDto.getHeaderList(0).length;

        for (int columnIndex = 0; columnIndex < headerColumnLength; columnIndex++) {

            /*
             * 컬럼별 너비 설정
             */
            if (workSheetDto.getWidthList() != null && workSheetDto.getWidthList()[columnIndex] != null) {

                worksheet.setColumnWidth(columnIndex //
                        , calculatePixelToColumnWidth(workSheetDto.getWidthList()[columnIndex]) //
                );

            } else { // 별도 정의된 컬럼 너비 목록을 전달받지 않은 경우 : 기본 컬럼 너비로 일괄 지정함

                worksheet.setColumnWidth(columnIndex //
                        , calculatePixelToColumnWidth(DEFAULT_COLUMN_PIXEL) //
                );

            }

        }

    }

    /*
     * 해당 엑셀 제목 헤더 라인에서 동일 이름 컬럼명 반복 여부 확인 : 반복시 반복 횟수만큼 좌우 셀 병합
     *
     */
    private void mergeWorkSheetHeaderHorizontal(Sheet worksheet, String[] headerList, int headerRowIndex) {

        int startMergedColumnIndex = 0; // 병합할 시작 컬럼 index
        int endMergedColumnIndex = 0; // 병합할 마지막 컬럼 index

        StringBuilder headerCellValue = new StringBuilder();

        for (int columnIndex = 0; columnIndex < headerList.length; columnIndex++) {

            /*
             * 첫번째 컬럼 : cellValue 에 첫번 째 헤더 제목 세팅하고 다음 반복 실시
             *
             */
            if (columnIndex == 0) {
                headerCellValue.setLength(0);
                headerCellValue.append(headerList[0]);

                continue;

            }

            /*
             * 이전 헤더 컬럼 제목과 현재 컬럼 제목이 일치하는 경우
             *
             * => 1. endMergedColumnIndex 를 해당 컬럼 index 로 세팅
             *
             * => 2. 마지막 컬럼인 경우 병합 실시
             *
             * => 이전 값과 현재 컬럼 제목 일치하므로 cellValue 유지
             *
             */
            if (headerCellValue.toString().equals(headerList[columnIndex])) {

                endMergedColumnIndex = columnIndex;

                if (columnIndex == headerList.length - 1) { // 마지막 컬럼인 경우 병합 실시

                    worksheet.addMergedRegion(new CellRangeAddress( //
                            headerRowIndex, headerRowIndex //
                            , startMergedColumnIndex, endMergedColumnIndex //
                    )); //

                }

            } else {

                /*
                 * 이전 헤더 컬럼 제목과 현재 컬럼 제목이 같지 않은 경우
                 *
                 * => 1. ( 병합 시작 컬럼 인덱스 != 병합 마지막 컬럼 인덱스 )
                 *
                 * ===> 같은 값 나오다 새로운 값 등장 : 이전 값은 값 셀 병합 후 현재 컬럼 인덱스로 시작 / 마지막 인덱스 세팅
                 *
                 * => 2. ( 병합 시작 컬럼 인덱스 == 병합 마지막 컬럼 인덱스 )
                 *
                 * ===> 계속 다른 값 등장 : 현재 컬럼 인덱스로 시작 / 마지막 인덱스 세팅
                 *
                 */
                headerCellValue.setLength(0); // 이전 값과 비교 끝났으므로 이전 값 초기화
                headerCellValue.append(headerList[columnIndex]); // 다음 비교시 사용할 수 있도록 현재 컬럼의 값 세팅

                if (startMergedColumnIndex != endMergedColumnIndex) {

                    worksheet.addMergedRegion(new CellRangeAddress( //
                            headerRowIndex, headerRowIndex //
                            , startMergedColumnIndex, endMergedColumnIndex //
                    )); //

                }

                startMergedColumnIndex = columnIndex;
                endMergedColumnIndex = columnIndex;

            }

        }

    }

    /*
     * 엑셀 제목 헤더 2차 세팅 : 상하로 같은 값이고 이미 merge 되지 않은 셀인 경우 상하 머지 실시
     *
     */
    private void setWorkSheetHeaderWithVerticalMerge(ExcelWorkSheetDto workSheetDto, Sheet worksheet) {

        // 첫번째 헤더 배열의 길이를 전체 컬럼 개수로 판단함
        int columnCount = workSheetDto.getHeaderList(0).length;

        int headerRowCount = workSheetDto.getHeaderRowCount();

        List<String> headerColumnValueList = null; // 각 엑셀 헤더 컬럼의 각 행별 값 취합할 리스트

        // 각 헤더 컬럼별 반복문 start
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {

            headerColumnValueList = new ArrayList<>(); // 초기화

            /*
             * 해당 헤더 컬럼에서 row 별 1차 반복문 Start : 각 row 의 값을 headerCellValueList 에 취합
             */
            for (int headerRowIndex = 0; headerRowIndex < headerRowCount; headerRowIndex++) {

                headerColumnValueList.add( //
                        worksheet.getRow(headerRowIndex).getCell(columnIndex).getStringCellValue() //
                );

            } // 해당 헤더 컬럼에서 row 별 1차 반복문 end

            /*
             * 해당 엑셀 제목 헤더 컬럼에서 상하로 값 반복 여부 확인 : 상하로 값 반복시 해당 셀이 merge 되지 않은 경우 상하 merge
             */
            mergeWorkSheetHeaderVertical(headerColumnValueList, worksheet, columnIndex);

        } // 각 헤더 컬럼별 반복문 end
    }

    /*
     * 해당 엑셀 제목 헤더 컬럼에서 상하로 값 반복 여부 확인 : 상하로 값 반복시 해당 셀이 merge 되지 않은 경우 상하 merge
     *
     */
    private void mergeWorkSheetHeaderVertical(List<String> headerColumnValueList, Sheet worksheet, int columnIndex) {

        int startMergedRowIndex = 0; // 병합할 시작 row index
        int endMergedRowIndex = 0; // 병합할 마지막 row index

        // 2번째 row 인덱스부터 반복문 시작
        for (int headerRowIndex = 1; headerRowIndex < headerColumnValueList.size(); headerRowIndex++) {

            String headerCellValue = headerColumnValueList.get(headerRowIndex);

            /*
             * 이전 row 제목과 현재 row 제목의 값이 일치하는 경우
             *
             * 1. 병합할 마지막 row 인덱스를 현재 제목의 row 인덱스로 세팅
             *
             * 2. 마지막 row 이면 merge 되지 않은 cell 인 경우, merge 실시
             *
             */
            if (headerCellValue.equals(headerColumnValueList.get(headerRowIndex - 1))) {

                endMergedRowIndex = headerRowIndex; // 병합할 마지막 row 인덱스를 현재 제목의 row 인덱스로 세팅

                if (headerRowIndex == headerColumnValueList.size() - 1 // 마지막 row 인 경우
                        && !isCellMerged(worksheet, headerRowIndex, columnIndex) // merge 되지 않은 cell 인 경우
                ) { //

                    worksheet.addMergedRegion(new CellRangeAddress( //
                            startMergedRowIndex, endMergedRowIndex //
                            , columnIndex, columnIndex //
                    )); //

                }

                /*
                 * 이전 행 제목과 현재 행 제목이 같지 않은 경우
                 *
                 * 1. ( 병합 시작 row 인덱스 != 병합 마지막 row 인덱스 )
                 *
                 * ===> 이전 row 까지 같은 값 나오다 새로운 값 등장 : 이전 값은 값 셀 병합 후 현재 r
                 *
                 * 2. ( 병합 시작 컬럼 인덱스 == 병합 마지막 컬럼 인덱스 )
                 *
                 * ===> 계속 다른 값 등장 : 별도 로직 없음
                 *
                 * 3. 현재 row 인덱스로 시작 / 마지막 인덱스 세팅
                 *
                 */

            } else {

                if (startMergedRowIndex != endMergedRowIndex // 이전 row 까지 같은 값 나오다 새로운 값 등장
                        && !isCellMerged(worksheet, headerRowIndex, columnIndex) // merge 되지 않은 cell 인 경우
                ) {

                    worksheet.addMergedRegion(new CellRangeAddress( //
                            startMergedRowIndex, endMergedRowIndex //
                            , columnIndex, columnIndex //
                    )); //

                }

                // 현재 row 인덱스로 시작 / 마지막 인덱스 세팅
                startMergedRowIndex = headerRowIndex;
                endMergedRowIndex = headerRowIndex;

            }

        } // 각 헤더 컬럼에서 row 별 반목분 end

    }

    /*
     * 헤더 마지막 행의 모든 컬럼에 필터 생성
     *
     */
    private void setFilterInLastWorkSheetHeadeHeader(ExcelWorkSheetDto workSheetDto, Sheet worksheet) {

        int headerRowCount = workSheetDto.getHeaderRowCount();
        int dataRowCount = 0;

        if (workSheetDto.getExcelDataList() != null) {
            dataRowCount = workSheetDto.getExcelDataList().size(); // 전체 데이터 행 수
        }
        int propertyCount = workSheetDto.getPropertyList().length;

        if(headerRowCount > 0 && propertyCount > 0)
	        worksheet.setAutoFilter( //
	                new CellRangeAddress(headerRowCount - 1, headerRowCount + dataRowCount - 1, 0, propertyCount - 1) //
	        );

    }

    /*
     * 해당 워크시트에 데이터 세팅
     *
     */
    private void setWorkSheetData(ExcelWorkSheetDto workSheetDto, Workbook workbook, Sheet worksheet) {

        Row dataRow = null;
        Cell dataCell = null;
        JsonNode nodeValue = null;

        StringBuilder dataCellValue = new StringBuilder();

        int headerRowCount = workSheetDto.getHeaderRowCount();
        int dataRowCount = 0;

        String[] propertyList = workSheetDto.getPropertyList();
        int propertyCount = propertyList.length;

        /*
         * 워크시트 본문 데이터의 컬럼별 CellStyle / Font 목록 생성
         *
         */
        List<CellStyle> dataCellStyleList = new ArrayList<>(); // 본문 데이터 컬럼별 cellStyle 목록

        for (int columnIndex = 0; columnIndex < propertyCount; columnIndex++) {

            // 본문 데이터의 컬럼별 셀 스타일, 폰트 생성 후 dataCellStyleList 에 추가
            dataCellStyleList.add(getDataCellStyle(workbook, workSheetDto, columnIndex));

        }

        /*
         * 워크시트 데이타 본문 생성
         *
         */
        if (workSheetDto.getExcelDataList() != null) {
            dataRowCount = workSheetDto.getExcelDataList().size(); // 전체 데이터 행 수
        }


        // ********************************************************************
        // [병합] 병합관련 변수 Start
        // ********************************************************************
        int rowIdx      = 0;
        String befVal   = "";
        String befVal2  = "";
        String curVal   = "";
        String curVal2  = "";
        int firstRow    = 0;
        int firstRow2    = 0;
        boolean isFirstReset = false;
        // [병합] 병합관련 변수 End
        // ********************************************************************

        List<Map<String, Object>> mergeList1 = new ArrayList<Map<String, Object>>();
        Map<String, Object> mergeMap = null;

        List<Map<String, Object>> mergeList2 = new ArrayList<Map<String, Object>>();
        Map<String, Object> mergeMap2 = null;

        log.debug("# dataRowCount   :: " + dataRowCount);
        log.debug("# headerRowCount :: " + headerRowCount);
        // --------------------------------------------------------------------
        // 로우 반복문 시작
        // --------------------------------------------------------------------
        for (int dataRowIdx = 0; dataRowIdx < dataRowCount; dataRowIdx++) {

          // 본문 데이터 행 생성
          dataRow = worksheet.createRow(headerRowCount + dataRowIdx);

          // 본문 데이터 행 높이 지정
          dataRow.setHeight(calculatePixelToRowHeight(DATA_CELL_HEIGHT));

          // ------------------------------------------------------------------
          // 컬럼 반복문 시작
          // ------------------------------------------------------------------
          for (int columnIndex = 0; columnIndex < propertyCount; columnIndex++) {

            dataCellValue.setLength(0);

            nodeValue = workSheetDto.getExcelDataList().get(dataRowIdx).get(propertyList[columnIndex]);

            // ****************************************************************
            // [병합] 컬럼 값 Get Start
            // ****************************************************************
            if (workSheetDto.isMergeYn()) {
              // [병합] 첫번 째 컬럼
              if (columnIndex == 0) {
                if(nodeValue.isTextual()) {
                  curVal = nodeValue.textValue();
                }
                else {
                  curVal =  nodeValue.toString();
                }
              }
              // [병합] 두번 째 컬럼
              if (columnIndex == 1) {
                if(nodeValue.isTextual()) {
                  curVal2 = nodeValue.textValue();
                }
                else {
                  curVal2 =  nodeValue.toString();
                }
              }
            }
            // [병합] 0번째 컬럼 값 Get End
            // ****************************************************************

            if (nodeValue != null) {
              if(nodeValue.isTextual()) {
                dataCellValue.append(nodeValue.textValue());
              }
              else {
                dataCellValue.append(nodeValue.toString());
              }
            }

            dataCell = dataRow.createCell(columnIndex);
            dataCell.setCellValue(dataCellValue.toString());

            /*
             * 본문 데이터 컬럼별 셀 스타일 && 폰트 설정
             */
            if(dataCellStyleList.get(columnIndex).getDataFormat() == (short) 3){
                dataCell.setCellValue(Float.parseFloat(dataCellValue.toString()));
            }
            dataCell.setCellStyle(dataCellStyleList.get(columnIndex));

          }
          // 각 행별 데이터 세팅 반복문 끝
          // ------------------------------------------------------------------

          // ******************************************************************
          // [병합] 컬럼 중복데이터 수집 Start
          // ******************************************************************
          if (workSheetDto.isMergeYn()) {
            // [병합] 첫번 째 컬럼
            if (!befVal.equals(curVal)) {
              // [병합] 새로운 값 (이전값과 상이) : 새로운 시작점
              log.debug("# [befVal][curVal] 상이 :: " + "[" + rowIdx + "] :: [" + befVal + "][" + curVal + "]");
              firstRow = rowIdx;
              isFirstReset = true;

              // [병합] 대상리스트 add : 이전 loop의 맵
              if (rowIdx > 0) {
                // [병합] 첫번 째 컬럼
                if ((int)mergeMap.get("firstRow") != (int)mergeMap.get("lastRow")) {
                  // [병합] 시작 로우와 종료 로우가 다를 경우
                  log.debug("# [1][firstRow][owIdx] 대상 :: " + "[" + (int)mergeMap.get("firstRow") + "][" + (int)mergeMap.get("lastRow") + "]");
                  mergeList1.add(mergeMap);
                }
              }
            }
            else {
              isFirstReset = false;
            }

            // [병합] 두번 째 컬럼
            if (!befVal2.equals(curVal2)) {
              // [병합] 새로운 값 (이전값과 상이) : 새로운 시작점
              log.debug("# [befVal2][curVal2] 상이 :: " + "[" + rowIdx + "] :: [" + befVal2 + "][" + curVal2 + "]");
              firstRow2 = rowIdx;

              // [병합] 대상리스트 add : 이전 loop의 맵
              if (rowIdx > 0) {
                // [병합] 두번 째 컬럼
                if ((int)mergeMap2.get("firstRow") != (int)mergeMap2.get("lastRow")) {
                  // [병합] 시작 로우와 종료 로우가 다를 경우
                  log.debug("# [2][firstRow][owIdx] 대상 :: " + "[" + (int)mergeMap2.get("firstRow") + "][" + (int)mergeMap2.get("lastRow") + "]");
                  mergeList2.add(mergeMap2);
                }
              }
            }

            // [병합] 루프마다 맵 생성
            mergeMap  = new HashMap<String, Object>();
            mergeMap2 = new HashMap<String, Object>();

            if (rowIdx == 0) {
              // [병합] 첫번 째 컬럼
              mergeMap.put("firstRow", 0);
              mergeMap.put("lastRow" , 0);
              // [병합] 두번 째 컬럼
              mergeMap2.put("firstRow", 0);
              mergeMap2.put("lastRow" , 0);
            }
            else {
              // [병합] 첫번 째 컬럼
              mergeMap.put("firstRow", firstRow);
              mergeMap.put("lastRow" , rowIdx);
              // [병합] 두번 째 컬럼
              mergeMap2.put("firstRow", firstRow2);
              mergeMap2.put("lastRow" , rowIdx);
            }

            // [병합] 이전값 저장 : 첫번 째 컬럼
            befVal  = curVal;
            // [병합] 이전값 저장 : 두번 째 컬럼
            if (isFirstReset) {
              befVal2 = ""; // 첫번째가 새로 시작하면 두번째의 이전값은 ""
            }
            else {
              befVal2 = curVal2;
            }
          }
          // [병합] 컬럼 중복데이터 수집 End
          // ******************************************************************

          // 컬럼 반복문 끝
          rowIdx++;
        }
        // 로우 반복문 끝
        // --------------------------------------------------------------------

        // ********************************************************************
        // [병합] 로우 병합 Start
        //        : 병합은 첫번째/두번째 컬럼만 고려되어 있음
        // ********************************************************************
        // [병합] 루프 종료 후 마지막 중복데이터 수집
        if (workSheetDto.isMergeYn()) {
          // [병합] 첫번 째 컬럼
          if ((int)mergeMap.get("firstRow") != (int)mergeMap.get("lastRow")) {
            // 시작 로우와 종료 로우가 다를 경우
            log.debug("# [1][firstRow][owIdx] 대상 :: " + "[" + (int)mergeMap.get("firstRow") + "][" + (int)mergeMap.get("lastRow") + "]");
            mergeList1.add(mergeMap);
          }
          // [병합] 두번 째 컬럼
          //if ((int)mergeMap2.get("firstRow") != (int)mergeMap2.get("lastRow")) {
          //  // 시작 로우와 종료 로우가 다를 경우
          //  log.debug("# [2][firstRow][owIdx] 대상 :: " + "[" + (int)mergeMap2.get("firstRow") + "][" + (int)mergeMap2.get("lastRow") + "]");
          //  mergeList2.add(mergeMap2);
          //}

          // [병합] Dto에 Set
          Map<Integer, List<Map<String, Object>>> mergeInfoMap = new HashMap<Integer, List<Map<String, Object>>>();
          mergeInfoMap.put(0, mergeList1);
          mergeInfoMap.put(1, mergeList2);
          workSheetDto.setMergeInfoMap(mergeInfoMap);

          // [병합] 셀병합 실행
          exeMergingRows(workSheetDto, worksheet, headerRowCount);
        }
        // [병합] 로우 병합 End
        // ********************************************************************

        // --------------------------------------------------------------------
        // 테이블 하단 추가정보 Add
        // --------------------------------------------------------------------
        if (workSheetDto != null && workSheetDto.getAddInfoList() != null && workSheetDto.getAddInfoList().size() > 0) {
          for (String str : workSheetDto.getAddInfoList()) {
            dataRow = worksheet.createRow(headerRowCount + rowIdx++);
            // 본문 데이터 행 높이 지정
            dataRow.setHeight(calculatePixelToRowHeight(DATA_CELL_HEIGHT));
            dataCell = dataRow.createCell(0);
            dataCell.setCellValue(str);
          }
        }

    }

    /*
     * 엑셀 본문 데이터의 컬럼별 CellStyle, Font 생성
     *
     */
    private CellStyle getDataCellStyle(Workbook workbook, ExcelWorkSheetDto workSheetDto, Integer columnIndex) {

        CellStyle dataCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        String[] alignList = workSheetDto.getAlignList();

        String alignOption = null;

        /*
         * 컬럼별 정렬 지정
         *
         */
        // 정렬 지정 옵션 alignList 이 null : 정렬 지정 옵션을 별도 지정하지 않은 경우
        if (alignList == null || alignList[columnIndex] == null) {

            alignOption = DEFAULT_HORIZONTAL_ALIGNMENT; // 기본값으로 지정

        } else {

            alignOption = alignList[columnIndex];

        }

        switch (alignOption) {

        case "left":

            dataCellStyle.setAlignment(HorizontalAlignment.LEFT); // 좌측 정렬

            break;

        case "center":

            dataCellStyle.setAlignment(HorizontalAlignment.CENTER); // 중앙 정렬

            break;

        case "right":

            dataCellStyle.setAlignment(HorizontalAlignment.RIGHT); // 우측 정렬

            break;

        case "justify":

            dataCellStyle.setAlignment(HorizontalAlignment.JUSTIFY); // JUSTIFY

            break;

        case "distributed":

            dataCellStyle.setAlignment(HorizontalAlignment.DISTRIBUTED); // DISTRIBUTED

            break;

        default:

            dataCellStyle.setAlignment(HorizontalAlignment.LEFT); // 위에 지정된 내용이 아닌 경우 : 좌측 정렬

        }

        /*
         * 컬럼별 타입 지정
         *
         */

        String[] cellTypeList = workSheetDto.getCellTypeList();

        String cellTypeOption = "";

        if (cellTypeList != null && cellTypeList[columnIndex] != null) {

            cellTypeOption = cellTypeList[columnIndex];

        }

        switch (cellTypeOption) {

            case "int":

                dataCellStyle.setDataFormat((short) 3); // 숫자

                break;

            case "double":

                dataCellStyle.setDataFormat((short) 4); // 숫자 (소수점)

                break;

            case "String":

                dataCellStyle.setDataFormat((short) 0x31); // 문자

                break;
        }

        /*
         * 모든 본문 데이터 셀 공통 처리
         *
         */
        dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 수직 가운데 정렬
        dataCellStyle.setFillForegroundColor(DATA_CELL_COLOR.getIndex()); // 본문 데이터 배경색
        dataCellStyle.setFillPattern(dataCellFillPattern); // 본문 데이터 배경색 패턴

        // 본문 데이터 셀 테두리 지정
        dataCellStyle.setBorderTop(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);

        // 본문 데이터 셀 폰트 생성
        font.setColor(DATA_CELL_FONT_COLOR.getIndex());
        font.setFontHeightInPoints(DATA_CELL_FONT_SIZE);

        dataCellStyle.setFont(font);

        return dataCellStyle;

    }

    /*
     * 엑셀 다운로드 response 정보 세팅
     */
    private void setExcelDownloadResponse(ExcelDownloadDto excelDownloadDto, HttpServletResponse response) throws UnsupportedEncodingException {

        // 엑셀 파일 확장자는 excelFileExtension 에 지정한 xlsx 만 허용
        String excelFileName = excelDownloadDto.getExcelFileName() + "." + EXCEL_FILE_EXTENSION;

        // 파일명 인코딩 처리 : 프론트에서 다시 디코딩
        String decodedExcelFileName = java.net.URLEncoder.encode(excelFileName, "UTF-8");

        // 인코딩된 다운로드 파일명을 Response Header 에 지정
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, //
                "attachment;filename=\"" + decodedExcelFileName + "\";charset=\"UTF-8\"");
        response.setHeader(EXCEL_DOWNLOAD_NAME_RESPONSE_HEADER_KEY, decodedExcelFileName);

        // Response Header 에서 노출할 Header Key 지정
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, EXCEL_DOWNLOAD_NAME_RESPONSE_HEADER_KEY);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    }

    /*
     * 해당 cell 이 이미 merge 되었는지 확인
     *
     */
    private boolean isCellMerged(Sheet workSheet, int rowIndex, int columnIndex) {

        for (int i = 0; i < workSheet.getNumMergedRegions(); ++i) {

            CellRangeAddress range = workSheet.getMergedRegion(i);

            if (rowIndex >= range.getFirstRow() && rowIndex <= range.getLastRow() && columnIndex >= range.getFirstColumn() && columnIndex <= range.getLastColumn()) {

                return true; // 해당 cell 은 merge 되었음

            }
        }

        return false; // 해당 cell 은 merge 되지 않음

    }

    /*
     * 엑셀 컬럼의 열 너비 pixel => poi 컬럼 width 계산 공식
     *
     */
    private int calculatePixelToColumnWidth(Integer pixel) {

        return (pixel * 4000) / 156;
    }

    /*
     * 엑셀 행 높이 pixel => poi 컬럼 height 계산 공식
     *
     */
    private short calculatePixelToRowHeight(short pixel) {

        return (short) ((pixel * 1000) / 83);

    }

    /**
     * 로우 병합 실행
     * @param workSheetDto
     * @param worksheet
     * @param headerRowCount
     */
    private void exeMergingRows(ExcelWorkSheetDto workSheetDto, Sheet worksheet, int headerRowCount) {
      log.debug("# exeMergingRows Start");
      Map<Integer, List<Map<String, Object>>> mergeInfoMap = workSheetDto.getMergeInfoMap();

      if (mergeInfoMap != null) {
        log.debug("# mergeInfoMap :: " + mergeInfoMap.toString());
      }
      else {
        log.debug("# mergeInfoMap is Null");
      }
      if (mergeInfoMap != null) {

        // --------------------------------------------------------------------
        // 첫번 째 컬럼
        // --------------------------------------------------------------------
        boolean isExist = IntStream.of(workSheetDto.getMergeIndexArr()).anyMatch(x -> x == 0);
        if (isExist) {
          List<Map<String, Object>> mergeList = (List<Map<String, Object>>)mergeInfoMap.get(0);
          if (mergeList != null) {
            log.debug("# mergeList[0] :: " + mergeList.toString());
          }
          else {
            log.debug("# mergeList[0] is Null");
          }
          if (mergeList != null && mergeList.size() > 0) {
            for (Map<String, Object> unitMap : mergeList) {
              worksheet.addMergedRegion(new CellRangeAddress((int)unitMap.get("firstRow")+headerRowCount, (int)unitMap.get("lastRow")+headerRowCount, 0, 0));
            }
          }
        }

        // --------------------------------------------------------------------
        // 두번 째 컬럼
        // --------------------------------------------------------------------
        isExist = IntStream.of(workSheetDto.getMergeIndexArr()).anyMatch(x -> x == 1);
        if (isExist) {
          List<Map<String, Object>> mergeList = (List<Map<String, Object>>)mergeInfoMap.get(1);
          if (mergeList != null) {
            log.debug("# mergeList[0] :: " + mergeList.toString());
          }
          else {
            log.debug("# mergeList[0] is Null");
          }
          if (mergeList != null && mergeList.size() > 0) {
            for (Map<String, Object> unitMap : mergeList) {
              worksheet.addMergedRegion(new CellRangeAddress((int)unitMap.get("firstRow")+headerRowCount, (int)unitMap.get("lastRow")+headerRowCount, 1, 1));
            }
          }
        }
      }

    }

}
