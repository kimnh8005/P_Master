package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.system.basic.dto.*;

import java.util.Map;

public interface SystemBasicSiteConfigBiz {
    GetUrGroupListResponseDto getUrGroup();

    GetStShopListResponseDto getStShop();

    GetPsConfigListResponseDto getPsConfig();

    GetPSKeyTypeListResponseDto getPSKeyType(GetPSKeyTypeRequestDto getPSKeyTypeRequestDto);

    Map<String, Object> siteConfigList();
}
