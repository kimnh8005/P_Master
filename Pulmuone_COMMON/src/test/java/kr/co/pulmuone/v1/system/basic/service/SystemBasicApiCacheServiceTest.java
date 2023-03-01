package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveApiCacheRequestSaveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SystemBasicApiCacheServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private SystemBasicApiCacheService service;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }


    @Test
    void 캐쉬_등록() {
        SaveApiCacheRequestSaveDto dto = SaveApiCacheRequestSaveDto.builder()
                .apiUrl("/test/cache/")
                .casheFilePath("/test/test")
                .casheData("TEST")
                .casheTime("2020-09-28 01:10:00")
                .memo("테스트")
                .useYn("Y")
                .build();
        List<SaveApiCacheRequestSaveDto> list = new ArrayList<>();
        list.add(dto);

        MessageCommEnum returnCode = service.checkApiUrlDuplicate(list);
        assertEquals(BaseEnums.Default.SUCCESS, returnCode);

        int flag = service.addApiCache(list);
        assertEquals(1, flag);

        GetApiCacheListResponseDto result =  service.getApiCacheList(new GetApiCacheRequestDto());
        GetApiCacheListResultVo cacheVo = result.getRows().get(0);
        boolean total = result.getTotal() > 0;
        assertTrue(total);

        list = new ArrayList<>();
        dto.setStApiCacheId(cacheVo.getStApiCacheId());
        list.add(dto);

        flag = service.putApiCache(list);
        assertEquals(1, flag);

        flag = service.delApiCache(list);
        assertEquals(1, flag);
    }


}