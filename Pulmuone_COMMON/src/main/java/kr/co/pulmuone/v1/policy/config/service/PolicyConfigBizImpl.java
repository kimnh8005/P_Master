package kr.co.pulmuone.v1.policy.config.service;

import kr.co.pulmuone.v1.goods.goods.dto.RegularShippingConfigDto;

import kr.co.pulmuone.v1.policy.config.dto.vo.MetaConfigVo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PolicyConfigBizImpl implements PolicyConfigBiz {

    @Autowired
    private PolicyConfigService policyConfigService;


    /**
     * 정기배송정보
     *
     * @param
     * @return 	RegularShippingConfigDto
     * @throws	Exception
     */
    @Override
    public RegularShippingConfigDto getRegularShippingConfig() throws Exception {
    	RegularShippingConfigDto regularShippingConfigDto = policyConfigService.getRegularShippingConfig();

    	if(regularShippingConfigDto == null) {
    		log.error("RegularShipping Default Value Error");
    		regularShippingConfigDto = new RegularShippingConfigDto();
    		regularShippingConfigDto.setRegularShippingBasicDiscountRate(5);
    		regularShippingConfigDto.setRegularShippingAdditionalDiscountApplicationTimes(3);
    		regularShippingConfigDto.setRegularShippingAdditionalDiscountRate(5);
    	}

    	return regularShippingConfigDto;
    }

    /**
     * 설정 정보 조회
     *
     * @return String
     * @throws Exception Exception
     */
    @Override
    public String getConfigValue(String psKey) throws Exception {
        return policyConfigService.getConfigValue(psKey);
    }

    /**
     * 메타 설정 정보 조회
     *
     * @return String
     * @throws Exception Exception
     */
    @Override
    public List<MetaConfigVo> getMetaConfig() throws Exception {
        return policyConfigService.getMetaConfig();
    }

    /**
     * 배송권역정책 적용 여부
     * 정책키의 적용 날짜를 비교하여 배송권역정책(도서산간, 배송불가권역 신규 테이블 로직)을 적용
     * @return
     * @throws Exception
     */
    @Override
    public boolean isApplyDeliveryAreaPolicy() throws Exception {
        return policyConfigService.isApplyDeliveryAreaPolicy();
    }
}
