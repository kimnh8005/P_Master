package kr.co.pulmuone.v1.order.delivery.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.service.ClaimUtilRefundService;
import kr.co.pulmuone.v1.order.delivery.dto.*;
import kr.co.pulmuone.v1.order.delivery.dto.vo.*;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateDto;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <PRE>
* Forbiz Korea
* 일괄송장 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 24.        이규한          	  최초작성
* =======================================================================
* </PRE>
*/
@Slf4j
@Service
public class OrderBulkTrackingNumberBizImpl  implements OrderBulkTrackingNumberBiz {

    @Autowired
    OrderBulkTrackingNumberService orderBulkTrackingNumberService;		// 일괄송장 Service

    @Autowired
    OrderStatusBiz	orderStatusBiz;		// 주문상태 관련 I/F

	@Autowired
	private OrderExcelUploadFactory orderExcelUploadFactory;

	@Autowired
	private ClaimUtilRefundService claimUtilRefundService;

	@Autowired
	private TrackingNumberBiz trackingNumberBiz;

    /**
     * @Desc 일괄송장 엑셀 업로드
     * @param paramDto
     * @return ApiResult
     * @throws Exception
     */
	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> orderBulkTrackingNumberExcelUpload(MultipartFile file, OrderBulkTrackingNumberExcelUploadRequestDto paramDto) {
		OutMallOrderResponseDto responseDto = new OutMallOrderResponseDto();
		OrderBulkTrackingNumberVo resultVo = new OrderBulkTrackingNumberVo();

		String result 		= "";	// 결과
		//String smsSendYn	= StringUtil.nvl(paramDto.getSendSmsYn(), "N");	// SMS 발송여부
		int successCnt 		= 0;	// 성공건수
		int failureCnt 		= 0;	// 실패건수
		List<Long> smsSendOdOrderDetlIdList = new ArrayList<>();
		// 업로드 내용이 없을 경우
		if (ExcelUploadUtil.isFile(file) != true) {
			responseDto.setTotalCount(0);
			responseDto.setSuccessCount(0);
			responseDto.setFailCount(0);
			return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);
		}

		// Excel Import 정보 -> Dto 변환
		Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
		if (uploadSheet == null) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);

// Excel 데이터 Mapping
		List<OrderTrackingNumberVo> excelList = (List<OrderTrackingNumberVo>) orderExcelUploadFactory.setExcelData(ExcelUploadEnums.ExcelUploadType.TRACKING_NUMBER.getCode(), uploadSheet);

		// 일괄송장정보 등록
		resultVo.setOriginNm(paramDto.getOriginNm());
		resultVo.setCreateId(Long.parseLong(paramDto.getUserVo().getUserId()));
		orderBulkTrackingNumberService.addOrderBulkTrackingNumber(resultVo);

		for(OrderTrackingNumberVo orderTrackingNumberVo : excelList) {

			// 엑셀 업로드 성공여부 기본값 성공으로 셋팅
			orderTrackingNumberVo.setSuccessYn("Y");

			orderTrackingNumberVo.setCreateId(Long.parseLong(paramDto.getUserVo().getUserId()));	// 등록자

			Integer odOrderDetlIdInt = orderBulkTrackingNumberService.getOdOrderDetlIdCount(orderTrackingNumberVo.getOdid(), orderTrackingNumberVo.getOdOrderDetlSeq());
			long odOrderDetlId = 0;
			if(StringUtil.isNotEmpty(odOrderDetlIdInt)){
				odOrderDetlId = StringUtil.nvlLong(odOrderDetlIdInt);
			}
			orderTrackingNumberVo.setOdOrderDetlId(odOrderDetlId);

			// 주문 I/F여부 체크
			String[] urWarehouseIds = { ErpApiEnums.UrWarehouseId.EATSSLIM_D3_FRANCHISEE.getCode()
					, ErpApiEnums.UrWarehouseId.EATSSLIM_D2_3PL.getCode()
					, ErpApiEnums.UrWarehouseId.EATSSLIM_D3_DELIVERY.getCode()
					, ErpApiEnums.UrWarehouseId.ORGA_STORE.getCode() };
			int batchExecFlCnt = claimUtilRefundService.getWarehouseIsInterfaceYnCheck(odOrderDetlId, urWarehouseIds);

			// 값이 없는 경우 실패 (주문상세 PK, 택배사코드, 개별송장번호)
			if (orderTrackingNumberVo.getOdid() == null || StringUtil.nvlLong(orderTrackingNumberVo.getPsShippingCompId()) <= 0 || StringUtils.isEmpty(orderTrackingNumberVo.getTrackingNo())) {
				orderTrackingNumberVo.setSuccessYn("N");
				orderTrackingNumberVo.setMsg(OrderEnums.OrderBulkTrackingExcelUploadErrMsg.VALUE_EMPTY.getMessage());
			// 주문상세PK 유효하지 않은 경우
			} else if (odOrderDetlId <= 0) {
				orderTrackingNumberVo.setSuccessYn("N");
				orderTrackingNumberVo.setMsg(OrderEnums.OrderBulkTrackingExcelUploadErrMsg.ORDER_DETL_ID_EMPTY.getMessage());
			// 택배사코드 유효하지 않은 경우
			} else if (orderBulkTrackingNumberService.getPsShippingCompIdCount(orderTrackingNumberVo.getPsShippingCompId()) < 1) {
				orderTrackingNumberVo.setSuccessYn("N");
				orderTrackingNumberVo.setMsg(OrderEnums.OrderBulkTrackingExcelUploadErrMsg.LOGISTICS_CD_EMPTY.getMessage());
			// 상태값이 DR(배송준비중),DI(배송중)이 아니거나 클레임 주문건인 경우
			}
			else if(orderBulkTrackingNumberService.getOrderBulkTrackingNumberOrderStatus(orderTrackingNumberVo.getOdOrderDetlId()) < 1){
				orderTrackingNumberVo.setSuccessYn("N");
				orderTrackingNumberVo.setMsg(OrderEnums.OrderBulkTrackingExcelUploadErrMsg.NOT_CHANGEABLE_ORDER_STATUS.getMessage());
			}
			// 주문 I/F여부가 있으면
			else if(batchExecFlCnt > 0){
                orderTrackingNumberVo.setSuccessYn("N");
                orderTrackingNumberVo.setMsg(OrderEnums.OrderBulkTrackingExcelUploadErrMsg.ORDER_IF.getMessage());

				// [SPMO-251]BOS_[일괄송장입력] 자동IF 출고처에 대해 송장 업로드 기능 차단됨-> 수정 업로드 기능 필요
                // 주문상세 송장번호 테이블 기등록 송장정보 조회
                int OrderTrackingNumberCnt = orderBulkTrackingNumberService.getOrderTrackingNumberCnt(orderTrackingNumberVo.getOdOrderDetlId());
                if(OrderTrackingNumberCnt > 0) {
                    orderTrackingNumberVo.setSuccessYn("Y");
                    log.info("> 주문 I/F 건 송장번호 변경 : {}", orderTrackingNumberVo);
                }
			}




			// 주문 상세별 송장번호 정보가 유효한 경우
			if ("Y".equals(orderTrackingNumberVo.getSuccessYn())) {

				// 주문상세 배송상태 업데이트
				ApiResult apiResult = this.putOrderDetailDeliveryStatus(orderTrackingNumberVo, OrderEnums.OrderStatus.DELIVERY_ING.getCode());

				// 일괄송장 성공내역 테이블 등록
				OrderBulkTrackingNumberSuccVo orderBulkTrackingNumberSuccVo = new OrderBulkTrackingNumberSuccVo();

				orderBulkTrackingNumberSuccVo.setOdBulkTrackingNumberId(resultVo.getOdBulkTrackingNumberId());		// 일괄송장입력 PK
				orderBulkTrackingNumberSuccVo.setOdOrderDetlId(orderTrackingNumberVo.getOdOrderDetlId());			// 주문상세 PK
				orderBulkTrackingNumberSuccVo.setPsShippingCompId(orderTrackingNumberVo.getPsShippingCompId());		// 택배사 PK
				orderBulkTrackingNumberSuccVo.setTrackingNo(orderTrackingNumberVo.getTrackingNo());					// 송장번호
				orderBulkTrackingNumberSuccVo.setOdid(orderTrackingNumberVo.getOdid());
				orderBulkTrackingNumberSuccVo.setOdOrderDetlSeq(orderTrackingNumberVo.getOdOrderDetlSeq());

				orderBulkTrackingNumberService.addOrderBulkTrackingNumberSucc(orderBulkTrackingNumberSuccVo);

				// 송장입력일 때만 SMS 발송 -> 송장수정일 경우는 발송x (HGRM-9521)

				if(OrderEnums.OrderDetailDeliveryStatusType.INSERT.getCode().equals(((OrderEnums.OrderDetailDeliveryStatusType) apiResult.getData()).getCode())){
					smsSendOdOrderDetlIdList.add(orderTrackingNumberVo.getOdOrderDetlId());
				}

				// 주문상세번호에 따른 클레임 번호 조회
				ClaimNumberSearchVo claimNumberSearchVo = trackingNumberBiz.getOdClaimId(orderTrackingNumberVo.getOdOrderDetlId());
				// 취소요청 존재시 취소거부 처리
				long odClaimId = StringUtil.nvlLong(claimNumberSearchVo.getOdClaimId());
				long odClaimDetlId = StringUtil.nvlLong(claimNumberSearchVo.getOdClaimDetlId());
				if (odClaimId > 0 && odClaimDetlId > 0) {
					try {
						trackingNumberBiz.putCancelRequestClaimDenial(claimNumberSearchVo.getOdid(), odClaimId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				successCnt++;
			} else  {
				// 일괄송장 실패내역 테이블 등록
				OrderBulkTrackingNumberFailVo orderBulkTrackingNumberFailVo= new OrderBulkTrackingNumberFailVo();

				orderBulkTrackingNumberFailVo.setOdBulkTrackingNumberId(resultVo.getOdBulkTrackingNumberId());		// 일괄송장입력 PK
				orderBulkTrackingNumberFailVo.setOdOrderDetlId(orderTrackingNumberVo.getOdOrderDetlId());			// 주문상세 PK
				orderBulkTrackingNumberFailVo.setPsShippingCompId(orderTrackingNumberVo.getPsShippingCompId());		// 택배사 PK
				orderBulkTrackingNumberFailVo.setTrackingNo(orderTrackingNumberVo.getTrackingNo());					// 송장번호
				orderBulkTrackingNumberFailVo.setFailReason(orderTrackingNumberVo.getMsg());						// 실패사유
				orderBulkTrackingNumberFailVo.setOdid(orderTrackingNumberVo.getOdid());
				orderBulkTrackingNumberFailVo.setOdOrderDetlSeq(orderTrackingNumberVo.getOdOrderDetlSeq());

				orderBulkTrackingNumberService.addOrderBulkTrackingNumberFail(orderBulkTrackingNumberFailVo);

				failureCnt++;
			}
		}



		// 상품발송 자동메일 발송
		// 외부몰은 SMS 발송 하면 안됨
		try {
			if(CollectionUtils.isNotEmpty(smsSendOdOrderDetlIdList)){
				orderStatusBiz.sendOrderGoodsDelivery(smsSendOdOrderDetlIdList,false);
			}
		}catch(Exception e){
			log.error("ERROR ====== 일괄 송장 입력 상품발송 자동메일 발송 오류 =========");
			log.error(e.getMessage());
		}


		// 일괄송장정보 수정(성공/실패 건수 업데이트)

		resultVo.setTotalCnt(excelList.size());
		resultVo.setSuccessCnt(successCnt);											// 성공건수
		resultVo.setFailureCnt(failureCnt);											// 실패건수
		resultVo.setCreateId(Long.parseLong(paramDto.getUserVo().getUserId()));		// 등록자
		resultVo.setOriginNm(paramDto.getOriginNm());								// 원본파일명
		orderBulkTrackingNumberService.putOrderBulkTrackingNumber(resultVo);

		//result = Integer.toString(paramDto.getUploadList().size())+"|"+Integer.toString(successCnt)+"|"+Integer.toString(failureCnt); //총건수|성공건수|실패건수



		return ApiResult.success(resultVo);
	}

    /**
     * @Desc 일괄송장 입력 내역 목록 조회
     * @param paramDto
     * @return ApiResult
     * @throws Exception
     */
	@Override
	public ApiResult<?> getOrderBulkTrackingNumberList(OrderBulkTrackingNumberListRequestDto paramDto) {
		OrderBulkTrackingNumberListResponseDto orderBulkTrackingNumberListResponseDto = new OrderBulkTrackingNumberListResponseDto();

		Page<OrderBulkTrackingNumberVo> odBulkTrackingNumberList = orderBulkTrackingNumberService.getOrderBulkTrackingNumberList(paramDto);
		orderBulkTrackingNumberListResponseDto.setTotal(odBulkTrackingNumberList.getTotal());
		orderBulkTrackingNumberListResponseDto.setRows(odBulkTrackingNumberList.getResult());

		return ApiResult.success(orderBulkTrackingNumberListResponseDto);
	}

	/**
     * @Desc 일괄송장 입력 실패내역 엑셀 다운로드
     * @param paramDto
     * @return ExcelDownloadDto
     * @throws Exception
     */
	@Override
	public ExcelDownloadDto getOrderBulkTrackingNumberFailList(OrderBulkTrackingNumberListRequestDto paramDto) {

		String excelSheetName	= "sheet"; 					// 엑셀 파일 내 워크시트 이름
		String excelFileName	= "일괄송장 입력 엑셀 업로드 실패 내역";	// 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = {180, 180, 180, 180, 400};

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = {"center", "center", "center", "center", "left"};

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] propertyListOfFirstWorksheet = {"odid", "odOrderDetlSeq", "psShippingCompId", "trackingNo", "failReason"};

		/*
		 * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		 * 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
		 */
		//String[] firstHeaderListOfFirstWorksheet 	= { "OD_ORDER_DETL_ID", "PS_SHIPPING_COMP_ID", "TRACKING_NO", "FAIL_REASON"};
		String[] firstHeaderListOfFirstWorksheet 	= { "주문번호", "주문상세순번", "택배사코드", "개별송장번호", "실패사유"};

		/*
		 * 워크시트 DTO 생성 후 정보 세팅
		 */
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
				.workSheetName(excelSheetName) 				// 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) 		// 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet)		// 컬럼별 정렬 목록
				.build();

		/*
		 * 엑셀 다단 헤더 구성 : (헤더 행 index , 헤더 제목 배열) 형식으로 세팅
		 */
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); 	// 첫번째 헤더 컬럼
		//firstWorkSheetDto.setHeaderList(1, secondHeaderListOfFirstWorksheet); 	// 두번째 헤더 컬럼

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		List<OrderBulkTrackingNumberFailVo> orderBulkTrackingNumberFailVo = orderBulkTrackingNumberService.getOrderBulkTrackingNumberFailList(paramDto);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (OrderBulkTrackingNumberFailVo vo: orderBulkTrackingNumberFailVo) {
			Map<String, Object> reulstMap = new HashMap<String, Object>();
			reulstMap.put("odid"			, vo.getOdid());
			reulstMap.put("odOrderDetlSeq"	, vo.getOdOrderDetlSeq());
			reulstMap.put("psShippingCompId", vo.getPsShippingCompId());
			reulstMap.put("trackingNo"		, vo.getTrackingNo());
			reulstMap.put("failReason"		, vo.getFailReason());
			resultList.add(reulstMap);
		}
		firstWorkSheetDto.setExcelDataList(resultList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();
		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
		return excelDownloadDto;
	}

	/**
     * @Desc 일괄 송장 입력 내역 상세 목록 조회
     * @param paramDto
     * @return ApiResult
     * @throws Exception
     */
	@Override
	public ApiResult<?> getOrderBulkTrackingNumberDetlList(OrderBulkTrackingNumberDetlListRequestDto paramDto) {

		OrderBulkTrackingNumberDetlListResponseDto orderBulkTrackingNumberDetlListResponseDto = new OrderBulkTrackingNumberDetlListResponseDto();
		ArrayList<String> codeArray = null;

		// 화면에서 전송한 검색코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
		String ilItemCodeListStr = paramDto.getCodeSearch().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
		codeArray = StringUtil.getArrayListComma(ilItemCodeListStr);
		paramDto.setCodeArray(codeArray);

		Page<OrderBulkTrackingNumberDetlDto> orderBulkTrackingNumberDetlDtoList = orderBulkTrackingNumberService.getOrderBulkTrackingNumberDetlList(paramDto);
		orderBulkTrackingNumberDetlListResponseDto.setTotal(orderBulkTrackingNumberDetlDtoList.getTotal());
		orderBulkTrackingNumberDetlListResponseDto.setRows(orderBulkTrackingNumberDetlDtoList.getResult());

        return ApiResult.success(orderBulkTrackingNumberDetlListResponseDto);
	}

	/**
     * @Desc 일괄 송장 입력 내역 상세 목록  엑셀 다운로드
     * @param paramDto
     * @return ExcelDownloadDto
     * @throws Exception
     */
	@Override
	public ExcelDownloadDto getOrderBulkTrackingNumberDetlExcelList(OrderBulkTrackingNumberDetlListRequestDto paramDto) {

		String excelSheetName	= "sheet"; 					// 엑셀 파일 내 워크시트 이름
		String excelFileName	= "일괄 송장 입력 상세 내역";		// 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = {60, 220, 180, 350, 180};

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = {"center", "center", "center", "center", "center"};

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] propertyListOfFirstWorksheet = {"rowNum", "odid", "odOrderDetlSeq", "shippingCompNm", "trackingNo"};

		/*
		 * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		 * 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
		 */
		String[] firstHeaderListOfFirstWorksheet 	= { "No", "주문번호", "주문순번", "택배사", "송장번호"};

		/*
		 * 워크시트 DTO 생성 후 정보 세팅
		 */
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
				.workSheetName(excelSheetName) 				// 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) 		// 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet)		// 컬럼별 정렬 목록
				.build();

		/*
		 * 엑셀 다단 헤더 구성 : (헤더 행 index , 헤더 제목 배열) 형식으로 세팅
		 */
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); 	// 첫번째 헤더 컬럼

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		ArrayList<String> codeArray = null;

		// 화면에서 전송한 검색코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
		String ilItemCodeListStr = paramDto.getCodeSearch().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
		codeArray = StringUtil.getArrayListComma(ilItemCodeListStr);
		paramDto.setCodeArray(codeArray);

		List<OrderBulkTrackingNumberDetlDto> orderBulkTrackingNumberDetlDtoList = orderBulkTrackingNumberService.getOrderBulkTrackingNumberDetlExcelList(paramDto);

		firstWorkSheetDto.setExcelDataList(orderBulkTrackingNumberDetlDtoList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();
		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
		return excelDownloadDto;
	}

	/**
     * @Desc 주문상세 배송상태 업데이트
     * @param orderTrackingNumberVo(주문 상세별 송장번호 Vo(주문상세 PK, 택배사코드,개별송장번호, 등록자)), orderStatus(배송상태코드)
	 * @param orderStatusCd
     * @return ApiResult
     * @throws Exception
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putOrderDetailDeliveryStatus(OrderTrackingNumberVo orderTrackingNumberVo, String orderStatusCd) {
		// 주문상세 배송상태 변경 타입
		OrderEnums.OrderDetailDeliveryStatusType orderDetailDeliveryStatusType = OrderEnums.OrderDetailDeliveryStatusType.INSERT;

		// 주문상세 송장번호 테이블 기등록 송장정보 조회
		int OrderTrackingNumberCnt = orderBulkTrackingNumberService.getOrderTrackingNumberCnt(orderTrackingNumberVo.getOdOrderDetlId());

		String trackingNo = orderTrackingNumberVo.getTrackingNo().replaceAll("-","");
		orderTrackingNumberVo.setTrackingNo(trackingNo);

		// 등록
		if (OrderTrackingNumberCnt < 1) {
			orderBulkTrackingNumberService.addOrderTrackingNumber(orderTrackingNumberVo);
		// 수정
		} else {
			orderBulkTrackingNumberService.putOrderTrackingNumber(orderTrackingNumberVo);
			orderDetailDeliveryStatusType = OrderEnums.OrderDetailDeliveryStatusType.UPDATE;
		}

		// 주문상세정보 - 주문상세PK, 주문상태코드 수정
		OrderDetlVo orderDetlVo = OrderDetlVo.builder()
												.odOrderDetlId(orderTrackingNumberVo.getOdOrderDetlId())
												.orderStatusCd(orderStatusCd)
												.diId(orderTrackingNumberVo.getCreateId())
												.build();

		// 주문상세 - 정상주문상태 (배송중) 업데이트
		OrderEnums.OrderDetailStatusHistMsg orderDetailStatusHistMsg = OrderEnums.OrderDetailStatusHistMsg.findByCode(orderStatusCd);
		List<OrderStatusUpdateDto> orderStatusUpdateList = new ArrayList<>();
		OrderStatusUpdateDto orderStatusUpdateDto = new OrderStatusUpdateDto();

		orderStatusUpdateDto.setOdOrderDetlId(orderTrackingNumberVo.getOdOrderDetlId());
		orderStatusUpdateDto.setOrderStatusCd(orderStatusCd);
		orderStatusUpdateDto.setHistMsg(MessageFormat.format(orderDetailStatusHistMsg.getMessage(), "", orderTrackingNumberVo.getTrackingNo()));
		orderStatusUpdateDto.setDiId(orderTrackingNumberVo.getCreateId());
		orderStatusUpdateDto.setDrId(orderTrackingNumberVo.getCreateId());
		orderStatusUpdateList.add(orderStatusUpdateDto);

		orderStatusBiz.putOrderDetailStatus(orderStatusUpdateList);

		return ApiResult.success(orderDetailDeliveryStatusType);
	}

	/**
	 * 주문상세 송장번호 등록
	 * @param orderTrackingNumberVo
	 * @return
	 */
	@Override
	public int addOrderTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo) {
		return orderBulkTrackingNumberService.addOrderTrackingNumber(orderTrackingNumberVo);
	}

	/**
	 * 주문상세 송장번호 수정
	 * @param orderTrackingNumberVo
	 * @return
	 */
	@Override
	public int putOrderTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo) {
		return orderBulkTrackingNumberService.putOrderTrackingNumber(orderTrackingNumberVo);
	}
}