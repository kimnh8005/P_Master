package kr.co.pulmuone.v1.user.employee.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.employee.dto.ErpRegalRequestDto;

public interface ErpRegalBiz {

    ApiResult<?> getPulmuoneRegalList(ErpRegalRequestDto erpRegalRequestDto);
    ApiResult<?> getPulmuoneRegalListWithoutPaging();
    ApiResult<?> erpRegalApiInterworking() throws Exception;
}
