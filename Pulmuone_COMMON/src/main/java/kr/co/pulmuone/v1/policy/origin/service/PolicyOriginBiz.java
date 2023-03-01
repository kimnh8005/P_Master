package kr.co.pulmuone.v1.policy.origin.service;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.AddPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DelPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginTypeListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.PutPolicyOriginRequestDto;

public interface PolicyOriginBiz {

    ApiResult<?> getOriginList(GetPolicyOriginListRequestDto getPolicyOriginListRequestDto);
    ApiResult<?> getOriginTypeList(GetPolicyOriginTypeListRequestDto getPolicyOriginTypeListRequestDto);
    ApiResult<?> addOrigin(AddPolicyOriginRequestDto addPolicyOriginRequestDto) throws Exception;
    ApiResult<?> putOrigin(PutPolicyOriginRequestDto putPolicyOriginRequestDto);
    ApiResult<?> delOrigin(DelPolicyOriginRequestDto delPolicyOriginRequestDto);
    ApiResult<?> getOrigin(GetPolicyOriginRequestDto getPolicyOriginRequestDto);
	ExcelDownloadDto getOriginListExportExcel(GetPolicyOriginListRequestDto dto);
}
