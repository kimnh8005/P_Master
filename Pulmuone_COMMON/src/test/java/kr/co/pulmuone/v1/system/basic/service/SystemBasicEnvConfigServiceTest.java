package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.GetLangListResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class SystemBasicEnvConfigServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SystemBasicEnvConfigService service;

    @Test
    void 환경설정목록_조회() {
        GetEnvListResponseDto dto = service.getEnvList();
        boolean booleanFlag = dto.getRows().size() > 0;
        assertTrue(booleanFlag);
    }

    @Test
    void 다국어정보목록_조회() {
        GetLangListResponseDto dto = service.getLangList();
        boolean booleanFlag = dto.getRows().size() > 0;
        assertTrue(booleanFlag);
    }
}