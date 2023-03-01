package kr.co.pulmuone.v1.promotion.adminpointsetting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.promotion.adminpointsetting.AdminPointSettingMapper;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingDetailResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.vo.AdminPointSettingVo;

class AdminPointSettingServiceTest extends CommonServiceTestBaseForJunit5 {
	@Autowired
	AdminPointSettingService adminPointSettingService;

    @InjectMocks
    private AdminPointSettingService mockAdminPointSettingService;

    @Mock
    AdminPointSettingMapper mockAdminPointSettingMapper;

    @BeforeEach
    void setUp() {
    	preLogin();
    	mockAdminPointSettingService = new AdminPointSettingService(mockAdminPointSettingMapper);
    }

    @Test
    void getAdminPointSetting_정상() throws Exception {
    	//given
    	AdminPointSettingRequestDto dto = new AdminPointSettingRequestDto();

    	//when
    	Page<AdminPointSettingVo> result = adminPointSettingService.getAdminPointSetting(dto);

        //then
        Assertions.assertTrue(result.getTotal()> 0);
    }


    @Test
    void getAdminPointSettingList_정상() throws Exception {
    	//given
    	AdminPointSettingRequestDto dto = new AdminPointSettingRequestDto();

    	//when
    	Page<AdminPointSettingVo> result = adminPointSettingService.getAdminPointSettingList(dto);

        //then
        Assertions.assertTrue(result.getTotal()> 0);
    }


    @Test
    void getAdminPointSettingList_조회내역없음() throws Exception {
    	//given
    	AdminPointSettingRequestDto dto = new AdminPointSettingRequestDto();
    	dto.setRoleGroup("-1");
    	//when
    	Page<AdminPointSettingVo> result = adminPointSettingService.getAdminPointSettingList(dto);


        //then
    	Assertions.assertNotEquals(result.getResult().size(), 1);
    }


    @Test
    void getAdminPointSettingDetail_정상() throws Exception {
    	//given
    	AdminPointSettingRequestDto dto = new AdminPointSettingRequestDto();
    	dto.setPmPointAdminSettingId("-1");

    	//when
    	AdminPointSettingDetailResponseDto result = adminPointSettingService.getAdminPointSettingDetail(dto);

        //then
        Assertions.assertNotEquals(result.getRows(), 1);
    }

    @Test
    void getAdminPointSettingDetail_조회내역없음() throws Exception {
    	//given
    	AdminPointSettingRequestDto dto = new AdminPointSettingRequestDto();
    	dto.setPmPointAdminSettingId("-1");

    	//when
    	AdminPointSettingDetailResponseDto result = adminPointSettingService.getAdminPointSettingDetail(dto);

        //then
        Assertions.assertEquals(result.getRows(), null);
    }

    @Test
    void addAdminPointSetting_정상() throws Exception {
        //given
    	AdminPointSettingRequestDto dto = new AdminPointSettingRequestDto();

    	dto.setAmount("1000");
    	dto.setAmountIndividual("500");
    	dto.setValidityDay("5");
    	dto.setRoleGroup("112");
    	dto.setDelYn("N");
    	dto.setSettingType("GROUP");
    	dto.setUrUserId("1");
    	//when, then
    	adminPointSettingService.addAdminPointSetting(dto);
    }

    @Test
    void putAdminPointSetting_정상() throws Exception {
        //given
    	AdminPointSettingRequestDto dto = new AdminPointSettingRequestDto();

    	dto.setPmPointAdminSettingId("1");
    	dto.setAmount("1000");
    	dto.setValidityDay("5");
    	dto.setDelYn("N");
    	dto.setSettingType("GROUP");
    	dto.setUrUserId("1");
    	//when, then
    	adminPointSettingService.putAdminPointSetting(dto);
    }

    @Test
    void removeAdminPointSetting_정상() throws Exception {
        //given
    	AdminPointSettingRequestDto dto = new AdminPointSettingRequestDto();

    	dto.setPmPointAdminSettingId("1");
    	dto.setDelYn("Y");
    	dto.setUrUserId("1");
    	//when, then
    	adminPointSettingService.removeAdminPointSetting(dto);
    }

}
