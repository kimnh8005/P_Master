package kr.co.pulmuone.v1.policy.origin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.policy.origin.PolicyOriginMapper;
import kr.co.pulmuone.v1.policy.origin.dto.basic.AddPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DelPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DuplicatePolicyOriginCountParamDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginTypeListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.PutPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginListResultVo;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginResultVo;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginTypeListResultVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyOriginService {

    @Autowired
    private final PolicyOriginMapper policyOriginMapper;

    /**
     * @Desc  원산지 목록 조회
     * @param getPolicyOriginListRequestDto
     * @throws Exception
     * @return Page<GetPolicyOriginListResultVo>
     */
    protected Page<GetPolicyOriginListResultVo> getOriginList(GetPolicyOriginListRequestDto getPolicyOriginListRequestDto) {
        PageMethod.startPage(getPolicyOriginListRequestDto.getPage(), getPolicyOriginListRequestDto.getPageSize());
        return policyOriginMapper.getOriginList(getPolicyOriginListRequestDto);
    }

    /**
     * @Desc  원산지 구분 목록 조회
     * @param getPolicyOriginTypeListRequestDto
     * @return List<GetPolicyOriginTypeListResultVo>
     */
    protected List<GetPolicyOriginTypeListResultVo> getOriginTypeList(GetPolicyOriginTypeListRequestDto getPolicyOriginTypeListRequestDto) {
        return policyOriginMapper.getOriginTypeList(getPolicyOriginTypeListRequestDto);

    }

    /**
     * @Desc 원산지 등록
     * @param addPolicyOriginRequestDto
     * @return int
     */
    protected int addOrigin(AddPolicyOriginRequestDto addPolicyOriginRequestDto) {
        return policyOriginMapper.addOrigin(addPolicyOriginRequestDto);
    }

    /**
     * @Desc  원산지 수정
     * @param putPolicyOriginRequestDto
     * @return int
     */
    protected int putOrigin(PutPolicyOriginRequestDto putPolicyOriginRequestDto) {
        return policyOriginMapper.putOrigin(putPolicyOriginRequestDto);
    }

    /**
     * @Desc 원산지 삭제
     * @param delPolicyOriginRequestDto
     * @return int
     */
    protected int delOrigin(DelPolicyOriginRequestDto delPolicyOriginRequestDto) {
        return policyOriginMapper.delOrigin(delPolicyOriginRequestDto);
    }

    /**
     * @Desc 원산지 상세 조회
     * @param getPolicyOriginRequestDto
     * @return GetPolicyOriginResultVo
     */
    protected GetPolicyOriginResultVo getOrigin(GetPolicyOriginRequestDto getPolicyOriginRequestDto) {
        return policyOriginMapper.getOrigin(getPolicyOriginRequestDto);
    }

    /**
     * @Desc 원산지 중복체크
     * @param duplicatePolicyOriginCountParamDto
     * @return int
     */
    protected int duplicateOriginCount(DuplicatePolicyOriginCountParamDto duplicatePolicyOriginCountParamDto) {
        return  policyOriginMapper.duplicateOriginCount(duplicatePolicyOriginCountParamDto);
    }

    /**
	 * 원산지 목록 조회 엑셀다운로드
	 *
	 * @param GetPolicyOriginListRequestDto
	 * @return ExcelDownloadDto
	 */
	protected ExcelDownloadDto getOriginListExportExcel(GetPolicyOriginListRequestDto dto) {
		String excelFileName = "원산지목록조회엑셀다운로드";
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */

		Integer[] widthListOfFirstWorksheet = { 120, 220, 320};

		String[] alignListOfFirstWorksheet = { "center", "center", "center"};

		String[] propertyListOfFirstWorksheet = { "originType", "originCode", "originName"};

		String[] firstHeaderListOfFirstWorksheet = { "원산지 구분", "원산지 코드", "원산지명"};

		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder().workSheetName(excelSheetName).propertyList(propertyListOfFirstWorksheet).widthList(widthListOfFirstWorksheet).alignList(alignListOfFirstWorksheet).build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		List<GetPolicyOriginListResultVo> rows = policyOriginMapper.getOriginListExportExcel(dto);

		firstWorkSheetDto.setExcelDataList(rows);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}


}
