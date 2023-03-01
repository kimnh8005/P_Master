package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.system.basic.dto.GetEnvListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.GetLangListResponseDto;

public interface SystemBasicEnvConfigBiz {

    GetEnvListResponseDto getEnvList();

    GetLangListResponseDto getLangList();
}
