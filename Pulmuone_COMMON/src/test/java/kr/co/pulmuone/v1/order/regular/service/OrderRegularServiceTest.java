package kr.co.pulmuone.v1.order.regular.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.RegularShippingCycle;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.RegularShippingCycleTerm;
import kr.co.pulmuone.v1.order.regular.dto.RegularShippingCycleDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularShippingCycleTermDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularInfoVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularPaymentKeyVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.RegularPaymentKeyVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.ShippingTemplateGroupByVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;

class OrderRegularServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private OrderRegularService orderRegularService;

	@Test
	void getActiveRegularInfo_성공() throws Exception{
		Long urUserId = 1646817L;

		OrderRegularInfoVo orderRegularInfoVo = orderRegularService.getActiveRegularInfo(urUserId);

		assertNotNull(orderRegularInfoVo);
	}

	@Test
	void getRegularShippingCycleList_성공() throws Exception{
		List<RegularShippingCycleDto> regularShippingCycleList = orderRegularService.getRegularShippingCycleList();

		assertNotNull(regularShippingCycleList);
	}

	@Test
	void getRegularShippingCycleTermList_성공() throws Exception{
		List<RegularShippingCycleTermDto> regularShippingCycleTermList = orderRegularService.getRegularShippingCycleTermList();

		assertNotNull(regularShippingCycleTermList);
	}

	@Test
	void getGoodsListByShippingPolicy_성공() throws Exception{
		ShippingTemplateGroupByVo shippingTemplateData = new ShippingTemplateGroupByVo();
		Long urUserId = 1L;

		List<SpCartVo> goodsListByShippingPolicy = orderRegularService.getGoodsListByShippingPolicy(shippingTemplateData, urUserId);

		assertNotNull(goodsListByShippingPolicy);
	}

	@Test
	void getRegularShippingZone_성공() throws Exception{
		//given
		Long odRegularReqId = 9L;

		//when
		GetSessionShippingResponseDto sessionShippingResponseDto = orderRegularService.getRegularShippingZone(odRegularReqId);

		//then
		assertNotNull(sessionShippingResponseDto);
	}

	@Test
	void addRegularPaymentKey_성공() throws Exception{
		OrderRegularPaymentKeyVo orderRegularPaymentKeyVo = new OrderRegularPaymentKeyVo();
		orderRegularPaymentKeyVo.setUrUserId(1L);
		orderRegularPaymentKeyVo.setBatchKey("TEST_BATCH_KEY");
		orderRegularPaymentKeyVo.setCardName("TEST_CARD_NAME");
		orderRegularPaymentKeyVo.setCardMaskNumber("TEST_CARDMASKNUM");

		int result = orderRegularService.addRegularPaymentKey(orderRegularPaymentKeyVo);

		assertTrue(result > 0);
	}

	@Test
	void addRegularPaymentKey_실패() throws Exception{
		OrderRegularPaymentKeyVo orderRegularPaymentKeyVo = new OrderRegularPaymentKeyVo();
		orderRegularPaymentKeyVo.setUrUserId(1L);

		assertThrows(Exception.class, () -> {
			orderRegularService.addRegularPaymentKey(orderRegularPaymentKeyVo);
		});
	}

	@Test
	void getRegularPaymentKey_성공() throws Exception{
		Long urUserId = 1646939L;

		RegularPaymentKeyVo regularPaymentKeyVo = orderRegularService.getRegularPaymentKey(urUserId);

		assertNotNull(regularPaymentKeyVo);
	}

	@Test
	void getRegularPaymentKey_조회결과없음() throws Exception{
		Long urUserId = 1L;

		RegularPaymentKeyVo regularPaymentKeyVo = orderRegularService.getRegularPaymentKey(urUserId);

		assertNull(regularPaymentKeyVo);
	}

	@Test
	void putNoPaymentRegularPaymentKey_성공() throws Exception{
		Long urUserId = 1646939L;
		String noPaymentReason = "TEST";

		int result = orderRegularService.putNoPaymentRegularPaymentKey(urUserId, noPaymentReason);

		assertTrue(result > 0);
	}

	@Test
	void putNoPaymentRegularPaymentKey_실패() throws Exception{
		Long urUserId = 1L;
		String noPaymentReason = "";

		int result = orderRegularService.putNoPaymentRegularPaymentKey(urUserId, noPaymentReason);

		assertFalse(result > 0);
	}

	@Test
	void getRegularArrivalScheduledDateList_성공() throws Exception{
		LocalDate date = LocalDate.now();
		RegularShippingCycle regularShippingCycle = RegularShippingCycle.WEEK3;
		RegularShippingCycleTerm regularShippingCycleTerm = RegularShippingCycleTerm.MONTH4;
		List<Long> urWarehouseIds = new ArrayList<>();

		List<LocalDate> result = orderRegularService.getRegularArrivalScheduledDateList(date, regularShippingCycle, regularShippingCycleTerm, urWarehouseIds);

		assertTrue(result.size() == 6);
	}
}
