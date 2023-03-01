package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.system.basic.dto.*;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetPsConfigListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetStShopListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetUrGroupResultResultVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SystemBasicSiteConfigServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SystemBasicSiteConfigService service;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }


    @Test
    void 회원그룹정보_목록() {
        GetUrGroupListResponseDto dto = service.getUrGroup();
        boolean booleanFlag = dto.getRows().size() > 0;

        assertTrue(booleanFlag);
    }

    @Test
    void 상점정보목록_조회() {
        GetStShopListResponseDto dto = service.getStShop();
        boolean booleanFlag = dto.getRows().size() > 0;

        assertTrue(booleanFlag);
    }

    @Test
    void 상점별_정책정보_목록_조회() {
        GetPsConfigListResponseDto dto = service.getPsConfig();
        boolean booleanFlag = dto.getRows().size() > 0;

        assertTrue(booleanFlag);

    }

    @Test
    void 키_조회() {
        GetPSKeyTypeRequestDto getPSKeyTypeRequestDto = new GetPSKeyTypeRequestDto();
        getPSKeyTypeRequestDto.setPsKey("ST_DESCRIPTION");

        GetPSKeyTypeListResponseDto dto = service.getPSKeyType(getPSKeyTypeRequestDto);
        boolean booleanFlag = dto.getRows().size() > 0;

        assertTrue(booleanFlag);
    }

    @Test
    void 사이트_환경설정정보_조회() {
        Map<String, Object> map = service.siteConfigList();

        List<GetUrGroupResultResultVo> list1 = (List<GetUrGroupResultResultVo>)map.get("UR_GROUP");
        boolean booleanFlag = list1.size() > 0;
        assertTrue(booleanFlag);

        List<GetStShopListResultVo> list2 = (List<GetStShopListResultVo>)map.get("ST_SHOP");
        booleanFlag = list2.size() > 0;
        assertTrue(booleanFlag);

        List<GetPsConfigListResultVo> list3 = (List<GetPsConfigListResultVo>)map.get("PS_CONFIG");
        booleanFlag = list3.size() > 0;
        assertTrue(booleanFlag);




    }
}