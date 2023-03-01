package kr.co.pulmuone.v1.promotion.adminpointpaymentuse.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.promotion.adminpointpaymentuse.AdminPointPaymentUseMapper;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.AdminPointPaymentUseListRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.vo.AdminPointPaymentUseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminPointPaymentUseService {

	private final AdminPointPaymentUseMapper adminPointPaymentUseMapper;

    /**
     * 관리자 적립금 설정 지급/ 설정 조회
     *
     * @param pointSettingListRequestDto
     * @return PointSettingListResponseDto
     * @throws Exception
     */
    protected Page<AdminPointPaymentUseVo> getAdminPointPaymentUseList(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception {
    	PageMethod.startPage(adminPointPaymentUseListRequestDto.getPage(), adminPointPaymentUseListRequestDto.getPageSize());
        return adminPointPaymentUseMapper.getAdminPointPaymentUseList(adminPointPaymentUseListRequestDto);
    }


    /**
	 * 관리자 적립금 지급/차감 내역  엑셀 다운로드 목록 조회
	 *
	 * @param getBuyerListRequestDto
	 * @return ExcelDownloadDto
	 * @throws Exception
	 */
	protected ExcelDownloadDto adminPointPaymentUseListExportExcel(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception {
		String excelFileName = "관리자 적립금 지급/차감 내역 엑셀다운로드";
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */
		Integer[] widthListOfFirstWorksheet = { 100, 200, 400, 200, 200, 200, 200, 200, 200, 200, 200 };

		String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "left" };

		String[] propertyListOfFirstWorksheet = { "rowNumber", "pointDetailTypeName", "erpOrganizationName", "adminUserName", "adminUserId", "urUserName", "urUserId", "paymentTypeName", "issueValue", "pointUseDate", "pointUsedMsg" };

		String[] firstHeaderListOfFirstWorksheet = { "No", "상세구분", "조직명", "처리자명", "처리자 ID", "회원명", "회원 ID", "구분", "내역", "지급/차감 일자", "비고"};
//		String[] secondtHeaderListOfFirstWorksheet = { "No", "회원유형", "회원명", "회원ID", "휴대폰", "EMAIL", "가입일자", "최근방문일자", "SMS", "EMAIL", "PUSH", "회원등급", "회원상태" };

		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder().workSheetName(excelSheetName).propertyList(propertyListOfFirstWorksheet).widthList(widthListOfFirstWorksheet).alignList(alignListOfFirstWorksheet).build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		List<AdminPointPaymentUseVo>  adminPointPaymentUseVo = null;
		try
		{

			adminPointPaymentUseVo = adminPointPaymentUseListExcel(adminPointPaymentUseListRequestDto);
			log.info("adminPointPaymentUseVo {}", adminPointPaymentUseVo);
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			throw e; // 추후 CustomException 으로 변환 예정
		}
		firstWorkSheetDto.setExcelDataList(adminPointPaymentUseVo);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}


	/**
	 * 관리자 적립금 지급/차감 내역 조회 엑셀다운로드
	 *
	 * @param getBuyerListRequestDto
	 * @return GetSerialNumberListResultVo
	 * @throws Exception
	 */
	@UserMaskingRun(system="BOS")
	protected List<AdminPointPaymentUseVo> adminPointPaymentUseListExcel(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception
	{

		 if (StringUtils.isNotEmpty(adminPointPaymentUseListRequestDto.getInputSearchValue())) {
			 adminPointPaymentUseListRequestDto.setSearchValueList(Stream.of(adminPointPaymentUseListRequestDto.getInputSearchValue().split("\n|,"))
	                    .map(String::trim)
	                    .filter(StringUtils::isNotEmpty)
	                    .collect(Collectors.toList()));
	        }
		 adminPointPaymentUseListRequestDto.setPointType(PointEnums.PointType.ADMIN.getCode());
		 List<AdminPointPaymentUseVo> result = adminPointPaymentUseMapper.adminPointPaymentUseListExcel(adminPointPaymentUseListRequestDto);

		// 화면과 동일하게 역순으로 no 지정
        for (int i = result.size() - 1; i >= 0; i--) {
        	result.get(i).setRowNumber(String.valueOf(result.size() - i));
        }

		return result;
	}

}
