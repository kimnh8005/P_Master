package kr.co.pulmuone.v1.policy.config.service;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.mapper.policy.config.PolicyConfigMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.goods.dto.RegularShippingConfigDto;
import kr.co.pulmuone.v1.policy.config.dto.vo.MetaConfigVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200824   	 천혜현            최초작성
 *  1.1    20201016   	 이원호            getConfigValue, getMetaConfig 추가
 * =======================================================================
 * </PRE>
 */
@RequiredArgsConstructor
@Service
public class PolicyConfigService {

    private final PolicyConfigMapper policyConfigMapper;


	/**
	 * 정기배송정보
	 *
	 * @param
	 * @return RegularShippingConfigDto
	 * @throws Exception
	 */
    protected RegularShippingConfigDto getRegularShippingConfig() throws Exception
	{
		return policyConfigMapper.getRegularShippingConfig();
	}

	/**
	 * 설정정보 조회
	 *
	 * @return String
	 * @throws Exception Exception
	 */
	protected String getConfigValue(String psKey) throws Exception {
		return policyConfigMapper.getConfigValue(psKey);
	}

	/**
	 * 메타 설정 정보 조회
	 *
	 * @return List<MetaConfigVo>
	 * @throws Exception Exception
	 */
	protected List<MetaConfigVo> getMetaConfig() throws Exception {
		return policyConfigMapper.getMetaConfig();
	}

	/**
	 * 배송권역정책 적용 여부
	 * 정책키의 적용 날짜를 비교하여 배송권역정책(도서산간, 배송불가권역 신규 테이블 로직)을 적용
	 * @return
	 * @throws Exception
	 */
	protected boolean isApplyDeliveryAreaPolicy() throws Exception {
		String ApplyDeliveryAreaPolicyDate = getConfigValue(Constants.APPLY_DELIVERY_AREA_POLICY_DATE);

		return DateUtil.getDateTime().compareTo(ApplyDeliveryAreaPolicyDate) >= 0 ? true : false;
	}
}
