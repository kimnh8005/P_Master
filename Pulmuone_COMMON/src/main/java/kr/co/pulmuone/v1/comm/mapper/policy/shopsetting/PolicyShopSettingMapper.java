package kr.co.pulmuone.v1.comm.mapper.policy.shopsetting;


import kr.co.pulmuone.v1.policy.shoppingsetting.dto.GetPolicyShopSettingFileUploadRequestDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.GetPolicyShopSettingListRequestDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.PutPolicyShopSettingRequestSaveDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.vo.GetPolicyShopSettingListResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PolicyShopSettingMapper {

    /**
     * @Desc 상점별 세부정책 리스트 조회
     * @param GetPolicyShopSettingListRequestDto
     * @return List<GetPolicyShopSettingListResultVo>
     */
    List<GetPolicyShopSettingListResultVo> getPolicyShopSettingList(GetPolicyShopSettingListRequestDto getPolicyShopSettingListRequestDto);
    /**
     * @Desc 상점별 세부정책 사항 저장
     * @param shippingTemplateRequestDto
     * @return int
     */
    int putPolicyShopSetting(List<PutPolicyShopSettingRequestSaveDto> voList);
    /**
     * @Desc 상점 파비콘 저장
     * @param shippingTemplateRequestDto
     * @return int
     */
    int putPolicyShopSettingFavicon(GetPolicyShopSettingFileUploadRequestDto getPolicyShopSettingFileUploadRequestDto);
}
