package kr.co.pulmuone.v1.policy.shoppingsetting.service;

import kr.co.pulmuone.v1.comm.mapper.policy.shopsetting.PolicyShopSettingMapper;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.GetPolicyShopSettingFileUploadRequestDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.GetPolicyShopSettingListRequestDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.PutPolicyShopSettingRequestSaveDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.vo.GetPolicyShopSettingListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 배송정책설정 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 10. 23.                최성현          최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class PolicyShopSettingService {

    @Autowired
    private PolicyShopSettingMapper policyShopSettingMapper ;

    /**
     * @Desc 상점별 세부정책 리스트 조회
     * @param GetPolicyShopSettingListRequestDto
     * @return List<GetPolicyShopSettingListResultVo>
     */
    protected List<GetPolicyShopSettingListResultVo> getPolicyShopSettingList(GetPolicyShopSettingListRequestDto getPolicyShopSettingListRequestDto) {
        return policyShopSettingMapper.getPolicyShopSettingList(getPolicyShopSettingListRequestDto);
    }

    /**
     * @Desc 상점 파비콘 저장
     * @param shippingTemplateRequestDto
     * @return int
     */
    protected int putPolicyShopSettingFavicon(GetPolicyShopSettingFileUploadRequestDto getPolicyShopSettingFileUploadRequestDto) {
      return  policyShopSettingMapper.putPolicyShopSettingFavicon(getPolicyShopSettingFileUploadRequestDto);
    }

    /**
     * @Desc 상점별 세부정책 사항 저장
     * @param shippingTemplateRequestDto
     * @return int
     */
    protected int putPolicyShopSetting(List<PutPolicyShopSettingRequestSaveDto> updateRequestDtoList) {
        return policyShopSettingMapper.putPolicyShopSetting(updateRequestDtoList);
    }

}
