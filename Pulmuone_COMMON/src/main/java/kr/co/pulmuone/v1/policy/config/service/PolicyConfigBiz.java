package kr.co.pulmuone.v1.policy.config.service;

import kr.co.pulmuone.v1.goods.goods.dto.RegularShippingConfigDto;
import kr.co.pulmuone.v1.policy.config.dto.vo.MetaConfigVo;

import java.util.List;

public interface PolicyConfigBiz {

	RegularShippingConfigDto getRegularShippingConfig() throws Exception;

	String getConfigValue(String psKey) throws Exception;

	List<MetaConfigVo> getMetaConfig() throws Exception;

	/**
	 * 배송권역정책 적용 여부
	 * 정책키의 적용 날짜를 비교하여 배송권역정책(도서산간, 배송불가권역 신규 테이블 로직)을 적용
	 * @return
	 * @throws Exception
	 */
	boolean isApplyDeliveryAreaPolicy() throws Exception;
}
