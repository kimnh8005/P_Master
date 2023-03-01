package kr.co.pulmuone.v1.policy.excel.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.policy.excel.PolicyExcelTmpltMapper;
import kr.co.pulmuone.v1.policy.excel.dto.PolicyExcelTmpltDto;
import kr.co.pulmuone.v1.policy.excel.dto.vo.PolicyExcelTmpltVo;
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
 *  1.0		20200924		박승현              최초작성
 *  1.1		20201126		박승현		startLine 시작행 SPEC OUT
 *
 * =======================================================================
 * </PRE>
 *
 */

@Service
@RequiredArgsConstructor
public class PolicyExcelTmpltService {

	@Autowired
	private final PolicyExcelTmpltMapper policyExcelTmpltMapper;

    /**
     * 엑셀양식관리 조회
     *
     * @param psExcelTemplateId
     * @return csCategoryId
     */
    protected PolicyExcelTmpltVo getPolicyExcelTmpltInfo(String psExcelTemplateId) {
    	return policyExcelTmpltMapper.getPolicyExcelTmpltInfo(psExcelTemplateId);
    }
    /**
     * 엑셀양식관리 양식목록 조회
     *
     * @param excelTemplateTp
     * @return PolicyExcelTmpltDto
     */
    protected PolicyExcelTmpltDto getPolicyExcelTmpltList(PolicyExcelTmpltVo vo) {
    	PolicyExcelTmpltDto result = new PolicyExcelTmpltDto();
		List<PolicyExcelTmpltVo> rows = policyExcelTmpltMapper.getPolicyExcelTmpltList(vo);

		// 계정별 양식이 Y인 경우
		if("Y".equals(vo.getAccountFormYn())) {
			List<PolicyExcelTmpltVo> accountFormRows = rows.stream().filter(f-> f.getUrUserId().equals(vo.getUserVo().getUserId())).collect(Collectors.toList());
			result.setRows(accountFormRows);
			return result;
		}
		result.setRows(rows);
		return result;
    }
    /**
     * 엑셀양식관리 설정 신규 등록
     *
     * @param PolicyExcelTmpltDto
     * @return int
     */
    protected int addPolicyExcelTmplt(PolicyExcelTmpltDto dto) {
		return policyExcelTmpltMapper.addPolicyExcelTmplt(dto);
    }
    /**
     * 엑셀양식관리 설정 수정
     *
     * @param PolicyExcelTmpltDto
     * @return int
     */
    protected int putPolicyExcelTmplt(PolicyExcelTmpltDto dto) {
		return policyExcelTmpltMapper.putPolicyExcelTmplt(dto);
    }
    /**
     * 엑셀양식관리 설정 삭제
     *
     * @param psExcelTemplateId
     * @return int
     */
    protected int delPolicyExcelTmplt(String psExcelTemplateId) {
    	return policyExcelTmpltMapper.delPolicyExcelTmplt(psExcelTemplateId);
    }
}
