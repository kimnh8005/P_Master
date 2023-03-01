package kr.co.pulmuone.v1.promotion.advertising.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.promotion.advertising.dto.*;
import kr.co.pulmuone.v1.promotion.advertising.dto.vo.AdvertisingExternalVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class PromotionAdvertisingBizImpl implements PromotionAdvertisingBiz {

    @Autowired
    private PromotionAdvertisingService promotionAdvertisingService;

    @Override
    public AdvertisingExternalListResponseDto getAdvertisingExternalList(AdvertisingExternalListRequestDto dto) throws Exception {
        return promotionAdvertisingService.getAdvertisingExternalList(dto);
    }

    @Override
    public AdvertisingExternalResponseDto getAdvertisingExternal(AdvertisingExternalRequestDto dto) throws Exception {
        return promotionAdvertisingService.getAdvertisingExternal(dto);
    }

    @Override
    public ExcelDownloadDto getAdvertisingExternalListExcelDownload(AdvertisingExternalListRequestDto dto) throws Exception {
        String excelFileName = "외부광고 코드관리"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 200, 150, 150, 150,
                150, 150, 150, 600, 600
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center",
                "center", "center", "center", "left", "left"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {
                "advertisingName", "pmAdExternalCd", "source", "medium", "campaign",
                "term", "content", "useYn", "redirectUrl", "advertisingUrl"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "광고명", "광고 ID", "매체(source)", "구좌(medium)", "캠페인(campaign)",
                "키워드(term)", "콘텐츠(content)", "사용여부(Y/N)", "Redirect URL", "제휴 광고 URL"
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
        AdvertisingExternalListResponseDto responseDto = promotionAdvertisingService.getAdvertisingExternalList(dto);
        List<AdvertisingExternalVo> itemList = responseDto.getRows();

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public ApiResult<?> addAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception {
        return promotionAdvertisingService.addAdvertisingExternal(dto);
    }

    @Override
    public void putAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception {
        promotionAdvertisingService.putAdvertisingExternal(dto);
    }

    @Override
    public ApiResult<?> addAdvertisingExternalExcelUpload(MultipartFile file) throws Exception {
        UserVo userVo = SessionUtil.getBosUserVO();

        if (!ExcelUploadUtil.isFile(file))
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

        // Excel Import 정보 -> Dto 변환
        Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
        if (uploadSheet == null)
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);

        List<AddAdvertisingExternalRequestDto> excelList = new ArrayList<>();
        for (int i = uploadSheet.getFirstRowNum() + 1; i <= uploadSheet.getLastRowNum(); i++) {
            Row row = uploadSheet.getRow(i);
            Iterator<Cell> cellItr = row.cellIterator();

            while (cellItr.hasNext()) {
                XSSFCell cell = (XSSFCell) cellItr.next();
            }

            // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
            if (StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))
                    || StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))
                    || StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(2)))
                    || StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(3)))
            ) {
                return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.UPLOAD_FAIL);
            }

            excelList.add(AddAdvertisingExternalRequestDto.builder()
                    .pmAdExternalCd(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.AdvertisingUploadCols.pmAdExternalCd.getColNum())))
                    .advertisingName(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.AdvertisingUploadCols.advertisingName.getColNum())))
                    .source(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.AdvertisingUploadCols.source.getColNum())))
                    .medium(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.AdvertisingUploadCols.medium.getColNum())))
                    .campaign(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.AdvertisingUploadCols.campaign.getColNum())))
                    .content(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.AdvertisingUploadCols.content.getColNum())))
                    .term(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.AdvertisingUploadCols.term.getColNum())))
                    .redirectUrl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.AdvertisingUploadCols.redirectUrl.getColNum())))
                    .useYn(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.AdvertisingUploadCols.useYn.getColNum())))
                    .userId(userVo != null ? userVo.getUserId() : "")
                    .build());
        }

        if (excelList.isEmpty()) {
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);
        }

        promotionAdvertisingService.addAdvertisingExternalList(excelList);
        return ApiResult.success();
    }

    @Override
    public GetCodeListResponseDto getAdvertisingType(AdvertisingTypeRequestDto dto) throws Exception {
        return promotionAdvertisingService.getAdvertisingType(dto);
    }

    @Override
    public boolean isExistPmAdExternalCd(String pmAdExternalCd) throws Exception {
        return promotionAdvertisingService.isExistPmAdExternalCd(pmAdExternalCd);
    }

}