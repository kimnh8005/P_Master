package kr.co.pulmuone.v1.calculate.employee.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.order.dto.ErpIfRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeCalculateHeaderDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeExcelListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListResponseDto;
import kr.co.pulmuone.v1.calculate.employee.dto.OuIdListResponseDto;
import kr.co.pulmuone.v1.calculate.employee.dto.SettleEmployeeMasterConfirmRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.CalculateConfirmProcVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.LimitUsePriceExceDownloadVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SupportPriceExceDownloadlVo;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.constants.CalculateConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.CalculateEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 지원금 정산 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 03.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class EmployeeBizImpl implements EmployeeBiz {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	ErpApiExchangeService erpApiExchangeService;

	/**
	 * 부문 구문 전체 조회
	 * @return ApiResult<?>
	 */
	@Override
	public ApiResult<?> getOuIdAllList() throws Exception {

		List<SettleOuMngVo> SettleOuMngList = employeeService.getOuIdAllList();
		OuIdListResponseDto ouIdListResponseDto = OuIdListResponseDto.builder()
												  .rows(SettleOuMngList)
												  .build();

		return ApiResult.success(ouIdListResponseDto);
	}

	/**
	 * 임직원 지원금 정산 리스트 조회
	 * @param employeeListRequestDto
	 * @return ApiResult<?>
	 */
	@Override
	public ApiResult<?> getEmployeeList(EmployeeListRequestDto employeeListRequestDto) throws Exception {

		employeeListRequestDto.setConfirmList(employeeService.getSearchKeyToSearchKeyList(employeeListRequestDto.getFindConfirmYn(), Constants.ARRAY_SEPARATORS)); // 확정여부

		long totalCnt = employeeService.getEmployeeListCount(employeeListRequestDto);

		List<EmployeeListDto> employeeList = new ArrayList<>();
		if (totalCnt > 0) {
			employeeList = employeeService.getEmployeeList(employeeListRequestDto);
		}

		EmployeeListResponseDto employeeListResponseDto = EmployeeListResponseDto.builder()
														  .rows(employeeList)
														  .total(totalCnt)
														  .build();

		return ApiResult.success(employeeListResponseDto);
	}

	/**
	 * 임직원 지원금 한도 사용액 엑셀 다운로드
	 * @param employeeListRequestDto
	 * @return ApiResult<?>
	 */
	@Override
	public ExcelDownloadDto selectLimitUsePriceExceDownloadlList(EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception {

		List<LimitUsePriceExceDownloadVo> limitUsePriceExcelList = employeeService.selectLimitUsePriceExceDownloadlList(employeeExcelListRequestDto);

        String excelFileName = "임직원한도사용액";
        String excelSheetName = "sheet";
        /* 화면값보다 20더 하면맞다. */
        Integer[] widthListOfFirstWorksheet = { 180, 180, 180, 180, 180, 180, 180};

        String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center" };

        String[] propertyListOfFirstWorksheet = { "urErpEmployeeCd", "erpNm", "erpRegalNm", "erpOrganizationNm", "employeeDiscountPrice", "brandNm", "settleDay" };

        String[] firstHeaderListOfFirstWorksheet = { "아이디", "직원명", "소속법인명", "소속조직", "사용한도액", "채널명", "결제일자" };

        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
                                                               .workSheetName(excelSheetName)
                                                               .propertyList(propertyListOfFirstWorksheet)
                                                               .widthList(widthListOfFirstWorksheet)
                                                               .alignList(alignListOfFirstWorksheet)
                                                               .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        firstWorkSheetDto.setExcelDataList(limitUsePriceExcelList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	/**
	 * 임직원 지원금 지원금정산 엑셀 다운로드
	 * @param employeeListRequestDto
	 * @return ApiResult<?>
	 */
	@Override
	public ExcelDownloadDto selectSupportPriceExceDownloadlList(EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception {

		List<SupportPriceExceDownloadlVo> supportPriceExcelList = employeeService.selectSupportPriceExceDownloadlList(employeeExcelListRequestDto);

        String excelFileName = "임직원지원금정산";
        String excelSheetName = "sheet";
        /* 화면값보다 20더 하면맞다. */
        Integer[] widthListOfFirstWorksheet = { 180, 180, 180, 180, 180, 180, 180, 180, 180, 180,
        										180, 180, 180, 180, 180, 180, 180, 180, 180, 180,
        										180, 180, 180, 180, 180 };

        String[] alignListOfFirstWorksheet = { 	"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
        										"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
        										"center", "center", "center", "center", "center" };

        String[] propertyListOfFirstWorksheet = {	"legCd", "linNo", "glDat", "invDat", "accDes", "", "invAmt", "", "supNo", "supNam",
        											"payMet", "payTer","payGrp", "creDepCd", "creAccCd", "creOrg", "", "", "", "debLinNo",
        											"debLinAmt", "debLegCd", "debDepCd", "debAccCd", "debOrg" };

        String[] firstHeaderListOfFirstWorksheet = {	"전표구분", "순번", "GL일자", "송장일자", "Header비고", "세액", "총금액", "세금코드", "공급자사업자번호", "공급자명",
        												"지급방법", "결제조건", "지급그룹", "대변부서코드", "대변계정", "대변매장", "보조필드제목", "보조필드1", "보조필드2", "라인번호",
        												"라인금액", "차변법인", "차변부서", "차변계정", "차변매장"};

        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
                                                               .workSheetName(excelSheetName)
                                                               .propertyList(propertyListOfFirstWorksheet)
                                                               .widthList(widthListOfFirstWorksheet)
                                                               .alignList(alignListOfFirstWorksheet)
                                                               .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        firstWorkSheetDto.setExcelDataList(supportPriceExcelList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	/**
	 * 임직원 지원금 정산 확정 완료
	 * @param settleMonthList
	 * @param ouIdList
	 * @param sessionIdList
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> putCalculateConfirmProc(List<String> settleMonthList, List<String> ouIdList, List<Long> sessionIdList, String userId) throws Exception {

		for(int i=0;i<ouIdList.size();i++) {

			String settleMonth = settleMonthList.get(i);
			String ouId = ouIdList.get(i);
			long sessionId = sessionIdList.get(i);

			// 임직원 지원금 정산 확정 대상목록 조회
			List<CalculateConfirmProcVo> calculateConfirmProcList = employeeService.selectCalculateConfirmProcList(settleMonth, ouId, sessionId);

			// 임직원 지원금 정산 API
			int successCnt = employeeListCalculateApi(calculateConfirmProcList, sessionId);

			// 성공횟수가 대상목록횟수와 같을 경우 임직원정산 마스터 확정 업데이트
			if(calculateConfirmProcList.size() == successCnt) {
				SettleEmployeeMasterConfirmRequestDto confirmRequestDto = SettleEmployeeMasterConfirmRequestDto.builder()
																		.settleMonth(settleMonth)
																		.ouId(ouId)
																		.sessionId(sessionId)
																		.userId(userId)
																		.build();
				putSettleEmployeeMasterConfirm(confirmRequestDto);
			}

			if(successCnt == 0) return ApiResult.fail();

		}

		return ApiResult.success();
	}

	/**
	 * 임직원정산 (일마감) 정산여부 업데이트
	 * @param headerItem
	 * @param sessionId
	 * @return
	 */
	public void putSettleEmployeeDayYn(CalculateConfirmProcVo headerItem, long sessionId) throws Exception {
		employeeService.putSettleEmployeeDayYn(headerItem, sessionId);
	}

	/**
	 * 임직원정산 마스터 확정 업데이트
	 * @param confirmRequestDto
	 * @return void
	 */
	public void putSettleEmployeeMasterConfirm(SettleEmployeeMasterConfirmRequestDto confirmRequestDto) throws Exception {
		employeeService.putSettleEmployeeMasterConfirm(confirmRequestDto);
	}

	/**
	 * 임직원정산 이력 저장
	 * @param headerDto
	 * @return void
	 */
	public void addSettleEmployeeHist(EmployeeCalculateHeaderDto headerDto) throws Exception {
		employeeService.addSettleEmployeeHist(headerDto);
	}

	/**
	 * 임직원 지원금 정산 API
	 * @param calculateConfirmProcList
	 * @param sessionId
	 * @return void
	 */
	protected int employeeListCalculateApi(List<CalculateConfirmProcVo> calculateConfirmProcList, long sessionId) throws Exception  {

		int chitNo = 0; // 전표번호
		int successCnt = 0; // 성공횟수

		for(CalculateConfirmProcVo headerItem : calculateConfirmProcList) {

			if(headerItem.getDebitSideNo() == 1) chitNo++;

			// header dto 생성
			EmployeeCalculateHeaderDto headerDto = getEmployeeListCalculateHeader(headerItem, sessionId, chitNo);

			// ERP API 송신
			boolean isSuccess = sendErpApi(headerDto);

			// 성공시 임직원정산 (일마감) 정산여부 업데이트 및 이력저장
			if(isSuccess) {
				// 임직원정산 (일마감) 정산여부 업데이트
				putSettleEmployeeDayYn(headerItem, sessionId);
				// 임직원정산 이력 저장
				addSettleEmployeeHist(headerDto);
				successCnt++;
			}

		}

		return successCnt;
	}

	/**
	 * 임직원 지원금 정산 header dto 생성
	 * @param headerItem
	 * @param sessionId
	 * @param chitNo
	 * @return EmployeeCalculateHeaderDto
	 * @throws
	 */
	protected EmployeeCalculateHeaderDto getEmployeeListCalculateHeader(CalculateConfirmProcVo headerItem, long sessionId, int chitNo) {
		return EmployeeCalculateHeaderDto.builder()
										 .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
										 .sesId(sessionId) // AP 전표 생성용 세션값
										 .ouId(headerItem.getOuId()) // ERP의 OU ID
		                                 .perId(headerItem.getSettleMonth()) // 생성월(YYYYMM)
										 .creDat(headerItem.getCreateDt()) // 생성일자(sysdate)
										 .legCd(CalculateConstants.CORPORATION_CODE) // 법인 구분(고정값 FF)
										 .linNo(StringUtil.nvl(chitNo)) // 전표 차변 Line 번호(차변이 하나인 경우 1)
										 .glDat(headerItem.getEndDt()) // GL일자
										 .invDat(headerItem.getEndDt()) // 송장일자
										 .accDes(CalculateConstants.INVOICE_BRIEF) // 송장 적요(고정값 복리후생비-이샵)
										 .invAmt(StringUtil.nvl(headerItem.getDebitSidePrice())) // 전표 발행 총금액
										 .supNo(CalculateConstants.SUPPLIER_BUSINESS_NUMBER) // 공급자사업자번호(고정값 2009123104)
										 .supNam(CalculateConstants.SUPPLIER_NAME) // 공급자명(고정값 E-shop[임직원정산용])
										 .payMet(CalculateConstants.PAYMENT_METHOD) // 지급방법(고정값 CHECK)
				                         .payTer(CalculateConstants.PAYMENT_TERMS) // 결제조건(고정값 익월25일)
										 .payGrp(CalculateConstants.PAYMENT_GROUP) // 지급그룹(고정값 현금출금)
										 .creLegCd(headerItem.getFinRegalCd()) // 대변법인 (차변법인 값으로)
										 .creDepCd(headerItem.getCreDepCd()) // 대변부서
										 .creAccCd(CalculateConstants.CREDIT_ACCOUNT_CODE) // 대변 계정코드(고정값 214411)
										 .creOrg(CalculateConstants.CREDIT_DIVISION) // 대변 사업부 (고정값 000000)
										 .debLinNo(StringUtil.nvl(headerItem.getDebitSideNo())) // 차변 순번
										 .debLinAmt(StringUtil.nvl(headerItem.getTotalEmployeeDiscountPrice())) // 차변 공급가액
										 .debLegCd(headerItem.getFinRegalCd()) // 차변법인
										 .debDepCd(headerItem.getFinOrganizationCd()) // 차변부서
										 .debAccCd(headerItem.getFinAccountCd()) // 차변계정코드
										 .debOrg(CalculateConstants.DEBIT_DIVISION) // 차변사업부(고정값 000000)
										 .build();
	}

	/**
	 * ERP API 송신
	 * @param headerDto
	 * @return boolean
	 * @throws
	 */
	protected boolean sendErpApi(EmployeeCalculateHeaderDto headerDto) {

		// INTERFACE ID
		String erpInterfaceId = CalculateEnums.ErpInterfaceId.CUST_SETTLE_EMPLOYEE_INTERFACE_ID.getCode();

		// header 생성
		List<EmployeeCalculateHeaderDto> headerList = new ArrayList<>();
		headerList.add(headerDto);

		// request 생성
		ErpIfRequestDto requestDto =  ErpIfRequestDto.builder()
													 .totalPage(1)
													 .currentPage(1)
													 .header(headerList)
													 .build();

		// ERP API 송신
		BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.post(requestDto, erpInterfaceId);
		log.info("=====baseApiResponseVo: " + baseApiResponseVo);

		// 실패시 재시도 총 3회
		boolean isSuccess = erpApiFailRetry(baseApiResponseVo, requestDto, erpInterfaceId);

		return isSuccess;
	}

	/**
	 * ERP API 송신 실패시 재시도 총 3회
	 * @param baseApiResponseVo
	 * @param custOrdRequestDto
	 * @param erpInterfaceId
	 * @return void
	 * @throws
	 */
	protected boolean erpApiFailRetry(BaseApiResponseVo baseApiResponseVo, Object custOrdRequestDto, String erpInterfaceId) {

		boolean isSuccess = baseApiResponseVo.isSuccess();

		// 실패
		if(!baseApiResponseVo.isSuccess()) {

			for (int failCnt = 0; failCnt < BatchConstants.BATCH_FAIL_RETRY_COUNT; failCnt++) {

				BaseApiResponseVo retryBaseApiResponseVo = erpApiExchangeService.post(custOrdRequestDto, erpInterfaceId);

				isSuccess = retryBaseApiResponseVo.isSuccess();

				// 성공
				if (retryBaseApiResponseVo.isSuccess()) {
					log.info("=====baseApiResponseVo: " + baseApiResponseVo);
					break;
				}
				// 3번 재시도 모두 실패
				else {
					log.info("=====baseApiResponseVo: "+ failCnt + " " + baseApiResponseVo);
					if(failCnt == BatchConstants.BATCH_FAIL_RETRY_COUNT-1) {
						// 실패알람
						// TODO SMS, Slack 작업필요
					}
				}

			}

		}

		return isSuccess;
	}


}