package kr.co.pulmuone.v1.calculate.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.calculate.employee.dto.OuIdListResponseDto;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import kr.co.pulmuone.v1.calculate.order.dto.CalOrderListDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalOrderListRequestDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalOrderListResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 주문 정산 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class CalOrderBizImpl implements CalOrderBiz {

	@Autowired
	CalOrderService calOrderService;

	/**
	 * 부문 구문 전체 조회
	 * @return
	 */
	@Override
	public ApiResult<?> getOuIdAllList() {
		List<SettleOuMngVo> list = calOrderService.getOuIdAllList();
		OuIdListResponseDto ouIdListResponseDto = OuIdListResponseDto.builder()
				.rows(calOrderService.getOuIdAllList())
				.build();
		System.out.println(list);

		return ApiResult.success(ouIdListResponseDto);
	}

	/**
	 * 주문 정산 리스트 조회
	 * @param calOrderListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getOrderList(CalOrderListRequestDto calOrderListRequestDto) {

		calOrderListRequestDto.setOmSellersIdList(calOrderService.getSearchKeyToSearchKeyList(calOrderListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 구분
    	calOrderListRequestDto.setSalesOrderGubunList(calOrderService.getSearchKeyToSearchKeyList(calOrderListRequestDto.getSalesOrderGubun(), Constants.ARRAY_SEPARATORS)); // 판매처
    	calOrderListRequestDto.setBuyerTypeList(calOrderService.getSearchKeyToSearchKeyList(calOrderListRequestDto.getBuyerTypeCode(), Constants.ARRAY_SEPARATORS)); // 판매처
    	calOrderListRequestDto.setPaymentMethodList(calOrderService.getSearchKeyToSearchKeyList(calOrderListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 판매처

		long totalCnt = calOrderService.getOrderListCount(calOrderListRequestDto);

		List<CalOrderListDto> orderList = new ArrayList<>();
		if (totalCnt > 0) {
			orderList = calOrderService.getOrderList(calOrderListRequestDto);
		}

		return ApiResult.success(
				CalOrderListResponseDto.builder()
									.rows(orderList)
									.total(totalCnt)
									.build()
		);
	}

	/**
     * @Desc 주문 정산 리스트 엑셀 다운로드 목록 조회
     * @param CalGoodsListRequestDto : 상품 정산 리스트  검색 조건 request dto
     * @return ExcelDownloadDto ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     */
    @Override
    public ExcelDownloadDto getOrderListExportExcel(CalOrderListRequestDto calOrderListRequestDto) {

    	calOrderListRequestDto.setExcelYn("Y");
    	calOrderListRequestDto.setOmSellersIdList(calOrderService.getSearchKeyToSearchKeyList(calOrderListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 구분
    	calOrderListRequestDto.setSalesOrderGubunList(calOrderService.getSearchKeyToSearchKeyList(calOrderListRequestDto.getSalesOrderGubun(), Constants.ARRAY_SEPARATORS)); // 판매처
    	calOrderListRequestDto.setBuyerTypeList(calOrderService.getSearchKeyToSearchKeyList(calOrderListRequestDto.getBuyerTypeCode(), Constants.ARRAY_SEPARATORS)); // 판매처
    	calOrderListRequestDto.setPaymentMethodList(calOrderService.getSearchKeyToSearchKeyList(calOrderListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 판매처


        String excelFileName = "주문 정산 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
                200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
                200, 200, 200, 200 };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
               "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center" };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "typeNm", "agentTypeCdNm", "odid", "outmallId", "collectionMallId", "sellersNm", "storeName", "buyerTypeCdNm", "payTpNm", "pgServiceNm",
                "orderDt", "approvalDt", "salePrice", "shippingPrice", "paymentPrice", "settlePrice", "vatRemoveSettlePrice", "vat", "pointPrice", "ticketPointPrice",
                "freePointPrice","goodsCouponPrice","cartCouponPrice","shippingDiscountPrice" };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "구분", "주문경로(유형)", "주문번호", "외부몰 주문번호", "수집몰 주문번호", "판매처", "매장명", "주문자 유형", "결제수단", "PG",
                "주문일자", "결제일자/환불일자", "주문금액", "배송비", "결제금액(환불금액)", "매출금액", "매출금액(VAT제외)", "VAT", "적립금 사용금액", "적립금 사용금액(이용권)",
                "적립금 사용금액(무상)","상품 쿠폰 할인금액 합계","장바구니쿠폰 할인금액 합계" , "배송비 쿠폰 할인금액 합계"};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        // 상품정산 조회 조건 : 구분
        if( StringUtils.isNotEmpty(calOrderListRequestDto.getSalesOrderGubun()) && calOrderListRequestDto.getSalesOrderGubun().indexOf("ALL") < 0 ) {
        	calOrderListRequestDto.setSalesOrderGubunList(Stream.of(calOrderListRequestDto.getSalesOrderGubun().split(Constants.ARRAY_SEPARATORS))
								                .map(String::trim)
								                .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
								                .collect(Collectors.toList())); // 검색어
		}


        // 상품정산 조회 조건 : 결제수단
        if( StringUtils.isNotEmpty(calOrderListRequestDto.getPaymentMethodCode()) && calOrderListRequestDto.getPaymentMethodCode().indexOf("ALL") < 0 ) {
        	calOrderListRequestDto.setPaymentMethodList(Stream.of(calOrderListRequestDto.getPaymentMethodCode().split(Constants.ARRAY_SEPARATORS))
								                .map(String::trim)
								                .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
								                .collect(Collectors.toList())); // 검색어
		}

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<CalOrderListDto> itemList = calOrderService.getOrderList(calOrderListRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }
}