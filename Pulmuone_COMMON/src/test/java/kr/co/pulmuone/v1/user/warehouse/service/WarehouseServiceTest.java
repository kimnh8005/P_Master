package kr.co.pulmuone.v1.user.warehouse.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseModifyDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseTemplateRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class WarehouseServiceTest extends CommonServiceTestBaseForJunit5{

 	@Autowired
 	WarehouseService warehouseService;

    @InjectMocks
    private WarehouseService mockWarehouseService;

    @Mock
    WarehouseBiz warehouseBiz;

    @Test
    void getWarehouseList_정상() throws Exception {

    	WarehouseRequestDto dto = new WarehouseRequestDto();

    	Page<WarehouseResultVo> voList = warehouseService.getWarehouseList(dto);

    	assertTrue(voList.getResult().size() > 0);

    }


    @Test
    void getWarehouseList_조회내역없음() throws Exception {

    	WarehouseRequestDto dto = new WarehouseRequestDto();
    	dto.setWarehouseName("1999");
    	Page<WarehouseResultVo> voList = warehouseService.getWarehouseList(dto);

    	Assertions.assertFalse(voList.getResult().size() > 0);

    }

    @Test
    void getWarehouse_정상() throws Exception {

    	WarehouseRequestDto dto = new WarehouseRequestDto();
    	dto.setUrWarehouseId("-1");

    	WarehouseResultVo vo = warehouseService.getWarehouse(dto);

    	Assertions.assertNull(vo);
    }

    @Test
    void getWarehouse_조회내역없음() throws Exception {

    	WarehouseRequestDto dto = new WarehouseRequestDto();
    	dto.setUrWarehouseId("200");

    	WarehouseResultVo vo = warehouseService.getWarehouse(dto);


    	Assertions.assertNull(vo);
    }

    @Test
    void addWarehouse_정상() throws Exception {
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        WarehouseModifyDto dto = new WarehouseModifyDto();
        dto.setWarehouseGroupCode("WAREHOUSE_GROUP.OWN");
        dto.setInputWarehouseName("TEST 1028 등록 테스트");
        dto.setStockOrderYn("N");
        dto.setLimitCount("100");
        dto.setHour("15");
        dto.setMinute("30");
        dto.setDawnDlvryYn("N");
        dto.setStoreYn("N");
        dto.setReceiverZipCode("11256");
        dto.setReceiverAddress1("주소1");
        dto.setReceiverAddress2("주소2");

        warehouseBiz.addWarehouse(dto);
    }

    @Test
    void putWarehouse_정상() throws Exception {
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        WarehouseModifyDto dto = new WarehouseModifyDto();
        dto.setWarehouseGroupCode("WAREHOUSE_GROUP.OWN");
        dto.setInputWarehouseName("TEST 1028 등록 테스트");
        dto.setStockOrderYn("N");
        dto.setLimitCount("100");
        dto.setHour("15");
        dto.setMinute("30");
        dto.setDawnDlvryYn("N");
        dto.setStoreYn("N");
        dto.setReceiverZipCode("11256");
        dto.setReceiverAddress1("주소1");
        dto.setReceiverAddress2("주소2");

        warehouseBiz.putWarehouse(dto);
    }


    @Test
    void getWarehouseTemplateInfo_정상() throws Exception {

    	WarehouseTemplateRequestDto dto = new WarehouseTemplateRequestDto();
    	dto.setUrWarehouseId(Long.parseLong("-1"));
    	WarehouseResultVo vo = warehouseService.getWarehouseTemplateInfo(dto);

    	Assertions.assertNull(vo);

    }

    @Test
    void getShippingTemplateList_정상() throws Exception {

    	WarehouseTemplateRequestDto dto = new WarehouseTemplateRequestDto();
    	dto.setUrWarehouseId(Long.parseLong("-1"));
    	List<ShippingTemplateVo> voList = warehouseService.getShippingTemplateList(dto);

    	Assertions.assertNotEquals(voList.size(), 1);
    }


    @Test
    void getDuplicateCompanyName_정상() throws Exception {

    	WarehouseModifyDto dto = new WarehouseModifyDto();
    	dto.setInputCompanyName("TEST");
    	int dupCount = warehouseService.getDuplicateCompanyName(dto);

    	assertTrue(dupCount > 0);
    }


    @Test
    void getWarehouseCompanyName_조회_정상() throws Exception {
        //given, when
        List<String> result = warehouseService.getWarehouseCompanyName();

        //then
        assertTrue(result.size() > 0);
    }

}
