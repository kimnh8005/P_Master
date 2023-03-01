package kr.co.pulmuone.v1.order.order.service;

import java.io.File;
import java.util.*;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.util.ExcelDownloadUtil;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsResponseDto;
import kr.co.pulmuone.v1.system.log.dto.ExcelDownloadAsyncRequestDto;
import kr.co.pulmuone.v1.system.log.service.SystemLogBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalStatus;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.CSRefundTp;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderExcelNm;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.policy.excel.service.PolicyExcelTmpltBiz;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 *  1.1    2020. 12. 15.            석세동         수정
 *  1.2    2021. 01. 11.            김명진         엑셀다운로드 추가
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class OrderListBizImpl implements OrderListBiz {
    @Autowired
    private OrderListService orderListService;

    @Autowired
	private PolicyExcelTmpltBiz policyExcelTmpltBiz;

	@Autowired
	private OrderListAsyncBiz orderListAsyncBiz;

	@Autowired
	private ExcelDownloadUtil excelDownloadUtil;

	@Autowired
	private SystemLogBiz systemLogBiz;

	/**
	 * 주문 리스트
	 * @param orderListRequestDto
	 * @return
	 */
    @Override
	@UserMaskingRun(system = "MUST_MASKING")
    public ApiResult<?> getOrderList(OrderListRequestDto orderListRequestDto) {

    	orderListService.setOrderListSearchParam(orderListRequestDto);

        return ApiResult.success(orderListService.getOrderList(orderListRequestDto));
    }

	/**
	 * 주문 리스트 엑셀 다운로드
	 * @param orderListRequestDto
	 * @return
	 */
    @Override
    public ExcelDownloadDto getOrderExcelList(OrderListRequestDto orderListRequestDto) {

    	orderListService.setOrderListSearchParam(orderListRequestDto);

       	List<OrderExcelListDto> orderList = orderListService.getOrderExcelList(orderListRequestDto);

        String excelFileName = OrderExcelNm.ODL.getCodeName();
        String excelSheetName = "sheet";
        /* 화면값보다 20더 하면맞다. */
        Integer[] widthListOfFirstWorksheet = { 180, 180, 180, 200, 200, 200, 200, 200, 200, 320, 180, 120, 120, 120, 120, 150, 150, 120, 200 };

        String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
            "center", "center", "center", "center", "center", "center" };

        String[] propertyListOfFirstWorksheet = { "orderCreateDt", "odid", "sellersNm", "buyerNm", "buyerHp", "recvNm", "recvHp", "recvAddr1", "recvAddr2", "goodsNm", "orderPrice",
            "shippingPrice", "couponPrice", "paidPrice", "orderPaymentType", "statusNm", "claimStatusNm", "agentType", "collectionMallId" };

        String[] firstHeaderListOfFirstWorksheet = { "주문일자", "주문번호", "판매처", "주문자정보", "주문자연락처", "수취인명", "수취인연락처", "주소1", "주소2", "상품명", "주문금액", "배송비합계", "쿠폰할인합계", "결제금액",
            "결제수단", "주문상태", "클레임상태", "유형", "수집몰주문번호" };

		String[] cellTypeListOfFirstWorksheet = {  "String", "String", "String", "String", "String", "String", "String", "String", "String", "String", "int", "int", "int", "int",
				"String", "String", "String", "String", "String"};

        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
                                                               .workSheetName(excelSheetName)
                                                               .propertyList(propertyListOfFirstWorksheet)
                                                               .widthList(widthListOfFirstWorksheet)
                                                               .alignList(alignListOfFirstWorksheet)
                                                               .cellTypeList(cellTypeListOfFirstWorksheet)
                                                               .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        firstWorkSheetDto.setExcelDataList(orderList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

	/**
	 * 매장 주문 리스트 엑셀 다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	@Override
	public ExcelDownloadDto getShopOrderExcelList(OrderListRequestDto orderListRequestDto) {

		orderListService.setOrderListSearchParam(orderListRequestDto);

		orderListRequestDto.setExcelYn("Y");
		OrderListResponseDto orderListResponseDto = orderListService.getOrderList(orderListRequestDto);
		List<OrderListDto> orderList = orderListResponseDto.getRows();

		String excelFileName = "매장 주문리스트";
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */
		Integer[] widthListOfFirstWorksheet = { 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180,
				180, 180, 180, 180, 180 };

		String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center" };

		String[] propertyListOfFirstWorksheet = { "createDt", "odid", "urStoreNm", "deliveryTypeNm", "storeScheduleNo", "storeStartTime", "storeEndTime", "deliveryDt", "buyerNm", "loginId", "goodsNm", "statusNm",
				"claimStatusNm", "orderPrice", "shippingPrice", "couponPrice", "paidPrice" };

		String[] firstHeaderListOfFirstWorksheet = { "주문일자", "주문번호", "매장명", "배송방법", "회차", "배송시작시간", "배송종료시간", "도착예정일", "주문자명", "아이디", "상품명", "주문상태",
				"클레임상태", "주문금액", "배송비합계", "쿠폰할인합계", "결제금액" };

		String[] cellTypeListOfFirstWorksheet = { "string", "string", "string", "string", "string", "string", "string", "string", "string", "string", "string", "string",
				"string", "int", "int", "int", "int" };

		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
				.workSheetName(excelSheetName)
				.propertyList(propertyListOfFirstWorksheet)
				.widthList(widthListOfFirstWorksheet)
				.alignList(alignListOfFirstWorksheet)
				.cellTypeList(cellTypeListOfFirstWorksheet)
				.build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		firstWorkSheetDto.setExcelDataList(orderList);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	/**
	 * 주문 상세 리스트
	 * @param orderListRequestDto
	 * @return
	 */
    @Override
	@UserMaskingRun(system = "MUST_MASKING")
    public ApiResult<?> getOrderDetailList(OrderListRequestDto orderListRequestDto) {

    	orderListService.setOrderDetailListSearchParam(orderListRequestDto);

        return ApiResult.success(orderListService.getOrderDetailList(orderListRequestDto));
    }

	/**
	 * 주문 상세 리스트 엑셀다운로드
	 * @param orderListRequestDto
	 * @return
	 */
    @Override
    public ExcelDownloadDto getOrderDetailExcelList(OrderListRequestDto orderListRequestDto) {

    	orderListService.setOrderDetailListSearchParam(orderListRequestDto);

       	List<OrderDetailExcelListDto> orderDetailList = orderListService.getOrderDetailExcelList(orderListRequestDto);

       	String excelFileName = orderListRequestDto.getPsExcelNm(); // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        //엑셀다운로드 양식을 위한 공통
        ExcelWorkSheetDto firstWorkSheetDto = policyExcelTmpltBiz.getCommonDownloadExcelTmplt(orderListRequestDto.getPsExcelTemplateId());
        firstWorkSheetDto.setExcelDataList(orderDetailList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();
        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

	/**
	 * 주문 상세 리스트 엑셀다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	@Override
	public DataDownloadStaticsResponseDto getOrderDetailExcelListMake(OrderListRequestDto orderListRequestDto) throws Exception {
		// dto 값 보정
		UserVo userVo = SessionUtil.getBosUserVO();
		if(userVo == null) return null;

		orderListService.setOrderDetailListSearchParam(orderListRequestDto);
		List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
		listAuthWarehouseId.removeIf(org.apache.commons.lang.StringUtils::isEmpty);
		List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
		listAuthSupplierId.removeIf(org.apache.commons.lang.StringUtils::isEmpty);
		List<String> listAuthStoreId = userVo.getListAuthStoreId();
		listAuthStoreId.removeIf(org.apache.commons.lang.StringUtils::isEmpty);
		List<String> listAuthSellersId = userVo.getListAuthSellersId();
		listAuthSellersId.removeIf(org.apache.commons.lang.StringUtils::isEmpty);

		orderListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
		orderListRequestDto.setListAuthSupplierId(listAuthSupplierId);
		orderListRequestDto.setListAuthStoreId(listAuthStoreId);
		orderListRequestDto.setListAuthSellersId(listAuthSellersId);

		//파일명 변환
		String loginId = userVo.getLoginId();
		String toDate = DateUtil.getCurrentDate("yyyyMMddHHmmss");
		String toDateYYYYMMDD = toDate.substring(0, 8);
		String excelFileExtension = ".xlsx";
		String filePath = excelDownloadUtil.getCacheLocalFilePath(toDateYYYYMMDD + File.separator + OrderExcelNm.ODD.getCode() + "_" + loginId + "_" + toDate + excelFileExtension);
		orderListRequestDto.setPsExcelNm(filePath);

		// 비동기 정보 저장
		ExcelDownloadAsyncRequestDto excelAsyncRequestDto = new ExcelDownloadAsyncRequestDto();
		excelAsyncRequestDto.setUrUserId(Long.valueOf(userVo.getUserId()));
		excelAsyncRequestDto.setFileName(filePath);
		systemLogBiz.addExcelDownloadAsync(excelAsyncRequestDto);

		// 비동기 실행
		orderListAsyncBiz.runOrderDetailExcelMake(orderListRequestDto, excelAsyncRequestDto.getStExcelDownloadAsyncId());

		// response 값 설정
		String dataDownloadString = excelAsyncRequestDto.getStExcelDownloadAsyncId() + "___" + filePath;
		return DataDownloadStaticsResponseDto.builder()
				.dataDownloadTypeResult(Collections.singletonList(dataDownloadString))
				.build();
	}

	/**
	 * 미출 주문 상세 리스트
	 * @param orderListRequestDto
	 * @return
	 */
	@Override
	@UserMaskingRun(system = "MUST_MASKING")
	public ApiResult<?> getUnreleasedOrderDetailList(OrderListRequestDto orderListRequestDto) {

		if(StringUtil.isNotEmpty(orderListRequestDto.getSearchEtc())){
			List<String> searchEtcList = StringUtil.getArrayList(orderListRequestDto.getSearchEtc());
			orderListRequestDto.setUnprocessMissYn( searchEtcList.stream().filter(searchEtc -> OrderEnums.MissEtc.UNPROCESS_MISS.getCode().equals(searchEtc)).count() > 0 ? "Y" : "N" );
			orderListRequestDto.setReturnMissYn( searchEtcList.stream().filter(searchEtc -> OrderEnums.MissEtc.RETURN_MISS.getCode().equals(searchEtc)).count() > 0 ? "Y" : "N" );
		}

		return getOrderDetailList(orderListRequestDto);
	}

	/**
	 * 미출 주문 상세 리스트 엑셀 다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	@Override
	public ExcelDownloadDto getUnreleasedOrderDetailExcelList(OrderListRequestDto orderListRequestDto) {

		orderListRequestDto.setExcelYn("Y");
		ApiResult<?> apiResult = getUnreleasedOrderDetailList(orderListRequestDto);

		OrderDetailListResponseDto orderDetailListResponseDto = (OrderDetailListResponseDto) apiResult.getData();

		List<OrderDetailListDto> orderDetailList = orderDetailListResponseDto.getRows();

		List<OrderDetailListDto> unreleasedOrderList = new ArrayList<>();
		int i = orderDetailList.size();

		for(OrderDetailListDto dto : orderDetailList){

			// 미출사유
			String missReason = StringUtil.nvl(dto.getMissReason()) == "" ? "" : "미출사유 : " + StringUtil.nvl(dto.getMissReason()) + ", 미출상세사유 : " + StringUtil.nvl(dto.getMissMsg());
			dto.setMissReason(missReason);
			dto.setRowNumber(i--);

			// 잔여처리수량
			int processMissCnt = StringUtil.nvlInt(dto.getMissCnt()) - StringUtil.nvlInt(dto.getMissClaimCnt());
			if("NONE".equals(dto.getMissProcessYn())) {
				processMissCnt = 0;
			}
			else if(StringUtil.nvlInt(dto.getMissCnt()) > 0 && StringUtil.nvlInt(dto.getMissClaimCnt()) == 0){
				processMissCnt = dto.getMissCnt();
			}
			dto.setProcessMissCnt(processMissCnt);

			unreleasedOrderList.add(dto);

		}

		String excelFileName = "미출 주문상세리스트";
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */
		Integer[] widthListOfFirstWorksheet = {	180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180,
				180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180,
                180, 180, 180, 180, 180 };

		String[] alignListOfFirstWorksheet = {	"center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center", "center"  };
		String[] propertyListOfFirstWorksheet = {	"rowNumber", "missDt", "urSupplierNm", "omSellersNm", "goodsTp", "warehouseNm", "odid", "odOrderDetlSeq", "itemBarcode", "ilItemCd", "ilGoodsId" , "goodsNm", "orderCnt",
                "missCnt", "missClaimCnt", "processMissCnt", "orderStatus", "buyerNm", "buyerHp", "recvNm", "recvHp", "recommendedPrice", "salePrice", "orderPrice", "couponPrice",
                "paidPrice", "missReason", "bosClaimNm", "claimStatusTpNm", "collectionMallId"  };

		String[] cellTypeListOfFirstWorksheet = {	"int", "String", "String", "String", "String", "String", "String", "String", "String", "String", "String", "String", "int",
				"int", "int", "int", "String", "String", "String", "String", "String", "int", "int", "int", "int",
                "int", "String", "String", "String", "String"  };

		String[] firstHeaderListOfFirstWorksheet = {	"No", "미출일자", "공급처", "판매처(외부몰 주문번호)", "주문유형(출고처)", "출고처(배송방법)", "주문번호", "상세번호", "품목바코드", "ERP코드", "상품코드", "상품명", "주문수량",
                "미출수량", "선미출수량", "잔여처리수량", "주문상태", "주문자정보", "주문자 핸드폰번호", "수령자명", "수령자 핸드폰번호", "정상가", "판매가", "주문금액", "쿠폰할인",
                "결제금액", "미출사유", "클레임사유", "상태변경", "수집몰주문번호" };

		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
				.workSheetName(excelSheetName)
				.propertyList(propertyListOfFirstWorksheet)
				.cellTypeList(cellTypeListOfFirstWorksheet)
				.widthList(widthListOfFirstWorksheet)
				.alignList(alignListOfFirstWorksheet)
				.build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		firstWorkSheetDto.setExcelDataList(unreleasedOrderList);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);


		return excelDownloadDto;
	}

	/**
	 * 반품 주문 상세 리스트
	 * @param orderListRequestDto
	 * @return
	 */
	@Override
	@UserMaskingRun(system = "MUST_MASKING")
	public ApiResult<?> getReturnOrderDetailList(OrderListRequestDto orderListRequestDto) {
		setReturnClaimState(orderListRequestDto);
       	return getOrderDetailList(orderListRequestDto);
	}

	/**
	 * 반품 주문 상세 리스트 엑셀 다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	@Override
	public ExcelDownloadDto getReturnOrderDetailExcelList(OrderListRequestDto orderListRequestDto) {

		setReturnClaimState(orderListRequestDto);

		return getOrderDetailExcelList(orderListRequestDto);
	}

	/**
	 * 반품 사유 코드 목록 조회
	 * @param
	 * @return
	 */
	@Override
	public ApiResult<?> getReturnReasonList() {
		GetCodeListResponseDto result = new GetCodeListResponseDto();
		List<GetCodeListResultVo> rows = orderListService.getReturnReasonList();
		result.setRows(rows);
		return ApiResult.success(result);
	}

	/**
	 * 환불 주문 상세 리스트
	 * @param orderListRequestDto
	 * @return
	 */
	@Override
	@UserMaskingRun(system = "MUST_MASKING")
	public ApiResult<?> getRefundOrderDetailList(OrderListRequestDto orderListRequestDto) {
		setReFundClaimState(orderListRequestDto);
		return getOrderDetailList(orderListRequestDto);
	}

	/**
	 * 환불 주문 상세 리스트 엑셀 다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	@Override
	public ExcelDownloadDto getRefundOrderDetailExcelList(OrderListRequestDto orderListRequestDto) {

		setReFundClaimState(orderListRequestDto);

		return getOrderDetailExcelList(orderListRequestDto);
	}

	/**
	 * CS환불 리스트 조회
	 * @param csRefundRequest
	 * @return
	 */
	@Override
	@UserMaskingRun(system = "BOS")
	public ApiResult<?> getCsRefundApprovalList(OrderListRequestDto csRefundRequest) {
//		setCsRefundApproveCdList(orderListRequestDto);
//		setcsRefundTpList(orderListRequestDto);
		orderListService.setCSRefundRequestParam(csRefundRequest);
		return ApiResult.success(orderListService.getCSRefundList(csRefundRequest));
	}

	/**
	 * CS환불 승인리스트 엑셀다운로드
	 * @param csRefundRequest
	 * @return
	 */
	@Override
	public ExcelDownloadDto getCsRefundApprovalExcelList(OrderListRequestDto csRefundRequest) {
//		setCsRefundApproveCdList(orderListRequestDto);
//		setcsRefundTpList(orderListRequestDto);
//		orderListService.setOrderDetailListSearchParam(orderListRequestDto);
//      List<OrderDetailExcelListDto> orderDetailList = orderListService.getOrderDetailExcelList(orderListRequestDto);
		orderListService.setCSRefundRequestParam(csRefundRequest);
		List<OrderCSRefundListDto> csRefundExcelList = orderListService.getCSRefundExcelList(csRefundRequest);

        String excelFileName = OrderExcelNm.CS.getCodeName();
        String excelSheetName = "sheet";
        /* 화면값보다 20더 하면맞다. */
        Integer[] widthListOfFirstWorksheet = {	200, 200, 120, 200, 120, 200, 200, 200, 340, 200,
												200, 200, 340, 200, 200, 200, 200, 120, 200, 200,
												200, 200, 340};
        String[] alignListOfFirstWorksheet = { 	"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
												"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
												"center", "center", "center"};

		String[] propertyListOfFirstWorksheet = {	"apprReqDt", "apprChgDt", "omSellersNm", "odid", "odOrderDetlSeq", "ilItemCd", "ilGoodsId", "compNm", "goodsNm", "salesSettleYn",
													"taxYn", "unreleasedYn", "warehouseNm", "csRefundTpNm", "bankNm", "accountHolder", "accountNumber", "refundPrice", "bosClaimLargeNm", "bosClaimMiddleNm",
													"bosClaimSmallNm", "csReasonMsg", "csRefundApproveCdNm" };

        String[] firstHeaderListOfFirstWorksheet = { "요청일자", "승인일자", "판매처", "주문번호", "상세번호", "마스터품목코드", "상품코드", "공급업체", "상품명", "매출여부",
													"과세구분", "미출여부", "출고처", "CS환불구분", "환불은행명", "예금주명", "계좌번호", "CS환불금액", "클레임사유(대)", "클레임사유(중)",
													"귀책구분", "상세사유", "처리상태" };

        String[] cellTypeListOfFirstWorksheet = {	"String", "String", "String", "String", "String", "String", "String", "String", "String", "String",
													"String", "String", "String", "String", "String", "String", "String", "int", "String", "String",
													"String", "String", "String"};

        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
                                                               .workSheetName(excelSheetName)
                                                               .propertyList(propertyListOfFirstWorksheet)
                                                               .widthList(widthListOfFirstWorksheet)
                                                               .alignList(alignListOfFirstWorksheet)
                                                               .cellTypeList(cellTypeListOfFirstWorksheet)
                                                               .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        firstWorkSheetDto.setExcelDataList(csRefundExcelList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
	}

	/**
	 * 반품 주문 클레임 코드 설정
	 * @param orderListRequestDto
	 */
	private void setReturnClaimState(OrderListRequestDto orderListRequestDto) {
       	if("singleSection".equals(orderListRequestDto.getSelectConditionType()) && !StringUtils.isEmpty(orderListRequestDto.getClaimStateSingle())) {
       		orderListRequestDto.setClaimState(orderListRequestDto.getClaimStateSingle());
       	}

		if(orderListRequestDto.getClaimState().startsWith("ALL") || StringUtils.isEmpty(orderListRequestDto.getClaimState())) {
			orderListRequestDto.setClaimState(
												OrderStatus.RETURN_APPLY.getCode() + Constants.ARRAY_SEPARATORS +
												OrderStatus.RETURN_ING.getCode() + Constants.ARRAY_SEPARATORS +
												OrderStatus.RETURN_DEFER.getCode() + Constants.ARRAY_SEPARATORS +
												OrderStatus.RETURN_COMPLETE.getCode()
											);
		}

	}

	/**
	 * 환불 주문 클레임 코드 설정
	 * @param orderListRequestDto
	 */
	private void setReFundClaimState(OrderListRequestDto orderListRequestDto) {
		if(orderListRequestDto.getClaimState().startsWith("ALL") || StringUtils.isEmpty(orderListRequestDto.getClaimState())) {
			orderListRequestDto.setClaimState(
												OrderStatus.CANCEL_COMPLETE.getCode() + Constants.ARRAY_SEPARATORS +
												OrderStatus.RETURN_COMPLETE.getCode()
											);
		}
	}

	/**
	 * CS환불 승인상태 설정
	 * @param orderListRequestDto
	 */
	private void setCsRefundApproveCdList(OrderListRequestDto orderListRequestDto) {

		System.out.println("orderListRequestDto.getCsRefundApproveCd() : " + orderListRequestDto.getCsRefundApproveCd());

		if(orderListRequestDto.getCsRefundApproveCd().startsWith("ALL") || StringUtils.isEmpty(orderListRequestDto.getCsRefundApproveCd())) {
			orderListRequestDto.setCsRefundApproveCd(
					ApprovalStatus.SAVE.getCode() + Constants.ARRAY_SEPARATORS +		// 저장
					ApprovalStatus.REQUEST.getCode() + Constants.ARRAY_SEPARATORS +		// 승인요청
					ApprovalStatus.APPROVED.getCode() + Constants.ARRAY_SEPARATORS +	// 승인완료
					ApprovalStatus.DENIED.getCode()										// 승인반려
											);
		}
	}

	/**
	 * CS환불구분 설정
	 * @param orderListRequestDto
	 */
	private void setcsRefundTpList(OrderListRequestDto orderListRequestDto) {
		if(orderListRequestDto.getCsRefundTp().startsWith("ALL") || StringUtils.isEmpty(orderListRequestDto.getCsRefundTp())) {
			orderListRequestDto.setCsRefundTp(
					CSRefundTp.PAYMENT_PRICE_REFUND.getCode() + Constants.ARRAY_SEPARATORS +
					CSRefundTp.POINT_PRICE_REFUND.getCode()
											);
		}
	}

	/**
	 * 상세리스트 카운트 요약정보
	 * @param orderListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getOrderTotalCountInfo(OrderListRequestDto orderListRequestDto) {

		orderListService.setOrderDetailListSearchParam(orderListRequestDto);

		return ApiResult.success(orderListService.getOrderTotalCountInfo(orderListRequestDto));
	}
}
