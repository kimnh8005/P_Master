package kr.co.pulmuone.v1.policy.dailygoods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.policy.dailygoods.PolicyDailyGoodsPickMapper;
import kr.co.pulmuone.v1.policy.dailygoods.dto.PolicyDailyGoodsPickDto;
import kr.co.pulmuone.v1.policy.dailygoods.dto.vo.PolicyDailyGoodsPickVo;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20201014		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Service
@RequiredArgsConstructor
public class PolicyDailyGoodsPickService {

	@Autowired
	private final PolicyDailyGoodsPickMapper policyDailyGoodsPickMapper;

    /**
     * 일일상품 골라담기 허용여부 목록 조회
     *
     * @param PolicyDailyGoodsPickDto
     * @return PolicyDailyGoodsPickDto
     */
    protected PolicyDailyGoodsPickDto getPolicyDailyGoodsPickList(PolicyDailyGoodsPickDto dto) {
    	PolicyDailyGoodsPickDto result = new PolicyDailyGoodsPickDto();
    	PageMethod.startPage(dto.getPage(), dto.getPageSize());
    	Page<PolicyDailyGoodsPickVo> rows = policyDailyGoodsPickMapper.getPolicyDailyGoodsPickList(dto);
		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());
		return result;
    }
    /**
     * 일일상품 골라담기 허용여부 수정
     *
     * @param PolicyDailyGoodsPickDto
     * @return int
     */
    protected int putPolicyDailyGoodsPick(PolicyDailyGoodsPickDto dto) {
		return policyDailyGoodsPickMapper.putPolicyDailyGoodsPick(dto);
    }

    /**
	 * 일일상품 골라담기 허용여부 목록 조회 엑셀다운로드
	 *
	 * @param PolicyDailyGoodsPickDto
	 * @return ExcelDownloadDto
	 */
	protected ExcelDownloadDto getPolicyDailyGoodsPickListExportExcel(PolicyDailyGoodsPickDto dto) {
		String excelFileName = "일일상품골라담기허용여부엑셀다운로드";
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */

		Integer[] widthListOfFirstWorksheet = { 70, 120, 120, 170, 170, 170, 120 };

		String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center" };

		String[] propertyListOfFirstWorksheet = { "rnum", "pickableYn", "goodsId", "goodsName", "supplierName", "brandName", "createDt" };

		String[] firstHeaderListOfFirstWorksheet = { "No", "골라담기 허용여부", "상품코드", "상품명", "공급업체", "브랜드", "등록일" };

		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder().workSheetName(excelSheetName).propertyList(propertyListOfFirstWorksheet).widthList(widthListOfFirstWorksheet).alignList(alignListOfFirstWorksheet).build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		List<PolicyDailyGoodsPickVo> rows = policyDailyGoodsPickMapper.getPolicyDailyGoodsPickListExportExcel(dto);

		firstWorkSheetDto.setExcelDataList(rows);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

}
