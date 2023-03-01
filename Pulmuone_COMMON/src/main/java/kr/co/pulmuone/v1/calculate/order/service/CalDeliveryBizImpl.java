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
import kr.co.pulmuone.v1.calculate.order.dto.CalDeliveryListDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalDeliveryListRequestDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalDeliveryListResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 택배비 내역 BizImpl
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
public class CalDeliveryBizImpl implements CalDeliveryBiz {

	@Autowired
	CalDeliveryService calDeliveryService;

	/**
	 * 부문 구문 전체 조회
	 * @return
	 */
	@Override
	public ApiResult<?> getOuIdAllList() {
		List<SettleOuMngVo> list = calDeliveryService.getOuIdAllList();
		OuIdListResponseDto ouIdListResponseDto = OuIdListResponseDto.builder()
				.rows(calDeliveryService.getOuIdAllList())
				.build();
		System.out.println(list);

		return ApiResult.success(ouIdListResponseDto);
	}

	/**
	 * 택배비 내역 리스트 조회
	 * @param calDeliveryListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getDeliveryList(CalDeliveryListRequestDto calDeliveryListRequestDto) {

		calDeliveryListRequestDto.setSalesDeliveryGubunList(calDeliveryService.getSearchKeyToSearchKeyList(calDeliveryListRequestDto.getSalesDeliveryGubun(), Constants.ARRAY_SEPARATORS)); // 구분
		calDeliveryListRequestDto.setOmSellersIdList(calDeliveryService.getSearchKeyToSearchKeyList(calDeliveryListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처

		long totalCnt = calDeliveryService.getDeliveryListCount(calDeliveryListRequestDto);

		List<CalDeliveryListDto> deliveryList = new ArrayList<>();
		if (totalCnt > 0) {
			deliveryList = calDeliveryService.getDeliveryList(calDeliveryListRequestDto);
		}

		return ApiResult.success(
				CalDeliveryListResponseDto.builder()
									.rows(deliveryList)
									.total(totalCnt)
									.build()
		);
	}


	/**
     * @Desc 택배비 내역 리스트 엑셀 다운로드 목록 조회
     * @param CalDeliveryListRequestDto : 택배비 내역 리스트  검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     */
    @Override
    public ExcelDownloadDto getDeliveryListExportExcel(CalDeliveryListRequestDto calDeliveryListRequestDto) {
    	calDeliveryListRequestDto.setExcelYn("Y");
    	calDeliveryListRequestDto.setSalesDeliveryGubunList(calDeliveryService.getSearchKeyToSearchKeyList(calDeliveryListRequestDto.getSalesDeliveryGubun(), Constants.ARRAY_SEPARATORS)); // 구분
		calDeliveryListRequestDto.setOmSellersIdList(calDeliveryService.getSearchKeyToSearchKeyList(calDeliveryListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처

        String excelFileName = "택배비 내역 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                200, 200, 200, 400, 400, 200, 200, 200, 200, 200, 200,
                200, 300, 300, 200, 300 };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center", "center" };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "divNm", "odid", "pgServiceNm", "outmallId", "collectionMallId", "omSellersId", "warehouseName", "storeName", "compNm", "odShippingPriceId", "conditionTpNm",
                "psClaimMallId","shippingPrice","returnShippingPrice","shippingDiscountPrice","settleDt" };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "구분", "주문번호", "PG", "외부몰 주문번호", "수집몰 주문번호", "판매처", "출고처", "매장명", "공급업체", "배송비번호", "조건 배송비 구분",
                "귀책구분","배송비(클레임배송비)","환불배송비","배송비 할인금액" , "배송중일자(반품승인일자)"};

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
        if( StringUtils.isNotEmpty(calDeliveryListRequestDto.getSalesDeliveryGubun()) && calDeliveryListRequestDto.getSalesDeliveryGubun().indexOf("ALL") < 0 ) {
        	calDeliveryListRequestDto.setSalesDeliveryGubunList(Stream.of(calDeliveryListRequestDto.getSalesDeliveryGubun().split(Constants.ARRAY_SEPARATORS))
								                .map(String::trim)
								                .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
								                .collect(Collectors.toList())); // 검색어
		}


        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<CalDeliveryListDto> itemList = calDeliveryService.getDeliveryList(calDeliveryListRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }

}