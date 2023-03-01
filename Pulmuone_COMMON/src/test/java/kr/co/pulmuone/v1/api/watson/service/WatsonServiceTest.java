package kr.co.pulmuone.v1.api.watson.service;

import kr.co.pulmuone.v1.api.ecs.dto.vo.CsEcsCodeVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WatsonServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private WatsonService watsonService;

	@Test
	void getClassifierIdCall_성공() {
		String question = "watson classifier_id 호출 API";

		CsEcsCodeVo csEcsCode = watsonService.getClassifierIdCall(question, null);

		assertTrue(!csEcsCode.getHdBcode().isEmpty());
	}

	@Test
	void getClassifierIdCall_실패() {
		String question = "";

		CsEcsCodeVo csEcsCode = watsonService.getClassifierIdCall(question, null);

		assertNull(csEcsCode.getCsEcsCodeId());
	}

}
