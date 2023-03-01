package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.GetUserGroupListRequestDto;
import kr.co.pulmuone.v1.base.dto.SellersResponseDto;
import kr.co.pulmuone.v1.base.dto.SupplierRequestDto;
import kr.co.pulmuone.v1.base.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.SellersVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.mapper.base.UrCommonMapper;
import kr.co.pulmuone.v1.user.buyer.dto.vo.AddBuyerChangeHististoryParamVo;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("unchecked")
class UrCommonServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    UrCommonService urCommonService;

    @InjectMocks
    private UrCommonService mockUrCommonService;

    @Mock
    UrCommonMapper mockUrCommonMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void 회원_그룹_조회() throws Exception {
        GetUserGroupListRequestDto dto = new GetUserGroupListRequestDto();

        ApiResult<?> apiResult = urCommonService.getUserGroupList(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 공급처_리스트_조회() {
        SupplierRequestDto dto = new SupplierRequestDto();

        ApiResult<?> apiResult = urCommonService.getDropDownSupplierList(dto, false);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 공급처에_해당되는_출고처_조회() {
        WarehouseRequestDto dto = new WarehouseRequestDto();
        dto.setSupplierId((long) 1);
        ApiResult<?> apiResult = urCommonService.getDropDownSupplierByWarehouseList(dto, false);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 출고처_그룹에_해당되는_출고처_조회() {
        WarehouseRequestDto dto = new WarehouseRequestDto();
        dto.setWarehouseGroupCode("WAREHOUSE_GROUP.OWN");
        ApiResult<?> apiResult = urCommonService.getDropDownWarehouseGroupByWarehouseList(dto, false);
        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void addBuyerChangeHististory() throws Exception {
        AddBuyerChangeHististoryParamVo addBuyerChangeHististoryParamVo = AddBuyerChangeHististoryParamVo.builder().build();
        given(mockUrCommonMapper.addBuyerChangeHististory(any())).willReturn(1);
        int n = mockUrCommonService.addBuyerChangeHististory(addBuyerChangeHististoryParamVo);
        assertTrue(n > 0);
    }

    @Test
    void getDropDownWarehouseList() {
    	WarehouseRequestDto warehouseRequestDto = new WarehouseRequestDto();
        ApiResult result = urCommonService.getDropDownWarehouseList(warehouseRequestDto, false);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

	@Test
    void getDropDownSupplierListByWarehouseId_성공() throws Exception{
    	WarehouseRequestDto warehouseRequestDto = new WarehouseRequestDto();
    	warehouseRequestDto.setWarehouseId(new Long(1));

    	SellersResponseDto result = (SellersResponseDto)urCommonService.getDropDownSupplierListByWarehouseId(warehouseRequestDto).getData();
    	List<SellersVo> rows = result.getRows();

    	assertTrue(CollectionUtils.isNotEmpty(rows));
    }

	@Test
	void getDropDownSupplierListByWarehouseId_조회결과없음() throws Exception{
		WarehouseRequestDto warehouseRequestDto = new WarehouseRequestDto();
    	warehouseRequestDto.setWarehouseId(new Long(0));

    	SellersResponseDto result = (SellersResponseDto)urCommonService.getDropDownSupplierListByWarehouseId(warehouseRequestDto).getData();
    	List<SellersVo> rows = result.getRows();


    	assertFalse(CollectionUtils.isNotEmpty(rows));
	}
}