package kr.co.pulmuone.v1.user.company.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.company.dto.BusinessInformationRequestDto;
import kr.co.pulmuone.v1.user.company.dto.vo.CommmonHeadQuartersCompanyVo;

public interface UserCompanyBiz {

    CommmonHeadQuartersCompanyVo getHeadquartersCompany();
    ApiResult<?> getBizInfo();
    ApiResult<?> addBizInfo(BusinessInformationRequestDto businessInformationRequestDto) throws Exception;
    ApiResult<?> putBizInfo(BusinessInformationRequestDto businessInformationRequestDto) throws Exception;
}
