package kr.co.pulmuone.v1.user.employee.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.employee.dto.ErpEmployeeRequestDto;

public interface ErpEmployeeBiz {

    ApiResult<?> getPulmuoneEmployeeList(ErpEmployeeRequestDto erpEmployeeRequestDto) throws Exception;
    ApiResult<?> getErpEmployeeAndErpOrganization() throws Exception;
}
