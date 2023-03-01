package kr.co.pulmuone.mall.promotion.shoplive.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveInfoRequestDto;

public interface ShopliveEventMallService {
    ApiResult<?> getShopliveInfo(ShopliveInfoRequestDto dto) throws Exception;
}
