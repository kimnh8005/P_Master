package kr.co.pulmuone.v1.api.watson.service;

import kr.co.pulmuone.v1.api.ecs.dto.vo.CsEcsCodeVo;

public interface WatsonBiz {

	CsEcsCodeVo getClassifierIdCall(String question, String goodsName) throws Exception;
}
