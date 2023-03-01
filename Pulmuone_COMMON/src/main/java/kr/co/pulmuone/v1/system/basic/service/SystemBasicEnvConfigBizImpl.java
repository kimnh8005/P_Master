package kr.co.pulmuone.v1.system.basic.service;


import kr.co.pulmuone.v1.system.basic.dto.GetEnvListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.GetLangListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("SystemBasicEnvConfigBizImpl")
@RequiredArgsConstructor
public class SystemBasicEnvConfigBizImpl implements SystemBasicEnvConfigBiz {

    @Autowired
    private final SystemBasicEnvConfigService systemBasicEnvConfigService;

    @Override
    public GetEnvListResponseDto getEnvList() {
        return systemBasicEnvConfigService.getEnvList();
    }

    @Override
    public GetLangListResponseDto getLangList() {
        return systemBasicEnvConfigService.getLangList();
    }

}
