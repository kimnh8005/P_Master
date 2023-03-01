package kr.co.pulmuone.bos.promotion.advertising.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.advertising.dto.AddAdvertisingExternalRequestDto;

public interface PromotionAdvertisingBosService {

    ApiResult<?> addAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception;

    void putAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception;

}
