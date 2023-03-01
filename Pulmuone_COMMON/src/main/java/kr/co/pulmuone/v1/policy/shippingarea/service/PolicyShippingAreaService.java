package kr.co.pulmuone.v1.policy.shippingarea.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.policy.shippingarea.PolicyShippingAreaMapper;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.policy.shippingarea.dto.*;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaExcelUploadFailVo;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaExcelUploadInfoVo;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaExcelUploadSuccVo;
import kr.co.pulmuone.v1.policy.shippingarea.util.ShippingAreaExcelUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyShippingAreaService {

    private final PolicyShippingAreaMapper policyShippingAreaMapper;

    @Autowired
    private OrderExcelUploadFactory orderExcelUploadFactory;

    // 도서산간 / 배송불가 권역 엑셀 업로드
    protected ApiResult<?> addShippingAreaExcelUpload(MultipartFile file, ShippingAreaListRequestDto shippingAreaListRequestDto) throws Exception {

        // 키워드 중복체크
        int checkDuplicateKeyword = policyShippingAreaMapper.getShippingAreaKeyword(shippingAreaListRequestDto.getKeyword());

        if(checkDuplicateKeyword > 0)
            return ApiResult.result(null, ExcelUploadValidateEnums.ValidateType.DUPLICATE_KEYWORD);

        if (ExcelUploadUtil.isFile(file) != true)
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

        ShippingAreaExcelUploadUtil shippingAreaExcelUploadUtil = new ShippingAreaExcelUploadUtil();
        String excelUploadType = ExcelUploadEnums.ExcelUploadType.SHIPPING_AREA_EXCEL_UPLOAD.getCode();

        // 엑셀 정보 설정
        ShippingAreaExcelUploadInfoVo infoVo = shippingAreaExcelUploadUtil.setShippingAreaExcelInfo();

        // return 값 설정
        ShippingAreaExcelUploadResponseDto responseDto = new ShippingAreaExcelUploadResponseDto();

        // Excel Import 정보 -> Dto 변환
        Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
        if (uploadSheet == null)
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);

        // Excel 데이터 Mapping
        List<ShippingAreaExcelUploadDto> excelList = (List<ShippingAreaExcelUploadDto>) orderExcelUploadFactory.setExcelData(excelUploadType, uploadSheet);

        if(excelList.isEmpty()){
            return ApiResult.result(responseDto, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);
        }

        List<ShippingAreaExcelUploadDto> uploadList = new ArrayList<>();
        for(ShippingAreaExcelUploadDto dto : excelList) {
            dto.setUndeliverableTp(shippingAreaListRequestDto.getUndeliverableTp());
            uploadList.add(dto);
        }

        // 항목별 검증 진행
        uploadList = (List<ShippingAreaExcelUploadDto>) orderExcelUploadFactory.getDefaultRowItemValidator(excelUploadType, uploadList);

        // 업로드 실패내역 저장
        List<ShippingAreaExcelUploadFailVo> failVoList = new ArrayList<>();     // 실패시 PS_SHIPPING_AREA_EXCEL_FAIL 에 저장.
        List<ShippingAreaExcelUploadSuccVo> successVoList = new ArrayList<>();  // 정상 데이터는 PS_SHIPPING_AREA에 저장
        List<ShippingAreaExcelUploadSuccVo> delVoList = new ArrayList<>();      // 정상 데이터는 PS_SHIPPING_AREA에서 삭제

        List<Long> shippingAreaExcelInfoIds = new ArrayList<>();                // 마스터 PK 저장

        for(ShippingAreaExcelUploadDto dto : uploadList) {

            dto.setCreateId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));        // 등록자 아이디 추가

            if(dto.isSuccess()){
                // 사용여부 : 등록인 경우 insert
                if(BaseEnums.EnumUseYn.INSERT.getCodeName().equals(dto.getUseYn())){
                    successVoList.add(new ShippingAreaExcelUploadSuccVo(dto));
                }
                // 사용여부 : 삭제인 경우 delete
                if(BaseEnums.EnumUseYn.DELETE.getCodeName().equals(dto.getUseYn())){
                    delVoList.add(new ShippingAreaExcelUploadSuccVo(dto));
                }
            } else {
                failVoList.add(new ShippingAreaExcelUploadFailVo(dto));
            }
        }

        // 마스터 정보 저장
        infoVo.setTotalCnt(excelList.size());
        infoVo.setUndeliverableTp(shippingAreaListRequestDto.getUndeliverableTp());
        infoVo.setKeyword(shippingAreaListRequestDto.getKeyword());
        infoVo.setCreateId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));
        infoVo.setStorageFileNm(shippingAreaListRequestDto.getStorageFileNm());
        infoVo.setFileNm(file.getOriginalFilename());

        policyShippingAreaMapper.addShippingAreaExcelInfo(infoVo);                  // 업로드 마스터 생성

        // 삭제 등록 발생시 업데이트 위한 생성된 마스터 키값 저장
        shippingAreaExcelInfoIds.add(infoVo.getPsShippingAreaExcelInfoId());


        // 성공건수가 있는 경우 도서산간/배송불가 추가.
        if(successVoList.size() > 0) {
            policyShippingAreaMapper.addShippingAreaInfo(infoVo.getPsShippingAreaExcelInfoId(), successVoList);
            shippingAreaExcelInfoIds.add(infoVo.getPsShippingAreaExcelInfoId());
        }

        // 삭제
        int delCount = 0;
        for(ShippingAreaExcelUploadSuccVo vo : delVoList){

            ShippingAreaExcelUploadFailRequestDto requestDto = new ShippingAreaExcelUploadFailRequestDto();
            requestDto.setZipCd(vo.getZipCd());
            requestDto.setUndeliverableTp(vo.getUndeliverableTp());

            // 삭제 위한 키값 수집
            Long shippingAreaExcelInfoId = policyShippingAreaMapper.getShippingAreaExcelInfoId(requestDto);

            // 조회된 값이 있는 경우.
            if(shippingAreaExcelInfoId != null){
                shippingAreaExcelInfoIds.add(shippingAreaExcelInfoId);
                requestDto.setPsShippingAreaExcelInfoId(shippingAreaExcelInfoId);

                // 삭제
                policyShippingAreaMapper.delShippingAreaInfo(requestDto);
                delCount++;
            } else {
                // 삭제할 우편번호가 존재하지 않을 경우 실패이력에 추가.
                ShippingAreaExcelUploadFailVo failVo = new ShippingAreaExcelUploadFailVo();
                failVo.setZipCd(requestDto.getZipCd());
                failVo.setUndeliverableTp(requestDto.getUndeliverableTp());
                failVo.setFailMsg(ExcelUploadValidateEnums.ValidateType.NO_MATCH_ZIP_CODE.getMessage());       // 대상 우편번호가 없음.
                failVoList.add(failVo);
            }
        }

        // 실패건수가 있는 경우 실패이력 추가.
        if(failVoList.size() > 0) {
            policyShippingAreaMapper.addShippingAreaExcelFail(infoVo.getPsShippingAreaExcelInfoId(), failVoList);
            shippingAreaExcelInfoIds.add(infoVo.getPsShippingAreaExcelInfoId());
        }

        // 성공건수, 실패건수 업데이트
        infoVo = shippingAreaExcelUploadUtil.setShippingAreaExcelResultUpdate(infoVo);

        for(Long psShippingAreaExcelInfoId : shippingAreaExcelInfoIds){
            infoVo.setPsShippingAreaExcelInfoId(psShippingAreaExcelInfoId);
            infoVo.setAlternateDeliveryTp(BaseEnums.AlternateDeliveryType.Y.getCode());
            policyShippingAreaMapper.putShippingAreaExcelInfo(infoVo);
        }

        int totalCnt = successVoList.size() + failVoList.size() + delCount;

        responseDto.setTotalCount(totalCnt);
        responseDto.setSuccessCount(successVoList.size());
        responseDto.setPsShippingAreaExcelInfoId(infoVo.getPsShippingAreaExcelInfoId());
        responseDto.setFailCount(failVoList.size());
        responseDto.setDeleteCount(delCount);

        if (responseDto.getFailCount() > 0) {
            List<String> failMessageList = new ArrayList<>();
            failVoList.stream()
                    .forEach(item -> {
                        String[] arr = item.getFailMsg().split(Constants.ARRAY_SEPARATORS);
                        for (String str : arr) {
                            if (!"".equals(str.trim())) {
                                failMessageList.add(str);
                            }
                        }
                    });

            responseDto.setFailMessage(
                    failMessageList.stream()
                            .distinct()
                            .collect(Collectors.joining("<br/>"))
            );
            return ApiResult.result(responseDto, ExcelUploadValidateEnums.UploadResponseCode.UPLOAD_FAIL);
        }
        return ApiResult.success(responseDto);
    }

    // 업로드한 엑셀 내역 조회
    protected ShippingAreaExcelUploadInfoVo getUploadShippingAreaInfo(ShippingAreaExcelUploadDto shippingAreaExcelUploadDto) {
        ShippingAreaExcelUploadInfoVo shippingAreaExcelUploadInfoVo = policyShippingAreaMapper.getUploadShippingAreaExcelInfo(shippingAreaExcelUploadDto);
        return shippingAreaExcelUploadInfoVo;
    }

    // 엑셀 적용 내역 조회
    protected ShippingAreaExcelUploadListResponseDto getPolicyShippingAreaList(ShippingAreaExcelUploadListRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<ShippingAreaExcelUploadInfoVo> result = policyShippingAreaMapper.getShippingAreaExcelInfoList(dto);

        return ShippingAreaExcelUploadListResponseDto.builder()
                .total((int) result.getTotal())
                .rows(result.getResult())
                .build();
    }

    // 도서산간/배송불가 우편번호 조회
    protected ShippingAreaExcelUploadListResponseDto getPolicyShippingAreaInfoList(ShippingAreaExcelUploadListRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<ShippingAreaExcelUploadInfoVo> result = policyShippingAreaMapper.getShippingAreaInfoList(dto);

        return ShippingAreaExcelUploadListResponseDto.builder()
                .total((int) result.getTotal())
                .rows(result.getResult())
                .build();
    }

    // 적용된 엑셀파일 다운로드
    protected ExcelDownloadDto getShippingAreaInfoExcelDownload(ShippingAreaExcelUploadFailRequestDto dto) {
        String excelFileName = "도서산간_배송불가 업로드 성공 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        // 우편번호 / 권역 / 등록키워드 / 대체배송 / 등록자 / 등록일자
        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                100, 100, 100, 400, 100, 200
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "zipCd", "undeliverableNm", "alternateDeliveryTp", "keyword", "createNm", "createDt"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "우편번호", "권역", "대체배송", "키워드 등록", "등록자", "등록일자"
        };

        String[] cellTypeListOfFirstWorksheet = {
                "String", "String", "String", "String", "String", "String"
        };

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .cellTypeList(cellTypeListOfFirstWorksheet)
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0,firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<ShippingAreaExcelUploadInfoVo> itemList = policyShippingAreaMapper.getShippingAreaInfoExcelDownload(dto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
        return excelDownloadDto;
    }

    // 엑셀 실패내역 다운로드
    protected ExcelDownloadDto getShippingAreaFailExcelDownload(ShippingAreaExcelUploadFailRequestDto dto) {
        String excelFileName = "도서산간_배송불가 업로드 실패 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        // 우편번호 / 권역 / 대체배송 / 등록키워드 / 실패사유
        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                100, 100, 100, 400, 500
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "left", "center", "center", "left", "left"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "zipCd", "undeliverableNm", "alternateDeliveryTp", "keyword", "failMsg"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "우편번호", "권역", "대체배송여부", "등록 키워드", "실패사유"
        };

        String[] cellTypeListOfFirstWorksheet = {
                "String", "String", "String", "String", "String"
        };

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .cellTypeList(cellTypeListOfFirstWorksheet)
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0,firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<ShippingAreaExcelUploadFailVo> itemList = policyShippingAreaMapper.getShippingAreaUpdateFailExcelDownload(dto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
        return excelDownloadDto;
    }

    // 도서산간/배송불가 엑셀 업로드 정보 일괄 삭제
    protected int delShippingAreaInfo(ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto) throws Exception {
        return policyShippingAreaMapper.delShippingAreaInfo(shippingAreaExcelUploadFailRequestDto);
    }

    // 도서산간/배송불가 엑셀 업로드 정보 일괄 삭제
    protected int delShippingAreaExcelInfo(ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto) throws Exception {
        return policyShippingAreaMapper.delShippingAreaExcelInfo(shippingAreaExcelUploadFailRequestDto);
    }

    // 도서산간/배송불가 엑셀 업로드 정보 일괄 삭제
    protected int delShippingAreaExcelFail(ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto) throws Exception {
        return policyShippingAreaMapper.delShippingAreaExcelFail(shippingAreaExcelUploadFailRequestDto);
    }

    // 등록된 키워드 조회
    protected int getShippingAreaKeyword(String keyword) {
        return policyShippingAreaMapper.getShippingAreaKeyword(keyword);
    }

    // 등록된 우편번호 조회
    protected int getShippingAreaZipCd(String zipCd, String undeliverableTp) {
        return policyShippingAreaMapper.getShippingAreaZipCd(zipCd, undeliverableTp);
    }
}
