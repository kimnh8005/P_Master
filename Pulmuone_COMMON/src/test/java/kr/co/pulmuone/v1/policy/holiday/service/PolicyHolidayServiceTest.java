package kr.co.pulmuone.v1.policy.holiday.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.policy.holiday.PolicyHolidayMapper;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.holiday.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
public class PolicyHolidayServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	private PolicyHolidayService policyHolidayService;

	@InjectMocks
	private PolicyHolidayService mockPolicyHolidayService;

	@Mock
	PolicyHolidayMapper mockPolicyHolidayMapper;

	@BeforeEach
	void setUp() {
		preLogin();
	}

	@Test
    public void test_휴일목록()  {

        // when
		GetHolidayListResponseDto result = policyHolidayService.getHolidayList();

    	// then
		assertNotNull(result.getRows());
    }



	@Test
    public void test_휴일저장()  {

		SaveHolidayRequestDto addDto = new SaveHolidayRequestDto();
		addDto.setInsertHolidayData("[{\"holidayDate\":\"2021-02-03\",\"dirtyFields\":{\"AreaID\":true}},{\"holidayDate\":\"2021-01-02\"},{\"holidayDate\":\"2021-07-08\"},{\"holidayDate\":\"2021-07-09\"},{\"holidayDate\":\"2021-08-26\"}]");
		addDto.getUserVo().setUserId("2");

		SaveholidayDateListRequestDto aa = new SaveholidayDateListRequestDto();
		List<SaveholidayDateListRequestDto> listDto = new ArrayList<>();
		aa.setHolidayDate("2022-12-08");
		listDto.add(aa);

		addDto.setInsertHolidayRequestDtoList(listDto);
        // when
		int count = policyHolidayService.saveHoliday(addDto);

    	// then
		assertEquals(1,count);
    }

	@Test
    public void test_휴일그룹목록()  {

		GetHolidayGroupListRequestDto dto = new  GetHolidayGroupListRequestDto();

		// when
		GetHolidayGroupListResponseDto result = policyHolidayService.getHolidayGroupList(dto);

    	// then
		assertNotNull(result.getRows());
    }

	@Test
    public void test_휴일그룹상세()  {

		GetHolidayGroupRequestDto dto = new  GetHolidayGroupRequestDto();

		// when
		GetHolidayGroupResponseDto result = policyHolidayService.getHolidayGroup(dto);

    	// then
		assertNotNull(result.getRows());
    }

	@Test
    public void test_휴일그룹저장()  {

		SaveHolidayGroupRequestDto dto = new SaveHolidayGroupRequestDto();
		dto.setHolidayGroupName("Test 20201111");
		dto.setCommonHolidayYn("Y");
		dto.setAddHolidayYn("Y");
		dto.setHolidayDateList("[{\"holidayDateList\":\"2020-11-25\"}]");

		List<SaveholidayGroupDateListRequestDto> insertRequestDtoList = new ArrayList<SaveholidayGroupDateListRequestDto>();
		SaveholidayGroupDateListRequestDto aa = new SaveholidayGroupDateListRequestDto();
		aa.setHolidayDateList("2020-11-26");
		insertRequestDtoList.add(aa);
		dto.setInsertRequestDtoList(insertRequestDtoList);

		try {
			//binding data
			dto.setInsertRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getHolidayDateList(), SaveholidayGroupDateListRequestDto.class));
		} catch (Exception e) {

		}

        // when
		SaveHolidayGroupResponseDto result = policyHolidayService.addHolidayGroup(dto);

    	// then
		assertNotNull(result);
    }

	@Test
    public void test_휴일그룹수정()  {

		SaveHolidayGroupRequestDto dto = new SaveHolidayGroupRequestDto();
		dto.setPsHolidayGroupId("87");
		dto.setHolidayGroupName("Test 20201111");
		dto.setCommonHolidayYn("Y");
		dto.setAddHolidayYn("Y");
		dto.setHolidayDateList("[{\"holidayDateList\":\"2020-11-25\"}]");

		List<SaveholidayGroupDateListRequestDto> insertRequestDtoList = new ArrayList<SaveholidayGroupDateListRequestDto>();
		SaveholidayGroupDateListRequestDto aa = new SaveholidayGroupDateListRequestDto();
		aa.setHolidayDateList("2020-11-26");
		insertRequestDtoList.add(aa);
		dto.setInsertRequestDtoList(insertRequestDtoList);

		try {
			//binding data
			dto.setInsertRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getHolidayDateList(), SaveholidayGroupDateListRequestDto.class));
			dto.setDeleteRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getDeletedDateList(), SaveholidayGroupDateListRequestDto.class));
		} catch (Exception e) {

		}

        // when
		SaveHolidayGroupResponseDto result = policyHolidayService.putHolidayGroup(dto);

    	// then
		assertNotNull(result);
    }

    @Test
    void 휴무일_관리_휴무일_저장() {
		SaveHolidayRequestDto saveHolidayRequestDto = new SaveHolidayRequestDto();
		List<SaveholidayDateListRequestDto> saveholidayDateListRequestDtoList = new ArrayList<>();
		SaveholidayDateListRequestDto saveholidayDateListRequestDto = new SaveholidayDateListRequestDto();
		saveholidayDateListRequestDto.setHolidayDate("20201201");
		saveholidayDateListRequestDtoList.add(saveholidayDateListRequestDto);
		saveHolidayRequestDto.setInsertHolidayRequestDtoList(saveholidayDateListRequestDtoList);

		given(mockPolicyHolidayMapper.addHoliday(any())).willReturn(1);

		//when
		mockPolicyHolidayMapper.deleteHoliday();
		int result = mockPolicyHolidayService.saveHoliday(saveHolidayRequestDto);

		//then
		assertEquals(1, result);
    }
}
