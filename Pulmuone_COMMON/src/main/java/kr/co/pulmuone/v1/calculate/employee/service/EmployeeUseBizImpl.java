package kr.co.pulmuone.v1.calculate.employee.service;

import kr.co.pulmuone.v1.calculate.employee.dto.*;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SupportPriceExceDownloadlVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 포인트 사용 현황 BizImpl
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
public class EmployeeUseBizImpl implements EmployeeUseBiz {

	@Autowired
	EmployeeUseService employeeUseService;

	/**
	 * 임직원 포인트 사용 현황리스트 조회
	 * @param employeeUseListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getEmployeeUseList(EmployeeUseListRequestDto employeeUseListRequestDto) {

		long totalCnt = employeeUseService.getEmployeeUseListCount(employeeUseListRequestDto);

		List<EmployeeUseListDto> employeeUseList = new ArrayList<>();
		if (totalCnt > 0) {
			employeeUseList = employeeUseService.getEmployeeUseList(employeeUseListRequestDto);
		}

		return ApiResult.success(
				EmployeeUseListResponseDto.builder()
									.rows(employeeUseList)
									.total(totalCnt)
									.build()
		);
	}

	/**
	 * 임직원 포인트 사용 현황 엑셀 리스트 조회
	 * @param employeeUseListRequestDto
	 * @return ExcelDownloadDto
	 * @throws Exception
	 */
	@Override
	public ExcelDownloadDto getEmployeeUseExcelList(EmployeeUseListRequestDto employeeUseListRequestDto) {

		employeeUseListRequestDto.setExcelYn("Y");
		List<EmployeeUseListDto> supportPriceExcelList = employeeUseService.getEmployeeUseList(employeeUseListRequestDto);

        String excelFileName = "임직원포인트사용현황";
        String excelSheetName = "sheet";
        /* 화면값보다 20더 하면맞다. */
        Integer[] widthListOfFirstWorksheet = { 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 320,
        										180, 180, 180, 180, 180, 180, 180 };

        String[] alignListOfFirstWorksheet = { 	"center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
        										"center", "center", "center", "center", "center", "center", "center" };

        String[] propertyListOfFirstWorksheet = {	"ouNm", "buyerNm", "urEmployeeCd", "erpRegalNm", "odid", "odOrderDetlSeq", "ilGoodsId", "ilItemCd", "itemBarcode", "masterNm", "goodsNm",
        											"orderStatus", "orderCnt","recommendedPrice", "salePrice", "supportPrice", "orderDt", "paymentDt" };

		String[] cellTypeListOfFirstWorksheet = {	"String", "String", "String", "String", "String", "String", "String", "String", "String", "String", "String",
													"String", "int","int", "int", "int", "String", "String" };

        String[] firstHeaderListOfFirstWorksheet = {	"부문구분", "주문자명", "사번", "소속", "주문번호", "상세번호", "상품코드", "마스터품목코드", "품목바코드", "임직원할인그룹", "상품명",
        												"주문상태", "수량", "정상가", "판매가", "회사지원액", "주문일자", "결제일자" };

        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
                                                               .workSheetName(excelSheetName)
                                                               .propertyList(propertyListOfFirstWorksheet)
															   .cellTypeList(cellTypeListOfFirstWorksheet)
                                                               .widthList(widthListOfFirstWorksheet)
                                                               .alignList(alignListOfFirstWorksheet)
                                                               .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        firstWorkSheetDto.setExcelDataList(supportPriceExcelList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}
}