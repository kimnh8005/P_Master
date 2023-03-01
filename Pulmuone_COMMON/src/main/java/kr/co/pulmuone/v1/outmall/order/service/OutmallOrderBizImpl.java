package kr.co.pulmuone.v1.outmall.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderDeliveryListDto;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.excel.util.OrderExcelSetData;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.*;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.outmall.order.dto.*;
import kr.co.pulmuone.v1.outmall.order.dto.vo.*;
import kr.co.pulmuone.v1.outmall.order.util.OutmallOrderUtil;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CoverageVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class OutmallOrderBizImpl implements OutmallOrderBiz {
    @Autowired
    private OutmallOrderService outmallOrderService;

    @Autowired
    private GoodsSearchBiz goodsSearchBiz;

    @Autowired
    private OrderExcelUploadFactory orderExcelUploadFactory;

    @Autowired
    private OutmallOrderRegistrationBiz outmallOrderRegistrationBiz;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    @Override
    public ApiResult<?> getCollectionMallInterfaceList(CollectionMallInterfaceListRequestDto dto) {
        return ApiResult.success(outmallOrderService.getCollectionMallInterfaceList(dto));
    }

    @Override
    public ApiResult<?> putCollectionMallInterfaceProgress(String processCode, Long ifEasyadminInfoId) {
        Long adminId = null;


        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null && processCode.equals(ExcelUploadValidateEnums.ProcessCode.I.getCode())) {

            adminId = Long.valueOf(userVo.getUserId());
        }

        outmallOrderService.putCollectionMallInterfaceProgress(processCode, adminId, ifEasyadminInfoId);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> putCollectionMallInterfaceProgressList(CollectionMallInterfaceProgressRequestDto dto) {
        outmallOrderService.putCollectionMallInterfaceProgressList(dto);
        return ApiResult.success();
    }

    @Override
    public ExcelDownloadDto getCollectionMallFailExcelDownload(CollectionMallInterfaceFailRequestDto dto) {
        String excelFileName = "수집몰 연동 실패 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름
        
        if(OutmallEnums.OutmallFailType.BATCH.getCode().equals(dto.getFailType())){
            excelFileName = "수집몰 주문생성 실패 내역"; 
        } else if(OutmallEnums.OutmallFailType.TRANS.getCode().equals(dto.getFailType())) {
            excelFileName = "수집몰 송장연동 실패 내역";
        }

        ExcelWorkSheetDto firstWorkSheetDto = null;

        if(OutmallEnums.OutmallFailType.TRANS.getCode().equals(dto.getFailType())) {
            /*
             * 컬럼별 width 목록 : 단위 pixel
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
             */
            Integer[] widthListOfFirstWorksheet = { //
                    300, 200, 200, 200, 200
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
                    "errorMessage", "logisticsCd", "trackingNo", "orderId", "outmallOrderDt"
            };

            // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
            String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                    "실패사유", "택배사", "송장번호", "주문번호",  "송장등록일"
            };

            // 워크시트 DTO 생성 후 정보 세팅
            firstWorkSheetDto = ExcelWorkSheetDto.builder() //
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
            List<CollectionMallInterfaceFailVo> itemList = outmallOrderService.getCollectionMallFailExcelDownload(dto);

            firstWorkSheetDto.setExcelDataList(itemList);
        } else {
            /*
             * 컬럼별 width 목록 : 단위 pixel
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
             */
            Integer[] widthListOfFirstWorksheet = { //
                    150, 150, 150, 150, 150, 150, 500, 100, 150, 150, 150,
                    150, 150, 150, 150, 150, 400, 300, 100, 200, 150, 150,
                    150, 150, 150, 150, 150
            };

            /*
             * 본문 데이터 컬럼별 정렬 목록
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
             * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
             */
            String[] alignListOfFirstWorksheet = { //
                    "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                    "center", "center", "center", "center", "center", "left", "left", "center", "center", "center", "center",
                    "center", "center", "center", "center", "center"
            };

            /*
             * 본문 데이터 컬럼별 데이터 property 목록
             * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
             */
            String[] propertyListOfFirstWorksheet = { //
                    "errorMessage", "pack", "seq", "shopId", "shopProductId", "ilItemCd", "productName", "qty", "amount", "orderName", "orderTel",
                    "orderMobile", "receiverName", "receiverTel", "receiverMobile", "receiverZip", "receiverAddress1", "receiverAddress2", "shippingPrice", "memo", "logisticsCd", "trackingNo",
                    "orderId",  "deliveryMessage", "outmallOrderDt", "outmallGoodsNm", "outmallOptNm",
            };

            // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
            String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                    "실패사유", "합포번호", "관리번호", "외부몰코드", "상품코드", "ERP 상품코드", "상품명", "수량", "상품총금액", "주문자명", "주문자연락처",
                    "주문자휴대폰번호", "수취인명", "수취인연락처", "수취인휴대폰", "수취인우편번호", "수취인주소1", "수취인주소2", "배송비", "메세지", "택배사", "송장번호",
                    "외부몰주문번호",  "고객상담", "수집일", "외부몰상품명", "외부몰옵션명",
            };

            // 워크시트 DTO 생성 후 정보 세팅
            firstWorkSheetDto = ExcelWorkSheetDto.builder() //
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
            List<CollectionMallInterfaceFailVo> itemList = outmallOrderService.getCollectionMallFailExcelDownload(dto);

            firstWorkSheetDto.setExcelDataList(itemList);
        }

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public ApiResult<?> getSellersList(String sellersGroupCode) {
        return ApiResult.success(outmallOrderService.getSellersList(sellersGroupCode));
    }

    @Override
    @UserMaskingRun(system = "BOS")
    public ApiResult<?> getClaimOrderList(ClaimOrderListRequestDto dto) {
        return ApiResult.success(outmallOrderService.getClaimOrderList(dto));
    }

    /**
     * 외부몰 클레임 주문리스트 엑셀다운로드
     */
    @Override
    public ExcelDownloadDto getClaimOrderListExportExcel(ClaimOrderListRequestDto dto) {
        String excelFileName = "claim_order_list"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                170, 200, 200, 150, 170, 150, 150, 200, 200, 150
                , 150, 180, 180, 180, 450, 150, 150, 150};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center"
                , "center", "center", "center", "center", "center", "center", "right", "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "shopName", "seq", "orderId", "orderName", "orderMobile", "receiverName", "receiverMobile", "orderCsName", "orderDate", "productId"
                , "orderDetailId", "masterProductCodeName", "shopProductId", "enableSaleName", "name", "qty", "shopPrice", "processCodeName"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "외부몰", "수집몰 주문상세번호", "외부몰 주문상세번호", "주문자명", "주문자연락처", "수취인명", "수취인연락처", "주문상태", "주문일", "수집몰 상품코드"
                , "주문상세번호", "마스터품목코드태(품목바코드)", "상품코드", "상품판매상태", "상품명", "수량", "판매가", "처리상태"};

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
        ClaimOrderListResponseDto claimOrderList  = outmallOrderService.getClaimOrderList(dto);

        firstWorkSheetDto.setExcelDataList(claimOrderList.getRows());

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public ApiResult<?> putClaimOrderProgress(ClaimOrderProgressRequestDto dto) {

        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null && dto.getProcessCode().equals(ExcelUploadValidateEnums.ProcessCode.I.getCode())) {
            dto.setAdminId(Long.valueOf(userVo.getUserId()));
        }

        outmallOrderService.putClaimOrderProgress(dto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> putClaimOrderProgressList(ClaimOrderProgressRequestDto dto) {

        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null && dto.getProcessCode().equals(ExcelUploadValidateEnums.ProcessCode.I.getCode())) {
            dto.setAdminId(Long.valueOf(userVo.getUserId()));
        }

        outmallOrderService.putClaimOrderProgressList(dto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> addOutMallExcelUpload(MultipartFile file) throws Exception {

        // Return 값 설정
        OutMallOrderResponseDto responseDto = new OutMallOrderResponseDto();

        //업로드 현황 정보 설정
        OutmallOrderUtil outmallOrderUtil = new OutmallOrderUtil();
        OutMallExcelInfoVo infoVo = outmallOrderUtil.setOutmallExcelInfo();

        try {
            if (ExcelUploadUtil.isFile(file) != true) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

            // Excel Import 정보 -> Dto 변환
            Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
            if (uploadSheet == null) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);

            infoVo = outmallOrderUtil.getOutmallType(infoVo, uploadSheet.getRow(0));
            if ("".equals(infoVo.getOutMallType())){
                return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.FILE_ERROR);
            }

            String outmallType = infoVo.getOutMallType();

            // Excel 데이터 Mapping
            List<OutMallOrderDto> excelList = (List<OutMallOrderDto>) orderExcelUploadFactory.setExcelData(outmallType, uploadSheet);

            if(excelList.isEmpty()) {
                return ApiResult.result(responseDto, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);
            }

            // 원본 데이터 JSON 변환
            List<OutMallOrderDto> resultList = new ArrayList<>();
            for(OutMallOrderDto x : excelList){
                resultList.add(OBJECT_MAPPER.convertValue(x, OutMallOrderDto.class));
            }

            Gson gson = new Gson();
            String uploadJsonData = gson.toJson(resultList);

        /*
        엑셀다운로드시 사용 소스
        Gson gson2 = new Gson();
        Type type = new TypeToken<List<OutMallOrderDto>>(){}.getType();
        List<OutMallOrderDto> contactList = gson2.fromJson(uploadJsonData, type);
        for (OutMallOrderDto x : contactList){
            System.out.println("x : " + x.toString());
        }
        */
            //업로드 현황 1차 저장
            infoVo.setUploadJsonData(uploadJsonData);
            infoVo.setTotalCount(excelList.size());
            infoVo.setFileNm(file.getOriginalFilename());
            outmallOrderService.addOutMallExcelInfo(infoVo);

            // 검증용 상품정보 조회
            Map<Long, GoodsSearchOutMallVo> goodsMaps = orderExcelUploadFactory.getIlGoodsIdList(outmallType, excelList);

            // 검증용 수집몰 번호
            Map<String, Object> collectionMallValidatorIdMaps = orderExcelUploadFactory.getCollectionMallValidatorIdList(outmallType, excelList);


            List<String> sellersGrpList = excelList.stream()
                    .map(OutMallOrderDto::getOmSellersId)
                    .filter(Objects::nonNull)
                    .filter(str -> !str.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());

            if (sellersGrpList == null || sellersGrpList.size() <= 0){
                sellersGrpList.add("0");
            }
            // 판매처 정보 조회
            List<OutMallOrderSellersDto> sellersList = outmallOrderService.getOmSellersInfoList(sellersGrpList);

            // 항목별 검증 진행
            excelList = (List<OutMallOrderDto>) orderExcelUploadFactory.getGoodsRowItemValidator(outmallType, excelList, goodsMaps, collectionMallValidatorIdMaps, sellersList);

            //업로드 현황 설정
            List<OutMallExcelSuccessVo> successVoList = new ArrayList<>();
            List<OutMallExcelSuccessVo> tmpSuccessVoList = new ArrayList<>();
            List<OutMallExcelFailVo> failVoList = new ArrayList<>();
            Map<String, String> checkFailMap = new HashMap<>();
            for (OutMallOrderDto dto : excelList) {
                if (dto.isSuccess()) {
                    tmpSuccessVoList.add(new OutMallExcelSuccessVo(dto));
                } else {
                    if (StringUtils.isNotEmpty(dto.getGoodsNo()) && StringUtil.nvlInt(StringUtil.nvl(dto.getGoodsNo(), "0")) > 0){
                        dto.setIlGoodsId(dto.getGoodsNo());
                    }
                    //failVoList.add(new OutMallExcelFailVo(dto));
                    if(!checkFailMap.containsKey(dto.getCollectionMallId())){
                        checkFailMap.put(dto.getCollectionMallId(), dto.getCollectionMallId());
                    }
                }
            }

            for (OutMallOrderDto dto : excelList) {
                if (checkFailMap.containsKey(dto.getCollectionMallId())) {
                    //dto.setFailMessage("");
                    if (StringUtils.isNotEmpty(dto.getGoodsNo()) && StringUtil.nvlInt(StringUtil.nvl(dto.getGoodsNo(), "0")) > 0){
                        dto.setIlGoodsId(dto.getGoodsNo());
                    }
                    failVoList.add(new OutMallExcelFailVo(dto));
                } else {
                    successVoList.add(new OutMallExcelSuccessVo(dto));
                }
            }


            // 상품 외부몰주문상태 해당없음 -> 판매중 업데이트
            List<Long> updateTargetList = goodsMaps.values().stream()
                    .filter(str -> "Y".equals(str.getGoodsOutmallSaleStatUpdateYn()))
                    .map(x -> x.getGoodsId())
                    .distinct()
                    .collect(Collectors.toList());

            // 외부몰 판매 상태 업데이트
            if (updateTargetList.size() > 0) {
                outmallOrderService.putGoodsOutmallStatus(updateTargetList);
            }



/*
        successVoList = successVoList.stream()
                .sorted(Comparator.comparing(OutMallExcelSuccessVo::getCollectionMallId)
                    .thenComparing(OutMallExcelSuccessVo::getCollectionMallDetailId))
                    .collect(Collectors.toList());

        failVoList = failVoList.stream()
                .sorted(Comparator.comparing(OutMallExcelFailVo::getCollectionMallId)
                        .thenComparing(OutMallExcelFailVo::getCollectionMallDetailId))
                .collect(Collectors.toList());
*/


            // 주문단위로 정상건이 있어도 실패 처리 함

            // 업데이트 데이터 세팅
            outmallOrderUtil.setOutmallExcelUpdateInfo(infoVo, successVoList.size(), failVoList.size());

            if (successVoList.size() > 0) {

                outmallOrderService.addOutMallExcelSuccess(infoVo.getIfOutmallExcelInfoId(), successVoList);

                // 이지어드민
                if (ExcelUploadEnums.ExcelUploadType.EASYADMIN.getCode().equals(outmallType)){
                    // 상품가격 비율을 적용해서 재계산
                    outmallOrderService.addEasyAdminExcelRatioPrice(infoVo.getIfOutmallExcelInfoId());

                    // 재계산 적용 업데이트
                    outmallOrderService.putEasyAdminExcelRatioPrice(infoVo.getIfOutmallExcelInfoId());
                } else { // 사방넷
                    // 상품가격 비율을 적용해서 재계산
                    outmallOrderService.addSabangnetExcelRatioPrice(infoVo.getIfOutmallExcelInfoId());

                    // 재계산 적용 업데이트
                    outmallOrderService.putSabangnetExcelRatioPrice(infoVo.getIfOutmallExcelInfoId());
                }


                // 재계산 성공 정보에 업데이트
                outmallOrderService.putOutMallExcelSuccRatioPrice(infoVo.getIfOutmallExcelInfoId());

                // 증정품 0원처리
                outmallOrderService.putOutMallExcelSuccGiftPrice(infoVo.getIfOutmallExcelInfoId());

                // 업로드 정보 주문생성
                //outmallOrderRegistrationBiz.setBindOrderOrder(infoVo.getIfOutmallExcelInfoId());

            }
            if (failVoList.size() > 0) {
                outmallOrderService.addOutMallExcelFail(infoVo.getIfOutmallExcelInfoId(), failVoList);
            }

            // 저장
            outmallOrderService.putOutMallExcelInfo(infoVo);

            responseDto.setTotalCount(infoVo.getTotalCount());
            responseDto.setSuccessCount(infoVo.getSuccessCount());
            responseDto.setFailCount(infoVo.getFailCount());

            if (responseDto.getFailCount() > 0) {
                List<String> failMessageList = new ArrayList<>();
                failVoList.stream()
                        .forEach(item -> {
                            String[] arr = item.getFailMessage().split(Constants.ARRAY_SEPARATORS);
                            for(String str:arr){
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

        } catch (Exception ex) {
            outmallOrderUtil.setOutmallExcelUpdateInfo(infoVo, 0, 0);
            // 저장
            outmallOrderService.putOutMallExcelInfo(infoVo);

            return ApiResult.result(responseDto, ExcelUploadValidateEnums.UploadResponseCode.UPLOAD_FAIL);
        }
    }

    @Override
    public List<String> getOutMallExcelDetailId(String outmallType, List<String> detailIdList) {
        return outmallOrderService.getOutMallExcelDetailId(outmallType, detailIdList);
    }

    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ApiResult<?> getOutMallExcelInfoList(OutMallExcelListRequestDto dto) {
        return ApiResult.success(outmallOrderService.getOutMallExcelInfoList(dto));
    }

    @Override
    public ExcelDownloadDto getOutMallFailExcelDownload(OutMallExcelFailRequestDto dto) throws InvocationTargetException, IllegalAccessException {
        String excelFileName = "외부몰 주문 엑셀업로드 실패 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        // 엑셀업로드 정보 실패 정보 조회
        OutMallExcelInfoVo outMallExcelInfo = outmallOrderService.getOutMallExcelInfo(dto);

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                300, 150, 150, 100, 150, 100, 400, 100, 100, 150,
                150, 100, 150, 150, 100, 200, 200, 100, 100, 100,
                100, 100, 100, 100, 100, 100, 100
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "left", "center", "center", "center", "center", "center", "left", "center", "center", "center",
                "center", "center", "center", "center", "center", "center", "left", "left", "center", "center",
                "center", "center", "center", "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "failMessage", "collectionMallId", "collectionMallDetailId", "omSellersId", "ilGoodsId", "ilItemCd", "goodsName", "orderCount", "paidPrice", "buyerName",
                "buyerTel", "buyerMobile", "receiverName", "receiverTel", "receiverMobile", "receiverZipCode", "receiverAddress1", "receiverAddress2", "shippingPrice", "deliveryMessage",
                "logisticsCd", "trackingNo", "outMallId", "memo", "outmallOrderDt", "outmallGoodsNm", "outmallOptNm"
        };



        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	            "실패사유", "합포번호", "관리번호", "외부몰코드", "샵풀무원상품코드", "Erp상품코드", "상품명", "수량", "상품총금액", "주문자명",
	            "주문자연락처", "주문자휴대폰번호", "수취인명", "수취인연락처", "수취인휴대폰", "수취인우편번호", "수취인주소1", "수취인주소2", "배송비", "배송메세지",
	            "택배사", "송장번호", "외부몰주문번호", "고객상담", "수집일", "외부몰상품명", "외부몰옵션명"
    	};

        // 사방넷일 경우
        if(ExcelUploadEnums.ExcelUploadType.SABANGNET.getCode().equals(outMallExcelInfo.getOutMallType())) {
        	firstHeaderListOfFirstWorksheet[1] = "수집몰주문번호";
        	firstHeaderListOfFirstWorksheet[2] = "수집몰상세번호";
        	firstHeaderListOfFirstWorksheet[24] = "외부몰 주문일";
        }


        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        dto.setOutmallType(outMallExcelInfo.getOutMallType());
        List<OutMallExcelFailVo> itemList = new ArrayList<>();
        if (OutmallEnums.OutmallDownloadType.ORG_UPLOAD.getCode().equals(dto.getFailType())) {
            excelFileName = "외부몰 주문 엑셀업로드 내역";
            if(StringUtils.isNotEmpty(outMallExcelInfo.getFileNm())) {
                String filename = outMallExcelInfo.getFileNm();
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

            if (!"".equals(outMallExcelInfo.getUploadJsonData())) {
                Gson gson2 = new Gson();
                Type type = new TypeToken<List<OutMallOrderDto>>() {
                }.getType();
                List<OutMallOrderDto> uploadJsonDataList = gson2.fromJson(outMallExcelInfo.getUploadJsonData(), type);
                for (OutMallOrderDto x : uploadJsonDataList) {
                    OutMallExcelFailVo outMallExcelFailVo = new OutMallExcelFailVo();
                    BeanUtils.copyProperties(outMallExcelFailVo, x);

                    itemList.add(outMallExcelFailVo);
                }
            }
        } else {
            if (OutmallEnums.OutmallDownloadType.BATCH.getCode().equals(dto.getFailType())) {
                excelFileName = "외부몰 주문 엑셀업데이트 실패 내역";
            }
            itemList = outmallOrderService.getOutMallFailExcelDownload(dto);
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


        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public List<String> getCollectionMallIdList(String outmallType, List<String> searchList) {
        return outmallOrderService.getCollectionMallIdList(outmallType, searchList);
    }

    @Override
    public List<String> getCollectionMallDetailIdList(String outmallType, List<String> searchList) {
        return outmallOrderService.getCollectionMallDetailIdList(outmallType, searchList);
    }

    @Override
    public List<String> getOutmallIdList(String outmallType, List<String> searchList) {
        return outmallOrderService.getOutmallIdList(outmallType, searchList);
    }


    @Override
    public ExcelDownloadDto getOutmallShippingInfoDownload(OutMallShippingInfoDownloadRequestDto reqDto) {

    	String excelFileName = "이지어드민 배송정보 내역 다운로드"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    	String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

    	if(OutmallEnums.OutmallType.SABANGNET.getCode().equals(reqDto.getOutmallType())) {
    		excelFileName = "사방넷 배송정보 내역 다운로드";
    	}

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheetForEzadmin = { // 1. 이지어드민
                300, 300, 200, 200, 200, 200, 300, 300
        };
        Integer[] widthListOfFirstWorksheetForSabangnet = { // 2. 사방넷
                300, 300, 100, 100, 300, 300, 300, 300, 300, 300
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheetForEzadmin = { // 1. 이지어드민
                "center", "center", "center", "center", "center", "center", "center", "center"
        };
        String[] alignListOfFirstWorksheetForSabangnet = { // 2. 사방넷
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheetForEzadmin = { // 1. 이지어드민
                "collectionMallDetailId", "trackingNo", "shippingCompNm", "orderStatus", "warehouseNm", "createDt", "sellersNm", "outmallDetailId"
        };
        String[] propertyListOfFirstWorksheetForSabangnet = { // 2. 사방넷
                "collectionMallDetailId", "trackingNo", "", "", "outmallShippingCompCd", "orderStatus", "warehouseNm", "createDt", "sellersNm", "outmallDetailId"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
        String[] firstHeaderListOfFirstWorksheetForEzadmin = {  // 1. 이지어드민
	            "관리번호", "송장번호", "택배사명", "배송상태", "출고처", "송장등록일", "주문경로", "외부몰상세번호"
    	};
        String[] firstHeaderListOfFirstWorksheetForSabangnet = {  // 2. 사방넷
	            "사방넷주문상세번호", "송장번호", "", "", "사방넷 택배사코드", "배송상태", "출고처", "송장등록일", "주문경로", "외부몰상세번호"
    	};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto;
        if(OutmallEnums.OutmallType.EASYADMIN.getCode().equals(reqDto.getOutmallType())) {
        	firstWorkSheetDto = ExcelWorkSheetDto.builder()
                    .workSheetName(excelSheetName) 							// 엑셀 파일내 워크시트 명
                    .propertyList(propertyListOfFirstWorksheetForEzadmin) 	// 컬럼별 데이터 property 목록
                    .widthList(widthListOfFirstWorksheetForEzadmin) 		// 컬럼별 너비 목록
                    .alignList(alignListOfFirstWorksheetForEzadmin) 		// 컬럼별 정렬 목록
                    .build();

        	// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        	firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheetForEzadmin); // 첫 번째 헤더 컬럼
        }else {
        	firstWorkSheetDto = ExcelWorkSheetDto.builder()
                    .workSheetName(excelSheetName) 							// 엑셀 파일내 워크시트 명
                    .propertyList(propertyListOfFirstWorksheetForSabangnet) // 컬럼별 데이터 property 목록
                    .widthList(widthListOfFirstWorksheetForSabangnet) 		// 컬럼별 너비 목록
                    .alignList(alignListOfFirstWorksheetForSabangnet) 		// 컬럼별 정렬 목록
                    .build();

        	// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        	firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheetForSabangnet); // 첫 번째 헤더 컬럼
        }

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */

        List<OutMallShippingInfoVo> outmallShippingInfoList = new ArrayList<>();
        try {
    		UserVo userVo = SessionUtil.getBosUserVO();

            reqDto.setOmSellersIdList(getSearchKeyToSearchKeyList(reqDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처


            List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
            listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
            List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
            listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));

            reqDto.setListAuthWarehouseId(listAuthWarehouseId);
            reqDto.setListAuthSupplierId(listAuthSupplierId);

        	// 외부몰 배송정보 내역 다운로드 정보 조회
        	outmallShippingInfoList =  outmallOrderService.getOutmallShippingInfoDownload(reqDto);

        	// 외부몰 배송정보 다운로드 내역 히스토리 저장
        	outmallOrderService.addOutmallShippingExcelDownHist(reqDto);
		} catch (Exception e) {
			log.error("========================== 배송정보 내역 다운로드 ERROR =====");
			log.error(e.getMessage());
		}

        System.out.println("--------------------------------------------------------");
        System.out.println(outmallShippingInfoList.toString());
        System.out.println("--------------------------------------------------------");

        firstWorkSheetDto.setExcelDataList(outmallShippingInfoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public OutMallShippingInfoDownloadHistResponseDto getOutMallShippingExceldownHist(OutMallShippingInfoDownloadHistRequestDto reqDto) throws Exception{

        reqDto.setOmSellersIdList(getSearchKeyToSearchKeyList(reqDto.getPopupOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처

    	return outmallOrderService.getOutMallShippingExceldownHist(reqDto);
    }


    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public OutMallTrackingNumberHistResponseDto getOutMallTrackingNumberHist(OutMallTrackingNumberHistRequestDto reqDto) throws Exception{
        return outmallOrderService.getOutMallTrackingNumberHist(reqDto);
    }


    /**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();
        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));
        }
        return searchKeyList;
    }
}