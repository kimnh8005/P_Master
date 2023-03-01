package kr.co.pulmuone.v1.user.warehouse.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.user.warehouse.dto.SaveWarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseHolidayResultVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WarehouseHolidayServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	WarehouseHolidayService warehouseHolidayService;

    @InjectMocks
    private WarehouseHolidayService mockWarehouseHolidayService;

    @Mock
    WarehouseHolidayBiz warehouseHolidayBiz;

    @Test
    void getWarehouseHolidayList_정상() throws Exception {

    	WarehouseHolidayRequestDto dto = new WarehouseHolidayRequestDto();

    	Page<WarehouseHolidayResultVo> voList = warehouseHolidayService.getWarehouseHolidayList(dto);

    	Assertions.assertTrue(voList.getResult().size() > 0);

    }

    @Test
    void getWarehouseSetList_정상() throws Exception {

    	WarehouseHolidayRequestDto dto = new WarehouseHolidayRequestDto();

    	Page<WarehouseHolidayResultVo> voList = warehouseHolidayService.getWarehouseSetList(dto);

    	Assertions.assertTrue(voList.getResult().size() > 0);

    }


    @Test
    void getWarehouseHolidayDetail_정상() throws Exception {

    	WarehouseHolidayRequestDto dto = new WarehouseHolidayRequestDto();
    	dto.setFromDate("1999-01-01");
    	dto.setToDate("1999-01-01");

    	Page<WarehouseHolidayResultVo> result = warehouseHolidayService.getWarehouseHolidayDetail(dto);
    	int count = result.getResult().size();

    	assertNotEquals(count , 1);
    }


    @Test
    void getConfirmWarehouseHolidayList_정상() throws Exception {

    	WarehouseHolidayRequestDto dto = new WarehouseHolidayRequestDto();
    	dto.setFromDate("1999-01-01");
    	dto.setToDate("1999-01-01");

    	Page<WarehouseHolidayResultVo> result = warehouseHolidayService.getConfirmWarehouseHolidayList(dto);

    	int count = result.getResult().size();

    	assertNotEquals(count , 1);
    }


    @Test
    void addWarehouseHoliday_정상_이용권() throws Exception {
        //given
    	SaveWarehouseHolidayRequestDto dto = new SaveWarehouseHolidayRequestDto();

    	WarehouseHolidayResultVo vo = new WarehouseHolidayResultVo();

    	List<WarehouseHolidayResultVo> insertDataList = new ArrayList<WarehouseHolidayResultVo>();

    	vo.setUrWarehouseId("35");
    	vo.setHoliday("2020-12-01");
    	vo.setDawnYn("N");
    	vo.setGroupYn("N");
		vo.setDawnYnName("Test");

    	insertDataList.add(vo);

    	dto.setInsertSaveDataList(insertDataList);
    	dto.setFromDate("2020-12-01");
    	dto.setToDate("2020-12-01");

    	dto.setUrUserId("1");

        //when, then
        warehouseHolidayService.addWarehouseHoliday(dto);
    }


    @Test
    void putWarehouseHoliday_정상_이용권() throws Exception {
        //given
    	SaveWarehouseHolidayRequestDto dto = new SaveWarehouseHolidayRequestDto();

    	WarehouseHolidayResultVo vo = new WarehouseHolidayResultVo();

    	List<WarehouseHolidayResultVo> insertDataList = new ArrayList<WarehouseHolidayResultVo>();

    	vo.setUrWarehouseId("36");
    	vo.setHoliday("2020-12-11");
    	vo.setDawnYn("N");
    	vo.setGroupYn("N");
    	vo.setDawnYnName("Test");

    	insertDataList.add(vo);

    	dto.setInsertSaveDataList(insertDataList);
    	dto.setFromDate("2020-12-11");
    	dto.setToDate("2020-12-11");
    	dto.setOldFromDate("2020-10-01");
    	dto.setOldToDate("2020-10-01");

    	dto.setUrUserId("1");

        //when, then
        warehouseHolidayService.putWarehouseHoliday(dto);
    }


    @Test
    void getScheduleWarehouseHolidayList_정상() throws Exception {

    	WarehouseHolidayRequestDto dto = new WarehouseHolidayRequestDto();

    	Page<WarehouseHolidayResultVo> voList = warehouseHolidayService.getScheduleWarehouseHolidayList(dto);

    	Assertions.assertTrue(voList.getResult().size() > 0);

    }


    @Test
    void getHolidayWarehouseInfo_정상() throws Exception {

    	WarehouseHolidayRequestDto dto = new WarehouseHolidayRequestDto();
    	dto.setHoliday("1999-01-01");
    	dto.setUrWarehouseId("01");

    	WarehouseResultVo vo = warehouseHolidayService.getHolidayWarehouseInfo(dto);

    	assertNull(vo);

    }

	@Test
	void getDuplicateHldy_정상() throws Exception{
		WarehouseHolidayResultVo vo = new WarehouseHolidayResultVo();

		vo.setUrWarehouseId("182");
		vo.setHoliday("2021-09-01 00:00:00.0");

		int count = warehouseHolidayService.getDuplicateHldy(vo);

		Assertions.assertTrue(count > 0);
	}

}
