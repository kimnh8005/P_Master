package kr.co.pulmuone.v1.system.shopconfig.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.GetSystemShopConfigListRequestDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.SaveSystemShopConfigRequestDto;

public interface SystemShopConfigBiz {

    ApiResult<?> getSystemShopConfigList(GetSystemShopConfigListRequestDto getSystemShopConfigListRequestDto);
    ApiResult<?> saveSystemShopConfig(SaveSystemShopConfigRequestDto saveSystemShopConfigRequestDto);
}
