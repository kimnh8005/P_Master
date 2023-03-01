package kr.co.pulmuone.v1.order.ifday.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.excel.util.OrderExcelSetData;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.order.ifday.IfDayExcelMapper;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.ifday.dto.*;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelFailVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelInfoVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelSuccessVo;
import kr.co.pulmuone.v1.order.ifday.util.IfDayChangeUtil;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelFailRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelListRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelListResponseDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelFailVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelSuccessVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IfDayExcelService {
    private final IfDayExcelMapper ifDayExcelMapper;

    @Autowired
    private OrderExcelUploadFactory orderExcelUploadFactory;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    protected ApiResult<?> addIfDayExcelUpload(MultipartFile file) throws Exception {
        if (ExcelUploadUtil.isFile(file) != true)
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

        IfDayChangeUtil ifDayChangeUtil = new IfDayChangeUtil();
        String excelUploadType = ExcelUploadEnums.ExcelUploadType.INTERFACE_DAY_CHANGE.getCode();
        //업로드 현황 정보 설정
        IfDayExcelInfoVo infoVo = ifDayChangeUtil.setIfDayExcelInfo();

        // Return 값 설정
        IfDayChangeResponseDto responseDto = new IfDayChangeResponseDto();

        // Excel Import 정보 -> Dto 변환
        Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
        if (uploadSheet == null)
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);


        // Excel 데이터 Mapping
        List<IfDayChangeDto> excelList = (List<IfDayChangeDto>) orderExcelUploadFactory.setExcelData(excelUploadType, uploadSheet);

        if (excelList.isEmpty()) {
            return ApiResult.result(responseDto, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);
        }

        // 원본 데이터 JSON 변환
        List<IfDayChangeDto> resultList = new ArrayList<>();
        for(IfDayChangeDto ifDayChangeDto : excelList){
            resultList.add(OBJECT_MAPPER.convertValue(ifDayChangeDto, IfDayChangeDto.class));
        }

        Gson gson = new Gson();
        String uploadJsonData = gson.toJson(resultList);

        //업로드 현황 1차 저장
        infoVo.setUploadJsonData(uploadJsonData);
        infoVo.setTotalCount(excelList.size());
        infoVo.setFileNm(file.getOriginalFilename());
        ifDayExcelMapper.addIfDayExcelInfo(infoVo);

        // 항목별 검증 진행
        excelList = (List<IfDayChangeDto>) orderExcelUploadFactory.getDefaultRowItemValidator(excelUploadType, excelList);

        //업로드 현황 설정
        /*
        List<IfDayExcelSuccessVo> successVoList = new ArrayList<>();
        List<IfDayExcelFailVo> failVoList = new ArrayList<>();

        for (IfDayChangeDto dto : excelList) {
            if (dto.isSuccess()) {
                successVoList.add(new IfDayExcelSuccessVo(dto));
            } else {
                failVoList.add(new IfDayExcelFailVo(dto));
            }
        }

         */

        List<IfDayExcelSuccessVo> successVoList = new ArrayList<>();
        List<IfDayExcelSuccessVo> tmpSuccessVoList = new ArrayList<>();
        List<IfDayExcelFailVo> failVoList = new ArrayList<>();
        Map<String, String> checkFailMap = new HashMap<>();
        for (IfDayChangeDto dto : excelList) {
            if (dto.isSuccess()) {
                tmpSuccessVoList.add(new IfDayExcelSuccessVo(dto));
            } else {

                //failVoList.add(new OutMallExcelFailVo(dto));
                if(!checkFailMap.containsKey(dto.getOdid())){
                    checkFailMap.put(dto.getOdid(), dto.getOdid());
                }
            }
        }

        for (IfDayChangeDto dto : excelList) {
            if (checkFailMap.containsKey(dto.getOdid())) {
                //dto.setFailMessage("");
                dto.setFailType(OutmallEnums.OutmallDownloadType.UPLOAD.getCode());
                failVoList.add(new IfDayExcelFailVo(dto));
            } else {
                successVoList.add(new IfDayExcelSuccessVo(dto));
            }
        }







        // 업데이트 데이터 세팅
        infoVo = ifDayChangeUtil.setIfDayExcelUpdateInfo(infoVo, successVoList.size(), failVoList.size());

        // 저장
        ifDayExcelMapper.putIfDayExcelInfo(infoVo);

        if (successVoList.size() > 0) {

            ifDayExcelMapper.addIfDayExcelSuccess(infoVo.getIfIfDayExcelInfoId(), successVoList);

            //
            //outmallOrderRegistrationBiz.setBindOrderOrder(infoVo.getIfOutmallExcelInfoId());

        }
        if (failVoList.size() > 0) {
            ifDayExcelMapper.addIfDayExcelFail(infoVo.getIfIfDayExcelInfoId(), failVoList);
        }

        responseDto.setTotalCount(infoVo.getTotalCount());
        responseDto.setSuccessCount(infoVo.getSuccessCount());
        responseDto.setFailCount(infoVo.getFailCount());

        if (responseDto.getFailCount() > 0) {
            List<String> failMessageList = new ArrayList<>();
            failVoList.stream()
                    .forEach(item -> {
                        String[] arr = item.getFailMessage().split(Constants.ARRAY_SEPARATORS);
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

    protected ExcelDownloadDto getIfDayFailExcelDownload(IfDayExcelFailRequestDto dto){
        String excelFileName = "주문 I/F 일자 변경 엑셀업로드 실패 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름


        // 엑셀업로드 정보 실패 정보 조회
        IfDayExcelInfoVo ifDayExcelInfoVo = ifDayExcelMapper.getIfDayExcelInfo(dto);

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                400, 200, 200, 200
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "left", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "failMessage",  "odid", "odOrderDetlSeq", "ifDay"
        };


        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "실패사유", "주문번호", "주문상세순번", "I/F 일자"
        };

        String[] cellTypeListOfFirstWorksheet = {  "String", "String", "int", "String" };



        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<IfDayExcelFailVo> itemList = new ArrayList<>();
        if (OutmallEnums.OutmallDownloadType.ORG_UPLOAD.getCode().equals(dto.getFailType())) {
            excelFileName = "주문 I/F 일자 변경 업로드 내역";
            if(StringUtils.isNotEmpty(ifDayExcelInfoVo.getFileNm())) {
                String filename = ifDayExcelInfoVo.getFileNm();
                excelFileName = filename.substring(0, filename.lastIndexOf("."));
            }
            List<String> newAlignListOfFirstWorksheet = new ArrayList<String>();
            Collections.addAll(newAlignListOfFirstWorksheet, alignListOfFirstWorksheet);
            newAlignListOfFirstWorksheet.remove(0);
            alignListOfFirstWorksheet = newAlignListOfFirstWorksheet.toArray(new String[newAlignListOfFirstWorksheet.size()]);

            List<String> newPropertyListOfFirstWorksheet = new ArrayList<String>();
            Collections.addAll(newPropertyListOfFirstWorksheet, propertyListOfFirstWorksheet);
            newPropertyListOfFirstWorksheet.remove(0);
            propertyListOfFirstWorksheet = newPropertyListOfFirstWorksheet.toArray(new String[newPropertyListOfFirstWorksheet.size()]);

            List<String> newFirstHeaderListOfFirstWorksheet = new ArrayList<String>();
            Collections.addAll(newFirstHeaderListOfFirstWorksheet, firstHeaderListOfFirstWorksheet);
            newFirstHeaderListOfFirstWorksheet.remove(0);
            firstHeaderListOfFirstWorksheet = newFirstHeaderListOfFirstWorksheet.toArray(new String[newFirstHeaderListOfFirstWorksheet.size()]);

            List<String> newCellTypeListOfFirstWorksheet = new ArrayList<String>();
            Collections.addAll(newCellTypeListOfFirstWorksheet, cellTypeListOfFirstWorksheet);
            newCellTypeListOfFirstWorksheet.remove(0);
            cellTypeListOfFirstWorksheet = newCellTypeListOfFirstWorksheet.toArray(new String[newCellTypeListOfFirstWorksheet.size()]);

            if (!"".equals(ifDayExcelInfoVo.getUploadJsonData())) {
                Gson gson2 = new Gson();
                Type type = new TypeToken<List<IfDayExcelFailVo>>() {
                }.getType();
                List<IfDayExcelFailVo> uploadJsonDataList = gson2.fromJson(ifDayExcelInfoVo.getUploadJsonData(), type);
                for (IfDayExcelFailVo x : uploadJsonDataList) {
                    itemList.add(x);
                }
            }
        } else {
            if (OutmallEnums.OutmallDownloadType.BATCH.getCode().equals(dto.getFailType())) {
                excelFileName = "주문 I/F 일자 변경 엑셀업데이트 실패 내역";
            }
            itemList = ifDayExcelMapper.getIfDayFailExcelDownload(dto);
        }

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


            firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

            excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
            return excelDownloadDto;
    }

    protected IfDayExcelListResponseDto getIfDayExcelInfoList(IfDayExcelListRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<IfDayExcelInfoVo> result = ifDayExcelMapper.getIfDayExcelInfoList(dto);

        return IfDayExcelListResponseDto.builder()
                .total((int) result.getTotal())
                .rows(result.getResult())
                .build();
    }

}
