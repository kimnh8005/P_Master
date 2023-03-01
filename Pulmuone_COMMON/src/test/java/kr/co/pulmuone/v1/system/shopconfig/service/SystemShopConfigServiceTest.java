package kr.co.pulmuone.v1.system.shopconfig.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.GetSystemShopConfigListRequestDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.SaveSystemShopConfigRequestDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.SaveSystemShopConfigRequestSaveDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.vo.GetSystemShopConfigListResultVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


class SystemShopConfigServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SystemShopConfigService systemShopConfigService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 상점별세부정책조회_성공() {

        GetSystemShopConfigListRequestDto getSystemShopConfigListRequestDto = new GetSystemShopConfigListRequestDto();

        Page<GetSystemShopConfigListResultVo> getSystemShopConfigListResultVo = systemShopConfigService.getSystemShopConfigList(getSystemShopConfigListRequestDto);

        Assertions.assertTrue(CollectionUtils.isNotEmpty(getSystemShopConfigListResultVo));
    }

    @Test
    void 상점별세부정책조회_실패() {

        GetSystemShopConfigListRequestDto getSystemShopConfigListRequestDto = new GetSystemShopConfigListRequestDto();
        getSystemShopConfigListRequestDto.setPsGroupType("1");
        Page<GetSystemShopConfigListResultVo> getSystemShopConfigListResultVo = systemShopConfigService.getSystemShopConfigList(getSystemShopConfigListRequestDto);

        Assertions.assertTrue(CollectionUtils.isEmpty(getSystemShopConfigListResultVo));
    }

    @Test
    void 상점별세부정책저장_성공() {
        List<SaveSystemShopConfigRequestSaveDto> insertRequestDtoList = new ArrayList<>();

        SaveSystemShopConfigRequestSaveDto saveSystemShopConfigRequestSaveDto = new SaveSystemShopConfigRequestSaveDto();
        saveSystemShopConfigRequestSaveDto.setStShopId("1");
        saveSystemShopConfigRequestSaveDto.setPsGroupType("80");
        saveSystemShopConfigRequestSaveDto.setPsKey("REGULAR_SHIPPING_BASIC_DISCOUNT_RATE");
        saveSystemShopConfigRequestSaveDto.setPsName("HEllow");
        saveSystemShopConfigRequestSaveDto.setUseYn("Y");
        saveSystemShopConfigRequestSaveDto.setComment("1");
        saveSystemShopConfigRequestSaveDto.setPsValue("1");

        insertRequestDtoList.add(saveSystemShopConfigRequestSaveDto);

        int count = systemShopConfigService.addShopConfig(insertRequestDtoList);

        Assertions.assertTrue(count == 1);


    }

    @Test
    void 상점별세부정책저장_실패() {

        SaveSystemShopConfigRequestSaveDto saveSystemShopConfigRequestSaveDto = new SaveSystemShopConfigRequestSaveDto();

        SaveSystemShopConfigRequestDto saveSystemShopConfigRequestDto = new SaveSystemShopConfigRequestDto();

        saveSystemShopConfigRequestSaveDto.setStShopId("");
        saveSystemShopConfigRequestSaveDto.setPsGroupType("");
        saveSystemShopConfigRequestSaveDto.setPsKey("");
        saveSystemShopConfigRequestSaveDto.setPsName("");
        saveSystemShopConfigRequestSaveDto.setUseYn("");
        saveSystemShopConfigRequestSaveDto.setComment("");

        List<SaveSystemShopConfigRequestSaveDto> insertRequestDtoList = saveSystemShopConfigRequestDto.getInsertRequestDtoList();

        insertRequestDtoList.add(saveSystemShopConfigRequestSaveDto);

        assertThrows(DataIntegrityViolationException.class, () -> {
            systemShopConfigService.addShopConfig(insertRequestDtoList);
        });

    }

    @Test
    void 상점별세부정책수정_성공() {
        SaveSystemShopConfigRequestSaveDto saveSystemShopConfigRequestSaveDto = new SaveSystemShopConfigRequestSaveDto();

        SaveSystemShopConfigRequestDto saveSystemShopConfigRequestDto = new SaveSystemShopConfigRequestDto();

        saveSystemShopConfigRequestSaveDto.setStShopId("1");
        saveSystemShopConfigRequestSaveDto.setPsGroupType("229");
        saveSystemShopConfigRequestSaveDto.setPsKey("INVTRY_ITEM_CL");
        saveSystemShopConfigRequestSaveDto.setPsName("기초설정 > 품목분류설정");
        saveSystemShopConfigRequestSaveDto.setUseYn("Y");
        saveSystemShopConfigRequestSaveDto.setComment("1");
        saveSystemShopConfigRequestSaveDto.setPsValue("1");
        saveSystemShopConfigRequestSaveDto.setPsConfigId("30");

        List<SaveSystemShopConfigRequestSaveDto> updateRequestDtoList = saveSystemShopConfigRequestDto.getInsertRequestDtoList();

        updateRequestDtoList.add(saveSystemShopConfigRequestSaveDto);

        int count = systemShopConfigService.putShopConfig(updateRequestDtoList);

        Assertions.assertTrue(count > 0);

    }

    @Test
    void 상점별세부정책수정_실패() {
        SaveSystemShopConfigRequestSaveDto saveSystemShopConfigRequestSaveDto = new SaveSystemShopConfigRequestSaveDto();

        SaveSystemShopConfigRequestDto saveSystemShopConfigRequestDto = new SaveSystemShopConfigRequestDto();

        saveSystemShopConfigRequestSaveDto.setStShopId("1");
        saveSystemShopConfigRequestSaveDto.setPsGroupType("");
        saveSystemShopConfigRequestSaveDto.setPsKey("");
        saveSystemShopConfigRequestSaveDto.setPsName("");
        saveSystemShopConfigRequestSaveDto.setUseYn("");
        saveSystemShopConfigRequestSaveDto.setComment("");
        saveSystemShopConfigRequestSaveDto.setPsValue("");

        List<SaveSystemShopConfigRequestSaveDto> updateRequestDtoList = saveSystemShopConfigRequestDto.getInsertRequestDtoList();

        updateRequestDtoList.add(saveSystemShopConfigRequestSaveDto);

        int count = systemShopConfigService.putShopConfig(updateRequestDtoList);

        Assertions.assertTrue(count == 0);

    }

    @Test
    void 상점별세부정책삭제_성공() {
        SaveSystemShopConfigRequestSaveDto saveSystemShopConfigRequestSaveDto = new SaveSystemShopConfigRequestSaveDto();

        SaveSystemShopConfigRequestDto saveSystemShopConfigRequestDto = new SaveSystemShopConfigRequestDto();

        saveSystemShopConfigRequestSaveDto.setPsConfigId("1061");
        saveSystemShopConfigRequestSaveDto.setStShopId("1");
        saveSystemShopConfigRequestSaveDto.setPsGroupType("7.230");
        saveSystemShopConfigRequestSaveDto.setPsKey("T_E");
        saveSystemShopConfigRequestSaveDto.setPsName("테스트");
        saveSystemShopConfigRequestSaveDto.setUseYn("Y");
        saveSystemShopConfigRequestSaveDto.setComment("");
        saveSystemShopConfigRequestSaveDto.setPsValue("222");

        List<SaveSystemShopConfigRequestSaveDto> delRequestDtoList = saveSystemShopConfigRequestDto.getInsertRequestDtoList();

        delRequestDtoList.add(saveSystemShopConfigRequestSaveDto);

        int count = systemShopConfigService.delShopConfig(delRequestDtoList);

        Assertions.assertTrue(count > 0);

    }

    @Test
    void 상점별세부정책삭제_실패() {
        SaveSystemShopConfigRequestSaveDto saveSystemShopConfigRequestSaveDto = new SaveSystemShopConfigRequestSaveDto();

        SaveSystemShopConfigRequestDto saveSystemShopConfigRequestDto = new SaveSystemShopConfigRequestDto();

        saveSystemShopConfigRequestSaveDto.setStShopId("1");
        saveSystemShopConfigRequestSaveDto.setPsGroupType("");
        saveSystemShopConfigRequestSaveDto.setPsKey("");
        saveSystemShopConfigRequestSaveDto.setPsName("");
        saveSystemShopConfigRequestSaveDto.setUseYn("");
        saveSystemShopConfigRequestSaveDto.setComment("");
        saveSystemShopConfigRequestSaveDto.setPsValue("");

        List<SaveSystemShopConfigRequestSaveDto> delRequestDtoList = saveSystemShopConfigRequestDto.getInsertRequestDtoList();

        delRequestDtoList.add(saveSystemShopConfigRequestSaveDto);

        int count = systemShopConfigService.delShopConfig(delRequestDtoList);

        Assertions.assertFalse(count > 0);

    }

}