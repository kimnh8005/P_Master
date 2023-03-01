package kr.co.pulmuone.v1.statics.data.service;

import kr.co.pulmuone.v1.comm.enums.StaticsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.statics.data.DataDownloadStaticsMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsRequestDto;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultHandler;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * <PRE>
 * 통계관리 > 데이터 추출 COMMON Service
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0       2021.06.10.              whseo         최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class DataDownloadStaticsService {
    @Value("${module-name}")
    private String moduleName; // 현재 가동중인 모듈명

    @Value("${app.storage.public.public-root-location}")
    private String publicRootLocation;

    @Value("${resource.server.url.image}")
    private String imageUrl;

    private final DataDownloadStaticsMapper dataDownloadStaticsMapper;

    /**
     * 공통 저장경로 + path getter
     */
    private String getCacheLocalFilePath(String path) {
        return Paths.get(moduleName + File.separator  + "statics" + File.separator + path).toString();
    }

    /**
     * 데이터 추출 조회
     */
    protected DataDownloadStaticsResponseDto getDataDownloadStaticsList(DataDownloadStaticsRequestDto dto) throws BaseException {
        dto.setStartDt(DateUtil.convertFormatNew(dto.getStartDt(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setEndDt(DateUtil.convertFormatNew(dto.getEndDt(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setDataDownloadList(StringUtil.getArrayListWithoutAll(dto.getDataDownloadFilter()));
        DataDownloadStaticsResponseDto result = new DataDownloadStaticsResponseDto();

        List<String> dataArray = makeDataFileAndReturnResult(dto);
        result.setDataDownloadTypeResult(dataArray);

        return result;
    }

    /**
     * 데이터 추출 조회 및 엑셀파일 생성 후 결과값 return
     */
    private List<String> makeDataFileAndReturnResult(DataDownloadStaticsRequestDto dto) throws BaseException {
        if(SessionUtil.getBosUserVO() == null) {
            throw new BaseException("Need BOS Login");
        }
        List<String> returnDataArray = new ArrayList<>();

        for (String dataType : dto.getDataDownloadList()) {

            // 1. 추출 유형에 따른 쿼리 실행 및 기본값 세팅
            StaticsTypeFactory staticsTypeFactory = new StaticsTypeFactory(dto, dataType);
            String excelFilePath = staticsTypeFactory.makeExcelFile();
            // 3. 조회결과 화면에 필요한 추출건수, 합계, 엑셀파일 저장 경로를 전달 (추출유형___추출건수___합계금액___엑셀파일경로)
            String dataDownloadString = dataType + "___" + staticsTypeFactory.getRowNum() + "___" + staticsTypeFactory.getSumAmt().setScale(0, RoundingMode.HALF_UP) + "___" + excelFilePath;
            log.info(">>> makeDataDownloadStatics : " + SessionUtil.getBosUserVO().getLoginId() + " , " + dataDownloadString);
            returnDataArray.add(dataDownloadString);
        }

        return returnDataArray;
    }

    /**
     * 추출 데이터 타입에 따른 반환을 책임지는 클래스
     */
    public class StaticsTypeFactory {
        @Getter
        private List<LinkedHashMap<String, Object>> resultList; //데이터 추출 결과리스트

        @Getter
        private String fileNamePrefix; // 엑셀 파일명

        @Getter
        private String sumTargetName; // 합계 처리하여 view 에 노출할 기준 컬럼명

        @Getter
        private int rowNum = 0;

        @Getter
        BigDecimal sumAmt = new BigDecimal(0);

        private SXSSFWorkbook wb; // turn off auto-flushing and accumulate all rows in memory

        CellStyle headerStyle;
        CellStyle dataCellTextStyle;
        CellStyle dataCellNumberStyle;
        SXSSFSheet sh;
        Set<String> columns = null;

        private StaticsTypeFactory(DataDownloadStaticsRequestDto dto, String dataType) throws BaseException {
            wb =  new SXSSFWorkbook(1000);
            headerStyle = getHeaderStyle(wb);
            dataCellTextStyle = getDataCellStyle(wb, "TEXT");
            dataCellNumberStyle = getDataCellStyle(wb, "NUMBER");
            sh = wb.createSheet("Result");
            parse(dto, dataType);
        }

        /**
         * 엑셀 헤더 스타일 return
         */
        private CellStyle getHeaderStyle(SXSSFWorkbook workbook) {
            // create style for header cells
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER); // 헤더는 모두 가운데 정렬
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 수직 가운데 정렬
            headerCellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex()); // 헤더 배경색
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 헤더 배경색 패턴
            // 헤더 셀 테두리 지정
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setWrapText(true); // 여러줄 표시
            // 헤더 셀 폰트 생성
            Font headerCellFont = workbook.createFont();
            headerCellFont.setColor(IndexedColors.WHITE.getIndex());
            headerCellFont.setFontHeightInPoints((short) 12);
            // headerCellFont.setBold(true); // 굵게 표시
            headerCellStyle.setFont(headerCellFont);

            return headerCellStyle;
        }

        /**
         * 엑셀 데이터셀 스타일 return
         */
        private CellStyle getDataCellStyle(SXSSFWorkbook workbook, String type) {
            CellStyle dataCellStyle = workbook.createCellStyle();
            Font dataCellFont = workbook.createFont();
            dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
            dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 수직 가운데 정렬
            dataCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex()); // 본문 데이터 배경색
            dataCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 본문 데이터 배경색 패턴
            // 본문 데이터 셀 테두리 지정
            dataCellStyle.setBorderTop(BorderStyle.THIN);
            dataCellStyle.setBorderRight(BorderStyle.THIN);
            dataCellStyle.setBorderBottom(BorderStyle.THIN);
            dataCellStyle.setBorderLeft(BorderStyle.THIN);
            dataCellStyle.setDataFormat((short) 0x31); // 문자
            // 본문 데이터 셀 폰트 생성
            dataCellFont.setColor(IndexedColors.BLACK.getIndex());
            dataCellFont.setFontHeightInPoints((short) 12);

            if(type.equals("NUMBER")) {
                dataCellStyle.setAlignment(HorizontalAlignment.RIGHT);
                //dataNumStyle.setDataFormat((short) 3); // 숫자
            }
            dataCellStyle.setFont(dataCellFont);

            return dataCellStyle;
        }

        String makeExcelFile() {
            String fileFullPath = null;
            String loginId = SessionUtil.getBosUserVO().getLoginId();
            String fileExtension = ".xlsx";
            String toDate = DateUtil.getCurrentDate("yyyyMMddHHmmss");
            String toDateYYYYMMDD = toDate.substring(0, 8);
            String publicRootStoragePath = SystemUtil.getAbsolutePathByProfile(publicRootLocation);
            try {
                fileFullPath = getCacheLocalFilePath(toDateYYYYMMDD + File.separator + fileNamePrefix + "_" + loginId + "_" + toDate + fileExtension);
                if(!new File(publicRootStoragePath + fileFullPath).canWrite()) {
                    new File(new File(publicRootStoragePath + fileFullPath).getParent()).mkdirs();
                }
                FileOutputStream out = new FileOutputStream(publicRootStoragePath + fileFullPath);
                wb.write(out);
                out.close();

                // dispose of temporary files backing this workbook on disk
                wb.dispose();
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("", e);
            }
            return fileFullPath;
        }

        void parse(DataDownloadStaticsRequestDto dto, String dataType) throws BaseException {
            ResultHandler<LinkedHashMap<String, Object>> resultHandler = context -> {
                LinkedHashMap<String, Object> result = context.getResultObject();
                try {

                    int cellNum = 0;
                    // 헤더 설정
                    if(rowNum == 0) {
                        SXSSFRow row = sh.createRow(rowNum);
                        columns = result.keySet();
                        for(String col: columns) {
                            SXSSFCell cell = row.createCell(cellNum++);
                            cell.setCellValue(col);
                            cell.setCellStyle(headerStyle);
                        }
                    }
                    SXSSFRow row = sh.createRow(rowNum + 1);
                    cellNum = 0;

                    Object sumValue = result.get(sumTargetName);

                    if(sumValue != null && sumValue.getClass().getName().contains("BigDecimal")) {
                        sumAmt = sumAmt.add((BigDecimal)sumValue);
                    } else if(sumValue != null && sumValue.getClass().getName().contains("BigInteger")) {
                        sumAmt = sumAmt.add(new BigDecimal((BigInteger)sumValue));
                    } else if(sumValue != null && sumValue.getClass().getName().matches(".*(Long|Integer)")) {
                        sumAmt = sumAmt.add(new BigDecimal(Long.parseLong(sumValue.toString())));
                    }

                    for(String col: columns) {
                        SXSSFCell cell = row.createCell(cellNum++);
                        cell.setCellStyle(dataCellNumberStyle);
                        if(result.get(col) != null && result.get(col).getClass().getName().contains("BigDecimal")) {
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(((BigDecimal) result.get(col)).doubleValue());
                        } else if(result.get(col) != null && result.get(col).getClass().getName().contains("BigInteger")) {
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(((BigInteger) result.get(col)).longValue());
                        } else if(result.get(col) != null && result.get(col).getClass().getName().matches(".*(Long|Integer)")) {
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(Long.parseLong(result.get(col).toString()));
                        } else {
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(StringUtil.nvl(result.get(col), ""));
                            cell.setCellStyle(dataCellTextStyle);
                        }
                    }

                    rowNum++;
//                    resultList.add(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            resultList = new ArrayList<>();
            if(dataType.equals(StaticsEnums.dataDownloadStaticsType.ONLINE_MEMBER_RESERVE_BALANCE.getCode())) {
                //적립금 회원 잔액
                this.fileNamePrefix = StaticsEnums.dataDownloadStaticsType.ONLINE_MEMBER_RESERVE_BALANCE.toString();
                this.sumTargetName = StaticsEnums.dataDownloadStaticsType.ONLINE_MEMBER_RESERVE_BALANCE.getSumTargetName();
                dataDownloadStaticsMapper.getOnlineMemberReserveBalance(dto, resultHandler);
            } else if(dataType.equals(StaticsEnums.dataDownloadStaticsType.EMPLOYEE_MEMBER_RESERVE_BALANCE.getCode())) {
                //적립금 임직원 잔액
                this.fileNamePrefix = StaticsEnums.dataDownloadStaticsType.EMPLOYEE_MEMBER_RESERVE_BALANCE.toString();
                this.sumTargetName = StaticsEnums.dataDownloadStaticsType.EMPLOYEE_MEMBER_RESERVE_BALANCE.getSumTargetName();
                dataDownloadStaticsMapper.getEmployeeMemberReserveBalance(dto, resultHandler);
            } else if(dataType.equals(StaticsEnums.dataDownloadStaticsType.SETTLEMENT_POINT.getCode())) {
                //적립금 정산
                this.fileNamePrefix = StaticsEnums.dataDownloadStaticsType.SETTLEMENT_POINT.toString();
                this.sumTargetName = StaticsEnums.dataDownloadStaticsType.SETTLEMENT_POINT.getSumTargetName();
                dataDownloadStaticsMapper.getSettlementPoint(dto, resultHandler);
            } else if(dataType.equals(StaticsEnums.dataDownloadStaticsType.SETTLEMENT_COUPON.getCode())) {
                //쿠폰 정산
                this.fileNamePrefix = StaticsEnums.dataDownloadStaticsType.SETTLEMENT_COUPON.toString();
                this.sumTargetName = StaticsEnums.dataDownloadStaticsType.SETTLEMENT_COUPON.getSumTargetName();
                dataDownloadStaticsMapper.getSettlementCoupon(dto, resultHandler);
            } else if(dataType.equals(StaticsEnums.dataDownloadStaticsType.INTERNAL_ACCOUNTING_COUPON_PAYMENT.getCode())) {
                //내부회계통제용 쿠폰 지급
                this.fileNamePrefix = StaticsEnums.dataDownloadStaticsType.INTERNAL_ACCOUNTING_COUPON_PAYMENT.toString();
                this.sumTargetName = StaticsEnums.dataDownloadStaticsType.INTERNAL_ACCOUNTING_COUPON_PAYMENT.getSumTargetName();
                dataDownloadStaticsMapper.getInternalAccountingCouponPayment(dto, resultHandler);
            } else if(dataType.equals(StaticsEnums.dataDownloadStaticsType.USE_COUPON_COST.getCode())) {
                //쿠폰비용 사용
                fileNamePrefix = StaticsEnums.dataDownloadStaticsType.USE_COUPON_COST.toString();
                sumTargetName = StaticsEnums.dataDownloadStaticsType.USE_COUPON_COST.getSumTargetName();
                dataDownloadStaticsMapper.getUseCouponCost(dto, resultHandler);
            } else if(dataType.equals(StaticsEnums.dataDownloadStaticsType.USE_RESERVE_COST.getCode())) {
                //적립금비용 사용
                this.fileNamePrefix = StaticsEnums.dataDownloadStaticsType.USE_RESERVE_COST.toString();
                this.sumTargetName = StaticsEnums.dataDownloadStaticsType.USE_RESERVE_COST.getSumTargetName();
                dataDownloadStaticsMapper.getUseReserveCost(dto, resultHandler);
            } else if(dataType.equals(StaticsEnums.dataDownloadStaticsType.EMPLOYEE_DISCOUNT_SUPPORT.getCode())) {
                //임직원 할인지원액
                this.fileNamePrefix = StaticsEnums.dataDownloadStaticsType.EMPLOYEE_DISCOUNT_SUPPORT.toString();
                this.sumTargetName = StaticsEnums.dataDownloadStaticsType.EMPLOYEE_DISCOUNT_SUPPORT.getSumTargetName();
                dataDownloadStaticsMapper.getEmployeeDiscountSupport(dto, resultHandler);
            } else if(dataType.equals(StaticsEnums.dataDownloadStaticsType.DISPOSAL_DATE_DISTRIBUTION_PERIOD.getCode())) {
                //용인물류 품목별 폐기 기준
                this.fileNamePrefix = StaticsEnums.dataDownloadStaticsType.DISPOSAL_DATE_DISTRIBUTION_PERIOD.toString();
                this.sumTargetName = StaticsEnums.dataDownloadStaticsType.DISPOSAL_DATE_DISTRIBUTION_PERIOD.getSumTargetName();
                dataDownloadStaticsMapper.getDisposalDateByDistributionPeriod(dto, resultHandler);
            } else if(dataType.equals(StaticsEnums.dataDownloadStaticsType.CUSTOMER_PRICE_COST.getCode())) {
                //객단가
                this.fileNamePrefix = StaticsEnums.dataDownloadStaticsType.CUSTOMER_PRICE_COST.toString();
                this.sumTargetName = StaticsEnums.dataDownloadStaticsType.CUSTOMER_PRICE_COST.getSumTargetName();
                dataDownloadStaticsMapper.getCustomerPriceCost(dto, resultHandler);
			}
        }
    }
}
