package kr.co.pulmuone.v1.policy.excel.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.policy.excel.dto.PolicyExcelTmpltDto;
import kr.co.pulmuone.v1.policy.excel.dto.vo.PolicyExcelTmpltVo;

public interface PolicyExcelTmpltBiz {

	ApiResult<?> getPolicyExcelTmpltInfo(String psExcelTemplateId);

	ApiResult<?> getPolicyExcelTmpltList(PolicyExcelTmpltVo vo);

	ApiResult<?> addPolicyExcelTmplt(PolicyExcelTmpltDto vo);

	ApiResult<?> putPolicyExcelTmplt(PolicyExcelTmpltDto dto);

	ApiResult<?> delPolicyExcelTmplt(String psExcelTemplateId);

	ExcelWorkSheetDto getCommonDownloadExcelTmplt(String psExcelTemplateId);
}
