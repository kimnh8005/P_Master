package kr.co.pulmuone.mall.customer.stndpnt.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointMallRequestDto;

public interface StandingPointMallService {

    ApiResult<?> addStandingPointQna(StandingPointMallRequestDto dto) throws Exception;

}
