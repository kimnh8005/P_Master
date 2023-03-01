package kr.co.pulmuone.v1.api.ezadmin.service;

import kr.co.pulmuone.v1.api.ezadmin.dto.EZAdminRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;

public interface EZAdminBiz {

	ApiResult<?> getEtcInfo(String searchType);

	ApiResult<?> getOrderInfo(EZAdminRequestDto reqDTO);

	ApiResult<?> setAutoCsCyncAnswer(EZAdminRequestDto reqDTO);
}
