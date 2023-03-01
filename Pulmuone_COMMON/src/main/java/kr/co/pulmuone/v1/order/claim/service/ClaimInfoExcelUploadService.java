package kr.co.pulmuone.v1.order.claim.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimInfoExcelUploadMapper;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadFailVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadInfoVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadSuccessVo;
import kr.co.pulmuone.v1.order.claim.util.ClaimExcelUploadUtil;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelFailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimInfoExcelUploadService {
    private final ClaimInfoExcelUploadMapper claimInfoExcelUploadMapper;

    @Autowired
    private OrderExcelUploadFactory orderExcelUploadFactory;

    protected ApiResult<?> addClaimExcelUpload(MultipartFile file) throws Exception {
        if (ExcelUploadUtil.isFile(file) != true)
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

        ClaimExcelUploadUtil claimExcelUploadUtil = new ClaimExcelUploadUtil();
        String excelUploadType = ExcelUploadEnums.ExcelUploadType.CLAIM_EXCEL_UPLOAD.getCode();
        //업로드 현황 정보 설정
        ClaimInfoExcelUploadInfoVo infoVo = claimExcelUploadUtil.setClaimExcelInfo();

        // Return 값 설정
        ClaimInfoExcelUploadResponseDto responseDto = new ClaimInfoExcelUploadResponseDto();

        // Excel Import 정보 -> Dto 변환
        Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
        if (uploadSheet == null)
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);


        // Excel 데이터 Mapping
        List<ClaimInfoExcelUploadDto> excelList = (List<ClaimInfoExcelUploadDto>) orderExcelUploadFactory.setExcelData(excelUploadType, uploadSheet);

        if (excelList.isEmpty()) {
            return ApiResult.result(responseDto, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);
        }
        //업로드 현황 1차 저장
        infoVo.setTotalCount(excelList.size());
        infoVo.setUploadJsonData(new Gson().toJson(excelList));
        infoVo.setFileNm(file.getOriginalFilename());
        claimInfoExcelUploadMapper.addClaimExcelInfo(infoVo);

        // 항목별 검증 진행
        excelList = (List<ClaimInfoExcelUploadDto>) orderExcelUploadFactory.getDefaultRowItemValidator(excelUploadType, excelList);

        //업로드 현황 설정
        List<ClaimInfoExcelUploadSuccessVo> successVoList = new ArrayList<>();
        List<ClaimInfoExcelUploadFailVo> failVoList = new ArrayList<>();


        System.out.println("excelList : " + excelList);
        for (ClaimInfoExcelUploadDto dto : excelList) {
            if (dto.isSuccess()) {
                successVoList.add(new ClaimInfoExcelUploadSuccessVo(dto));
            } else {
                failVoList.add(new ClaimInfoExcelUploadFailVo(dto));
            }
        }


        // 업데이트 데이터 세팅
        infoVo = claimExcelUploadUtil.setClaimExcelUpdateInfo(infoVo, successVoList.size(), failVoList.size());

        // 저장
        claimInfoExcelUploadMapper.putClaimExcelInfo(infoVo);

        if (successVoList.size() > 0) {

            claimInfoExcelUploadMapper.addClaimExcelSuccess(infoVo.getIfClaimExcelInfoId(), successVoList);

            //
            //outmallOrderRegistrationBiz.setBindOrderOrder(infoVo.getIfOutmallExcelInfoId());

        }
        if (failVoList.size() > 0) {
            claimInfoExcelUploadMapper.addClaimExcelFail(infoVo.getIfClaimExcelInfoId(), failVoList);
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

    protected ExcelDownloadDto getClaimUploadExcelDownload(ClaimInfoExcelUploadFailRequestDto dto){
        String excelFileName = "주문 클레임 업로드 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

        ExcelWorkSheetDto firstWorkSheetDto = makeWorkSheetDto("UPLOAD");

        List<ClaimInfoExcelUploadFailVo> itemList = new ArrayList<>();
        ClaimInfoExcelUploadInfoVo claimInfoExcelUploadInfoVo = claimInfoExcelUploadMapper.getClaimExcelInfo(dto);

        if(ObjectUtils.isNotEmpty(claimInfoExcelUploadInfoVo)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ClaimInfoExcelUploadFailVo>>() {}.getType();
            itemList = gson.fromJson(claimInfoExcelUploadInfoVo.getUploadJsonData(), type);

            if(StringUtils.isNotEmpty(claimInfoExcelUploadInfoVo.getFileNm())) {
                String filename = claimInfoExcelUploadInfoVo.getFileNm();
                excelFileName = filename.substring(0, filename.lastIndexOf("."));
            }
        }

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
        return excelDownloadDto;
    }

    protected ExcelDownloadDto getClaimUploadFailExcelDownload(ClaimInfoExcelUploadFailRequestDto dto){
        String excelFileName = "주문 클레임 업로드 실패 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = makeWorkSheetDto("FAIL");

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<ClaimInfoExcelUploadFailVo> itemList = claimInfoExcelUploadMapper.getClaimFailExcelDownload(dto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
        return excelDownloadDto;
    }

    protected ExcelDownloadDto getClaimUpdateFailExcelDownload(ClaimInfoExcelUploadFailRequestDto dto){
        String excelFileName = "주문 클레임 업데이트 실패 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

        ExcelWorkSheetDto firstWorkSheetDto = makeWorkSheetDto("FAIL");

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<ClaimInfoExcelUploadFailVo> itemList = claimInfoExcelUploadMapper.getClaimUpdateFailExcelDownload(dto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
        return excelDownloadDto;
    }

    protected ClaimInfoExcelUploadListResponseDto getClaimExcelInfoList(ClaimInfoExcelUploadListRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<ClaimInfoExcelUploadInfoVo> result = claimInfoExcelUploadMapper.getClaimExcelInfoList(dto);

        return ClaimInfoExcelUploadListResponseDto.builder()
                .total((int) result.getTotal())
                .rows(result.getResult())
                .build();
    }

    /**
     * makeWorkSheetDto 엑셀시트 생성
     * @param type FAIL 업로드 실패 엑셀 생성 / 그외에는 업로드 엑셀형식과 동일
     * @return ExcelWorkSheetDto
     */
    private ExcelWorkSheetDto makeWorkSheetDto(String type) {
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름
        ExcelWorkSheetDto firstWorkSheetDto;

        Integer[] widthListOfFirstWorksheet;
        String[] alignListOfFirstWorksheet, propertyListOfFirstWorksheet, firstHeaderListOfFirstWorksheet, cellTypeListOfFirstWorksheet;
        if("FAIL".equals(type)) {
            /*
             * 컬럼별 width 목록 : 단위 pixel
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
             */
            widthListOfFirstWorksheet = new Integer[]{ //
                    400, 200, 200, 200, 200, 200, 200, 200
            };

            /*
             * 본문 데이터 컬럼별 정렬 목록
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
             * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
             */
            alignListOfFirstWorksheet = new String[]{ //
                    "left", "center", "center", "center", "center", "center", "center", "center"
            };

            /*
             * 본문 데이터 컬럼별 데이터 property 목록
             * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
             */
            propertyListOfFirstWorksheet = new String[]{ //
                    "failMessage", "odid", "odOrderDetlSeq", "claimCnt", "psClaimBosId", "returnsYn", "consultMsg", "claimStatusTp"
            };

            // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
            firstHeaderListOfFirstWorksheet = new String[]{ // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                    "실패사유", "주문번호", "주문상세순번", "클레임수량", "몰클레임사유", "반품회수여부", "고객상담내용", "클레임구분"
            };

            cellTypeListOfFirstWorksheet = new String[]{
                    "String", "String", "int", "int", "String", "String", "String", "String"
            };
        } else {
            /*
             * 컬럼별 width 목록 : 단위 pixel
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
             */
            widthListOfFirstWorksheet = new Integer[]{ //
                    200, 200, 200, 200, 200, 200, 200
            };

            /*
             * 본문 데이터 컬럼별 정렬 목록
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
             * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
             */
            alignListOfFirstWorksheet = new String[]{ //
                    "center", "center", "center", "center", "center", "center", "center"
            };

            /*
             * 본문 데이터 컬럼별 데이터 property 목록
             * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
             */
            propertyListOfFirstWorksheet = new String[]{ //
                    "odid", "odOrderDetlSeq", "claimCnt", "psClaimBosId", "returnsYn", "consultMsg", "claimStatusTp"
            };

            // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
            firstHeaderListOfFirstWorksheet = new String[]{ // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                    "주문번호", "주문상세순번", "클레임수량", "몰클레임사유", "반품회수여부", "고객상담내용", "클레임구분"
            };

            cellTypeListOfFirstWorksheet = new String[]{
                    "String", "int", "int", "String", "String", "String", "String"
            };
        }

        // 워크시트 DTO 생성 후 정보 세팅
        firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .cellTypeList(cellTypeListOfFirstWorksheet)
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        return firstWorkSheetDto;
    }

}
