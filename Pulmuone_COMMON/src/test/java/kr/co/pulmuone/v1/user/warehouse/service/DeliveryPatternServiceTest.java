package kr.co.pulmuone.v1.user.warehouse.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternDetailResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.DeliveryPatternListVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeliveryPatternServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	DeliveryPatternService deliveryPatternService;

    @InjectMocks
    private DeliveryPatternService mockDeliveryPatternService;

    @Mock
    DeliveryPatternBiz deliveryPatternBiz;

    @Test
    void getDeliveryPatternList_정상() throws Exception {

    	DeliveryPatternRequestDto dto = new DeliveryPatternRequestDto();

    	Page<DeliveryPatternListVo> voList = deliveryPatternService.getDeliveryPatternList(dto);

    	Assertions.assertTrue(voList.getResult().size() > 0);

    }


    @Test
    void getDeliveryPatternList_조회내역없음() throws Exception {

    	DeliveryPatternRequestDto dto = new DeliveryPatternRequestDto();
    	dto.setSearchTitle("1999");
    	Page<DeliveryPatternListVo> voList = deliveryPatternService.getDeliveryPatternList(dto);

    	Assertions.assertTrue(voList.getResult().size() == 0);

    }


    @Test
    void getShippingPattern_정상() throws Exception {

    	DeliveryPatternRequestDto dto = new DeliveryPatternRequestDto();
    	dto.setPsShippingPatternId("-1");

    	DeliveryPatternDetailResponseDto vo = deliveryPatternService.getShippingPattern(dto);

    	Assertions.assertNull(vo.getRows());

    }

    @Test
    void addDeliveryPattern_정상() throws Exception {

    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

    	DeliveryPatternRequestDto dto = new DeliveryPatternRequestDto();
    	dto.setDeliveryPatternName("TEST 등록");
    	dto.setCheckMon("Y");
    	dto.setWarehouseMon("10");
    	dto.setArrivedMon("30");
    	dto.setWeekCode("WEEK_CD.MON");


    	deliveryPatternService.addDeliveryPattern(dto);
    }

    @Test
    void putPattern_정상() throws Exception {

    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

    	DeliveryPatternRequestDto dto = new DeliveryPatternRequestDto();
    	dto.setDeliveryPatternName("TEST 수정");
    	dto.setCheckTue("Y");
    	dto.setWarehouseTue("10");
    	dto.setArrivedTue("30");
    	dto.setWeekCode("WEEK_CD.TUE");

    	deliveryPatternService.putDeliveryPattern(dto);
    }


}
