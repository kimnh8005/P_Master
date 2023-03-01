package kr.co.pulmuone.v1.comm.mapper.policy.excel;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.policy.excel.dto.PolicyExcelTmpltDto;
import kr.co.pulmuone.v1.policy.excel.dto.vo.PolicyExcelTmpltVo;

@Mapper
public interface PolicyExcelTmpltMapper {

	PolicyExcelTmpltVo getPolicyExcelTmpltInfo(String psExcelTemplateId);
    List<PolicyExcelTmpltVo> getPolicyExcelTmpltList(PolicyExcelTmpltVo vo);
	int addPolicyExcelTmplt(PolicyExcelTmpltDto dto);
	int putPolicyExcelTmplt(PolicyExcelTmpltDto dto);
	int delPolicyExcelTmplt(String psExcelTemplateId);

}
