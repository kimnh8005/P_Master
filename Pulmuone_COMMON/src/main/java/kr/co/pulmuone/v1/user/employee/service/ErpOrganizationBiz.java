package kr.co.pulmuone.v1.user.employee.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.employee.dto.ErpOrganizationRequestDto;

public interface ErpOrganizationBiz {

    ApiResult<?> getPulmuoneOrganizationList(ErpOrganizationRequestDto erpOrganizationRequestDto);
    ApiResult<?> erpOrganizationApiInterworking() throws Exception;
}
