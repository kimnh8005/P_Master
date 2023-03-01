package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.system.basic.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service("SystemBasicSiteConfigBizImpl")
@RequiredArgsConstructor
public class SystemBasicSiteConfigBizImpl implements SystemBasicSiteConfigBiz {

    @Autowired
    private final SystemBasicSiteConfigService systemBasicSiteConfigService;

    @Override
    public GetUrGroupListResponseDto getUrGroup(){
        return systemBasicSiteConfigService.getUrGroup();
    }

    @Override
    public GetStShopListResponseDto getStShop() {
        return systemBasicSiteConfigService.getStShop();
    }

    @Override
    public GetPsConfigListResponseDto getPsConfig() {
        return systemBasicSiteConfigService.getPsConfig();
    }

    @Override
    public GetPSKeyTypeListResponseDto getPSKeyType(GetPSKeyTypeRequestDto getPSKeyTypeRequestDto){
        return systemBasicSiteConfigService.getPSKeyType(getPSKeyTypeRequestDto);
    }

    @Override
    public Map<String, Object> siteConfigList(){
        return systemBasicSiteConfigService.siteConfigList();
    }
}
